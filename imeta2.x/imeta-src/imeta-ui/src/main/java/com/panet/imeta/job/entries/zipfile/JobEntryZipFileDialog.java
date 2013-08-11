package com.panet.imeta.job.entries.zipfile;

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

public class JobEntryZipFileDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 作业条目名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	private MenuTabMeta meta1;

	/***************************************************************************
	 * 一般
	 **************************************************************************/

	/**
	 * 文件来源
	 */
	private ColumnFieldsetMeta sourceFile;
	/**
	 * 得到论点
	 */
	private LabelInputMeta isfromprevious;

	/**
	 * 源目录
	 */
	private LabelInputMeta sourcedirectory;

	// private ButtonMeta editconncet;
	//
	// private ButtonMeta newconncet;
	/**
	 * 包含通配符
	 */
	private LabelInputMeta wildcard;
	/**
	 * 排除通配符
	 */
	private LabelInputMeta wildcardexclude;
	/**
	 * Zip 文件
	 */
	private ColumnFieldsetMeta zipFile;

	/**
	 * Zip 文件名
	 */
	private LabelInputMeta zipFilename;

	// private ButtonMeta enwconncet;
	/**
	 * 创建父文件夹
	 */
	private LabelInputMeta createparentfolder;
	/**
	 * 包括日期文件名
	 */
	private LabelInputMeta adddate;
	/**
	 * 包括时间文件名
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

	/***************************************************************************
	 * 高级
	 **************************************************************************/
	/**
	 * 高级
	 */
	private ColumnFieldsetMeta advanced;

	/**
	 * 压缩
	 */
	private LabelSelectMeta compressionrate;
	/**
	 * 如果zip文件存在
	 */
	private LabelSelectMeta ifzipfileexists;
	/**
	 * 压缩后
	 */
	private LabelSelectMeta afterzip;
	/**
	 * 移动文件到
	 */
	private LabelInputMeta movetodirectory;

	/**
	 * 结果文件名
	 */
	private ColumnFieldsetMeta destinationAddressests;
	/**
	 * 结果添加zip文件
	 */
	private LabelInputMeta addfiletoresult;

	public JobEntryZipFileDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryZipFile job = (JobEntryZipFile) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业条目名称
			this.name = new LabelInputMeta(id + ".name", "作业条目名称", null, null,
					"作业条目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "一般", "高级" });
			this.meta.setSingle(true);

			this.meta1 = new MenuTabMeta(id, new String[] { "一般", "高级" });
			this.meta1.setSingle(true);
			/*******************************************************************
			 * 一般
			 ******************************************************************/

			// 文件来源
			this.sourceFile = new ColumnFieldsetMeta(null, "文件来源");
			this.sourceFile.setSingle(true);
			// 得到论点
			boolean isfrompreviousClick = job.getDatafromprevious();
			this.isfromprevious = new LabelInputMeta(id + ".isfromprevious",
					"从上一个作业获取参数", null, null, null, String
							.valueOf(isfrompreviousClick),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.isfromprevious
					.addClick("jQuery.imeta.jobEntries.zipFile.listeners.isfrompreviousClick");
			this.isfromprevious.setSingle(true);

			// 得到源目录
			// List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			// options.add(new OptionDataMeta("dbconncet", ""));
			this.sourcedirectory = new LabelInputMeta(id + ".sourcedirectory",
					"源目录", null, null, "源目录", job.getSourceDirectory(), null,
					ValidateForm.getInstance().setRequired(false));
			// this.editconncet = new ButtonMeta(id + ".btn.editconncet", id
			// + ".btn.editconncet", "文件", "文件");
			// this.newconncet = new ButtonMeta(id + ".btn.newconncet", id
			// + ".btn.newconncet", "文件夹", "文件夹");
			// this.sourcedirectory.addButton(new ButtonMeta[] {
			// this.editconncet,
			// this.newconncet });
			this.sourcedirectory.setDisabled(isfrompreviousClick);
			this.sourcedirectory.setSingle(true);
			// 包含通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "包含通配符", null,
					null, "包含通配符", job.getWildcard(), null, ValidateForm
							.getInstance().setRequired(false));
			this.wildcard.setDisabled(isfrompreviousClick);
			this.wildcard.setSingle(true);
			// 排除通配符
			this.wildcardexclude = new LabelInputMeta(id + ".wildcardexclude",
					"排除通配符", null, null, "排除通配符", job.getWildcardExclude(),
					null, ValidateForm.getInstance().setRequired(false));
			this.wildcardexclude.setDisabled(isfrompreviousClick);
			this.wildcardexclude.setSingle(true);

			this.sourceFile.putFieldsetsContent(new BaseFormMeta[] {
					this.isfromprevious, this.sourcedirectory, this.wildcard,
					this.wildcardexclude });

			// Zip 文件
			this.zipFile = new ColumnFieldsetMeta(null, "Zip 文件");
			this.zipFile.setSingle(true);

			// 得到Zip 文件名
			// List<OptionDataMeta> option = new ArrayList<OptionDataMeta>();
			// options.add(new OptionDataMeta());
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			// options.add(new OptionDataMeta("bdconncet", ""));
			this.zipFilename = new LabelInputMeta(id + ".zipFilename",
					"Zip 文件名", null, null, "Zip 文件名", job.getZipFilename(),
					null, ValidateForm.getInstance().setRequired(false));

			// this.enwconncet = new ButtonMeta(id + ".btn.enwconncet", id
			// + ".btn.enwconncet", "浏览", "浏览");
			// this.zipFilename.addButton(new ButtonMeta[] { this.enwconncet });
			this.zipFilename.setDisabled(isfrompreviousClick);
			this.zipFilename.setSingle(true);
			// 创建父文件夹
			this.createparentfolder = new LabelInputMeta(id
					+ ".createparentfolder", "创建父文件夹", null, null, null,
					String.valueOf(job.getcreateparentfolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createparentfolder.setSingle(true);
			// 包括日期文件名
			boolean specifyFormatClick = job.isSpecifyFormat();
			this.adddate = new LabelInputMeta(id + ".adddate", "包括日期文件名", null,
					null, null, String.valueOf(job.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.adddate.setDisabled(isfrompreviousClick);
			this.adddate.setDisabled(specifyFormatClick);
			this.adddate.setSingle(true);
			// 包括时间文件名
			this.addtime = new LabelInputMeta(id + ".addtime", "包括时间文件名", null,
					null, null, String.valueOf(job.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addtime.setDisabled(isfrompreviousClick);
			this.addtime.setDisabled(specifyFormatClick);
			this.addtime.setSingle(true);
			// 指定日期时间格式
			this.SpecifyFormat = new LabelInputMeta(id + ".SpecifyFormat",
					"指定日期时间格式", null, null, null, String.valueOf(job
							.isSpecifyFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat
					.addClick("jQuery.imeta.jobEntries.zipFile.listeners.isSpecifyFormatClick");
			this.SpecifyFormat.setSingle(true);

			// 日期时间格式
			String dats[] = Const.getDateFormats();
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < dats.length; i++) {
				options.add(new OptionDataMeta(String.valueOf(i + 1), dats[i]));
			}
			this.date_time_format = new LabelSelectMeta(id
					+ ".date_time_format", "日期时间格式", null, null,
					"Date time format", job.getDateTimeFormat(), null, options);
			this.date_time_format.setDisabled(!specifyFormatClick);
			this.date_time_format.setSingle(true);
			this.date_time_format.setHasEmpty(true);

			this.zipFile.putFieldsetsContent(new BaseFormMeta[] {
					this.zipFilename, this.createparentfolder, this.adddate,
					this.addtime, this.SpecifyFormat, this.date_time_format });
			this.meta.putTabContent(0, new BaseFormMeta[] { this.sourceFile,
					this.zipFile });

			/*******************************************************************
			 * 高级
			 ******************************************************************/
			this.advanced = new ColumnFieldsetMeta(null, "高级");
			this.advanced.setSingle(true);

			// 压缩
			List<OptionDataMeta> compressionrateOptions = new ArrayList<OptionDataMeta>();
			compressionrateOptions.add(new OptionDataMeta("0", Messages
					.getString("JobZipFiles.NO_COMP_CompressionRate.Label")));
			compressionrateOptions.add(new OptionDataMeta("1", Messages
					.getString("JobZipFiles.DEF_COMP_CompressionRate.Label")));
			compressionrateOptions.add(new OptionDataMeta("2", Messages
					.getString("JobZipFiles.BEST_COMP_CompressionRate.Label")));
			compressionrateOptions
					.add(new OptionDataMeta(
							"3",
							Messages
									.getString("JobZipFiles.BEST_SPEED_CompressionRate.Label")));
			this.compressionrate = new LabelSelectMeta(id + ".compressionrate",
					"压缩", null, null, null,
					String.valueOf(job.compressionrate), null,
					compressionrateOptions);
			this.compressionrate.setSingle(true);
			// 如果zip文件存在
			List<OptionDataMeta> ifzipfileexistsOptions = new ArrayList<OptionDataMeta>();
			ifzipfileexistsOptions
					.add(new OptionDataMeta(
							"0",
							Messages
									.getString("JobZipFiles.Create_NewFile_IfFileExists.Label")));
			ifzipfileexistsOptions.add(new OptionDataMeta("1", Messages
					.getString("JobZipFiles.Append_File_IfFileExists.Label")));
			ifzipfileexistsOptions.add(new OptionDataMeta("2", Messages
					.getString("JobZipFiles.Do_Nothing_IfFileExists.Label")));
			ifzipfileexistsOptions.add(new OptionDataMeta("3", Messages
					.getString("JobZipFiles.Fail_IfFileExists.Label")));
			this.ifzipfileexists = new LabelSelectMeta(id + ".ifzipfileexists",
					"如果zip文件存在", null, null, null, String
							.valueOf(job.ifzipfileexists), null,
					ifzipfileexistsOptions);
			this.ifzipfileexists.setSingle(true);
			// 压缩后
			List<OptionDataMeta> afterzipOptions = new ArrayList<OptionDataMeta>();
			afterzipOptions.add(new OptionDataMeta("0", Messages
					.getString("JobZipFiles.Do_Nothing_AfterZip.Label")));
			afterzipOptions.add(new OptionDataMeta("1", Messages
					.getString("JobZipFiles.Delete_Files_AfterZip.Label")));
			afterzipOptions.add(new OptionDataMeta("2", Messages
					.getString("JobZipFiles.Move_Files_AfterZip.Label")));
			this.afterzip = new LabelSelectMeta(id + ".afterzip", "压缩后:", null,
					null, null, String.valueOf(job.afterzip), null,
					afterzipOptions);
			this.afterzip.addListener("change",
					"jQuery.imeta.jobEntries.zipFile.listeners.afterzipChange");
			this.afterzip.setSingle(true);
			// 移动文件到
			this.movetodirectory = new LabelInputMeta(id + ".movetodirectory",
					"移动文件到", null, null, "移动文件到", job.getMoveToDirectory(),
					null, ValidateForm.getInstance().setRequired(false));
			this.movetodirectory.setDisabled(job.afterzip != 2);
			this.movetodirectory.setSingle(true);
			this.advanced.putFieldsetsContent(new BaseFormMeta[] {
					this.compressionrate, this.ifzipfileexists, this.afterzip,
					this.movetodirectory });

			// 结果文件名
			this.destinationAddressests = new ColumnFieldsetMeta(null, "结果文件名");
			this.destinationAddressests.setSingle(true);
			// 结果添加zip文件
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult",
					"结果添加zip文件", null, null, null, String.valueOf(job
							.isAddFileToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addfiletoresult.setSingle(false);

			this.destinationAddressests
					.putFieldsetsContent(new BaseFormMeta[] { this.addfiletoresult });
			this.meta.putTabContent(1, new BaseFormMeta[] { this.advanced,
					this.destinationAddressests });
			// 装载到form

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });
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
