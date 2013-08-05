package com.panet.imeta.job.entries.delay;

import java.util.ArrayList;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
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

public class JobEntryDelayDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 作业名称
	 */
	private LabelInputMeta name;
	
	/**
	 * 最大超时
	 */
	private LabelInputMeta maximumTimeout;
	
	/**
	 * 
	 */
	private LabelSelectMeta scaleTime;


	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryDelayDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryDelay job = (JobEntryDelay)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 作业名
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					"作业名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 最大超时
			this.maximumTimeout = new LabelInputMeta(id + ".maximumTimeout", "最大超时", null, null,
					"最大超时必须填写", job.getMaximumTimeout(), null, ValidateForm
							.getInstance().setRequired(true));
			this.maximumTimeout.setSingle(true);
			
			// 
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("0", Messages.getString("JobEntryDelay.SScaleTime.Label")));
		    options.add(new OptionDataMeta("1", Messages.getString("JobEntryDelay.MnScaleTime.Label")));
			options.add(new OptionDataMeta("2", Messages.getString("JobEntryDelay.HrScaleTime.Label")));
			this.scaleTime = new LabelSelectMeta(id + ".scaleTime","",
					null,null,null,String.valueOf(job.getScaleTime()),null,options);
			this.scaleTime.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.maximumTimeout, this.scaleTime
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
