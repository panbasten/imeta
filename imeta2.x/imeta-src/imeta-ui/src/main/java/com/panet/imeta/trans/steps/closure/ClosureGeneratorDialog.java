package com.panet.imeta.trans.steps.closure;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ClosureGeneratorDialog extends BaseStepDialog implements
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
	 * Parent ID field
	 */
	private LabelSelectMeta parentIdFieldName;
	
	/**
	 * Child ID field
	 */
	private LabelSelectMeta childIdFieldName;
	
	/**
	 * Distance field name
	 */
	private LabelInputMeta distanceFieldName;
	
	/**
	 * Root is zero(Integer)?
	 */
	private LabelInputMeta rootIdZero;
	
	public ClosureGeneratorDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			ClosureGeneratorMeta step = (ClosureGeneratorMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Parent ID field
			this.parentIdFieldName = new LabelSelectMeta(id + ".parentIdFieldName","父ID字段",
					null,null,null,
					String
					.valueOf(step.getParentIdFieldName()),
			        null, super.getPrevStepResultFields());
			this.parentIdFieldName.setSingle(true);
	
			// Child ID field
			this.childIdFieldName = new LabelSelectMeta(id + ".childIdFieldName","子ID字段",
					null,null,null,
					String
					.valueOf(step.getChildIdFieldName()),
				    null, super.getPrevStepResultFields());
			this.childIdFieldName.setSingle(true);
			
            // Distance field name
			
			this.distanceFieldName = new LabelInputMeta(id + ".distanceFieldName", "字段名称间距", null, null,
					"字段名称间距必须填写", 
					step.getDistanceFieldName(),
				    null, ValidateForm
							.getInstance().setRequired(true));
			this.distanceFieldName.setSingle(true);
			
			//Root is zero(Integer)?
			this.rootIdZero = new LabelInputMeta(
					id + ".rootIdZero",
					"允许延时转换",
					null,
					null,
					null,
					String
					.valueOf(step.isRootIdZero()),
			InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
			this.rootIdZero.setSingle(true);
           
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.parentIdFieldName,this.childIdFieldName,this.distanceFieldName,this.rootIdZero});

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
