package com.panet.imeta.trans.steps.uniquerows;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class UniqueRowsDialog extends BaseStepDialog implements
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
	 * 增加计数器到输出？
	 */
	private LabelInputMeta countRows;
	
	/**
	 * 计数器字段
	 */
	private LabelInputMeta countField;
	
	/**
	 * 用来比较的字段
	 */
	private LabelGridMeta fields;
	
	public UniqueRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			UniqueRowsMeta step = (UniqueRowsMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 增加计数器到输出
			this.countRows = new LabelInputMeta(id + ".countRows", "增加计数器到输出？", null,
					null, null,
					String
					.valueOf(step.isCountRows()),
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.countRows.addClick("jQuery.imeta.steps.uniquerows.listeners.countRows");
			
			// 计数器字段
			this.countField = new LabelInputMeta(id + ".countField", "计数器字段", null, null,
					"排序缓存大小必须填写",
					step.getCountField(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.countField.setDisabled(!step.isCountRows());
			
			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "用来比较的字段（没有条目意味着：比较现在完成了）", 200);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.compareFields", "字段名称", null, false, 100);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			GridHeaderDataMeta fieldBooleanTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.caseInsensitive", "忽略大小写", null, false, 100);
			fieldBooleanTypeDataMeta.setOptions(super
					.getOptionsByTrueAndFalse(false));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta,
					fieldBooleanTypeDataMeta
			});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.uniquerows.btn.uniquerowsAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);;
			
			String[] values = step.getCompareFields();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getCompareFields()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getCaseInsensitive()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.countRows,this.countField,this.fields});

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getGetfields("jQuery.imeta.steps.uniquerows.btn.getfields")});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
