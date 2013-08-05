package com.panet.imeta.trans.steps.excelinput;

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

public class ExcelInputDialog extends BaseStepDialog implements
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

	// 文件
	/**
	 * 文件或目录
	 */
	// private LabelInputMeta fileOrDir;
	/**
	 * 规则表达式
	 */
	// private LabelInputMeta ruleExpression;
	/**
	 * 选中的文件列表
	 */
	private LabelGridMeta selectedFiles;

	/**
	 * 从前面的步骤获取文件名的域
	 */
	private ColumnFieldsetMeta getFilesNameField;

	/**
	 * 从前面的步骤获取文件名
	 */
	private LabelInputMeta getFilesName;

	/**
	 * 从哪个步骤读文件名
	 */
	private LabelSelectMeta stepFilesName;

	/**
	 * 从哪个步骤读文件名
	 */
	private LabelInputMeta stepFieldFilesName;

	// 工作表
	/**
	 * 要读取的工作表列表
	 */
	private LabelGridMeta selectedSheets;
	
	/**
	 * 获取工作表名称
	 */
	private ButtonMeta getSheetsName;
	
	// 内容
	/**
	 * 头部
	 */
	private LabelInputMeta startsWithHeader;

	/**
	 * 非空记录
	 */
	private LabelInputMeta ignoreEmptyRows;

	/**
	 * 停在空记录
	 */
	private LabelInputMeta stopOnEmpty;

	/**
	 * 文件名称字段
	 */
	private LabelInputMeta fileField;

	/**
	 * 工作表名称字段
	 */
	private LabelInputMeta sheetField;

	/**
	 * 表单的行号列
	 */
	private LabelInputMeta sheetRowNumberField;

	/**
	 * 行号列
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * 限制
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 编码
	 */
	private LabelSelectMeta encoding;

	/**
	 * 结果文件名的字段
	 */
	private ColumnFieldsetMeta resultFilesNameFieldColumn;

	/**
	 * 添加文件名
	 */
	private LabelInputMeta addToResult;

	// 错误处理
	/**
	 * 严格类型
	 */
	private LabelInputMeta strictTypes;

	/**
	 * 忽略错误
	 */
	private LabelInputMeta errorIgnored;

	/**
	 * 跳过错误行
	 */
	private LabelInputMeta errorLineSkipped;

	/**
	 * 告警文件目录
	 */
	private LabelInputMeta warningFilesDestinationDirectory;

	/**
	 * 告警文件目录扩展名
	 */
	private LabelInputMeta warningFilesExtension;
	// private ButtonMeta warningFilesVariableBtn;
	// private ButtonMeta warningFilesBrowseBtn;

	/**
	 * 错误文件目录
	 */
	private LabelInputMeta errorFilesDestinationDirectory;

	/**
	 * 错误文件目录扩展名
	 */
	private LabelInputMeta errorFilesExtension;
	// private ButtonMeta errorFilesVariableBtn;
	// private ButtonMeta errorFilesBrowseBtn;

	/**
	 * 失败的记录数文件目录
	 */
	private LabelInputMeta lineNumberFilesDestinationDirectory;

	/**
	 * 失败的记录数文件目录扩展名
	 */
	private LabelInputMeta lineNumberFilesExtension;
	// private ButtonMeta lineNumberFilesVariableBtn;
	// private ButtonMeta lineNumberFilesBrowseBtn;

	// 字段
	/**
	 * 字段列表
	 */
	private GridMeta fields;

	/**
	 * 获取来自头部数据的字段
	 */
//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ExcelInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ExcelInputMeta step = (ExcelInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "工作表", "内容",
					"错误处理", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0--文件
			 ******************************************************************/
			// 文件或目录
			// this.fileOrDir = new LabelInputMeta(id + ".fileOrDir", "文件或目录",
			// null, null, null, String.valueOf(step.getFileName()), null,
			// ValidateForm.getInstance()
			// .setRequired(true));
			// this.fileOrDir.setSingle(true);
			// this.fileOrDir.setDisabled(step.isAcceptingFilenames());
			// 规则表达式
			// this.ruleExpression = new LabelInputMeta(id + ".ruleExpression",
			// "规则表达式", null, null, null, String.valueOf(step.getFileMask()),
			// null, null);
			// this.ruleExpression.setSingle(true);
			// this.ruleExpression.setDisabled(step.isAcceptingFilenames());
			// 选中的文件
			boolean isAccepting = step.isAcceptingFilenames();
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"选中的文件", 200);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件＼目录", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedFiles.fileRequired",
							"要求", null, false, 150) });

			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, !isAccepting,
					"jQuery.imeta.steps.excelinput.selectedfiles.btn.fieldAdd");
			this.selectedFiles
					.setHasDelete(true, !isAccepting,
							"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					GridCellDataMeta name, mask, required;
					name = new GridCellDataMeta(null, step.getFileName()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isAccepting);
					mask = new GridCellDataMeta(null, step.getFileMask()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					mask.setDisabled(isAccepting);
					required = new GridCellDataMeta(null, step
							.getFileRequired()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					required.setDisabled(isAccepting);
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1), name, mask, required });
				}
			}

			// 从前面的步骤获取文件名
			this.getFilesNameField = new ColumnFieldsetMeta(null, "从前面的步骤获取文件名");
			this.getFilesNameField.setSingle(true);
			// 从前面的步骤获取文件名
			this.getFilesName = new LabelInputMeta(id + ".getFilesName",
					"从前面的步骤获取文件名", null, null, null, String.valueOf(step
							.isAcceptingFilenames()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.getFilesName.setSingle(true);
			this.getFilesName
					.addClick("jQuery.imeta.steps.excelinput.listeners.getFilesNameListeners");
			// 从哪个步骤读文件名
			this.stepFilesName = new LabelSelectMeta(id + ".stepFilesName",
					"从哪个步骤读文件名", null, null, null, step.getAcceptingStepName(),
					null, super.getPrevStepNames());
			this.stepFilesName.setSingle(true);
			this.stepFilesName.setDisabled(!step.isAcceptingFilenames());
			this.stepFilesName.setHasEmpty(true);
			// 保存文件名的字段名
			this.stepFieldFilesName = new LabelInputMeta(id
					+ ".stepFieldFilesName", "保存文件名的字段名", null, null, null,
					step.getAcceptingField(), null, null);
			this.stepFieldFilesName.setSingle(true);
			this.stepFieldFilesName.setDisabled(!step.isAcceptingFilenames());

			this.getFilesNameField.putFieldsetsContent(new BaseFormMeta[] {
					this.getFilesName, this.stepFilesName,
					this.stepFieldFilesName });

			// this.meta.putTabContent(0, new BaseFormMeta[] { this.fileOrDir,
			// this.ruleExpression, this.selectedFiles,
			// this.getFilesNameField });
			this.meta.putTabContent(0, new BaseFormMeta[] { this.selectedFiles,
					this.getFilesNameField });

			/*******************************************************************
			 * 标签1--工作表
			 ******************************************************************/
			// 要读取的工作表列表
			this.selectedSheets = new LabelGridMeta(id + "_selectedSheets",
					"要读取的工作表列表", 220);
			this.selectedSheets.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedSheets.sheetId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedSheets.sheetName",
							"工作表名称", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedSheets.startRow",
							"起始行", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedSheets.startColumn",
							"起始列", null, false, 150) });
			this.selectedSheets.setSingle(true);
			this.selectedSheets.setHasBottomBar(true);
			this.selectedSheets
					.setHasAdd(true, true,
							"jQuery.imeta.steps.excelinput.selectedsheets.btn.fieldAdd");
			this.selectedSheets
					.setHasDelete(true, true,
							"jQuery.imeta.parameter.fieldsDelete");
			// TODO DEMO
			String[] sheetName = step.getSheetName();
			if (sheetName != null && sheetName.length > 0) {
				for (int i = 0; i < sheetName.length; i++) {
					this.selectedSheets.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getSheetName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getStartRow()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getStartColumn()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			}

			// 获取工作表名称
			DivMeta getSheetsNamebtn = new DivMeta(
					new NullSimpleFormDataMeta(), true);
			this.getSheetsName = new ButtonMeta(id + ".getSheetsName", id
					+ ".getSheetsName", "获取工作表名称", "获取工作表名称");
			this.getSheetsName.addClick("jQuery.imeta.steps.excelinput.btn.getsheetsname");
			this.getSheetsName.appendTo(getSheetsNamebtn);

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.selectedSheets, getSheetsNamebtn});

			/*******************************************************************
			 * 标签2--内容
			 ******************************************************************/
			// 头部
			this.startsWithHeader = new LabelInputMeta(
					id + ".startsWithHeader", "头部", null, null, null, String
							.valueOf(step.startsWithHeader()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// 非空记录
			this.ignoreEmptyRows = new LabelInputMeta(id + ".ignoreEmptyRows",
					"非空记录", null, null, null, String.valueOf(step
							.ignoreEmptyRows()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// 停在空记录
			this.stopOnEmpty = new LabelInputMeta(id + ".stopOnEmpty", "停在空记录",
					null, null, null, String.valueOf(step.stopOnEmpty()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// 文件名称字段
			this.fileField = new LabelInputMeta(id + ".fileField", "文件名称字段",
					null, null, null, step.getFileField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.fileField.setSingle(true);
			// 工作表名称字段
			this.sheetField = new LabelInputMeta(id + ".sheetField", "工作表名称字段",
					null, null, null, step.getSheetField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.sheetField.setSingle(true);
			// 表单的行号列
			this.sheetRowNumberField = new LabelInputMeta(id
					+ ".sheetRowNumberField", "表单的行号列", null, null, null, step
					.getSheetRowNumberField(), null, ValidateForm.getInstance()
					.setRequired(true));
			this.sheetRowNumberField.setSingle(true);
			// 行号列
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号列", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setSingle(true);
			// 限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			// 编码
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, String.valueOf(step.getEncoding()), null, super
							.getEncoding());
			this.encoding.setSingle(true);

			// 结果文件名的字段
			this.resultFilesNameFieldColumn = new ColumnFieldsetMeta(null,
					"结果文件名");
			this.resultFilesNameFieldColumn.setSingle(true);
			// 添加文件名
			this.addToResult = new LabelInputMeta(id + ".addToResult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.addToResult.setSingle(true);
			this.resultFilesNameFieldColumn
					.putFieldsetsContent(new BaseFormMeta[] { this.addToResult });

			this.meta.putTabContent(2, new BaseFormMeta[] {
					this.startsWithHeader, this.ignoreEmptyRows,
					this.stopOnEmpty, this.fileField, this.sheetField,
					this.sheetRowNumberField, this.rowNumberField,
					this.rowLimit, this.encoding,
					this.resultFilesNameFieldColumn });
			/*******************************************************************
			 * 标签3--错误处理
			 ******************************************************************/
			// 严格类型
			this.strictTypes = new LabelInputMeta(id + ".strictTypes", "严格类型",
					null, null, null, String.valueOf(step.isStrictTypes()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.strictTypes.setSingle(true);
			// 忽略错误
			this.errorIgnored = new LabelInputMeta(id + ".errorIgnored",
					"忽略错误", null, null, null, String.valueOf(step
							.isErrorIgnored()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.errorIgnored.setSingle(true);
			this.errorIgnored
					.addClick("jQuery.imeta.steps.excelinput.listeners.errorIgnoredListeners");
			// 跳过错误行
			this.errorLineSkipped = new LabelInputMeta(
					id + ".errorLineSkipped", "跳过错误行", null, null, null, String
							.valueOf(step.isErrorLineSkipped()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.errorLineSkipped.setSingle(true);
			this.errorLineSkipped.setDisabled(!step.isErrorIgnored());

			// 告警文件目录
			this.warningFilesDestinationDirectory = new LabelInputMeta(id
					+ ".warningFilesDestinationDirectory", "告警文件目录", null,
					null, null, step.getWarningFilesDestinationDirectory(),
					null, null);
			this.warningFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 告警文件目录扩展名
			this.warningFilesExtension = new LabelInputMeta(id
					+ ".warningFilesExtension", "扩展名", null, null, null, step
					.getBadLineFilesExtension(), null, null);
			this.warningFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.warningFilesVariableBtn = new ButtonMeta(id +
			// ".warningFilesVariableBtn",
			// id+ ".warningFilesVariableBtn", "变量", "变量");
			// this.warningFilesVariableBtn.setDisabled(!step.isErrorIgnored());
			// this.warningFilesBrowseBtn = new ButtonMeta(id +
			// ".warningFilesBrowseBtn",
			// id+ ".warningFilesBrowseBtn", "浏览", "浏览");
			// this.warningFilesBrowseBtn.setDisabled(!step.isErrorIgnored());
			// this.warningFilesExtension.addButton(new ButtonMeta[] {
			// this.warningFilesVariableBtn,
			// this.warningFilesBrowseBtn });

			// 错误文件目录
			this.errorFilesDestinationDirectory = new LabelInputMeta(id
					+ ".errorFilesDestinationDirectory", "错误文件目录", null, null,
					null, step.getErrorFilesDestinationDirectory(), null, null);
			this.errorFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 错误文件目录扩展名
			this.errorFilesExtension = new LabelInputMeta(id
					+ ".errorFilesExtension", "扩展名", null, null, null, step
					.getErrorFilesExtension(), null, null);
			this.errorFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.errorFilesVariableBtn = new ButtonMeta(id +
			// ".errorFilesVariableBtn",
			// id+ ".errorFilesVariableBtn", "变量", "变量");
			// this.errorFilesVariableBtn.setDisabled(!step.isErrorIgnored());
			//			
			// this.errorFilesBrowseBtn = new ButtonMeta(id +
			// ".errorFilesBrowseBtn",
			// id+ ".errorFilesBrowseBtn", "浏览", "浏览");
			// this.errorFilesBrowseBtn.setDisabled(!step.isErrorIgnored());
			// this.errorFilesExtension.addButton(new ButtonMeta[] {
			// this.errorFilesVariableBtn,
			// this.errorFilesBrowseBtn });

			// 失败的记录数文件目录
			this.lineNumberFilesDestinationDirectory = new LabelInputMeta(id
					+ ".lineNumberFilesDestinationDirectory", "失败的记录数文件目录",
					null, null, null, step
							.getLineNumberFilesDestinationDirectory(), null,
					null);
			this.lineNumberFilesDestinationDirectory.setDisabled(!step
					.isErrorIgnored());

			// 失败的记录数文件目录扩展名
			this.lineNumberFilesExtension = new LabelInputMeta(id
					+ ".lineNumberFilesExtension", "扩展名", null, null, null,
					step.getLineNumberFilesExtension(), null, null);
			this.lineNumberFilesExtension.setDisabled(!step.isErrorIgnored());

			// this.lineNumberFilesVariableBtn = new ButtonMeta(id +
			// ".lineNumberFilesVariableBtn",
			// id+ ".lineNumberFilesVariableBtn", "变量", "变量");
			// this.lineNumberFilesVariableBtn.setDisabled(!step.isErrorIgnored());
			// this.lineNumberFilesBrowseBtn = new ButtonMeta(id +
			// ".lineNumberFilesBrowseBtn",
			// id+ ".lineNumberFilesBrowseBtn", "浏览", "浏览");
			// this.lineNumberFilesBrowseBtn.setDisabled(!step.isErrorIgnored());
			// this.lineNumberFilesExtension.addButton(new ButtonMeta[] {
			// this.lineNumberFilesVariableBtn,
			// this.lineNumberFilesBrowseBtn
			// });

			this.meta.putTabContent(3, new BaseFormMeta[] { this.strictTypes,
					this.errorIgnored, this.errorLineSkipped,
					this.warningFilesDestinationDirectory,
					this.warningFilesExtension,
					this.errorFilesDestinationDirectory,
					this.errorFilesExtension,
					this.lineNumberFilesDestinationDirectory,
					this.lineNumberFilesExtension });

			/*******************************************************************
			 * 标签4--字段
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 220, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldTrimType",
							"去除空格类型", null, false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.trimTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldRepeat", "重复",
							null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency",
							"货币符号", null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "小数",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "数组",
							null, false, 50), });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.excelinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			ExcelInputField[] excelInputField = step.getField();
			if (excelInputField != null && excelInputField.length > 0) {
				for (int i = 0; i < excelInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, excelInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(excelInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i].getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(excelInputField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, excelInputField[i]
									.getFormat(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, excelInputField[i]
									.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, excelInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, excelInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			// // 获取来自头部数据的字段
			DivMeta getFieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.excelinput.btn.getfields")
			.appendTo(getFieldsbtn);

			this.meta.putTabContent(4, new BaseFormMeta[] { this.fields,
					getFieldsbtn });

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
