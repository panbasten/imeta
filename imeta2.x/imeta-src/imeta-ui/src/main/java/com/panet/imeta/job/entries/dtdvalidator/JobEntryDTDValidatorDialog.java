package com.panet.imeta.job.entries.dtdvalidator;

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

public class JobEntryDTDValidatorDialog extends JobEntryDialog implements
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
	 * DTD Intern
	 */
	private LabelInputMeta dtdintern;

	/**
	 * DTD File name
	 */
	private LabelInputMeta dtdfilename;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryDTDValidatorDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryDTDValidator entry = (JobEntryDTDValidator) super.getJobEntry();
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

			// DTD Intern
			this.dtdintern = new LabelInputMeta(id + ".dtdintern",
					"DTD保留",null, null, null, String.valueOf(entry.getDTDIntern()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));

			// XSD File name
			this.dtdfilename = new LabelInputMeta(id + ".dtdfilename", "XSD文件名",
					null, null,null, entry.getdtdFilename(), null, ValidateForm.getInstance()
					.setRequired(true));
			this.dtdfilename.setSingle(true);

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.xmlfilename , this.dtdintern, dtdfilename});

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
