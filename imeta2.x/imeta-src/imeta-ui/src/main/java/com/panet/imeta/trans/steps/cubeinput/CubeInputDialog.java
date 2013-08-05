package com.panet.imeta.trans.steps.cubeinput;

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

public class CubeInputDialog extends BaseStepDialog implements
StepDialogInterface{

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
	 * 文件名称
	 */
	private LabelInputMeta filename;
//	private ButtonMeta fileNameSelecter;

	/**
	 * 记录行数限制
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Add filename to新增的文件名
	 */
	private LabelInputMeta addfilenameresult;

	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public CubeInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			CubeInputMeta step = (CubeInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);
			//Filename
			this.filename =  new LabelInputMeta(id + ".filename", "文件名称", null, null,
					null, step.getFilename(), null, ValidateForm
					.getInstance().setRequired(true));
//			this.fileNameSelecter = new ButtonMeta(id+ ".btn.fileNameSelecter", id +
//					".btn.fileNameSelecter","浏览(B)...", "浏览");
//			this.filename.addButton(new ButtonMeta[] { this.fileNameSelecter });
			this.filename.setSingle(true);
			//记录行数限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "记录行数限制", null, 
					null,null, String.valueOf(step.getRowLimit()), null, ValidateForm
					.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			//Add filename to
			this.addfilenameresult = new LabelInputMeta(id + ".addfilenameresult", "新增的文件名",
					null, null,null, String.valueOf(step.isAddResultFile()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {this.name,
					this.filename,this.rowLimit,this.addfilenameresult});

			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() ,super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
