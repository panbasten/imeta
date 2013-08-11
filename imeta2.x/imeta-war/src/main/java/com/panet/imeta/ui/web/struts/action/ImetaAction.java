package com.panet.imeta.ui.web.struts.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.iform.forms.messageBox.MessageBoxMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepLoaderException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.logging.LogWriterProxy;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.i18n.LanguageChoice;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryLoader;
import com.panet.imeta.job.JobHopMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.JobPlugin;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.StepLoader;
import com.panet.imeta.trans.StepPlugin;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransHopMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.JobRunSettingDialog;
import com.panet.imeta.ui.dialog.RepExplorerDialog;
import com.panet.imeta.ui.dialog.StepFieldDialog;
import com.panet.imeta.ui.dialog.TransRunSettingDialog;
import com.panet.imeta.ui.dialog.XMLFileUploadDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.meta.ImetaUIUtils;
import com.panet.imeta.ui.meta.XMLFile;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaStepDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

/**
 * iMeta模块控制类
 * 
 * @author Peter Pan
 * 
 */
@Controller("imeta.ui.imetaAction")
public class ImetaAction extends ActionSupport {

	private static final long serialVersionUID = -3384194945204610401L;

	/**
	 * 元模型服务代理
	 */
	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobs;

	@Autowired
	private ImetaTransformationDelegate trans;

	@Autowired
	private ImetaStepDelegate steps;

	/**
	 * 编辑转换或者作业的节点
	 */
	public void editElement() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			String id = request.getParameter("id");
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String stepType = request.getParameter("stepType");
			String directoryId = request.getParameter("directoryId");

			// 转换
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), Long
						.valueOf(directoryId), roName);
				StepMeta stepMeta = transMeta.getStepByGraphId(id);
				if (stepMeta == null) {
					stepMeta = newStep(transMeta, stepType);
				}
				String dialog = steps.editStep(id, transMeta, stepMeta)
						.toString();
				response.getWriter().write(dialog);
			}
			// 作业
			else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), Long
						.valueOf(directoryId), roName);

				JobEntryCopy jobEntry = jobMeta.getJobEntryByGraphId(id);
				if (jobEntry == null) {
					jobEntry = newJobEntry(jobMeta, stepType);
				}
				String dialog = steps.editStep(id, jobMeta, jobEntry)
						.toString();
				response.getWriter().write(dialog);
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"editElement"));
		}
	}

	/**
	 * 设置转换或者作业的基本属性
	 */
	public void settingFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String winId = request.getParameter("id");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);

			JSONObject jo = null;
			UserInfo user = UserInfo.getLoginUser();
			Repository rep = delegates.getRep(user);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						roName);
				if (transMeta != null) {
					// 如果是该用户操作转换，清除
					if (transMeta.getModifiedUser().equals(user.getLogin())) {
						trans.clearEditTransMetaByName(rep, d, roName);
						jo = trans.getSettingContent(rep, d, roName, winId);
						jo.put("success", true);
					} else {
						jo = new JSONObject();
						jo.put("success", false);
						jo.put("message", "用户【" + transMeta.getModifiedUser()
								+ "】正在编辑转换，不能进行设置");
					}
				} else {
					jo = trans.getSettingContent(rep, d, roName, winId);
					jo.put("success", true);
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
				if (jobMeta != null) {
					// 如果是该用户操作作业，清除
					if (jobMeta.getModifiedUser().equals(user.getLogin())) {
						jobs.clearEditJobMetaByName(rep, d, roName);
						jo = jobs.getSettingContent(rep, d, roName, winId);
						jo.put("success", true);
					} else {
						jo = new JSONObject();
						jo.put("success", false);
						jo.put("message", "用户【" + jobMeta.getModifiedUser()
								+ "】正在编辑作业，不能进行设置");
					}
				} else {
					jo = jobs.getSettingContent(rep, d, roName, winId);
					jo.put("success", true);
				}

			}
			response.getWriter().write(jo.toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingFile"));
		}
	}

	/**
	 * 设置转换或者作业的基本属性的提交操作
	 */
	@SuppressWarnings("unchecked")
	public void settingFileSubmit() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		boolean isSuccess = true;
		String ms = "提交修改成功!";
		String id = request.getParameter("id");
		String roName = request.getParameter("roName");
		String roType = request.getParameter("roType");
		String newName = request.getParameter(id + ".name");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);
		newName = (StringUtils.isNotEmpty(newName)) ? newName : roName;
		Repository rep = UserInfo.getLoginUser().getRep();
		try {
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {

				TransMeta transMeta = trans.getTransMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), d, roName);
				transMeta.setModifiedDate(new Date());
				transMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());

				if (!newName.equals(roName)) {
					if (ImetaUIUtils.checkTransMetaByName(rep, d, newName,
							trans) == null) {
						transMeta.setInfo(request.getParameterMap(), id);
						transMeta.setName(newName);
						transMeta.saveRep(rep);
					} else {
						isSuccess = false;
						ms = "新修改的转换名称冲突，请更换名称！";
					}
				} else {
					transMeta.setInfo(request.getParameterMap(), id);
					transMeta.saveRep(rep);
				}
				// 清除旧的
				trans.clearTransMetaByName(rep, d, roName);

			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getJobMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), d, roName);
				jobMeta.setModifiedDate(new Date());
				jobMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());

				if (!newName.equals(roName)) {
					if (ImetaUIUtils.checkJobMetaByName(rep, d, newName, jobs) == null) {
						jobMeta.setInfo(request.getParameterMap(), id);
						jobMeta.setName(newName);
						jobMeta.saveRep(rep);
					} else {
						isSuccess = false;
						ms = "新修改的作业名称冲突，请更换名称！";
					}
				} else {
					jobMeta.setInfo(request.getParameterMap(), id);
					jobMeta.saveRep(rep);
				}
				jobs.clearJobMetaByName(rep, d, roName);
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingFileSubmit"));
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
			rtn.put("oldName", roName);
			rtn.put("newName", newName);
			rtn.put("success", isSuccess);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingFileSubmit"));
		}
	}

	public void closeFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			Repository rep = UserInfo.getLoginUser().getRep();
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				// 编辑用户是当前用户时，删除
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						roName);
				if (transMeta != null) {
					UserInfo user = UserInfo.getLoginUser();
					if (user != null
							&& user.getLogin().equals(
									transMeta.getModifiedUser())) {
						trans.clearEditTransMetaByName(rep, d, roName);
					}
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
				if (jobMeta != null) {
					UserInfo user = UserInfo.getLoginUser();
					if (user != null
							&& user.getLogin()
									.equals(jobMeta.getModifiedUser())) {
						jobs.clearEditJobMetaByName(rep, d, roName);
					}
				}
			}
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"closeFile"));
		}
	}

	/**
	 * 从资源库中打开
	 */
	public void openFile() {
		openFile(false);
	}

	/**
	 * 从XML文件中打开
	 */
	public void importFile() {
		openFile(true);
	}

	private void openFile(boolean importfile) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");

		// 从XML文件载入
		if (delegates.getRep(UserInfo.getLoginUser()) == null || importfile) {
			try {
				XMLFileUploadDialog dialog = new XMLFileUploadDialog();
				response.getWriter().write(dialog.open().toString());
			} catch (Exception e) {
				delegates.getLog().logError(
						toString(),
						Messages.getString("IMeta.Log.RunImetaAction",
								"openFile"));
			}
		} else {
			try {
				UserInfo user = UserInfo.getLoginUser();
				Repository rep = user.getRep();
				String roName = request.getParameter("roName");
				String roType = request.getParameter("roType");
				String directoryId = request.getParameter("directoryId");
				long d = Long.valueOf(directoryId);

				// 清除当前用户上次未关闭的编辑对象
				if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
						.equals(roType)) {
					TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
							roName);
					if (transMeta != null
							&& transMeta.getModifiedUser().equals(
									user.getLogin())) {
						trans.clearEditTransMetaByName(rep, d, roName);
					}
				} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
						.equals(roType)) {
					JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
					if (jobMeta != null
							&& jobMeta.getModifiedUser()
									.equals(user.getLogin())) {
						jobs.clearEditJobMetaByName(rep, d, roName);
					}
				}
				response.getWriter().write(
						drawRepObjectByNameAndType(d, roName, roType, true));

			} catch (Exception e) {
				delegates.getLog().logError(
						toString(),
						Messages.getString("IMeta.Log.RunImetaAction",
								"openFile"));
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @throws ImetaException
	 * @throws KettleException
	 */
	public void deleteFile() throws ImetaException, KettleException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(UserInfo.getLoginUser());

		try {
			String elType = request.getParameter("elType");

			String elName = request.getParameter("elName");
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(elType)) {
				String directoryId = request.getParameter("directoryId");
				long directoryId_l = -1;
				try {
					directoryId_l = Long.valueOf(directoryId);
				} catch (Exception e) {
					throw new ImetaException("删除对象出错");
				}
				rep.delAllFromTrans(rep.getTransformationID(elName,
						directoryId_l));
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(elType)) {
				String directoryId = request.getParameter("directoryId");
				long directoryId_l = -1;
				try {
					directoryId_l = Long.valueOf(directoryId);
				} catch (Exception e) {
					throw new ImetaException("删除对象出错");
				}
				rep.delAllFromJob(rep.getJobID(elName, directoryId_l));
			} else if (RepositoryObject.STRING_ELEMENT_TYPE_DATABASE
					.equals(elType)) {
				long databaseId = 0;
				try {
					databaseId = Long.valueOf(request.getParameter("elId"));
				} catch (Exception e) {
					throw new ImetaException("删除对象出错");
				}
				rep.delDatabase(databaseId);

			}
			rep.unlockRepository();

			// Perform a commit!
			rep.commit();
			MessageBoxMeta box = new MessageBoxMeta(null, null, null, "运行结果",
					"删除【" + elName + "】成功", true);
			box.setOraWidth(350);
			box.setOraHeight(160);

			response.getWriter().write(box.getFormJo().toString());

		} catch (Exception e) {
			rep.rollback();
			throw new ImetaException(e);
		} finally {
			rep.unlockRepository();
		}
	}

	/**
	 * 编辑对象
	 * 
	 * @throws ImetaException
	 */
	public void editFile() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		String roName = request.getParameter("roName");
		String roType = request.getParameter("roType");
		String edit = request.getParameter("edit");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);

		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		UserInfo user = UserInfo.getLoginUser();
		boolean isSuccess = false;
		String ms = "";
		try {
			// 如果是转换
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						roName);

				if ("off".equals(edit)) {
					if (transMeta != null) {
						trans.clearEditTransMetaByName(rep, d, transMeta
								.getName());
					}
					isSuccess = true;
					ms = "修改为不可编辑状态";
				} else {
					// 如果用户为操作员
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())) {
						isSuccess = false;
						ms = "当前用户不具有编辑权限";
					}
					// 正在被编辑
					else if (transMeta != null) {
						if (UserInfo.STRING_USER_TYPE_EDITOR
								.equalsIgnoreCase(user.getAccountType())
								&& !transMeta.getCreatedUser().equals(
										user.getLogin())) {
							isSuccess = false;
							ms = "当前用户不具有编辑该转换的权限";
						}
						// 如果是当前用户在编辑
						else if (transMeta.getModifiedUser().equals(
								user.getLogin())) {
							isSuccess = true;
							ms = "修改为编辑状态";
						}
						// 如果不是当前用户在编辑
						else {
							isSuccess = false;
							ms = "用户【" + transMeta.getModifiedUser()
									+ "】正在编辑该转换";
						}
					}
					// 未被编辑
					else {
						transMeta = trans.getTransMetaByName(rep, d, roName);
						if (UserInfo.STRING_USER_TYPE_EDITOR
								.equalsIgnoreCase(user.getAccountType())
								&& !transMeta.getCreatedUser().equals(
										user.getLogin())) {
							isSuccess = false;
							ms = "当前用户不具有编辑该转换的权限";
						}
						// 不是生产状态就可以编辑
						else if (transMeta.getTransstatus() != TransMeta.TRANSSTATUS_PRODUCTION) {
							transMeta.setModifiedUser(user.getLogin());
							trans.putEditTransMeta(rep, transMeta);
							isSuccess = true;
							ms = "修改为编辑状态";
						} else {
							isSuccess = false;
							ms = "转换【" + transMeta.getName()
									+ "】正处在生产状态，请将该转换先设置为草稿状态再进行编辑";
						}
					}
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
				if ("off".equals(edit)) {
					if (jobMeta != null) {
						jobs.clearEditJobMetaByName(rep, d, jobMeta.getName());
					}
					isSuccess = true;
					ms = "修改为不可编辑状态";
				} else {
					// 如果用户为操作员
					if (UserInfo.STRING_USER_TYPE_OPERATOR
							.equalsIgnoreCase(user.getAccountType())) {
						isSuccess = false;
						ms = "当前用户不具有编辑权限";
					}
					// 正在被编辑
					else if (jobMeta != null) {
						if (UserInfo.STRING_USER_TYPE_EDITOR
								.equalsIgnoreCase(user.getAccountType())
								&& !jobMeta.getCreatedUser().equals(
										user.getLogin())) {
							isSuccess = false;
							ms = "当前用户不具有编辑该作业的权限";
						}
						// 如果是当前用户在编辑
						else if (jobMeta.getModifiedUser().equals(
								user.getLogin())) {
							isSuccess = true;
							ms = "修改为编辑状态";
						}
						// 如果不是当前用户在编辑
						else {
							isSuccess = false;
							ms = "用户【" + jobMeta.getModifiedUser() + "】正在编辑该转换";
						}
					}
					// 未被编辑
					else {
						jobMeta = jobs.getJobMetaByName(rep, d, roName);
						if (UserInfo.STRING_USER_TYPE_EDITOR
								.equalsIgnoreCase(user.getAccountType())
								&& !jobMeta.getCreatedUser().equals(
										user.getLogin())) {
							isSuccess = false;
							ms = "当前用户不具有编辑该作业的权限";
						} else if (jobMeta.getJobstatus() != JobMeta.JOBSTATUS_PRODUCTION) {
							jobMeta.setModifiedUser(user.getLogin());
							jobs.putEditJobMeta(rep, jobMeta);
							isSuccess = true;
							ms = "修改为编辑状态";
						} else {
							isSuccess = false;
							ms = "作业【" + jobMeta.getName()
									+ "】正处在生产状态，请将该作业先设置为草稿状态再进行编辑";
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ImetaException(e);
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("success", isSuccess);
			rtn.put("canvas", drawRepObjectByNameAndType(d, roName, roType,
					false));
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "editFile"));
		}
	}

	/**
	 * 绘制对象
	 * 
	 * @param roName
	 * @param roType
	 * @return
	 * @throws ImetaException
	 */
	private String drawRepObjectByNameAndType(long directoryId, String roName,
			String roType, boolean exeditable) throws ImetaException {
		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		UserInfo user = UserInfo.getLoginUser();
		return ImetaUIUtils.drawRepObjectByNameAndType(directoryId, roName,
				roType, exeditable, rep, user, trans, jobs);
	}

	public void saveAndCloseFile() {
		boolean isSuccess = true;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html; charset=UTF-8");
			String roType = request.getParameter("roType");
			String canvas = request.getParameter("canvas");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			saveFile(roType, d, canvasJo);
			closeFile();
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveAndCloseFile"));
			isSuccess = false;
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", (isSuccess) ? "保存成功" : "保存失败", true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveAndCloseFile"));
		}
	}

	public void saveAndNotEditFile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html; charset=UTF-8");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			saveFile(roType, d, canvasJo);
			editFile();
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveAndNotEditFile"));
		}
	}

	public void saveFileAsXMLDownload() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String xmlFileKey = request.getParameter("xmlFileKey");
			XMLFile xmlFile = this.delegates.getXMLFile(xmlFileKey);
			String fileName = xmlFile.getName() + ".xml";
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			fileName = StringUtils.replace(fileName, " ", "%20");
			fileName = ImetaUIUtils.replaceFileName(fileName);
			if (xmlFile == null) {
				throw new ImetaException("未得到下载文件");
			}
			response.setContentType(".xml;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			PrintWriter os = response.getWriter();
			os.write(xmlFile.getXml());

			os.flush();
			os.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveFileAsXMLDownload"));
		}
	}

	/**
	 * 保存成XML文件
	 */
	public void saveFileAsXML() {
		boolean isSuccess = true;
		String ms = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		StringBuffer xml = new StringBuffer(XMLHandler.getXMLHeader());
		try {
			response.setContentType("text/html; charset=UTF-8");
			String roType = request.getParameter("roType");
			String roName = request.getParameter("roName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);

			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {

				TransMeta transMeta = modifyTrans(d, canvasJo);
				xml.append(transMeta.getXML());
				isSuccess = true;
				ms = roName;
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {

				JobMeta jobMeta = modifyJob(d, canvasJo);
				xml.append(jobMeta.getXML());
				isSuccess = true;
				ms = roName;
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveFileAsXML"));
			isSuccess = false;
			ms = "另存为XML失败!";
		}

		try {
			if (isSuccess) {
				JSONObject rtnJo = new JSONObject();
				rtnJo.put("success", isSuccess);
				String key = Encr.encryptPassword(UserInfo.getLoginUser()
						.getLogin()
						+ ".xmlFile");
				rtnJo.put("xmlFileKey", key);
				this.delegates.putXMLFile(key, ms, xml.toString());
				response.getWriter().write(rtnJo.toString());
			} else {
				MessageBoxMeta box = new MessageBoxMeta(null, null,
						MessageBoxMeta.MESSAGEBOX_ICON_ERROR, "运行结果", ms, true);
				box.setOraWidth(350);
				box.setOraHeight(160);
				JSONObject rtnJo = box.getFormJo();
				rtnJo.put("success", isSuccess);
				response.getWriter().write(rtnJo.toString());
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"saveFileAsXML"));
		}
	}

	/**
	 * 另存为
	 */
	public void saveFileAs() {
		boolean isSuccess = true;
		String ms = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Repository rep = UserInfo.getLoginUser().getRep();
		String newCanvas = null;
		UserInfo user = UserInfo.getLoginUser();
		String id = "";
		try {
			response.setContentType("text/html; charset=UTF-8");
			String newName = request.getParameter("newName");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long oldD = Long.valueOf(directoryId);

			Object[] dn = ImetaUIUtils.getDirectoryAndName(rep, newName);

			long d = ((RepositoryDirectory) dn[0]).getID();
			newName = (String) dn[1];

			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				if (ImetaUIUtils.checkTransMetaByName(rep, d, newName, trans) != null) {
					isSuccess = false;
					ms = "另存为的转换名称冲突，请更换名称！";
				} else {
					TransMeta transMeta = modifyTrans(oldD, canvasJo);
					TransMeta newTransMeta = (TransMeta) transMeta.clone();
					newTransMeta.setTransstatus(TransMeta.TRANSSTATUS_DRAFT);
					newTransMeta.setName(newName);
					newTransMeta.setDirectory((RepositoryDirectory) dn[0]);
					newTransMeta.setCreatedUser(user.getLogin());
					newTransMeta.setModifiedUser(user.getLogin());
					newTransMeta.setCreatedDate(new Date());
					newTransMeta.setModifiedDate(new Date());
					newTransMeta.saveRep(rep);

					trans.putEditTransMeta(rep, newTransMeta);
					newCanvas = drawRepObjectByNameAndType(d, newName, roType,
							true);
					isSuccess = true;
					ms = "另存为【" + newName + "】成功！";

				}

			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				if (ImetaUIUtils.checkJobMetaByName(rep, d, newName, jobs) != null) {
					isSuccess = false;
					ms = "另存为的作业名称冲突，请更换名称！";
				} else {
					JobMeta jobMeta = modifyJob(d, canvasJo);
					JobMeta newJobMeta = (JobMeta) jobMeta.clone();
					newJobMeta.setJobstatus(JobMeta.JOBSTATUS_DRAFT);
					newJobMeta.setName(newName);
					newJobMeta.setDirectory((RepositoryDirectory) dn[0]);
					newJobMeta.setCreatedUser(user.getLogin());
					newJobMeta.setModifiedUser(user.getLogin());
					newJobMeta.setCreatedDate(new Date());
					newJobMeta.setModifiedDate(new Date());
					// newJobMeta.saveRep(rep);
					jobs.putEditJobMeta(rep, newJobMeta);
					newCanvas = drawRepObjectByNameAndType(d, newName, roType,
							true);

					isSuccess = true;
					ms = "另存为【" + newName + "】成功！";

				}
			}
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"saveFileAs"));
			isSuccess = false;
			ms = "另存为失败!";
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtnJo = box.getFormJo();
			rtnJo.put("canvas", newCanvas);
			rtnJo.put("success", isSuccess);
			rtnJo.put("id", id);

			response.getWriter().write(rtnJo.toString());
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"saveFileAs"));
		}
	}

	private void saveFile(String roType, long d, JSONObject canvasJo)
			throws ImetaException, KettleException {
		Repository rep = UserInfo.getLoginUser().getRep();
		if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION.equals(roType)) {
			TransMeta transMeta = modifyTrans(d, canvasJo);
			transMeta.setModifiedDate(new Date());
			transMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());
			transMeta.saveRep(rep);

			String transname = transMeta.getName();
			// 清除旧的
			trans.clearTransMetaByName(rep, d, transname);
			// 得到新的
			TransMeta newTransMeta = trans
					.getTransMetaByName(rep, d, transname);
			trans.putEditTransMeta(rep, newTransMeta);
		} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
			JobMeta jobMeta = modifyJob(d, canvasJo);
			jobMeta.setModifiedDate(new Date());
			jobMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());
			jobMeta.saveRep(rep);

			String jobname = jobMeta.getName();
			// 清除旧的
			jobs.clearJobMetaByName(rep, d, jobname);
			// 得到新的
			JobMeta newJobMeta = jobs.getJobMetaByName(rep, d, jobname);
			jobs.putEditJobMeta(rep, newJobMeta);
		}
	}

	/**
	 * 保存修改
	 */
	public void saveFile() {
		boolean isSuccess = true;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String newCanvas = null;

		try {
			response.setContentType("text/html; charset=UTF-8");
			String roType = request.getParameter("roType");
			String roName = request.getParameter("roName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);

			saveFile(roType, d, canvasJo);

			newCanvas = drawRepObjectByNameAndType(d, roName, roType, true);
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "saveFile"));
			isSuccess = false;
			e.printStackTrace();
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", (isSuccess) ? "保存成功" : "保存失败", true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtnJo = box.getFormJo();
			rtnJo.put("canvas", newCanvas);
			rtnJo.put("success", isSuccess);

			response.getWriter().write(rtnJo.toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "saveFile"));
		}
	}

	private JobMeta modifyJob(long d, JSONObject canvasJo)
			throws ImetaException {
		try {
			Repository rep = UserInfo.getLoginUser().getRep();
			JSONObject c = canvasJo.getJSONObject("canvas");
			int x = c.getInt("x0");
			int y = c.getInt("y0");
			double s = c.getDouble("scale");
			JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, canvasJo
					.getString("objectName"));
			boolean isUpdate = canvasJo.getBoolean("isUpdate");
			// 创建job
			if (isUpdate) {

				if (jobMeta != null) {
					// 1.修改job的坐标尺寸
					jobMeta.setGuiLocationX(x);
					jobMeta.setGuiLocationY(y);
					jobMeta.setGuiScale(s);

					// 2.修改job
					JSONArray entriesJa = c.getJSONArray("steps");
					JSONObject entryJo;
					String newJeName, jeId;
					boolean isEnable;
					List<JobEntryCopy> entryList = new ArrayList<JobEntryCopy>();
					for (int i = 0; i < entriesJa.size(); i++) {

						entryJo = entriesJa.getJSONObject(i);
						isEnable = entryJo.getBoolean("isEnable");
						if (isEnable) {
							newJeName = entryJo.getJSONArray("bText")
									.getString(0);
							jeId = entryJo.getString("id");

							JobEntryCopy step = jobMeta
									.getJobEntryByGraphId(jeId);
							step.setLocation(entryJo.getInt("dx"), entryJo
									.getInt("dy"));
							step.setName(newJeName);

							entryList.add(step);
						}
					}
					jobMeta.removeAllJobEntries();
					for (JobEntryCopy je : entryList) {
						jobMeta.addJobEntry(je);
					}

					// 3.修改连接线
					JSONArray hopsJa = c.getJSONArray("hops");
					JSONObject hopJo;
					String fromStep, toStep, customType;
					JSONArray xJa, yJa;
					int[] xArr = null, yArr = null;
					int hopJoinNr;

					List<JobHopMeta> hopMetaList = new ArrayList<JobHopMeta>();
					for (int i = 0; i < hopsJa.size(); i++) {
						hopJo = hopsJa.getJSONObject(i);
						isEnable = hopJo.getBoolean("isEnable");
						if (isEnable) {
							fromStep = hopJo.getString("fromEl");
							toStep = hopJo.getString("toEl");
							xJa = hopJo.getJSONArray("x");
							yJa = hopJo.getJSONArray("y");
							customType = hopJo.getString("customType");
							xArr = null;
							yArr = null;
							hopJoinNr = xJa.size() - 2;
							if (hopJoinNr > 0) {
								xArr = new int[hopJoinNr];
								yArr = new int[hopJoinNr];
								for (int h = 0; h < hopJoinNr; h++) {
									xArr[h] = xJa.getInt(h + 1);
									yArr[h] = yJa.getInt(h + 1);
								}
							}
							JobHopMeta jobHopMeta = new JobHopMeta(jobMeta
									.getJobEntryByGraphId(fromStep), jobMeta
									.getJobEntryByGraphId(toStep));
							jobHopMeta.setGuiMidLocationX(xArr);
							jobHopMeta.setGuiMidLocationY(yArr);
							if (JobHopMeta.CUSTOM_TYPE_UNCONDITIONAL
									.equals(customType)) {
								jobHopMeta.setUnconditional();
							} else if (JobHopMeta.CUSTOM_TYPE_SUCCESS
									.equals(customType)) {
								jobHopMeta.setConditional();
								jobHopMeta.setEvaluation();
							} else if (JobHopMeta.CUSTOM_TYPE_FAILURE
									.equals(customType)) {
								jobHopMeta.setConditional();
								jobHopMeta.setEvaluation(false);
							}

							hopMetaList.add(jobHopMeta);
						}
					}
					jobMeta.removeAllJobHops();
					for (JobHopMeta hop : hopMetaList) {
						jobMeta.addJobHop(hop);
					}
				}
			}
			return jobMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 修改转换，输入页面信息添加或删除元素
	 * 
	 * @param canvasJo
	 * @return
	 * @throws KettleException
	 */
	private TransMeta modifyTrans(long d, JSONObject canvasJo)
			throws ImetaException {
		try {
			Repository rep = UserInfo.getLoginUser().getRep();
			JSONObject c = canvasJo.getJSONObject("canvas");
			int x = c.getInt("x0");
			int y = c.getInt("y0");
			double s = c.getDouble("scale");
			TransMeta transMeta = trans.getEditTransMetaByName(rep, d, canvasJo
					.getString("objectName"));
			boolean isUpdate = canvasJo.getBoolean("isUpdate");
			// 创建转换
			if (isUpdate) {
				if (transMeta != null) {
					// 1.修改trans的坐标尺寸
					transMeta.setGuiLocationX(x);
					transMeta.setGuiLocationY(y);
					transMeta.setGuiScale(s);

					// 2.修改step
					JSONArray stepsJa = c.getJSONArray("steps");
					JSONObject stepJo;
					String newStepName, stepId;
					boolean isEnable;
					List<StepMeta> stepMetaList = new ArrayList<StepMeta>();
					for (int i = 0; i < stepsJa.size(); i++) {

						stepJo = stepsJa.getJSONObject(i);
						isEnable = stepJo.getBoolean("isEnable");
						if (isEnable) {
							newStepName = stepJo.getJSONArray("bText")
									.getString(0);
							stepId = stepJo.getString("id");

							StepMeta step = transMeta.getStepByGraphId(stepId);
							step.setLocation(stepJo.getInt("dx"), stepJo
									.getInt("dy"));
							step.setName(newStepName);

							stepMetaList.add(step);
						}
					}
					transMeta.removeAllStep();
					for (StepMeta stepMeta : stepMetaList) {
						transMeta.addStep(stepMeta);
					}

					// 3.修改连接线
					JSONArray hopsJa = c.getJSONArray("hops");
					JSONObject hopJo;
					String fromStep, toStep;
					JSONArray xJa, yJa;
					int[] xArr = null, yArr = null;
					int hopJoinNr;

					List<TransHopMeta> hopMetaList = new ArrayList<TransHopMeta>();
					for (int i = 0; i < hopsJa.size(); i++) {
						hopJo = hopsJa.getJSONObject(i);
						isEnable = hopJo.getBoolean("isEnable");
						if (isEnable) {
							fromStep = hopJo.getString("fromEl");
							toStep = hopJo.getString("toEl");
							xJa = hopJo.getJSONArray("x");
							yJa = hopJo.getJSONArray("y");
							xArr = null;
							yArr = null;
							hopJoinNr = xJa.size() - 2;
							if (hopJoinNr > 0) {
								xArr = new int[hopJoinNr];
								yArr = new int[hopJoinNr];
								for (int h = 0; h < hopJoinNr; h++) {
									xArr[h] = xJa.getInt(h + 1);
									yArr[h] = yJa.getInt(h + 1);
								}
							}

							TransHopMeta transHopMeta = new TransHopMeta(
									transMeta.getStepByGraphId(fromStep),
									transMeta.getStepByGraphId(toStep));
							transHopMeta.setGuiMidLocationX(xArr);
							transHopMeta.setGuiMidLocationY(yArr);

							hopMetaList.add(transHopMeta);
						}
					}
					transMeta.removeAllTransHop();
					for (TransHopMeta hop : hopMetaList) {
						transMeta.addTransHop(hop);
					}
				}
			}
			return transMeta;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 获得一个jobEntry的名字
	 * 
	 * @param jobMeta
	 * @param name
	 * @return
	 */
	private String getJobEntryName(JobMeta jobMeta, String name) {
		if (jobMeta.findJobEntry(name) != null) {
			int i = 2;
			while (jobMeta.findJobEntry(name + " " + i) != null) {
				i++;
			}
			name = name + " " + i;
		}
		return name;
	}

	/**
	 * 创建一个jobEntry
	 * 
	 * @param jobMeta
	 * @param type
	 * @return
	 * @throws ImetaException
	 */
	private JobEntryCopy newJobEntry(JobMeta jobMeta, String type)
			throws ImetaException {
		JobEntryCopy inf = null;

		JobEntryLoader jeloader = JobEntryLoader.getInstance();
		JobPlugin jobPlugin = null;

		try {
			jobPlugin = jeloader.findJobEntriesWithID(type);
			if (jobPlugin != null) {

				JobEntryInterface info = jeloader.getJobEntryClass(jobPlugin);

				// jobEntry的命名
				String locale = LanguageChoice.getInstance().getDefaultLocale()
						.toString().toLowerCase();
				String name = jobPlugin.getDescription(locale);

				inf = new JobEntryCopy(delegates.getLog(), info);

				inf.setName(getJobEntryName(jobMeta, name));

				inf.setDrawn(true);

			}
		} catch (KettleStepLoaderException e) {
			throw new ImetaException(e);
		}

		return inf;
	}

	/**
	 * 得到step的名字
	 * 
	 * @param transMeta
	 * @param name
	 * @return
	 */
	private String getStepName(TransMeta transMeta, String name) {
		if (transMeta.findStep(name) != null) {
			int i = 2;
			while (transMeta.findStep(name + " " + i) != null) {
				i++;
			}
			name = name + " " + i;
		}
		return name;
	}

	/**
	 * 添加新的step
	 * 
	 * @param transMeta
	 * @param name
	 *            step名称
	 * @param description
	 *            stepType 的描述
	 * @return
	 * @throws KettleException
	 */
	private StepMeta newStep(TransMeta transMeta, String type)
			throws ImetaException {
		StepMeta inf = null;

		StepLoader steploader = StepLoader.getInstance();
		StepPlugin stepPlugin = null;

		try {
			stepPlugin = steploader.findStepPluginWithID(type);
			if (stepPlugin != null) {
				StepMetaInterface info = BaseStep.getStepInfo(stepPlugin,
						steploader);

				info.setDefault();

				// step的命名
				String locale = LanguageChoice.getInstance().getDefaultLocale()
						.toString().toLowerCase();
				String name = stepPlugin.getDescription(locale);

				inf = new StepMeta(stepPlugin.getID()[0], getStepName(
						transMeta, name), info);

			}
		} catch (KettleStepLoaderException e) {
			throw new ImetaException(e);
		}

		return inf;
	}

	/**
	 * 运行
	 * 
	 * @throws ImetaException
	 */
	public void run() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		try {
			String rootId = request.getParameter("taskId");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			String objectName = canvasJo.getString("objectName");
			Repository rep = delegates.getRep(UserInfo.getLoginUser());
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						objectName);
				if (transMeta == null) {
					transMeta = trans.getTransMetaByName(rep, d, objectName);
				}
				TransRunSettingDialog dialog = new TransRunSettingDialog(
						transMeta, true, false, false, false, false, null,
						false);
				dialog.setRootId(rootId);
				response.getWriter().write(dialog.open().toString());
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, objectName);
				if (jobMeta == null) {
					jobMeta = jobs.getJobMetaByName(rep, d, objectName);
				}
				JobRunSettingDialog dialog = new JobRunSettingDialog(jobMeta,
						true, false, false, false, false, null, false);
				dialog.setRootId(rootId);
				response.getWriter().write(dialog.open().toString());
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "run"));
		}
	}

	/**
	 * 暂停
	 * 
	 * @throws ImetaException
	 */
	public void pause() throws ImetaException {
		String ms = "";
		boolean isSuccess = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		String roType = request.getParameter("roType");
		String objectName = request.getParameter("objectName");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);
		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		try {
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				Trans trans = this.trans.getActiveTransByName(rep, d,
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
					isSuccess = true;
				} else {
					ms = "转换未运行或者已经停止运行！";
					isSuccess = false;
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {

			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "pause"));
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				ms = "转换暂停/恢复操作失败！";
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				ms = "作业暂停/恢复操作失败！";
			}
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
					Messages.getString("IMeta.Log.RunImetaAction", "pause"));
		}
	}

	/**
	 * 停止
	 * 
	 * @throws ImetaException
	 */
	public void stop() throws ImetaException {
		String ms = "";
		boolean isSuccess = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		String roType = request.getParameter("roType");
		String objectName = request.getParameter("objectName");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);
		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		try {
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				Trans trans = this.trans.getActiveTransByName(rep, d,
						objectName);
				if (trans != null) {
					trans.stopAll();
					trans.endProcessing("stop");
					ms = "转换停止成功！";
					isSuccess = true;
				} else {
					ms = "转换未运行或者已经停止运行！";
					isSuccess = false;
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				Job job = this.jobs.getActiveJobByName(rep, d, objectName);
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
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "stop"));
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				ms = "转换停止操作失败！";
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				ms = "作业停止操作失败！";
			}
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

	/**
	 * 开始运行
	 * 
	 * @throws ImetaException
	 */
	public void start() throws ImetaException {
		String ms = "";
		boolean isSuccess = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		String roType = request.getParameter("roType");
		String objectName = request.getParameter("objectName");
		String isWait = request.getParameter("isWait");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);

		UserInfo user = UserInfo.getLoginUser();

		String id = request.getParameter("id");

		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		try {

			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						objectName);
				if (transMeta == null) {
					transMeta = trans.getTransMetaByName(rep, d, objectName);
				}
				// 参数
				String[] params = request.getParameterValues(id
						+ "_parameters.parameter");
				String[] value = request.getParameterValues(id
						+ "_parameters.value");

				int logLevel = LogWriter.getLogLevel(request.getParameter(id
						+ ".logLevel"));

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						transMeta.setParameterValue(params[i], Const.NVL(
								value[i], ""));
					}
				}
				transMeta.activateParameters();
				Trans trans = null;
				trans = this.trans.getActiveTransByName(rep, d, objectName);
				if (trans == null) {
					trans = new Trans(new LogWriterProxy(delegates.getLog(),
							logLevel, user.getLogin(),
							LogWriterProxy.IVOKE_TYPE_MANUAL), transMeta);

					trans.execute(null);// 在这里可以设置命令行参数

					if ("true".equalsIgnoreCase(isWait)) {
						trans.waitUntilFinished();// 等待所有子线程运行结束
						int errNum = trans.getErrors();
						if (errNum > 0) {
							isSuccess = false;
							ms = Messages.getString(
									"IMeta.Trans.Run.FailureMsg", String
											.valueOf(errNum));
						} else {
							isSuccess = true;
							ms = Messages
									.getString("IMeta.Trans.Run.SuccessMsg");
						}
					} else {
						isSuccess = true;
						ms = Messages.getString("IMeta.Trans.Run.SubmitMsg");
					}
				} else {
					isSuccess = false;
					ms = Messages
							.getString("IMeta.Trans.Run.RunningOrPauseMsg");
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, objectName);
				if (jobMeta == null) {
					jobMeta = jobs.getJobMetaByName(rep, d, objectName);
				}
				// 参数
				String[] params = request.getParameterValues(id
						+ "_parameters.parameter");
				String[] value = request.getParameterValues(id
						+ "_parameters.value");

				int logLevel = LogWriter.getLogLevel(request.getParameter(id
						+ ".logLevel"));

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						jobMeta.setParameterValue(params[i], Const.NVL(
								value[i], ""));
					}
				}
				jobMeta.activateParameters();
				Job job = null;
				job = jobs.getActiveJobByName(rep, d, objectName);
				if (job == null) {
					job = new Job(new LogWriterProxy(delegates.getLog(),
							logLevel, user.getLogin(),
							LogWriterProxy.IVOKE_TYPE_MANUAL), rep, jobMeta);
					jobs.putActiveJobByName(rep, d, objectName, job);
					job.getJobMeta().setArguments(jobMeta.getArguments());
					job.shareVariablesWith(jobMeta);
					job.start();
					int errNum = job.getErrors();
					if (errNum > 0) {
						isSuccess = false;
						ms = Messages.getString("IMeta.Trans.Run.FailureMsg",
								String.valueOf(errNum));
					} else {
						isSuccess = true;
						ms = Messages.getString("IMeta.Jobs.Run.SubmitMsg");
					}
				} else {
					isSuccess = false;
					ms = Messages.getString("IMeta.Jobs.Run.RunningOrPauseMsg");
				}

			}

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "start"));

			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				ms = Messages.getString("IMeta.Trans.Run.ErrorMsg", e
						.getMessage());
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				ms = Messages.getString("IMeta.Jobs.Run.ErrorMsg", e
						.getMessage());
			}
		}
		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					Messages.getString("IMeta.Run.Title"), ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "start"));
		}
	}

	public void fileOpen() {
		try {
			Repository rep = UserInfo.getLoginUser().getRep();
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				String id = request.getParameter("id");
				String parentId = request.getParameter("parentId");
				String showTypes = request.getParameter("showTypes");
				String customOkFn = request.getParameter("customOkFn");
				RepExplorerDialog repExplorerDialog = new RepExplorerDialog(
						rep, id);
				repExplorerDialog.setShowTypes(Const
						.NVL(showTypes, "trans,job"));
				repExplorerDialog.setCustomOkFn(Const.NVL(customOkFn, ""));
				repExplorerDialog.setParentId(Const.NVL(parentId, ""));
				response.getWriter().write(repExplorerDialog.open().toString());
			}

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction"));
		}
	}

	/**
	 * 新建一个转换
	 */
	public void newTransFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			Repository rep = UserInfo.getLoginUser().getRep();
			TransMeta transMeta = new TransMeta(rep);
			transMeta.clearChanged();

			Date now = new Date();
			transMeta.setCreatedDate(now);
			transMeta.setModifiedDate(now);
			transMeta.setCreatedUser(UserInfo.getLoginUser().getLogin());
			transMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());

			Object[] dn = ImetaUIUtils.getDirectoryAndName(rep, request
					.getParameter("newTransName"));

			transMeta.setDirectory((RepositoryDirectory) dn[0]);
			String newTransName = Const.NVL((String) dn[1],
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION_DESP);
			newTransName = ImetaUIUtils.getCheckedTransMetaName(rep,
					((RepositoryDirectory) dn[0]).getID(), newTransName, trans);
			transMeta.setName(newTransName);

			trans.putEditTransMeta(rep, transMeta);

			response.getWriter().write(
					drawRepObjectByNameAndType(((RepositoryDirectory) dn[0])
							.getID(), newTransName,
							RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION,
							true));

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"newTransFile"));
		}
	}

	/**
	 * 新建一个作业
	 */
	public void newJobFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String newJobName = request.getParameter("newJobName");

			Repository rep = UserInfo.getLoginUser().getRep();

			JobMeta jobMeta = new JobMeta(delegates.getLog());
			jobMeta.clearChanged();
			jobMeta.setRep(rep);
			jobMeta.readSharedObjects(rep);

			Date now = new Date();
			jobMeta.setCreatedDate(now);
			jobMeta.setModifiedDate(now);
			jobMeta.setCreatedUser(UserInfo.getLoginUser().getLogin());
			jobMeta.setModifiedUser(UserInfo.getLoginUser().getLogin());

			Object[] dn = ImetaUIUtils.getDirectoryAndName(rep, newJobName);
			newJobName = Const.NVL((String) dn[1],
					RepositoryObject.STRING_OBJECT_TYPE_JOB_DESP);
			newJobName = ImetaUIUtils.getCheckedJobMetaName(rep,
					((RepositoryDirectory) dn[0]).getID(), newJobName, jobs);
			jobMeta.setName(newJobName);
			jobMeta.setDirectory((RepositoryDirectory) dn[0]);

			jobs.putEditJobMeta(rep, jobMeta);

			response.getWriter().write(
					drawRepObjectByNameAndType(((RepositoryDirectory) dn[0])
							.getID(), newJobName,
							RepositoryObject.STRING_OBJECT_TYPE_JOB, true));

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"newJobFile"));
		}
	}

	public void deleteElement() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			JSONObject rtn = new JSONObject();

			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				modifyTrans(d, canvasJo);
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				modifyJob(d, canvasJo);
			}

			rtn.put("success", true);
			response.getWriter().write(rtn.toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"deleteElement"));
		}
	}

	/**
	 * 修改元素信息（分发、复制、执行次数）
	 */
	public void modifyElement() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String elId = request.getParameter("elId");
			String modifyType = request.getParameter("modifyType");
			String modifyValue = request.getParameter("modifyValue");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String canvas = request.getParameter("canvas");
			JSONObject canvasJo = JSONObject.fromObject(canvas);
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = modifyTrans(d, canvasJo);
				StepMeta stepMeta = transMeta.getStepByGraphId(elId);
				if ("dataMoveType".equals(modifyType)) {
					if ("distribute".equals(modifyValue)) {
						stepMeta.setDistributes(true);
					} else if ("copy".equals(modifyValue)) {
						stepMeta.setDistributes(false);
					}
				} else if ("dataCopies".equals(modifyType)) {
					stepMeta.setCopies(Const.toInt(modifyValue, 1));
				}
			}

			response.getWriter().write(
					drawRepObjectByNameAndType(d, roName, roType, true));
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"modifyElement"));
		}
	}

	/**
	 * 新增一个元素
	 */
	public void newElement() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String element = request.getParameter("element");
			JSONObject rtn = null;
			Repository rep = UserInfo.getLoginUser().getRep();
			if ("step".equals(element)) {
				String elType = request.getParameter("elType");
				int x = Const.toInt(request.getParameter("x"), -1);
				int y = Const.toInt(request.getParameter("y"), -1);

				if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
						.equals(roType)) {
					TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
							roName);
					// 创建新的对象
					StepMeta stepMeta = newStep(transMeta, elType);
					String id = "step_n_" + stepMeta.getTypeId();
					if (transMeta.getStepByGraphId(id) != null) {
						int i = 2;
						while (transMeta.getStepByGraphId(id + "_" + i) != null) {
							i++;
						}
						id = id + "_" + i;
					}
					// 写入临时ID
					stepMeta.setTempId(id);
					// 添加到编辑对象中
					transMeta.addStep(stepMeta);
					rtn = stepMeta.getJSON();
					rtn.put("id", id);
					rtn.put("stepType", elType);
					rtn.put("stepName", stepMeta.getName());
					rtn.put("dx", x);
					rtn.put("dy", y);

				} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
						.equals(roType)) {
					JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
					// 创建新的对象
					JobEntryCopy jobEntry = newJobEntry(jobMeta, elType);
					String id = "jobEntry_n_"
							+ jobEntry.getEntry().getTypeCode();
					if (jobMeta.getJobEntryByGraphId(id) != null) {
						int i = 2;
						while (jobMeta.getJobEntryByGraphId(id + "_" + i) != null) {
							i++;
						}
						id = id + "_" + i;
					}
					// 写入临时ID
					jobEntry.setTempId(id);
					// 添加到编辑对象中
					jobMeta.addJobEntry(jobEntry);
					rtn = jobEntry.getJSON();
					rtn.put("id", id);
					rtn.put("stepType", elType);
					rtn.put("stepName", jobEntry.getName());
					rtn.put("dx", x);
					rtn.put("dy", y);

				}
			} else if ("hop".equals(element)) {
				String fromEl = request.getParameter("fromEl");
				String toEl = request.getParameter("toEl");
				String toElWidth = request.getParameter("toElWidth");
				String toElHeight = request.getParameter("toElHeight");

				if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
						.equals(roType)) {
					TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
							roName);
					TransHopMeta hop = new TransHopMeta(transMeta
							.getStepByGraphId(fromEl), transMeta
							.getStepByGraphId(toEl));
					String id = "hop_n_" + fromEl + "_to_" + toEl;
					if (transMeta.getStepByGraphId(id) != null) {
						int i = 2;
						while (transMeta.getStepByGraphId(id + "_" + i) != null) {
							i++;
						}
						id = id + "_" + i;
					}
					hop.setTempId(id);
					transMeta.addTransHop(hop);
					rtn = new JSONObject();
					rtn.put("id", id);
					rtn.put("toEl", toEl);
					rtn.put("toElWidth", toElWidth);
					rtn.put("toElHeight", toElHeight);
					if (!hop.getFromStep().isDistributes()) {
						JSONArray text = new JSONArray();
						text.add("复制");
						rtn.put("text", text);
					}
				} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
						.equals(roType)) {
					JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
					JobHopMeta hop = new JobHopMeta(jobMeta
							.getJobEntryByGraphId(fromEl), jobMeta
							.getJobEntryByGraphId(toEl));
					String id = "jobhop_n_" + fromEl + "_to_" + toEl;
					if (jobMeta.getJobHopByGraphId(id) != null) {
						int i = 2;
						while (jobMeta.getJobHopByGraphId(id + "_" + i) != null) {
							i++;
						}
						id = id + "_" + i;
					}
					hop.setTempId(id);
					jobMeta.addJobHop(hop);
					rtn = new JSONObject();
					rtn.put("id", id);
					rtn.put("toEl", toEl);
					rtn.put("toElWidth", toElWidth);
					rtn.put("toElHeight", toElHeight);
				}
			}
			rtn.put("success", true);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"newElement"));
		}
	}

	public void addAndUpdateParameters() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		JSONObject rtn = new JSONObject();
		boolean isSuccess = true;
		try {
			String roType = request.getParameter("roType");
			String roName = request.getParameter("roName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			String elementName = request.getParameter("elementName");
			String getfieldsType = request.getParameter("getfieldsType");
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), d, roName);
				StepMeta stepMeta = transMeta.findStep(elementName);

				RowMetaInterface fields = null;
				if ("info".equals(getfieldsType)) {
					fields = transMeta.getPrevInfoFields(stepMeta);
				} else {
					fields = transMeta.getPrevStepFields(stepMeta);
				}
				StepFieldDialog stepFieldDialog = new StepFieldDialog(fields);
				rtn.putAll(stepFieldDialog.open());
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {

			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"addAndUpdateParameters"));
			isSuccess = false;
		}

		try {
			rtn.put("success", isSuccess);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"addAndUpdateParameters"));
		}
	}

	/**
	 * 修改提交修改
	 */
	@SuppressWarnings("unchecked")
	public void editElementSubmit() {
		boolean isSuccess = true;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		String id = request.getParameter("id");
		String roType = request.getParameter("roType");
		String roName = request.getParameter("roName");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);
		String elementName = request.getParameter("elementName");
		String newName = request.getParameter(id + ".name");
		newName = (StringUtils.isNotEmpty(newName)) ? newName : elementName;
		try {

			Repository rep = delegates.getRep(UserInfo.getLoginUser());
			Map<String, String[]> p = request.getParameterMap();
			if (id != null) {
				if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
						.equals(roType)) {
					TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
							roName);
					StepMeta stepMeta = transMeta.getStepByGraphId(id);
					stepMeta.setInfo(p, id, transMeta.getDatabases());
					int stepIndex = transMeta.getStepIndex(elementName);
					if (!newName.equals(elementName)) {
						newName = getStepName(transMeta, newName);
						stepMeta.setName(newName);
					}
					transMeta.setStep(stepIndex, stepMeta);

				} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
						.equals(roType)) {
					JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
					JobEntryCopy jobEntryCopy = jobMeta
							.findJobEntry(elementName);
					jobEntryCopy.setInfo(p, id, jobMeta.getDatabases());
					int jobEntryIndex = jobMeta.getJobEntryIndex(elementName);
					if (!newName.equals(elementName)) {
						newName = getJobEntryName(jobMeta, newName);
						jobEntryCopy.setName(newName);
					}
					jobMeta.setJobEntry(jobEntryIndex, jobEntryCopy);
				}
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"editElementSubmit"));
			isSuccess = false;
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", (isSuccess) ? "提交修改成功！" : "提交修改失败！", true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("oldName", elementName);
			rtn.put("newName", newName);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"editElementSubmit"));
		}
	}

	/**
	 * 数据验证模块 提交修改
	 */
	@SuppressWarnings("unchecked")
	public void validatorElementSubmit() {
		boolean isSuccess = true;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		String optionValue = request.getParameter("optionValue");
		String id = request.getParameter("id");
		String roType = request.getParameter("roType");
		String roName = request.getParameter("roName");
		String directoryId = request.getParameter("directoryId");
		long d = Long.valueOf(directoryId);
		String elementName = request.getParameter("elementName");
		String newName = request.getParameter(id + ".name");
		newName = (StringUtils.isNotEmpty(newName)) ? newName : elementName;
		try {

			Repository rep = delegates.getRep(UserInfo.getLoginUser());
			Map<String, String[]> p = request.getParameterMap();
			if (id != null) {
				if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
						.equals(roType)) {
					TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
							roName);
					StepMeta stepMeta = transMeta.getStepByGraphId(id);
					Map<String, String[]> p1 = new HashMap(p);
					if (optionValue.length() == 0) {
						p1.put("validations", null);
					} else {
						String[] validations = optionValue.split("---");
						p1.put("validations", validations);
					}
					stepMeta.setInfo(p1, id, transMeta.getDatabases());
					int stepIndex = transMeta.getStepIndex(elementName);
					if (!newName.equals(elementName)) {
						newName = getStepName(transMeta, newName);
						stepMeta.setName(newName);
					}
					transMeta.setStep(stepIndex, stepMeta);

				} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB
						.equals(roType)) {
					JobMeta jobMeta = jobs.getJobMetaByName(rep, d, roName);
					JobEntryCopy jobEntryCopy = jobMeta
							.findJobEntry(elementName);
					jobEntryCopy.setInfo(p, id, jobMeta.getDatabases());
					int jobEntryIndex = jobMeta.getJobEntryIndex(elementName);
					if (!newName.equals(elementName)) {
						newName = getJobEntryName(jobMeta, newName);
						jobEntryCopy.setName(newName);
					}
					jobMeta.setJobEntry(jobEntryIndex, jobEntryCopy);
				}
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"validatorElementSubmit"));
			isSuccess = false;
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", (isSuccess) ? "提交修改成功！" : "提交修改失败！", true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("oldName", elementName);
			rtn.put("newName", newName);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"validatorElementSubmit"));
		}
	}

}
