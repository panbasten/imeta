package com.panet.imeta.job.entries.writetolog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryWriteToLogDialog extends JobEntryDialog implements
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
	 * log level
	 */
	private LabelSelectMeta loglevel;
	
	/**
	 * Log subject
	 */
	private LabelInputMeta logsubject;
	
	/**
	 * Log message
	 */
	private LabelTextareaMeta logmessage;
	
	public JobEntryWriteToLogDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			JobEntryWriteToLog job = (JobEntryWriteToLog)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Job entry name
			this.name = new LabelInputMeta(id + ".name", "工作项目名称:", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			//Log level
			this.loglevel = new LabelSelectMeta(id + ".loglevel","日志级别：",
					null,null,null,String.valueOf(job.loglevel),null,super.getLoglevel());
			this.loglevel.setSingle(true);
			
			// Log subject
			this.logsubject = new LabelInputMeta(id + ".logsubject", "日志主题：", null, null,
					"日志主题必须填写", String.valueOf(job.getLogSubject()), null, ValidateForm
							.getInstance().setRequired(true));
			this.logsubject.setSingle(true);
			
			// Log message
			this.logmessage = new LabelTextareaMeta(id + ".logmessage", "日志信息：", null, null,
					null, String.valueOf(job.getLogMessage()), 8, null);
			
			this.logmessage.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.loglevel,
					this.logsubject, this.logmessage
					});

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {super.getOkBtn(), super.getCancelBtn()
					});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
