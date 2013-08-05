package com.panet.imeta.job.entries.filesexist;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryFilesExistDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * Job entry name
	 */
	private LabelInputMeta name;

	/**
	 * File/folder name
	 */
//	private LabelInputMeta filename;

	// private ButtonMeta add;
	// private ButtonMeta file;
	// private ButtonMeta folder;

	/**
	 * Files/Folders
	 */
	private LabelGridMeta files;

	// /**
	// * delete
	// */
	// private ButtonMeta delete;
	//
	// /**
	// * edit
	// */
	// private ButtonMeta edit;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryFilesExistDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryFilesExist job = (JobEntryFilesExist) super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到job名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称：", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// File/folder name
//			this.filename = new LabelInputMeta(id + ".filename", "文件/文件夹名称：",
//					null, null, "文件/文件夹名称必须填写", job.getFilename(), null,
//					ValidateForm.getInstance().setRequired(true));
			// this.add = new ButtonMeta(id + ".btn.add", id + ".btn.add",
			// "增加", "增加");
			//
			// this.file = new ButtonMeta(id + ".btn.file", id + ".btn.file",
			// "文件", "文件");
			//			
			// this.folder = new ButtonMeta(id + ".btn.folder", id +
			// ".btn.folder",
			// "文件夹", "文件夹");
			// this.fileName.addButton(new ButtonMeta[] { this.add, this.file,
			// this.folder });
//			this.filename.setSingle(true);

			// Files/Folders
			this.files = new LabelGridMeta(id + "_files", "文件/文件夹", 200);
			this.files.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_files.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_files.arguments", "文件/文件夹",
							null, false, 525) });
			this.files.setSingle(true);
			this.files.setHasBottomBar(true);
			this.files.setHasAdd(true, true,
					"jQuery.imeta.jobEntries.filesexist.btn.filesexistAdd");
			this.files.setHasDelete(true, true,
	                "jQuery.imeta.parameter.fieldsDelete");

			String[] values = job.arguments;
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.files.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			// DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), false);
			// this.delete = new ButtonMeta(id + ".btn.delete", id +
			// ".btn.delete",
			// "删除", "删除");
			// this.delete.appendTo(div);
			// this.edit = new ButtonMeta(id + ".btn.edit", id + ".btn.edit",
			// "编辑", "编辑");
			// this.edit.appendTo(div);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
//					this.filename, 
					this.files,
			// div
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
