package com.panet.imeta.job.entries.exportrepository;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryExportRepositoryDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * Job entry name
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	//-----页签1
	/**
	 * Repository
	 */
	private ColumnFieldsetMeta repository;

	/**
	 *Repository name
	 */

	private LabelInputMeta repositoryname;

	/**
	 *Repository use
	 */

	private LabelInputMeta username;

	/**
	 *Repository password
	 */

	private LabelInputMeta password;

//	/**
//	 *Test connection 按键
//	 */
//	private ButtonMeta testConnectionBtn;

	/**
	 * Settings  框
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * Export type
	 */
	private LabelSelectMeta export_type;

	/**
	 *Folder name
	 */
	private LabelInputMeta directoryPath;

	/**
	 *Create separate folde
	 */
	private LabelInputMeta createfolder;

	/**
	 * Target   框
	 */
	private ColumnFieldsetMeta target;

	/**
	 *Target folder/filename
	 */
	private LabelInputMeta targetfilename;

	/**
	 *Create folder #
	 */
	private LabelInputMeta newfolder;

	/**
	 *Add date to filename # 
	 */
	private LabelInputMeta add_date;

	/**
	 *Add time to filename # 
	 */
	private LabelInputMeta add_time;

	/**
	 *Specify Date time format #
	 */
	private LabelInputMeta SpecifyFormat;

	/**
	 *Date time format
	 */
	private LabelInputMeta date_time_format;

	/**
	 * If target file exists
	 */
	private LabelSelectMeta iffileexists;

	/**
	 *Add filenames to resul
	 */
	private LabelInputMeta add_result_filesname;

	//-----页签2
	/**
	 * Target   框
	 */
	private ColumnFieldsetMeta successOn;

	/**
	 * Success condition 下拉s
	 */
	private LabelSelectMeta success_condition;

	/**
	 *Limit
	 */
	private LabelInputMeta nr_errors_less_than;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryExportRepositoryDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryExportRepository jobEntry = (JobEntryExportRepository) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "高级" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 页签 General			 
			 ******************************************************************/

			// ------------------------- Repository ----------------
			this.repository = new ColumnFieldsetMeta(null, "库");
			this.repository.setSingle(true);

			//Repository name
			this.repositoryname = new LabelInputMeta(id + ".repositoryname",
					"库名称", null, null, "库名称", jobEntry.getRepositoryname(), null,
					ValidateForm.getInstance().setRequired(false));

			this.repositoryname.setSingle(true);

			// Repository user
			this.username = new LabelInputMeta(id + ".username",
					"库用户", null, null, "库用户", jobEntry.getUsername(), null,
					ValidateForm.getInstance().setRequired(false));
			this.username.setSingle(true);

			// Repository password
			this.password = new LabelInputMeta(id + ".password", "仓库密码",
					null, null, "仓库密码", jobEntry.getPassword(), null,
					ValidateForm.getInstance().setRequired(false));
			this.password.setSingle(true);

//			// Test connection
//			this.testConnectionBtn = new ButtonMeta(id
//					+ ".btn.testConnectionBtn", id + ".btn.testConnectionBtn",
//					"测试连接 ", " 测试连接 ");
//			this.testConnectionBtn.setWidth(15);
//			this.testConnectionBtn.setSingle(true);

			//------------------Repository -------------	
			this.repository.putFieldsetsContent(new BaseFormMeta[] {
					this.repositoryname, this.username, //内容
					this.password
//					, this.testConnectionBtn
});

			// ----------------Settings ------------------------
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			//Export type
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("export_type", "导出所有对象"));
			options.add(new OptionDataMeta("export_type", "仅导出任务对象"));
			options.add(new OptionDataMeta("export_type", "仅导出转换对象"));
			options.add(new OptionDataMeta("export_type", "导出一个文件夹"));
			options.add(new OptionDataMeta("export_type", "按文件夹导出"));

			this.export_type = new LabelSelectMeta(id + ".export_type", "导出类型：",
					null, null, null, jobEntry.getExportType(), null, options);
			this.export_type.setSingle(true);

			// Folder name
			this.directoryPath = new LabelInputMeta(id + ".directoryPath", "文件夹名称",
					null, null, "文件夹名称", jobEntry.getDirectory(), null,
					ValidateForm.getInstance().setRequired(false));
			this.directoryPath.setSingle(true);

			//Create separate folder
			this.createfolder = new LabelInputMeta(id + ".createfolder",
					"创建单独的文件夹", null, null, null, 
					String.valueOf(jobEntry.isCreateFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createfolder.setSingle(true);

			//------------------settings -------------	
			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.export_type, this.directoryPath, //内容
					this.createfolder });

			// ----------------Target  ------------------------
			this.target = new ColumnFieldsetMeta(null, " 目标 ");
			this.target.setSingle(true);

			// Target folder/filename
			this.targetfilename = new LabelInputMeta(id + ".targetfilename",
					"目标文件夹/文件名", null, null, "目标文件夹/文件名", jobEntry.getTargetfilename(),
					null, ValidateForm.getInstance().setRequired(false));
			this.targetfilename.setSingle(true);

			//Create folder
			this.newfolder = new LabelInputMeta(id + ".newfolder",
					"创建文件夹", null, null, null, String.valueOf(jobEntry.isNewFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.newfolder.setSingle(true);

			//Add date to filename
			this.add_date = new LabelInputMeta(id + ".add_date", "添加日期到文件名",
					null, null, null, String.valueOf(jobEntry.isAddDate()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.add_date.setDisabled(jobEntry.isSpecifyFormat());
			this.add_date.setSingle(true);

			//Add time to filename
			this.add_time = new LabelInputMeta(id + ".add_time", "添加时间到文件名", null,
					null, null, null, InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.add_time.setDisabled(jobEntry.isSpecifyFormat());
			this.add_time.setSingle(true);

			//Specify Date time format
			this.SpecifyFormat = new LabelInputMeta(id + ".SpecifyFormat",
					"指定日期时间格式", null, null, null, String.valueOf(jobEntry.isSpecifyFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat.addClick("jQuery.imeta.jobEntries.exportrepository.listeners.SpecifyFormat");
			this.SpecifyFormat.setSingle(true);

			// Date time format
			this.date_time_format = new LabelInputMeta(id + ".date_time_format",
					"  日期时间格式化 ", null, null, "日期时间格式化",jobEntry.getDateTimeFormat(),
					null, ValidateForm.getInstance().setRequired(false));
			this.date_time_format.setSingle(true);
			this.date_time_format.setDisabled(!jobEntry.isSpecifyFormat());

			//If target file exists
			options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("iffileexists", "跳过"));
			options.add(new OptionDataMeta("iffileexists", "替换文件"));
			options.add(new OptionDataMeta("iffileexists", "建立档案与独特的名称"));
			options.add(new OptionDataMeta("iffileexists", "失败"));

			this.iffileexists = new LabelSelectMeta(id + ".iffileexists",
					"如果目标文件存在：", null, null, null, jobEntry.getIfFileExists(), null, options);
			this.iffileexists.setSingle(true);

			//Add filenames to result
			this.add_result_filesname = new LabelInputMeta(id + ".add_result_filesname",
					"添加文件名的结果", null, null, null, String.valueOf(jobEntry.isAddresultfilesname()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.add_result_filesname.setSingle(true);

			//------------------Target  -------------	
			this.target.putFieldsetsContent(new BaseFormMeta[] {
					this.targetfilename,
					this.newfolder, //内容
					this.add_date, this.add_time, this.SpecifyFormat,
					this.date_time_format, this.iffileexists, this.add_result_filesname

			});

			//页签里面添加内容

			this.meta.putTabContent(0, new BaseFormMeta[] { this.repository,
					this.settings, this.target });

			/*******************************************************************
			 * 页签 Advanced			 
			 ******************************************************************/

			// ----------------Success On  ------------------------
			this.successOn = new ColumnFieldsetMeta(null, "成功");
			this.successOn.setSingle(true);

			//Success condition
			options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("success_condition", "所有工作正常是否成功"));
			options.add(new OptionDataMeta("success_condition", "发生错误的数目是否更少"));

			this.success_condition = new LabelSelectMeta(
					id + ".success_condition", "取得成功的条件：", null, null, null,
					jobEntry.getSuccessCondition(), null, options);
			this.success_condition.setSingle(true);

			// nr_errors_less_than
			this.nr_errors_less_than = new LabelInputMeta(id + ".nr_errors_less_than", "极限", null, null,
					"极限", jobEntry.getNrLimit(), null, ValidateForm
							.getInstance().setRequired(false));
			this.nr_errors_less_than.setSingle(true);

			//------------------ successOn  -------------	
			this.successOn.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_errors_less_than //内容
					});

			//页签里面添加内容

			this.meta.putTabContent(1, new BaseFormMeta[] { this.successOn });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定，取消
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
