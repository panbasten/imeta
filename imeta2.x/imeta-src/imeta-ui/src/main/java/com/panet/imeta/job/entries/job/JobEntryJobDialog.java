package com.panet.imeta.job.entries.job;

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

public class JobEntryJobDialog extends JobEntryDialog implements
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
	 * 作业名称
	 */
	private LabelInputMeta jobname;

	private ButtonMeta jobNameBtn;

	/**
	 * 数据库目录
	 */
	private LabelInputMeta directory;

	/**
	 * 设置日志
	 */
	private ColumnFieldsetMeta setLog;

	/**
	 * 指定日志文件?
	 */
	private LabelInputMeta setLogfile;

	/**
	 * 追加日志文件
	 */
	private LabelInputMeta setAppendLogfile;

	/**
	 * 日志文件名
	 */
	private LabelInputMeta logfile;

	/**
	 * 日志文件扩展名
	 */
	private LabelInputMeta logext;

	/**
	 * 日志文件包含日期
	 */
	private LabelInputMeta addDate;

	/**
	 * 日志文件包含时间
	 */
	private LabelInputMeta addTime;

	/**
	 * 日志级别
	 */
	private LabelSelectMeta logLevel;

	/**
	 * 将上一结果作为参数
	 */
	private LabelInputMeta argFromPrevious;

	/**
	 * 对每个输入行执行一次
	 */
	private LabelInputMeta execPerRow;

	/**
	 * 远程从服务器
	 */
	private LabelSelectMeta remoteSlaveServerName;

	private LabelInputMeta waitingToFinish, followingAbortRemotely;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryJobDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryJob je = (JobEntryJob) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 作业名称
			this.jobname = new LabelInputMeta(id + ".jobname", "作业名称", null,
					null, "作业名称必须填写", je.getJobName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.jobNameBtn = new ButtonMeta(id + ".btn.jobName", id
					+ ".btn.jobName", "作业", "作业");
			this.jobNameBtn
					.addClick("jQuery.imeta.jobEntries.job.btn.jobnameBtn");
			this.jobname.addButton(new ButtonMeta[] { this.jobNameBtn });
			this.jobname.setSingle(true);
			this.jobname.setReadonly(true);

			// 资源库目录
			this.directory = new LabelInputMeta(id + ".directory", "资源库目录",
					null, null, "资源库目录必须填写", je.getDirectory(), null,
					ValidateForm.getInstance().setRequired(true));
			this.directory.setSingle(true);

			// 设置日志
			this.setLog = new ColumnFieldsetMeta(null, "设置日志");
			this.setLog.setSingle(true);

			// 指定日志文件
			this.setLogfile = new LabelInputMeta(id + ".setLogfile", "指定日志文件?",
					null, null, null, String.valueOf(je.setLogfile),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.setLogfile
					.addClick("jQuery.imeta.jobEntries.job.listeners.setLogfile");

			// 追加文件日志
			this.setAppendLogfile = new LabelInputMeta(
					id + ".setAppendLogfile", "追加文件日志", null, null, null,
					String.valueOf(je.setAppendLogfile),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.setAppendLogfile.setDisabled(!je.setLogfile);

			// 日志文件名
			this.logfile = new LabelInputMeta(id + ".logfile", "日志文件名", null,
					null, "日志文件名必须填写", je.getLogfile(), null, ValidateForm
							.getInstance().setRequired(true));
			this.logfile.setSingle(true);
			this.logfile.setDisabled(!je.setLogfile);

			// 日志文件扩展名
			this.logext = new LabelInputMeta(id + ".logext", "日志文件扩展名", null,
					null, "日志文件扩展名必须填写", je.logext, null, ValidateForm
							.getInstance().setRequired(true));
			this.logext.setSingle(true);
			this.logext.setDisabled(!je.setLogfile);

			// 日志包含日期
			this.addDate = new LabelInputMeta(id + ".addDate", "日志文件中包含日期",
					null, null, null, String.valueOf(je.addDate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.addDate.setDisabled(!je.setLogfile);

			// 日志包含时间
			this.addTime = new LabelInputMeta(id + ".addTime", "日志文件中包含时间",
					null, null, null, String.valueOf(je.addTime),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.addTime.setSingle(true);

			// 日志级别
			this.logLevel = new LabelSelectMeta(id + ".loglevel", "日志级别", null,
					null, null, String.valueOf(je.loglevel), null, super
							.getLoglevel());
			this.logLevel.setSingle(true);
			this.logLevel.setDisabled(!je.setLogfile);

			this.setLog.putFieldsetsContent(new BaseFormMeta[] {
					this.setLogfile, this.setAppendLogfile, this.logfile,
					this.logext, this.addDate, this.addTime, this.logLevel });

			// 将上一结果作为参数
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"将上一结果作为参数", null, null, null, String
							.valueOf(je.argFromPrevious),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 对每个输入行执行一次
			this.execPerRow = new LabelInputMeta(id + ".execPerRow",
					"对每个输入行执行一次", null, null, null, String
							.valueOf(je.execPerRow),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 远程从服务器
			this.remoteSlaveServerName = new LabelSelectMeta(id
					+ ".remoteSlaveServerName", "远程从服务器", null, null, null, je
					.getRemoteSlaveServerName(), null, super.getSlaveServer());
			this.remoteSlaveServerName.setSingle(true);

			// Wait for the job
			this.waitingToFinish = new LabelInputMeta(id + ".waitingToFinish",
					"等待远程作业完成", null, null, null, String
							.valueOf(je.waitingToFinish),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// Follow local abort to remote
			this.followingAbortRemotely = new LabelInputMeta(id
					+ ".followingAbortRemotely", "随着本地中止结束远程作业", null, null,
					null, String.valueOf(je.followingAbortRemotely),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.arguments", "参数",
							null, false, 300) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.jobEntries.job.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] values = je.arguments;
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, je.arguments[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.jobname, this.directory, this.setLog,
					this.argFromPrevious, this.execPerRow,
					this.remoteSlaveServerName, this.waitingToFinish,
					this.followingAbortRemotely, this.fields });

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
