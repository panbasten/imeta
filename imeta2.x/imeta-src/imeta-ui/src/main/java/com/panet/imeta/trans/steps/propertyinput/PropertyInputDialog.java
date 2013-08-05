package com.panet.imeta.trans.steps.propertyinput;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class PropertyInputDialog extends BaseStepDialog implements
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

	// File

	/**
	 * Filenames from field
	 */
	private ColumnFieldsetMeta fileNameField;

	/**
	 * Filename is defined in a field?
	 */
	private LabelInputMeta filefield;

	/**
	 * Get filename from fields
	 */
	private LabelSelectMeta dynamicFilenameField;

	/**
	 * File or directory
	 */
	// private LabelInputMeta fileOrdirectory;
	// private ButtonMeta fileOrdirectoryAdd;
	// private ButtonMeta fileOrdirectorySelecter;
	/**
	 * Regular Expression
	 */
	// private LabelInputMeta regularExpression;
	/**
	 * Selected files
	 */
	private LabelGridMeta selectedFiles;

	// private ButtonMeta delete;
	//	
	// private ButtonMeta edit;
	//
	// private ButtonMeta showFilename;

	// Content

	/**
	 * Additional Fields
	 */
	private ColumnFieldsetMeta additionalFieldsColum;

	/**
	 * Include filename in output
	 */
	private LabelInputMeta includeFilename;

	/**
	 * Filename fieldname
	 */
	private LabelInputMeta filenameField;

	/**
	 * Rownum in output
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * Rownum fieldname
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Reset Rownum per file?
	 */
	private LabelInputMeta resetRowNumber;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Add to result filename
	 */
	private ColumnFieldsetMeta addResultFilenameColum;

	/**
	 * Add files to result
	 */
	private LabelInputMeta isaddresult;

	// Fields

	/**
	 * Files
	 */
	private LabelGridMeta fields;

	/**
	 * 获取字段
	 */
//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public PropertyInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			PropertyInputMeta step = (PropertyInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------File
			 ******************************************************************/
			// Filenames from field
			this.fileNameField = new ColumnFieldsetMeta(id, "从字段得到文件名");
			this.fileNameField.setSingle(true);

			// Filename is defined in a field ?
			boolean isFileField = step.isFileField();
			this.filefield = new LabelInputMeta(id + ".filefield", "在字段里定义？",
					null, null, null, String.valueOf(step.isFileField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.filefield
					.addClick("jQuery.imeta.steps.propertyinput.listeners.filefieldListeners");

			// Get filename from field
			this.dynamicFilenameField = new LabelSelectMeta(id
					+ ".dynamicFilenameField", "从字段获取文件名", null, null, null,
					step.getDynamicFilenameField(), null, super
							.getPrevStepResultFields());
			this.dynamicFilenameField.setSingle(true);
			this.dynamicFilenameField.setHasEmpty(true);
			this.dynamicFilenameField.setDisabled(!step.isFileField());

			this.fileNameField.putFieldsetsContent(new BaseFormMeta[] {
					this.filefield, this.dynamicFilenameField });

			// File or directory
			// this.fileOrdirectory = new LabelInputMeta(id +
			// ".fileOrdirectory",
			// "文件或目录",null, null, null, String.valueOf(step.getFileName()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.fileOrdirectoryAdd = new ButtonMeta(id
			// + ".btn.fileOrdirectoryAdd", id + ".btn.fileOrdirectoryAdd",
			// "增加(A)...", "增加");
			// this.fileOrdirectoryAdd.setDisabled(step.isFileField());
			// this.fileOrdirectorySelecter = new ButtonMeta(id
			// + ".btn.fileOrdirectorySelecter", id +
			// ".btn.fileOrdirectorySelecter",
			// "浏览(B)...", "浏览");
			// this.fileOrdirectorySelecter.setDisabled(step.isFileField());
			// this.fileOrdirectory.addButton(new
			// ButtonMeta[]{fileOrdirectoryAdd,
			// fileOrdirectorySelecter});
			// this.fileOrdirectory.setSingle(true);
			// this.fileOrdirectory.setDisabled(step.isFileField());

			// Regular Expression
			// this.regularExpression = new LabelInputMeta(id +
			// ".regularExpression",
			// "正则表达式",null, null, null, String.valueOf(step.getFileMask()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// this.regularExpression.setDisabled(step.isFileField());

			// Selected files
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件名称", 120);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件＼目录", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 200), });

			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles
					.setHasAdd(true, !isFileField,
							"jQuery.imeta.steps.propertyinput.selectedfiles.btn.fieldAdd");
			this.selectedFiles
					.setHasDelete(true, !isFileField,
							"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			if (fileName != null && fileName.length > 0) {
				GridCellDataMeta name, mask;
				for (int i = 0; i < fileName.length; i++) {

					name = new GridCellDataMeta(null, step.getFileName()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isFileField);
					mask = new GridCellDataMeta(null, step.getFileMask()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					mask.setDisabled(isFileField);
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1), name, mask });
				}
			}
			// DivMeta showfilenameBtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.delete = new ButtonMeta(
			// id + ".btn.delete", id + ".btn.delete",
			// "删除", "删除");
			// this.delete.appendTo(showfilenameBtn);
			// this.delete.setDisabled(step.isFileField());
			//			
			// this.edit = new ButtonMeta(
			// id + ".btn.edit", id + ".btn.edit",
			// "编辑", "编辑");
			// this.edit.appendTo(showfilenameBtn);
			// this.edit.setDisabled(step.isFileField());
			//			
			// this.showFilename = new ButtonMeta(
			// id + ".btn.showFilename", id + ".btn.showFilename",
			// "查看文件", "查看文件");
			// this.showFilename.appendTo(showfilenameBtn);
			// this.showFilename.setDisabled(step.isFileField());

			// this.meta.putTabContent(0, new BaseFormMeta[]
			// {this.fileNameField,
			// this.fileOrdirectory,this.regularExpression, this.selectedFiles
			// });
			this.meta.putTabContent(0, new BaseFormMeta[] { this.fileNameField,
					this.selectedFiles });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/

			// Additional Fields
			this.additionalFieldsColum = new ColumnFieldsetMeta(id, "其他领域");
			this.additionalFieldsColum.setSingle(true);
			// Include filename in output
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"包括输出的文件", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilename
					.addClick("jQuery.imeta.steps.propertyinput.listeners.includeFilenametListeners");
			// Filename fieldname
			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"文件字段", null, null, null, step
							.getFilenameField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());
			this.filenameField.setDisabled(!step.isFileField());
			// Rownum in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出行号", null, null, null, String
							.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.propertyinput.listeners.includeRowNumberListeners");
			// Rownum fieldname
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段名", null, null, null, step
							.getRowNumberField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// Reset Rownum per file
			this.resetRowNumber = new LabelInputMeta(id + ".resetRowNumber",
					"重置行号每个文件", null, null, null, String.valueOf(step
							.resetRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.resetRowNumber.setDisabled(!step.includeRowNumber());

			this.additionalFieldsColum.putFieldsetsContent(new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeRowNumber, this.rowNumberField,
					this.resetRowNumber });

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			// Add to result filename
			this.addResultFilenameColum = new ColumnFieldsetMeta(id, "添加到结果文件名");
			this.addResultFilenameColum.setSingle(true);
			// Add files to result filename
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.addResultFilenameColum
					.putFieldsetsContent(new BaseFormMeta[] { this.isaddresult });

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.additionalFieldsColum, this.rowLimit,
					this.addResultFilenameColum });

			/*******************************************************************
			 * 标签2---------Filters
			 ******************************************************************/
			this.fields = new LabelGridMeta(id + "_fields", null, 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldColumn", "列",
							null, false, 50)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									PropertyInputField.ColumnDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "组",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldTrimType",
							"去空格类型", null, false, 120)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.trimTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldRepeat", "重复",
							null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.propertyinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			PropertyInputField[] propertyInputField = step.getInputFields();
			if (propertyInputField != null && propertyInputField.length > 0)
				for (int i = 0; i < propertyInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, propertyInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(propertyInputField[i]
											.getColumn()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(propertyInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									String.valueOf(propertyInputField[i]
											.getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(propertyInputField[i]
											.getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(propertyInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(propertyInputField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, propertyInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, propertyInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(propertyInputField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									String.valueOf(propertyInputField[i]
											.isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 获取字段
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.propertyinput.btn.getfields")
			.appendTo(fieldBtn);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields,
					fieldBtn });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
