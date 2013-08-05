package com.panet.imeta.job.entries.ftpdelete;

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

public class JobEntryFTPDeleteDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * Name of this job entry
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
	 * Server
	 */
	private ColumnFieldsetMeta server;
	
	/**
	 * Protocol
	 */
	private LabelSelectMeta protocol;
	
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
	 * use proxy
	 */
	private LabelInputMeta useproxy;
	
	/**
	 * Proxy host
	 */
	private LabelInputMeta proxyHost;
	
	/**
	 * Proxy port
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
	
	/**
	 * Use Public Key
	 */
	private LabelInputMeta publicpublickey;
	
	/**
	 * Key file
	 */
	private LabelInputMeta keyFilename;
	
//	private ButtonMeta browse;
	
	/**
	 * Key passphrase
	 */
	private LabelInputMeta keyFilePass;
	
//	private ButtonMeta testConnection;
	
	/********************************
	 * File
	 ********************************/
	
	/**
	 * Advanced
	 */
	private ColumnFieldsetMeta advanced;
	
	/**
	 * Timeout
	 */
	private LabelInputMeta timeout;
	
	/**
	 * Use active FTP connection
	 */
	private LabelInputMeta activeConnection;
	
	/**
	 * Remote
	 */
	private ColumnFieldsetMeta remote;
	
	/**
	 * Copy previous results to args
	 */
	private LabelInputMeta copyprevious;
	
	/**
	 * Remote directory
	 */
	private LabelInputMeta ftpDirectory;
	
//	private ButtonMeta checkFolder;
	
	/**
	 * Wildcard
	 */
	private LabelInputMeta wildcard;
	
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
	private LabelInputMeta nr_limit_success;

	public JobEntryFTPDeleteDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryFTPDelete job = (JobEntryFTPDelete)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "文件"});
			this.meta.setSingle(true);
			/*******************************************************************
			 * General
			 ******************************************************************/
			
			// Server
			this.server = new ColumnFieldsetMeta(null, "服务器");
			this.server.setSingle(true);
			
			// Protocol
			List<OptionDataMeta> optionsa = new ArrayList<OptionDataMeta>();
			optionsa.add(new OptionDataMeta("0", "FTP"));
			optionsa.add(new OptionDataMeta("1", "SFTP"));
			optionsa.add(new OptionDataMeta("2", "SSH"));
			this.protocol= new LabelSelectMeta(id + ".protocol","议定书",
					null,null,null,job.getProtocol(),null,optionsa);	
			this.protocol.addListener("change", 
			"jQuery.imeta.jobEntries.ftpdelete.listeners.protocolChange");
			this.protocol.setSingle(true);
		
			// FTP服务器名称/IP地址：
			this.serverName = new LabelInputMeta(id + ".serverName", "FTP服务器名称/IP地址：", null, null,
					"服务器名称名称/IP地址必须填写", job.getServerName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverName.setSingle(true);
			
			// Server port：
			this.port = new LabelInputMeta(id + ".port", "服务器端口：", null, null,
					"服务器端口必须填写", String.valueOf(job.getPort()), null, ValidateForm
					.getInstance().setRequired(true));
			this.port.setSingle(true);
			
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
			
			// Use proxy
			this.useproxy = new LabelInputMeta(id + ".useproxy", "使用代理服务器", null, null,
					null, String.valueOf(job.isUseProxy()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
			this.useproxy.setSingle(true);
			this.useproxy.addClick("jQuery.imeta.jobEntries.ftpdelete.listeners.useproxy");
			
			// Proxy host：
			this.proxyHost = new LabelInputMeta(id + ".proxyHost", "代理服务器主机：", null, null,
					"代理服务器主机必须填写", job.getProxyHost(), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyHost.setSingle(true);
			this.proxyHost.setDisabled(!job.isUseProxy());
			
			// Proxy port：
			this.proxyPort = new LabelInputMeta(id + ".proxyPort", "代理服务器端口：", null, null,
					"代理服务器端口必须填写", String.valueOf(job.getPort()), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyPort.setSingle(true);
			this.proxyPort.setDisabled(!job.isUseProxy());
			
			// 代理服务器用户名：
			this.proxyUsername = new LabelInputMeta(id + ".proxyUsername", "代理服务器用户名：", null, null,
					"代理服务器用户名必须填写", job.getProxyUsername(), null, ValidateForm
					.getInstance().setRequired(true));
			this.proxyUsername.setSingle(true);
			this.proxyUsername.setDisabled(!job.isUseProxy());
			
			// 代理服务器密码：
			this.proxyPassword = new LabelInputMeta(id + ".proxyPassword", "代理服务器密码：", null, null,
					"代理服务器密码必须填写", job.getProxyPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.proxyPassword.setSingle(true);
			this.proxyPassword.setDisabled(!job.isUseProxy());
			
			// Use Public Key
			this.publicpublickey = new LabelInputMeta(id + ".publicpublickey", "使用公共密码", null,
					null, null, String.valueOf(job.isUsePublicKey()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.publicpublickey.setSingle(true);
			this.publicpublickey.addClick("jQuery.imeta.jobEntries.ftpdelete.listeners.publicpublickey");
			this.publicpublickey.setDisabled(job.getProtocol().equals("2"));
			
			// Key file：
			this.keyFilename = new LabelInputMeta(id + ".keyFilename", "密码文件", null, null,
					"密码文件必须填写", job.getKeyFilename(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.browse = new ButtonMeta(id + ".btn.browse", id
//				      + ".btn.browse", "浏览", "浏览");
//			 
//			this.keyFilename.addButton(new ButtonMeta[] { this.browse });
			
			this.keyFilename.setSingle(true);
			this.keyFilename.setDisabled(job.getProtocol().equals("2"));
			this.keyFilename.setDisabled(!job.isUsePublicKey());
			
			// Key passphrase：
			this.keyFilePass = new LabelInputMeta(id + ".keyFilePass", "关键密码：", null, null,
					"关键密码必须填写", job.getKeyFilePass(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.keyFilePass.setSingle(true);
			this.keyFilePass.setDisabled(job.getProtocol().equals("2"));
			this.keyFilePass.setDisabled(!job.isUsePublicKey());
			
//			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
//			this.testConnection = new ButtonMeta(id + ".btn.testConnection", id
//				      + ".btn.testConnection", "测试连接", "测试连接");
//			this.testConnection.appendTo(div); 	
			this.server.putFieldsetsContent(new BaseFormMeta[] {
					this.protocol,
					this.serverName, this.port, this.userName,
	                this.password, this.useproxy, this.proxyHost,
	                this.proxyPort,
	                this.proxyUsername, this.proxyPassword,
	                this.publicpublickey, this.keyFilename, this.keyFilePass,
//	                div
			});
			

			this.meta.putTabContent(0, new BaseFormMeta[] { this.server });
			
			/*********************************************************************
			 * Files
			 *********************************************************************/
			
			// Advanced
			this.advanced = new ColumnFieldsetMeta(null, "高级");
			this.advanced.setSingle(true);
			
			// Timeout
			this.timeout = new LabelInputMeta(id + ".timeout", "超时：", null, null,
					"Timeout必须填写", String.valueOf(job.getTimeout()), null, ValidateForm
					.getInstance().setRequired(true));
			this.timeout.setSingle(true);

			// Use active FTP connection
			this.activeConnection = new LabelInputMeta(id + ".activeConnection", "使用主动的FTP连接：", null,
					null, null, String.valueOf(job.isActiveConnection()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.activeConnection.setSingle(true);
			
			this.advanced.putFieldsetsContent(new BaseFormMeta[] {
					this.timeout, this.activeConnection
			});
			
			// Remote
			this.remote = new ColumnFieldsetMeta(null, "远程");
			this.remote.setSingle(true);
			
			// Copy previous results to args
			this.copyprevious = new LabelInputMeta(id + ".copyprevious", "复制最初结果到args？", null,
					null, null, String.valueOf(job.isCopyPrevious()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.copyprevious.setSingle(true);
			this.copyprevious.addClick("jQuery.imeta.jobEntries.ftpdelete.listeners.copyprevious");

			
			// Remote directory
			this.ftpDirectory = new LabelInputMeta(id + ".ftpDirectory", "远程目录：", null, null,
					"远程目录必须填写", job.getFtpDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.checkFolder = new ButtonMeta(id + ".btn.checkFolder", id + ".btn.checkFolder",
//					"检查文件夹", "检查文件夹");
//
//			this.ftpDirectory.addButton(new ButtonMeta[] { this.checkFolder });
			
			this.ftpDirectory.setSingle(true);
			this.ftpDirectory.setDisabled(!job.isCopyPrevious());
			
			// Wildcard
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符（正则表达式）", null, null,
					"通配符必须填写", job.getWildcard(), null, ValidateForm
					.getInstance().setRequired(true));
			this.wildcard.setSingle(true);
			this.wildcard.setDisabled(!job.isCopyPrevious());
			
			this.remote.putFieldsetsContent(new BaseFormMeta[] {
					this.copyprevious,
					this.ftpDirectory, this.wildcard,
					
					});
			
			// Success condition
			this.successCondition = new ColumnFieldsetMeta(null, "本地");
			this.successCondition.setSingle(true);
			
			// Success on
			List<OptionDataMeta> optionsDate = new ArrayList<OptionDataMeta>();
			optionsDate.add(new OptionDataMeta("0", Messages.getString("JobFTPDelete.SuccessWhenAllWorksFine.Label")));
			optionsDate.add(new OptionDataMeta("1", Messages.getString("JobFTPDelete.SuccessWhenAtLeat.Label")));
			optionsDate.add(new OptionDataMeta("2", Messages.getString("JobFTPDelete.SuccessWhenNrErrorsLessThan.Label")));
	
			this.success_condition= new LabelSelectMeta(id + ".success_condition","成功",
					null,null,null,job.getSuccessCondition(),null,optionsDate);	
			this.success_condition.addListener("change", 
			"jQuery.imeta.jobEntries.ftpdelete.listeners.ftpdeleteChange");
			this.success_condition.setSingle(true);

			// Limit files
			this.nr_limit_success = new LabelInputMeta(id + ".nr_limit_success", "限制文件", null, null,
					"限制文件必须填写", String.valueOf(job.getLimitSuccess()), null, ValidateForm
					.getInstance().setRequired(true));
			this.nr_limit_success.setSingle(true);
			this.nr_limit_success.setDisabled(job.getSuccessCondition().equals("0"));
			this.successCondition.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.nr_limit_success
					});
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.advanced, this.remote, this.successCondition });
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.meta});

	        columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] { super.getOkBtn(), super.getCancelBtn() });

	        cArr.add(columnFormMeta.getFormJo());

	        rtn.put("items", cArr);
	        rtn.put("title", super.getJobMeta().getName());

	        return rtn;
          } catch (Exception ex) {
	   throw new ImetaException(ex.getMessage(), ex);
      }
   }
}
