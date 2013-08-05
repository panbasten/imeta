package com.panet.imeta.trans.steps.reservoirsampling;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ReservoirSamplingDialog extends BaseStepDialog implements
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
	 * Sample size
	 */
	private LabelInputMeta sampleSize;

	/**
	 * Random seed
	 */
	private LabelInputMeta randomSeed;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	public ReservoirSamplingDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			ReservoirSamplingMeta step = (ReservoirSamplingMeta) super
					.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Sample size
			this.sampleSize = new LabelInputMeta(id + ".sampleSize", "样本大小",
					null, null, "样本大小必须填写", step.getSampleSize(), null,
					ValidateForm.getInstance().setRequired(true));
			this.sampleSize.setSingle(true);

			// Random seed
			this.randomSeed = new LabelInputMeta(id + ".randomSeed", "随机样本",
					null, null, "随机样本必须填写", step.getSeed(), null, ValidateForm
							.getInstance().setRequired(true));
			this.randomSeed.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.sampleSize, this.randomSeed });
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
