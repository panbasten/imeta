package com.panet.imeta.job.entries.getpop;

import java.util.ArrayList;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryGetPOPDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * job entry name
	 */
	private LabelInputMeta name;
	
	/**
	 * Source Host
	 */
	private LabelInputMeta servername;
	
	/**
	 * Username
	 */
	private LabelInputMeta username;
	
	/**
	 * Password
	 */
	private LabelInputMeta password;
	
	/**
	 * Use POP with SSL
	 */
	private LabelInputMeta usessl;
	
	/**
	 * Port
	 */
	private LabelInputMeta sslport;
	
	/**
	 * Target directory
	 */
	private LabelInputMeta outputdirectory;
	
	/**
	 * Target filename pattern
	 */
	private LabelInputMeta filenamepattern;
	
	/**
	 * Retrieve
	 */
	private LabelSelectMeta retrievemails;
	
	/**
	 * Retrieve the ... first emails
	 */
	private LabelInputMeta firstmails;
	
	/**
	 * Delete emails after
	 */
	private LabelInputMeta delete;


	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryGetPOPDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryGetPOP job = (JobEntryGetPOP)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到job名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称", null, null,
					"job名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Source Host
			this.servername = new LabelInputMeta(id + ".servername", "源主机：", null, null,
					"源主机必须填写", job.getServerName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.servername.setSingle(true);
			
			// Username
			this.username = new LabelInputMeta(id + ".username", "用户名：", null, null,
					"用户名必须填写", job.getUserName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.username.setSingle(true);
			
			// Password
			this.password = new LabelInputMeta(id + ".password", "密码：", null, null,
					"密码必须填写", job.getPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(true));
			this.password.setSingle(true);
			
			// Use POP with SSL
			this.usessl = new LabelInputMeta(id + ".usessl", "在SSL层下使用POP协议", null,
					null, null, String.valueOf(job.getUseSSL()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.usessl.setSingle(true);
			this.usessl.addClick("jQuery.imeta.jobEntries.getpop.listeners.usessl");
			
			// Port
			this.sslport = new LabelInputMeta(id + ".sslport", "端口：", null, null,
					"端口必须填写", String.valueOf(job.getSSLPort()), null, ValidateForm
							.getInstance().setRequired(true));
			this.sslport.setSingle(true);
			this.sslport.setDisabled(!job.getUseSSL());
			
			// Target directory
			this.outputdirectory = new LabelInputMeta(id + ".outputdirectory", "目标目录：", null, null,
					"目标目录必须填写", job.getOutputDirectory(), null, ValidateForm
							.getInstance().setRequired(true));
			this.outputdirectory.setSingle(true);
			
			// Target filename pattern
			this.filenamepattern = new LabelInputMeta(id + ".filenamepattern", "目标文件格式：", null, null,
					"目标文件格式必须填写", job.getFilenamePattern(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filenamepattern.setSingle(true);
			
			// Retrieve
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("0", Messages.getString("JobGetPOP.RetrieveAllMails.Label")));
			options.add(new OptionDataMeta("1", Messages.getString("JobGetPOP.RetrieveUnreadMails.Label")));
			options.add(new OptionDataMeta("2", Messages.getString("JobGetPOP.RetrieveFirstMails.Label")));
			this.retrievemails = new LabelSelectMeta(id + ".retrievemails","检索：",
					null,null,null,String.valueOf(job.retrievemails),null,options);
			this.retrievemails.setSingle(true);
			this.retrievemails.addListener("change", 
			"jQuery.imeta.jobEntries.getpop.listeners.getpopChange");
            // Retrieve the...first emails
			this.firstmails = new LabelInputMeta(id + ".firstmails", "检索第...封电子邮件：", null,
					null, "检索电子邮件必须填写", job.getFirstMails(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.firstmails.setSingle(true);
//			this.firstmails.setDisabled(||);
			if(String.valueOf(job.retrievemails).equals("1")){
				this.firstmails.setDisabled(true);
			}else if(String.valueOf(job.retrievemails).equals("0")){
				this.firstmails.setDisabled(true);
			}
			// Delete emails after
			this.delete = new LabelInputMeta(id + ".delete", "删除电子邮件", null,
					null, null, String.valueOf(job.getDelete()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.delete.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.servername, this.username,
					this.password, this.usessl, this.sslport, this.outputdirectory, this.filenamepattern,
					this.retrievemails, this.firstmails, this.delete
					});

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
