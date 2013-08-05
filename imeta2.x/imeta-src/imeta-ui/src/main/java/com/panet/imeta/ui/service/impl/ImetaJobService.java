package com.panet.imeta.ui.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.panet.iform.exception.ImetaFormException;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.logging.LogWriterProxy;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobDialogInterface;
import com.panet.imeta.job.JobEntryLoader;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.JobPlugin;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.ui.dialog.JobSettingDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaJobDelegate;

/**
 * 元数据作业服务层接口实现类
 * 
 * @author Peter Pan
 * 
 */
@Service("imeta.ui.imetaJobService")
public class ImetaJobService implements ImetaJobDelegate {

	public static final String AUTO_RUN_TYPE_AUTO = "auto";

	public static final String AUTO_RUN_TYPE_DISAUTO = "disauto";

	public static final String AUTO_RUN_ATTR_PARAMETER = "parameter";

	public static final String AUTO_RUN_ATTR_VARIABLE = "variable";

	private static boolean autoRun = false;

	/**
	 * 任务种类
	 */
	private String[] categories = null;

	/**
	 * 所有任务实体
	 */
	private JobPlugin[] jobEntries = null;

	private JSONArray jobEntryTab = null;

	private static Map<String, JobMeta> jobMetaMap = new HashMap<String, JobMeta>();

	private static Map<String, JobMeta> editJobMetaMap = new HashMap<String, JobMeta>();

	private static Map<String, Job> activeJobsMap = new HashMap<String, Job>();

	private static Map<String, String> autoRunJobs = new HashMap<String, String>();

	public ImetaJobService() {
		super();
	}

	/**
	 * 初始化自动执行JOBS
	 * 
	 * @param rep
	 * @throws ImetaException
	 */
	public void initAutoRunJobs(Repository rep, LogWriter log)
			throws ImetaException {
		try {
			if (!autoRun) {
				Collection<RowMetaAndData> jobs = rep.getAllAutoStartJobs();
				if (jobs != null) {
					for (RowMetaAndData m : jobs) {
						Long jobId = m
								.getInteger(Repository.FIELD_AUTO_START_ID_AUTO_START_JOB);
						String startType = m.getString(
								Repository.FIELD_AUTO_START_START_TYPE, "");
						Long logLevel = m
								.getInteger(Repository.FIELD_AUTO_START_ARG_LOG_LEVEL);
						autoRunJobs.put(rep.getName() + "." + jobId, startType);

						if (AUTO_RUN_TYPE_AUTO.equals(startType)) {
							startJob(rep, jobId, (logLevel != null) ? logLevel
									.intValue() : 0, log);
						}
					}
				}
			}
		} catch (KettleException e) {
			throw new ImetaException(e);
		}
	}

	private void startJob(Repository rep, long id_job, int logLevel,
			LogWriter log) {
		try {
			RowMetaAndData jobMetaDate = rep.getJob(id_job);

			long dirId = jobMetaDate
					.getInteger(Repository.FIELD_JOB_ID_DIRECTORY);
			String jobName = jobMetaDate.getString(Repository.FIELD_JOB_NAME,
					"");
			Long jobId = jobMetaDate.getInteger(Repository.FIELD_JOB_ID_JOB);

			JobMeta jobMeta = newJobMetaByName(rep, dirId, jobName);

			// 参数
			Collection<RowMetaAndData> attrs = rep.getStartJobAttribute(jobId);
			if (attrs != null) {
				for (RowMetaAndData md : attrs) {
					String attrType = md.getString(
							Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_TYPE,
							"");
					String attrKey = md
							.getString(
									Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_KEY,
									"");
					String attrValue = md.getString(
							Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_VALUE,
							"");

					if (AUTO_RUN_ATTR_PARAMETER.equals(attrType)) {
						jobMeta.setParameterValue(attrKey, Const.NVL(attrValue,
								""));
					}
				}
			}

			jobMeta.activateParameters();

			Job job = getActiveJobByName(rep, dirId, jobName);

			if (job == null) {
				job = new Job(new LogWriterProxy(log, logLevel), rep, jobMeta);
				putActiveJobByName(rep, dirId, jobName, job);
				job.getJobMeta().setArguments(jobMeta.getArguments());
				job.shareVariablesWith(jobMeta);
				job.start();
			}
		} catch (Exception e) {
			if (log != null) {
				log.logError("AutoRunJob", "自动启动Job出现错误，Job ID : " + id_job);
			}
		}
	}

	public void clearAutoRunJobs() {
		autoRunJobs.clear();
		autoRun = false;
	}

	public void setAutoRun() {
		autoRun = true;
	}

	public boolean isAutoRun() {
		return autoRun;
	}

	/**
	 * 得到活动的Job
	 * 
	 * @param rep
	 * @param jobname
	 * @return
	 * @throws ImetaException
	 */
	public Job getActiveJobByName(Repository rep, long directoryId,
			String jobname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			Job job = activeJobsMap.get(lookupId);
			if (job != null) {
				if (!job.isActive() || !job.isAlive()) {
					activeJobsMap.remove(lookupId);
					return null;
				}
			}
			return job;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 通过用户登录名清除正在编辑的任务
	 * 
	 * @param rep
	 * @param loginName
	 */
	public void clearEditJobMetaByLoginName(Repository rep, String loginName) {
		synchronized (editJobMetaMap) {
			String lookupId = null;
			for (Iterator<String> iter = editJobMetaMap.keySet().iterator(); iter
					.hasNext();) {
				lookupId = iter.next();
				if (lookupId.startsWith(rep.getName() + ".")) {
					JobMeta jobMeta = editJobMetaMap.get(lookupId);
					if (jobMeta != null
							&& loginName.equals(jobMeta.getModifiedUser())) {
						editJobMetaMap.remove(lookupId);
					}
				}
			}
		}
	}

	/**
	 * 清除Job
	 * 
	 * @param rep
	 * @param jobname
	 */
	public void clearActiveJobByName(Repository rep, long directoryId,
			String jobname) {
		synchronized (activeJobsMap) {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			activeJobsMap.remove(lookupId);
		}
	}

	/**
	 * 保存Job
	 * 
	 * @param rep
	 * @param jobname
	 * @param job
	 */
	public void putActiveJobByName(Repository rep, long directoryId,
			String jobname, Job job) {
		synchronized (activeJobsMap) {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			activeJobsMap.put(lookupId, job);
		}
	}

	/**
	 * 清除不活动的Job
	 */
	public void clearActiveJob() {
		synchronized (activeJobsMap) {
			if (activeJobsMap != null && activeJobsMap.size() > 0) {
				String key;
				Job job;
				for (Iterator<String> keys = activeJobsMap.keySet().iterator(); keys
						.hasNext();) {
					key = keys.next();
					job = activeJobsMap.get(key);
					if (!job.isActive() || !job.isAlive()) {
						activeJobsMap.remove(key);
					}
				}
			}
		}
	}

	/**
	 * 载入目录中的Job
	 * 
	 * @param rep
	 * @param jobname
	 * @return
	 * @throws ImetaException
	 */
	public JobMeta getJobMetaByName(Repository rep, long directoryId,
			String jobname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			JobMeta jobMeta = jobMetaMap.get(lookupId);
			if (jobMeta == null) {
				jobMeta = newJobMetaByName(rep, directoryId, jobname);
				jobMetaMap.put(lookupId, jobMeta);
			}
			if (jobMeta.getRep() == null) {
				jobMeta.setRep(rep);
			}
			return jobMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 通过名称清除一个Job
	 * 
	 * @param rep
	 * @param jobname
	 */
	public void clearJobMetaByName(Repository rep, long directoryId,
			String jobname) {
		synchronized (jobMetaMap) {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			jobMetaMap.remove(lookupId);
		}
	}

	public void clearEditJobMetaByName(Repository rep, long directoryId,
			String jobname) {
		synchronized (editJobMetaMap) {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			editJobMetaMap.remove(lookupId);
		}
	}

	/**
	 * 得到Job设置的JSON表达式
	 * 
	 * @param rep
	 * @param jobname
	 * @param winId
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getSettingContent(Repository rep, long directoryId,
			String jobname, String winId) throws ImetaFormException {

		try {
			clearJobMetaByName(rep, directoryId, jobname);
			JobMeta jobMeta = getJobMetaByName(rep, directoryId, jobname);
			JobDialogInterface jobDialog = new JobSettingDialog(jobMeta, winId);
			return jobDialog.open();
		} catch (ImetaException ex) {
			throw new ImetaFormException("转换错误", ex);
		}

	}

	/**
	 * 加入任务的实体
	 * 
	 * @throws ImetaException
	 */
	public JSONArray getJobEntryTab() throws ImetaException {
		if (this.jobEntryTab != null) {
			return this.jobEntryTab;
		}

		if (this.categories == null) {
			JobEntryLoader jel = JobEntryLoader.getInstance();
			this.categories = jel.getCategories(JobPlugin.TYPE_ALL);
			this.jobEntries = jel.getJobEntriesWithType(JobPlugin.TYPE_ALL);
		}

		this.jobEntryTab = new JSONArray();
		for (String cs : this.categories) {
			JSONObject tabTitle = new JSONObject();
			tabTitle.put("createTag", "<a class='toolbar-job'></a>");
			tabTitle.put("html", cs);
			this.jobEntryTab.add(tabTitle);

			JSONObject tabBody = new JSONObject();
			tabBody.put("createTag",
					"<div class='toolbar-job toolbar-content'></div>");

			JSONArray tabBody_items = new JSONArray();

			for (JobPlugin jp : this.jobEntries) {
				if (jp != null && jp.getCategory().equalsIgnoreCase(cs)) {
					JSONObject item = new JSONObject();
					item.put("createTag", "<div></div>");

					JSONObject item_frame = new JSONObject();
					item_frame.put("id", jp.getID());
					item_frame.put("img", jp.getIconFilename());
					item_frame.put("name", jp.getDescription());
					item_frame.put("title", jp.getTooltip());
					item_frame.put("type",
							RepositoryObject.STRING_OBJECT_TYPE_JOB);

					item.put("frame", item_frame);

					tabBody_items.add(item);
				}
			}

			tabBody.put("items", tabBody_items);
			this.jobEntryTab.add(tabBody);
		}
		return this.jobEntryTab;
	}

	/**
	 * 增加数据库
	 * 
	 * @param databaseMeta
	 */
	public void addOrReplaceDatabases(DatabaseMeta databaseMeta) {
		synchronized (editJobMetaMap) {
			for (JobMeta job : editJobMetaMap.values()) {
				job.addDatabase(databaseMeta);
			}
		}

		synchronized (jobMetaMap) {
			jobMetaMap.clear();
		}
	}

	/**
	 * 移除数据库
	 * 
	 * @param i
	 */
	public void removeDatabases(long i) {
		synchronized (editJobMetaMap) {
			for (JobMeta job : editJobMetaMap.values()) {
				job.removeDatabase(i);
			}
		}

		synchronized (jobMetaMap) {
			jobMetaMap.clear();
		}
	}

	public JobMeta getEditJobMetaByName(Repository rep, long directoryId,
			String jobname) throws ImetaException {
		try {
			String lookupId = rep.getName() + "." + directoryId + "." + jobname;
			JobMeta jobMeta = editJobMetaMap.get(lookupId);
			if (jobMeta == null) {
				return null;
			}
			if (jobMeta.getRep() != null) {
				jobMeta.setRep(rep);
			}
			return jobMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	public JobMeta newJobMetaByName(Repository rep, long directoryId,
			String jobname) throws KettleException {
		RepositoryDirectory directory = new RepositoryDirectory(rep);
		JobMeta jobMeta = new JobMeta(rep.log, rep, jobname, directory
				.findDirectory(directoryId));
		return jobMeta;
	}

	public void putEditJobMeta(Repository rep, JobMeta jobMeta)
			throws ImetaException {
		try {
			synchronized (editJobMetaMap) {
				RepositoryDirectory directory = jobMeta.getDirectory();
				String lookupId = rep.getName() + "." + directory.getID() + "."
						+ jobMeta.getName();
				try {
					JobMeta editJobMeta = newJobMetaByName(rep, directory
							.getID(), jobMeta.getName());
					editJobMetaMap.put(lookupId, editJobMeta);
				} catch (KettleException ke) {
					editJobMetaMap.put(lookupId, jobMeta);
				}
			}
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	public Map<String, Job> getActiveJobsMap() {
		return activeJobsMap;
	}

	public Map<String, JobMeta> getJobMetaMap() {
		return jobMetaMap;
	}

	public Map<String, String> getAutoRunJobs() {
		return autoRunJobs;
	}

}
