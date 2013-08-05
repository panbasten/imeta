package com.panet.imeta.job.entries.sftp;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntrySFTPDialog extends JobEntryDialog implements
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
	 * Server settings
	 */
	private ColumnFieldsetMeta serverSettings;
	
	/**
	 * SFTP server name/IP
	 */
	private LabelInputMeta serverName;
	
	/**
	 * Port
	 */
	private LabelInputMeta serverPort;
	
	/**
	 * User name
	 */
	private LabelInputMeta userName;
	
	/**
	 * password
	 */
	private LabelInputMeta password;
	
	/**
	 * Test connection
	 */
//	private ButtonMeta testConnection;
	
	/**
	 * Source files
	 */
	private ColumnFieldsetMeta sourceFiles ;
	
	/**
	 * Copy previous results to args
	 */
	private LabelInputMeta copyprevious;
	
	/**
	 * Remote directory
	 */
	private LabelInputMeta sftpDirectory;
	
//	private ButtonMeta testFolder;
	
	/**
	 * Wildcard
	 */
	private LabelInputMeta wildcard;
	
	/**
	 * Remove files after retrieval
	 */
	private LabelInputMeta remove;
	
	/**
	 * Target files
	 */
	private ColumnFieldsetMeta targetFiles;
	
	/**
	 * Target directory
	 */
	private LabelInputMeta targetDirectory;
	
//	private ButtonMeta browse;
	
	/**
	 * Create target folder
	 */
	private LabelInputMeta createtargetfolder;
	
	/**
	 * Add filename to result
	 */
	private LabelInputMeta isaddresult;
	
	/**
	 * 确定按钮
	 */
//	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
//	private ButtonMeta cancelBtn;

	public JobEntrySFTPDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntrySFTP job = (JobEntrySFTP)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 作业名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// Server settings
			this.serverSettings = new ColumnFieldsetMeta(null, "服务器设置");
			this.serverSettings.setSingle(true);
			
			// SETP server name/IP
			this.serverName = new LabelInputMeta(id + ".serverName", "SFTP服务器名称/IP", null, null,
					"SFTP服务器名称/IP必须填写", job.getServerName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverName.setSingle(true);

			// Port
			this.serverPort = new LabelInputMeta(id + ".serverPort", "端口", null, null,
					"端口必须填写", String.valueOf(job.getServerPort()), null, ValidateForm
					.getInstance().setRequired(true));
			this.serverPort.setSingle(true);
			
			// User name
			this.userName = new LabelInputMeta(id + ".userName", "用户名：", null, null,
					"用户名必须填写", job.getUserName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.userName.setSingle(true);
			
			// Password
			this.password = new LabelInputMeta(id + ".password", "密码：", null, null,
					"密码必须填写", job.getPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.password.setSingle(true);
			
//			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
//			this.testConnection = new ButtonMeta(id + ".btn.testConnection", id + ".btn.testConection",
//					"测试连接", "测试连接");
//			this.testConnection.appendTo(div);
			
			this.serverSettings.putFieldsetsContent(new BaseFormMeta[] {
					this.serverName, this.serverPort,
					this.userName, this.password,//div
					
					});
		
			// Source files
			this.sourceFiles = new ColumnFieldsetMeta(null, "源文件");
			this.sourceFiles.setSingle(true);
			
			// Copy previous results to args
			this.copyprevious = new LabelInputMeta(id + ".copyprevious", "复制以前的结果到args？", null,
					null, null, String.valueOf(job.isCopyPrevious()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.copyprevious.setSingle(true);
			this.copyprevious.addClick("jQuery.imeta.jobEntries.sftp.listeners.copyprevious");
			
			// Remote directory
			this.sftpDirectory = new LabelInputMeta(id + ".sftpDirectory", "远程目录：", null, null,
					"远程目录必须填写", job.getScpDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.testFolder = new ButtonMeta(id + ".btn.testFolder", id + ".btn.testFolder",
//					"测试文件夹", "测试文件夹");
//
//			this.sftpDirectory.addButton(new ButtonMeta[] { this.testFolder });
			
			this.sftpDirectory.setSingle(true);
			
			// Wildcard
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符（正则表达式）：", null, null,
					"通配符必须填写", job.getWildcard(), null, ValidateForm
					.getInstance().setRequired(true));
			this.wildcard.setSingle(true);
			this.wildcard.setDisabled(!job.isCopyPrevious());
			
			// Remove files after transferral
			this.remove = new LabelInputMeta(id + ".remove", "删除文件后移交？", null,
					null, null, String.valueOf(job.getRemove()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.remove.setSingle(true);
			
			this.sourceFiles.putFieldsetsContent(new BaseFormMeta[] {
					this.copyprevious, this.sftpDirectory,
					this.wildcard, this.remove
					
					});
		
			// Target files
			this.targetFiles = new ColumnFieldsetMeta(null, "目标文件");
			this.targetFiles.setSingle(true);
			
			// Target directory
			this.targetDirectory = new LabelInputMeta(id + ".targetDirectory", "目标目录：", null, null,
					"目标目录必须填写", job.getTargetDirectory(), null, ValidateForm
					.getInstance().setRequired(true));
			
//			this.browse = new ButtonMeta(id + ".btn.browse", id + ".btn.browse",
//					"浏览", "浏览");
//
//			this.targetDirectory.addButton(new ButtonMeta[] { this.browse });
			
			this.targetDirectory.setSingle(true);
			
			// Create target folder
			this.createtargetfolder = new LabelInputMeta(id + ".createtargetfolder", "创建目标文件夹", null,
					null, null, String.valueOf(job.iscreateTargetFolder()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.createtargetfolder.setSingle(true);
			
			// Add filename to result
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "在结果增加文件名？", null,
					null, null, String.valueOf(job.isAddToResult()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.isaddresult.setSingle(true);
			
			this.targetFiles.putFieldsetsContent(new BaseFormMeta[] {
					this.targetDirectory, this.createtargetfolder,
					this.isaddresult
					});
			
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.serverSettings, this.sourceFiles,
					this.targetFiles
					});

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
