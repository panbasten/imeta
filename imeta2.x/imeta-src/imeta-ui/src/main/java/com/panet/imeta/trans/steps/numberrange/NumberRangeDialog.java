package com.panet.imeta.trans.steps.numberrange;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class NumberRangeDialog extends BaseStepDialog implements
StepDialogInterface{
	
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
	 * Input field
	 */
	private LabelSelectMeta inputField;
	
	/**
	 * Output field
	 */
	private LabelInputMeta outputField;
	
	/**
	 * Default value(if no range matches)
	 */
	private LabelInputMeta fallBackValue;
	
	/**
	 * Ranges( min <= x < max)
	 */
	private LabelGridMeta ranges;
	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public NumberRangeDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			

			NumberRangeMeta step = (NumberRangeMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			//
			
			// Step name
			this.name = new LabelInputMeta ( id + ".name", "步骤名称：", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Input field
			this.inputField= new LabelSelectMeta(id + "inputField","输入字段：",
					null, null, null, step.getInputField(), null,super.getPrevStepResultFields());	
			
		    this.inputField.setSingle(true);
			
		    // Output field
		    this.outputField = new LabelInputMeta ( id + ".outputField", "输出字段", null, null,
		    		"输出字段必须填写", 
		    		String.valueOf(step.getOutputField()),
					null,ValidateForm.getInstance().setRequired(true));
		    this.outputField.setSingle(true);
		    
		    // Default value
		    this.fallBackValue = new LabelInputMeta ( id + ".fallBackValue", "默认值（如果没有匹配范围）：", null, null,
		    		"默认值必须填写", 
		    		String.valueOf(step.getFallBackValue()),
					null, ValidateForm.getInstance().setRequired(true));
		    this.fallBackValue.setSingle(true);
		    
		    // Ranges
		    this.ranges = new LabelGridMeta( id + "_ranges", "匹配 ( min <= x < max): ", 200);
			this.ranges.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_ranges.fieldId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_ranges.lowerBoundStr", "下界", null, false, 100),
					new GridHeaderDataMeta(id + "_ranges.upperBoundStr", "上界", null, false, 100),
					new GridHeaderDataMeta(id + "_ranges.value", "值", null, false, 335),
					});
			this.ranges.addRow(new String[] { "1", "", "5.0", "少于5", ""});
			this.ranges.addRow(new String[] { "2", "5.0", "10.0", "5-10", ""});
			this.ranges.addRow(new String[] { "3", "10.0", "", "多于10", ""});
			this.ranges.setHasBottomBar(true);
			this.ranges.setHasAdd(true, true,
					"jQuery.imeta.steps.numberrange.btn.numberrangeAdd");
			this.ranges.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.ranges.setSingle(true);
		    
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.inputField, this.outputField,
                    this.fallBackValue, this.ranges
					 });
			
			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
