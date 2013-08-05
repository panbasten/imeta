package com.panet.imeta.job.entries.filecompare;

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

public class JobEntryFileCompareDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 输入工作名
	 */
	private LabelInputMeta name;

	/**
	 * 文件名 1
	 */
	private LabelInputMeta filename1;

	private ButtonMeta browse;

	/**
	 * 文件名 2
	 */
	private LabelInputMeta filename2;

	private ButtonMeta browses;
	/**
	 * 添加文件名
	 */
	private LabelInputMeta addFilenameToResult;

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
	public JobEntryFileCompareDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryFileCompare job = (JobEntryFileCompare) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到输入工作名：
			this.name = new LabelInputMeta(id + ".name", "输入工作名", null, null,
					"输入工作名必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到文件名 1
			this.filename1 = new LabelInputMeta(id + ".filename1", "文件名 1",
					null, null, "文件名1必须填写", job.getFilename1(), null,
					ValidateForm.getInstance().setRequired(false));
			this.filename1.setSingle(true);
			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览", "浏览");
			//
			// this.filename1.addButton(new ButtonMeta[] { this.browse });
			this.filename1.setSingle(true);
			// 得到文件名 2
			this.filename2 = new LabelInputMeta(id + ".filename2", "文件名 2",
					null, null, "文件名2必须填写", job.getFilename2(), null,
					ValidateForm.getInstance().setRequired(false));
			this.filename2.setSingle(true);
			// this.browses = new ButtonMeta(id + ".btn.browses", id
			// + ".btn.browses", "浏览", "浏览");
			//
			// this.filename2.addButton(new ButtonMeta[] { this.browses });
			this.filename2.setSingle(true);
			// 添加文件名
			this.addFilenameToResult = new LabelInputMeta(id
					+ ".addFilenameToResult", "添加文件名", null, null, null, String
					.valueOf(job.isAddFilenameToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addFilenameToResult.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filename1, this.filename2, this.addFilenameToResult });

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
