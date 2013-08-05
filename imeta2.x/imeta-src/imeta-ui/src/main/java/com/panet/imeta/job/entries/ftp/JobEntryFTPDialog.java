package com.panet.imeta.job.entries.ftp;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
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

public class JobEntryFTPDialog extends JobEntryDialog implements
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

	/***************************************
	 * General
	 ***************************************/

	/**
	 * Server
	 */
	private ColumnFieldsetMeta server;

	/**
	 * FTP 服务器名称/IP地址
	 */
	private LabelInputMeta serverName;

	/**
	 * Server port
	 */
	private LabelInputMeta port;

	/**
	 * 用户名
	 */
	private LabelInputMeta userName;

	/**
	 * 密码
	 */
	private LabelInputMeta password;

	/**
	 * 代理服务器
	 */
	private LabelInputMeta proxyHost;

	/**
	 * 代理服务器端口
	 */
	private LabelInputMeta proxyPort;

	/**
	 * 代理服务器用户名
	 */
	private LabelInputMeta proxyUsername;

	/**
	 * 代理服务器密码
	 */
	private LabelInputMeta proxyPassword;

	private ButtonMeta dbTest;

	/**
	 * Advanced
	 */
	private ColumnFieldsetMeta advanced;

	/**
	 * 二进制模式
	 */
	private LabelInputMeta binaryMode;

	/**
	 * 超时
	 */
	private LabelInputMeta timeout;

	/**
	 * 使用活动的FTP连接
	 */
	private LabelInputMeta activeConnection;

	/**
	 * 控制编码
	 */
	private LabelSelectMeta controlEncoding;

	/********************************
	 * File
	 ********************************/

	/**
	 * Remote
	 */
	private ColumnFieldsetMeta remote;

	/**
	 * 远程目标
	 */
	private LabelInputMeta ftpDirectory;

	// private ButtonMeta checkFoldera;

	/**
	 * 通配符(正则表达式)
	 */
	private LabelInputMeta wildcard;

	/**
	 * 获取后删除文件
	 */
	private LabelInputMeta remove;

	/**
	 * Move file after retrieval
	 */
	private LabelInputMeta movefiles;

	/**
	 * Move to folder
	 */
	private LabelInputMeta movetodirectory;

	// private ButtonMeta checkFolderb;

	/**
	 * Create folder
	 */
	private LabelInputMeta createmovefolder;

	/**
	 * Local
	 */
	private ColumnFieldsetMeta local;

	/**
	 * 目标目录
	 */
	private LabelInputMeta targetDirectory;

	// private ButtonMeta browse;

	/**
	 * Include date in filename
	 */
	private LabelInputMeta adddate;

	/**
	 * Include time in filename
	 */
	private LabelInputMeta addtime;

	/**
	 * Specify Date time format
	 */
	private LabelInputMeta SpecifyFormat;

	/**
	 * Date time format
	 */
	private LabelSelectMeta date_time_format;

	/**
	 * Add date before extension
	 */
	private LabelInputMeta AddDateBeforeExtension;

	/**
	 * 不能覆盖文件
	 */
	private LabelInputMeta onlyGettingNewFiles;

	/**
	 * If file exists
	 */
	private LabelSelectMeta SifFileExists;

	/**
	 * Add filename to result
	 */
	private LabelInputMeta isaddresult;

	/*******************************************
	 * Advanced
	 *******************************************/

	/**
	 * Success Condition
	 */
	private ColumnFieldsetMeta successCondition;

	/**
	 * Success on
	 */
	private LabelSelectMeta success_condition;

	/**
	 * Limit files
	 */
	private LabelInputMeta nr_limit;

	/*******************************************
	 * Socks Proxy
	 *******************************************/
	/**
	 * Proxy
	 */
	private ColumnFieldsetMeta proxy;

	/**
	 * Host
	 */
	private LabelInputMeta socksProxyHost;

	/**
	 * Port
	 */
	private LabelInputMeta socksProxyPort;

	/**
	 * Username
	 */
	private LabelInputMeta socksProxyUsername;

	/**
	 * Password
	 */
	private LabelInputMeta socksProxyPassword;

	public JobEntryFTPDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryFTP job = (JobEntryFTP) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "文件", "高级",
					"代理" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * General
			 ******************************************************************/

			// Server
			this.server = new ColumnFieldsetMeta(null, "服务器");
			this.server.setSingle(true);

			// FTP服务器名称/IP地址：
			this.serverName = new LabelInputMeta(id + ".serverName",
					"FTP服务器名称/IP地址：", null, null, "名称/地址必须填写", job
							.getServerName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.serverName.setSingle(true);

			// Server port：
			this.port = new LabelInputMeta(id + ".port", "服务器端口：", null, null,
					"服务器端口必须填写", job.getPort(), null, ValidateForm
							.getInstance().setRequired(true));
			this.port.setSingle(true);

			// 用户名：
			this.userName = new LabelInputMeta(id + ".userName", "用户名：", null,
					null, "用户名必须填写", job.getUserName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.userName.setSingle(true);

			// 密码：
			this.password = new LabelInputMeta(id + ".password", "密码：", null,
					null, "密码必须填写", job.getPassword(),
					InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(true));
			this.password.setSingle(true);

			// 代理服务器
			this.proxyHost = new LabelInputMeta(id + ".proxyHost", "代理服务器：",
					null, null, "服务器必须填写", job.getProxyHost(), null,
					ValidateForm.getInstance().setRequired(true));
			this.proxyHost.setSingle(true);

			// 代理服务器端口：
			this.proxyPort = new LabelInputMeta(id + ".proxyPort", "代理服务器端口：",
					null, null, "代理服务器端口必须填写", job.getProxyPort(), null,
					ValidateForm.getInstance().setRequired(true));
			this.proxyPort.setSingle(true);

			// 代理服务器用户名：
			this.proxyUsername = new LabelInputMeta(id + ".proxyUsername",
					"代理服务器用户名：", null, null, "代理服务器用户名必须填写", job
							.getProxyUsername(), null, ValidateForm
							.getInstance().setRequired(true));
			this.proxyUsername.setSingle(true);

			// 代理服务器密码：
			this.proxyPassword = new LabelInputMeta(id + ".proxyPassword",
					"代理服务器密码：", null, null, "代理服务器密码必须填写", job
							.getProxyPassword(),
					InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(true));
			this.proxyPassword.setSingle(true);

			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			this.dbTest = new ButtonMeta(id + ".btn.dbTest",
					id + ".btn.dbTest", "测试连接", "测试连接");
			this.dbTest.addClick("jQuery.imeta.jobEntries.ftp.btn.dbTest");
			this.dbTest.appendTo(div);

			this.server.putFieldsetsContent(new BaseFormMeta[] {
					this.serverName, this.port, this.userName, this.password,
					this.proxyHost, this.proxyPort, this.proxyUsername,
					this.proxyPassword
			// , div
					});

			// Advanced
			this.advanced = new ColumnFieldsetMeta(null, "高级");
			this.advanced.setSingle(true);

			// 二进制模式
			this.binaryMode = new LabelInputMeta(id + ".binaryMode", "二进制模式?",
					null, null, null, String.valueOf(job.isBinaryMode()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.binaryMode.setSingle(true);

			// 超时：
			this.timeout = new LabelInputMeta(id + ".timeout", "超时：", null,
					null, "超时必须填写", String.valueOf(job.getTimeout()), null,
					ValidateForm.getInstance().setRequired(true));
			this.timeout.setSingle(true);

			// 使用活动的FTP连接
			this.activeConnection = new LabelInputMeta(
					id + ".activeConnection", "使用活动的FTP连接?", null, null, null,
					String.valueOf(job.isActiveConnection()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.activeConnection.setSingle(true);

			// 优先级
			this.controlEncoding = new LabelSelectMeta(id + ".controlEncoding",
					"控制编码", null, null, null, job.getControlEncoding(), null,
					super.getEncoding());
			this.controlEncoding.setSingle(true);

			this.advanced.putFieldsetsContent(new BaseFormMeta[] {
					this.binaryMode, this.timeout, this.activeConnection,
					this.controlEncoding });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.server,
					this.advanced });

			/*********************************************************************
			 * Files
			 *********************************************************************/
			// Remote
			this.remote = new ColumnFieldsetMeta(null, "远程");
			this.remote.setSingle(true);

			// 远程目录：
			this.ftpDirectory = new LabelInputMeta(id + ".ftpDirectory",
					"远程目录：", null, null, "远程目录必须填写", job.getFtpDirectory(),
					null, ValidateForm.getInstance().setRequired(true));

			// this.checkFoldera = new ButtonMeta(id + ".btn.checkFoldera", id +
			// ".btn.checkFoldera",
			// "检查文件夹", "检查文件夹");
			//
			// this.ftpDirectory.addButton(new ButtonMeta[] { this.checkFoldera
			// });

			this.ftpDirectory.setSingle(true);

			// 通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符（正则表达式）：",
					null, null, "通配符必须填写", job.getWildcard(), null,
					ValidateForm.getInstance().setRequired(true));
			this.wildcard.setSingle(true);

			// 获取后删除文件
			this.remove = new LabelInputMeta(id + ".remove", "获取后删除文件.", null,
					null, null, String.valueOf(job.getRemove()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.remove.setSingle(true);

			// Move files after retrieval
			this.movefiles = new LabelInputMeta(id + ".movefiles",
					"检索完毕后移动文件夹？", null, null, null, String.valueOf(job
							.isMoveFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.movefiles.setSingle(true);
			this.movefiles
					.addClick("jQuery.imeta.jobEntries.ftp.listeners.movefiles");

			// Move to folder
			this.movetodirectory = new LabelInputMeta(id + ".movetodirectory",
					"移动到文件夹", null, null, "文件夹必须填写", job.getMoveToDirectory(),
					null, ValidateForm.getInstance().setRequired(true));

			// this.checkFolderb = new ButtonMeta(id + ".btn.checkFolderb", id +
			// ".btn.checkFolderb",
			// "检查文件夹", "检查文件夹");
			//
			// this.movetodirectory.addButton(new ButtonMeta[] {
			// this.checkFolderb });

			this.movetodirectory.setSingle(true);
			this.movetodirectory.setDisabled(!job.isMoveFiles());

			// Create folder
			this.createmovefolder = new LabelInputMeta(
					id + ".createmovefolder", "创建文件夹", null, null, null, String
							.valueOf(job.isCreateMoveFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.createmovefolder.setSingle(true);
			this.createmovefolder.setDisabled(!job.isMoveFiles());

			this.remote
					.putFieldsetsContent(new BaseFormMeta[] {
							this.ftpDirectory, this.wildcard, this.remove,
							this.movefiles, this.movetodirectory,
							this.createmovefolder });

			// Local
			this.local = new ColumnFieldsetMeta(null, "本地");
			this.local.setSingle(true);

			// 目标目录
			this.targetDirectory = new LabelInputMeta(id + ".targetDirectory",
					"目标目录：", null, null, "目标目录必须填写", job.getTargetDirectory(),
					null, ValidateForm.getInstance().setRequired(true));
			// this.browse = new ButtonMeta(id + ".btn.browse", id +
			// ".btn.browse",
			// "浏览", "浏览");
			//
			// this.targetDirectory.addButton(new ButtonMeta[] { this.browse });

			this.targetDirectory.setSingle(true);

			// Include date in filename
			this.adddate = new LabelInputMeta(id + ".adddate", "在文件名中包含日期",
					null, null, null, String.valueOf(job.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.adddate.setSingle(true);
			this.adddate.setDisabled(job.isSpecifyFormat());

			// Include time in filename
			this.addtime = new LabelInputMeta(id + ".addtime", "在文件名中包含时间",
					null, null, null, String.valueOf(job.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addtime.setSingle(true);
			this.addtime.setDisabled(job.isSpecifyFormat());

			// Specify Date time format
			this.SpecifyFormat = new LabelInputMeta(id + ".SpecifyFormat",
					"指定日期时间格式", null, null, null, String.valueOf(job
							.isSpecifyFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.SpecifyFormat.setSingle(true);
			this.SpecifyFormat
					.addClick("jQuery.imeta.jobEntries.ftp.listeners.SpecifyFormat");

			// Date time format

			this.date_time_format = new LabelSelectMeta(id
					+ ".date_time_format", "日期时间格式", null, null, null, job
					.getDateTimeFormat(), null, super.getDateFormats());
			this.date_time_format.setSingle(true);
			this.date_time_format.setDisabled(!job.isSpecifyFormat());

			// Add date before extension
			this.AddDateBeforeExtension = new LabelInputMeta(id
					+ ".AddDateBeforeExtension", "在扩展名前增加日期", null, null, null,
					String.valueOf(job.isAddDateBeforeExtension()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.AddDateBeforeExtension.setSingle(true);
			this.AddDateBeforeExtension.setDisabled(!job.isSpecifyFormat());

			// 不能覆盖文件
			this.onlyGettingNewFiles = new LabelInputMeta(id
					+ ".onlyGettingNewFiles", "不能覆盖文件:", null, null, null,
					String.valueOf(job.isOnlyGettingNewFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.onlyGettingNewFiles.setSingle(true);
			this.onlyGettingNewFiles
					.addClick("jQuery.imeta.jobEntries.ftp.listeners.onlyGettingNewFiles");

			// If file exists
			List<OptionDataMeta> optionsIf = new ArrayList<OptionDataMeta>();
			optionsIf.add(new OptionDataMeta("0", Messages
					.getString("JobFTP.Skip.Label")));
			optionsIf.add(new OptionDataMeta("1", Messages
					.getString("JobFTP.Give_Unique_Name.Label")));
			optionsIf.add(new OptionDataMeta("2", Messages
					.getString("JobFTP.Fail.Label")));

			this.SifFileExists = new LabelSelectMeta(id + ".SifFileExists",
					"如果文件存在", null, null, null, job.SifFileExists, null,
					optionsIf);
			this.SifFileExists.setSingle(true);
			this.SifFileExists.setDisabled(!job.isOnlyGettingNewFiles());

			// Add filename to result
			this.isaddresult = new LabelInputMeta(id + ".isaddresult",
					"在结果添加文件名", null, null, null, String.valueOf(job
							.isAddToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.isaddresult.setSingle(true);

			this.local.putFieldsetsContent(new BaseFormMeta[] {
					this.targetDirectory, this.adddate, this.addtime,
					this.SpecifyFormat, this.date_time_format,
					this.AddDateBeforeExtension, this.onlyGettingNewFiles,
					this.SifFileExists, this.isaddresult });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.remote,
					this.local });

			/*************************************************
			 * Advanced
			 ****************************************************/

			// Success condition
			this.successCondition = new ColumnFieldsetMeta(null, "成功条件");
			this.successCondition.setSingle(true);

			// Success on
			List<OptionDataMeta> optionsucc = new ArrayList<OptionDataMeta>();
			optionsucc
					.add(new OptionDataMeta(job.SUCCESS_IF_NO_ERRORS, Messages
							.getString("JobFTP.SuccessWhenAllWorksFine.Label")));
			optionsucc.add(new OptionDataMeta(
					job.SUCCESS_IF_AT_LEAST_X_FILES_DOWNLOADED, Messages
							.getString("JobFTP.SuccessWhenAtLeat.Label")));
			optionsucc
					.add(new OptionDataMeta(
							job.SUCCESS_IF_ERRORS_LESS,
							Messages
									.getString("JobFTP.SuccessWhenNrErrorsLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id
					+ ".success_condition", "成功", null, null, null, job
					.getSuccessCondition(), null, optionsucc);
			this.success_condition.setSingle(true);
			this.success_condition.addListener("change",
					"jQuery.imeta.jobEntries.ftp.listeners.ftpChange");
			// Limit files
			this.nr_limit = new LabelInputMeta(id + ".nr_limit", "限制文件", null,
					null, "限制文件必须填写", String.valueOf(job.getLimit()), null,
					ValidateForm.getInstance().setRequired(true));
			this.nr_limit.setSingle(true);
			this.nr_limit.setDisabled(job.getSuccessCondition().equals("0"));
			this.successCondition.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_limit });

			this.meta.putTabContent(2,
					new BaseFormMeta[] { this.successCondition });

			/*************************************************
			 * Proxy
			 ****************************************************/
			this.proxy = new ColumnFieldsetMeta(null, "代理");
			this.proxy.setSingle(true);

			this.socksProxyHost = new LabelInputMeta(id + ".socksProxyHost",
					"主机IP/名称", null, null, "主机IP/名称必须填写", String.valueOf(job
							.getSocksProxyHost()), null, ValidateForm
							.getInstance().setRequired(true));
			this.socksProxyHost.setSingle(true);
			this.socksProxyPort = new LabelInputMeta(id + ".socksProxyPort",
					"端口", null, null, "端口必须填写", String.valueOf(job
							.getSocksProxyPort()), null, ValidateForm
							.getInstance().setRequired(true));
			this.socksProxyPort.setSingle(true);
			this.socksProxyUsername = new LabelInputMeta(id
					+ ".socksProxyUsername", "用户名称", null, null, "用户名称必须填写",
					String.valueOf(job.getSocksProxyUsername()), null,
					ValidateForm.getInstance().setRequired(true));
			this.socksProxyUsername.setSingle(true);
			this.socksProxyPassword = new LabelInputMeta(id
					+ ".socksProxyPassword", "用户密码", null, null, "用户密码必须填写",
					String.valueOf(job.getSocksProxyPassword()), null,
					ValidateForm.getInstance().setRequired(true));
			this.socksProxyPassword.setSingle(true);

			this.proxy.putFieldsetsContent(new BaseFormMeta[] {
					this.socksProxyHost, this.socksProxyPort,
					this.socksProxyUsername, this.socksProxyPassword });

			this.meta.putTabContent(3, new BaseFormMeta[] { this.proxy });

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
