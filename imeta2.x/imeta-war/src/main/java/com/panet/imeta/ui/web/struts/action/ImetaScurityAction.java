package com.panet.imeta.ui.web.struts.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.panet.iform.forms.messageBox.MessageBoxMeta;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.dialog.UserDialog;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaDelegates;
import com.panet.imeta.ui.service.ImetaJobDelegate;
import com.panet.imeta.ui.service.ImetaTransformationDelegate;

@Controller("imeta.ui.imetaScurityAction")
public class ImetaScurityAction extends ActionSupport {

	private static final long serialVersionUID = -4893052902179688677L;

	@Autowired
	private ImetaDelegates delegates;

	@Autowired
	private ImetaJobDelegate jobs;

	@Autowired
	private ImetaTransformationDelegate trans;

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

	// 获得创建新用户的窗口
	public void createUser() {

		try {
			Repository rep = UserInfo.getLoginUser().getRep();
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
			Repository rep = delegates.getRep(UserInfo.getLoginUser());
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
			Repository rep = UserInfo.getLoginUser().getRep();
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

			Repository rep = delegates.getRep(UserInfo.getLoginUser());
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
			Repository rep = delegates.getRep(UserInfo.getLoginUser());
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
			Repository rep = delegates.getRep(UserInfo.getLoginUser());
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
			Repository rep = UserInfo.getLoginUser().getRep();
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
