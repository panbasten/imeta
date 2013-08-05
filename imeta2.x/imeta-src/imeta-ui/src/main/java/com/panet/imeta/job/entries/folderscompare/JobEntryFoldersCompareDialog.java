package com.panet.imeta.job.entries.folderscompare;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryFoldersCompareDialog extends JobEntryDialog implements
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
	 * 设置
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * 包括子文件夹
	 */
	private LabelInputMeta includesubfolders;
	/**
	 * 比较
	 */
	private LabelSelectMeta compareonly;
	/**
	 * 通配符
	 */
	private LabelInputMeta wildcard;
	/**
	 * 比较文件大小
	 */
	private LabelInputMeta comparefilesize;
	/**
	 * 比较文件
	 */
	private LabelInputMeta comparefilecontent;

	/**
	 * 文件名/文件夹名 1:
	 */
	private LabelInputMeta filename1;

	// private ButtonMeta editconncet;
	//
	// private ButtonMeta newconncet;
	/**
	 * 文件名/文件夹名 2:
	 */
	private LabelInputMeta filename2;

	// private ButtonMeta editconncets;
	//
	// private ButtonMeta newconncets;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	public JobEntryFoldersCompareDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryFoldersCompare job = (JobEntryFoldersCompare) super
					.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到输入工作名:
			this.name = new LabelInputMeta(id + ".name", "输入工作名", null, null,
					"输入工作名必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 设置
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			// 包括子文件夹
			this.includesubfolders = new LabelInputMeta(id
					+ ".includesubfolders", "包括子文件夹", null, null, null, String
					.valueOf(job.isIncludeSubfolders()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includesubfolders.setSingle(true);
			// 比较
			List<OptionDataMeta> compareonlyOptions = new ArrayList<OptionDataMeta>();
			compareonlyOptions.add(new OptionDataMeta("0", Messages
					.getString("JobFoldersCompare.All_CompareOnly.Label")));
			compareonlyOptions.add(new OptionDataMeta("1", Messages
					.getString("JobFoldersCompare.Files_CompareOnly.Label")));
			compareonlyOptions.add(new OptionDataMeta("2", Messages
					.getString("JobFoldersCompare.Folders_CompareOnly.Label")));
			compareonlyOptions.add(new OptionDataMeta("3", Messages
					.getString("JobFoldersCompare.Specify_CompareOnly.Label")));
			this.compareonly = new LabelSelectMeta(id + ".compareonly", "比较",
					null, null, null, job.getCompareOnly(), null,
					compareonlyOptions);
			this.compareonly
					.addListener("change",
							"jQuery.imeta.jobEntries.foldersCompare.listeners.compareonlyChange");
			this.compareonly.setSingle(true);
			// 得到通配符
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符", null,
					null, "通配符", job.getWildcard(), null, ValidateForm
							.getInstance().setRequired(false));
			this.wildcard.setDisabled(!job.getCompareOnly().equals("3"));
			this.wildcard.setSingle(true);
			// 比较文件大小
			this.comparefilesize = new LabelInputMeta(id + ".comparefilesize",
					"比较文件大小", null, null, null, String.valueOf(job
							.isCompareFileSize()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.comparefilesize.setDisabled(job.getCompareOnly().equals("2"));
			this.comparefilesize.setSingle(true);
			// 比较文件
			this.comparefilecontent = new LabelInputMeta(id
					+ ".comparefilecontent", "比较文件", null, null, null, String
					.valueOf(job.isCompareFileContent()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.comparefilecontent.setDisabled(job.getCompareOnly().equals("2"));
			this.comparefilecontent.setSingle(true);

			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.includesubfolders, this.compareonly, this.wildcard,
					this.comparefilesize, this.comparefilecontent });

			// 得到文件名/文件夹名 1:
			this.filename1 = new LabelInputMeta(id + ".filename1",
					"文件名/文件夹名 1:", null, null, null, job.getFilename1(), null,
					ValidateForm.getInstance().setRequired(false));
			this.filename1.setSingle(true);

			// 得到文件名/文件夹名 2:
			this.filename2 = new LabelInputMeta(id + ".filename2",
					"文件名/文件夹名 2:", null, null, null, job.getFilename2(), null,
					ValidateForm.getInstance().setRequired(false));
			this.filename2.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.settings, this.filename1, this.filename2 });

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
