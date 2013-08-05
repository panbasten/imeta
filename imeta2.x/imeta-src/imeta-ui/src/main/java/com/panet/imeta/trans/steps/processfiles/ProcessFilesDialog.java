package com.panet.imeta.trans.steps.processfiles;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ProcessFilesDialog extends BaseStepDialog implements
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
	 * Setting
	 */
	private ColumnFieldsetMeta setting;
	
	/**
	 * Operation
	 */
	private LabelSelectMeta operationType;
	
	/**
	 * Create target parent folder
	 */
	private LabelInputMeta createparentfolder;
	
	/**
	 * Overwrite target file
	 */
	private LabelInputMeta overwritetargetfile;
	
	/**
	 * Add target filename to result
	 */
	private LabelInputMeta addresultfilenames;
	
	/**
	 * Set simulation
	 */
	private LabelInputMeta simulate;
	
	/**
	 * Source filename field
	 */
	private LabelSelectMeta sourcefilenamefield;
	
	/**
	 * Target filename field;
	 */
	private LabelSelectMeta targetfilenamefield;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ProcessFilesDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ProcessFilesMeta step = (ProcessFilesMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Settings
			this.setting = new ColumnFieldsetMeta(null, "设置");
			this.setting.setSingle(true);
			
			// Operation
			List<OptionDataMeta> optionsOperation = new ArrayList<OptionDataMeta>();
			optionsOperation.add(new OptionDataMeta("0", "复制"));
			optionsOperation.add(new OptionDataMeta("1", "移动"));
			optionsOperation.add(new OptionDataMeta("2", "删除"));
			this.operationType = new LabelSelectMeta(id + ".operationType","操作",
					null,null,null,String.valueOf(step.getOperationType()),null,optionsOperation);	
			this.operationType.setSingle(true);
			
			// Create target parent folder
			this.createparentfolder = new LabelInputMeta ( id + ".createparentfolder", "创建目标父文件夹", null, null, 
					null, String.valueOf(step.isCreateParentFolder()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.createparentfolder.setSingle(true);
			
			// Overwrite target file
			this.overwritetargetfile = new LabelInputMeta ( id + ".overwritetargetfile", "覆盖目标文件", null, null,
					null, String.valueOf(step.isOverwriteTargetFile()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.overwritetargetfile.setSingle(true);
			
			// Add target filename to result
			this.addresultfilenames = new LabelInputMeta ( id + ".addresultfilenames", "添加目标文件到结果", null, null, 
					null, String.valueOf(step.isaddTargetFileNametoResult()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.addresultfilenames.setSingle(true);
            
            // Set simulation mode
            this.simulate = new LabelInputMeta ( id + ".simulate", "设置仿真模式", null, null,
            		null, String.valueOf(step.isSimulate()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.simulate.setSingle(true);
            
            this.setting.putFieldsetsContent(new BaseFormMeta[] {
					this.operationType, this.createparentfolder,
					this.overwritetargetfile, this.addresultfilenames,
					this.simulate});
            
            // Source filename field
			this.sourcefilenamefield = new LabelSelectMeta(id + ".sourcefilenamefield","源文件名字段",
					null,null,null," ",null,super.getPrevStepResultFields());	
			this.sourcefilenamefield.setSingle(true);
            
			// Target filename field
			this.targetfilenamefield = new LabelSelectMeta(id + ".targetfilenamefield","目标文件名字段",
					null,null,null," ",null,super.getPrevStepResultFields());	
			this.targetfilenamefield.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.setting, this.sourcefilenamefield, this.targetfilenamefield
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
