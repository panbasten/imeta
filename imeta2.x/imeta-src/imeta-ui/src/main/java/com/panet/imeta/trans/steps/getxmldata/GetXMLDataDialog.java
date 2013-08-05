package com.panet.imeta.trans.steps.getxmldata;

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

public class GetXMLDataDialog extends BaseStepDialog implements
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
	 * XML source from field
	 */
	private ColumnFieldsetMeta xmlSourceColum;
	/**
	 * XML source is defined in a field
	 */
	private LabelInputMeta IsInFields;
	/**
	 * XML source is a filename
	 */
	private LabelInputMeta IsAFile;
	/**
	 * Read source as URL
	 */
	private LabelInputMeta readurl;
	/**
	 * get XML source from a field
	 */
	private LabelSelectMeta XmlField;

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
	 * Setting
	 */
	private ColumnFieldsetMeta settingColum;
	/**
	 * Loop path
	 */
	private LabelInputMeta loopxpath;
	// private ButtonMeta loopPathBtn;
	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;
	/**
	 * Name space aware
	 */
	private LabelInputMeta nameSpaceAware;
	/**
	 * Ignore comments
	 */
	private LabelInputMeta ignorecomments;
	/**
	 * Validate XML
	 */
	private LabelInputMeta validating;
	/**
	 * Use token
	 */
	private LabelInputMeta usetoken;
	/**
	 * Ignore empty file
	 */
	private LabelInputMeta IsIgnoreEmptyFile;
	/**
	 * Do not raise an error if no files
	 */
	private LabelInputMeta doNotFailIfNoFile;
	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;
	/**
	 * Prune path to handle large files
	 */
	private LabelInputMeta prunePath;

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
	 * Add to result filename
	 */
	private ColumnFieldsetMeta addResultFilenameColum;
	/**
	 * Add files to result filename
	 */
	private LabelInputMeta addResultFile;

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
	public GetXMLDataDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			GetXMLDataMeta step = (GetXMLDataMeta) super.getStep();

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
			// XML source from field
			this.xmlSourceColum = new ColumnFieldsetMeta(id, "XML源来自字段");
			this.xmlSourceColum.setSingle(true);
			// XML source is defined in a field
			boolean isInFields = step.getIsInFields();
			this.IsInFields = new LabelInputMeta(id + ".IsInFields",
					"XML源是一个字段", null, null, null, String.valueOf(step
							.getIsInFields()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.IsInFields.setSingle(true);
			this.IsInFields
					.addClick("jQuery.imeta.steps.getxmldata.listeners.IsInFieldsListeners");
			// XML source is a filename
			this.IsAFile = new LabelInputMeta(id + ".IsAFile", "XML源是一个文件名",
					null, null, null, String.valueOf(step.getIsAFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.IsAFile.setSingle(true);
			this.IsAFile.setDisabled(!step.getIsInFields());
			// Read source as URL
			this.readurl = new LabelInputMeta(id + ".readurl", "阅读来源网址", null,
					null, null, String.valueOf(step.isReadUrl()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.readurl.setSingle(true);
			this.readurl.setDisabled(!step.getIsInFields());
			// get XML source from a field
			this.XmlField = new LabelSelectMeta(id + ".XmlField", "从字段获取XML源",
					null, null, null, step.getXMLField(), null, super
							.getPrevStepResultFields());
			this.XmlField.setSingle(true);
			this.XmlField.setHasEmpty(true);
			this.XmlField.setDisabled(!step.getIsInFields());

			this.xmlSourceColum
					.putFieldsetsContent(new BaseFormMeta[] { this.IsInFields,
							this.IsAFile, this.readurl, this.XmlField });

			// File or directory
			// this.fileOrdirectory = new LabelInputMeta(id +
			// ".fileOrdirectory",
			// "文件或目录",null, null, null, String.valueOf(step.getFileName()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.fileOrdirectoryAdd = new ButtonMeta(id
			// + ".btn.fileOrdirectoryAdd", id + ".btn.fileOrdirectoryAdd",
			// "增加(A)...", "增加");
			// this.fileOrdirectoryAdd.setDisabled(step.getIsInFields());
			// this.fileOrdirectorySelecter = new ButtonMeta(id
			// + ".btn.fileOrdirectorySelecter", id +
			// ".btn.fileOrdirectorySelecter",
			// "浏览(B)...", "浏览");
			// this.fileOrdirectorySelecter.setDisabled(step.getIsInFields());
			// this.fileOrdirectory.addButton(new
			// ButtonMeta[]{fileOrdirectoryAdd,
			// fileOrdirectorySelecter});
			// this.fileOrdirectory.setSingle(true);
			// this.fileOrdirectory.setDisabled(step.getIsInFields());

			// Regular Expression
			// this.regularExpression = new LabelInputMeta(id +
			// ".regularExpression",
			// "正规表达式",null, null, null, String.valueOf(step.getFileMask()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// this.regularExpression.setDisabled(step.getIsInFields());

			// Selected files
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"选定的文件", 200);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件/目录", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedFiles.fileRequired",
							"要求", null, false, 150), });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, !isInFields,
					"jQuery.imeta.steps.getxmldata.selectedfiles.btn.fieldAdd");
			this.selectedFiles
					.setHasDelete(true, !isInFields,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			String[] fileMark = step.getFileMask();
			String[] fileRequired = step.getFileRequired();
			if (fileName != null && fileName.length > 0) {
				GridCellDataMeta name, mask, required;
				for (int i = 0; i < fileName.length; i++) {

					name = new GridCellDataMeta(null, fileName[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isInFields);
					mask = new GridCellDataMeta(null, fileMark[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					mask.setDisabled(isInFields);
					required = new GridCellDataMeta(null, fileRequired[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					required.setDisabled(isInFields);
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1), name, mask, required });
				}
			}

			// DivMeta selectedFilesbtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.delete = new ButtonMeta(
			// id + ".delete", id + ".delete",
			// "删除", "删除");
			// this.delete.appendTo(selectedFilesbtn);
			// this.delete.setDisabled(step.getIsInFields());
			// this.edit = new ButtonMeta(
			// id + ".edit", id + ".edit",
			// "编辑", "编辑");
			// this.edit.appendTo(selectedFilesbtn);
			// this.edit.setDisabled(step.getIsInFields());
			// this.showFilename = new ButtonMeta(
			// id + ".showFilename", id + ".showFilename",
			// "查看文件", "查看文件");
			// this.showFilename.appendTo(selectedFilesbtn);
			// this.showFilename.setDisabled(step.getIsInFields());

			// this.meta.putTabContent(0, new BaseFormMeta[]
			// {this.xmlSourceColum,
			// this.fileOrdirectory,this.regularExpression, this.selectedFiles
			// });
			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.xmlSourceColum, this.selectedFiles });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// 设置
			this.settingColum = new ColumnFieldsetMeta(id, "设置");
			this.settingColum.setSingle(true);
			// Loop path
			this.loopxpath = new LabelInputMeta(id + ".loopxpath", "循环路径",
					null, null, null, step.getLoopXPath(), null, ValidateForm
							.getInstance().setRequired(true));
			this.loopxpath.setSingle(true);
			// this.loopPathBtn = new ButtonMeta(id
			// + ".btn.loopPathBtn", id + ".btn.loopPathBtn",
			// "获取的XPath节点", "获取的XPath节点");
			// this.loopxpath.addButton(new ButtonMeta[]{this.loopPathBtn});
			// Encoding
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, String.valueOf(step.getEncoding()), null, super
							.getEncoding());
			this.encoding.setSingle(true);
			this.encoding.setDisabled(step.getIsInFields());
			// Name space aware
			this.nameSpaceAware = new LabelInputMeta(id + ".nameSpaceAware",
					"名称空间", null, null, null, String.valueOf(step
							.isNamespaceAware()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Ignore comments
			this.ignorecomments = new LabelInputMeta(id + ".ignorecomments",
					"忽略评论", null, null, null, String.valueOf(step
							.isIgnoreComments()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Validate XML
			this.validating = new LabelInputMeta(id + ".validating", "验证XML",
					null, null, null, String.valueOf(step.isValidating()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Use token
			this.usetoken = new LabelInputMeta(id + ".usetoken", "使用标记", null,
					null, null, String.valueOf(step.isuseToken()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Ignore empty file
			this.IsIgnoreEmptyFile = new LabelInputMeta(id
					+ ".IsIgnoreEmptyFile", "忽略空文件", null, null, null, String
					.valueOf(step.isIgnoreEmptyFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Do not raise an error if no files
			this.doNotFailIfNoFile = new LabelInputMeta(id
					+ ".doNotFailIfNoFile", "如果没有文件不会引起错误", null, null, null,
					String.valueOf(step.isdoNotFailIfNoFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null, ValidateForm
					.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			this.rowLimit.setDisabled(step.getIsInFields());
			// Prune path to handle large files
			this.prunePath = new LabelInputMeta(id + ".prunePath",
					"删除来处理大文件的路径", null, null, "删除来处理大文件的路径未填", step.getPrunePath(), null,
					ValidateForm.getInstance().setRequired(true));
			this.prunePath.setSingle(true);
			this.prunePath.setDisabled(step.getIsInFields());

			this.settingColum.putFieldsetsContent(new BaseFormMeta[] {
					this.loopxpath, this.encoding, this.nameSpaceAware,
					this.ignorecomments, this.validating, this.usetoken,
					this.IsIgnoreEmptyFile, this.doNotFailIfNoFile,
					this.rowLimit, this.prunePath });

			// Additional Fields
			this.additionalFieldsColum = new ColumnFieldsetMeta(id, "其他字段");
			this.additionalFieldsColum.setSingle(true);
			// Include filename in output
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"包含输出文件", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilename
					.addClick("jQuery.imeta.steps.getxmldata.listeners.includeFilenameListeners");
			this.includeFilename.setDisabled(step.getIsInFields());
			// Filename fieldname
			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"文件字段", null, null, null, step.getFilenameField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());
			this.filenameField.setDisabled(!step.getIsInFields());
			// Rownum in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出行号", null, null, null, String
							.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.getxmldata.listeners.includeRowNumberListeners");
			// Rownum fieldnames
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			this.additionalFieldsColum.putFieldsetsContent(new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeRowNumber, this.rowNumberField });

			// Add to result filename
			this.addResultFilenameColum = new ColumnFieldsetMeta(id, "添加到结果文件名");
			this.addResultFilenameColum.setSingle(true);
			// Add files to result filename
			this.addResultFile = new LabelInputMeta(id + ".addResultFile",
					"文件添加到结果文件名", null, null, null, String.valueOf(step
							.addResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.addResultFilenameColum
					.putFieldsetsContent(new BaseFormMeta[] { this.addResultFile });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.settingColum,
					this.additionalFieldsColum, this.addResultFilenameColum });

			/*******************************************************************
			 * 标签2---------Filters
			 ******************************************************************/
			this.fields = new LabelGridMeta(id + "_fields", null, 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldXpath", "XPach",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldElement", "元素",
							null, false, 50)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									GetXMLDataField.ElementTypeDesc, false)),
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
					"jQuery.imeta.steps.getxmldata.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			GetXMLDataField[] getXMLDataField = step.getInputFields();
			if (getXMLDataField != null && getXMLDataField.length > 0)
				for (int i = 0; i < getXMLDataField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, getXMLDataField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].getXPath()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i]
											.getElementType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(getXMLDataField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, getXMLDataField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, getXMLDataField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(getXMLDataField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 获取字段
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.getxmldata.btn.getfields")
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
