package com.panet.imeta.trans.steps.cubeoutput;

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

public class CubeOutputDialog extends BaseStepDialog implements
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
	 * 文件名
	 */
	private LabelInputMeta filename;
	
	/**
	 * Do not create file at start
	 */
	private LabelInputMeta doNotOpenNewFileInit;
	
	/**
	 * Add filenames to result
	 */
	private LabelInputMeta addToResultFilenames;
	
	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public CubeOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			CubeOutputMeta step = (CubeOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
			
            // 得到文件名
			this.filename = new LabelInputMeta(id + ".filename", "文件名", null, null,
					"文件名必须填写", step.getFilename(),null, ValidateForm.getInstance().setRequired(false));
			
			this.filename.setSingle(true);
			
			//  Do not create file at start
			this.doNotOpenNewFileInit = new LabelInputMeta(id + ".doNotOpenNewFileInit", "在开始时创建新文件", null,
					null, null, 
					String.valueOf(step.isDoNotOpenNewFileInit()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.doNotOpenNewFileInit.setSingle(true);

		    //  Add filenames to result
			this.addToResultFilenames = new LabelInputMeta(id + ".addToResultFilenames", "在结束时添加文件名", null,
					null, null, 
					String.valueOf(step.isAddToResultFiles()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.addToResultFilenames.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
					this.name,
					this.filename,this.doNotOpenNewFileInit,
					this.addToResultFilenames});

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
