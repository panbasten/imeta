package com.panet.imeta.trans.steps.randomvalue;

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

public class RandomValueDialog extends BaseStepDialog implements
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
	 * 字段列表
	 */
	private LabelGridMeta fieldsList;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public RandomValueDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			RandomValueMeta step = (RandomValueMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			// 字段列表
			this.fieldsList = new LabelGridMeta(id + "_fieldsList", "字段列表", 200);
			this.fieldsList.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsList.fieldId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fieldsList.fieldName", "名称",
							null, false, 200),
					(new GridHeaderDataMeta(id + "_fieldsList.fieldType", "类型",
							null, false, 200)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									RandomValueMeta.fieldTypeDesc, false)) });
			this.fieldsList.setSingle(true);
			this.fieldsList.setHasBottomBar(true);
			this.fieldsList.setHasAdd(true, true,
					"jQuery.imeta.steps.randomvalue.btn.fieldAdd");
			this.fieldsList.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			String[] fieldName = step.getFieldName();
			if (fieldName != null && fieldName.length > 0) {
				for (int i = 0; i < fieldName.length; i++) {
					this.fieldsList.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fieldsList });

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
