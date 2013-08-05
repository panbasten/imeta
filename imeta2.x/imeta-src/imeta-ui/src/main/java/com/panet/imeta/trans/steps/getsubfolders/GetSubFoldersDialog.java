package com.panet.imeta.trans.steps.getsubfolders;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class GetSubFoldersDialog extends BaseStepDialog implements
		StepDialogInterface {
	public GetSubFoldersDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

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

	/***************************************************************************
	 * Folder
	 **************************************************************************/
	/**
	 * Foldernames from field
	 */
	private ColumnFieldsetMeta foldernamesFromField;

	/**
	 * Accept foldername from field
	 */
	private LabelInputMeta isFoldernameDynamic;

	/**
	 * Foldername field
	 */
	private LabelSelectMeta dynamicFoldernameField;

	/**
	 * Directory
	 */
//	private LabelInputMeta directory;

	/**
	 * Selected directories
	 */
	private LabelGridMeta files;

	/***************************************************************************
	 * Setting
	 **************************************************************************/

	/**
	 * Additional fields
	 */
	private ColumnFieldsetMeta additionalFields;

	/**
	 * Include rownum in output
	 */
	private LabelInputMeta includeRowNumber;
	private LabelInputMeta rowNumberField;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	@Override
	public JSONObject open() throws ImetaException {

		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			GetSubFoldersMeta step = (GetSubFoldersMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 页签
			this.meta = new MenuTabMeta(id, new String[] { "文件夹", "设置" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * Folder
			 ******************************************************************/

			/**
			 * Foldernames form field
			 */
			this.foldernamesFromField = new ColumnFieldsetMeta(null,
					"文件夹名称表单字段");
			this.foldernamesFromField.setSingle(true);

			// Accept foldername from field
			boolean isFoldernameDynamic = step.isFoldernameDynamic();
			this.isFoldernameDynamic = new LabelInputMeta(id
					+ ".isFoldernameDynamic", "从字段中接收文件名？", null, null, null,
					String.valueOf(step.isFoldernameDynamic()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.isFoldernameDynamic
					.addClick("jQuery.imeta.steps.getsubfolders.listeners.isFoldernameDynamic");

			// Foldername field
			this.dynamicFoldernameField = new LabelSelectMeta(id
					+ ".dynamicFoldernameField", "文件名字段", null, null, null,
					step.getDynamicFoldernameField(), null, super
							.getPrevStepResultFields());
			this.dynamicFoldernameField.setSingle(true);
			this.dynamicFoldernameField.setHasEmpty(true);
			this.dynamicFoldernameField
					.setDisabled(!step.isFoldernameDynamic());

			this.foldernamesFromField.putFieldsetsContent(new BaseFormMeta[] {
					this.isFoldernameDynamic, this.dynamicFoldernameField });

			// Directory
//			this.directory = new LabelInputMeta(id + ".directory", "目录", null,
//					null, "目录必须填写", null, null, ValidateForm.getInstance()
//							.setRequired(true));
//			this.directory.setSingle(true);
//			this.directory.setDisabled(step.isFoldernameDynamic());

			// Selected directories
			this.files = new LabelGridMeta(id + "_files", "选择目录", 200);
			this.files.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_files.folderId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_files.folderName", "目录",
							null, false, 200),
					new GridHeaderDataMeta(id + "_files.folderRequired", "要求",
							null, false, 200) });
			this.files.setHasBottomBar(true);
			this.files.setHasAdd(true, !step.isFoldernameDynamic(),
					"jQuery.imeta.steps.getsubfolders.btn.filesAdd");
			this.files.setHasDelete(true, !step.isFoldernameDynamic(),
					"jQuery.imeta.parameter.fieldsDelete");
			this.files.setSingle(true);

			String[] values = step.getFolderName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {
					GridCellDataMeta name,required;
					
					name = new GridCellDataMeta(null, step.getFolderName()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
					name.setDisabled(isFoldernameDynamic);
					required = new GridCellDataMeta(null, String.valueOf(step
						.getFolderRequired()),
						GridCellDataMeta.CELL_TYPE_INPUT);
					required.setDisabled(isFoldernameDynamic);
					this.files.addRow(new Object[] {
						 String.valueOf(i + 1),name,required});
				}

//			this.meta.putTabContent(0, new BaseFormMeta[] {
//					this.foldernamesFromField, this.directory, this.files });

			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.foldernamesFromField, this.files });
			/*******************************************************************
			 * Setting
			 ******************************************************************/

			// Additional fields
			this.additionalFields = new ColumnFieldsetMeta(null, "添加字段");
			this.additionalFields.setSingle(true);

			// Include rownum in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "在输出中包含rownum?", null, null,
					null, String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeRowNumber.setSingle(false);
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.getsubfolders.listeners.includeRowNumber");

			// Rownum fieldname
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"Rownum字段名称", null, null, "Rownum字段名称必须填写", step
							.getRowNumberField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.rowNumberField.setSingle(false);
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			this.additionalFields.putFieldsetsContent(new BaseFormMeta[] {
					this.includeRowNumber, this.rowNumberField });

			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, "限制必须填写", String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowLimit.setSingle(true);

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.additionalFields, this.rowLimit });

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
