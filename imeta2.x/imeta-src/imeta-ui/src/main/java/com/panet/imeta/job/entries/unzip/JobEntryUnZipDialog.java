package com.panet.imeta.job.entries.unzip;

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
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryUnZipDialog extends JobEntryDialog implements
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
	 * 页签
	 */
	private MenuTabMeta meta;

	/***************************************************************************
	 * 一般
	 **************************************************************************/

	/**
	 * 源文件
	 */
	private ColumnFieldsetMeta sourceFile;
	/**
	 * 从上一个作业获取参数
	 */
	private LabelInputMeta isfromprevious;

	/**
	 * Zip 文件名
	 */
	private LabelInputMeta zipFilename;

	// private ButtonMeta editconncet;
	//
	// private ButtonMeta newconncet;

	/**
	 * 源通配符
	 */
	private LabelInputMeta wildcardSource;

	/**
	 * 解压文件
	 */
	private ColumnFieldsetMeta unzippedFiles;

	/**
	 * 是否把zip文件名当做根目录
	 */
	private LabelInputMeta rootzip;

	/**
	 * 目标目录
	 */
	private LabelInputMeta targetdirectory;

	private ButtonMeta enwconncet;

	/**
	 * 创建文件夹
	 */
	private LabelInputMeta createfolder;

	/**
	 * 包含通配符
	 */
	private LabelInputMeta wildcard;

	/**
	 * 不含通配符
	 */
	private LabelInputMeta wildcardexclude;

	/**
	 * 文件名包含日期
	 */
	private LabelInputMeta adddate;

	/**
	 * 文件名包含时间
	 */
	private LabelInputMeta addtime;

	/**
	 * 指定日期时间格式
	 */
	private LabelInputMeta SpecifyFormat;

	/**
	 * 日期时间格式
	 */
	private LabelSelectMeta date_time_format;

	/**
	 * 如果文件存在
	 */
	private LabelSelectMeta iffileexist;

	/**
	 * 解压缩后
	 */
	private LabelSelectMeta afterunzip;

	/**
	 * 移动文件
	 */
	private LabelInputMeta movetodirectory;

	/**
	 * 创建目录
	 */
	private LabelInputMeta createMoveToDirectory;

	/***************************************************************************
	 * 高级
	 **************************************************************************/
	/**
	 * 结果文件名
	 */
	private ColumnFieldsetMeta resultFileName;

	/**
	 * 将抽出来的文件增加到结果中
	 */
	private LabelInputMeta addfiletoresult;

	/**
	 * 合格条件
	 */
	private ColumnFieldsetMeta successCondition;

	/**
	 * 合格
	 */
	private LabelSelectMeta success_condition;

	/**
	 * 极限文件
	 */
	private LabelInputMeta nr_limit;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	public JobEntryUnZipDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryUnZip job = (JobEntryUnZip) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "一般", "高级" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * 一般
			 ******************************************************************/

			// 源文件
			this.sourceFile = new ColumnFieldsetMeta(null, "源文件");
			this.sourceFile.setSingle(true);
			// 从上一个作业获取参数
			boolean isfrompreviousClick = job.getDatafromprevious();
			this.isfromprevious = new LabelInputMeta(id + ".isfromprevious",
					"从上一个作业获取参数", null, null, null, String
							.valueOf(isfrompreviousClick),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.isfromprevious
					.addClick("jQuery.imeta.jobEntries.unzip.listeners.isfrompreviousClick");
			this.isfromprevious.setSingle(true);

			// 得到Zip 文件名
			// List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			this.zipFilename = new LabelInputMeta(id + ".zipFilename",
					"Zip文件名", null, null, null, job.getZipFilename(), null,
					ValidateForm.getInstance().setRequired(false));
			// this.editconncet = new ButtonMeta(id + ".btn.editconncet", id
			// + ".btn.editconncet", "文件", "文件");
			// this.newconncet = new ButtonMeta(id + ".btn.newconncet", id
			// + ".btn.newconncet", "目录", "目录");
			//
			// this.zipFilename.addButton(new ButtonMeta[] { this.editconncet,
			// this.newconncet });
			this.zipFilename.setDisabled(isfrompreviousClick);
			this.zipFilename.setSingle(true);
			// 源通配符
			this.wildcardSource = new LabelInputMeta(id + ".wildcardSource",
					"源通配符", null, null, "源通配符", job.getWildcardSource(), null,
					ValidateForm.getInstance().setRequired(false));
			this.wildcardSource.setDisabled(isfrompreviousClick);
			this.wildcardSource.setSingle(true);
			//			
			this.sourceFile
					.putFieldsetsContent(new BaseFormMeta[] {
							this.isfromprevious, this.zipFilename,
							this.wildcardSource });

			// 解压文件
			this.unzippedFiles = new ColumnFieldsetMeta(null, "解压文件");
			this.unzippedFiles.setSingle(true);
			// 使用压缩文件源
			this.rootzip = new LabelInputMeta(id + ".rootzip", "使用压缩文件源", null,
					null, null, String.valueOf(job.isCreateFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.rootzip.setSingle(true);

			// 得到目标目录
			// List<OptionDataMeta> option = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			this.targetdirectory = new LabelInputMeta(id + ".targetdirectory",
					"目标目录", null, null, null, job.getSourceDirectory(), null,
					ValidateForm.getInstance().setRequired(false));

			// this.enwconncet = new ButtonMeta(id + ".btn.enwconncet", id
			// + ".btn.enwconncet", "目录", "目录");
			// this.targetdirectory.addButton(new ButtonMeta[] { this.enwconncet
			// });
			this.targetdirectory.setSingle(true);

			// 创建文件夹
			this.createfolder = new LabelInputMeta(id + ".createfolder",
					"创建文件夹", null, null, null, String.valueOf(job
							.isCreateFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createfolder.setSingle(true);

			// 包含通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "包含通配符", null,
					null, null, job.getWildcard(), null, ValidateForm
							.getInstance().setRequired(false));
			this.wildcard.setSingle(true);

			// 不含通配符
			this.wildcardexclude = new LabelInputMeta(id + ".wildcardexclude",
					"不含通配符:", null, null, null, job.getWildcardExclude(), null,
					ValidateForm.getInstance().setRequired(false));
			this.wildcardexclude.setSingle(true);

			// 文件名包含日期
			boolean specifyFormatClick = job.isSpecifyFormat();
			this.adddate = new LabelInputMeta(id + ".adddate", "文件名包含日期", null,
					null, null, String.valueOf(job.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.adddate.setDisabled(specifyFormatClick);
			this.adddate.setSingle(true);

			// 文件名包含时间
			this.addtime = new LabelInputMeta(id + ".addtime", "文件名包含时间", null,
					null, null, String.valueOf(job.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addtime.setDisabled(specifyFormatClick);
			this.addtime.setSingle(true);

			// 指定日期时间格式
			this.SpecifyFormat = new LabelInputMeta(id + ".SpecifyFormat",
					"指定日期时间格式", null, null, null, String
							.valueOf(specifyFormatClick),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat
					.addClick("jQuery.imeta.jobEntries.unzip.listeners.isSpecifyFormatClick");
			this.SpecifyFormat.setSingle(true);

			// 日期时间格式
			String dats[] = Const.getDateFormats();
			List<OptionDataMeta> datetimeOptions = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < dats.length; i++) {
				datetimeOptions.add(new OptionDataMeta(String.valueOf(i + 1),
						dats[i]));
			}
			this.date_time_format = new LabelSelectMeta(id
					+ ".date_time_format", "日期时间格式", null, null, null, job
					.getDateTimeFormat(), null, datetimeOptions);
			this.date_time_format.setDisabled(!specifyFormatClick);
			this.date_time_format.setSingle(true);
			this.date_time_format.setHasEmpty(true);
			// 如果文件存在
			String[] options = JobEntryUnZip.typeIfFileExistsDesc;
			List<OptionDataMeta> iffileexistOptions = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < options.length; i++) {
				iffileexistOptions.add(new OptionDataMeta(
						String.valueOf(i + 1), options[i]));
			}
			this.iffileexist = new LabelSelectMeta(id + ".iffileexist",
					"如果文件存在", null, null, null, String.valueOf(job
							.getIfFileExist()), null, iffileexistOptions);
			this.iffileexist.setSingle(true);
			// 解压缩后

			List<OptionDataMeta> afterunzipOptions = new ArrayList<OptionDataMeta>();
			afterunzipOptions.add(new OptionDataMeta("0", Messages
					.getString("JobUnZip.Do_Nothing_AfterUnZip.Label")));
			afterunzipOptions.add(new OptionDataMeta("1", Messages
					.getString("JobUnZip.Delete_Files_AfterUnZip.Label")));
			afterunzipOptions.add(new OptionDataMeta("2", Messages
					.getString("JobUnZip.Move_Files_AfterUnZip.Label")));
			this.afterunzip = new LabelSelectMeta(id + ".afterunzip", "解压缩后",
					null, null, "什么都不做", String.valueOf(job.afterunzip), null,
					afterunzipOptions);
			this.afterunzip.addListener("change",
					"jQuery.imeta.jobEntries.unzip.listeners.afterunzipChange");
			this.afterunzip.setSingle(true);
			// 移动文件
			this.movetodirectory = new LabelInputMeta(id + ".movetodirectory",
					"移动文件到", null, null, "移动文件到", job.getMoveToDirectory(),
					null, ValidateForm.getInstance().setRequired(false));
			this.movetodirectory.setDisabled(job.afterunzip != 2);
			this.movetodirectory.setSingle(true);
			// 创建目录
			this.createMoveToDirectory = new LabelInputMeta(id
					+ ".createMoveToDirectory", "创建目录", null, null, null,
					String.valueOf(job.isCreateMoveToDirectory()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createMoveToDirectory.setDisabled(job.afterunzip != 2);
			this.createMoveToDirectory.setSingle(true);
			//
			this.unzippedFiles.putFieldsetsContent(new BaseFormMeta[] {
					this.rootzip, this.targetdirectory, this.createfolder,
					this.wildcard, this.wildcardexclude, this.adddate,
					this.addtime, this.SpecifyFormat, this.date_time_format,
					this.iffileexist, this.afterunzip, this.movetodirectory,
					this.createMoveToDirectory });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.sourceFile,
					this.unzippedFiles });

			/*******************************************************************
			 * 高级
			 ******************************************************************/
			// 结果文件名
			this.resultFileName = new ColumnFieldsetMeta(null, "结果文件名");
			this.resultFileName.setSingle(true);

			// 将抽出来的文件增加到结果中
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult",
					"将抽出来的文件增加到结果中", null, null, null, String.valueOf(job
							.isAddFileToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addfiletoresult.setSingle(true);
			//			
			this.resultFileName
					.putFieldsetsContent(new BaseFormMeta[] { this.addfiletoresult });

			// 合格条件
			this.successCondition = new ColumnFieldsetMeta(null, "合格条件");
			this.successCondition.setSingle(true);

			// 合格
			List<OptionDataMeta> success_conditionOptions = new ArrayList<OptionDataMeta>();
			success_conditionOptions.add(new OptionDataMeta("0", Messages
					.getString("JobUnZip.SuccessWhenAllWorksFine.Label")));
			success_conditionOptions.add(new OptionDataMeta("1", Messages
					.getString("JobUnZip.SuccessWhenAtLeat.Label")));
			success_conditionOptions.add(new OptionDataMeta("2", Messages
					.getString("JobUnZip.SuccessWhenNrErrorsLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id
					+ ".success_condition", "合格", null, null, "合格", job
					.getSuccessCondition(), null, success_conditionOptions);
			this.success_condition.addListener("change",
					"jQuery.imeta.jobEntries.unzip.listeners.successOnChange");
			this.success_condition.setSingle(true);
			// 极限文件
			this.nr_limit = new LabelInputMeta(id + ".nr_limit", "限制文件个数",
					null, null, "10", job.getLimit(), null, ValidateForm
							.getInstance().setRequired(false));
			this.nr_limit.setDisabled(job.getSuccessCondition().equals(
					"success_if_no_errors"));
			this.nr_limit.setSingle(true);
			//
			this.successCondition.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_limit });

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.resultFileName, this.successCondition });

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
