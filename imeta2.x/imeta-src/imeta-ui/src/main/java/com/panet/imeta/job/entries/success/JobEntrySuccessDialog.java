package com.panet.imeta.job.entries.success;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntrySuccessDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private LabelInputMeta name;
	
	public JobEntrySuccessDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 任务实体名
			this.name = new LabelInputMeta(id + ".name", "任务实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name });

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

