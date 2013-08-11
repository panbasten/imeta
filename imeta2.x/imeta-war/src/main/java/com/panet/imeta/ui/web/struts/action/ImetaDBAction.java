package com.panet.imeta.ui.web.struts.action;

import java.util.Iterator;
import java.util.List;

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
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.repository.RepositoryUtil;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.DatabaseExplorerDialog;
import com.panet.imeta.ui.dialog.DatabaseSettingDialog;
import com.panet.imeta.ui.dialog.RepDialog;
import com.panet.imeta.ui.dialog.RepDirectoryDialog;
import com.panet.imeta.ui.dialog.RepExplorerDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

@Controller("imeta.ui.imetaDBAction")
public class ImetaDBAction extends ActionSupport {

	private static final long serialVersionUID = -5705734788855417222L;

	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaTransformationDelegate trans;

	@Autowired
	private ImetaJobDelegate jobs;

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

			Repository rep = UserInfo.getLoginUser().getRep();
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
	 * 设置数据库
	 */
	public void settingDatabase() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			String elementId = request.getParameter("elementId");

			DatabaseSettingDialog dialog = getDatabaseDialog(Integer
					.parseInt(elementId));
			response.getWriter().write(dialog.open().toString());

		} catch (Exception e) {
			delegates.getLog().logError(
					toString(),
					Messages.getString("IMeta.Log.RunImetaAction",
							"settingElement"));
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
			Repository rep = delegates.getRep(UserInfo.getLoginUser());

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
	 * 浏览数据库
	 */
	@SuppressWarnings("unchecked")
	public void exploreDatabase() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			Repository rep = UserInfo.getLoginUser().getRep();

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

	private DatabaseSettingDialog getDatabaseDialog(int databaseId)
			throws KettleException {
		DatabaseMeta databaseMeta;
		Repository rep = UserInfo.getLoginUser().getRep();
		if (databaseId == 0) {
			databaseMeta = new DatabaseMeta();
		} else {
			databaseMeta = RepositoryUtil.loadDatabaseMeta(delegates
					.getRep(UserInfo.getLoginUser()), databaseId);
		}
		return new DatabaseSettingDialog(databaseMeta, rep);
	}

	/**
	 * 获得探测资源库的窗口
	 */
	public void detectRep() {
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
			Repository rep = delegates.getRep(UserInfo.getLoginUser());

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
	 * 载入对象内容树
	 */
	public void loadRepositoryObjects() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");
			JSONArray repositoryObjects = new JSONArray();

			// 0表示根目录
			Repository rep = delegates.getRep(UserInfo.getLoginUser());
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
						.getRep(UserInfo.getLoginUser()), Long
						.valueOf(directoryId), roName);
				ro.append(transMeta.getTransMetaTreeSegment(roId));

			} else if (RepositoryObject.STRING_OBJECT_TYPE_JOB.equals(roType)) {
				JobMeta jobMeta = jobs.getJobMetaByName(delegates
						.getRep(UserInfo.getLoginUser()), Long
						.valueOf(directoryId), roName);
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

		Repository rep = delegates.getRep(UserInfo.getLoginUser());
		UserInfo user = UserInfo.getLoginUser();
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

	/**
	 * 连接一个资源库
	 */
	public void connectRep() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=UTF-8");

			// Repository rep = UserInfo.getLoginUser().getRep();
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

	/**
	 * 连接资源库的提交操作
	 */
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

	/**
	 * 断开资源库连接
	 */
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
}
