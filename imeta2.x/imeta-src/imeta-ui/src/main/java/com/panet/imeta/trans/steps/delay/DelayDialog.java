package com.panet.imeta.trans.steps.delay;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DelayDialog extends BaseStepDialog implements
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
	 * Timeout
	 */
	private LabelInputMeta timeout;
	
	/**
	 * 
	 */
	private LabelSelectMeta scaletime;
	
	public DelayDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			DelayMeta step = (DelayMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Timeout
			this.timeout = new LabelInputMeta(id + ".timeout", "超时", null, null,
					"超时必须填写", 
                   String.valueOf(step.getTimeOut()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.timeout.setSingle(true);
			
			//
			List<OptionDataMeta> optionstimeout = new ArrayList<OptionDataMeta>();
			optionstimeout.add(new OptionDataMeta("milliseconds", Messages.getString("DelayDialog.MSScaleTime.Label")));
			optionstimeout.add(new OptionDataMeta("seconds", Messages.getString("DelayDialog.SScaleTime.Label")));
			optionstimeout.add(new OptionDataMeta("minutes", Messages.getString("DelayDialog.MnScaleTime.Label")));
			optionstimeout.add(new OptionDataMeta("hours", Messages.getString("DelayDialog.HrScaleTime.Label")));
			this.scaletime = new LabelSelectMeta(id + ".scaletime","",
					null,null,null, String.valueOf(step.getScaleTime()),null,optionstimeout);
			this.scaletime.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.timeout,this.scaletime
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
