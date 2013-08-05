package com.panet.imeta.trans.steps.exceloutput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ExcelOutputDialog extends BaseStepDialog implements
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

	/**
	 * ------------------------------------------------------------------ 文件
	 */

	/**
	 * 文件名
	 */
	private LabelInputMeta fileName;

	/**
	 * 不在初始化时建立文件
	 */
	private LabelInputMeta doNotOpenNewFileInit;

	/**
	 * 扩展名
	 */
	private LabelInputMeta extension;

	/**
	 * 在文件里面包含步骤数
	 */
	private LabelInputMeta stepNrInFilename;

	/**
	 * 在文件里包含日期
	 */
	private LabelInputMeta dateInFilename;

	/**
	 * 在文件里包含时间
	 */
	private LabelInputMeta timeInFilename;

	/**
	 * 指定日期时间格式 Specify Date time format
	 */
	private LabelInputMeta specifyFormat;

	/**
	 * 日期时间格式 Date time format
	 */
	private LabelSelectMeta dateTimeFormat;

	/**
	 * 显示字段名称
	 */
	private ButtonMeta showFieldName;

	/**
	 * 添加文件名的结果 Add filenames to result
	 */
	private LabelInputMeta addToResultFilenames;

	/**
	 * ------------------------------------------------------------------ 内容
	 */
	/**
	 * 追加
	 */
	private LabelInputMeta append;

	/**
	 * 头
	 */
	private LabelInputMeta headerEnabled;

	/**
	 * 脚
	 */
	private LabelInputMeta footerEnabled;

	/**
	 * 编码
	 */
	private LabelSelectMeta encoding;

	/**
	 * 每隔多少行分割
	 */
	private LabelInputMeta splitEvery;

	/**
	 * 工作表名称
	 */
	private LabelInputMeta sheetname;

	/**
	 * 保护工作表
	 */
	private LabelInputMeta protectsheet;

	/**
	 * 密码
	 */
	private LabelInputMeta password;

	/**
	 * Auto size columns
	 */
	private LabelInputMeta autoSizeColums;

	/**
	 * Template
	 */
	private ColumnFieldsetMeta template;

	/**
	 * 使用模板
	 */
	private LabelInputMeta templateEnabled;

	/**
	 * Excel模板
	 */
	private LabelInputMeta templateFileName;
//	private ButtonMeta templateFileNameSelecter;

	/**
	 * 追加Excel模板
	 */
	private LabelInputMeta templateAppend;

	/**
	 * 字段 2
	 */
	private LabelGridMeta outputFields;

// 	/**
// 	 * 最小宽度
// 	 */
// 	private ButtonMeta minWidth;


	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ExcelOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ExcelOutputMeta step = (ExcelOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------文件
			 ******************************************************************/
			// 文件名
			this.fileName = new LabelInputMeta(
					id + ".fileName",
					"文件名",
					null,
					null,
					null,
					step.getFileName(),
					null, ValidateForm.getInstance().setRequired(false));
//			
//			this.fileNameSelecter = new ButtonMeta(
//					id + ".btn.fileNameSelecter", id + ".btn.fileNameSelecter",
//					"浏览", "浏览");
//			this.fileName.addButton(new ButtonMeta[] { this.fileNameSelecter });
			this.fileName.setSingle(true);

			// 不在初始化时建立文件
			this.doNotOpenNewFileInit = new LabelInputMeta(
					id + ".doNotOpenNewFileInit",
					"不在初始化时建立文件",
					null,
					null,
					null,
					String.valueOf(step.isDoNotOpenNewFileInit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			// this.createFile.setSingle(true);

			// 扩展名
			this.extension = new LabelInputMeta(
					id + ".extension",
					"扩展名",
					null,
					null,
					null,
					step.getExtension(),
					null, ValidateForm.getInstance().setRequired(false));
			this.extension.setSingle(true);

			// 在文件里面包含步骤数
			this.stepNrInFilename = new LabelInputMeta(
					id + ".stepNrInFilename",
					"在文件里面包含步骤数",
					null,
					null,
					null,
					String.valueOf(step.isStepNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.stepNrInFilename.setSingle(true);

			// 在文件里包含日期
			this.dateInFilename = new LabelInputMeta(
					id + ".dateInFilename",
					"在文件里包含日期",
					null,
					null,
					null,
					String
							.valueOf(step.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dateInFilename.setSingle(true);
			this.dateInFilename.setDisabled(step.isSpecifyFormat());

			// 在文件里包含时间
			this.timeInFilename = new LabelInputMeta(
					id + ".timeInFilename",
					"在文件里包含时间",
					null,
					null,
					null,
					String
							.valueOf(step.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.timeInFilename.setSingle(true);
			this.timeInFilename.setDisabled(step.isSpecifyFormat());

			// 指定日期时间格式
			this.specifyFormat = new LabelInputMeta(
					id + ".specifyFormat",
					"指定日期时间格式",
					null,
					null,
					null,
					String
							.valueOf(step.isSpecifyFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.specifyFormat.setSingle(true);
			this.specifyFormat.addClick("jQuery.imeta.steps.exceloutput.listeners.specifyFormat");
			

			// 日期时间格式
			this.dateTimeFormat = new LabelSelectMeta(
					id + ".dateTimeFormat",
					"日期时间格式",
					null,
					null,
					null,
					step.getDateTimeFormat(),
					null, super.getDateFormats());
			this.dateTimeFormat.setSingle(true);
			this.dateTimeFormat.setDisabled(!step.isSpecifyFormat());

			// 显示字段名称
			DivMeta btnDiv0 = new DivMeta(new NullSimpleFormDataMeta(), true);
			
			this.showFieldName = new ButtonMeta(id + ".btn.showFieldName", id
					+ ".btn.ShowFieldName", "显示字段名称", "显示字段名称");
			
			this.showFieldName.appendTo(btnDiv0);

			// 把文件名添加到结果中
			this.addToResultFilenames = new LabelInputMeta(
					id + ".addToResultFilenames",
					"把文件名添加到结果中",
					null,
					null,
					null,
					String
							.valueOf(step.isAddToResultFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			this.meta.putTabContent(0, new BaseFormMeta[] { this.fileName,
					this.doNotOpenNewFileInit, this.extension,
					this.stepNrInFilename, this.dateInFilename,
					this.timeInFilename, this.specifyFormat,
					this.dateTimeFormat, this.addToResultFilenames });

			/*******************************************************************
			 * 标签1---------内容
			 ******************************************************************/
			// 追加
			this.append = new LabelInputMeta(
					id + ".append",
					"追加",
					null,
					null,
					null,
					String
							.valueOf(step.isAppend()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// 头
			this.headerEnabled = new LabelInputMeta(
					id + ".headerEnabled",
					"头",
					null,
					null,
					null,
					String
							.valueOf(step.isHeaderEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// 脚
			this.footerEnabled = new LabelInputMeta(
					id + ".footerEnabled",
					"脚",
					null,
					null,
					null,
					String
							.valueOf(step.isFooterEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// 编码
			this.encoding = new LabelSelectMeta(
					id + ".encoding",
					"编码",
					null,
					null,
					null,
					step.getEncoding(),
					null, super.getEncoding());
			this.encoding.setSingle(true);
			
			// 每隔多少行分割
			this.splitEvery = new LabelInputMeta(id + ".splitEvery", "每隔多少行分割",
					null, null, null,
					String.valueOf( step.getSplitEvery()), 
					null, ValidateForm.getInstance()
							.setRequired(false));
			this.splitEvery.setSingle(true);
			
			// 工作表名称
			this.sheetname = new LabelInputMeta(id + ".sheetname", "工作表名称",
					null, null, null, step.getSheetname(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.sheetname.setSingle(true);
			
			// 保护工作表
			this.protectsheet = new LabelInputMeta(id + ".protectsheet",
					"保护工作表", null, null,
					String.valueOf(step.isSheetProtected()),
					null,InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.protectsheet.addClick("jQuery.imeta.steps.exceloutput.listeners.protectsheet");
			
			// 密码
			this.password = new LabelInputMeta(id + ".password", "密码", null,
					null, null,
					step.getPassword(),
					null, ValidateForm.getInstance()
							.setRequired(false));
			this.password.setSingle(true);
			this.password.setDisabled(!step.isSheetProtected());
			
			// 自动列尺寸
			this.autoSizeColums = new LabelInputMeta(id + ".autoSizeColums",
					"自动列尺寸", null, null, null, 
					String.valueOf(step.isAutoSizeColums()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			
			// 模板
			this.template = new ColumnFieldsetMeta(null, "模板");
			this.template.setSingle(true);
			
			// 使用模板
			this.templateEnabled = new LabelInputMeta(id + ".templateEnabled",
					"使用模板", null, null, null, 
					String.valueOf(step.isTemplateEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.templateEnabled.addClick("jQuery.imeta.steps.exceloutput.listeners.templateEnabled");
			
			// Excel模板
			this.templateFileName = new LabelInputMeta(
					id + ".templateFileName", "Excel模板", null, null, null,
					step.getTemplateFileName(),
					null, ValidateForm.getInstance().setRequired(false));
			this.templateFileName.setDisabled(!step.isTemplateEnabled());
			
//			this.templateFileNameSelecter = new ButtonMeta(id
//					+ ".btn.templateFileNameSelecter", id
//					+ ".btn.templateFileNameSelecter", "浏览", "浏览");
//			this.templateFileName
//					.addButton(new ButtonMeta[] { this.templateFileNameSelecter });
			
			this.templateFileName.setSingle(true);
			this.templateFileName.setDisabled(true);
			
			// 追加Excel模板
			this.templateAppend = new LabelInputMeta(id + ".templateAppend",
					"追加Excel模板", null, null, null, 
					String.valueOf(step.isTemplateAppend()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.templateAppend.setDisabled(true);

			this.template.putFieldsetsContent(new BaseFormMeta[] {
					this.templateEnabled, this.templateFileName,
					this.templateAppend });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.append,
					this.headerEnabled, this.footerEnabled, this.encoding,
					this.splitEvery, this.sheetname, this.protectsheet,
					this.password, this.autoSizeColums, this.template });

			/*******************************************************************
			 * 标签2---------字段
			 ******************************************************************/
			// 字段  
			this.outputFields = new LabelGridMeta(id + "_fields", null, 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArray(
					ValueMetaInterface.typeCodes, false));

			
			this.outputFields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",null, false, 100),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",null, false, 100),
			});
			
			ExcelField[] outputFields1 = step.getOutputFields();
			if(outputFields1 != null && outputFields1.length > 0){
				for(int i = 0; i < outputFields1.length; i++){
					this.outputFields.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,outputFields1[i].getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,ValueMeta.getTypeDesc(outputFields1[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,String.valueOf(outputFields1[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT)
							
					});	
				}
			}
			this.outputFields.setSingle(true);
			this.outputFields.setHasBottomBar(true);
			this.outputFields.setHasAdd(true, true, 
					"jQuery.imeta.steps.exceloutput.btn.outputFieldsAdd");
			this.outputFields.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");

			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.exceloutput.btn.getfields").appendTo(div);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.outputFields,
					div });

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
