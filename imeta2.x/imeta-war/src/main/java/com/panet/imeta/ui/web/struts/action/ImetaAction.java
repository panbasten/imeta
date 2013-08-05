package com.panet.imeta.ui.web.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.panet.iform.core.base.ButtonDataMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.messageBox.MessageBoxMeta;
import com.panet.iform.forms.tree.TreeMeta;
import com.panet.iform.forms.tree.TreeNodeDataMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Props;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
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
import com.panet.imeta.repository.RepositoryUtil;
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
import com.panet.imeta.ui.dialog.AnalyseImpactProgressDialog;
import com.panet.imeta.ui.dialog.CheckTransProgressDialog;
import com.panet.imeta.ui.dialog.DatabaseExplorerDialog;
import com.panet.imeta.ui.dialog.DatabaseSettingDialog;
import com.panet.imeta.ui.dialog.EntryPluginDialog;
import com.panet.imeta.ui.dialog.JobListDialog;
import com.panet.imeta.ui.dialog.JobRunSettingDialog;
import com.panet.imeta.ui.dialog.JobStartDialog;
import com.panet.imeta.ui.dialog.LogDialog;
import com.panet.imeta.ui.dialog.RepDialog;
import com.panet.imeta.ui.dialog.RepDirectoryDialog;
import com.panet.imeta.ui.dialog.RepExplorerDialog;
import com.panet.imeta.ui.dialog.StepFieldDialog;
import com.panet.imeta.ui.dialog.StepPluginDialog;
import com.panet.imeta.ui.dialog.TransRunSettingDialog;
import com.panet.imeta.ui.dialog.UserDialog;
import com.panet.imeta.ui.dialog.XMLFileUploadDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.meta.ImetaUIUtils;
import com.panet.imeta.ui.meta.XMLFile;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaStepDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;
import com.panet.imeta.ui.service.impl.ImetaJobService;

/**
 * iMeta模块控制类
 * 
 * @author Peter Pan
 * 
 */
@Controller("imeta.ui.imetaAction")
public class ImetaAction extends ActionSupport {

	private static final long serialVersionUID = -3384194945204610401L;

	public static final String LOGO_TEXT = "中科软科技股份有限公司";

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
	 * 载入日志
	 */
	public void loadLog() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			LogDialog log = new LogDialog();

			response.getWriter().write(log.open().toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "loadLog"));
		}
	}

	/**
	 * 创建日志头部
	 */
	public void loadLogBar() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			JSONArray ro = new JSONArray();

			JSONObject logo = new JSONObject();
			logo.put("createTag", "<div class='logo'></div>");
			logo.put("html",
					"<img src='framework/images/logo.png' style='margin-bottom:-3px;'/>"
							+ LOGO_TEXT + " 版权所有&copy;");
			ro.add(logo);

			UserInfo user = getLoginUser();
			JSONObject userInfo = new JSONObject();
			userInfo.put("createTag", "<div class='user_info_div'></div>");
			userInfo.put("html", "当前登录用户：<font style='color:black;'>"
					+ user.getName()
					+ "</font>　　身份：<font style='color:black;'>"
					+ UserDialog.getUserTypeLabel(user.getAccountType())
					+ "</font>");
			ro.add(userInfo);

			JSONObject logDiv = new JSONObject();
			logDiv
					.put(
							"createTag",
							"<div class='log_div'><span class='log_span'><button id='logCollapse' class='logCollapse' title='隐藏＼打开' closed='on'></button></span></div>");
			ro.add(logDiv);

			response.getWriter().write(ro.toString());
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "loadLog"));
		}
	}

	/**
	 * 载入工具箱
	 */
	public void loadToolbarTab() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JSONArray ro = new JSONArray();
			JSONObject joHeader = new JSONObject();
			joHeader.put("createTag",
					"<div id='imb-tl' class='toolbar-title'></div>");
			joHeader.put("html", Messages.getString("IMeta.ImetaBar.Title"));
			JSONArray joHeader_items = new JSONArray();
			JSONObject joHeader_items_item1 = new JSONObject();
			joHeader_items_item1.put("createTag",
					"<div id='imb-min' class='toolbar-min'></div>");
			joHeader_items.add(joHeader_items_item1);
			joHeader.put("items", joHeader_items);
			ro.add(joHeader);

			// 加入转换的工具箱
			ro.addAll(trans.getTransStepTab());

			// 加入任务的工具箱
			ro.addAll(jobs.getJobEntryTab());

			response.getWriter().write(ro.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"loadToolbarTab"));
		}
	}

	/**
	 * 编辑节点
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

			// 如果是转换
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getEditTransMetaByName(delegates
						.getRep(getLoginUser()), Long.valueOf(directoryId),
						roName);
				StepMeta stepMeta = transMeta.getStepByGraphId(id);
				if (stepMeta == null) {
					stepMeta = newStep(transMeta, stepType);
				}
				String dialog = steps.editStep(id, transMeta, stepMeta)
						.toString();
				response.getWriter().write(dialog);
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(delegates
						.getRep(getLoginUser()), Long.valueOf(directoryId),
						roName);

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
	 * 通过Name得到一个对象的各类元素的树型展现
	 */
	public void loadRepositoryObjectByName() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String roId = request.getParameter("roId");
			String directoryId = request.getParameter("directoryId");

			StringBuffer ro = new StringBuffer();
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				TransMeta transMeta = trans.getTransMetaByName(delegates
						.getRep(getLoginUser()), Long.valueOf(directoryId),
						roName);
				ro.append(transMeta.getTransMetaTreeSegment(roId));

			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getJobMetaByName(delegates
						.getRep(getLoginUser()), Long.valueOf(directoryId),
						roName);
				ro.append(jobMeta.getJobMetaTreeSegment(roId));
			}

			response.getWriter().write(ro.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"loadRepositoryObjectByName"));
		}
	}

	/**
	 * 设置转换
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
			UserInfo user = getLoginUser();
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
					// 如果是该用户操作任务，清除
					if (jobMeta.getModifiedUser().equals(user.getLogin())) {
						jobs.clearEditJobMetaByName(rep, d, roName);
						jo = jobs.getSettingContent(rep, d, roName, winId);
						jo.put("success", true);
					} else {
						jo = new JSONObject();
						jo.put("success", false);
						jo.put("message", "用户【" + jobMeta.getModifiedUser()
								+ "】正在编辑任务，不能进行设置");
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
		Repository rep = getLoginUser().getRep();
		try {
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {

				TransMeta transMeta = trans.getTransMetaByName(delegates
						.getRep(getLoginUser()), d, roName);
				transMeta.setModifiedDate(new Date());
				transMeta.setModifiedUser(getLoginUser().getLogin());

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
						.getRep(getLoginUser()), d, roName);
				jobMeta.setModifiedDate(new Date());
				jobMeta.setModifiedUser(getLoginUser().getLogin());

				if (!newName.equals(roName)) {
					if (ImetaUIUtils.checkJobMetaByName(rep, d, newName, jobs) == null) {
						jobMeta.setInfo(request.getParameterMap(), id);
						jobMeta.setName(newName);
						jobMeta.saveRep(rep);
					} else {
						isSuccess = false;
						ms = "新修改的任务名称冲突，请更换名称！";
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

	private DatabaseSettingDialog getDatabaseDialog(int databaseId)
			throws KettleException {
		DatabaseMeta databaseMeta;
		Repository rep = getLoginUser().getRep();
		if (databaseId == 0) {
			databaseMeta = new DatabaseMeta();
		} else {
			databaseMeta = RepositoryUtil.loadDatabaseMeta(delegates
					.getRep(getLoginUser()), databaseId);
		}
		return new DatabaseSettingDialog(databaseMeta, rep);
	}

	/**
	 * 设置要素
	 */
	public void settingElement() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String elementType = request.getParameter("elementType");
			String elementId = request.getParameter("elementId");

			if (RepositoryObject.STRING_ELEMENT_TYPE_DATABASE
					.equals(elementType)) {
				DatabaseSettingDialog dialog = getDatabaseDialog(Integer
						.parseInt(elementId));
				response.getWriter().write(dialog.open().toString());
			} else if (RepositoryObject.STRING_ELEMENT_TYPE_STEP
					.equals(elementType)
					|| RepositoryObject.STRING_ELEMENT_TYPE_JOBENTRY
							.equals(elementType)) {
				editElement();
			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingElement"));
		}
	}

	/**
	 * 创建数据库
	 */
	public void createDatabase() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			DatabaseSettingDialog dialog = getDatabaseDialog(0);
			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"createDatabase"));
		}
	}

	/**
	 * 创建或更新数据库
	 */
	@SuppressWarnings("unchecked")
	public void createOrUpdateDatabase() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			Repository rep = getLoginUser().getRep();
			String databaseId = request.getParameter("databaseId");
			int databaseId_i = 0;
			try {
				databaseId_i = Integer.valueOf(databaseId);
			} catch (Exception e) {
				throw new ImetaException("添加修改数据库出现异常");
			}

			boolean isSuccess = false;
			String ms = "";
			String name = request.getParameter("database_" + databaseId_i
					+ ".connectionName");
			if (StringUtils.isNotEmpty(name)) {
				DatabaseSettingDialog dialog = getDatabaseDialog(Integer
						.valueOf(databaseId));
				isSuccess = true;
				// 更新数据库
				if (databaseId_i > 0) {
					ms = dialog.save(request.getParameterMap());
				}
				// 插入数据库
				else {
					String[] dbNames = rep.getDatabaseNames();
					if (dbNames != null && dbNames.length > 0) {
						for (String dbName : dbNames) {
							if (name.equals(dbName)) {
								isSuccess = false;
								break;
							}
						}
					}
					if (isSuccess) {
						ms = dialog.save(request.getParameterMap());
					} else {
						ms = "数据库名称冲突，请更换名称";
					}
				}

			} else {
				isSuccess = false;
				ms = "新建数据库名称不能为空";
			}

			// 更新缓存

			DatabaseMeta databaseMeta = RepositoryUtil.loadDatabaseMeta(rep,
					rep.getDatabaseID(name));
			trans.addOrReplaceDatabase(databaseMeta);
			jobs.addOrReplaceDatabases(databaseMeta);

			MessageBoxMeta box = new MessageBoxMeta(null, null, null, "操作结果",
					ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject rtn = box.getFormJo();
			rtn.put("success", isSuccess);
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"createOrUpdateDatabase"));
		}
	}

	/**
	 * 设置数据库配置字段
	 */
	public void settingDatabaseFields() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String databaseId = request.getParameter("databaseId");
			String connType = request.getParameter("connType");
			String access = request.getParameter("access");

			DatabaseSettingDialog dialog = getDatabaseDialog(Integer
					.parseInt(databaseId));
			response.getWriter().write(
					dialog.getFields(Integer.valueOf(connType),
							Integer.valueOf(access)).toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingDatabaseFields"));
		}
	}

	/**
	 * 探测数据库
	 */
	@SuppressWarnings("unchecked")
	public void testDatabase() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			String databaseId = request.getParameter("databaseId");
			DatabaseSettingDialog dialog = getDatabaseDialog(Integer
					.parseInt(databaseId));
			String ms = dialog.test(request.getParameterMap());

			MessageBoxMeta box = new MessageBoxMeta(null, null, null, "测试结果",
					ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"testDatabase"));
		}
	}

	/**
	 * 浏览数据库
	 */
	@SuppressWarnings("unchecked")
	public void exploreDatabase() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			Repository rep = getLoginUser().getRep();

			String databaseId = request.getParameter("databaseId");
			DatabaseSettingDialog settingDialog = getDatabaseDialog(Integer
					.parseInt(databaseId));
			DatabaseMeta databaseMeta = new DatabaseMeta();
			settingDialog.getInfo(databaseMeta, request.getParameterMap());
			databaseMeta.setID(settingDialog.getDatabaseMeta().getID());

			if (databaseMeta.getAccessType() != DatabaseMeta.TYPE_ACCESS_PLUGIN) {
				DatabaseExplorerDialog explorerDialog = new DatabaseExplorerDialog(
						databaseMeta, rep);
				response.getWriter().write(explorerDialog.open().toString());
			} else {
				MessageBoxMeta box = new MessageBoxMeta(null, null, null,
						"对不起", "对不起，针对该数据库的浏览还没有实现！", true);
				box.setOraWidth(350);
				box.setOraHeight(160);

				response.getWriter().write(box.getFormJo().toString());
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"exploreDatabase"));
		}
	}

	public void closeFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String roName = request.getParameter("roName");
			String roType = request.getParameter("roType");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			Repository rep = getLoginUser().getRep();
			if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
					.equals(roType)) {
				// 编辑用户是当前用户时，删除
				TransMeta transMeta = trans.getEditTransMetaByName(rep, d,
						roName);
				if (transMeta != null) {
					UserInfo user = getLoginUser();
					if (user != null
							&& user.getLogin().equals(
									transMeta.getModifiedUser())) {
						trans.clearEditTransMetaByName(rep, d, roName);
					}
				}
			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getEditJobMetaByName(rep, d, roName);
				if (jobMeta != null) {
					UserInfo user = getLoginUser();
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
		if (delegates.getRep(getLoginUser()) == null || importfile) {
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
				UserInfo user = getLoginUser();
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
	 * 编辑目录
	 * 
	 * @throws KettleException
	 * @throws ImetaException
	 */
	public void editDirectory() throws KettleException, ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(getLoginUser());

		boolean isSuccess = false;
		String ms = "";

		try {
			String directoryId = request.getParameter("directoryId");
			String directoryName = request.getParameter("directoryName");
			String newDirectoryName = request.getParameter("newDirectoryName");
			long directoryId_l = -1;
			try {
				directoryId_l = Long.valueOf(directoryId);
			} catch (Exception e) {
				directoryId_l = -1;
			}
			// 如果名称未改变
			if (directoryName.equals(newDirectoryName)) {
				isSuccess = true;
				ms = "该目录成功更名为【" + newDirectoryName + "】！";
			} else {
				// 如果新名称不为空
				if (StringUtils.isNotEmpty(newDirectoryName)) {
					if (directoryId_l == 0) {
						isSuccess = false;
						ms = "根目录不能更名！";
					} else if (directoryId_l > 0) {
						rep.renameDirectory(directoryId_l, newDirectoryName);
						rep.unlockRepository();
						rep.commit();
						isSuccess = true;
						ms = "该目录成功更名为【" + newDirectoryName + "】！";
					}
				} else {
					isSuccess = false;
					ms = "新目录名称不能为空！";
				}
			}
		} catch (KettleDatabaseException e) {
			rep.rollback();
			throw new ImetaException(e);
		} finally {
			rep.unlockRepository();
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 删除目录
	 * 
	 * @throws KettleException
	 * @throws ImetaException
	 */
	public void deleteDirectory() throws KettleException, ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(getLoginUser());

		boolean isSuccess = false;
		String ms = "";

		try {
			String directoryId = request.getParameter("directoryId");
			long directoryId_l = -1;
			try {
				directoryId_l = Long.valueOf(directoryId);
			} catch (Exception e) {
				directoryId_l = -1;
			}

			if (directoryId_l == 0) {
				isSuccess = false;
				ms = "根目录不能删除！";
			} else if (directoryId_l > 0) {
				isSuccess = true;
				ms = "删除目录成功！";

				// 删除该目录下的所有转换和任务
				// 1.得到转换目录名s
				String[] transNames = rep.getTransformationNames(directoryId_l);
				// 2.删除转换
				if (transNames != null && transNames.length > 0) {
					long id_transformation;
					for (String transName : transNames) {
						id_transformation = rep.getTransformationID(transName,
								directoryId_l);
						rep.delAllFromTrans(id_transformation);
					}
				}
				// 3.得到任务目录名s
				String[] jobNames = rep.getJobNames(directoryId_l);
				// 4.删除任务
				if (jobNames != null && jobNames.length > 0) {
					long id_job;
					for (String jobName : jobNames) {
						id_job = rep.getJobID(jobName, directoryId_l);
						rep.delAllFromJob(id_job);
					}
				}

				// 删除目录
				rep.deleteDirectory(directoryId_l);

				rep.unlockRepository();
				rep.commit();
			} else {
				isSuccess = false;
				ms = "目录不存在！";
			}

		} catch (KettleDatabaseException e) {
			rep.rollback();
			throw new ImetaException(e);
		} finally {
			rep.unlockRepository();
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	/**
	 * 添加目录
	 * 
	 * @throws KettleException
	 * @throws ImetaFormException
	 * @throws ImetaException
	 */
	public void addDirectory() throws KettleException, ImetaFormException,
			ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(getLoginUser());

		boolean isSuccess = false;
		String ms = "";

		try {
			String directoryName = request.getParameter("directoryName");
			String directoryId = request.getParameter("directoryId");
			long directoryId_l = -1;
			try {
				directoryId_l = Long.valueOf(directoryId);
			} catch (Exception e) {
				directoryId_l = -1;
			}

			if (StringUtils.isNotEmpty(directoryName)) {
				isSuccess = true;
				ms = "添加目录成功！";
				String[] names = rep.getDirectoryNames(directoryId_l);
				if (names != null && names.length > 0) {
					for (String name : names) {
						if (directoryName.equals(name)) {
							isSuccess = false;
							ms = "目录名称冲突，请更换名称";
						}
					}
				}
				if (isSuccess) {
					RepositoryDirectory rd = new RepositoryDirectory();
					rd.setDirectoryName(directoryName);
					rep.insertDirectory(directoryId_l, rd);
				}
			} else {
				isSuccess = false;
				ms = "目录名称不能为空！";
			}

			rep.unlockRepository();

			// Perform a commit!
			rep.commit();
		} catch (KettleDatabaseException e) {
			rep.rollback();
			throw new ImetaException(e);
		} finally {
			rep.unlockRepository();
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(isSuccess) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", ms, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());
		} catch (Exception e) {
			throw new ImetaException(e);
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
		Repository rep = delegates.getRep(getLoginUser());

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

		Repository rep = delegates.getRep(getLoginUser());
		UserInfo user = getLoginUser();
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
							ms = "当前用户不具有编辑该任务的权限";
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
							ms = "当前用户不具有编辑该任务的权限";
						} else if (jobMeta.getJobstatus() != JobMeta.JOBSTATUS_PRODUCTION) {
							jobMeta.setModifiedUser(user.getLogin());
							jobs.putEditJobMeta(rep, jobMeta);
							isSuccess = true;
							ms = "修改为编辑状态";
						} else {
							isSuccess = false;
							ms = "任务【" + jobMeta.getName()
									+ "】正处在生产状态，请将该任务先设置为草稿状态再进行编辑";
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
		Repository rep = delegates.getRep(getLoginUser());
		UserInfo user = getLoginUser();
		return ImetaUIUtils.drawRepObjectByNameAndType(directoryId, roName,
				roType, exeditable, rep, user, trans, jobs);
	}

	/**
	 * 载入探测资源库对象
	 */
	public void loadRepExplorerObjects() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JSONArray repositoryObjects = new JSONArray();

			String id = request.getParameter("id");
			String parentId = request.getParameter("parentId");
			String showTypes = request.getParameter("showTypes");
			String modify = request.getParameter("modify");
			String customOkFn = request.getParameter("customOkFn");

			// 0表示根目录
			Repository rep = delegates.getRep(getLoginUser());

			RepExplorerDialog re = new RepExplorerDialog(rep, id);
			re.setShowTypes(Const.NVL(showTypes, "all"));
			re.setModify("true".equalsIgnoreCase(modify));
			re.setCustomOkFn(Const.NVL(customOkFn, ""));
			re.setParentId(Const.NVL(parentId, ""));
			re.createRepTree();

			repositoryObjects.add(re.getDbTree().getFormJa().get(0));

			response.getWriter().write(repositoryObjects.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"loadRepositoryObjects"));
		}
	}

	/**
	 * 载入探测资源库目录对象
	 */
	public void loadRepDirectoryObjects() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JSONArray repositoryObjects = new JSONArray();

			String id = request.getParameter("id");
			String parentId = request.getParameter("parentId");
			String modify = request.getParameter("modify");
			String customOkFn = request.getParameter("customOkFn");

			// 0表示根目录
			Repository rep = delegates.getRep(getLoginUser());

			RepDirectoryDialog re = new RepDirectoryDialog(rep, id);
			re.setModify("true".equalsIgnoreCase(modify));
			re.setCustomOkFn(Const.NVL(customOkFn, ""));
			re.setParentId(Const.NVL(parentId, ""));
			re.createRepTree();

			repositoryObjects.add(re.getDbTree().getFormJa().get(0));

			response.getWriter().write(repositoryObjects.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"loadRepositoryObjects"));
		}
	}

	/**
	 * 清除空的节点
	 * 
	 * @param tm
	 */
	private void clearEmptyNode(TreeNodeDataMeta tm) {
		if (tm.getSubNode() != null) {
			Iterator<TreeNodeDataMeta> iter = tm.getSubNode().iterator();
			while (iter.hasNext()) {
				TreeNodeDataMeta sub = iter.next();
				if ("dir".equals(sub.getNodeExtends().get("type"))) {
					clearEmptyNode(sub);
					if (isEmptySubNode(sub))
						iter.remove();
				}

			}
		}
	}

	private boolean isEmptySubNode(TreeNodeDataMeta tm) {
		return (tm.getSubNode() == null || tm.getSubNode().size() == 0);
	}

	/**
	 * 载入对象内容树
	 */
	public void loadRepositoryObjects() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JSONArray repositoryObjects = new JSONArray();

			// 0表示根目录
			Repository rep = delegates.getRep(getLoginUser());
			RepositoryDirectory directory = new RepositoryDirectory(rep);
			TreeMeta transRoot = new TreeMeta(
					new TreeNodeDataMeta[] { createRepositoryObjectTrees(
							directory,
							RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION,
							true) });
			transRoot.setRootTree(true);
			clearEmptyNode(transRoot.getTreeDataMetas().get(0));
			repositoryObjects.add(transRoot.getFormJa().get(0));

			TreeMeta jobRoot = new TreeMeta(
					new TreeNodeDataMeta[] { createRepositoryObjectTrees(
							directory, RepositoryObject.STRING_OBJECT_TYPE_JOB,
							true) });
			jobRoot.setRootTree(true);
			clearEmptyNode(jobRoot.getTreeDataMetas().get(0));
			repositoryObjects.add(jobRoot.getFormJa().get(0));

			response.getWriter().write(repositoryObjects.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"loadRepositoryObjects"));
		}
	}

	/**
	 * 创建一个json格式的对象视图树
	 * 
	 * @param l
	 *            对象列表
	 * @param objectType
	 *            对象类型
	 * @return
	 * @throws KettleException
	 */
	private TreeNodeDataMeta createRepositoryObjectTrees(
			RepositoryDirectory directory, String objectType, boolean isRoot)
			throws ImetaFormException, KettleException {

		Repository rep = delegates.getRep(getLoginUser());
		UserInfo user = getLoginUser();
		long directoryId = directory.getID();
		TreeNodeDataMeta nodes_0 = null;
		if (isRoot) {
			nodes_0 = new TreeNodeDataMeta(objectType, Messages
					.getString("IMeta.ObjectView." + objectType + ".CSS"),
					Messages.getString("IMeta.ObjectView." + objectType
							+ ".Title"), Messages.getString("IMeta.ObjectView."
							+ objectType + ".Title"), false, false);
		} else {
			String directoryName = directory.getDirectoryName();
			nodes_0 = new TreeNodeDataMeta(objectType, null, directoryName,
					directoryName, true, false);
		}
		nodes_0.putNodeExtend("directoryId", directoryId);
		nodes_0.putNodeExtend("directoryName", directory.getDirectoryName());
		nodes_0.putNodeExtend("directoryPath", directory.getPath());
		nodes_0.putNodeExtend("type", "dir");

		// 添加目录子目录
		List<RepositoryDirectory> children = directory.getChildren();
		if (children != null && children.size() > 0) {
			for (RepositoryDirectory sub : children) {
				nodes_0.putSubNode(createRepositoryObjectTrees(sub, objectType,
						false));
			}
		}
		// 添加目录对象
		List<RepositoryObject> repositoryObjects = null;
		if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION
				.equals(objectType)) {
			repositoryObjects = rep.getTransformationObjects(directoryId);

		} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(objectType)) {
			repositoryObjects = rep.getJobObjects(directoryId);

		}
		Iterator<RepositoryObject> iter = repositoryObjects.iterator();
		RepositoryObject ro;
		String treeId;
		while (iter.hasNext()) {
			ro = iter.next();
			if (UserInfo.STRING_USER_TYPE_EDITOR.equalsIgnoreCase(user
					.getAccountType())
					&& !ro.getCreatedUser().equals(user.getLogin())) {
				continue;
			}
			treeId = ro.getObjectType() + ro.getRepId();
			TreeNodeDataMeta nodes_1 = new TreeNodeDataMeta(treeId, "file", ro
					.getName(), ro.getName(), true, false);
			nodes_1.putNodeExtend("treeId", treeId);
			nodes_1.putNodeExtend("objectType", ro.getObjectType());
			nodes_1.putNodeExtend("objectName", ro.getName());
			nodes_1.putNodeExtend("load", "n");
			nodes_1.putNodeExtend("directoryId", directoryId);
			nodes_1
					.putNodeExtend("directoryName", directory
							.getDirectoryName());
			nodes_1.putNodeExtend("directoryPath", directory.getPath());
			nodes_1.putNodeExtend("type", "file");

			ButtonDataMeta openBtn = new ButtonDataMeta(null, null,
					new String[] { "file-open" }, null, "打开", null);

			if (!UserInfo.STRING_USER_TYPE_OPERATOR.equalsIgnoreCase(user
					.getAccountType())) {
				ButtonDataMeta settingBtn = new ButtonDataMeta(null, null,
						new String[] { "file-setting" }, null, "设置", null);

				nodes_1
						.putButtons(new ButtonDataMeta[] { openBtn, settingBtn });
			} else {
				nodes_1.putButtons(new ButtonDataMeta[] { openBtn });
			}

			nodes_0.putSubNode(nodes_1);
		}

		try {
			return nodes_0;
		} catch (Exception ex) {
			throw new ImetaFormException("生成对象树出错", ex);
		}
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
				String key = Encr.encryptPassword(getLoginUser().getLogin()
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
		Repository rep = getLoginUser().getRep();
		String newCanvas = null;
		UserInfo user = getLoginUser();
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
					ms = "另存为的任务名称冲突，请更换名称！";
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
		Repository rep = getLoginUser().getRep();
		if (RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION.equals(roType)) {
			TransMeta transMeta = modifyTrans(d, canvasJo);
			transMeta.setModifiedDate(new Date());
			transMeta.setModifiedUser(getLoginUser().getLogin());
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
			jobMeta.setModifiedUser(getLoginUser().getLogin());
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
			Repository rep = getLoginUser().getRep();
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
			Repository rep = getLoginUser().getRep();
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
	 * 验证转换
	 * 
	 * @throws ImetaException
	 */
	public void checkTrans() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		try {
			String rootId = request.getParameter("taskId");
			String objectName = request.getParameter("objectName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			TransMeta transMeta = trans.getEditTransMetaByName(delegates
					.getRep(getLoginUser()), d, objectName);
			CheckTransProgressDialog dialog = new CheckTransProgressDialog(
					transMeta);
			dialog.setRootId(rootId);
			response.getWriter().write(dialog.open().toString());
		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"checkTrans"));
		}
	}

	/**
	 * 分析转换
	 * 
	 * @throws ImetaException
	 */
	public void analyseImpactTrans() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		try {
			String rootId = request.getParameter("taskId");
			String objectName = request.getParameter("objectName");
			String directoryId = request.getParameter("directoryId");
			long d = Long.valueOf(directoryId);
			TransMeta transMeta = trans.getEditTransMetaByName(delegates
					.getRep(getLoginUser()), d, objectName);
			AnalyseImpactProgressDialog dialog = new AnalyseImpactProgressDialog(
					transMeta);
			dialog.setRootId(rootId);
			response.getWriter().write(dialog.open().toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"analyseImpactTrans"));
		}
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
			Repository rep = delegates.getRep(getLoginUser());
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
		Repository rep = delegates.getRep(getLoginUser());
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
				ms = "任务暂停/恢复操作失败！";
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

	public void jobListStop() {
		boolean isSuccess = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(getLoginUser());
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
				ms = "任务停止成功！";
				isSuccess = true;
			} else {
				ms = "任务未运行或者已经停止运行！";
				isSuccess = false;
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "stop"));
			ms = "任务停止操作失败！";
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
		Repository rep = delegates.getRep(getLoginUser());
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
					ms = "任务停止成功！";
					isSuccess = true;
				} else {
					ms = "任务未运行或者已经停止运行！";
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
				ms = "任务停止操作失败！";
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

		UserInfo user = getLoginUser();

		String id = request.getParameter("id");

		Repository rep = delegates.getRep(getLoginUser());
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

	// 获得任务列表的窗口
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

	public void openAutoJobSetting() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");

			String dirId = request.getParameter("dirId");
			String jobName = request.getParameter("jobName");

			JobMeta jobMeta = jobs.getJobMetaByName(getLoginUser().getRep(),
					Long.valueOf(dirId), jobName);

			String rootId = "auto_run_job_setting_" + jobMeta.getID();

			Repository rep = getLoginUser().getRep();

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
						ImetaJobService.AUTO_RUN_TYPE_DISAUTO, getLoginUser()
								.getLogin(), new Date());
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

	public void submitAutoJobSetting() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html; charset=UTF-8");

		boolean isSuccess = true;
		String ms = "提交自动运行任务成功!";

		String id = request.getParameter("id");
		Long jobId = Long.valueOf(request.getParameter("jobId"));

		Repository rep = getLoginUser().getRep();

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
			ms = "提交自动运行任务失败！";
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
	 * 任务启动管理
	 */
	public void jobStart() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JobStartDialog dialog = new JobStartDialog(getLoginUser().getRep(),
					jobs.getActiveJobsMap(), jobs.getAutoRunJobs());

			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "jobStart"));
		}

	}

	/**
	 * 设置任务启动配置
	 */
	public void settingJobStart() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		boolean isSuccess = true;
		String ms = "提交修改成功!";

		try {
			String id = request.getParameter("id");
			Repository rep = getLoginUser().getRep();

			// 判断Auto表中的任务
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

			// 判断新增任务
			Long[] jobIds = rep.getAllJobIDs();
			if (jobIds != null) {
				for (Long l : jobIds) {
					if (!autoRunJobs.containsKey(rep.getName() + "." + l)) {
						String hasAuto = request.getParameter(id
								+ "_autostart." + l);
						if (hasAuto != null) {
							rep.insertAutoStartJob(l,
									ImetaJobService.AUTO_RUN_TYPE_AUTO,
									getLoginUser().getLogin(), new Date());
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
	 * 载入登录准备信息
	 * 
	 * @throws ImetaException
	 */
	public void perpareLogin() throws ImetaException {

		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html; charset=UTF-8");
			JSONObject rtn = new JSONObject();

			rtn.put("reps", delegates.getReps());

			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"perpareLogin"));
		}
	}

	/**
	 * 载入登录准备信息
	 * 
	 * @throws ImetaException
	 */
	public void login() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		JSONObject rtn = new JSONObject();
		String msg = "登录成功";
		boolean success = true;
		try {
			response.setContentType("text/html; charset=UTF-8");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String repository = request.getParameter("repository");
			request.getSession(false).removeAttribute(UserInfo.STRING_USERINFO);
			Repository rep = delegates.getRep(repository);
			UserInfo user = new UserInfo(rep, username, password);
			request.getSession(false).setAttribute(UserInfo.STRING_USERINFO,
					user);
			rtn.put("username", username);
			rtn.put("redirect", request.getContextPath() + "/portal");
		} catch (KettleException e) {
			msg = e.getMessage();
			success = false;
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "login"));
		}
		try {

			rtn.put("msg", msg);
			rtn.put("success", success);
			response.getWriter().write(rtn.toString());

		} catch (IOException e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "login"));
		}
	}

	private UserInfo getLoginUser() {
		return UserInfo.getLoginUser();

	}

	// 获得创建新用户的窗口
	public void createUser() {

		try {
			Repository rep = getLoginUser().getRep();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				UserDialog userDialog = new UserDialog(rep, null,
						UserDialog.USER_ADD);

				response.getWriter().write(userDialog.open().toString());
			}

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"createUser"));
		}

	}

	// 在数据库中保存用户
	public void saveUser() throws ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String msg = "保存用户出现错误。";

		boolean success = true;
		try {
			response.setContentType("text/html; charset=UTF-8");
			String login = request.getParameter(UserDialog.USER_ADD + ".login");
			String password = request.getParameter(UserDialog.USER_ADD
					+ ".password");
			String username = request.getParameter(UserDialog.USER_ADD
					+ ".username");
			String accountType = request.getParameter(UserDialog.USER_ADD
					+ ".accountType");
			String description = request.getParameter(UserDialog.USER_ADD
					+ ".description");
			Repository rep = delegates.getRep(getLoginUser());
			ArrayList<String> logins = (ArrayList<String>) rep.getLogins();

			for (int i = 0; i < logins.size(); i++) {
				if (login.equalsIgnoreCase(logins.get(i))) {
					msg = "该用户已存在";
					success = false;
					break;
				}
			}

			if (success) {
				rep.saveUser(login, Encr.encryptPassword(password), username,
						accountType, description);
				msg = "添加成功";
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "saveUser"));
		}

		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(success) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", msg, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject jo = box.getFormJo();
			jo.put("success", success);
			response.getWriter().write(jo.toString());

		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "saveUser"));
		}
	}

	// 获得编辑用户的窗口
	public void toEditCurrentUser() {
		try {
			Repository rep = getLoginUser().getRep();
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				UserInfo userInfo = (UserInfo) request.getSession(false)
						.getAttribute(UserInfo.STRING_USERINFO);
				UserInfo user = rep.getUserById(userInfo.getID());
				UserDialog userDialog = new UserDialog(rep, user,
						UserDialog.USER_MODIFY);
				response.getWriter().write(userDialog.open().toString());
			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"toEditCurrentUser"));
		}
	}

	// 在数据库中修改用户
	public void editCurrentUser() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String msg = "修改成功";
		boolean success = true;
		try {
			response.setContentType("text/html; charset=UTF-8");
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(UserInfo.STRING_USERINFO);
			String id_user = new Long(userInfo.getID()).toString();
			long userid = Long.parseLong(new String(id_user));
			String oldpassword = request.getParameter(UserDialog.USER_MODIFY
					+ ".oldpassword");
			String password = Encr.encryptPassword(request
					.getParameter(UserDialog.USER_MODIFY + ".password"));
			String username = request.getParameter(UserDialog.USER_MODIFY
					+ ".username");
			String accountType = request.getParameter(UserDialog.USER_MODIFY
					+ ".accountType");
			String description = request.getParameter(UserDialog.USER_MODIFY
					+ ".description");

			Repository rep = delegates.getRep(getLoginUser());
			UserInfo user = rep.getUserById(userid);

			if (!user.getPassword().equals(oldpassword)) {
				msg = "旧密码错误";
				success = false;
			}

			if (success) {
				rep.editUser(userid, password, username, accountType,
						description);
			}
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"editCurrentUser"));
		}
		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(success) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", msg, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject jo = box.getFormJo();
			jo.put("success", success);
			response.getWriter().write(jo.toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"editCurrentUser"));
		}
	}

	// 获得删除用户的窗口
	public void toDeleteUser() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			Repository rep = delegates.getRep(getLoginUser());
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				UserDialog userDialog = new UserDialog(rep, null,
						UserDialog.USER_DEL);
				response.getWriter().write(userDialog.open().toString());

			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"toDeleteUser"));
		}
	}

	// 在数据库中删除用户
	public void deleteUser() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String msg = "";
		boolean success = true;
		try {
			String id = request.getParameter("id");
			response.setContentType("text/html; charset=UTF-8");
			Repository rep = delegates.getRep(getLoginUser());
			ArrayList<Long> id_users = (ArrayList<Long>) rep.getId_users();
			int count = 0;
			for (int i = 0; i < id_users.size(); i++) {
				String checkedId = id + ".checkbox." + id_users.get(i);
				if (request.getParameter(checkedId) != null) {
					rep.delUser(id_users.get(i));
					count++;
				}
			}
			if (count == 0) {
				msg = "请选择要删除的用户";
				success = false;
			} else {
				msg = "成功删除" + count + "名用户";
			}

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"deleteUser"));
		}
		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(success) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", msg, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			JSONObject jo = box.getFormJo();
			jo.put("success", success);

			response.getWriter().write(jo.toString());

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"deleteUser"));
		}
	}

	public void connectRep() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			// Repository rep = getLoginUser().getRep();
			RepDialog repDialog = new RepDialog(delegates.getReps(), null, null);

			response.getWriter().write(repDialog.open().toString());

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"connectRep"));
		}
	}

	public void connectRepSubmit() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String msg = "连接资源库成功";
		boolean success = true;
		try {
			response.setContentType("text/html; charset=UTF-8");
			String username = request.getParameter(RepDialog.REP_PREFIX_ID
					+ ".username");
			String password = request.getParameter(RepDialog.REP_PREFIX_ID
					+ ".password");
			String repository = request.getParameter(RepDialog.REP_PREFIX_ID
					+ ".repository");

			Repository rep = delegates.getRep(repository);
			UserInfo user = new UserInfo(rep, username, password);
			request.getSession(false).setAttribute(UserInfo.STRING_USERINFO,
					user);
		} catch (Exception e) {
			msg = e.getMessage();
			success = false;
		}
		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(success) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", msg, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"connectRepSubmit"));
		}
	}

	public void disConnectRep() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		JSONObject rtn = new JSONObject();
		String msg = "断开成功";
		boolean success = true;
		try {
			response.setContentType("text/html; charset=UTF-8");
			UserInfo user = (UserInfo) request.getSession(false).getAttribute(
					UserInfo.STRING_USERINFO);
			if (user.getRep() == null) {
				msg = "资源库已断开";
			}
			user.setRep(null);
			request.getSession(false).setAttribute(UserInfo.STRING_USERINFO,
					user);
			rtn.put("username", user.getLogin());
			rtn.put("redirect", request.getContextPath() + "/imeta");
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"disConnectRep"));
		}
		try {
			MessageBoxMeta box = new MessageBoxMeta(null, null,
					(success) ? null : MessageBoxMeta.MESSAGEBOX_ICON_ERROR,
					"运行结果", msg, true);
			box.setOraWidth(350);
			box.setOraHeight(160);
			response.getWriter().write(box.getFormJo().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"disConnectRep"));
		}
	}

	public void getDirectory() {
		try {
			Repository rep = getLoginUser().getRep();
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				String id = request.getParameter("id");
				String parentId = request.getParameter("parentId");
				String customOkFn = request.getParameter("customOkFn");
				RepDirectoryDialog repDirectoryDialog = new RepDirectoryDialog(
						rep, id);
				repDirectoryDialog.setCustomOkFn(customOkFn);
				repDirectoryDialog.setParentId(parentId);
				response.getWriter()
						.write(repDirectoryDialog.open().toString());

			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"getDirectory"));
		}
	}

	// 获得探测资源库的窗口
	public void detectRep() {
		try {
			Repository rep = getLoginUser().getRep();
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
				String modify = request.getParameter("modify");
				String customOkFn = request.getParameter("customOkFn");

				RepExplorerDialog repExplorerDialog = new RepExplorerDialog(
						rep, id);
				repExplorerDialog.setShowTypes(Const.NVL(showTypes, "all"));
				repExplorerDialog.setModify("true".equalsIgnoreCase(modify));
				repExplorerDialog.setCustomOkFn(Const.NVL(customOkFn, ""));
				repExplorerDialog.setParentId(Const.NVL(parentId, ""));
				response.getWriter().write(repExplorerDialog.open().toString());
			}

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"detectRep"));
		}
	}

	public void fileOpen() {
		try {
			Repository rep = getLoginUser().getRep();
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

			Repository rep = getLoginUser().getRep();
			TransMeta transMeta = new TransMeta(rep);
			transMeta.clearChanged();

			Date now = new Date();
			transMeta.setCreatedDate(now);
			transMeta.setModifiedDate(now);
			transMeta.setCreatedUser(getLoginUser().getLogin());
			transMeta.setModifiedUser(getLoginUser().getLogin());

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
	 * 新建一个任务
	 */
	public void newJobFile() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String newJobName = request.getParameter("newJobName");

			Repository rep = getLoginUser().getRep();

			JobMeta jobMeta = new JobMeta(delegates.getLog());
			jobMeta.clearChanged();
			jobMeta.setRep(rep);
			jobMeta.readSharedObjects(rep);

			Date now = new Date();
			jobMeta.setCreatedDate(now);
			jobMeta.setModifiedDate(now);
			jobMeta.setCreatedUser(getLoginUser().getLogin());
			jobMeta.setModifiedUser(getLoginUser().getLogin());

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
			Repository rep = getLoginUser().getRep();
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
						.getRep(getLoginUser()), d, roName);
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

			Repository rep = delegates.getRep(getLoginUser());
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

			Repository rep = delegates.getRep(getLoginUser());
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

	// 获得步骤插件列表的窗口
	public void stepPluginList() {

		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			StepPluginDialog dialog = new StepPluginDialog();
			response.getWriter().write(dialog.open().toString());
		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"stepPluginList"));
		}

	}

	// 获得步骤插件列表的窗口
	public void entryPluginList() {

		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			EntryPluginDialog dialog = new EntryPluginDialog();

			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"entryPluginList"));
		}

	}

	public void helpAbout() {

		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			StringBuffer helpAbout = new StringBuffer();
			Props props = delegates.getProps();
			helpAbout.append(props.getProperty("Version"));
			helpAbout.append("<br>");
			helpAbout.append("<br>");
			helpAbout.append(props.getProperty("Company"));
			helpAbout.append("<br>");
			helpAbout.append(props.getProperty("NetAddress"));
			helpAbout.append("<br>");
			helpAbout.append("<br>");
			helpAbout.append("<br>");
			helpAbout.append("BuildVersion："
					+ props.getProperty("BuildVersion"));
			helpAbout.append("<br>");
			helpAbout.append("BuildDate：" + props.getProperty("BuildDate"));

			response.getWriter().write(helpAbout.toString());

		} catch (Exception e) {
			delegates.getLog()
					.logError(
							toString(),
							Messages.getString("IMeta.Log.RunImetaAction",
									"helpAbout"));
		}

	}

	public void logout() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		JSONObject rtn = new JSONObject();
		try {
			response.setContentType("text/html; charset=UTF-8");
			UserInfo user = (UserInfo) request.getSession(false).getAttribute(
					UserInfo.STRING_USERINFO);
			if (user != null) {
				Repository rep = user.getRep();
				String loginUser = user.getLogin();
				// 登出用户
				user.logout();
				// 关闭正在编辑的对象
				trans.clearEditTransMetaByLoginName(rep, loginUser);
				jobs.clearEditJobMetaByLoginName(rep, loginUser);

				user.setRep(null);
				request.getSession(false).setAttribute(
						UserInfo.STRING_USERINFO, null);

				rtn.put("username", user.getLogin());
				rtn.put("redirect", request.getContextPath() + "/imeta");
				response.getWriter().write(rtn.toString());
			}
		} catch (Exception e) {
			delegates.getLog().logError(toString(),
					Messages.getString("IMeta.Log.RunImetaAction", "logout"));
			try {
				response.setContentType("text/html; charset=UTF-8");
				rtn.put("redirect", request.getContextPath() + "/imeta");
				response.getWriter().write(rtn.toString());
			} catch (Exception ex) {

			}
		}
	}

	// 获得当前登录用户级别
	public void toCurrentUserLevel() {
		try {
			Repository rep = getLoginUser().getRep();
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setContentType("text/html; charset=UTF-8");
			if (rep == null) {
				throw new KettleException("资源库未连接");
			}
			if (rep != null) {
				UserInfo userInfo = (UserInfo) request.getSession(false)
						.getAttribute(UserInfo.STRING_USERINFO);
				UserInfo user = rep.getUserById(userInfo.getID());
				UserDialog userDialog = new UserDialog(rep, user,
						UserDialog.USER_MODIFY);
				response.getWriter().write(
						userDialog.getUserInfo().getAccountType().toString());
			}

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"toCurrentUserLevel"));
		}
	}

}
