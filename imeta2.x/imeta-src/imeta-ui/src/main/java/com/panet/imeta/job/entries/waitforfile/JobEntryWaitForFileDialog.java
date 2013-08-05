package com.panet.imeta.job.entries.waitforfile;

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

public class JobEntryWaitForFileDialog extends JobEntryDialog implements
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
	 * 文件名
	 */
	private LabelInputMeta filename;

	private ButtonMeta browse;

	/**
	 * 最大超时
	 */
	private LabelInputMeta maximumTimeout;

	/**
	 * 检查周期时间
	 */
	private LabelInputMeta checkCycleTime;
	/**
	 * 超时成功
	 */
	private LabelInputMeta successOnTimeout;
	/**
	 * 检查文件大小
	 */
	private LabelInputMeta fileSizeCheck;
	/**
	 * 添加文件名
	 */
	private LabelInputMeta addFilenameToResult;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryWaitForFileDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryWaitForFile job = (JobEntryWaitForFile)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到输入工作名
			this.name = new LabelInputMeta(id + ".name", "输入工作名", null, null,
					"输入工作名必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到文件名
			this.filename = new LabelInputMeta(id + ".filename", "文件名", null,
					null, "File name必须填写", job.getFilename(), null, ValidateForm
							.getInstance().setRequired(false));
			this.filename.setSingle(true);
//			this.browse = new ButtonMeta(id + ".btn.browse",
//					id + ".btn.browse", "浏览", "浏览");
//
//			this.filename.addButton(new ButtonMeta[] { this.browse });
			this.filename.setSingle(true);
			// 得到最大超时
			this.maximumTimeout = new LabelInputMeta(id + ".maximumTimeout", "最大超时", null,
					null, "0",job.getMaximumTimeout(), null, ValidateForm
							.getInstance().setRequired(false));
			this.maximumTimeout.setSingle(true);
			// 得到检查周期时间
			this.checkCycleTime = new LabelInputMeta(id + ".checkCycleTime", "检查周期时间",
					null, null, "60", job.getCheckCycleTime(), null,
					ValidateForm.getInstance().setRequired(false));
			this.checkCycleTime.setSingle(true);
			// 超时成功
			this.successOnTimeout = new LabelInputMeta(id + ".successOnTimeout",
					"超时成功", null, null, null, String.valueOf(job.isSuccessOnTimeout()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.successOnTimeout.setSingle(true);
			// 检查文件大小
			this.fileSizeCheck = new LabelInputMeta(id + ".fileSizeCheck",
					"检查文件大小", null, null, null, String.valueOf(job.isFileSizeCheck()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.fileSizeCheck.setSingle(true);
			// 添加文件名
			this.addFilenameToResult = new LabelInputMeta(id + ".addFilenameToResult",
					"添加文件名", null, null, null, String.valueOf(job.isAddFilenameToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addFilenameToResult.setSingle(true);

			// 装载到form
			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name,
							this.filename, this.maximumTimeout, this.checkCycleTime,
							this.successOnTimeout, this.fileSizeCheck,
							this.addFilenameToResult });
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
