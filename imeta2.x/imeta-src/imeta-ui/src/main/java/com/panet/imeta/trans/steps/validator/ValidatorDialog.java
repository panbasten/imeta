package com.panet.imeta.trans.steps.validator;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.columnDiv.ColumnDivMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ValidatorDialog extends BaseStepDialog implements
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
	 * Report all error
	 */
	private LabelInputMeta validatingAll;

	/**
	 * 选择验证
	 */
	private LabelSelectMeta selectVal;

	/**
	 * Select a validation to edit
	 */
	private ColumnFieldsetMeta valEdit;
	private ColumnFieldsetMeta selectAndEdit;

	/**
	 * New validation
	 */
	private ButtonMeta newValidation;

	/**
	 * Remove validation
	 */
	private ButtonMeta removeValidation;

	// 规则描述
	private LabelInputMeta valName;
	// 要验证的字段名称
	private LabelInputMeta fieldName;
	// 错误代码
	private LabelInputMeta errorCode;
	// 错误描述
	private LabelInputMeta errorDescription;
	// 类型
	private ColumnFieldsetMeta type;
	// 是否验证数据类型
	private LabelInputMeta dataTypeVerified;
	// 数据类型
	private LabelSelectMeta dataType;
	// 转换掩码
	private LabelInputMeta conversionMask;
	// 小数符号
	private LabelInputMeta decimalSymbol;
	// 分组符号
	private LabelInputMeta groupingSymbol;
	// 数据
	private ColumnFieldsetMeta data;
	// 是否允许null
	private LabelInputMeta nullAllowed;
	// 是否只允许null
	private LabelInputMeta onlyNullAllowed;
	// 是否只期望数值数据
	private LabelInputMeta onlyNumericAllowed;
	// 最大字符串长度
	private LabelInputMeta maximumLength;
	// 最小字符串长度
	private LabelInputMeta minimumLength;
	// 最大值
	private LabelInputMeta maximumValue;
	// 最小值
	private LabelInputMeta minimumValue;
	// 开始字符串
	private LabelInputMeta startString;
	// 结束字符串
	private LabelInputMeta endString;
	// 不允许的开始字符串
	private LabelInputMeta startStringNotAllowed;
	// 不允许的结束字符串
	private LabelInputMeta endStringNotAllowed;
	// 期望匹配的正则表达式
	private LabelInputMeta regularExpression;
	// 不期望匹配的正则表达式
	private LabelInputMeta regularExpressionNotAllowed;
	// 允许的值
	private LabelGridMeta allowedValues;
	// 是否允许从其他step读取值
	private LabelInputMeta sourcingValues;
	// 源step名称
	private LabelSelectMeta sourcingStepName;
	// 源字段
	private LabelSelectMeta sourcingField;

	private ButtonMeta okBtn;

	public ValidatorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			ValidatorMeta step = (ValidatorMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Report
			this.validatingAll = new LabelInputMeta(id + ".validatingAll",
					"报告所有的错误", null, null, null, String.valueOf(step
							.isValidatingAll()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.validatingAll.setLabelAfter(true);
			this.validatingAll.setSingle(true);

			// Select a validation to edit
			ColumnDivMeta columnDivMeta = new ColumnDivMeta();
			this.selectVal = new LabelSelectMeta(id + "_select", "选择一个验证进行编辑",
					null, null, "选择一个验证进行编辑", null, null, null);
			this.selectVal.setSingle(true);
			this.selectVal.setLayout(LabelSelectMeta.LAYOUT_COLUMN);
			this.selectVal.setSize(20);
			this.selectVal.addListener("change",
					"jQuery.imeta.steps.validator.listeners.validationChange");
			this.selectVal.addListener("click",
					"jQuery.imeta.steps.validator.listeners.validationClick");

			Validation[] validations = step.getValidations();
			for (int i = 0; i < validations.length; i++) {
				JSONObject jo = new JSONObject();
				if (validations[i] != null) {
					// value += val.isSourcingValues();
					jo.put("valName", validations[i].getName());
					jo.put("fieldName", validations[i].getFieldName());
					jo.put("errorCode", validations[i].getErrorCode());
					jo.put("errorDescription", validations[i]
							.getErrorDescription());
					jo.put("dataType", validations[i].getDataType());
					jo.put("dataTypeVerified", validations[i]
							.isDataTypeVerified());
					jo.put("allowedValues", validations[i].getAllowedValues());
					jo
							.put("conversionMask", validations[i]
									.getConversionMask());
					jo.put("decimalSymbol", validations[i].getDecimalSymbol());
					jo
							.put("groupingSymbol", validations[i]
									.getGroupingSymbol());
					jo.put("nullAllowed", validations[i].isNullAllowed());
					jo.put("onlyNullAllowed", validations[i]
							.isOnlyNullAllowed());
					jo.put("onlyNumericAllowed", validations[i]
							.isOnlyNumericAllowed());
					jo.put("maximumLength", validations[i].getMaximumLength());
					jo.put("minimumLength", validations[i].getMinimumLength());
					jo.put("maximumValue", validations[i].getMaximumValue());
					jo.put("minimumValue", validations[i].getMinimumValue());
					jo.put("startString", validations[i].getStartString());
					jo.put("endString", validations[i].getEndString());
					jo.put("startStringNotAllowed", validations[i]
							.getStartStringNotAllowed());
					jo.put("endStringNotAllowed", validations[i]
							.getEndStringNotAllowed());
					jo.put("regularExpression", validations[i]
							.getRegularExpression());
					jo.put("regularExpressionNotAllowed", validations[i]
							.getRegularExpressionNotAllowed());
					jo.put("sourcingValues", validations[i].isSourcingValues());
					jo.put("sourcingStepName", validations[i]
							.getSourcingStepName());
					jo.put("sourcingField", validations[i].getSourcingField());
					String value = jo.toString();

					OptionDataMeta o = new OptionDataMeta();
					o.setValue(value);
					o.setText(validations[i].getName());
					if (i == 0)
						o.setSelected(true);
					this.selectVal.appendOption(o);
					// this.selectVal.appendOption(value,
					// validations[i].getName());
				}
			}
			Validation validation = null;
			if (validations != null && validations.length > 0)
				validation = validations[0];
			// this.selectVal.appendOption("test", "test");
			columnDivMeta.putDivContent(new BaseFormMeta[] { this.selectVal });
			columnDivMeta.getRoot().setStyle("float", "left");

			this.valEdit = new ColumnFieldsetMeta(id + "_edit", "");
			this.valEdit.getRoot().setStyle("float", "left");
			this.valEdit.putFieldsetsContent(initFields(this.getId(),
					validation));
			DivMeta divMeta = this.valEdit.getRoot();
			divMeta.setDisplay(validation != null);

			this.selectAndEdit = new ColumnFieldsetMeta(null, "");
			this.selectAndEdit.setSingle(true);
			this.selectAndEdit.putFieldsetsContent(new BaseFormMeta[] {
					columnDivMeta, this.valEdit });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.validatingAll, selectAndEdit });

			this.newValidation = new ButtonMeta(id + ".btn.newValidation", id
					+ ".btn.newValidation", "新建验证", "新建验证");
			this.newValidation
					.addClick("jQuery.imeta.steps.validator.btn.newValidation");
			this.removeValidation = new ButtonMeta(
					id + ".btn.removeValidation", id + ".btn.removeValidation",
					"删除验证", "删除验证");
			this.removeValidation
					.addClick("jQuery.imeta.steps.validator.btn.deleteValidation");
			this.okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定",
					"确定");
			this.okBtn.putProperty("transName", super.getTransMeta().getName());
			this.okBtn.putProperty("stepName", super.getStepName());
			this.okBtn.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			this.okBtn.addClick("jQuery.imeta.steps.validator.btn.ok");
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.okBtn, this.newValidation, this.removeValidation,
					super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	private BaseFormMeta[] initFields(String id, Validation validation)
			throws ImetaFormException, KettleException {
		if (validation == null) {
			validation = new Validation();

		}
		this.valName = new LabelInputMeta(id + ".valName", "验证描述", null, null,
				"验证描述", validation.getName(), null, ValidateForm.getInstance()
						.setRequired(false));
		this.valName.setReadonly(true);
		this.valName.addListener("change",
				"jQuery.imeta.steps.validator.listeners.valNameChange");

		this.valName.setSingle(true);

		this.fieldName = new LabelInputMeta(id + ".fieldName", "要验证的字段名称",
				null, null, "要验证的字段名称", validation.getFieldName(), null,
				ValidateForm.getInstance().setRequired(false));
		this.fieldName.setSingle(true);

		this.errorCode = new LabelInputMeta(id + ".errorCode", "错误码", null,
				null, "错误码", validation.getErrorCode(), null, ValidateForm
						.getInstance().setRequired(false));
		this.errorCode.setSingle(true);
		this.errorDescription = new LabelInputMeta(id + ".errorDescription",
				"错误描述", null, null, "错误描述", validation.getErrorDescription(),
				null, ValidateForm.getInstance().setRequired(false));
		this.errorDescription.setSingle(true);

		List<OptionDataMeta> dataTypeVerifiedOptions = new ArrayList<OptionDataMeta>();
		String[] dataType = ValueMeta.getTypes();
		for (int i = 0; i < dataType.length; i++) {
			dataTypeVerifiedOptions.add(new OptionDataMeta(String
					.valueOf(i + 1), dataType[i]));
		}
		this.dataTypeVerified = new LabelInputMeta(id + ".dataTypeVerified",
				"验证数据类型？", null, null, null, String.valueOf(validation
						.isDataTypeVerified()),
				InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance()
						.setRequired(false));
		this.dataTypeVerified.setSingle(true);

		this.dataType = new LabelSelectMeta(id + ".dataType", "数据类型", null,
				null, "数据类型", String.valueOf(validation.getDataType()), null,
				dataTypeVerifiedOptions);
		this.dataType.setSingle(true);
		this.dataType.setHasEmpty(true);

		this.conversionMask = new LabelInputMeta(id + ".conversionMask",
				"转换掩码", null, null, "转换掩码", validation.getConversionMask(),
				null, ValidateForm.getInstance().setRequired(false));
		this.conversionMask.setSingle(true);

		this.decimalSymbol = new LabelInputMeta(id + ".decimalSymbol", "小数符号",
				null, null, "小数符号", validation.getDecimalSymbol(), null,
				ValidateForm.getInstance().setRequired(false));
		this.decimalSymbol.setSingle(true);

		this.groupingSymbol = new LabelInputMeta(id + ".groupingSymbol",
				"分组符号", null, null, "分组符号", validation.getGroupingSymbol(),
				null, ValidateForm.getInstance().setRequired(false));
		this.groupingSymbol.setSingle(true);

		this.type = new ColumnFieldsetMeta(null, "类型");
		this.type.setSingle(true);
		this.type.putFieldsetsContent(new BaseFormMeta[] {
				this.dataTypeVerified, this.dataType, this.conversionMask,
				this.decimalSymbol, this.groupingSymbol });

		this.nullAllowed = new LabelInputMeta(id + ".nullAllowed", "允许空值？",
				null, null, null, String.valueOf(validation.isNullAllowed()),
				InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance()
						.setRequired(false));
		this.nullAllowed.setSingle(true);

		this.onlyNullAllowed = new LabelInputMeta(id + ".onlyNullAllowed",
				"只允许空值？", null, null, null, String.valueOf(validation
						.isOnlyNullAllowed()),
				InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance()
						.setRequired(false));
		this.onlyNullAllowed.setSingle(true);

		this.onlyNumericAllowed = new LabelInputMeta(
				id + ".onlyNumericAllowed", "只允许数值？", null, null, null, String
						.valueOf(validation.isOnlyNumericAllowed()),
				InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance()
						.setRequired(false));
		this.onlyNumericAllowed.setSingle(true);

		this.maximumLength = new LabelInputMeta(id + ".maximumLength",
				"字符串最大长度", null, null, "字符串最大长度", String.valueOf(validation
						.getMaximumLength()), null, ValidateForm.getInstance()
						.setRequired(false));
		this.maximumLength.setSingle(true);

		this.minimumLength = new LabelInputMeta(id + ".minimumLength",
				"字符串最小长度", null, null, "字符串最小长度", String.valueOf(validation
						.getMinimumLength()), null, ValidateForm.getInstance()
						.setRequired(false));
		this.minimumLength.setSingle(true);

		this.maximumValue = new LabelInputMeta(id + ".maximumValue", "最大值",
				null, null, "最大值", validation.getMaximumValue(), null,
				ValidateForm.getInstance().setRequired(false));
		this.maximumValue.setSingle(true);

		this.minimumValue = new LabelInputMeta(id + ".minimumValue", "最小值",
				null, null, "最小值", validation.getMinimumValue(), null,
				ValidateForm.getInstance().setRequired(false));
		this.minimumValue.setSingle(true);

		this.startString = new LabelInputMeta(id + ".startString", "期望开始字符串",
				null, null, "期望开始字符串", validation.getStartString(), null,
				ValidateForm.getInstance().setRequired(false));
		this.startString.setSingle(true);

		this.endString = new LabelInputMeta(id + ".endString", "期望结束字符串", null,
				null, "期望结束字符串", validation.getEndString(), null, ValidateForm
						.getInstance().setRequired(false));
		this.endString.setSingle(true);

		this.startStringNotAllowed = new LabelInputMeta(id
				+ ".startStringNotAllowed", "不允许的开始字符串", null, null,
				"不允许的开始字符串", validation.getStartStringNotAllowed(), null,
				ValidateForm.getInstance().setRequired(false));
		this.startStringNotAllowed.setSingle(true);

		this.endStringNotAllowed = new LabelInputMeta(id
				+ ".endStringNotAllowed", "不允许的结束字符串", null, null, "不允许的结束字符串",
				validation.getEndStringNotAllowed(), null, ValidateForm
						.getInstance().setRequired(false));
		this.endStringNotAllowed.setSingle(true);

		this.regularExpression = new LabelInputMeta(id + ".regularExpression",
				"期望匹配的正则表达式", null, null, "期望匹配的正则表达式", validation
						.getRegularExpression(), null, ValidateForm
						.getInstance().setRequired(false));
		this.regularExpression.setSingle(true);

		this.regularExpressionNotAllowed = new LabelInputMeta(id
				+ ".regularExpressionNotAllowed", "不期望匹配的正则表达式", null, null,
				"不期望匹配的正则表达式", validation.getRegularExpressionNotAllowed(),
				null, ValidateForm.getInstance().setRequired(false));
		this.regularExpressionNotAllowed.setSingle(true);

		boolean isSourcingValues = validation.isSourcingValues();
		this.allowedValues = new LabelGridMeta(id + "_allowedValues", "允许的值：",
				200, 0);
		this.allowedValues.setWidth(35);
		this.allowedValues.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_allowedValues.fieldId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_allowedValues.value", "允许的值",
						null, false, 100) });
		this.allowedValues.setHasBottomBar(true);
		this.allowedValues.setHasAdd(true, !isSourcingValues,
				"jQuery.imeta.steps.validator.btn.valueAdd");
		this.allowedValues.setHasDelete(true, !isSourcingValues,
				"jQuery.imeta.parameter.fieldsDelete");
		this.allowedValues.setSingle(true);

		String[] values = validation.getAllowedValues();
		if (values != null && values.length > 0) {
			GridCellDataMeta value;
			for (int i = 0; i < values.length; i++) {
				value = new GridCellDataMeta(null, values[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				value.setDisabled(isSourcingValues);
				this.allowedValues.addRow(new Object[] { String.valueOf(i + 1),
						value });
			}
		}
		this.sourcingValues = new LabelInputMeta(id + ".sourcingValues",
				"从另一个step读允许的值？", null, null, null, String.valueOf(validation
						.isSourcingValues()),
				InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance()
						.setRequired(false));
		this.sourcingValues
				.addClick("jQuery.imeta.steps.validator.listeners.isSourcingValuesClick");
		this.sourcingValues.setSingle(false);

		this.sourcingStepName = new LabelSelectMeta(id + ".sourcingStepName",
				"要读取的step名称", null, null, "要读取的step名称", validation
						.getSourcingStepName(), null, super.getPrevStepNames());
		this.sourcingStepName.setDisabled(!isSourcingValues);
		this.sourcingStepName.setSingle(true);
		this.sourcingStepName.setHasEmpty(true);
		// List<OptionDataMeta> resultOptions = new ArrayList<OptionDataMeta>();
		// try {
		// RowMetaInterface r = super.getTransMeta().getPrevStepFields(
		// super.getStepName());
		// if (r != null) {
		// String[] resultNames = r.getFieldNames();
		// for (int i = 0; i < resultNames.length; i++) {
		// resultOptions.add(new OptionDataMeta(String.valueOf(i),
		// resultNames[i]));
		// }
		// }
		// } catch (KettleException ke) {
		// throw new KettleException(ke.getMessage(), ke);
		// }
		this.sourcingField = new LabelSelectMeta(id + ".sourcingField",
				"要读取的字段", null, null, "要读取的字段", validation.getSourcingField(),
				null, super.getPrevStepResultFields());
		this.sourcingField.setDisabled(!isSourcingValues);
		this.sourcingField.setSingle(true);
		this.sourcingField.setHasEmpty(true);

		this.data = new ColumnFieldsetMeta(null, "数据");
		this.data.setSingle(true);
		this.data.putFieldsetsContent(new BaseFormMeta[] { this.nullAllowed,
				this.onlyNullAllowed, this.onlyNumericAllowed,
				this.maximumLength, this.minimumLength, this.maximumValue,
				this.minimumValue, this.startString, this.endString,
				this.startStringNotAllowed, this.endStringNotAllowed,
				this.regularExpression, this.regularExpressionNotAllowed,
				this.allowedValues, this.sourcingValues, this.sourcingStepName,
				this.sourcingField });

		return new BaseFormMeta[] { this.valName, this.fieldName,
				this.errorCode, this.errorDescription, this.type, this.data };
	}

}
