package com.panet.imeta.job.entries.http;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryHTTPDialog extends JobEntryDialog implements
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
	 * 网址
	 */
	private LabelInputMeta url;

	/**
	 * 运行每行结果?
	 */
	private LabelInputMeta runForEveryRow;
	/**
	 * 包含了网址的输入字段
	 */
	private LabelInputMeta urlFieldname;

	/**
	 * 目标文件
	 */
	private LabelInputMeta targetFilename;

	/**
	 * 添加到指定目标表?
	 */
	private LabelInputMeta fileAppended;

	/**
	 * 文件名添加时间日期?
	 */
	private LabelInputMeta dateTimeAdded;
	/**
	 * 目标文件扩展名
	 */
	private LabelInputMeta targetFilenameExtention;
	/**
	 * 上传文件
	 */
	private LabelInputMeta uploadFilename;
	/**
	 * 用户名
	 */
	private LabelInputMeta username;
	/**
	 * 密码
	 */
	private LabelInputMeta password;
	/**
	 * 上传代理服务器
	 */
	private LabelInputMeta proxyHostname;
	/**
	 * 代理端口
	 */
	private LabelInputMeta proxyPort;
	/**
	 * 对于下列主机忽略代理服务器
	 */
	private LabelInputMeta nonProxyHosts;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryHTTPDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryHTTP job = (JobEntryHTTP) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业名称
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					"作业名称是必填项，必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			boolean isRunForEveryRow = job.isRunForEveryRow();
			// 得到网址
			this.url = new LabelInputMeta(id + ".url", "网址:", null, null,
					"URL", job.getUrl(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.url.setSingle(true);
			this.url.setDisabled(isRunForEveryRow);

			// 运行每行结果?
			this.runForEveryRow = new LabelInputMeta(id + ".runForEveryRow",
					"是否运行所有行的结果?", null, null, null, String
							.valueOf(isRunForEveryRow),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.runForEveryRow
					.addClick("jQuery.imeta.jobEntries.http.listeners.isRunForEveryRowClick");
			this.runForEveryRow.setSingle(true);
			// 得到包含了网址的输入字段
			this.urlFieldname = new LabelInputMeta(id + ".urlFieldname",
					"包含了网址的输入字段", null, null, "包含了网址的输入字段", job
							.getUrlFieldname(), null, ValidateForm
							.getInstance().setRequired(false));
			this.urlFieldname.setDisabled(!isRunForEveryRow);
			this.urlFieldname.setSingle(true);
			// 得到目标表
			this.targetFilename = new LabelInputMeta(id + ".targetFilename",
					"目标文件", null, null, "目标文件", job.getTargetFilename(), null,
					ValidateForm.getInstance().setRequired(false));
			this.targetFilename.setSingle(true);

			boolean isDateTimeAdded = job.isDateTimeAdded();
			// 添加到指定目标表?
			this.fileAppended = new LabelInputMeta(id + ".fileAppended",
					"添加到指定目标文件?", null, null, "添加到指定目标文件?", String.valueOf(job
							.isFileAppended()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.fileAppended.setSingle(true);
			this.fileAppended.setDisabled(isDateTimeAdded);
			// 文件名添加时间日期?

			this.dateTimeAdded = new LabelInputMeta(id + ".dateTimeAdded",
					"文件名添加日期时间?", null, null, null, String
							.valueOf(isDateTimeAdded),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dateTimeAdded
					.addClick("jQuery.imeta.jobEntries.http.listeners.isDateTimeAddedClick");
			this.dateTimeAdded.setSingle(true);
			// 目标文件扩展名
			this.targetFilenameExtention = new LabelInputMeta(id
					+ ".targetFilenameExtention", "目标文件扩展名:", null, null,
					"目标文件扩展名", job.getTargetFilenameExtention(), null,
					ValidateForm.getInstance().setRequired(false));
			this.targetFilenameExtention.setSingle(true);
			this.targetFilenameExtention.setDisabled(!isDateTimeAdded);

			// 得到上传文件
			this.uploadFilename = new LabelInputMeta(id + ".uploadFilename",
					"上传文件名", null, null, "上传文件名", job.getUploadFilename(),
					null, ValidateForm.getInstance().setRequired(false));
			this.uploadFilename.setSingle(true);

			// 得到用户名
			this.username = new LabelInputMeta(id + ".username", "用户名", null,
					null, "用户名", job.getUsername(), null, ValidateForm
							.getInstance().setRequired(false));
			this.username.setSingle(true);

			// 得到密码
			this.password = new LabelInputMeta(id + ".password", "密码", null,
					null, "密码", job.getPassword(),
					InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(false));
			this.password.setSingle(true);

			// 得到上传代理服务器
			this.proxyHostname = new LabelInputMeta(id + ".proxyHostname",
					"上传代理服务器", null, null, "上传代理服务器", job.getProxyHostname(),
					null, ValidateForm.getInstance().setRequired(false));
			this.proxyHostname.setSingle(true);

			// 得到代理端口
			this.proxyPort = new LabelInputMeta(id + ".proxyPort", "代理端口",
					null, null, "代理端口", job.getProxyPort(), null, ValidateForm
							.getInstance().setRequired(false));
			this.proxyPort.setSingle(true);

			// 得到对于下列主机忽略代理服务器
			this.nonProxyHosts = new LabelInputMeta(id + ".nonProxyHosts",
					"下列主机忽略代理服务器", null, null, "对于下列主机忽略代理服务器", job
							.getNonProxyHosts(), null, ValidateForm
							.getInstance().setRequired(false));
			this.nonProxyHosts.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.url, this.runForEveryRow, this.urlFieldname,
					this.targetFilename, this.fileAppended, this.dateTimeAdded,
					this.targetFilenameExtention, this.uploadFilename,
					this.username, this.password, this.proxyHostname,
					this.proxyPort, this.nonProxyHosts });

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
