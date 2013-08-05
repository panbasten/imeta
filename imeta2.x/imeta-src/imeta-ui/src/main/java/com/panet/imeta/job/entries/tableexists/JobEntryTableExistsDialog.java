package com.panet.imeta.job.entries.tableexists;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryTableExistsDialog extends JobEntryDialog implements
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
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
	
	/**
	 * Schema name
	 */
	private LabelInputMeta schemaname;
	
	/**
	 * Table name
	 */
	private LabelInputMeta tablename;


	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryTableExistsDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryTableExists job = (JobEntryTableExists)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到job名称
			this.name = new LabelInputMeta(id + ".name", "工作项目名称", null, null,
					"job名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			//数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null, String
					.valueOf((job.getDatabase() != null) ? job
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connection.setSingle(true);

			// Schema name
			this.schemaname = new LabelInputMeta(id + ".schemaname", "模式名称：", null, null,
					"模式名称必须填写", job.getSchemaname(), null, ValidateForm
							.getInstance().setRequired(true));
			this.schemaname.setSingle(true);
			
			//Table name
			this.tablename = new LabelInputMeta(id + ".tablename", "表名", null, null,
					"表名必须填写", job.getTablename(), null, ValidateForm
							.getInstance().setRequired(true));
			this.tablename.setSingle(true);
		
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.connection, this.schemaname,
				    this.tablename 
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
