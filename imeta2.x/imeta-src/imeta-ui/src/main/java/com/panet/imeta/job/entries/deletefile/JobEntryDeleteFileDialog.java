package com.panet.imeta.job.entries.deletefile;

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

public class JobEntryDeleteFileDialog extends JobEntryDialog implements
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
	 * 文件名
	 */
	private LabelInputMeta filename;

	private ButtonMeta browse;
	/**
	 * 如果文件不存在则失
	 */
	private LabelInputMeta failIfFileNotExists;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryDeleteFileDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryDeleteFile job = (JobEntryDeleteFile) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业名称
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					"作业名称必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到文件名
			this.filename = new LabelInputMeta(id + ".filename", "文件名：", null,
					null, "文件名必须填写", job.getFilename(), null, ValidateForm
							.getInstance().setRequired(false));
			this.filename.setSingle(true);
			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览", "浏览");
			//
			// this.filename.addButton(new ButtonMeta[] { this.browse });
			this.filename.setSingle(true);
			// 如果文件不存在则失
			this.failIfFileNotExists = new LabelInputMeta(id
					+ ".failIfFileNotExists", "如果文件不存在则删除失败", null, null, null,
					String.valueOf(job.isFailIfFileNotExists()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.failIfFileNotExists.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filename, this.failIfFileNotExists });
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
