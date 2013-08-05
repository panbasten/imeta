package com.panet.imeta.job.entries.copyfiles;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryCopyFilesDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 作业名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/***************************************************************************
	 * 一般项
	 **************************************************************************/

	/**
	 * 设置
	 */
	private ColumnFieldsetMeta settings;
	/**
	 * 包括子目录
	 */
	private LabelInputMeta include_subfolders;
	/**
	 * 目标是文件
	 */
	private LabelInputMeta destination_is_a_file;
	/**
	 * 拷贝空目录
	 */
	private LabelInputMeta copy_empty_folders;
	/**
	 * 创建目标目录
	 */
	private LabelInputMeta create_destination_folder;
	/**
	 * 替换已存在的文件
	 */
	private LabelInputMeta overwrite_files;
	/**
	 * 移除源文件
	 */
	private LabelInputMeta remove_source_files;
	/**
	 * 拷贝上一个结果作为参
	 */
	private LabelInputMeta arg_from_previous;
	/**
	 * 文件/目录源
	 */
	private LabelSelectMeta dbconncet;

	private ButtonMeta editconncet;

	private ButtonMeta newconncet;

	private ButtonMeta newconncets;

	/**
	 * 文件/目录目标
	 */
	private LabelSelectMeta bdconncet;

	private ButtonMeta enwconncet;

	private ButtonMeta enwconncets;
	/**
	 * 通配符
	 */
	private LabelInputMeta names;
	/**
	 * 文件/目录
	 */
	private LabelGridMeta fileFolderTable;
	/**
	 * delete
	 */
	private ButtonMeta delete;
	/**
	 * edit
	 */
	private ButtonMeta edit;

	/***************************************************************************
	 * 结果文件
	 **************************************************************************/
	/**
	 * 结果文件名
	 */
	private ColumnFieldsetMeta resultFileName;

	/**
	 * 增加文件
	 */
	private LabelInputMeta add_result_filesname;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	public JobEntryCopyFilesDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryCopyFiles job = (JobEntryCopyFiles) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					"作业名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "一般项", "结果文件" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * 一般项
			 ******************************************************************/

			// 设置
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);
			// 包括子目录
			boolean isIncludeSubfolders = job.include_subfolders;
			this.include_subfolders = new LabelInputMeta(id
					+ ".include_subfolders", "包括子目录", null, null, null, String
					.valueOf(isIncludeSubfolders),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.include_subfolders
					.addClick("jQuery.imeta.jobEntries.copyFiles.listeners.isIncludeSubfoldersClick");
			this.include_subfolders.setSingle(true);
			// 目标是文件
			this.destination_is_a_file = new LabelInputMeta(id
					+ ".destination_is_a_file", "目标是文件", null, null, null,
					String.valueOf(job.destination_is_a_file),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.destination_is_a_file.setSingle(true);
			// 拷贝空目录
			this.copy_empty_folders = new LabelInputMeta(id
					+ ".copy_empty_folders", "拷贝空目录", null, null, "true",
					String.valueOf(job.copy_empty_folders),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.copy_empty_folders.setDisabled(!isIncludeSubfolders);
			this.copy_empty_folders.setSingle(true);
			// 创建目标目录
			this.create_destination_folder = new LabelInputMeta(id
					+ ".create_destination_folder", "创建目标目录", null, null, null,
					String.valueOf(job.create_destination_folder),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.create_destination_folder.setSingle(true);
			// 替换已存在的文件
			this.overwrite_files = new LabelInputMeta(id + ".overwrite_files",
					"替换已存在的文件", null, null, null, String
							.valueOf(job.overwrite_files),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.overwrite_files.setSingle(true);
			// 移除源文件
			this.remove_source_files = new LabelInputMeta(id
					+ ".remove_source_files", "移除源文件", null, null, null, String
					.valueOf(job.remove_source_files),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.remove_source_files.setSingle(true);
			// 拷贝上一个结果作为参
			boolean is_arg_from_previous_click = job.arg_from_previous;
			this.arg_from_previous = new LabelInputMeta(id
					+ ".arg_from_previous", "拷贝上一个结果作为参数", null, null, null,
					String.valueOf(is_arg_from_previous_click),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.arg_from_previous
					.addClick("jQuery.imeta.jobEntries.copyFiles.listeners.is_arg_from_previous_click");
			this.arg_from_previous.setSingle(true);
			//			
			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.include_subfolders, this.destination_is_a_file,
					this.copy_empty_folders, this.create_destination_folder,
					this.overwrite_files, this.remove_source_files,
					this.arg_from_previous });

			// 得到文件/目录源
			// List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// this.dbconncet = new LabelSelectMeta(id + "dbconncet", "文件/目录源",
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
			// // 得到文件/目录目标
			// List<OptionDataMeta> option = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// this.bdconncet = new LabelSelectMeta(id + "bdconncet", "文件/目录目标",
			// null, null, null, "bdconncet", null, options);
			//
			// this.enwconncet = new ButtonMeta(id + ".btn.enwconncet", id
			// + ".btn.enwconncet", "文件", "文件");
			// this.enwconncets = new ButtonMeta(id + ".btn.enwconncets", id
			// + ".btn.enwconncets", "目录", "目录");
			//
			// this.bdconncet.addButton(new ButtonMeta[] { this.enwconncet,
			// this.enwconncets });
			//
			// this.bdconncet.setSingle(true);
			// // 通配符
			// this.names = new LabelInputMeta(id + ".names", "通配符", null, null,
			// "通配符", super.getJobMeta().getName(), null, ValidateForm
			// .getInstance().setRequired(true));
			// this.names.setSingle(true);
			// 得到文件/目录

			this.fileFolderTable = new LabelGridMeta(id + "_fileFolderTable",
					"文件/目录：", 200, 0);
			this.fileFolderTable.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fileFolderTable.fieldId",
							"#", GridHeaderDataMeta.HEADER_TYPE_NUMBER, false,
							50),
					new GridHeaderDataMeta(id
							+ "_fileFolderTable.fileFolderSource", "文件/目录 源",
							null, false, 100),
					new GridHeaderDataMeta(id
							+ "_fileFolderTable.fileFolderDest", "文件/目录 目标",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fileFolderTable.wildcard",
							"通配符", null, false, 70) });
			this.fileFolderTable.setHasBottomBar(true);
			this.fileFolderTable.setHasAdd(true, !is_arg_from_previous_click,
					"jQuery.imeta.jobEntries.copyFiles.btn.fieldAdd");
			this.fileFolderTable.setHasDelete(true,
					!is_arg_from_previous_click,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fileFolderTable.setSingle(true);

			String[] sourceFolder = job.source_filefolder;
			if (sourceFolder != null && sourceFolder.length > 0) {
				GridCellDataMeta source, dest, wildcard;
				for (int i = 0; i < sourceFolder.length; i++) {
					source = new GridCellDataMeta(null, sourceFolder[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					source.setDisabled(is_arg_from_previous_click);
					dest = new GridCellDataMeta(null,
							job.destination_filefolder[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					dest.setDisabled(is_arg_from_previous_click);
					wildcard = new GridCellDataMeta(null, job.wildcard[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					wildcard.setDisabled(is_arg_from_previous_click);
					this.fileFolderTable.addRow(new Object[] {
							String.valueOf(i + 1), source, dest, wildcard });
				}
			}
			// this.delete = new ButtonMeta(id + ".btn.delete",
			// id + ".btn.delete", "删除", "删除");
			//			
			// this.edit = new ButtonMeta(id + ".btn.edit", id + ".btn.edit",
			// "编辑", "编辑");

			this.meta.putTabContent(0, new BaseFormMeta[] { this.settings,
					this.fileFolderTable });

			/*******************************************************************
			 * 结果文件
			 ******************************************************************/
			// 结果文件名
			this.resultFileName = new ColumnFieldsetMeta(null, "结果文件名");
			this.resultFileName.setSingle(true);

			// 增加文件
			this.add_result_filesname = new LabelInputMeta(id
					+ ".add_result_filesname", "增加文件", null, null, null, String
					.valueOf(job.add_result_filesname),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_result_filesname.setSingle(true);
			//		
			this.resultFileName
					.putFieldsetsContent(new BaseFormMeta[] { this.add_result_filesname });

			this.meta.putTabContent(1,
					new BaseFormMeta[] { this.resultFileName });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta, });

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
