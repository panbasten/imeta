package com.panet.imeta.trans.steps.xbaseinput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class XBaseInputDialog extends BaseStepDialog implements
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
	 * 文件名
	 */
	private LabelInputMeta dbfFileName;
//	private ButtonMeta fileNameSelecter;
	
	/**
	 * Accept filenames from previous steps
	 */
	private ColumnFieldsetMeta acceptFilenameColum;
	
	/**
	 * Accept filenames from previous step
	 */
	private LabelInputMeta acceptingFilenames;
	
	/**
	 * Step to read filenames from
	 */
	private LabelSelectMeta acceptingStepName;
	
	/**
	 * field in the input to use as
	 */
	private LabelInputMeta acceptingField;

	/**
	 * Limit size
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 增加记录行数
	 */
	private LabelInputMeta rowNrAdded;

	/**
	 * 记录的字段名
	 */
	private LabelInputMeta rowNrField;

	/**
	 * Include filename in output
	 */
	private LabelInputMeta IncludeFilename;

	/**
	 * Filename field name
	 */
	private LabelInputMeta filenameField;

	/**
	 * The character-set name to use
	 */
	private LabelInputMeta charactersetName;


	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public XBaseInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			XBaseInputMeta step= (XBaseInputMeta) super.getStep();


			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);
			// 文件名
			this.dbfFileName = new LabelInputMeta(id + ".dbfFileName", "文件名", null,
					null, null, step.getDbfFileName(),
					null, ValidateForm.getInstance().setRequired(true));
//			this.fileNameSelecter = new ButtonMeta(
//					id + ".fileNameSelecter", id + ".fileNameSelecter",
//					"浏览", "浏览");
//			this.fileNameSelecter.setDisabled(step.isAcceptingFilenames());
//			this.dbfFileName.addButton(new ButtonMeta[] { this.fileNameSelecter });
			this.dbfFileName.setSingle(true);
			this.dbfFileName.setDisabled(step.isAcceptingFilenames());
			
			//Accept filenames from previous steps
			this.acceptFilenameColum = new ColumnFieldsetMeta(id, "从前面的步骤接受文件名");
			this.acceptFilenameColum.setSingle(true);
			
			//Accept filenames from previous step
			this.acceptingFilenames = new LabelInputMeta(id + ".acceptingFilenames", "从前面的步骤接受文件名", null,
					null, null, String.valueOf(step.isAcceptingFilenames()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,ValidateForm
					.getInstance().setRequired(false));
			this.acceptingFilenames.setSingle(false);
			this.acceptingFilenames.addClick("jQuery.imeta.steps.xbaseinput.listeners.acceptingFilenamesListeners");
			
			//Step to read filenames from从哪个步骤读文件名
			this.acceptingStepName = new LabelSelectMeta(id + ".acceptingStepName","从哪个步骤读文件名",
					null,null,null,step.getAcceptingStepName(),null,super.getPrevStepNames());
			this.acceptingStepName.setSingle(true);
			
			// field in the input to use as
			this.acceptingField = new LabelInputMeta(id + ".acceptingField", "输入的字段名作为文件名", null,
					null, null, step.getAcceptingField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.acceptingField.setSingle(true);
			
			this.acceptFilenameColum.putFieldsetsContent(new BaseFormMeta[] {
					this.acceptingFilenames,this.acceptingStepName,this.acceptingField
					});
			
			//Limit size
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制大小", null,
					null, null, String.valueOf(step.getRowLimit()), 
					null, ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			//增加记录行数
			this.rowNrAdded = new LabelInputMeta(id + ".rowNrAdded", 
					"增加记录行数", null,null, null, String
					.valueOf(step.isRowNrAdded()), InputDataMeta.INPUT_TYPE_CHECKBOX, 
					ValidateForm.getInstance().setRequired(true));
			this.rowNrAdded.addClick("jQuery.imeta.steps.xbaseinput.listeners.rowNrAddedListeners");
			//记录的字段名
			this.rowNrField = new LabelInputMeta(id + ".rowNrField", "记录的字段名",
					null,null, null, step.getRowNrField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.rowNrField.setDisabled(!step.isRowNrAdded());
			
			//Include filename in output
			this.IncludeFilename = new LabelInputMeta(id + ".IncludeFilename", 
					"包含输出的文件", null,null, null, String
					.valueOf(step.includeFilename()), 
							InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.IncludeFilename.addClick("jQuery.imeta.steps.xbaseinput.listeners.IncludeFilenameListeners");
			//Filename field name
			this.filenameField = new LabelInputMeta(id + ".filenameField", 
					"文件字段名称", null,null, null, step.getFilenameField(), 
					null, ValidateForm.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());
			
			//The character-set name to use
			this.charactersetName = new LabelInputMeta(id + ".charactersetName", 
					"使用该字符集名称", null,null, null, step.getCharactersetName(),
					null,ValidateForm.getInstance().setRequired(true));
			this.charactersetName.setSingle(true);
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.dbfFileName, this.acceptFilenameColum, this.rowLimit,this.rowNrAdded,this.rowNrField
					,this.IncludeFilename,this.filenameField,this.charactersetName});

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
