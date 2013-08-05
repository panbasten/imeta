package com.panet.imeta.ui.meta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.JobGraph;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.TransGraph;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

public class ImetaUIUtils {
	/**
	 * 通过路径ID得到全路径名称
	 * 
	 * @param rep
	 * @param directoryId
	 * @param name
	 * @return
	 * @throws KettleException
	 */
	public static String getFullName(Repository rep, long directoryId,
			String name) throws KettleException {
		RepositoryDirectory directory = new RepositoryDirectory(rep);
		directory = directory.findDirectory(directoryId);
		return directory.getPath() + ((directory.isRoot()) ? "" : "/") + name;
	}

	/**
	 * 通过包含路径的名字，返回路径和名称
	 * 
	 * @param rep
	 * @param pathname
	 * @return
	 * @throws KettleException
	 */
	public static Object[] getDirectoryAndName(Repository rep, String pathname)
			throws KettleException {
		pathname = Const.NVL(pathname, "");
		RepositoryDirectory directory = new RepositoryDirectory(rep);
		if (StringUtils.isNotEmpty(pathname)) {
			directory = directory.findDirectory(pathname);
		}
		pathname = pathname.replaceFirst(directory.getPath(), "");
		if (pathname.startsWith("/")) {
			pathname = pathname.substring(1);
		}

		return new Object[] { directory, pathname };
	}

	public static RepositoryDirectory getDirectoryByPath(Repository rep,
			String path) throws KettleException {
		path = Const.NVL(path, "");
		RepositoryDirectory directory = new RepositoryDirectory(rep);
		if (StringUtils.isNotEmpty(path)) {
			directory = directory.findDirectory(path);
		}

		return directory;
	}

	/**
	 * 绘制对象
	 * 
	 * @param directoryId
	 * @param roName
	 * @param roType
	 * @param exeditable
	 * @param rep
	 * @param user
	 * @param trans
	 * @param jobs
	 * @return
	 * @throws ImetaException
	 */
	public static String drawRepObjectByNameAndType(long directoryId,
			String roName, String roType, boolean exeditable, Repository rep,
			UserInfo user, ImetaTransformationDelegate trans,
			ImetaJobDelegate jobs) throws ImetaException {
		try {
			JSONObject ro = null;
			boolean editable = false;

			// 如果是转换
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(rep,
						directoryId, roName);
				// 如果未编辑，取得转换，判断是否是草稿状态，如果不是生产状态可以编辑
				if (transMeta == null) {
					transMeta = trans.getTransMetaByName(rep, directoryId,
							roName);
					// 如果用户为操作员或者编辑员但不是该转换的创建者
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())
							|| (UserInfo.STRING_USER_TYPE_EDITOR
									.equalsIgnoreCase(user.getAccountType()) && !transMeta
									.getCreatedUser().equals(user.getLogin()))) {
						editable = false;
					}
					// 如果是不是生产，可以修改
					else if (transMeta.getTransstatus() != TransMeta.TRANSSTATUS_PRODUCTION
							&& exeditable) {
						editable = true;
						transMeta.setModifiedUser(user.getLogin());
						trans.putEditTransMeta(rep, transMeta);
					} else {
						editable = false;
					}
				}
				// 如果正在编辑，判断是否为当前用户正在编辑，如果是可以编辑
				else {
					// 如果用户为操作员或者编辑员但不是该转换的创建者
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())
							|| (UserInfo.STRING_USER_TYPE_EDITOR
									.equalsIgnoreCase(user.getAccountType()) && !transMeta
									.getCreatedUser().equals(user.getLogin()))) {
						editable = false;
					}
					else if (transMeta.getModifiedUser().equals(user.getLogin())) {
						editable = true;
					}
				}

				TransGraph transGraph = new TransGraph(transMeta);
				transGraph.setEditable(editable);
				ro = transGraph.open();
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, directoryId,
						roName);
				// 如果未编辑，取得转换，判断是否是草稿状态，如果不是生产状态可以编辑
				if (jobMeta == null) {
					jobMeta = jobs.getJobMetaByName(rep, directoryId, roName);
					// 如果用户为操作员或者编辑员但不是该转换的创建者
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())
							|| (UserInfo.STRING_USER_TYPE_EDITOR
									.equalsIgnoreCase(user.getAccountType()) && !jobMeta
									.getCreatedUser().equals(user.getLogin()))) {
						editable = false;
					}
					else if (jobMeta.getJobstatus() != JobMeta.JOBSTATUS_PRODUCTION) {
						editable = true;
						jobMeta.setModifiedUser(user.getLogin());
						jobs.putEditJobMeta(rep, jobMeta);
					} else {
						editable = false;
					}
				}
				// 如果正在编辑，判断是否为当前用户正在编辑，如果是可以编辑
				else {
					// 如果用户为操作员或者编辑员但不是该转换的创建者
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())
							|| (UserInfo.STRING_USER_TYPE_EDITOR
									.equalsIgnoreCase(user.getAccountType()) && !jobMeta
									.getCreatedUser().equals(user.getLogin()))) {
						editable = false;
					}
					else if (jobMeta.getModifiedUser().equals(user.getLogin())) {
						editable = true;
					}
				}
				JobGraph jobGraph = new JobGraph(jobMeta);
				jobGraph.setEditable(editable);
				ro = jobGraph.open();
			}

			return ro.toString();
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 得到验证后的转换名
	 * 
	 * @param rep
	 * @param newTransName
	 * @return
	 */
	public static String getCheckedTransMetaName(Repository rep,
			long directoryId, String newTransName,
			ImetaTransformationDelegate trans) {
		if (checkTransMetaByName(rep, directoryId, newTransName, trans) != null) {
			int nr = 2;
			while (checkTransMetaByName(rep, directoryId, newTransName + " "
					+ nr, trans) != null) {
				nr++;
			}
			return newTransName + " " + nr;
		}
		return newTransName;
	}

	/**
	 * 得到验证后的认为名
	 * 
	 * @param rep
	 * @param newJobName
	 * @return
	 */
	public static String getCheckedJobMetaName(Repository rep,
			long directoryId, String newJobName, ImetaJobDelegate jobs) {
		if (checkJobMetaByName(rep, directoryId, newJobName, jobs) != null) {
			int nr = 2;
			while (checkJobMetaByName(rep, directoryId, newJobName + " " + nr,
					jobs) != null) {
				nr++;
			}
			return newJobName + " " + nr;
		}
		return newJobName;
	}

	/**
	 * 得到一个转换通过name，如果是转换不存在则返回null
	 * 
	 * @param rep
	 * @param name
	 * @return
	 */
	public static TransMeta checkTransMetaByName(Repository rep,
			long directoryId, String name, ImetaTransformationDelegate trans) {
		try {
			TransMeta transMeta = trans.getEditTransMetaByName(rep,
					directoryId, name);
			if (transMeta != null) {
				return transMeta;
			}

			return trans.getTransMetaByName(rep, directoryId, name);
		} catch (Exception e) {
			return null;
		}
	}

	public static JobMeta checkJobMetaByName(Repository rep, long directoryId,
			String name, ImetaJobDelegate jobs) {
		try {
			JobMeta jobMeta = jobs.getEditJobMetaByName(rep, directoryId, name);
			if (jobMeta != null) {
				return jobMeta;
			}

			return jobs.getJobMetaByName(rep, directoryId, name);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean matchFileName(String str) {
		String regex = "[*/?&:;><|\\\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean b = m.find();
		return b;
	}

	public static String replaceFileName(String str) {
		String regex = "[*/?&:;><|\\\\]";
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll(regex, "");
		}
		return "";
	}

	public static void main(String[] args) {
		String aaa = "aa \\ bb";
		System.out.println(replaceFileName(aaa));
	}
}
