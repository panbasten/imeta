package com.panet.imeta.trans.steps.abort;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AbortDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名
	 */
	private LabelInputMeta name;
	
	/**
	 * 失败阙值
	 */
	private LabelInputMeta rowThreshold;
	
	/**
	 * 失败信息
	 */
	private LabelInputMeta message;
	
	/**
	 * 总是记录
	 */
	private LabelInputMeta alwaysLogRows;
	
	public AbortDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			AbortMeta step = (AbortMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名
			this.name = new LabelInputMeta(id + ".name", "步骤名", null, null,
					"步骤名必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 失败阙值
			this.rowThreshold = new LabelInputMeta(id + ".rowThreshold", "失败阙值", null, null,
					"失败阙值必须填写", String.valueOf(step.getRowThreshold()), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowThreshold.setSingle(true);
			
			// 失败信息
			this.message = new LabelInputMeta(id + ".message", "失败信息", null, null,
					"失败信息必须填写", String.valueOf(step.getMessage()), null, ValidateForm
							.getInstance().setRequired(true));
			this.message.setSingle(true);

			//总是记录
			this.alwaysLogRows = new LabelInputMeta(id + ".alwaysLogRows", "总是记录", null,
					null, null, String.valueOf(step.isAlwaysLogRows()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.alwaysLogRows.setSingle(false);
           
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
					this.name,
					this.rowThreshold,
					this.message,
					this.alwaysLogRows
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
