package com.panet.imeta.trans.steps.getfilenames;

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
import com.panet.imeta.core.fileinput.FileInputList;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class GetFileNamesDialog extends BaseStepDialog implements
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
	 * Filenames from field
	 */
	private ColumnFieldsetMeta filenamesFromfield;
	/**
	 * Filename is defined in a 文件名定义
	 */
	private LabelInputMeta filenameDefined;
	/**
	 * Get Filename from field
	 */
	private LabelSelectMeta dynamicFilenameField;
	/**
	 * Get wild card from field
	 */
	private LabelSelectMeta dynamicWildcardField;

	/**
	 * 文件或目录
	 */
	// private LabelInputMeta fileOrcontents;
	// private ButtonMeta fileOrcontentsAdd;
	// private ButtonMeta fileOrcontentsSelecter;
	/**
	 * 正则表达式
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

	// Filters
	/**
	 * Get
	 */
	private LabelSelectMeta getFile;

	/**
	 * Additional fields
	 */
	private ColumnFieldsetMeta additionalFields;
	/**
	 * Include rownum in output
	 */
	private LabelInputMeta includeRowNumber;
	/**
	 * Rownum fieldname
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Add to result filename
	 */
	private ColumnFieldsetMeta addResultFilename;
	/**
	 * Add filename to result
	 */
	private LabelInputMeta isaddresult;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public GetFileNamesDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			GetFileNamesMeta step = (GetFileNamesMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "过滤" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------文件
			 ******************************************************************/
			// Filenames from field
			this.filenamesFromfield = new ColumnFieldsetMeta(id, "外部文件名");
			this.filenamesFromfield.setSingle(true);
			// Filename is defined in a
			boolean isFileField = step.isFileField();
			this.filenameDefined = new LabelInputMeta(id + ".filenameDefined",
					"文件名定义", null, null, null, String.valueOf(step
							.isFileField()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(true));
			this.filenameDefined
					.addClick("jQuery.imeta.steps.getfilenames.listeners.filenameDefinedListeners");
			// Get Filename from field
			this.dynamicFilenameField = new LabelSelectMeta(id
					+ ".dynamicFilenameField", "从哪个字段读取文件名", null, null, null,
					step.getDynamicFilenameField(), null, super
							.getPrevStepResultFields());
			this.dynamicFilenameField.setSingle(true);
			this.dynamicFilenameField.setHasEmpty(true);
			this.dynamicFilenameField.setDisabled(!step.isFileField());
			// Get wild card from field
			this.dynamicWildcardField = new LabelSelectMeta(id
					+ ".dynamicWildcardField", "从哪个字段读取通配符", null, null, null,
					step.getDynamicWildcardField(), null, super.getPrevStepResultFields());
			this.dynamicWildcardField.setSingle(true);
			this.dynamicWildcardField.setHasEmpty(true);
			this.dynamicWildcardField.setDisabled(!step.isFileField());

			this.filenamesFromfield.putFieldsetsContent(new BaseFormMeta[] {
					this.filenameDefined, this.dynamicFilenameField,
					this.dynamicWildcardField });

			// 文件或目录
			// this.fileOrcontents = new LabelInputMeta(id + ".fileOrcontents",
			// "文件或目录", null, null, null, String.valueOf(step
			// .getFileName()), null, ValidateForm.getInstance()
			// .setRequired(true));
			// this.fileOrcontents.setSingle(true);
			// this.fileOrcontents.setDisabled(step.isFileField());
			// 正则表达式
			// this.regularExpression = new LabelInputMeta(id
			// + ".regularExpression", "正则表达式", null, null, null, String
			// .valueOf(step.getFileName()), null, ValidateForm
			// .getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// this.regularExpression.setDisabled(step.isFileField());
			// 已选择的文件名称
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件名称", 150);
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
			this.selectedFiles.setHasAdd(true, !isFileField,
					"jQuery.imeta.steps.getfilenames.btn.fieldAdd");
			this.selectedFiles.setHasDelete(true, !isFileField,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					GridCellDataMeta name, mask, required;
					name = new GridCellDataMeta(null, step.getFileName()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isFileField);
					mask = new GridCellDataMeta(null, step.getFileMask()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					mask.setDisabled(isFileField);
					required = new GridCellDataMeta(null, step
							.getFileRequired()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					required.setDisabled(isFileField);
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1), name, mask, required });
				}
			}

			// // 显示文件名称
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

			// 装载到页签0
			// this.meta.putTabContent(0, new BaseFormMeta[] {
			// this.filenamesFromfield, this.fileOrcontents,
			// this.regularExpression, this.selectedFiles });
			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.filenamesFromfield, this.selectedFiles });
			/*******************************************************************
			 * 标签1---------Filters
			 ******************************************************************/
			// Get
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options
					.add(new OptionDataMeta(
							FileInputList.FileTypeFilter.FILES_AND_FOLDERS
									.toString(),
							Messages
									.getString("GetFileNamesDialog.FilterTab.FileType.All.Label")));
			options
					.add(new OptionDataMeta(
							FileInputList.FileTypeFilter.ONLY_FILES.toString(),
							Messages
									.getString("GetFileNamesDialog.FilterTab.FileType.OnlyFile.Label")));
			options
					.add(new OptionDataMeta(
							FileInputList.FileTypeFilter.ONLY_FOLDERS.toString(),
							Messages
									.getString("GetFileNamesDialog.FilterTab.FileType.OnlyFolder.Label")));
			this.getFile = new LabelSelectMeta(id + ".fileTypeFilter", "获得",
					null, null, null, String.valueOf(step.getFileTypeFilter()),
					null, options);
			this.getFile.setSingle(true);
			this.getFile.isHasEmpty();

			// Additional fields
			this.additionalFields = new ColumnFieldsetMeta(id, "其他字段");
			this.additionalFields.setSingle(true);
			// Include rownum in output输出包含行数
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出包含行数", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Rownum fieldname字段行数
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"字段行数", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));

			this.additionalFields.putFieldsetsContent(new BaseFormMeta[] {
					this.includeRowNumber, this.rowNumberField });

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			this.rowLimit.setDisabled(step.isFileField());

			// Add to result filename
			this.addResultFilename = new ColumnFieldsetMeta(id, "添加到结果文件名");
			this.addResultFilename.setSingle(true);
			// Add filename to result
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));

			this.addResultFilename
					.putFieldsetsContent(new BaseFormMeta[] { this.isaddresult });

			// 装载到页签1
			this.meta.putTabContent(1, new BaseFormMeta[] { this.getFile,
					this.additionalFields, this.rowLimit,
					this.addResultFilename });

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
