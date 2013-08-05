package com.panet.imeta.trans.steps.calculator;

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
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class CalculatorDialog extends BaseStepDialog implements
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
	 * 字段
	 */
	private LabelGridMeta fields;

	public CalculatorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			CalculatorMeta step = (CalculatorMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到字段

			this.fields = new LabelGridMeta(id + "_fields", "字段", 150);

			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.valueType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.typeCodes, false));

			GridHeaderDataMeta fieldBooleanTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.removedFromResult", "移除", null, false, 80);
			fieldBooleanTypeDataMeta.setOptions(super
					.getOptionsByTrueAndFalse(false));
            
			// 下拉框获得字段
			// RowMetaInterface rA = super.getTransMeta().getPrevStepFields(
			// super.getStepMeta());
			// String[] resultNamesA = rA.getFieldNames();
			// GridHeaderDataMeta fieldA = new GridHeaderDataMeta(id
			// + "_fields.fieldA", "字段A", null, false, 100);
			// fieldA.setOptions(super.getOptionsByStringArray(
			// resultNamesA, true));
			//			
			// RowMetaInterface rB = super.getTransMeta().getPrevStepFields(
			// super.getStepMeta());
			// String[] resultNamesB = rB.getFieldNames();
			// GridHeaderDataMeta fieldB = new GridHeaderDataMeta(id
			// + "_fields.fieldB", "字段B", null, false, 100);
			// fieldB.setOptions(super.getOptionsByStringArray(
			// resultNamesB, true));
			//			
			// RowMetaInterface rC = super.getTransMeta().getPrevStepFields(
			// super.getStepMeta());
			// String[] resultNamesC = rC.getFieldNames();
			// GridHeaderDataMeta fieldC = new GridHeaderDataMeta(id
			// + "_fields.fieldC", "字段C", null, false, 100);
			// fieldC.setOptions(super.getOptionsByStringArray(
			// resultNamesC, true));

			this.fields
					.addHeaders(new GridHeaderDataMeta[] {
							new GridHeaderDataMeta(id + "_fields.fieldId", "#",
									GridHeaderDataMeta.HEADER_TYPE_NUMBER,
									false, 50),
							new GridHeaderDataMeta(id + "_fields.fieldName",
									"新字段", null, false, 50),
							(new GridHeaderDataMeta(id + "_fields.calcType",
									"计算", null, false, 200))
									.setOptions(super
											.getOptionsByStringArrayWithNumberValue(
													CalculatorMetaFunction.fieldTypeDesc,
													false)),
							// fieldA,
							// fieldB,
							// fieldC,
							new GridHeaderDataMeta(id + "_fields.fieldA",
									"字段A", null, false, 50),
							new GridHeaderDataMeta(id + "_fields.fieldB",
									"字段B", null, false, 50),
							new GridHeaderDataMeta(id + "_fields.fieldC",
									"字段C", null, false, 50),
							fieldTypeDataMeta,
							new GridHeaderDataMeta(id + "_fields.valueLength",
									"长度", null, false, 50),
							new GridHeaderDataMeta(id
									+ "_fields.valuePrecision", "精确度", null,
									false, 50),
							fieldBooleanTypeDataMeta,
							new GridHeaderDataMeta(id
									+ "_fields.conversionMask", "转换模式", null,
									false, 80),
							new GridHeaderDataMeta(
									id + "_fields.decimalSymbol", "十进制符号",
									null, false, 100),
							new GridHeaderDataMeta(id
									+ "_fields.groupingSymbol", "分组符号", null,
									false, 80),
							new GridHeaderDataMeta(id
									+ "_fields.currencySymbol", "流通符号", null,
									false, 80) });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.calculator.btn.calculatorAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			CalculatorMetaFunction[] calculator = step.getCalculation();
			if (calculator != null && calculator.length > 0)
				for (int i = 0; i < calculator.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, calculator[i]
									.getFieldName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(calculator[i].getCalcType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, calculator[i].getFieldA(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, calculator[i].getFieldB(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, calculator[i].getFieldC(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(calculator[i].getValueType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(calculator[i].getValueLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(calculator[i]
											.getValuePrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(calculator[i]
											.isRemovedFromResult()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, calculator[i]
											.getConversionMask(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, calculator[i].getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, calculator[i]
											.getGroupingSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, calculator[i]
											.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT)

					});
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
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
