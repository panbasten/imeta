package com.panet.imeta.ui.dialog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.ui.exception.ImetaException;

public class XMLFileUploadDialog implements BaseDialogInterface {

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	private LabelInputMeta upload;
	private LabelInputMeta path;
	private ButtonMeta pathBtn;
	private LabelSelectMeta objectType;

	private ButtonMeta ok, cancel;

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = "importFile";

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.columnFormMeta.putProperty("enctype", "multipart/form-data");
			this.columnFormMeta.setSingle(true);

			this.upload = new LabelInputMeta("upload", "选择要导入的XML文件", null,
					null, null, null, InputDataMeta.INPUT_TYPE_FILE,
					ValidateForm.getInstance().setRequired(true));
			this.upload.setSingle(true);

			this.path = new LabelInputMeta(id + ".path", "选择要导入对象的路径", null, null,
					null, "/", null, ValidateForm.getInstance().setRequired(
							true));
			this.path.setSingle(true);

			this.pathBtn = new ButtonMeta(id + ".btn.pathBtn", id
					+ ".btn.pathBtn", "选择", "选择");
			this.pathBtn
					.addClick("jQuery.imenu.iMenuFn.file.fromXMLToOpenBtn.pathBtn");
			this.path.addButton(this.pathBtn);

			this.objectType = new LabelSelectMeta(id + ".objectType",
					"选择要导入文件的类型", null, null, null, null, ValidateForm
							.getInstance().setRequired(true), OptionDataMeta
							.getOptionDataMetaList(
									RepositoryObject.STRING_OBJECT_TYPES,
									RepositoryObject.STRING_OBJECT_TYPES_DESP));
			this.objectType.setSingle(true);

			this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.upload, this.path, this.objectType });

			this.ok = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
			this.ok.addClick("jQuery.imenu.iMenuFn.file.fromXMLToOpenBtn.ok");

			this.cancel = new ButtonMeta(id + ".btn.cancel",
					id + ".btn.cancel", "取消", "取消");
			this.cancel
					.addClick("jQuery.imenu.iMenuFn.file.fromXMLToOpenBtn.cancel");

			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.ok, this.cancel });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
