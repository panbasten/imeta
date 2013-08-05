package com.panet.imeta.trans.steps.fieldsplitter;

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
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class FieldSplitterDialog extends BaseStepDialog implements
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
	 * 需要拆分的字段
	 */
	private LabelSelectMeta splitField;

	/**
	 * 分隔符
	 */
	private LabelInputMeta delimiter;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;

	public FieldSplitterDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			FieldSplitterMeta step = (FieldSplitterMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到需要拆分的字段

			this.splitField = new LabelSelectMeta(id + ".splitField",
					"需要拆分的字段", null, null, null, step.getSplitField(), null,
					super.getPrevStepResultFields());
			this.splitField.setSingle(true);

			// 得到分隔符

			this.delimiter = new LabelInputMeta(id + ".delimiter", "分隔符", null,
					null, "分隔符必须填写", String.valueOf(step.getDelimiter()), null,
					ValidateForm.getInstance().setRequired(true));
			this.delimiter.setSingle(true);

			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 200);

			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.typeCodes, false));

			GridHeaderDataMeta fieldTrimTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.fieldTrimType", "去空格类型", null, false, 100);
			fieldTrimTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.trimTypeDesc, false));

			GridHeaderDataMeta fieldBooleanTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.fieldRemoveID", "移除ID？", null, false, 80);
			fieldBooleanTypeDataMeta.setOptions(super
					.getOptionsByTrueAndFalse(false));

			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "新的字段",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldID", "ID", null,
							false, 50),
					fieldBooleanTypeDataMeta,
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false, 60),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency", "通用",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldNullIf", "如果为空",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldIfNull", "缺省",
							null, false, 50), 
				    fieldTrimTypeDataMeta });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.fieldsplitter.btn.fieldsAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] fieldNames = step.getFieldName();
			if (fieldNames != null && fieldNames.length > 0)
				for (int i = 0; i < fieldNames.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldID()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldRemoveID()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldLength()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldPrecision()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldFormat()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldGroup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldDecimal()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldCurrency()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldNullIf()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldIfNull()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldTrimType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			//			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.splitField, this.delimiter, this.fields });

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
