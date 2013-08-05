package com.panet.imeta.job.entries.deletefolders;

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
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryDeleteFoldersDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 作业项名称
	 */
	private LabelInputMeta name;

	/**
	 * 设置
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * 复制先前的结果
	 */
	private LabelInputMeta argFromPrevious;

	/**
	 * 成功
	 */
	private ColumnFieldsetMeta successOn;

	/**
	 * 合格条件
	 */
	private LabelSelectMeta success_condition;
	/**
	 * 极限值
	 */
	private LabelInputMeta limit_folders;

	/**
	 * 多个文件夹
	 */
	private LabelGridMeta foldersToDeleteTable;

	public JobEntryDeleteFoldersDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryDeleteFolders job = (JobEntryDeleteFolders) super
					.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到作业项名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称必须填写", super.getJobEntryName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);
			// 设置
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			// 复制先前的结果
			boolean isArgFromPrevious = job.isArgFromPrevious();
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"复制先前的结果", null, null, null, String
							.valueOf(isArgFromPrevious),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.argFromPrevious
					.addClick("jQuery.imeta.jobEntries.deleteFolders.listeners.isArgFromPreviousClick");
			this.argFromPrevious.setSingle(false);

			this.settings
					.putFieldsetsContent(new BaseFormMeta[] { this.argFromPrevious });

			// 成功
			this.successOn = new ColumnFieldsetMeta(null, "成功");
			this.successOn.setSingle(true);

			// 得到合格条件

			List<OptionDataMeta> successOptions = new ArrayList<OptionDataMeta>();
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_NO_ERRORS,
							Messages
									.getString("JobDeleteFolders.SuccessWhenAllWorksFine.Label")));
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_AT_LEAST_X_FOLDERS_DELETED,
							Messages
									.getString("JobDeleteFolders.SuccessWhenAtLeat.Label")));
			successOptions
					.add(new OptionDataMeta(
							job.SUCCESS_IF_ERRORS_LESS,
							Messages
									.getString("JobDeleteFolders.SuccessWhenErrorsLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id
					+ ".success_condition", "成功条件", null, null, "成功条件", job
					.getSuccessCondition(), null, successOptions);
			this.success_condition
					.addListener("change",
							"jQuery.imeta.jobEntries.deleteFolders.listeners.success_conditionChange");
			this.success_condition.setSingle(true);

			this.limit_folders = new LabelInputMeta(id + ".limit_folders",
					"限制个数", null, null, "限制个数", job.getLimitFolders(), null,
					ValidateForm.getInstance().setRequired(false));
			this.limit_folders.setDisabled(job.getSuccessCondition().equals(
					job.SUCCESS_IF_NO_ERRORS));
			this.limit_folders.setSingle(true);
			
			this.successOn.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition, this.limit_folders });
//			this.successOn
//					.putFieldsetsContent(new BaseFormMeta[] { this.success_condition,this.limit_folders });

			// 得到极限值
			

			// 得到多个文件夹

			this.foldersToDeleteTable = new LabelGridMeta(id
					+ "_foldersToDeleteTable", "文件夹：", 200, 0);
			this.foldersToDeleteTable.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_foldersToDeleteTable.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id
							+ "_foldersToDeleteTable.folderName", "文件夹", null,
							false, 100) });
			this.foldersToDeleteTable.setHasBottomBar(true);
			this.foldersToDeleteTable.setHasAdd(true, !isArgFromPrevious,
					"jQuery.imeta.jobEntries.deleteFolders.btn.fieldAdd");
			this.foldersToDeleteTable.setHasDelete(true, !isArgFromPrevious,
					"jQuery.imeta.parameter.fieldsDelete");
			this.foldersToDeleteTable.setSingle(true);

			String[] folders = job.getArguments();
			if (folders != null && folders.length > 0) {
				GridCellDataMeta folderNames;
				for (int i = 0; i < folders.length; i++) {
					folderNames = new GridCellDataMeta(null, folders[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					folderNames.setDisabled(isArgFromPrevious);
					this.foldersToDeleteTable.addRow(new Object[] {
							String.valueOf(i + 1), folderNames });
				}
			}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.settings, this.successOn, 
					this.foldersToDeleteTable });

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
