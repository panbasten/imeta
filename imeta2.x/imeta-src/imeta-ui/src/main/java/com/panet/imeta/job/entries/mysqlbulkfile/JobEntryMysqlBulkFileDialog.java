package com.panet.imeta.job.entries.mysqlbulkfile;

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
import com.panet.imeta.job.entries.msaccessbulkload.Messages;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryMysqlBulkFileDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	// 批量导出mysql
	
	/**
	 * 作业项名称
	 */
	private LabelInputMeta name;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta  connection;
	

	/**
	 * source schema
	 */
	private LabelInputMeta schemaname;

	
	/**
	 * source table name
	 */
	private LabelInputMeta tablename;
	
	/**
	 * target file name
	 */
	private LabelInputMeta filename;
	
	/**
	 * high priority 
	 */
	private LabelInputMeta highpriority ;
	
	/**
	 * type
	 */
	private LabelSelectMeta outdumpvalue;
	
	/**
	 * fields separator by
	 */
	private LabelInputMeta separator;
	
	/**
	 * fields enclosed by
	 */
	private LabelInputMeta enclosed;

	/**
	 * optionally Enclosed
	 */
	private LabelInputMeta optionenclosed;
	
	
	/**
	 * lines terminated by
	 */
	private LabelInputMeta lineterminated;
	
	/**
	 * names of columns
	 */
	private LabelInputMeta listcolumn;

	/**
	 * limit the first lines
	 */
	private LabelInputMeta limitlines;
	
	/**
	 * if file Exists
	 */
	private LabelSelectMeta iffileexists;

	/**
	* result filenames
	*/
	  private ColumnFieldsetMeta filenames;
	  
	/**
	 * add file to result filenames
	 */
	private LabelInputMeta addfiletoresult;
	
	
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryMysqlBulkFileDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryMysqlBulkFile  jobEntry = (JobEntryMysqlBulkFile) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
	
			
			//数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null, String
					.valueOf((jobEntry.getDatabase() != null) ? jobEntry
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connection.setSingle(true);
			
			// source schema
			this.schemaname= new LabelInputMeta(id + ".schemaname", "基础纲要", null, null,
					"基础纲要",jobEntry.getSchemaname(), null, ValidateForm
							.getInstance().setRequired(false));
			this.schemaname.setSingle(true);
			
			// source table
			this.tablename= new LabelInputMeta(id + ".tablename", "基础表", null, null,
					"基础表", jobEntry.getTablename(), null, ValidateForm
							.getInstance().setRequired(false));
			
			this.tablename.setSingle(true);
			
			// target file name
			this.filename= new LabelInputMeta(id + ".filename", "目标文件名", null, null,
					"目标文件名", jobEntry.getFilename(), null, ValidateForm
							.getInstance().setRequired(false));

			this.filename.setSingle(true);
			
			// high priority 
			this.highpriority= new LabelInputMeta(id + ".highpriority","最高优先级", null,null, 
					"最高优先级", String.valueOf(jobEntry.isHighPriority()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.highpriority.setSingle(true);
	        
	        // type 
	        List<OptionDataMeta> outdumpvalueOptions = new ArrayList<OptionDataMeta>();
	        
	        outdumpvalueOptions.add(new OptionDataMeta("0", Messages
					.getString("JobMysqlBulkFile.OutFileValue.Label")));
	        outdumpvalueOptions.add(new OptionDataMeta("1", Messages
					.getString("JobMysqlBulkFile.DumpFileValue.Label")));

			this.outdumpvalue = new LabelSelectMeta(id + ".outdumpvalue","类型：",null,null,null,
					                        String.valueOf(jobEntry.outdumpvalue),null,outdumpvalueOptions);
			this.outdumpvalue.setSingle(true);
			
			 // fields separator by
			this.separator= new LabelInputMeta(id + ".separator", "分隔字段", null, null,
					"分隔字段", jobEntry.getSeparator(), null, ValidateForm
							.getInstance().setRequired(false));
			this.separator.setSingle(true);
			
			 //optionally enclosed 	
			this.optionenclosed  = new LabelInputMeta(id + ".optionenclosed","随机关闭 ", null,null, null, 
					String.valueOf(jobEntry.isOptionEnclosed()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.optionenclosed .setSingle(true);
			
			 // fields enclosed by
			this.enclosed= new LabelInputMeta(id + ".enclosed", "封闭字段 ", null, null,
					"封闭字段", jobEntry.getEnclosed(), null, ValidateForm
							.getInstance().setRequired(false));
			this.enclosed.setSingle(true);
			
			
			 // lines terminated by
			this.lineterminated= new LabelInputMeta(id + ".lineterminated", "结束行", null, null,
					"结束行", jobEntry.getLineterminated(), null, ValidateForm
							.getInstance().setRequired(false));
			this.lineterminated.setSingle(true);
			
			 // names Of Columns
			this.listcolumn = new LabelInputMeta(id + ".listcolumn", "命名行", null, null,
					"命名行",jobEntry.getListColumn(), null, ValidateForm
							.getInstance().setRequired(false));
			this.listcolumn.setSingle(true);
			
		
			
			 // limit the first lines
			this.limitlines = new LabelInputMeta(id + ".limitlines", "限制第一行", null, null,
					"限制第一行", jobEntry.getLimitlines(), null, ValidateForm
							.getInstance().setRequired(false));
			this.limitlines.setSingle(true);
			
			 // if file exists 
			List<OptionDataMeta> iffileexistsOptions = new ArrayList<OptionDataMeta>();
			
			iffileexistsOptions.add(new OptionDataMeta("0", Messages
						.getString("JobMysqlBulkFile.Create_NewFile_IfFileExists.Label")));
			iffileexistsOptions.add(new OptionDataMeta("1", Messages
						.getString("JobMysqlBulkFile.Do_Nothing_IfFileExists.Label")));
			iffileexistsOptions.add(new OptionDataMeta("2", Messages
						.getString("JobMysqlBulkFile.Fail_IfFileExists.Label")));

			this.iffileexists = new LabelSelectMeta(id + ".iffileexists","文件是否存在：", null,null,
					null,String.valueOf(jobEntry.iffileexists),null,iffileexistsOptions);
			this.iffileexists.setSingle(true);
			
			
			// result filenames
			this.filenames  = new ColumnFieldsetMeta(null, "结果文件名");
			this.filenames.setSingle(true);
			
		   //add file to result filenames	
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult", "添加文件名", null,
					null, null, 
					String.valueOf(jobEntry.isAddFileToResult()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.addfiletoresult.setSingle(true);
	        
	        this.filenames.putFieldsetsContent(new BaseFormMeta[] {this.addfiletoresult});

        	// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.connection,this.schemaname 
					,this.tablename ,this.filename 
					,this.highpriority ,this.outdumpvalue 
					,this.separator ,this.enclosed 
					,this.optionenclosed ,this.lineterminated  
					,this.listcolumn  ,this.limitlines 
					, this.iffileexists ,this.filenames 
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
