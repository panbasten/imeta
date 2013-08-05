package com.panet.imeta.job.entries.xsdvalidator;

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

public class JobEntryXSDValidatorDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 任务实体名
	 */
	private LabelInputMeta name;

	/**
	 * XML File name
	 */
	private LabelInputMeta xmlfilename;

	/**
	 * XSD File name
	 */
	private LabelInputMeta xsdfilename;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryXSDValidatorDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryXSDValidator entry = (JobEntryXSDValidator) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 任务实体名
			this.name = new LabelInputMeta(id + ".name", "任务实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);

			// XML File name
			this.xmlfilename = new LabelInputMeta(id + ".xmlfilename", "XML文件名",
					null, null,null, entry.getxmlFilename(), null, 
					ValidateForm.getInstance().setRequired(true));
			this.xmlfilename.setSingle(true);

			// XSD File name
			this.xsdfilename = new LabelInputMeta(id + ".xsdfilename", "XSD文件名",
					null, null,null, entry.getxsdFilename(), null, 
					ValidateForm.getInstance().setRequired(true));
			this.xsdfilename.setSingle(true);

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.xmlfilename ,xsdfilename});


			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
