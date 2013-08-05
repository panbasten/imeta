package com.panet.imeta.trans.steps.propertyoutput;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class PropertyOutputDialog extends BaseStepDialog implements
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
	 * 页签
	 */
	private MenuTabMeta meta;

	//General-----------------------------------------------------

	/**
	 * fields
	 */
	private ColumnFieldsetMeta fields;

	private LabelSelectMeta keyfield;

	private LabelSelectMeta valuefield;

	/**
	 * Comment
	 */
	private LabelTextareaMeta comment;

	//Content-----------------------------------------------------

	/**
	 * file
	 */
	private ColumnFieldsetMeta file;

	private LabelInputMeta fileName;

	private LabelInputMeta createparentfolder;

	private LabelInputMeta extension;

	private LabelInputMeta stepNrInFilename;

	private LabelInputMeta dateInFilename;

	private LabelInputMeta timeInFilename;

	/**
	 * result fileName
	 */
	private ColumnFieldsetMeta resultFileName;

	private LabelInputMeta AddToResult;

	//	
	//	// showFileName
	//	private ButtonMeta showFieldName;

	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public PropertyOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			PropertyOutputMeta step = (PropertyOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "内容" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0--General
			 ******************************************************************/
			this.fields = new ColumnFieldsetMeta(id, "文件转化XML");
			this.fields.setSingle(true);

			this.keyfield = new LabelSelectMeta(id + ".keyfield", "主字段", null,
					null, null, step.getKeyField(), null,
					super.getPrevStepResultFields());
			this.keyfield.setSingle(true);

		
			this.valuefield = new LabelSelectMeta(id + ".valueField", "值字段",
					null, null, null, step.getValueField(), null, super.getPrevStepResultFields());

			this.valuefield.setSingle(true);

			this.fields.putFieldsetsContent(new BaseFormMeta[] { this.keyfield,
					this.valuefield });

			//Comment
			this.comment = new LabelTextareaMeta(id + ".comment", "评论", null,
					null, null, step.getComment(), 10, null);

			this.comment.setSingle(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.fields,
					this.comment });

			/*******************************************************************
			 * 标签1--Content
			 ******************************************************************/
			this.file = new ColumnFieldsetMeta(id, "文件");
			this.file.setSingle(true);

			// fileName
			this.fileName = new LabelInputMeta(id + ".fileName", "文件名", null,
					null, null, step.getFileName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fileName.setSingle(true);

			// create parent folder
			this.createparentfolder = new LabelInputMeta(id + ".createparentfolder",
					"创建父文件夹", null, null, null, String.valueOf(step
							.isCreateParentFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createparentfolder.setSingle(false);

			//扩展名 extension
			this.extension = new LabelInputMeta(id + ".extension", "扩展名", null,
					null, null, step.getExtension(), null, ValidateForm
							.getInstance().setRequired(false));
			this.extension.setSingle(true);

			// include stepnr in filename
			this.stepNrInFilename = new LabelInputMeta(id + ".stepNrInFilename",
					"在文件名包含stepnr", null, null, null, String.valueOf(step
							.isStepNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.stepNrInFilename.setSingle(false);

			//include date in filename
			this.dateInFilename = new LabelInputMeta(id + ".dateInFilename",
					"在文件名中包含日期", null, null, null, String.valueOf(step
							.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dateInFilename.setSingle(false);

			//include time in filename
			this.timeInFilename = new LabelInputMeta(id + ".timeInFilename",
					"在文件名包含当前时间", null, null, null, String.valueOf(step
							.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.timeInFilename.setSingle(false);

			this.file.putFieldsetsContent(new BaseFormMeta[] { this.fileName,
					this.createparentfolder, this.extension, this.stepNrInFilename,
					this.dateInFilename, this.timeInFilename });

			//--------result filename

			this.resultFileName = new ColumnFieldsetMeta(id, "结果文件名");
			this.resultFileName.setSingle(true);

			// Add File to result 
			this.AddToResult = new LabelInputMeta(id + ".AddToResult", "将文件添加到结果",
					null, null, null, String.valueOf(step.AddToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddToResult.setSingle(true);

//			// 显示字段名称
//			DivMeta btnDiv0 = new DivMeta(new NullSimpleFormDataMeta(), true);
//
//			this.showFieldName = new ButtonMeta(id + ".btn.showFieldName", id
//					+ ".btn.ShowFieldName", "显示字段名称", "显示字段名称");
//
//			this.showFieldName.appendTo(btnDiv0);

			this.resultFileName.putFieldsetsContent(new BaseFormMeta[] {
					this.AddToResult
//					, btnDiv0 
					});

			this.meta.putTabContent(1, new BaseFormMeta[] { this.file,
					this.resultFileName });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
