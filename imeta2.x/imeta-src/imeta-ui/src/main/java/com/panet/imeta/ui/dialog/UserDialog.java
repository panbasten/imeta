package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.Messages;
import com.panet.imeta.ui.exception.ImetaException;

public class UserDialog implements BaseDialogInterface {

	public static final String USER_ADD = "add";
	public static final String USER_MODIFY = "modify";
	public static final String USER_DEL = "delete";

	private UserInfo userInfo;

	private String id;

	private String type = USER_MODIFY;

	private String title;

	private long userId;

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private LabelInputMeta id_user;

	private LabelInputMeta login;

	private LabelInputMeta oldpassword;

	private LabelInputMeta password;

	private LabelInputMeta repassword;

	private LabelInputMeta username;

	private LabelTextareaMeta description;

	private LabelSelectMeta accountType;

	private ButtonMeta ok;

	private ButtonMeta cancel;

	private ArrayList<UserInfo> users;

	private LabelGridMeta userList;

	public UserDialog(Repository rep, UserInfo userInfo, String type)
			throws KettleException {
		this.type = type;
		if (USER_MODIFY.equals(type)) {
			this.type = USER_MODIFY;
			this.title = Messages.getString("IMeta.User.Dialog.Title.Modify");
			this.userInfo = userInfo;
		} else if (USER_ADD.equals(type)) {
			this.type = USER_ADD;
			this.title = Messages.getString("IMeta.User.Dialog.Title.Add");
			this.userInfo = new UserInfo();
		} else if (USER_DEL.equals(type)) {
			this.type = USER_DEL;
			this.title = Messages.getString("IMeta.User.Dialog.Title.Delete");
			ArrayList<UserInfo> users = (ArrayList<UserInfo>) rep.getUsers();
			this.users = users;
		}

	}

	public ArrayList<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<UserInfo> users) {
		this.users = users;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {

			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			this.id = "user" + this.userId;
			// 添加用户
			if (USER_ADD.equals(this.type)) {

				// 得到form
				this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
				this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
				// 登录账户
				this.login = new LabelInputMeta(USER_ADD + ".login", "登录账户",
						null, null, null, this.userInfo.getLogin(), null,
						ValidateForm.getInstance().setRequired(true)
								.setRangelength(1, 20));
				this.login.setSingle(true);
				// 登录密码
				this.password = new LabelInputMeta(USER_ADD + ".password",
						"登录密码", null, null, null, null,
						InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
								.getInstance().setRequired(true)
								.setRangelength(1, 20));
				this.password.setSingle(true);

				// 确认密码
				this.repassword = new LabelInputMeta(USER_ADD + ".repassword",
						"确认密码", null, null, null, null,
						InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
								.getInstance().setRequired(true).setEqualTo(
										USER_ADD + ".password").setRangelength(
										1, 20));
				this.repassword.setSingle(true);

				// 账户全称
				this.username = new LabelInputMeta(USER_ADD + ".username",
						"账户全称", null, null, null, this.userInfo.getName(),
						null, ValidateForm.getInstance().setRangelength(1, 20));
				this.username.setSingle(true);

				// 账户类型
				List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
				options.add(new OptionDataMeta(UserInfo.STRING_USER_TYPE_ADMIN,
						Messages.getString("IMeta.User.Type.Admin")));
				options.add(new OptionDataMeta(
						UserInfo.STRING_USER_TYPE_EDITOR, Messages
								.getString("IMeta.User.Type.Editor")));
				options.add(new OptionDataMeta(
						UserInfo.STRING_USER_TYPE_OPERATOR, Messages
								.getString("IMeta.User.Type.Operator")));
				this.accountType = new LabelSelectMeta(USER_ADD
						+ ".accountType", "账户类型", null, null, null,
						this.userInfo.getAccountType(), ValidateForm
								.getInstance().setRequired(true), options);
				this.accountType.setSingle(true);

				// 详细描述
				this.description = new LabelTextareaMeta(USER_ADD
						+ ".description", "详细描述", null, null, null,
						this.userInfo.getDescription(), 5, ValidateForm
								.getInstance().setRangelength(0, 200));
				this.description.setSingle(true);

				this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
						this.login, this.password, this.repassword,
						this.username, this.accountType, this.description });

				this.ok = new ButtonMeta(this.id + ".btn.ok", this.id
						+ ".btn.ok", "确定", "确定");
				this.ok.addClick("jQuery.imenu.iMenuFn.file.createUserBtn.ok");
				this.cancel = new ButtonMeta(this.id + ".btn.cancel", this.id
						+ ".btn.cancel", "取消", "取消");
				this.cancel
						.addClick("jQuery.imenu.iMenuFn.file.createUserBtn.cancel");
				this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
						this.ok, this.cancel });

				cArr.add(this.columnFormMeta.getFormJo());
				rtn.put("items", cArr);

				rtn.put("title", this.title);
				rtn.put("id", this.id);
			}
			// 修改
			else if (USER_MODIFY.equals(this.type)) {
				// 得到form
				this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
				this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

				// 隐藏域用户id
				String userid = new Long(this.userInfo.getID()).toString();

				this.id_user = new LabelInputMeta(this.id + ".id_user", "",
						null, null, userid, userid,
						InputDataMeta.INPUT_TYPE_HIDDEN, null);
				this.id_user.setSingle(true);

				// 用户输入旧密码
				this.oldpassword = new LabelInputMeta(USER_MODIFY
						+ ".oldpassword", "旧密码", null, null, null, null,
						InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
								.getInstance().setRequired(true)
								.setRangelength(1, 20));
				this.oldpassword.setSingle(true);
				// 新密码
				this.password = new LabelInputMeta(USER_MODIFY + ".password",
						"新密码", null, null, null, null,
						InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
								.getInstance().setRequired(true)
								.setRangelength(1, 20));
				this.password.setSingle(true);

				// 确认密码
				this.repassword = new LabelInputMeta(USER_MODIFY
						+ ".repassword", "确认密码", null, null, null, null,
						InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
								.getInstance().setRequired(true).setEqualTo(
										USER_MODIFY + ".password")
								.setRangelength(1, 20));
				this.repassword.setSingle(true);

				// 账户全称
				this.username = new LabelInputMeta(USER_MODIFY + ".username",
						"账户全称", null, null, null, this.userInfo.getName(),
						null, ValidateForm.getInstance().setRangelength(1, 20));
				this.username.setSingle(true);

				// 账户类型
				if (!UserInfo.STRING_DEFAULT_USER_LOGIN
						.equalsIgnoreCase(this.userInfo.getLogin())) {
					List<OptionDataMeta> optionsa = new ArrayList<OptionDataMeta>();
					optionsa.add(new OptionDataMeta(
							UserInfo.STRING_USER_TYPE_ADMIN, Messages
									.getString("IMeta.User.Type.Admin")));
					optionsa.add(new OptionDataMeta(
							UserInfo.STRING_USER_TYPE_EDITOR, Messages
									.getString("IMeta.User.Type.Editor")));
					optionsa.add(new OptionDataMeta(
							UserInfo.STRING_USER_TYPE_OPERATOR, Messages
									.getString("IMeta.User.Type.Operator")));
					this.accountType = new LabelSelectMeta(USER_MODIFY
							+ ".accountType", "账户类型", null, null, null,
							this.userInfo.getAccountType(), ValidateForm
									.getInstance().setRequired(true), optionsa);
					this.accountType.setSingle(true);
				}

				// 详细描述
				this.description = new LabelTextareaMeta(USER_MODIFY
						+ ".description", "详细描述", null, null, null,
						this.userInfo.getDescription(), 5, ValidateForm
								.getInstance().setRangelength(0, 200));
				this.description.setSingle(true);

				if (!UserInfo.STRING_DEFAULT_USER_LOGIN
						.equalsIgnoreCase(this.userInfo.getLogin())) {
					this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
							this.id_user, this.oldpassword, this.password,
							this.repassword, this.username, this.accountType,
							this.description });
				} else {
					this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
							this.id_user, this.oldpassword, this.password,
							this.repassword, this.username, this.description });
				}

				this.ok = new ButtonMeta(this.id + ".btn.ok", this.id
						+ ".btn.ok", "确定", "确定");
				this.ok.addClick("jQuery.imenu.iMenuFn.file.editUserBtn.ok");
				this.cancel = new ButtonMeta(this.id + ".btn.cancel", this.id
						+ ".btn.cancel", "取消", "取消");
				this.cancel
						.addClick("jQuery.imenu.iMenuFn.file.editUserBtn.cancel");
				this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
						this.ok, this.cancel });

				cArr.add(this.columnFormMeta.getFormJo());

				rtn.put("items", cArr);

				rtn.put("title", this.title);
				rtn.put("id", this.id);
			}
			// 删除
			else if (USER_DEL.equals(this.type)) {
				// 得到form
				this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
				this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
				this.userList = new LabelGridMeta(id + "_userlist", "用户列表", 200);
				this.userList.addHeaders(new GridHeaderDataMeta[] {
						new GridHeaderDataMeta(this.id + ".checkbox", "null",
								GridHeaderDataMeta.HEADER_TYPE_CHECKBOX, false,
								50),
						new GridHeaderDataMeta(this.id + ".login", "登录名", null,
								false, 120),
						new GridHeaderDataMeta(this.id + ".name", "账户名", null,
								false, 120),
						new GridHeaderDataMeta(this.id + ".type", "账户类型", null,
								false, 120) });
				String checkboxId, loginValue, nameValue;
				GridCellDataMeta userCheckbox;
				for (int i = 0; i < this.users.size(); i++) {
					loginValue = ((UserInfo) users.get(i)).getLogin();

					if (!UserInfo.STRING_DEFAULT_USER_LOGIN
							.equalsIgnoreCase(loginValue)) {
						GridRowDataMeta rowDataMeta = new GridRowDataMeta(4);
						checkboxId = new Long(((UserInfo) users.get(i)).getID())
								.toString();
						userCheckbox = new GridCellDataMeta(checkboxId, "false");
						userCheckbox
								.setType(GridCellDataMeta.CELL_TYPE_CHECKBOX);
						nameValue = ((UserInfo) users.get(i)).getName();
						rowDataMeta.setCell(0, userCheckbox);
						rowDataMeta.setCell(1, loginValue);
						rowDataMeta.setCell(2, nameValue);
						rowDataMeta.setCell(3,
								getUserTypeLabel(((UserInfo) users.get(i))
										.getAccountType()));
						this.userList.addRow(rowDataMeta);
					}
				}

				this.userList.setSingle(true);

				this.columnFormMeta
						.putFieldsetsContent(new BaseFormMeta[] { this.userList });

				this.ok = new ButtonMeta(this.id + ".btn.ok", this.id
						+ ".btn.ok", "删除", "删除");
				this.ok.addClick("jQuery.imenu.iMenuFn.file.deleteUserBtn.ok");
				this.cancel = new ButtonMeta(this.id + ".btn.cancel", this.id
						+ ".btn.cancel", "取消", "取消");
				this.cancel
						.addClick("jQuery.imenu.iMenuFn.file.deleteUserBtn.cancel");
				this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
						this.ok, this.cancel });

				cArr.add(this.columnFormMeta.getFormJo());

				rtn.put("items", cArr);

				rtn.put("title", this.title);
				rtn.put("id", this.id);
			}

			return rtn;

		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	public static String getUserTypeLabel(String type) {
		if (UserInfo.STRING_USER_TYPE_ADMIN.equalsIgnoreCase(type))
			return Messages.getString("IMeta.User.Type.Admin");
		else if (UserInfo.STRING_USER_TYPE_EDITOR.equalsIgnoreCase(type))
			return Messages.getString("IMeta.User.Type.Editor");
		else if (UserInfo.STRING_USER_TYPE_OPERATOR.equalsIgnoreCase(type))
			return Messages.getString("IMeta.User.Type.Operator");
		return "";
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long id) {
		this.userId = id;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
