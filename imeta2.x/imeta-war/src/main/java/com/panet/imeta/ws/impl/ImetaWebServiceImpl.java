package com.panet.imeta.ws.impl;

import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.meta.ImetaUIUtils;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;
import com.panet.imeta.ui.service.impl.ImetaJobService;
import com.panet.imeta.ui.service.impl.ImetaServices;
import com.panet.imeta.ui.service.impl.ImetaTransformationService;
import com.panet.imeta.ws.ImetaWebService;

@WebService(endpointInterface = "com.panet.imeta.ws.ImetaWebService")
public class ImetaWebServiceImpl implements ImetaWebService {

	private ImetaDelegates delegates;

	private ImetaJobDelegate jobs;

	private ImetaTransformationDelegate trans;

	public static final String keyValueSegment = ">--<";
	public static final String parametersSegment = "<-->";

	public ImetaWebServiceImpl() throws KettleException {

		this.delegates = new ImetaServices();
		this.jobs = new ImetaJobService();
		this.trans = new ImetaTransformationService();
	}

	@Override
	public String start(String repName, String path, String objectType,
			String objectName, String args) {

		Repository rep = delegates.getRep(repName);

		String ms = "";
		try {
			RepositoryDirectory d = ImetaUIUtils.getDirectoryByPath(rep, path);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				TransMeta transMeta = trans.newTransMetaByName(rep, d.getID(),
						objectName);
				if (transMeta.getTransstatus() != TransMeta.TRANSSTATUS_PRODUCTION) {
					ms = "转换未处于生产状态，不能被执行！";
				} else {
					// 参数
					if (StringUtils.isNotEmpty(args)) {
						String[] pvs = args.split(parametersSegment);
						String[] pv;
						if (pvs != null && pvs.length > 0) {
							for (String pvStr : pvs) {
								pv = pvStr.split(keyValueSegment);
								if (pv.length >= 2) {
									transMeta.setParameterValue(pv[0], Const
											.NVL(pv[1], ""));
								}
							}
						}
					}

					transMeta.activateParameters();
					Trans trans = null;
					trans = this.trans.getActiveTransByName(rep, d.getID(),
							objectName);
					if (trans == null) {
						trans = new Trans(transMeta);

						trans.execute(null);// 在这里可以设置命令行参数

						ms = "转换执行成功！";
					} else {
						ms = "转换正在运行或暂停！";
					}
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				JobMeta jobMeta = jobs.newJobMetaByName(rep, d.getID(),
						objectName);
				if (jobMeta.getJobstatus() != JobMeta.JOBSTATUS_PRODUCTION) {
					ms = "作业未处于生产状态，不能被执行！";
				} else {
					Job job = null;
					job = jobs.getActiveJobByName(rep, d.getID(), objectName);
					if (job == null) {
						job = new Job(delegates.getLog(), rep, jobMeta);
						jobs
								.putActiveJobByName(rep, d.getID(), objectName,
										job);
						job.getJobMeta().setArguments(jobMeta.getArguments());
						job.shareVariablesWith(jobMeta);
						job.start();
						ms = "作业执行成功！";
					} else {
						ms = "作业正在运行！";
					}
				}

			}

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction"));
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				ms = "转换执行失败";
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				ms = "作业执行失败";
			}
		}
		return ms;
	}

	@Override
	public List<String> getActiveObjectList(String repName, String path,
			String objectType, String objectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pause(String repName, String path, String objectType,
			String objectName) {
		Repository rep = delegates.getRep(repName);
		String ms = "";
		try {
			RepositoryDirectory d = ImetaUIUtils.getDirectoryByPath(rep, path);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				Trans trans = this.trans.getActiveTransByName(rep, d.getID(),
						objectName);
				if (trans != null) {
					boolean pausing = trans.isPaused();
					if (pausing) {
						trans.resumeRunning();
						ms = "转换恢复运行！";
					} else {
						trans.pauseRunning();
						ms = "转换暂停运行！";
					}
				} else {
					ms = "转换未运行或者已经停止运行！";
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {

			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction"));
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				ms = "转换暂停/恢复操作失败！";
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				ms = "作业暂停/恢复操作失败！";
			}
		}
		return ms;
	}

	@Override
	public String stop(String repName, String path, String objectType,
			String objectName) {
		Repository rep = delegates.getRep(repName);
		String ms = "";
		try {
			RepositoryDirectory d = ImetaUIUtils.getDirectoryByPath(rep, path);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				Trans trans = this.trans.getActiveTransByName(rep, d.getID(),
						objectName);
				if (trans != null) {
					trans.stopAll();
					trans.endProcessing("stop");
					ms = "转换停止成功！";
				} else {
					ms = "转换未运行或者已经停止运行！";
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				Job job = this.jobs.getActiveJobByName(rep, d.getID(),
						objectName);
				if (job != null) {
					job.stopAll();
					job.endProcessing("stop", new Result());
					job.waitUntilFinished(5000);
					ms = "作业停止成功！";
				} else {
					ms = "作业未运行或者已经停止运行！";
				}
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction"));
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(objectType)) {
				ms = "转换停止操作失败！";
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
					.equals(objectType)) {
				ms = "作业停止操作失败！";
			}
		}
		return ms;
	}

}
