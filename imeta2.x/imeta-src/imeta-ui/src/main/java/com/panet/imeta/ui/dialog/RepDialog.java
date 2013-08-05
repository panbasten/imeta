package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.ui.exception.ImetaException;

public class RepDialog implements BaseDialogInterface {

	public static final String REP_PREFIX_ID = "connection_rep";

	private JSONArray reps;

	private UserInfo userInfo;

	private String id;

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private LabelInputMeta username, password;

	private LabelSelectMeta repository;

	private ButtonMeta ok;

	private Repository rep;

	private ButtonMeta cancel;

	public RepDialog(JSONArray reps, Repository rep, UserInfo userInfo)
			throws KettleException {
		this.id = REP_PREFIX_ID;
		this.rep = rep;
		this.reps = reps;
		this.userInfo = userInfo;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			this.username = new LabelInputMeta(id + ".username", "登录用户", null,
					null, null, (userInfo != null) ? userInfo.getLogin() : "",
					null, ValidateForm.getInstance().setRequired(true).setRangelength(1, 20));
			this.username.setSingle(true);

			this.password = new LabelInputMeta(id + ".password", "登录密码", null,
					null, null, null, null, ValidateForm.getInstance()
							.setRequired(true).setRangelength(1, 20));
			this.password.setSingle(true);

			List<OptionDataMeta> repsList = new ArrayList<OptionDataMeta>();
			String repName;
			for (int i = 0; i < reps.size(); i++) {
				repName = reps.getString(i);
				repsList.add(new OptionDataMeta(repName, repName));
			}
			this.repository = new LabelSelectMeta(id + ".repository", "资源库",
					null, null, null, null, null, repsList);
			this.repository.setSingle(true);

			this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					username, password, repository });

			this.ok = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
			this.ok.addClick("jQuery.imenu.iMenuFn.file.connectRepBtn.ok");

			this.cancel = new ButtonMeta(id + ".btn.cancel",
					id + ".btn.cancel", "取消", "取消");
			this.cancel
					.addClick("jQuery.imenu.iMenuFn.file.connectRepBtn.cancel");
			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.ok, this.cancel });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			rtn.put("title", "探测资源库");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
