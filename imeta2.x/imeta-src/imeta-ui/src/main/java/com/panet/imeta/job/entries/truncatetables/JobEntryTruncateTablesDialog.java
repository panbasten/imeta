package com.panet.imeta.job.entries.truncatetables;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class JobEntryTruncateTablesDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 任务实体名
	 */
	private LabelInputMeta name;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
//	private ButtonMeta editBtn;
//	private ButtonMeta newBtn;
	
	/**
	 * Click here to return tables name list
	 */
//	private ButtonMeta returnTablesNameList;
	
	/**
	 * Previous result to args
	 */
	private LabelInputMeta argFromPrevious;
	
	/**
	 * Selected tables
	 */
	private LabelGridMeta selectedTablesGrid;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryTruncateTablesDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryTruncateTables entry = (JobEntryTruncateTables) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 任务实体名
			this.name = new LabelInputMeta(id + ".name", "任务实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 数据库连接
			this.connection = new LabelSelectMeta(id + ".connection",
					"数据库连接", null, null, null, String
					.valueOf((entry.getDatabase() != null) ? entry
							.getDatabase().getID() : ""), null, 
					super.getConnectionLine());
//			this.editBtn = new ButtonMeta(id
//					+ ".btn.editBtn", id + ".btn.editBtn",
//					"编辑", "编辑");
//			this.newBtn = new ButtonMeta(id
//					+ ".btn.newBtn", id + ".btn.newBtn",
//					"新建", "新建");
//			this.databaseConnection.addButton(new ButtonMeta[]{this.editBtn,this.newBtn});
			this.connection.setSingle(true);

			// Previous result to args
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"先前的结果参数",null, null, null, String.valueOf(entry.argFromPrevious), 
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
//				
//			// Click here to return tables name list
//			this.returnTablesNameList = new ButtonMeta(id
//					+ ".btn.returnTablesNameList", id + ".btn.returnTablesNameList",
//					"点击此处返回名单表", 
//					"点击此处返回名单表");
//			this.returnTablesNameList.setSingle(true);
			
			// Selected tables
			this.selectedTablesGrid = new LabelGridMeta(id + "_selectedTablesGrid",
					"选择表",150);
			this.selectedTablesGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedTablesGrid.fieldId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedTablesGrid.arguments", "表名", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedTablesGrid.schemaname", "模式名称", null, false, 200)
					}) ;
			this.selectedTablesGrid.setSingle(true);
			this.selectedTablesGrid.setHasBottomBar(true);
			this.selectedTablesGrid.setHasAdd(true, true, 
					"jQuery.imeta.jobEntries.jobentrytruncatetables.btn.fieldAdd");
			this.selectedTablesGrid.setHasDelete(true, true, 
					"jQuery.imeta.parameter.fieldsDelete");
			
			String[] arguments = entry.arguments;
			if(arguments != null && arguments.length > 0){
				for(int i = 0; i < arguments.length; i++){
					this.selectedTablesGrid.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,entry.arguments[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.schemaname[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
					});	
				}
			}

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection ,this.argFromPrevious,
					this.selectedTablesGrid});
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});
			cArr.add(columnFormMeta.getFormJo());
			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
