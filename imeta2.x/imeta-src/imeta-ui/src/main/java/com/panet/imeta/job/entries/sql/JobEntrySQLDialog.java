package com.panet.imeta.job.entries.sql;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class JobEntrySQLDialog extends JobEntryDialog implements
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
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
		
	/**
	 * 从文件中得到的
	 */
	private LabelInputMeta sqlfromfile;
	
	/**
	 * sql文件名
	 */
	private LabelInputMeta sqlfilename;
	
	
	/**
	 * 使用变量替换
	 */
	private LabelInputMeta useVariableSubstitution;
	
	/**
	 * sql脚本
	 */
	private LabelTextareaMeta sql;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntrySQLDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntrySQL jobEntry = (JobEntrySQL) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到连接数据库
			
			//数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null, String
					.valueOf((jobEntry.getDatabase() != null) ? jobEntry
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connection.setSingle(true);
			
			//从文件中得到的
			  this.sqlfromfile = new LabelInputMeta(id + ".sqlfromfile", "从文件中得到的", null,
						null, null, 
						String.valueOf(jobEntry.getSQLFromFile()),
						InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
			  this.sqlfromfile.addClick("jQuery.imeta.jobEntries.sql.listeners.sqlfromfile");
			  
			  //设置显示的宽度
			  this.sqlfromfile.setWidth(300);
			  this.sqlfromfile.setSingle(true);
			
			// sql文件名
			this.sqlfilename = new LabelInputMeta(id + ".sqlfilename", "sql文件名", null, null,
					"sql文件名", jobEntry.getSQLFilename(), null, ValidateForm
							.getInstance().setRequired(false));			
			this.sqlfilename.setDisabled(jobEntry.getSQLFromFile());
			this.sqlfilename.setSingle(true);                          
			
			
			// 使用变量替换
			  this.useVariableSubstitution = new LabelInputMeta(id + ".useVariableSubstitution", "使用变量替换", null,
						null, null, String.valueOf(jobEntry.getUseVariableSubstitution()), InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
			  this.useVariableSubstitution.setDisabled(jobEntry.getSQLFromFile());
			  this.useVariableSubstitution.setSingle(true);
			    
			// sql脚本
			  
				this.sql = new LabelTextareaMeta(id + ".sql", "sql脚本", null, null,
						null, jobEntry.getSQL(), 300, null);
				this.sql.setDisabled(jobEntry.getSQLFromFile());
				this.sql.setSingle(true);
			 
				// 装载到form
				columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
						this.name,
						this.connection,
						this.sqlfromfile,
						this.sqlfilename,
						this.useVariableSubstitution,
						this.sql
						  });
				
				// 确定，取消
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
