package com.panet.imeta.job.entries.addresultfilenames;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryAddResultFilenamesDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 作业项名称
	 */
	private LabelInputMeta name;

	/**
	 * 设置
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * 包括子文件夹？
	 */
	private LabelInputMeta includeSubfolders;
	/**
	 * 将上一个作业项的结果作为参
	 */
	private LabelInputMeta argFromPrevious;
	/**
	 * 清除结果文件名？
	 */
	private LabelInputMeta deleteallbefore;
	/**
	 * 文件/目录
	 */
	private LabelSelectMeta dbconncet;

	/**
	 * 通配符
	 */
	private LabelInputMeta obmode;

	/**
	 * 多个文件/多个目录
	 */
	private LabelGridMeta multiFileFolderTable;
	

	public JobEntryAddResultFilenamesDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryAddResultFilenames job = (JobEntryAddResultFilenames) super
					.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称：", null, null,
					"作业项名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 设置
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			// 包括子文件夹？
			this.includeSubfolders = new LabelInputMeta(id
					+ ".includeSubfolders", "包括子文件夹？", null, null, null, String
					.valueOf(job.isIncludeSubfolders()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeSubfolders.setSingle(true);
			// 将上一个作业项的结果作为参
			boolean isArgFromPreviousClick = job.isArgFromPrevious();
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"将上一个作业项的结果作为参数", null, null, null, String
							.valueOf(isArgFromPreviousClick),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.argFromPrevious
					.addClick("jQuery.imeta.jobEntries.addResultFileNames.listeners.isArgFromPreviousClick");
			this.argFromPrevious.setSingle(true);
			// 清除结果文件名？
			this.deleteallbefore = new LabelInputMeta(id + ".deleteallbefore",
					"清楚结果文件名", null, null, null, String.valueOf(job
							.deleteAllBefore()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.deleteallbefore.setSingle(true);

			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.includeSubfolders, this.argFromPrevious,
					this.deleteallbefore });

			// // 得到文件/目录
			// List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// this.dbconncet = new LabelSelectMeta(id + "dbconncet", "文件/目录",
			// null, null, null, "dbconncet", null, options);
			// this.editconncet = new ButtonMeta(id + ".btn.editconncet", id
			// + ".btn.editconncet", "增加", "增加");
			// this.newconncet = new ButtonMeta(id + ".btn.newconncet", id
			// + ".btn.newconncet", "文件", "文件");
			// this.newconncets = new ButtonMeta(id + ".btn.newconncets", id
			// + ".btn.newconncets", "目录", "目录");
			//
			// this.dbconncet.addButton(new ButtonMeta[] { this.editconncet,
			// this.newconncet, this.newconncets });
			//
			// this.dbconncet.setSingle(true);

			// // 得到通配符
			//
			// this.obmode = new LabelInputMeta(id + ".obmode", "通配符", null,
			// null,
			// "通配符", null, null, ValidateForm.getInstance().setRequired(
			// true));
			// this.obmode.setSingle(true);

			// 得到多个文件/多个目录

			this.multiFileFolderTable = new LabelGridMeta(id
					+ "_multiFileFolderTable", "多个文件/多个目录：", 200, 0);
			this.multiFileFolderTable.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_multiFileFolderTable.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id
							+ "_multiFileFolderTable.fileFolder", "文件/目录",
							null, false, 100),
					new GridHeaderDataMeta(id
							+ "_multiFileFolderTable.wildcard", "通配符", null,
							false, 100) });
			this.multiFileFolderTable.setHasBottomBar(true);
			this.multiFileFolderTable.setHasAdd(true, !isArgFromPreviousClick,
					"jQuery.imeta.jobEntries.addResultFileNames.btn.fieldAdd");
			this.multiFileFolderTable
					.setHasDelete(true, !isArgFromPreviousClick,
							"jQuery.imeta.parameter.fieldsDelete");
			this.multiFileFolderTable.setSingle(true);

			String[] fileFolderName = job.getArguments();
			if (fileFolderName != null && fileFolderName.length > 0) {
				GridCellDataMeta fileFolder, wildcard;
				for (int i = 0; i < fileFolderName.length; i++) {
					fileFolder = new GridCellDataMeta(null, fileFolderName[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					fileFolder.setDisabled(isArgFromPreviousClick);
					wildcard = new GridCellDataMeta(null,
							job.getFilemasks()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					wildcard.setDisabled(isArgFromPreviousClick);
					this.multiFileFolderTable.addRow(new Object[] {
							String.valueOf(i + 1), fileFolder, wildcard });
				}
			}
			// this.delete = new ButtonMeta(id + ".btn.delete",
			// id + ".btn.delete", "删除", "删除");
			//
			// this.edit = new ButtonMeta(id + ".btn.edit", id + ".btn.edit",
			// "编辑", "编辑");

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.settings, this.dbconncet, this.obmode,
					this.multiFileFolderTable });

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
