package com.panet.imeta.ui.web.struts.action;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.iform.forms.messageBox.MessageBoxMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.JobListDialog;
import com.panet.imeta.ui.dialog.JobRunSettingDialog;
import com.panet.imeta.ui.dialog.JobStartDialog;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.impl.ImetaJobService;

@Controller("imeta.ui.imetaJobAction")
public class ImetaJobAction extends ActionSupport {

	private static final long serialVersionUID = -8453832303438427861L;

	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobs;

	/**
	 * 开始-作业-列表：获得作业列表的窗口
	 */
	public void jobList() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JobListDialog dialog = new JobListDialog(jobs.getActiveJobsMap());

			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "jobList"));
		}

	}

	/**
	 * 向导-启动项-任务：作业启动管理
	 */
	public void jobStart() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JobStartDialog dialog = new JobStartDialog(UserInfo.getLoginUser()
					.getRep(), jobs.getActiveJobsMap(), jobs.getAutoRunJobs());

			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "jobStart"));
		}

	}

	/**
	 * 设置作业启动配置
	 */
	public void settingJobStart() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		boolean isSuccess = true;
		String ms = "提交修改成功!";

		try {
			String id = request.getParameter("id");
			Repository rep = UserInfo.getLoginUser().getRep();

			// 判断Auto表中的作业
			Map<String, String> autoRunJobs = jobs.getAutoRunJobs();
			for (String k : autoRunJobs.keySet()) {
				if (k != null && k.startsWith(rep.getName())) {
					String jobId = k.split("\\.")[1];
					boolean page = (request.getParameter(id + "_autostart."
							+ jobId) != null);
					boolean cache = ImetaJobService.AUTO_RUN_TYPE_AUTO
							.equals(autoRunJobs.get(k));

					if (page != cache) {
						autoRunJobs
								.put(
										k,
										page ? ImetaJobService.AUTO_RUN_TYPE_AUTO
												: ImetaJobService.AUTO_RUN_TYPE_DISAUTO);
						rep
								.updateAutoStartJob(
										Integer.valueOf(jobId),
										page ? ImetaJobService.AUTO_RUN_TYPE_AUTO
												: ImetaJobService.AUTO_RUN_TYPE_DISAUTO);
					}
				}
			}

			// 判断新增作业
			Long[] jobIds = rep.getAllJobIDs();
			if (jobIds != null) {
				for (Long l : jobIds) {
					if (!autoRunJobs.containsKey(rep.getName() + "." + l)) {
						String hasAuto = request.getParameter(id
								+ "_autostart." + l);
						if (hasAuto != null) {
							rep.insertAutoStartJob(l,
									ImetaJobService.AUTO_RUN_TYPE_AUTO,
									UserInfo.getLoginUser().getLogin(),
									new Date());
							autoRunJobs.put(rep.getName() + "." + l,
									ImetaJobService.AUTO_RUN_TYPE_AUTO);
						}
					}
				}
			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingJobStart"));
			isSuccess = false;
			ms = "提交修改失败！";
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("success", isSuccess);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingJobStart"));
		}
	}

	/**
	 * 打开自动执行设置窗口
	 */
	public void openAutoJobSetting() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");

			String dirId = request.getParameter("dirId");
			String jobName = request.getParameter("jobName");

			JobMeta jobMeta = jobs.getJobMetaByName(UserInfo.getLoginUser()
					.getRep(), Long.valueOf(dirId), jobName);

			String rootId = "auto_run_job_setting_" + jobMeta.getID();

			Repository rep = UserInfo.getLoginUser().getRep();

			JobRunSettingDialog dialog = new JobRunSettingDialog(jobMeta, true,
					false, false, false, false, null, false);

			// 设置属性
			dialog.setRunBtnName("确定");
			dialog
					.setRunClick("jQuery.imenu.iMenuFn.wizard.jobStartFn.btn.settingSubmit");

			RowMetaAndData asmd = rep.getAutoStartJob(jobMeta.getID());
			Long jobId = asmd
					.getInteger(Repository.FIELD_AUTO_START_ID_AUTO_START_JOB);
			Long logLevel = asmd
					.getInteger(Repository.FIELD_AUTO_START_ARG_LOG_LEVEL);
			if (jobId == null) {
				rep.insertAutoStartJob(jobMeta.getID(),
						ImetaJobService.AUTO_RUN_TYPE_DISAUTO, UserInfo
								.getLoginUser().getLogin(), new Date());
				jobs.getAutoRunJobs().put(
						rep.getName() + "." + jobMeta.getID(),
						ImetaJobService.AUTO_RUN_TYPE_DISAUTO);
			} else {
				if (logLevel != null)
					dialog.setcLogLevel(logLevel.intValue());
			}

			dialog.clearParams();
			dialog.clearVars();

			Collection<RowMetaAndData> asAttrs = rep
					.getStartJobAttribute(jobMeta.getID());
			if (asAttrs != null) {
				for (RowMetaAndData md : asAttrs) {
					String atype = md.getString(
							Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_TYPE,
							"");
					String akey = md
							.getString(
									Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_KEY,
									"");
					String avalue = md.getString(
							Repository.FIELD_R_AUTO_START_JOB_ATTR_ATTR_VALUE,
							"");
					if (!Const.isEmpty(akey)) {
						if (ImetaJobService.AUTO_RUN_ATTR_PARAMETER
								.equals(atype)) {
							dialog.addParams(akey, Const.NVL(avalue, ""));
						} else if (ImetaJobService.AUTO_RUN_ATTR_VARIABLE
								.equals(atype)) {
							dialog.addVars(akey, Const.NVL(avalue, ""));
						}
					}
				}
			}

			dialog.setRootId(rootId);

			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"openAutoJobSetting"));
		}
	}

	/**
	 * 自动执行设置窗口的提交操作
	 */
	public void submitAutoJobSetting() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html; charset=UTF-8");

		boolean isSuccess = true;
		String ms = "提交自动运行作业成功!";

		String id = request.getParameter("id");
		Long jobId = Long.valueOf(request.getParameter("jobId"));

		Repository rep = UserInfo.getLoginUser().getRep();

		try {
			String logLevel = request.getParameter(id + ".logLevel");

			String[] paramKey = request.getParameterValues(id
					+ "_parameters.parameter");
			String[] paramValue = request.getParameterValues(id
					+ "_parameters.value");

			String[] varKey = request.getParameterValues(id
					+ "_variables.parameter");
			String[] varValue = request.getParameterValues(id
					+ "_variables.value");

			// 保存设置
			rep.updateAutoStartJobLogLevel(jobId, Integer.parseInt(logLevel));

			rep.delAutoStartJobAttributes(jobId);

			if (paramKey != null && paramKey.length > 0) {
				for (int i = 0; i < paramKey.length; i++) {
					if (!Const.isEmpty(paramKey[i])) {
						rep.insertAutoStartJobAttribute(jobId,
								ImetaJobService.AUTO_RUN_ATTR_PARAMETER,
								paramKey[i], Const.NVL(paramValue[i], ""));
					}
				}
			}

			if (varKey != null && varKey.length > 0) {
				for (int i = 0; i < varKey.length; i++) {
					if (!Const.isEmpty(varKey[i])) {
						rep.insertAutoStartJobAttribute(jobId,
								ImetaJobService.AUTO_RUN_ATTR_VARIABLE,
								varKey[i], Const.NVL(varValue[i], ""));
					}
				}
			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"submitAutoJobSetting"));
			isSuccess = false;
			ms = "提交自动运行作业失败！";
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("success", isSuccess);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingFileSubmit"));
		}
	}

	/**
	 * 作业列表的停止操作
	 */
	public void jobListStop() {
		boolean isSuccess = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		String ms = "";

		String dirId = request.getParameter("dirId");
		String jobName = request.getParameter("jobName");

		try {
			Job job = this.jobs.getActiveJobByName(rep, Long.valueOf(dirId),
					jobName);
			if (job != null) {
				job.stopAll();
				job.endProcessing("stop", new Result());
				job.waitUntilFinished(5000);
				ms = "作业停止成功！";
				isSuccess = true;
			} else {
				ms = "作业未运行或者已经停止运行！";
				isSuccess = false;
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "stop"));
			ms = "作业停止操作失败！";
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "stop"));
		}

	}
}
