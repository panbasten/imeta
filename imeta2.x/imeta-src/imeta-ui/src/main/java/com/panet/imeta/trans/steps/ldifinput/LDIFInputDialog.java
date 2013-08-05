package com.panet.imeta.trans.steps.ldifinput;

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
import com.panet.iform.forms.grid.GridMeta;
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

public class LDIFInputDialog extends BaseStepDialog implements
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
	 * Files origin
	 */
	private ColumnFieldsetMeta filesOriginColum;
	/**
	 * Get fileName from field
	 */
	private LabelInputMeta filefield;
	/**
	 * fileName field
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
	 * 已选择的文件名称
	 */
	private LabelGridMeta selectedFiles;

	/**
	 * 删除
	 */
	// private ButtonMeta deletebtn;
	// /**
	// * 编辑
	// */
	// private ButtonMeta editbtn;
	//	
	// /**
	// * 显示文件名
	// */
	// private ButtonMeta showFilesName;
	// Content
	/**
	 * Include filename in output
	 */
	private LabelInputMeta includeFilename;

	/**
	 * Filename field name
	 */
	private LabelInputMeta filenameField;

	/**
	 * Row number in output
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * Row number field name
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Include content type
	 */
	private LabelInputMeta includeContentType;

	/**
	 * content type field name
	 */
	private LabelInputMeta contentTypeField;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 多值字段分隔符
	 */
	private LabelInputMeta multiValuedSeparator;

	/**
	 * Result filenames
	 */
	private ColumnFieldsetMeta resultFilenamesColum;
	/**
	 * Add filename to result
	 */
	private LabelInputMeta addtoresultfilename;

	// Fields
	/**
	 * 字段列表
	 */
	private GridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public LDIFInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			LDIFInputMeta step = (LDIFInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------File
			 ******************************************************************/
			// Files origin
			this.filesOriginColum = new ColumnFieldsetMeta(id, "文件来源");
			this.filesOriginColum.setSingle(true);
			// Get fileName from field
			boolean isFileField = step.isFileField();
			this.filefield = new LabelInputMeta(id + ".filefield", "从字段获取文件名",
					null, null, null, String.valueOf(step.isFileField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.filefield
					.addClick("jQuery.imeta.steps.ldifinput.listeners.filefieldListeners");
			// Filename field
			this.dynamicFilenameField = new LabelSelectMeta(id
					+ ".dynamicFilenameField", "获取文件字段", null, null, null, step
					.getDynamicFilenameField(), null, super.getPrevStepResultFields());
			this.dynamicFilenameField.setSingle(true);
			this.dynamicFilenameField.setHasEmpty(true);
			this.dynamicFilenameField.setDisabled(!step.isFileField());

			this.filesOriginColum.putFieldsetsContent(new BaseFormMeta[] {
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
			// 已选择的文件名称
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件名称", 120);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件＼目录", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 200), });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, !isFileField,
					"jQuery.imeta.steps.ldifinput.selectedfiles.btn.fieldAdd");
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

			// // 显示文件名称
			// DivMeta showFilesNamebtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.deletebtn = new ButtonMeta(id + ".deletebtn", id
			// + ".deletebtn", "删除", "删除");
			// this.deletebtn.appendTo(showFilesNamebtn);
			// this.deletebtn.setDisabled(step.isFileField());
			// this.editbtn = new ButtonMeta(id + ".editbtn", id
			// + ".editbtn", "编辑", "编辑");
			// this.editbtn.appendTo(showFilesNamebtn);
			// this.editbtn.setDisabled(step.isFileField());
			// this.showFilesName = new ButtonMeta(id + ".showFilesName", id
			// + ".showFilesName", "显示文件名称", "显示文件名称");
			// this.showFilesName.appendTo(showFilesNamebtn);
			// this.showFilesName.setDisabled(step.isFileField());

			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.filesOriginColum,
					// this.fileOrdirectory,
					// this.regularExpression,
					this.selectedFiles });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Include filename in output
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"包括输出的文件", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilename.setDisabled(step.isFileField());
			this.includeFilename
					.addClick("jQuery.imeta.steps.ldifinput.listeners.includeFilenameListeners");

			// Filename field name
			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"文件字段名称", null, null, null, step
							.getFilenameField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());
			this.filenameField.setDisabled(!step.isFileField());

			// Row number in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出行号", null, null, null, String
							.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.ldifinput.listeners.includeRowNumberListeners");

			// Row number field name
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段名称", null, null, null, step
							.getRowNumberField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// Include content type
			this.includeContentType = new LabelInputMeta(id
					+ ".includeContentType", "包括内容类型", null, null, null, String
					.valueOf(step.includeContentType()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));

			// content type field name
			this.contentTypeField = new LabelInputMeta(
					id + ".contentTypeField", "内容类型字段名称", null, null, null,
					step.getContentTypeField(), null,
					ValidateForm.getInstance().setRequired(true));

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			this.rowLimit.setDisabled(step.isFileField());

			// 多值字段分隔符
			this.multiValuedSeparator = new LabelInputMeta(id
					+ ".multiValuedSeparator", "多值字段分隔符", null, null, null,
					String.valueOf(step.getMultiValuedSeparator()), null,
					ValidateForm.getInstance().setRequired(true));
			this.multiValuedSeparator.setSingle(true);

			// Result filenames
			this.resultFilenamesColum = new ColumnFieldsetMeta(id, "结果文件名");
			this.resultFilenamesColum.setSingle(true);
			// Add filename to result
			this.addtoresultfilename = new LabelInputMeta(id
					+ ".addtoresultfilename", "添加文件名", null, null, null, String
					.valueOf(step.AddToResultFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.resultFilenamesColum
					.putFieldsetsContent(new BaseFormMeta[] { this.addtoresultfilename });

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeRowNumber, this.rowNumberField,
					this.includeContentType, this.contentTypeField,
					this.rowLimit, this.multiValuedSeparator,
					this.resultFilenamesColum });

			/*******************************************************************
			 * 标签2---------Filters
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 200, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldAttribute", "属性",
							null, false, 100),
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
					"jQuery.imeta.steps.ldifinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			LDIFInputField[] LDIFInputField = step.getInputFields();
			if (LDIFInputField != null && LDIFInputField.length > 0)
				for (int i = 0; i < LDIFInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, LDIFInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].getAttribut()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i]
											.getDecimalSymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(LDIFInputField[i]
											.getGroupSymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i]
											.getTrimTypeDesc()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(LDIFInputField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT)

					});
				}

			// 获取字段
			DivMeta getFieldsBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.ldifinput.btn.getfields")
					.appendTo(getFieldsBtn);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields,
					getFieldsBtn });

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
