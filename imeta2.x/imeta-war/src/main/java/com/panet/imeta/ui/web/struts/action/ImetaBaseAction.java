package com.panet.imeta.ui.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.messageBox.MessageBoxMeta;
import com.panet.imeta.core.Props;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.EntryPluginDialog;
import com.panet.imeta.ui.dialog.RepDirectoryDialog;
import com.panet.imeta.ui.dialog.StepPluginDialog;
import com.panet.imeta.ui.dialog.UserDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

@Controller("imeta.ui.imetaBaseAction")
public class ImetaBaseAction extends ActionSupport {

	private static final long serialVersionUID = -1406536564396449828L;

	public static final String LOGO_TEXT = "中科软科技股份有限公司";

	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobs;

	@Autowired
	private ImetaTransformationDelegate trans;

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

			UserInfo user = UserInfo.getLoginUser();
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

			// 加入作业的工具箱
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
	 * 编辑目录
	 * 
	 * @throws KettleException
	 * @throws ImetaException
	 */
	public void editDirectory() throws KettleException, ImetaException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType("text/html; charset=UTF-8");
		Repository rep = delegates.getRep(UserInfo.getLoginUser());

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
	 * 获得目录对话框
	 */
	public void getDirectory() {
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
		Repository rep = delegates.getRep(UserInfo.getLoginUser());

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

				// 删除该目录下的所有转换和作业
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
				// 3.得到作业目录名s
				String[] jobNames = rep.getJobNames(directoryId_l);
				// 4.删除作业
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
		Repository rep = delegates.getRep(UserInfo.getLoginUser());

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
	 * 获得步骤插件列表的窗口
	 */
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

	/**
	 * 获得步骤插件列表的窗口
	 */
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

	/**
	 * 获得帮助信息
	 */
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
}
