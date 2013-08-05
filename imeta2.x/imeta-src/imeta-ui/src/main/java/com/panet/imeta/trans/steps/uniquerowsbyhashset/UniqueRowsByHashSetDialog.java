package com.panet.imeta.trans.steps.uniquerowsbyhashset;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class UniqueRowsByHashSetDialog extends BaseStepDialog implements
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
	 * Compare using stored row values？
	 */
	private LabelInputMeta storeValues;

	/**
	 * 用来比较的字段
	 */
	private LabelGridMeta fields;

	public UniqueRowsByHashSetDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			UniqueRowsByHashSetMeta step = (UniqueRowsByHashSetMeta) super
					.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Compare using stored row values? 比较使用存储行价值？
			this.storeValues = new LabelInputMeta(id + ".storeValues",
					"比较使用存储行价值？", null, null, null, String.valueOf(step
							.getStoreValues()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.storeValues.setSingle(false);

			// 字段
			this.fields = new LabelGridMeta(id + "_fields",
					"用来比较的字段（没有条目意味着：比较现在完成了）", 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.compareFields",
							"字段名称", null, false, 550) });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.uniquerowsbyhashset.btn.fieldsAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] values = step.getCompareFields();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.storeValues, this.fields });
			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							super.getCancelBtn(),
							super.getGetfields("jQuery.imeta.steps.uniquerowsbyhashset.btn.getfields") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
