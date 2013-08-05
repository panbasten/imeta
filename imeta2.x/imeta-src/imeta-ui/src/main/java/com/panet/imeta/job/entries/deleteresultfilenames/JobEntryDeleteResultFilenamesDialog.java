package com.panet.imeta.job.entries.deleteresultfilenames;

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

public class JobEntryDeleteResultFilenamesDialog extends JobEntryDialog
		implements JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 作业项名称:
	 */                         
	private LabelInputMeta name;
	/**
	 * 限制操作
	 */
	private LabelInputMeta specifywildcard;
	/**
	 * 通配符
	 */
	private LabelInputMeta wildcard;
	/**
	 * 排除通配符
	 */
	private LabelInputMeta wildcardexclude;

	

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryDeleteResultFilenamesDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryDeleteResultFilenames job = (JobEntryDeleteResultFilenames)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称:", null, null,
					null, super.getJobEntryName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.name.setSingle(true);
			// 限制行径
			boolean isSpecifywildcardClick = job.isSpecifyWildcard();
			this.specifywildcard = new LabelInputMeta(id + ".specifywildcard",
					"限制操作", null, null, null, String.valueOf(isSpecifywildcardClick),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.specifywildcard
			.addClick("jQuery.imeta.jobEntries.deleteResultFileNames.listeners.isSpecifywildcardClick");
			this.specifywildcard.setSingle(true);
			// 得到通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符", null,
					null, "通配符", job.getWildcard(), null,
					ValidateForm.getInstance().setRequired(false));
			this.wildcard.setDisabled(!isSpecifywildcardClick);
			this.wildcard.setSingle(true);
			// 排除通配符
			this.wildcardexclude = new LabelInputMeta(id + ".wildcardexclude", "排除通配符",
					null, null, "排除通配符", job.getWildcardExclude(), null,
					ValidateForm.getInstance().setRequired(false));
			this.wildcardexclude.setDisabled(!isSpecifywildcardClick);
			this.wildcardexclude.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.specifywildcard, this.wildcard, this.wildcardexclude });

			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(),super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
