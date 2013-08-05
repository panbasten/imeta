package com.panet.imeta.trans.steps.switchcase;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SwitchCaseDialog extends BaseStepDialog implements
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
	 * Field name to switch
	 */
	private LabelInputMeta fieldname;

	/**
	 * Case value date type
	 */
	private LabelSelectMeta caseValueType;

	/**
	 * Case value conversion
	 */
	private LabelInputMeta caseValueFormat;

	/**
	 * Case value decimal symbol
	 */
	private LabelInputMeta caseValueDecimal;

	/**
	 * Case value grouping symbol
	 */
	private LabelInputMeta caseValueGroup;

	/**
	 * Case values
	 */
	private LabelGridMeta caseValues;

	/**
	 * Default target step
	 */
	private LabelSelectMeta defaultTargetStepname;

	public SwitchCaseDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SwitchCaseMeta step = (SwitchCaseMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Field name to switch
			this.fieldname = new LabelInputMeta(id + ".fieldname", "用于切换的字段名称",
					null, null, null, step.getFieldname(), null, null);
			this.fieldname.setSingle(true);

			// Case value date type
			this.caseValueType = new LabelSelectMeta(id + ".caseValueType",
					"用例值数据类型", null, null, null, String.valueOf(step
							.getCaseValueType()), null, super
							.getValueTypeByIndex());
			this.caseValueType.setSingle(true);

			// Case value conversion
			this.caseValueFormat = new LabelInputMeta(id + ".caseValueFormat",
					"用例转换", null, null, null, step.getCaseValueFormat(), null,
					null);
			this.caseValueFormat.setSingle(true);

			// Case value decimal symbol
			this.caseValueDecimal = new LabelInputMeta(
					id + ".caseValueDecimal", "用例小数点符号", null, null, null, step
							.getCaseValueDecimal(), null, null);
			this.caseValueDecimal.setSingle(true);

			// Case value grouping symbol
			this.caseValueGroup = new LabelInputMeta(id + ".caseValueGroup",
					"用例分组符号", null, null, null, step.getCaseValueGroup(), null,
					null);
			this.caseValueGroup.setSingle(true);

			// Case values
			this.caseValues = new LabelGridMeta(id + "_caseValues", "用例", 200);
			
			GridHeaderDataMeta caseTargetStepnamesDataMeta = new GridHeaderDataMeta(
					id + "_caseValues.caseTargetStepnames", "目标步骤", null,
					false, 200);
			caseTargetStepnamesDataMeta.setOptions(super.getOptionsByList(super
					.getNextStepNames(), true));
			
			this.caseValues.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_caseValues.caseId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_caseValues.caseValues", "值",
							null, false, 100), caseTargetStepnamesDataMeta });
			this.caseValues.setHasBottomBar(true);
			this.caseValues.setHasAdd(true, true,
					"jQuery.imeta.steps.switchcase.btn.caseValuesAdd");
			this.caseValues.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.caseValues.setSingle(true);

			String[] field = step.getCaseValues();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.caseValues.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getCaseValues()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getCaseTargetStepnames()[i],
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// Default target step
			this.defaultTargetStepname = new LabelSelectMeta(id
					+ ".defaultTargetStepname", "预设目标步骤", null, null, null,
					step.getDefaultTargetStepname(), null, super
							.getNextStepNames());
			this.defaultTargetStepname.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fieldname, this.caseValueType, this.caseValueFormat,
					this.caseValueDecimal, this.caseValueGroup,
					this.caseValues, this.defaultTargetStepname });

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
