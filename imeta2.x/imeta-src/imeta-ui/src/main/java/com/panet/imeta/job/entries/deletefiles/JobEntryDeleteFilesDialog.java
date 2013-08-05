package com.panet.imeta.job.entries.deletefiles;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryDeleteFilesDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 输入工作名
	 */
	private LabelInputMeta name;

	/**
	 * 设置
	 */
	private ColumnFieldsetMeta destinationAddress;

	/**
	 * 包含子资料夹
	 */
	private LabelInputMeta includeSubfolders;
	/**
	 * 复制先前的结果
	 */
	private LabelInputMeta argFromPrevious;

	/**
	 * 通配符
	 */
	private LabelInputMeta obmode;

	/**
	 * 文件/文件夹
	 */
	private LabelGridMeta fileFolderTable;

	public JobEntryDeleteFilesDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryDeleteFiles job = (JobEntryDeleteFiles) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到输入工作名
			this.name = new LabelInputMeta(id + ".name", "输入工作名", null, null,
					"输入工作名必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 设置
			this.destinationAddress = new ColumnFieldsetMeta(null, "设置");
			this.destinationAddress.setSingle(true);

			// 包含子资料夹
			this.includeSubfolders = new LabelInputMeta(id
					+ ".includeSubfolders", "包含子资料夹", null, null, null, String
					.valueOf(job.isIncludeSubfolders()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeSubfolders.setSingle(true);
			// 复制先前的结果
			boolean isArgFromPrevious = job.isArgFromPrevious();
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"复制先前的结果", null, null, null, String
							.valueOf(isArgFromPrevious),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.argFromPrevious
					.addClick("jQuery.imeta.jobEntries.deleteFiles.listeners.isArgFromPreviousClick");
			this.argFromPrevious.setSingle(true);

			this.destinationAddress.putFieldsetsContent(new BaseFormMeta[] {
					this.includeSubfolders, this.argFromPrevious });

			// // 得到通配符
			//
			// this.obmode = new LabelInputMeta(id + ".obmode", "通配符", null,
			// null,
			// "通配符", null, null, ValidateForm.getInstance().setRequired(
			// true));
			// this.obmode.setSingle(true);

			// 文件/多个文件夹
			this.fileFolderTable = new LabelGridMeta(id + "_fileFolderTable",
					"文件/文件夹：", 200, 0);
			this.fileFolderTable.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fileFolderTable.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fileFolderTable.fileFolder",
							"文件/文件夹", null, false, 100),
					new GridHeaderDataMeta(id + "_fileFolderTable.wildcard",
							"通配符", null, false, 70) });
			this.fileFolderTable.setHasBottomBar(true);
			this.fileFolderTable.setHasAdd(true, !isArgFromPrevious,
					"jQuery.imeta.jobEntries.deleteFiles.btn.fieldAdd");
			this.fileFolderTable.setHasDelete(true, !isArgFromPrevious,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fileFolderTable.setSingle(true);

			String[] fieldFolder = job.getArguments();
			if (fieldFolder != null && fieldFolder.length > 0) {
				GridCellDataMeta fileFolder, wildcard;
				for (int i = 0; i < fieldFolder.length; i++) {
					fileFolder = new GridCellDataMeta(null, fieldFolder[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					fileFolder.setDisabled(isArgFromPrevious);
					wildcard = new GridCellDataMeta(null,
							job.getFilemasks()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					wildcard.setDisabled(isArgFromPrevious);
					this.fileFolderTable.addRow(new Object[] {
							String.valueOf(i + 1), fileFolder, wildcard });
				}
			}
			// 装载到form
			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name,
							this.destinationAddress, this.obmode,
							this.fileFolderTable, });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
