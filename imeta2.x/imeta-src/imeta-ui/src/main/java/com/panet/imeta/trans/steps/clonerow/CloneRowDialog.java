package com.panet.imeta.trans.steps.clonerow;

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

public class CloneRowDialog extends BaseStepDialog implements
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
	 * Nr clones
	 */
	private LabelInputMeta nrclones;
	
	/**
	 * Nr clone in field
	 */
	private LabelInputMeta nrcloneinfield;
	
	/**
	 * Nr clone field
	 */
	private LabelSelectMeta nrclonefield;
	
	/**
	 * Add clone flag
	 */
	private LabelInputMeta addcloneflag;
	
	/**
	 * Clone flag
	 */
	private LabelInputMeta cloneflagfield;
	
	public CloneRowDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			CloneRowMeta step = (CloneRowMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Nr clones
			this.nrclones = new LabelInputMeta(id + ".nrclones", "Nr复制", null, null,
					"Nr clones必须填写",step.getNrClones(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.nrclones.setSingle(true);
			this.nrclones.setDisabled(step.isNrCloneInField());

			// Nr clone in field?
			this.nrcloneinfield = new LabelInputMeta(
					id + ".nrcloneinfield",
					"Nr复制字段?",
					null, null, null,
					String
							.valueOf(step.isNrCloneInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.nrcloneinfield.setSingle(true);
			this.nrcloneinfield.addClick("jQuery.imeta.steps.clonerow.listeners.nrcloneinfield");
			
			// Nr Clone field
			this.nrclonefield = new LabelSelectMeta ( id + ".nrclonefield", "Nr复制字段",
					null, null, null, step.getNrCloneField(), null, super.getPrevStepResultFields());
			this.nrclonefield.setSingle(true);
			this.nrclonefield.setDisabled(!step.isNrCloneInField());
			
			// Add clone flag
			this.addcloneflag = new LabelInputMeta(id + ".addcloneflag", "添加复制标记", null,
					null, null, 
					String
					.valueOf(step.isAddCloneFlag()),
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.addcloneflag.setSingle(true);
			this.addcloneflag.addClick("jQuery.imeta.steps.clonerow.listeners.addcloneflag");
			
			// Clone flag
			this.cloneflagfield = new LabelInputMeta(id + ".cloneflagfield", "复制标记", null, null,
					"复制标记必须填写", 
					step.getCloneFlagField(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.cloneflagfield.setSingle(true);
			this.cloneflagfield.setDisabled(!step.isAddCloneFlag());

           
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.nrclones,
					this.nrcloneinfield, this.nrclonefield,
					this.addcloneflag, this.cloneflagfield
					});

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
