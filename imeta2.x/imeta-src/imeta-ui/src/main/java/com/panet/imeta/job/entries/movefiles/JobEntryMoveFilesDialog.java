package com.panet.imeta.job.entries.movefiles;

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
import com.panet.imeta.core.Const;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryMoveFilesDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 作业项名称
	 */
	private LabelInputMeta jobEntryName;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/***************************************************************************
	 * 一般
	 **************************************************************************/

	/**
	 * 设置
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * 包含子文件
	 */
	private LabelInputMeta include_subfolders;

	/**
	 * 移动空文件
	 */
	private LabelInputMeta move_empty_folders;

	/**
	 * 模拟
	 */
	private LabelInputMeta simulate;

	/**
	 * 复制结果到args
	 */
	private LabelInputMeta arg_from_previous;

	/**
	 * 文件/文件夹 来源
	 */
	private LabelInputMeta fileSource;
	private ButtonMeta add;
	private ButtonMeta filea;
	private ButtonMeta foldera;

	/**
	 * 文件/文件夹 目的地
	 */
	private LabelInputMeta fileDestination;
	private ButtonMeta fileb;
	private ButtonMeta folderb;

	/**
	 * 通配符
	 */
	private LabelInputMeta wildcard;

	/**
	 * 文件/多个文件夹
	 */
	private LabelGridMeta fileFolderTable;
	private ButtonMeta delete;
	private ButtonMeta edit;

	/***************************************************************************
	 * 目的地
	 **************************************************************************/

	/**
	 * 目的文件
	 */
	private ColumnFieldsetMeta destinationFile;

	/**
	 * 新建目的文件夹
	 */
	private LabelInputMeta create_destination_folder;

	/**
	 * 目的是一个文件
	 */
	private LabelInputMeta destination_is_a_file;

	/**
	 * 不保存文件夹结构
	 */
	private LabelInputMeta DoNotKeepFolderStructure;

	/**
	 * 文件名添加日期;
	 */
	private LabelInputMeta add_date;

	/**
	 * 文件名添加时间
	 */
	private LabelInputMeta add_time;

	/**
	 * 指定日期时间格式
	 */
	private LabelInputMeta SpecifyFormat;

	/**
	 * 日期时间格式
	 */
	private LabelSelectMeta date_time_format;

	/**
	 * 添加日期延长
	 */
	private LabelInputMeta AddDateBeforeExtension;

	/**
	 * 如果目的文件存在
	 */
	private LabelSelectMeta iffileexists;

	/**
	 * 移动文件夹
	 */
	private ColumnFieldsetMeta moveToFolder;

	/**
	 * 目的文件夹
	 */
	private LabelInputMeta destinationFolder;
	private ButtonMeta browse;

	/**
	 * 新建文件夹
	 */
	private LabelInputMeta create_move_to_folder;

	/**
	 * 添加日期
	 */
	private LabelInputMeta add_moved_date;

	/**
	 * 添加时间
	 */
	private LabelInputMeta add_moved_time;

	/**
	 * 指定格式
	 */
	private LabelInputMeta SpecifyMoveFormat;

	/**
	 * 日期格式
	 */
	private LabelSelectMeta moved_date_time_format;

	/**
	 * 添加日期延长
	 */
	private LabelInputMeta AddMovedDateBeforeExtension;

	/**
	 * 如果文件在目的文件夹存在
	 */
	private LabelSelectMeta ifmovedfileexists;

	/***************************************************************************
	 * *********************************************8 高级
	 **************************************************************************/

	/**
	 * 成功
	 */
	private ColumnFieldsetMeta successOn;

	/**
	 * 成功条件
	 */
	private LabelSelectMeta success_condition;

	/**
	 * Nr 错误减少
	 */
	private LabelInputMeta nr_errors_less_than;

	/**
	 * 结果文件名
	 */
	private ColumnFieldsetMeta add_result_filesname;

	/**
	 * 添加文件到结果文件名
	 */
	private LabelInputMeta addFilesToResult;

	/**
	 * @param jobMeta
	 * @param jobEntryMeta
	 */

	public JobEntryMoveFilesDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryMoveFiles job = (JobEntryMoveFiles) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业项名称
			this.jobEntryName = new LabelInputMeta(id + ".jobEntryName",
					"作业项名称", null, null, "作业项名称必须填写", super.getJobEntryName(),
					null, ValidateForm.getInstance().setRequired(true));
			this.jobEntryName.setSingle(true);

			// 页签
			this.meta = new MenuTabMeta(id, new String[] { "一般", "目的文件", "高级" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 一般
			 ******************************************************************/

			// 设置
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			// 包含子文件
			boolean is_include_subfolders_click = job.include_subfolders;
			this.include_subfolders = new LabelInputMeta(id
					+ ".include_subfolders", "包含子文件夹", null, null, null, String
					.valueOf(is_include_subfolders_click),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.include_subfolders
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.is_include_subfolders_click");
			this.include_subfolders.setSingle(true);

			// 移动空文件
			this.move_empty_folders = new LabelInputMeta(id
					+ ".move_empty_folders", "移动空文件夹", null, null, null, String
					.valueOf(job.move_empty_folders),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.move_empty_folders.setDisabled(!is_include_subfolders_click);
			this.move_empty_folders.setSingle(true);

			// 模拟
			this.simulate = new LabelInputMeta(id + ".simulate", "模拟", null,
					null, null, String.valueOf(job.simulate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.simulate.setSingle(true);

			// 复制结果到args
			boolean is_arg_from_previous_click = job.arg_from_previous;
			this.arg_from_previous = new LabelInputMeta(id
					+ ".arg_from_previous", "从之前的结果得到参数", null, null, null,
					String.valueOf(is_arg_from_previous_click),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.arg_from_previous
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.is_arg_from_previous_click");
			this.arg_from_previous.setSingle(true);

			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.include_subfolders, this.move_empty_folders,
					this.simulate, this.arg_from_previous });

			// // 文件/文件夹 来源
			// this.fileSource = new LabelInputMeta(id + ".fileSource",
			// "文件/文件夹 来源", null, null, "File/Folder sourse必须填写", null,
			// null, ValidateForm.getInstance().setRequired(true));
			// this.add = new ButtonMeta(id + ".btn.add", id + ".btn.add", "添加",
			// "添加");
			// this.filea = new ButtonMeta(id + ".btn.filea", id + ".btn.filea",
			// "文件...", "文件...");
			// this.foldera = new ButtonMeta(id + ".btn.foldera", id
			// + ".btn.foldera", "文件夹...", "文件夹...");
			// this.fileSource.addButton(new ButtonMeta[] { this.add,
			// this.filea,
			// this.foldera });
			// this.fileSource.setSingle(true);
			//
			// // 文件/文件夹 目的地
			// this.fileDestination = new LabelInputMeta(id +
			// ".fileDestination",
			// "文件/文件夹 目的地", null, null, "File/Folder destination必须填写",
			// null, null, ValidateForm.getInstance().setRequired(true));
			//
			// this.fileb = new ButtonMeta(id + ".btn.fileb", id + ".btn.fileb",
			// "文件...", "文件...");
			// this.folderb = new ButtonMeta(id + ".btn.folderb", id
			// + ".btn.folderb", "文件夹...", "文件夹...");
			//
			// this.fileDestination.addButton(new ButtonMeta[] { this.fileb,
			// this.folderb });
			// this.fileDestination.setSingle(true);
			//
			// // 通配符
			// this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符", null,
			// null, "Wildcard必须填写", null, null, ValidateForm
			// .getInstance().setRequired(true));
			// this.wildcard.setSingle(true);

			// 文件/多个文件夹
			this.fileFolderTable = new LabelGridMeta(id + "_fileFolderTable",
					"文件/目录：", 200, 0);
			this.fileFolderTable.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fileFolderTable.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
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
					"jQuery.imeta.jobEntries.moveFiles.btn.fieldAdd");
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
			 * 目的文件
			 ******************************************************************/

			// 目的文件
			this.destinationFile = new ColumnFieldsetMeta(null, "目的文件");
			this.destinationFile.setSingle(true);

			// 新建目的文件夹
			this.create_destination_folder = new LabelInputMeta(id
					+ ".create_destination_folder", "新建目的文件夹", null, null,
					null, String.valueOf(job.create_destination_folder),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.create_destination_folder.setSingle(true);

			// 目的是空文件
			this.destination_is_a_file = new LabelInputMeta(id
					+ ".destinationIsAFile", "目的是空文件", null, null, null, null,
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.destination_is_a_file.setSingle(true);

			// 不保存文件夹结构
			this.DoNotKeepFolderStructure = new LabelInputMeta(id
					+ ".DoNotKeepFolderStructure", "不保存文件夹结构", null, null,
					null, String.valueOf(job.isDoNotKeepFolderStructure()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.DoNotKeepFolderStructure.setDisabled(true);
			this.DoNotKeepFolderStructure.setSingle(true);

			boolean isSpecifyFormat = job.isSpecifyFormat();
			// 文件名添加日期
			boolean isAddDate = job.isAddDate();
			this.add_date = new LabelInputMeta(id + ".add_date", "文件名添加日期",
					null, null, null, String.valueOf(isAddDate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_date.setDisabled(isSpecifyFormat);
			this.add_date
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isAddDateTimeClick");
			this.add_date.setSingle(true);

			// 文件名添加时间
			boolean isAddTime = job.isAddTime();
			this.add_time = new LabelInputMeta(id + ".add_time", "文件名添加时间",
					null, null, null, String.valueOf(isAddTime),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_time.setDisabled(isSpecifyFormat);
			this.add_time
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isAddDateTimeClick");
			this.add_time.setSingle(true);

			// 指定日期时间格式

			this.SpecifyFormat = new LabelInputMeta(id
					+ ".specifyDateTimeFormat", "指定日期时间格式", null, null, null,
					String.valueOf(isSpecifyFormat),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isSpecifyFormatClick");
			this.SpecifyFormat.setSingle(true);

			// 日期时间格式
			String dats[] = Const.getDateFormats();
			List<OptionDataMeta> dateTimeFormatOptions = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < dats.length; i++) {
				dateTimeFormatOptions.add(new OptionDataMeta(String
						.valueOf(i + 1), dats[i]));
			}

			this.date_time_format = new LabelSelectMeta(id
					+ ".date_time_format", "日期时间格式", null, null, null, job
					.getDateTimeFormat(), null, dateTimeFormatOptions);
			this.date_time_format.setDisabled(!isSpecifyFormat);
			this.date_time_format.setSingle(true);
			this.date_time_format.setHasEmpty(true);

			// 添加日期延长
			this.AddDateBeforeExtension = new LabelInputMeta(id
					+ ".AddDateBeforeExtension", "在扩展名前添加日期", null, null, null,
					String.valueOf(job.isAddDateBeforeExtension()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddDateBeforeExtension.setDisabled(!isSpecifyFormat
					&& !isAddTime && !isAddDate);
			this.AddDateBeforeExtension.setSingle(true);

			// 如果目的文件存在
			List<OptionDataMeta> iffileexistsOptions = new ArrayList<OptionDataMeta>();
			iffileexistsOptions.add(new OptionDataMeta("0", Messages
					.getString("JobMoveFiles.Do_Nothing_IfFileExists.Label")));
			iffileexistsOptions
					.add(new OptionDataMeta(
							"1",
							Messages
									.getString("JobMoveFiles.Overwrite_File_IfFileExists.Label")));
			iffileexistsOptions.add(new OptionDataMeta("2", Messages
					.getString("JobMoveFiles.Unique_Name_IfFileExists.Label")));
			iffileexistsOptions
					.add(new OptionDataMeta(
							"3",
							Messages
									.getString("JobMoveFiles.Delete_Source_File_IfFileExists.Label")));
			iffileexistsOptions
					.add(new OptionDataMeta(
							"4",
							Messages
									.getString("JobMoveFiles.Move_To_Folder_IfFileExists.Label")));
			iffileexistsOptions.add(new OptionDataMeta("5", Messages
					.getString("JobMoveFiles.Fail_IfFileExists.Label")));

			this.iffileexists = new LabelSelectMeta(id + ".iffileexists",
					"如果目的文件存在", null, null, null, job.getIfFileExists(), null,
					iffileexistsOptions);
			this.iffileexists
					.addListener("change",
							"jQuery.imeta.jobEntries.moveFiles.listeners.iffileexistsChange");
			this.iffileexists.setSingle(true);

			this.destinationFile.putFieldsetsContent(new BaseFormMeta[] {
					this.create_destination_folder, this.destination_is_a_file,
					this.DoNotKeepFolderStructure, this.add_date,
					this.add_time, this.SpecifyFormat, this.date_time_format,
					this.AddDateBeforeExtension, this.iffileexists });

			// 移动文件夹
			this.moveToFolder = new ColumnFieldsetMeta(null, "移动文件夹");
			this.moveToFolder.setSingle(true);

			// 目的文件夹
			this.destinationFolder = new LabelInputMeta(id
					+ ".destinationFolder", "目的文件夹", null, null,
					"Destination folder必须填写", job.getDestinationFolder(), null,
					ValidateForm.getInstance().setRequired(false));
			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览(B)...", "浏览(B)...");
			//
			// this.destinationFolder.addButton(new ButtonMeta[] { this.browse
			// });
			this.destinationFolder.setDisabled(!job.getIfFileExists().equals(
					"4"));
			this.destinationFolder.setSingle(true);

			// 新建文件夹
			this.create_move_to_folder = new LabelInputMeta(id
					+ ".create_move_to_folder", "新建文件夹", null, null, null,
					String.valueOf(job.create_move_to_folder),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.create_move_to_folder.setDisabled(!job.getIfFileExists()
					.equals("4"));
			this.create_move_to_folder.setSingle(true);

			// 添加日期
			boolean isAddMovedDate = job.isAddMovedDate();
			this.add_moved_date = new LabelInputMeta(id + ".add_moved_date",
					"添加日期", null, null, null, String.valueOf(isAddMovedDate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_moved_date
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isAddMovedDateTimeClick");
			this.add_moved_date.setDisabled(!job.getIfFileExists().equals("4"));
			this.add_moved_date.setSingle(true);

			// 添加时间
			boolean isAddMovedTime = job.isAddMovedTime();
			this.add_moved_time = new LabelInputMeta(id + ".add_moved_time",
					"添加时间", null, null, null, String.valueOf(isAddMovedTime),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_moved_time
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isAddMovedDateTimeClick");
			this.add_moved_time.setDisabled(!job.getIfFileExists().equals("4"));
			this.add_moved_time.setSingle(true);

			// 指定格式
			boolean isSpecifyMoveFormat = job.isSpecifyMoveFormat();
			this.SpecifyMoveFormat = new LabelInputMeta(id
					+ ".SpecifyMoveFormat", "指定格式", null, null, null, String
					.valueOf(isSpecifyMoveFormat),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyMoveFormat.setDisabled(!job.getIfFileExists().equals(
					"4"));
			this.SpecifyMoveFormat
					.addClick("jQuery.imeta.jobEntries.moveFiles.listeners.isSpecifyMoveFormatClick");
			this.SpecifyMoveFormat.setSingle(true);

			// 日期格式
			List<OptionDataMeta> movedDateTimeFormatOptions = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < dats.length; i++) {
				movedDateTimeFormatOptions.add(new OptionDataMeta(String
						.valueOf(i + 1), dats[i]));
			}
			this.moved_date_time_format = new LabelSelectMeta(id
					+ ".moved_date_time_format", "日期格式", null, null, null, job
					.getMovedDateTimeFormat(), null, movedDateTimeFormatOptions);
			this.moved_date_time_format.setDisabled(!isSpecifyMoveFormat);
			this.moved_date_time_format.setHasEmpty(true);
			this.moved_date_time_format.setSingle(true);

			// 添加日期延长
			this.AddMovedDateBeforeExtension = new LabelInputMeta(id
					+ ".AddMovedDateBeforeExtension", "在扩展名前添加日期", null, null,
					null, null, InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddMovedDateBeforeExtension.setDisabled(!isSpecifyMoveFormat
					&& !isAddMovedTime && !isAddMovedDate);
			// this.AddMovedDateBeforeExtension.setDisabled(!isAddMovedTime);
			// this.AddMovedDateBeforeExtension.setDisabled(!isAddMovedDate);
			this.AddMovedDateBeforeExtension.setSingle(true);

			// 如果文件在目的文件夹存在
			List<OptionDataMeta> optionsb = new ArrayList<OptionDataMeta>();
			optionsb
					.add(new OptionDataMeta(
							"0",
							Messages
									.getString("JobMoveFiles.Do_Nothing_IfMovedFileExists.Label")));
			optionsb
					.add(new OptionDataMeta(
							"1",
							Messages
									.getString("JobMoveFiles.Overwrite_Filename_IffMovedFileExists.Label")));
			optionsb
					.add(new OptionDataMeta(
							"2",
							Messages
									.getString("JobMoveFiles.UniqueName_IfMovedFileExists.Label")));
			optionsb.add(new OptionDataMeta("3", Messages
					.getString("JobMoveFiles.Fail_IfMovedFileExists.Label")));

			this.ifmovedfileexists = new LabelSelectMeta(id
					+ ".ifmovedfileexists", "如果文件在目的文件夹存在", null, null, null,
					job.getIfMovedFileExists(), null, optionsb);
			this.ifmovedfileexists.setDisabled(!job.getIfFileExists().equals(
					"4"));
			this.ifmovedfileexists.setSingle(true);

			this.moveToFolder.putFieldsetsContent(new BaseFormMeta[] {
					this.destinationFolder, this.create_move_to_folder,
					this.add_moved_date, this.add_moved_time,
					this.SpecifyMoveFormat, this.moved_date_time_format,
					this.AddMovedDateBeforeExtension, this.ifmovedfileexists });

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.destinationFile, this.moveToFolder });

			/*******************************************************************
			 * 高级
			 ******************************************************************/

			// 成功
			this.successOn = new ColumnFieldsetMeta(null, "成功");
			this.successOn.setSingle(true);

			// 成功条件
			List<OptionDataMeta> optionsSucc = new ArrayList<OptionDataMeta>();
			optionsSucc
					.add(new OptionDataMeta(
							job.SUCCESS_IF_NO_ERRORS,
							Messages
									.getString("JobMoveFiles.SuccessWhenAllWorksFine.Label")));
			optionsSucc
					.add(new OptionDataMeta(
							job.SUCCESS_IF_AT_LEAST_X_FILES_UN_ZIPPED,
							Messages
									.getString("JobMoveFiles.SuccessWhenAtLeat.Label")));
			optionsSucc
					.add(new OptionDataMeta(
							job.SUCCESS_IF_ERRORS_LESS,
							Messages
									.getString("JobMoveFiles.SuccessWhenErrorsLessThan.Label")));

			this.success_condition = new LabelSelectMeta(id
					+ ".success_condition", "成功条件", null, null, null, job
					.getSuccessCondition(), null, optionsSucc);
			this.success_condition
					.addListener("change",
							"jQuery.imeta.jobEntries.moveFiles.listeners.success_conditionChange");
			this.success_condition.setSingle(true);

			// Nr 错误减少
			this.nr_errors_less_than = new LabelInputMeta(id
					+ ".nr_errors_less_than", "Nr 错误小于", null, null,
					"Nr errors lesser than必须填写", job.getNrErrorsLessThan(),
					null, ValidateForm.getInstance().setRequired(true));
			this.nr_errors_less_than.setDisabled(job.getSuccessCondition()
					.equals(job.SUCCESS_IF_NO_ERRORS));
			this.nr_errors_less_than.setSingle(true);

			this.successOn.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_errors_less_than });

			// 结果文件名
			this.add_result_filesname = new ColumnFieldsetMeta(null, "结果文件名");
			this.add_result_filesname.setSingle(true);

			// 添加文件到结果文件名
			this.addFilesToResult = new LabelInputMeta(
					id + ".addFilesToResult", "添加文件到结果文件名", null, null, null,
					String.valueOf(job.add_result_filesname),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addFilesToResult.setSingle(true);

			this.add_result_filesname
					.putFieldsetsContent(new BaseFormMeta[] { this.addFilesToResult });
			this.meta.putTabContent(2, new BaseFormMeta[] { this.successOn,
					this.add_result_filesname });
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.jobEntryName, this.meta });

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
