package com.panet.imeta.job.entries.connectedtorepository;

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

public class JobEntryConnectedToRepositoryDialog extends JobEntryDialog implements
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
	 * 指定资源库
	 */
	private LabelInputMeta isspecificrep;
	
	/**
	 * 资源库名称
	 */
	private LabelInputMeta repname;

//	/**
//	 * 资源库按钮
//	 */
//	private ButtonMeta dbBtn;
//	
	/**
	 * 指定用户
	 */
	private LabelInputMeta isspecificuser;
	
	/**
	 * 用户名
	 */
	private LabelInputMeta username;

	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryConnectedToRepositoryDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			JobEntryConnectedToRepository  jobEntry = (JobEntryConnectedToRepository) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
			
			//指定资源库
			  this.isspecificrep = new LabelInputMeta(id + ".isspecificrep", "指定资源库", null,
						null, null,String.valueOf(jobEntry.isSpecificRep()), InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
			  
			  this.isspecificrep.addClick("jQuery.imeta.jobEntries.connectedtorepository.listeners.isspecificrep");
			  this.isspecificrep.setSingle(true);
			  
			  
			// 资源库名称
				this.repname = new LabelInputMeta(id + ".repname", "资源库名称", null, null,
						"资源库名称", jobEntry.getRepName(), null, ValidateForm
								.getInstance().setRequired(false));
				 this.repname.setDisabled(!jobEntry.isSpecificRep());
				
                  this.repname.setSingle(true);
			
              //指定用户名
  			  this.isspecificuser = new LabelInputMeta(id + ".isspecificuser", "指定用户名", null,
  						null, null, String.valueOf(jobEntry.isSpecificUser()), InputDataMeta.INPUT_TYPE_CHECKBOX,
  						ValidateForm.getInstance().setRequired(false));
  			  this.isspecificuser.addClick("jQuery.imeta.jobEntries.connectedtorepository.listeners.isspecificuser");
  			  this.isspecificuser.setSingle(true);
  			  
  			// 用户名
				this.username = new LabelInputMeta(id + ".username", "用户名", null, null,
						"用户名", jobEntry.getUserName(), null, ValidateForm
								.getInstance().setRequired(false));
				this.username.setDisabled(!jobEntry.isSpecificUser());
				this.username.setSingle(true);
			
			
        	// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
					this.name,
					this.isspecificrep,
					this.repname,
					this.isspecificuser,
					this.username
					});

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
