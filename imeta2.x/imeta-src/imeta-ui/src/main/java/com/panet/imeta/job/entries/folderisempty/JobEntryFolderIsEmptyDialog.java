package com.panet.imeta.job.entries.folderisempty;

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

public class JobEntryFolderIsEmptyDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * job entry name
	 */
	private LabelInputMeta name;
	
	/**
	 * Folder name
	 */
	private LabelInputMeta foldername;
    
//	private Button?Meta browse;
	
	/**
	 * Include
	 */
	private LabelInputMeta includeSubfolders;
	
	/**
	 * Limit search to
	 */
	private LabelInputMeta specifywildcard;
	
	/**
	 * Wildcard
	 */
	private LabelInputMeta wildcard;
	

	public JobEntryFolderIsEmptyDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryFolderIsEmpty job = (JobEntryFolderIsEmpty)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到job entry name
			this.name = new LabelInputMeta(id + ".name", "工作项目名称：", null, null,
					"job名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
            // Folder name
			this.foldername = new LabelInputMeta(id + ".foldername", "文件夹名称：", null,
					null, "文件夹名称必须填写", job.getFoldername(), null, ValidateForm.getInstance()
							.setRequired(true));
//			this.browse = new ButtonMeta(id + ".btn.browse", id + ".btn.browse",
//					"浏览", "浏览");
//
//			this.foldername.addButton(new ButtonMeta[] { this.browse });
			this.foldername.setSingle(true);
			
			// Include
			this.includeSubfolders = new LabelInputMeta(id + ".includeSubfolders", "包含", null,
					null, null, String.valueOf(job.isIncludeSubFolders()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.includeSubfolders.setSingle(true);
			
			// Limit search to
			this.specifywildcard = new LabelInputMeta(id + ".specifywildcard", "限制搜索", null,
					null, null, String.valueOf(job.isSpecifyWildcard()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.specifywildcard.setSingle(true);
			this.specifywildcard.addClick("jQuery.imeta.jobEntries.folderisempty.listeners.specifywildcard");
			
			// Wildcard
			this.wildcard = new LabelInputMeta(id + ".wildcard", "通配符", null, null,
					"通配符必须填写", job.getWildcard(), null, ValidateForm
							.getInstance().setRequired(true));
			this.wildcard.setSingle(true);
			this.wildcard.setDisabled(!job.isSpecifyWildcard());
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.foldername, this.includeSubfolders,
					this.specifywildcard, this.wildcard
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

