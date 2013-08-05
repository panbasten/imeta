package com.panet.imeta.job.entries.ssh2put;

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

public class JobEntrySSH2PUTDialog extends JobEntryDialog implements
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
	 * host
	 */
	private ColumnFieldsetMeta host;
	
	/**
	 * FTP 服务器名称/IP地址
	 */
	private LabelInputMeta serverName;
	
	/**
	 * Port
	 */
	private LabelInputMeta serverPort;
	
	/**
	 * Cache host key
	 */
	private LabelInputMeta cachehostkey;
	
	/**
	 * 用户名
	 */
	private LabelInputMeta userName;
	
	/**
	 * 密码
	 */
	private LabelInputMeta password;
	
	/**
	 * Timeout
	 */
	private LabelInputMeta timeout;
	
	/**
	 * Test connection
	 */
	
//	private ButtonMeta testConnection;
	
	/**
	 * HTTP Proxy
	 */
	private ColumnFieldsetMeta httpProxy;
	
	/**
	 * Use HTTP Proxy
	 */
	private LabelInputMeta usehttpproxy;
	
	/**
	 * Http Proxy host
	 */
	private LabelInputMeta httpproxyhost;
	
	/**
	 * Http Proxy port
	 */
	private LabelInputMeta httpproxyport;
	
	/**
	 * Use Basic authentication
	 */
	private LabelInputMeta useBasicAuthentication;
	
	/**
	 * 代理服务器用户名
	 */
	private LabelInputMeta httpproxyusername;
	
	/**
	 * 代理服务器密码
	 */
	private LabelInputMeta httpProxyPassword;
	
	/**
	 * Public key
	 */
	private ColumnFieldsetMeta publicKey;
	
	/**
	 * User Public Key
	 */
	private LabelInputMeta publicpublickey;
	
	/**
	 * Key file
	 */
    private LabelInputMeta keyFilename;
	
//	private ButtonMeta browse;
	
	/**
	 * Key passPhrase
	 */
	private LabelInputMeta keyFilePass;
	
	/*******************************************
	 * Files
	 *******************************************/
	
	/**
	 * Files
	 */
	private ColumnFieldsetMeta files;
	
	/**
	 * Source directory
	 */
    private LabelInputMeta ftpDirectory;
	
//	private ButtonMeta foldera;
    
    /**
	 * Wildcard
	 */
    private LabelInputMeta wildcard;
    
    /**
	 * Target directory
	 */
    private LabelInputMeta localDirectory;
	
//	private ButtonMeta checkFolder;
    
	/**
	 * Create remote directory
	 */
    private LabelInputMeta createRemoteFolder;
    
    /**
	 * Don't overwrite files
	 */
    private LabelInputMeta onlyGettingNewFiles;
    
	/**
	 * After FTP Put
	 */
	private LabelSelectMeta afterFtpPut;
	
	/**
	 * Destination folder
	 */
	private LabelInputMeta destinationfolder;
	
//	private ButtonMeta folderb;
	
	/**
	 * Create destination folder
	 */
	private LabelInputMeta createDestinationFolder;
	

	public JobEntrySSH2PUTDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntrySSH2PUT job = (JobEntrySSH2PUT)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "工程项目名称", null, null,
					"工程项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "文件" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * General
			 ******************************************************************/
			
			// host
			this.host = new ColumnFieldsetMeta(null, "主机");
			this.host.setSingle(true);
			
			// FTP服务器名称/IP地址：
			this.serverName = new LabelInputMeta(id + ".serverName", "FTP服务器名称/IP地址：", null, null,
					"FTP服务器名称/IP地址必须填写", job.getServerName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverName.setSingle(true);
			
			// Port：
			this.serverPort = new LabelInputMeta(id + ".serverPort", "端口：", null, null,
					"端口必须填写", String.valueOf(job.getServerPort()), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverPort.setSingle(true);
			
			// Cache host key
			this.cachehostkey = new LabelInputMeta(id + ".cachehostkey", "缓存主机密码", null,
					null, null, String.valueOf(job.isCacheHostKey()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.cachehostkey.setSingle(true);
			
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
			
			// Timeout
			this.timeout = new LabelInputMeta(id + ".timeout", "超时：", null, null,
					"超时必须填写", String.valueOf(job.getTimeout()), null, ValidateForm
					.getInstance().setRequired(true));
			this.timeout.setSingle(true);
			Const.NVL("aa","");
			
			//Test connection
//			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
//			this.testConnection = new ButtonMeta(id + ".btn.testConnection", id
//				      + ".btn.testConnection", "测试连接", "测试连接");
//			this.testConnection.appendTo(div);
			 
			this.host.putFieldsetsContent(new BaseFormMeta[] {
					this.serverName, this.serverPort, this.cachehostkey, this.userName,
	                this.password, this.timeout,
//	                div
			});
			
			// HTTP Proxy
			this.httpProxy = new ColumnFieldsetMeta(null, "HTTP代理服务器");
			this.httpProxy.setSingle(true);
			
			// Use HTTP Proxy
			this.usehttpproxy = new LabelInputMeta(id + ".usehttpproxy", "使用HTTP代理服务器", null,
					null, null, String.valueOf(job.isUseHTTPProxy()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.usehttpproxy.setSingle(true);
			this.usehttpproxy.addClick("jQuery.imeta.jobEntries.ssh2put.listeners.usehttpproxy");
			
			// Http Proxy host
			this.httpproxyhost = new LabelInputMeta(id + ".httpproxyhost", "HTTP代理服务器主机", null, null,
					"HTTP代理服务器主机必须填写", job.getHTTPProxyHost(), null, ValidateForm
					.getInstance().setRequired(true));
			this.httpproxyhost.setSingle(true);
			this.httpproxyhost.setDisabled(!job.isUseHTTPProxy());
			
			// Http Proxy port
			this.httpproxyport = new LabelInputMeta(id + ".httpproxyport", "HTTP代理服务器端口", null, null,
					"HTTP代理服务器端口必须填写", job.getHTTPProxyPort(), null, ValidateForm
					.getInstance().setRequired(true));
			this.httpproxyport.setSingle(true);
			this.httpproxyport.setDisabled(!job.isUseHTTPProxy());
			
			// Use Basic authentication
			this.useBasicAuthentication = new LabelInputMeta(id + ".useBasicAuthentication", "使用基本身份验证", null,
					null, null, String.valueOf(job.isUseBasicAuthentication()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.useBasicAuthentication.setSingle(true);
			this.useBasicAuthentication.setDisabled(!job.isUseHTTPProxy());
			this.useBasicAuthentication.addClick("jQuery.imeta.jobEntries.ssh2put.listeners.useBasicAuthentication");
			
			// 代理服务器用户名：
			this.httpproxyusername = new LabelInputMeta(id + ".httpproxyusername", "代理服务器用户名：", null, null,
					"代理服务器用户名必须填写", job.getHTTPProxyUsername(), null, ValidateForm
					.getInstance().setRequired(true));
			this.httpproxyusername.setSingle(true);
			this.httpproxyusername.setDisabled(!job.isUseHTTPProxy());
			this.httpproxyusername.setDisabled(!job.isUseBasicAuthentication());
			
			// 代理服务器密码：
			this.httpProxyPassword = new LabelInputMeta(id + ".httpProxyPassword", "代理服务器密码：", null, null,
					"代理服务器密码必须填写", job.getHTTPProxyPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.httpProxyPassword.setSingle(true);
			this.httpProxyPassword.setDisabled(!job.isUseHTTPProxy());
			this.httpproxyusername.setDisabled(!job.isUseBasicAuthentication());
			
			this.httpProxy.putFieldsetsContent(new BaseFormMeta[] {
					this.usehttpproxy, this.httpproxyhost, this.httpproxyport,
	                this.useBasicAuthentication, this.httpproxyusername,
	                this.httpProxyPassword
			});
	            
			// Public key
			this.publicKey = new ColumnFieldsetMeta(null, "公共密码");
			this.publicKey.setSingle(true);
			
			// Use Public Key
			this.publicpublickey = new LabelInputMeta(id + ".publicpublickey", "使用公共密码", null,
					null, null, String.valueOf(job.isUsePublicKey()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.publicpublickey.setSingle(true);
			this.publicpublickey.addClick("jQuery.imeta.jobEntries.ssh2put.listeners.publicpublickey");
			
			// Key file
			this.keyFilename = new LabelInputMeta(id + ".keyFilename", "密码文件：", null, null,
					"密码文件必须填写", job.getKeyFilename(), null, ValidateForm
					.getInstance().setRequired(true));
//			this.browse = new ButtonMeta(id + ".btn.browse", id + ".btn.browse",
//					"浏览", "浏览");
//
//			this.keyFilename.addButton(new ButtonMeta[] { this.browse });
			
			this.keyFilename.setSingle(true);
			this.keyFilename.setDisabled(!job.isUsePublicKey());
			
			// Key passphrase
			this.keyFilePass = new LabelInputMeta(id + ".keyFilePass", "关键密码", null, null,
					"关键密码必须填写", job.getKeyFilepass(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.keyFilePass.setSingle(true);
			this.keyFilePass.setDisabled(!job.isUsePublicKey());
			
			this.publicKey.putFieldsetsContent(new BaseFormMeta[] {
					this.publicpublickey, this.keyFilename, this.keyFilePass
			});
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.host, this.httpProxy, this.publicKey });
			
			/*********************************************************************
			 * Files
			 *********************************************************************/
			// Files
			this.files = new ColumnFieldsetMeta(null, "文件");
			this.files.setSingle(true);
			
			// Source directory
			this.ftpDirectory = new LabelInputMeta(id + ".ftpDirectory", "源代码目录：", null, null,
					"源代码目录必须填写", job.getFtpDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.foldera = new ButtonMeta(id + ".btn.foldera", id + ".btn.foldera",
//					"文件夹", "文件夹");
//
//			this.ftpDirectory.addButton(new ButtonMeta[] { this.foldera });
//			
			this.ftpDirectory.setSingle(true);
			
			// Wildcard
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符（正则表达式）", null, null,
					"通配符必须填写", job.getWildcard(), null, ValidateForm
					.getInstance().setRequired(true));
			this.wildcard.setSingle(true);
			
			// Target directory
			this.localDirectory = new LabelInputMeta(id + ".localDirectory", "目标目录：", null, null,
					"目标目录必须填写", job.getlocalDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.checkFolder = new ButtonMeta(id + ".btn.checkFolder", id + ".btn.checkFolder",
//					"检查文件夹", "检查文件夹");
//
//			this.localDirectory.addButton(new ButtonMeta[] { this.checkFolder });
//			
			this.localDirectory.setSingle(true);
			
			// Create remote directory
			this.createRemoteFolder = new LabelInputMeta(id + ".createRemoteFolder", "创建远程目录", null,
					null, null, String.valueOf(job.isCreateRemoteFolder()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.createRemoteFolder.setSingle(true);
			
			// Don't overwrite files
			this.onlyGettingNewFiles = new LabelInputMeta(id + ".onlyGettingNewFiles", "不覆盖文件：", null,
					null, null, String.valueOf(job.isOnlyGettingNewFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.onlyGettingNewFiles.setSingle(true);
			
			// After FTP Put
			List<OptionDataMeta> optionAfter = new ArrayList<OptionDataMeta>();
			optionAfter.add(new OptionDataMeta("0", Messages.getString("JobSSH2PUT.Do_Nothing_AfterFTPPut.Label")));
			optionAfter.add(new OptionDataMeta("1", Messages.getString("JobSSH2PUT.Delete_Files_AfterFTPPut.Label")));
			optionAfter.add(new OptionDataMeta("2", Messages.getString("JobSSH2PUT.Move_Files_AfterFTPPut.Label")));
			
			this.afterFtpPut= new LabelSelectMeta(id + ".afterFtpPut","FTP发送后",
					null,null,null,job.getAfterFTPPut(),null,optionAfter);	
			this.afterFtpPut.setSingle(true);
			this.afterFtpPut.addListener("change", 
			"jQuery.imeta.jobEntries.ssh2put.listeners.ssh2putChange");
			
			// Destination folder
			this.destinationfolder = new LabelInputMeta(id + ".destinationfolder", "目标文件夹", null, null,
					"目标文件夹必须填写", job.getDestinationFolder(), null, ValidateForm
					.getInstance().setRequired(true));
//			this.folderb = new ButtonMeta(id + ".btn.folderb", id + ".btn.folderb",
//					"文件夹", "文件夹");
//			this.destinationfolder.addButton(new ButtonMeta[] { this.folderb });
			
			this.destinationfolder.setSingle(true);
			this.destinationfolder.setDisabled(!job.getAfterFTPPut().equals("2"));
			// Create destination folder
			this.createDestinationFolder = new LabelInputMeta(id + ".createDestinationFolder", "创建目标文件夹", null,
					null, null, String.valueOf(job.isCreateDestinationFolder()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.createDestinationFolder.setSingle(true);
			this.createDestinationFolder.setDisabled(!job.getAfterFTPPut().equals("2"));
			this.files.putFieldsetsContent(new BaseFormMeta[] {
					this.ftpDirectory,
					this.wildcard, this.localDirectory, 
					this.createRemoteFolder, this.onlyGettingNewFiles,
					this.afterFtpPut, this.destinationfolder,
					this.createDestinationFolder
					});
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.files });
			

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
