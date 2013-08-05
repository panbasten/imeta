package com.panet.imeta.job.entries.copymoveresultfilenames;

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
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryCopyMoveResultFilenamesDialog extends JobEntryDialog
		implements JobEntryDialogInterface {
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
	 * 动作（拷贝或移动）
	 */
	private LabelSelectMeta action;
	/**
	 * 目的文件夹
	 */
	private LabelInputMeta destination_folder;
	/**
	 * 创建目的文件夹
	 */
	private LabelInputMeta CreateDestinationFolder;
	/**
	 * 替换目前文件
	 */
	private LabelInputMeta OverwriteFile;
	/**
	 * 删除源文件
	 */
	private LabelInputMeta RemovedSourceFilename;
	/**
	 * 在结果添加指定文件
	 */
	private LabelInputMeta AddDestinationFilename;
	/**
	 * 添加日期文件
	 */
	private LabelInputMeta add_date;
	/**
	 * 添加时间文件
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
	 * 扩展先前日期
	 */
	private LabelInputMeta AddDateBeforeExtension;

	/**
	 * 限制
	 */
	private ColumnFieldsetMeta limitTo;

	/**
	 * 限制行动
	 */
	private LabelInputMeta specifywildcard;
	/**
	 * 通配符
	 */
	private LabelInputMeta wildcard;
	/**
	 * 不含通配符
	 */
	private LabelInputMeta wildcardexclude;

	/**
	 * 成功
	 */
	private ColumnFieldsetMeta successOn;

	/**
	 * 成功状态
	 */
	private LabelSelectMeta success_condition;
	/**
	 * Nr 减少错误
	 */
	private LabelInputMeta nr_errors_less_than;

	public JobEntryCopyMoveResultFilenamesDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryCopyMoveResultFilenames job = (JobEntryCopyMoveResultFilenames) super
					.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称必须填写", super.getJobEntryName(),
					null, ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);
			// 开始
			List<OptionDataMeta> actionOptions = new ArrayList<OptionDataMeta>();
			actionOptions.add(new OptionDataMeta("0", Messages
					.getString("JobEntryCopyMoveResultFilenames.Copy.Label")));
			actionOptions.add(new OptionDataMeta("1", Messages
					.getString("JobEntryCopyMoveResultFilenames.Move.Label")));
			this.action = new LabelSelectMeta(id + ".action", "开始", null, null,
					"Copy", job.getAction(), null, actionOptions);
			this.action.setSingle(true);
			// 指定文件夹
			this.destination_folder = new LabelInputMeta(id
					+ ".destination_folder", "指定文件夹", null, null,
					"Destination folder", job.getDestinationFolder(), null,
					ValidateForm.getInstance().setRequired(false));
			this.destination_folder.setSingle(true);

			// 新建指定文件夹
			this.CreateDestinationFolder = new LabelInputMeta(id
					+ ".CreateDestinationFolder", "创建目的文件夹", null, null, null,
					String.valueOf(job.isCreateDestinationFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.CreateDestinationFolder.setSingle(true);
			// 替换目前文件
			this.OverwriteFile = new LabelInputMeta(id + ".OverwriteFile",
					"替换已存在文件", null, null, null, String.valueOf(job
							.isOverwriteFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.OverwriteFile.setSingle(true);
			// 删除源文件
			this.RemovedSourceFilename = new LabelInputMeta(id
					+ ".RemovedSourceFilename", "删除源文件", null, null, null,
					String.valueOf(job.isRemovedSourceFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.RemovedSourceFilename.setSingle(true);
			// 在结果添加指定文件
			this.AddDestinationFilename = new LabelInputMeta(id
					+ ".AddDestinationFilename", "在结果添加目的文件名", null, null,
					null, String.valueOf(job.isAddDestinationFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddDestinationFilename.setSingle(true);
			// 添加日期文件
			boolean isSpecifyFormat = job.isSpecifyFormat();
			boolean isAddDate = job.isAddDate();
			this.add_date = new LabelInputMeta(id + ".add_date", "文件名中添加日期",
					null, null, null, String.valueOf(job.isAddDate()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_date.setDisabled(isSpecifyFormat);
			this.add_date
					.addClick("jQuery.imeta.jobEntries.copyMoveResultFilename.listeners.isAddDateTimeClick");
			this.add_date.setSingle(true);
			// 添加时间文件
			boolean isAddTime = job.isAddTime();
			this.add_time = new LabelInputMeta(id + ".add_time", "文件名中添加时间",
					null, null, null, String.valueOf(job.isAddTime()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_time.setDisabled(isSpecifyFormat);
			this.add_time
					.addClick("jQuery.imeta.jobEntries.copyMoveResultFilename.listeners.isAddDateTimeClick");
			this.add_time.setSingle(true);
			// 指定日期时间格式
			this.SpecifyFormat = new LabelInputMeta(id + ".SpecifyFormat",
					"指定日期时间格式", null, null, null, String.valueOf(job
							.isSpecifyFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat
					.addClick("jQuery.imeta.jobEntries.copyMoveResultFilename.listeners.isSpecifyFormatClick");
			this.SpecifyFormat.setSingle(true);
			// 日期时间格式
			String dats[] = Const.getDateFormats();
			List<OptionDataMeta> dateTimeFormatOptions = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < dats.length; i++) {
				dateTimeFormatOptions.add(new OptionDataMeta(String
						.valueOf(i + 1), dats[i]));
			}
			this.date_time_format = new LabelSelectMeta(
					id + ".date_time_format", "日期时间格式", null, null, "日期时间格式",
					job.getDateTimeFormat(), null, dateTimeFormatOptions);
			this.date_time_format.setDisabled(!isSpecifyFormat);
			this.date_time_format.setSingle(true);
			this.date_time_format.setHasEmpty(true);
			// 扩展先前日期
			this.AddDateBeforeExtension = new LabelInputMeta(id
					+ ".AddDateBeforeExtension", "扩展名前添加日期", null, null, null,
					String.valueOf(job.isAddDateBeforeExtension()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddDateBeforeExtension.setDisabled(!isSpecifyFormat
					&& !isAddTime && !isAddDate);
			this.AddDateBeforeExtension.setSingle(true);

			// 限制
			this.limitTo = new ColumnFieldsetMeta(null, "限制");
			this.limitTo.setSingle(true);

			// 限制行动
			boolean isSpecifywildcardClick = job.isSpecifyWildcard();
			this.specifywildcard = new LabelInputMeta(id + ".specifywildcard",
					"限制操作", null, null, null, String.valueOf(job
							.isSpecifyWildcard()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.specifywildcard
					.addClick("jQuery.imeta.jobEntries.copyMoveResultFilename.listeners.isSpecifywildcardClick");
			this.specifywildcard.setSingle(true);

			// 通配符

			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符", null,
					null, "通配符", job.getWildcard(), null, ValidateForm
							.getInstance().setRequired(false));
			this.wildcard.setDisabled(!isSpecifywildcardClick);
			this.wildcard.setSingle(true);
			// 不含通配符

			this.wildcardexclude = new LabelInputMeta(id + ".wildcardexclude",
					"不含通配符", null, null, "不含通配符", job.getWildcardExclude(),
					null, ValidateForm.getInstance().setRequired(false));
			this.wildcardexclude.setDisabled(!isSpecifywildcardClick);
			this.wildcardexclude.setSingle(true);

			this.limitTo
					.putFieldsetsContent(new BaseFormMeta[] {
							this.specifywildcard, this.wildcard,
							this.wildcardexclude });

			// 成功
			this.successOn = new ColumnFieldsetMeta(null, "成功");
			this.successOn.setSingle(true);

			// 成功状态
			List<OptionDataMeta> successOptions = new ArrayList<OptionDataMeta>();
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_NO_ERRORS,
							Messages
									.getString("JobEntryCopyMoveResultFilenames.SuccessWhenAllWorksFine.Label")));
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_AT_LEAST_X_FILES_UN_ZIPPED,
							Messages
									.getString("JobEntryCopyMoveResultFilenames.SuccessWhenAtLeat.Label")));
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_ERRORS_LESS,
							Messages
									.getString("JobEntryCopyMoveResultFilenames.SuccessWhenErrorsLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id
					+ ".success_condition", "成功条件", null, null, "成功条件", job
					.getSuccessCondition(), null, successOptions);
			this.success_condition
					.addListener(
							"change",
							"jQuery.imeta.jobEntries.copyMoveResultFilename.listeners.success_conditionChange");
			this.success_condition.setSingle(true);
			// Nr 减少错误

			this.nr_errors_less_than = new LabelInputMeta(id
					+ ".nr_errors_less_than", "Nr 错误小于", null, null, null, job
					.getNrErrorsLessThan(), null, ValidateForm.getInstance()
					.setRequired(false));
			this.nr_errors_less_than.setDisabled(job.getSuccessCondition()
					.equals(job.SUCCESS_IF_NO_ERRORS));
			this.nr_errors_less_than.setSingle(true);

			this.successOn.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_errors_less_than });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.action, this.destination_folder,
					this.CreateDestinationFolder, this.OverwriteFile,
					this.RemovedSourceFilename, this.AddDestinationFilename,
					this.add_date, this.add_time, this.SpecifyFormat,
					this.date_time_format, this.AddDateBeforeExtension,
					this.limitTo, this.successOn });

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
