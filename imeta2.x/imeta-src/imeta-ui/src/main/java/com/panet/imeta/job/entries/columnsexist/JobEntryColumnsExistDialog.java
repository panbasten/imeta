package com.panet.imeta.job.entries.columnsexist;

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
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryColumnsExistDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
	
//	private ButtonMeta edit;
//	
//	private ButtonMeta newDb;
	
	/**
	 * 模式名
	 */
	private LabelInputMeta schemaname;
	
	/**
	 * 表名
	 */
	private LabelInputMeta tablename;
	
//	private ButtonMeta browse;
	
	/**
	 * 列
	 */
	private LabelGridMeta line;
	
//	private ButtonMeta getLineName;
//	
//	private ButtonMeta delete;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryColumnsExistDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			JobEntryColumnsExist job = (JobEntryColumnsExist)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null, String
					.valueOf((job.getDatabase() != null) ? job
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connection.setSingle(true);
			
			// 模式名
			this.schemaname = new LabelInputMeta ( id + ".schemaname", "模式名", null, null,
					null, job.getSchemaname(), null, ValidateForm.getInstance().setRequired(false));
			this.schemaname.setSingle(true);
			
			// 表名
			this.tablename = new LabelInputMeta ( id + ".tablename", "表名", null, null,
					"表名必须填写", job.getTablename(), null, ValidateForm.getInstance().setRequired(true));
			
//			this.browse = new ButtonMeta ( id + ".btn.browse", id + ".btn.browse",
//					"浏览...", "浏览...");
//			this.tablename.addButton( new ButtonMeta[] {
//					this.browse
//			});
			this.tablename.setSingle(true);
			
			// 列
			this.line = new LabelGridMeta( id + "_line", "列", 200);
			this.line.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_line.lineId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_line.arguments", "列", null, false, 540)
					});
			this.line.setSingle(true);
			this.line.setHasBottomBar(true);
			this.line.setHasAdd(true, true,
					"jQuery.imeta.jobEntries.columnsexist.btn.lineAdd");
			this.line.setHasDelete(true, true,
	                "jQuery.imeta.parameter.fieldsDelete");
			
			String[] values = job.arguments;
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.line.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, job.arguments[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
//			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), false);
//			this.getLineName = new ButtonMeta ( id + ".btn.getLineName", id + ".btn.getLineName",
//					"获得列名", "获得列名");
//			this.getLineName.appendTo(div);
//			this.delete = new ButtonMeta ( id + ".btn.delete", id + ".btn.delete",
//					"删除", "删除");
//			this.delete.appendTo(div);
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaname, this.tablename, this.line,
//					div
					 });
		
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(),super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
