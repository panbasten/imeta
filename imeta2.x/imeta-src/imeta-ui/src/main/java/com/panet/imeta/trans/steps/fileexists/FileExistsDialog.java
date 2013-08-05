package com.panet.imeta.trans.steps.fileexists;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class FileExistsDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 文件名域
	 */
	private LabelSelectMeta filenamefield;

	/**
	 * 结果文件名
	 */
	private LabelInputMeta resultfieldname;

	/**
	 * 添加文件名
	 */
	private LabelInputMeta addresultfilenames;

	/**
	 * 附加域
	 */
	private ColumnFieldsetMeta destinationAddress;

	/**
	 * 包含文件类型
	 */
	private LabelInputMeta includefiletype;

	/**
	 * 文件类型域
	 */
	private LabelInputMeta filetypefieldname;

	public FileExistsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			FileExistsMeta step = (FileExistsMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"File exists", super.getStepName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到文件名域
			this.filenamefield = new LabelSelectMeta(id + ".filenamefield",
					"文件名域", null, null, null, step.getDynamicFilenameField(),
					null, super.getPrevStepResultFields());
			this.filenamefield.setSingle(true);
			this.filenamefield.setHasEmpty(true);
			// 得到结果文件名

			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname",
					"结果文件名", null, null, "结果文件名必须填写",
					step.getResultFieldName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.resultfieldname.setSingle(true);

			// 添加文件名
			this.addresultfilenames = new LabelInputMeta(id
					+ ".addresultfilenames", null, null, null, null, String
					.valueOf(step.addResultFilenames()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addresultfilenames.setSingle(false);

			// 附加域
			this.destinationAddress = new ColumnFieldsetMeta(null, "附加域");
			this.destinationAddress.setSingle(true);

			// 包含文件类型
			this.includefiletype = new LabelInputMeta(id + ".includefiletype",
					"包含文件类型", null, null, null, String.valueOf(step
							.includeFileType()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includefiletype.setSingle(false);
			this.includefiletype
					.addClick("jQuery.imeta.steps.fileexists.listeners.includefiletype");

			// 文件类型域
			this.filetypefieldname = new LabelInputMeta(id
					+ ".filetypefieldname", "文件类型域", null, null, "文件类型域必须填写",
					step.getFileTypeFieldName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filetypefieldname.setSingle(false);
			this.filetypefieldname.setDisabled(!step.includeFileType());

			this.destinationAddress.putFieldsetsContent(new BaseFormMeta[] {
					this.includefiletype, this.filetypefieldname });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filenamefield, this.resultfieldname,
					this.addresultfilenames, this.destinationAddress });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
