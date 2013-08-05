package com.panet.imeta.job.entries.waitforsql;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryWaitForSQLDialog extends JobEntryDialog implements
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
	private LabelSelectMeta databaseConnection;
//	private ButtonMeta editBtn;
//	private ButtonMeta newBtn;
	
	/**
	 * Target schema
	 */
	private LabelInputMeta schemaname;
	
	/**
	 * Target table name
	 */
	private LabelInputMeta tablename;
	
	/**
	 * Success condition
	 */
	private ColumnFieldsetMeta successConditionColumn;
	
	/**
	 * Success when rows count
	 */
	private LabelSelectMeta successCondition;
	
	/**
	 * Value
	 */
	private LabelInputMeta rowsCountValue;
	
	/**
	 * Maximum timeout
	 */
	private LabelInputMeta maximumTimeout;
	
	/**
	 * Check cycle time
	 */
	private LabelInputMeta checkCycleTime;
	
	/**
	 * Success on timeout
	 */
	private LabelInputMeta successOnTimeout;
	
	/**
	 * Custom SQL
	 */
	private ColumnFieldsetMeta customSQLColumn;
	
	/**
	 * Custom SQL
	 */
	private LabelInputMeta iscustomSQL;
	
	/**
	 * Use variable substitution
	 */
	private LabelInputMeta isUseVars;
	
	/**
	 * Clear list of result rows before
	 */
	private LabelInputMeta isClearResultList;
	
	/**
	 * Add rows to result
	 */
	private LabelInputMeta isAddRowsResult;
	
	/**
	 * SQL Script
	 */
	private LabelTextareaMeta customSQL;
//	private ButtonMeta getSqlSelect;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryWaitForSQLDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryWaitForSQL entry = (JobEntryWaitForSQL) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 任务实体名
			this.name = new LabelInputMeta(id + ".name", "任务实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 数据库连接
			this.databaseConnection = new LabelSelectMeta(id + ".databaseConnection",
					"数据库连接", null, null, null, String
					.valueOf((entry.getDatabase() != null) ? entry
							.getDatabase().getID() : ""),
					null, super.getConnectionLine());
//			this.editBtn = new ButtonMeta(id
//					+ ".btn.editBtn", id + ".btn.editBtn",
//					"编辑", "编辑");
//			this.newBtn = new ButtonMeta(id
//					+ ".btn.newBtn", id + ".btn.newBtn",
//					"新建", "新建");
//			this.databaseConnection.addButton(new ButtonMeta[]{this.editBtn,this.newBtn});
			this.databaseConnection.setSingle(true);
			
			// Target schema
			this.schemaname = new LabelInputMeta(id + ".schemaname",
					"目标模式",null, null, "目标模式未填", entry.schemaname, null, 
					ValidateForm.getInstance().setRequired(true));
			this.schemaname.setSingle(true);
			this.schemaname.setDisabled(entry.iscustomSQL);
			
			// Target table name
			this.tablename = new LabelInputMeta(id + ".tablename",
					"目标表的名字",null, null, "目标表的名字未填", entry.tablename,
					null, ValidateForm.getInstance().setRequired(true));
			this.tablename.setSingle(true);
			this.tablename.setDisabled(entry.iscustomSQL);
			
			// Success condition
			this.successConditionColumn = new ColumnFieldsetMeta(id, "成功的条件");
			this.successConditionColumn.setSingle(true);
			
			// Success when rows count
			String[] options = JobEntryWaitForSQL.successConditionsDesc;
			List<OptionDataMeta> successConditionOptions = new ArrayList<OptionDataMeta>();
			for(int i = 0; i < options.length; i++){
				successConditionOptions.add(new OptionDataMeta(
						String.valueOf(i + 1), options[i]
				));
			}
			this.successCondition = new LabelSelectMeta(id + ".successCondition",
					"成功时的行数", null, null, null, String.valueOf(entry.successCondition),
					null, successConditionOptions);
			this.successCondition.setSingle(true);
			
			// Value
			this.rowsCountValue = new LabelInputMeta(id + ".rowsCountValue",
					"值",null, null, null, String.valueOf(entry.rowsCountValue),
					null, ValidateForm.getInstance().setNumber(true));
			this.rowsCountValue.setSingle(true);
			
			// Maximum timeout
			this.maximumTimeout =  new LabelInputMeta(id + ".maximumTimeout",
					"最大超时",null, null, null, String.valueOf(entry.getMaximumTimeout()),
					null, ValidateForm.getInstance().setNumber(true));
			this.maximumTimeout.setSingle(true);
			
			// Check cycle time
			this.checkCycleTime = new LabelInputMeta(id + ".checkCycleTime",
					"检查周期时间",null, null, null, String.valueOf(entry.getCheckCycleTime()),
					null, ValidateForm.getInstance().setNumber(true));
			this.checkCycleTime.setSingle(true);
			
			// Success on timeout
			this.successOnTimeout = new LabelInputMeta(id + ".successOnTimeout",
					"成功超时",null, null, null, String.valueOf(entry.isSuccessOnTimeout()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
		
			this.successConditionColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.successCondition,this.rowsCountValue,this.maximumTimeout,
					this.checkCycleTime,this.successOnTimeout
			});
			
			// Custom SQL
			this.customSQLColumn = new ColumnFieldsetMeta(id, "自定义的SQL");
			this.customSQLColumn.setSingle(true);
			
			// Custom SQL
			this.iscustomSQL = new LabelInputMeta(id + ".iscustomSQL", 
					"自定义的SQL",null, null, null, String.valueOf(entry.iscustomSQL), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,ValidateForm
					.getInstance().setRequired(true));
			this.iscustomSQL.addClick("jQuery.imeta.jobEntries.jobentrywaitforSQL.listeners.iscustomSQLListeners");
			
			// Use variable substitution
			this.isUseVars = new LabelInputMeta(id + ".isUseVars", 
					"利用变量代换",null, null, null, String.valueOf(entry.isUseVars), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,ValidateForm
					.getInstance().setRequired(true));
			this.isUseVars.setDisabled(!entry.iscustomSQL);
			
			// Clear list of result rows before
			this.isClearResultList = new LabelInputMeta(id + ".isClearResultList", 
					"结果行前明确的清单",null, null, null, String.valueOf(entry.isClearResultList), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,ValidateForm
					.getInstance().setRequired(true));
			this.isClearResultList.setDisabled(!entry.iscustomSQL);
			
			// Add rows to result
			this.isAddRowsResult = new LabelInputMeta(id + ".isAddRowsResult", 
					"添加行的结果",null, null, null, String.valueOf(entry.isAddRowsResult), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,ValidateForm
					.getInstance().setRequired(true));
			this.isAddRowsResult.setDisabled(!entry.iscustomSQL);
			
			// SQL Script
			this.customSQL = new LabelTextareaMeta(id + ".customSQL" , "SQL脚本" , null , null,
					null , String.valueOf(entry.customSQL) , 5 , null);
//			this.getSqlSelect = new ButtonMeta(id
//					+ ".getSqlSelect", id + ".getSqlSelect",
//					"获取SQL查询", "获取SQL查询");
//			this.getSqlSelect.setDisabled(!entry.iscustomSQL);
//			this.customSQL.addButton(new ButtonMeta[]{this.getSqlSelect});
			this.customSQL.setSingle(true);
			this.customSQL.setDisabled(!entry.iscustomSQL);
			
			this.customSQLColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.iscustomSQL,this.isUseVars,this.isClearResultList,
					this.isAddRowsResult,this.customSQL});
			
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.databaseConnection,this.schemaname,this.tablename,
					this.successConditionColumn,this.customSQLColumn});
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});
			cArr.add(columnFormMeta.getFormJo());
			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		}catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
