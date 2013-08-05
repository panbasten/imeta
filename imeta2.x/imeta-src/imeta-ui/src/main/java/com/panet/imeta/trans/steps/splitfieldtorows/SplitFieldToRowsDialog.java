package com.panet.imeta.trans.steps.splitfieldtorows;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SplitFieldToRowsDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * Step name
	 */
	private LabelInputMeta name;

	/**
	 * Field to split
	 */
	private LabelInputMeta splitField;

	/**
	 * Delimiter
	 */
	private LabelInputMeta delimiter;

	/**
	 * New field name
	 */
	private LabelInputMeta newFieldname;

	private LabelInputMeta emptyField;

	/**
	 * Additional fields
	 */
	private ColumnFieldsetMeta additiionalFields;

	/**
	 * Include rownum in output
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * Rownum fieldname
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Reset rownum at each input row?
	 */
	private LabelInputMeta resetRowNumber;

	public SplitFieldToRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SplitFieldToRowsMeta step = (SplitFieldToRowsMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Field to split
			this.splitField = new LabelInputMeta(id + ".splitField", "分解字段",
					null, null, "分解字段", String.valueOf(step.getSplitField()),
					null, ValidateForm.getInstance().setRequired(true));
			this.splitField.setSingle(true);

			// Delimiter
			this.delimiter = new LabelInputMeta(id + ".delimiter", "分隔符", null,
					null, "分隔符", String.valueOf(step.getDelimiter()), null,
					ValidateForm.getInstance().setRequired(true));
			this.delimiter.setSingle(true);

			// New field name
			this.newFieldname = new LabelInputMeta(id + ".newFieldname", "新字段",
					null, null, "新字段必须填写", String.valueOf(step
							.getNewFieldname()), null, ValidateForm
							.getInstance().setRequired(true));
			this.newFieldname.setSingle(true);

			this.emptyField = new LabelInputMeta(id + ".emptyField", "是否包含空字段",
					null, null, null, String
							.valueOf(step.isIncludeEmptyField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Additional fields
			this.additiionalFields = new ColumnFieldsetMeta(null, "附件字段");
			this.additiionalFields.setSingle(true);

			// Include rownum in output:
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "是否在输出中包含行号字段", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeRowNumber.setSingle(false);
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.splitfieldtorows.listeners.includeRowNumber");

			// Rownum fieldname
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段名称", null, null, "行号字段名称必须填写", step
							.getRowNumberField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowNumberField.setSingle(false);
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// reset rownum at each input row?
			this.resetRowNumber = new LabelInputMeta(id + ".resetRowNumber",
					"每个输入行重置行号", null, null, null, String.valueOf(step
							.resetRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.resetRowNumber.setDisabled(!step.includeRowNumber());
			this.additiionalFields.putFieldsetsContent(new BaseFormMeta[] {
					this.includeRowNumber, this.rowNumberField,
					this.resetRowNumber });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.splitField, this.delimiter, this.newFieldname,
					this.emptyField, this.additiionalFields });

			// 确定，取消
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
