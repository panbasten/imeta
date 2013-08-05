package com.panet.imeta.job.entries.createfolder;

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

public class JobEntryCreateFolderDialog extends JobEntryDialog implements
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
	 * 目录名
	 */
	private LabelInputMeta foldername;

	private ButtonMeta browse;
	/**
	 * 如果目录存在则失
	 */
	private LabelInputMeta failOfFolderExists;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryCreateFolderDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryCreateFolder job = (JobEntryCreateFolder) super
					.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业名称
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					"作业名称必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到目录名
			this.foldername = new LabelInputMeta(id + ".foldername", "目录名：",
					null, null, "目录名必须填写", job.getFoldername(),null, 
					ValidateForm.getInstance().setRequired(false));
			this.foldername.setSingle(true);
			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览", "浏览");

			// this.foldername.addButton(new ButtonMeta[] { this.browse });
			this.foldername.setSingle(true);
			// 如果目录存在则失
			this.failOfFolderExists = new LabelInputMeta(id
					+ ".failOfFolderExists", "如果目录存在则创建失败", null, null, null,
					String.valueOf(job.isFailOfFolderExists()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.failOfFolderExists.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.foldername, this.failOfFolderExists });

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
