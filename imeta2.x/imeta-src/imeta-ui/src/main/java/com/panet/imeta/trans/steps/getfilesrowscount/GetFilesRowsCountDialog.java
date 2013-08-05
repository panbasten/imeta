package com.panet.imeta.trans.steps.getfilesrowscount;

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
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class GetFilesRowsCountDialog extends BaseStepDialog implements
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
	 * Filename from field
	 */
	private ColumnFieldsetMeta filenameFromfieldColum;
	/**
	 * Get filename from field
	 */
	private LabelInputMeta filefield;
	/**
	 * Filename from field
	 */
	private LabelSelectMeta filenameFromfield;

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

	// /**
	// * 显示文件名
	// */
	// private ButtonMeta showFilesName;
	//	
	// /**
	// * 删除
	// */
	// private ButtonMeta deleteBtn;
	//	
	// /**
	// * 编辑
	// */
	// private ButtonMeta editBtn ;

	// Content
	/**
	 * Rows Count field
	 */
	private ColumnFieldsetMeta rowsCountfieldColum;
	/**
	 * Rows Count field name
	 */
	private LabelInputMeta rowsCountFieldName;

	/**
	 * Rows Separator
	 */
	private ColumnFieldsetMeta rowsSeparatorColum;
	/**
	 * Rows Separator type
	 */
	private LabelSelectMeta RowSeparator_format;
	/**
	 * Rows Separator
	 */
	private LabelInputMeta RowSeparator;

	/**
	 * Additional Fields
	 */
	private ColumnFieldsetMeta additionalFieldsColum;
	/**
	 * Include files count in
	 */
	private LabelInputMeta includeFilesCount;
	/**
	 * Files Count fieldname
	 */
	private LabelInputMeta filesCountFieldName;

	/**
	 * Add filename to result
	 */
	private ColumnFieldsetMeta addFilenameResultColum;
	private LabelInputMeta isaddresult;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public GetFilesRowsCountDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			GetFilesRowsCountMeta step = (GetFilesRowsCountMeta) super
					.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------File
			 ******************************************************************/
			// Filename from field
			this.filenameFromfieldColum = new ColumnFieldsetMeta(id, "文件名从字段");
			this.filenameFromfieldColum.setSingle(true);
			// Get filename from field
			boolean isFileField = step.isFileField();
			this.filefield = new LabelInputMeta(
					id + ".filefield", "从字段获取文件名", null, null, null,
					String.valueOf(step.isFileField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.filefield
					.addClick("jQuery.imeta.steps.getfilesrowscount.listeners.getFilenamefieldListeners");
			// Filename from field
			this.filenameFromfield = new LabelSelectMeta(id
					+ ".filenameFromfield", "从字段获取文件名", null, null, null, "",
					null, super.getPrevStepResultFields());
			this.filenameFromfield.setSingle(true);
			this.filenameFromfield.setHasEmpty(true);
			this.filenameFromfield.setDisabled(!step.isFileField());

			this.filenameFromfieldColum.putFieldsetsContent(new BaseFormMeta[] {
					this.filefield, this.filenameFromfield });

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
			// "正规表达式",null, null, null, String.valueOf(step.getFileMask()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// this.regularExpression.setDisabled(step.isFileField());

			// 已选择的文件名称
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件名称", 120);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.selectedFilesId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件＼目录", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 200) });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, !isFileField,
					"jQuery.imeta.steps.getfilesrowscount.btn.fieldAdd");
			this.selectedFiles.setHasDelete(true, !isFileField,
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

			// 显示文件名称
			// DivMeta showFilesNamebtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.showFilesName = new ButtonMeta(id + ".showFilesName", id
			// + ".showFilesName", "显示文件名称", "显示文件名称");
			// this.showFilesName.appendTo(showFilesNamebtn);
			// this.showFilesName.setDisabled(step.isFileField());
			// //删除
			// this.deleteBtn = new ButtonMeta(id + ".deleteBtn", id
			// + ".deleteBtn", "删除", "删除");
			// this.deleteBtn.appendTo(showFilesNamebtn);
			// this.deleteBtn.setDisabled(step.isFileField());
			// //编辑
			// this.editBtn = new ButtonMeta(id + ".editBtn", id
			// + ".editBtn", "编辑", "编辑");
			// this.editBtn.appendTo(showFilesNamebtn);
			// this.editBtn.setDisabled(step.isFileField());
			//			
			// this.meta.putTabContent(0, new BaseFormMeta[]
			// {filenameFromfieldColum,
			// this.fileOrdirectory ,this.regularExpression ,this.selectedFiles
			// });
			this.meta.putTabContent(0, new BaseFormMeta[] {
					filenameFromfieldColum, this.selectedFiles });

			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Rows Count field
			this.rowsCountfieldColum = new ColumnFieldsetMeta(id, "行计数字段");
			this.rowsCountfieldColum.setSingle(true);
			// Rows Count field name
			this.rowsCountFieldName = new LabelInputMeta(id
					+ ".rowsCountFieldName", "行计数字段名称", null, null, null,
					String.valueOf(step.getRowsCountFieldName()), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowsCountFieldName.setSingle(true);

			this.rowsCountfieldColum
					.putFieldsetsContent(new BaseFormMeta[] { this.rowsCountFieldName });

			// Rows Separator
			this.rowsSeparatorColum = new ColumnFieldsetMeta(id, "行分隔符");
			this.rowsSeparatorColum.setSingle(true);
			// Rows Separator type
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options
					.add(new OptionDataMeta(
							"CR",
							Messages
									.getString("GetFilesRowsCountDialog.RowSeparatorFormat.CR.Label")));
			options
					.add(new OptionDataMeta(
							"LF",
							Messages
									.getString("GetFilesRowsCountDialog.RowSeparatorFormat.LF.Label")));
			options
					.add(new OptionDataMeta(
							"TAB",
							Messages
									.getString("GetFilesRowsCountDialog.RowSeparatorFormat.TAB.Label")));
			options
					.add(new OptionDataMeta(
							"CUSTOM",
							Messages
									.getString("GetFilesRowsCountDialog.RowSeparatorFormat.CUSTOM.Label")));
			this.RowSeparator_format = new LabelSelectMeta(id
					+ ".RowSeparator_format", "行分离型", null, null, null, step
					.getRowSeparatorFormat(), null, options);
			this.RowSeparator_format.setSingle(true);
			this.RowSeparator_format.isHasEmpty();
			this.RowSeparator_format
					.addListener("change",
							"jQuery.imeta.steps.getfilesrowscount.listeners.RowSeparatorChange");
			// Rows Separator
			this.RowSeparator = new LabelInputMeta(id + ".RowSeparator",
					"行分隔符", null, null, null, step.getRowSeparator(), null,
					ValidateForm.getInstance().setRequired(true));
			this.RowSeparator.setSingle(true);
			this.RowSeparator.setDisabled(!step.getRowSeparatorFormat().equals(
					"CUSTOM"));

			this.rowsSeparatorColum.putFieldsetsContent(new BaseFormMeta[] {
					this.RowSeparator_format, this.RowSeparator });

			// Additional Fields
			this.additionalFieldsColum = new ColumnFieldsetMeta(id, "其他领域");
			this.additionalFieldsColum.setSingle(true);
			// Include files count in
			this.includeFilesCount = new LabelInputMeta(id
					+ ".includeFilesCount", "包括档案计数", null, null, null, String
					.valueOf(step.includeCountFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilesCount
					.addClick("jQuery.imeta.steps.getfilesrowscount.listeners.includeFilesCountListeners");
			// Files Count fieldname
			this.filesCountFieldName = new LabelInputMeta(id
					+ ".filesCountFieldName", "文件计数字段", null, null, null,
					step.getFilesCountFieldName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.filesCountFieldName.setDisabled(!step.includeCountFiles());

			this.additionalFieldsColum.putFieldsetsContent(new BaseFormMeta[] {
					this.includeFilesCount, this.filesCountFieldName });

			// Add filename to result
			this.addFilenameResultColum = new ColumnFieldsetMeta(id, "新增文件名到结果");
			this.addFilenameResultColum.setSingle(true);
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.addFilenameResultColum
					.putFieldsetsContent(new BaseFormMeta[] { this.isaddresult });

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.rowsCountfieldColum, this.rowsSeparatorColum,
					this.additionalFieldsColum, this.addFilenameResultColum });

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
