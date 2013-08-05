package com.panet.imeta.job.entries.mysqlbulkload;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
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

public class JobEntryMysqlBulkLoadDialog extends JobEntryDialog implements
JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	// 批量导入mysql
	
	/**
	 * 作业项名称
	 */
	private LabelInputMeta name;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta  connection;
	
	/**
	 * 目标纲要
	 */
	private LabelInputMeta schemaname;

	
	/**
	 * 目标表名
	 */
	private LabelInputMeta tablename;

	
	/**
	 * 源文件名
	 */
	private LabelInputMeta filename;
	
	/**
	 * 本地
	 */
	private LabelInputMeta localinfile;
	
	/**
	 * 优先选择
	 */
	private LabelSelectMeta prorityvalue;
	
	/**
	 * 字段被终止
	 */
	private LabelInputMeta separator;
	
	/**
	 * 字段被关闭
	 */
	private LabelInputMeta enclosed;


	/**
	 * 字段溢出
	 */
	private LabelInputMeta escaped;
	
	/**
	 * 线开始
	 */
	private LabelInputMeta linestarted;
	
	/**
	 * 线被终止
	 */
	private LabelInputMeta lineterminated;
	
	/**
	 * 字段
	 */
	private LabelInputMeta listattribut;
	
	/**
	 *替换数据
	 */
	private LabelInputMeta replacedata;

	/**
	 * ignore the first lines 忽略第一行
	 */
	private LabelInputMeta ignorelines;

	/**
	* result filenames  产生的文件名
	*/
	  private ColumnFieldsetMeta filenames;
	  
	/**
	 * add file to result filenames   添加文件
	 */
	private LabelInputMeta addfiletoresult;

	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryMysqlBulkLoadDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}
	

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryMysqlBulkLoad  jobEntry = (JobEntryMysqlBulkLoad) super.getJobEntry();

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
			
			
			// target schema
			this.schemaname= new LabelInputMeta(id + ".schemaname", "目标纲要", null, null,
					"目标纲要", jobEntry.getSchemaname(), null, ValidateForm
							.getInstance().setRequired(false));
			this.schemaname.setSingle(true);
			
			// target table name
			this.tablename= new LabelInputMeta(id + ".tablename", "目标表名", null, null,
					"目标表名", jobEntry.getTablename(), null, ValidateForm
							.getInstance().setRequired(false));

			this.tablename.setSingle(true);
			
			// source file name
			this.filename= new LabelInputMeta(id + ".filename", "源文件名", null, null,
					"源文件名", jobEntry.getFilename(), null, ValidateForm
							.getInstance().setRequired(false));
			
			this.filename.setSingle(true);
			
			// localinfile 
			this.localinfile= new LabelInputMeta(id + ".localinfile", "本地", null,
					null, null,String.valueOf(jobEntry.isLocalInfile()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.localinfile.setSingle(true);
	        
			this.prorityvalue = new LabelSelectMeta(id + ".prorityvalue","优先级：",
					null,null,null,String.valueOf(jobEntry.prorityvalue),null,super.getLoglevel());

			this.prorityvalue.setSingle(true);
			
			 // fields terminated by 字段终止
			this.separator= new LabelInputMeta(id + ".separator", "终止字段", null, null,
					"字段终止", jobEntry.getLineterminated(), null, ValidateForm
							.getInstance().setRequired(false));
			this.separator.setSingle(true);
			
			 // fields enclosed by 封闭的领域
			this.enclosed= new LabelInputMeta(id + ".enclosed", "封闭的字段", null, null,
					"封闭的字段", jobEntry.getEnclosed(), null, ValidateForm
							.getInstance().setRequired(false));
			this.enclosed.setSingle(true);
			
			 // fields escaped by  逃出领域
			this.escaped= new LabelInputMeta(id + ".escaped", "溢出领域 ", null, null,
					"溢出领域", jobEntry.getEscaped(), null, ValidateForm
							.getInstance().setRequired(false));
			this.escaped   .setSingle(true);
			
			 // lines started by 线开始
			this.linestarted = new LabelInputMeta(id + ".linestarted", "开始行", null, null,
					"开始行", jobEntry.getLinestarted(), null, ValidateForm
							.getInstance().setRequired(false));
			this.linestarted .setSingle(true);
			
			 // lines terminated by
			this.lineterminated= new LabelInputMeta(id + ".lineterminated", "结束行", null, null,
					"结束行",  jobEntry.getRealLineterminated(), null, ValidateForm
							.getInstance().setRequired(false));
			this.lineterminated.setSingle(true);
			
			 // fields
			this.listattribut = new LabelInputMeta(id + ".listattribut", "领域", null, null,
					"领域",jobEntry.getRealListattribut(), null, ValidateForm
							.getInstance().setRequired(false));

			this.listattribut.setSingle(true);
			
			 //replace data 	 替代数据
			this.replacedata  = new LabelInputMeta(id + ".replacedata", "替代数据 ", null,
					null, null, String.valueOf(jobEntry.isReplacedata()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.replacedata .setSingle(true);
			
			 // ignore the first lines 忽视第一线
			this.ignorelines = new LabelInputMeta(id + ".ignorelines", "忽略第一行", null, null,
					"忽略第一行 ", jobEntry.getIgnorelines(), null, ValidateForm
							.getInstance().setRequired(false));
			this.ignorelines.setSingle(true);
			
			// result filenames  结果文件名
			this.filenames  = new ColumnFieldsetMeta(null, "结果文件名");
			this.filenames.setSingle(true);
			
		   //add file to result filenames	
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult", "将文件添加到结果文件名", null,
					null, null, String.valueOf(jobEntry.isAddFileToResult()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
	        this.addfiletoresult.setSingle(true);
	        
	        this.filenames.putFieldsetsContent(new BaseFormMeta[] {this.addfiletoresult});

        	// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.connection,this.schemaname 
					,this.tablename ,this.filename 
					,this.localinfile ,this.listattribut 
					,this.separator ,this.enclosed 
					,this.escaped ,this.linestarted 
					,this.lineterminated  ,this.prorityvalue  
					,this.replacedata  ,this.ignorelines  ,this.filenames 
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
