package com.panet.imeta.job.entries.eval;

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

public class JobEntryEvalDialog extends JobEntryDialog implements
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
	 * javaScriptName
	 */
	private LabelTextareaMeta script;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryEvalDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryEval  jobEntry = (JobEntryEval) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
	
			// javaScriptName
			this.script = new LabelTextareaMeta(id + ".script","脚本", null, null,
					"脚本",jobEntry.getScript(),
					50, ValidateForm
							.getInstance().setRequired(false));
			this.script.setSingle(true);
			
				// 装载到form
				columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
						this.name,
						this.script
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
