package com.panet.imeta.job.entries.ftpput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class JobEntryFTPPUTDialog extends JobEntryDialog implements
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
	 *  General
	 ***************************************/
	
	/**
	 * Server settings
	 */
	private ColumnFieldsetMeta server;
	
	/**
	 * FTP 服务器名称/IP地址
	 */
	private LabelInputMeta serverName;
	
	/**
	 * Server port
	 */
	private LabelInputMeta serverPort;
	
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
	
//	private ButtonMeta testConnection;
	
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
	 * Source (local) files
	 */
	private ColumnFieldsetMeta sourceFile;
	
	/**
	 * 本地目标
	 */
	private LabelInputMeta localDirectory;
	
//	private ButtonMeta browseButton;
	
	/**
	 * 通配符
	 */
	private LabelInputMeta wildcard;
	
	/**
	 * Remove files after transferal
	 */
	private LabelInputMeta remove;
	
	/**
	 * 不覆盖文件
	 */
	private LabelInputMeta  onlyPuttingNewFiles;
	
	/**
	 * target files
	 */
	private ColumnFieldsetMeta targetFiles;
	
	/**
	 * Remote directory
	 */
	private LabelInputMeta remoteDirectory;
	
//	private ButtonMeta testFolder;

	public JobEntryFTPPUTDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryFTPPUT job = (JobEntryFTPPUT)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称：", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "文件" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * General
			 ******************************************************************/
			
			// Server
			this.server = new ColumnFieldsetMeta(null, "服务器设置");
			this.server.setSingle(true);
			
			// FTP服务器名称/IP地址：
			this.serverName = new LabelInputMeta(id + ".serverName", "FTP服务器名称/IP地址：", null, null,
					"FTP服务器名称/IP地址必须填写", job.getServerName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverName.setSingle(true);
			
			// Server port：
			this.serverPort = new LabelInputMeta(id + ".serverPort", "端口：", null, null,
					"端口必须填写", String.valueOf(job.getServerPort()), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverPort.setSingle(true);
			
			// 用户名：
			this.userName = new LabelInputMeta(id + ".userName", "用户名：", null, null,
					"用户名必须填写", job.getUserName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.userName.setSingle(true);
			
			// 密码：
			this.password = new LabelInputMeta(id + ".password", "密码：", null, null,
					"密码必须填写", job.getPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.password.setSingle(true);
			
			// 代理服务器
			this.proxyHost = new LabelInputMeta(id + ".proxyHost", "代理服务器：", null, null,
					"代理服务器必须填写", job.getProxyHost(), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyHost.setSingle(true);
			
			// 代理服务器端口：
			this.proxyPort = new LabelInputMeta(id + ".proxyPort", "代理服务器端口：", null, null,
					"代理服务器端口必须填写", job.getProxyPort(), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyPort.setSingle(true);
			
			// 代理服务器用户名：
			this.proxyUsername = new LabelInputMeta(id + ".proxyUsername", "代理服务器用户名：", null, null,
					"代理服务器用户名必须填写", job.getProxyUsername(), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyUsername.setSingle(true);
			
			// 代理服务器密码：
			this.proxyPassword = new LabelInputMeta(id + ".proxyPassword", "代理服务器密码：", null, null,
					"代理服务器密码必须填写", job.getProxyPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.proxyPassword.setSingle(true);
			
//			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
//			this.testConnection = new ButtonMeta(id + ".btn.testConnection", id
//				      + ".btn.testConnection", "测试连接", "测试连接");
//			this.testConnection.appendTo(div);
			
			this.server.putFieldsetsContent(new BaseFormMeta[] {
					this.serverName, this.serverPort, this.userName,
	                this.password, this.proxyHost, this.proxyPort,
	                this.proxyUsername, this.proxyPassword,
//	                div
			});
			
			// Advanced
			this.advanced = new ColumnFieldsetMeta(null, "高级设置");
			this.advanced.setSingle(true);
			
			// 二进制模式
			this.binaryMode = new LabelInputMeta(id + ".binaryMode", "二进制模式?", null,
					null, null, String.valueOf(job.isBinaryMode()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.binaryMode.setSingle(true);
			
			// 超时：
			this.timeout = new LabelInputMeta(id + ".timeout", "超时：", null, null,
					"超时必须填写", String.valueOf(job.getTimeout()), null, ValidateForm
					.getInstance().setRequired(true));
			this.timeout.setSingle(true);
			
			// 使用活动的FTP连接
			this.activeConnection = new LabelInputMeta(id + ".activeConnection", "使用活动的FTP连接?", null,
					null, null, String.valueOf(job.isActiveConnection()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.activeConnection.setSingle(true);
			
			// 控制编码
			this.controlEncoding= new LabelSelectMeta(id + ".controlEncoding","控制编码：",
					null,null,null,job.getControlEncoding(),null,super.getEncoding());	
			this.controlEncoding.setSingle(true);
			
			this.advanced.putFieldsetsContent(new BaseFormMeta[] {
					this.binaryMode, this.timeout, this.activeConnection,
	                this.controlEncoding
			});
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.server, this.advanced });
			
			/*********************************************************************
			 * Files
			 *********************************************************************/
			// Source files
			this.sourceFile = new ColumnFieldsetMeta(null, "来源（本地）文件");
			this.sourceFile.setSingle(true);
			
			// 本地目录：
			this.localDirectory = new LabelInputMeta(id + ".localDirectory", "本地目录：", null, null,
					"本地目录必须填写", job.getLocalDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.browseButton = new ButtonMeta(id + ".btn.browseButton", id + ".btn.browseButton",
//					"浏览", "浏览");
//
//			this.localDirectory.addButton(new ButtonMeta[] { this.browseButton });
			
			this.localDirectory.setSingle(true);
			
			// 通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符（正则表达式）", null, null,
					"通配符必须填写", job.getWildcard(), null, ValidateForm
					.getInstance().setRequired(true));
			this.wildcard.setSingle(true);
			
			// 传输完成后删除文件
			this.remove = new LabelInputMeta(id + ".remove", "传输完成后删除文件？", null,
					null, null, String.valueOf(job.getRemove()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.remove.setSingle(true);
			
			// 不覆盖文件
			this.onlyPuttingNewFiles = new LabelInputMeta(id + ".onlyPuttingNewFiles", "不覆盖文件：", null,
					null, null, String.valueOf(job.isOnlyPuttingNewFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.onlyPuttingNewFiles.setSingle(true);
			
			this.sourceFile.putFieldsetsContent(new BaseFormMeta[] {
					this.localDirectory, this.wildcard,
					this.remove, this.onlyPuttingNewFiles
					});
			
			// Target files
			this.targetFiles = new ColumnFieldsetMeta(null, "目标（远程）文件");
			this.targetFiles.setSingle(true);
			
			// Remote directory
			this.remoteDirectory = new LabelInputMeta(id + ".remoteDirectory", "远程目录：", null, null,
					"远程目录必须填写", job.getRemoteDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
//			this.testFolder = new ButtonMeta(id + ".btn.testFolder", id + ".btn.testFolder",
//					"测试文件夹", "测试文件夹");
//
//			this.remoteDirectory.addButton(new ButtonMeta[] { this.testFolder });
			
			this.remoteDirectory.setSingle(true);
			
			this.targetFiles.putFieldsetsContent(new BaseFormMeta[] {
					this.remoteDirectory
					});
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.sourceFile, this.targetFiles });
			
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.meta});

	        columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {super.getOkBtn(), super.getCancelBtn() });

	        cArr.add(columnFormMeta.getFormJo());

	        rtn.put("items", cArr);
	        rtn.put("title", super.getJobMeta().getName());

	        return rtn;
          } catch (Exception ex) {
	   throw new ImetaException(ex.getMessage(), ex);
      }
   }
}
