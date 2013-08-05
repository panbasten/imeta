package com.panet.imeta.job.entries.msgboxinfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryMsgBoxInfoDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * MsgBox Info
	 */
	private LabelInputMeta name;
	
	/**
	 * Message Title
	 */
	private LabelInputMeta titremessage;
	
	/**
	 * Message Body
	 */
	private LabelTextareaMeta bodymessage;
	
	public JobEntryMsgBoxInfoDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			JobEntryMsgBoxInfo job = (JobEntryMsgBoxInfo)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// MsgBox Info
			this.name = new LabelInputMeta(id + ".name", "MsgBox信息：", null, null,
					"MsgBox信息必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Message Title
			this.titremessage = new LabelInputMeta(id + ".titremessage", "信息标题：", null, null,
					null, String.valueOf(job.getTitleMessage()), null, ValidateForm
							.getInstance().setRequired(false));
			this.titremessage.setSingle(true);
			
			// Message Body
			this.bodymessage = new LabelTextareaMeta(id + ".bodymessage", "信息内容", null, null,
					null, String.valueOf(job.getBodyMessage()), 8, null);
			
			this.bodymessage.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.titremessage,
					this.bodymessage
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
