package com.panet.imeta.trans.steps.valuemapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ValueMapperDialog extends BaseStepDialog implements
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
	 * 使用的字段名
	 */
	private LabelInputMeta fieldToUse;

	/**
	 * 目标字段名
	 */
	private LabelInputMeta targetField;

	/**
	 * Default upon non-matching
	 */
	private LabelInputMeta nonMatchDefault;

	/**
	 * 字段值
	 */
	private LabelGridMeta fields;

	public ValueMapperDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ValueMapperMeta step = (ValueMapperMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 使用的字段名

			this.fieldToUse = new LabelInputMeta(id + ".fieldToUse", "使用的字段名",
					null, null, null, step.getFieldToUse(), null, 
					ValidateForm.getInstance().setRequired(true));
			this.fieldToUse.setSingle(true);

			// 目标字段名

			this.targetField = new LabelInputMeta(id + ".targetField",
					"目标字段名(emply=overwrite)", null, null, "目标字段名必须填写", step
							.getTargetField(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.targetField.setSingle(true);

			// Default upon non-matching
			this.nonMatchDefault = new LabelInputMeta(id + ".nonMatchDefault",
					"默认情况下不匹配", null, null, "", step.getNonMatchDefault(),
					null, ValidateForm.getInstance().setRequired(false));
			this.nonMatchDefault.setSingle(true);

			// 字段值
			this.fields = new LabelGridMeta(id + "_fields", "字段值", 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.field", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.sourceValue", "源值",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.targetValue", "目标值",
							null, false, 490),

			});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.valuemapper.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] values = step.getTargetValue();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getSourceValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getTargetValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fieldToUse, this.targetField, this.nonMatchDefault,
					this.fields });

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
