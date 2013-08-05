package com.panet.imeta.job.entries.mssqlbulkload;

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
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryMssqlBulkLoadDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	// 批量导入---mssql

	/**
	 * job entry name
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * target table
	 */
	private ColumnFieldsetMeta targetTable;

	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;

	/**
	 * Target schema
	 */
	private LabelInputMeta schemaname;

	/**
	 * Target table name
	 */
	private LabelInputMeta tablename;

	/**
	 * Truncate table
	 */
	private LabelInputMeta truncate;

	// -----------------------DATA FILE-------

	/**
	 * DATA FILE
	 */
	private ColumnFieldsetMeta dataFile;

	/**
	 * Source File name
	 */
	private LabelInputMeta filename;

	/**
	 * DATA file type
	 */
	private LabelSelectMeta datafiletype;

	/**
	 * Fields Terminated by
	 */
	private LabelInputMeta fieldterminator;

	/**
	 * Row Terminated by
	 */
	private LabelInputMeta lineterminated;

	// -----页签2

	/**
	 * Codepage
	 */
	private LabelSelectMeta codepage;

	/**
	 * Specific Codepage
	 */
	private LabelInputMeta specificcodepage;

	/**
	 * Format File
	 */
	private LabelInputMeta formatfilename;

	/**
	 * Fire Triggers
	 */
	private LabelInputMeta firetriggers;

	/**
	 * Check Cinstraints
	 */
	private LabelInputMeta checkconstraints;

	/**
	 * Keep Nulls
	 */
	private LabelInputMeta keepnulls;
	
	/**
	 * Keep Identity
	 */
	private LabelInputMeta keepidentity;

	/**
	 * Tablock
	 */
	private LabelInputMeta tablock;

	/**
	 * Start At Lines
	 */
	private LabelInputMeta startfile;

	/**
	 * End At Lines
	 */
	private LabelInputMeta endfile;

	/**
	 * Order By
	 */
	private LabelInputMeta orderby;

	/**
	 * Direction
	 */
	private LabelSelectMeta orderdirection;

	/**
	 * Error File
	 */
	private LabelInputMeta errorfilename;

	/**
	 * Add Date Time
	 */
	private LabelInputMeta adddatetime;

	/**
	 * Max Errors
	 */
	private LabelInputMeta maxerrors;

	/**
	 * Batch Size
	 */
	private LabelInputMeta batchsize;

	/**
	 * Row Per Batch
	 */
	private LabelInputMeta rowsperbatch;

	/**
	 * Result filenames
	 */
	private ColumnFieldsetMeta filenames;

	/**
	 * Add file to result filenames
	 */
	private LabelInputMeta addfiletoresult;

	/**
	 * 初始化
	 * 
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryMssqlBulkLoadDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryMssqlBulkLoad jobEntry = (JobEntryMssqlBulkLoad) super
					.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "高级" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 页签 General
			 ******************************************************************/

			// -------------------Target table-----------
			this.targetTable = new ColumnFieldsetMeta(null, "目标表");
			this.targetTable.setSingle(true);

			//数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null, String
					.valueOf((jobEntry.getDatabase() != null) ? jobEntry
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connection.setSingle(true);

			// Target schema
			this.schemaname = new LabelInputMeta(id + ".schemaname",
					"目标纲要", null, null, "目标纲要：", jobEntry.getSchemaname(), null,
					ValidateForm.getInstance().setRequired(false));
			this.schemaname.setSingle(true);

			// Target table
			this.tablename = new LabelInputMeta(id + ".tablename", "目标表",
					null, null, "目标表：", jobEntry.getTablename(), null,
					ValidateForm.getInstance().setRequired(false));
			this.tablename.setSingle(true);


			// Truncate table
			this.truncate = new LabelInputMeta(id + ".truncate",
					"截断表", null, null, null, String.valueOf(jobEntry
							.isTruncate()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.truncate.setSingle(true);

			// -----------Target table-------
			this.targetTable.putFieldsetsContent(new BaseFormMeta[] {
					this.connection, this.schemaname, this.tablename,
					this.truncate });

			// -------------------Data File-----------
			this.dataFile = new ColumnFieldsetMeta(null, "数据文件");
			this.dataFile.setSingle(true);

			// Source file name
			this.filename = new LabelInputMeta(id + ".filename",
					"源文件名", null, null, "源文件名", jobEntry.getFilename(), null,
					ValidateForm.getInstance().setRequired(false));

			this.filename.setSingle(true);

			// datafiletype
			 List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("datafiletype", " char"));
			options.add(new OptionDataMeta("datafiletype", " native"));
			options.add(new OptionDataMeta("datafiletype", " widechar"));
			options.add(new OptionDataMeta("datafiletype", " widenative"));

			this.datafiletype = new LabelSelectMeta(id + ".datafiletype", "类型：", null, null,
					null, jobEntry.getDataFileType(), null, options);
			this.datafiletype.setSingle(true);

			// ------Data File-------
			this.dataFile.putFieldsetsContent(new BaseFormMeta[] {
					this.filename, this.datafiletype });

			// Fields terminated by
			this.fieldterminator = new LabelInputMeta(
					id + ".fieldterminator", "终止文件", null, null, " 终止文件",
					jobEntry.getFieldTerminator(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fieldterminator.setSingle(true);

			// Row terminated by
			this.lineterminated = new LabelInputMeta(id + ".lineterminated",
					"行被终止 ", null, null, "行被终止", jobEntry.getLineterminated(),
					null, ValidateForm.getInstance().setRequired(false));
			this.lineterminated.setSingle(true);

			// 页签里面添加内容

			this.meta.putTabContent(0, new BaseFormMeta[] { this.targetTable,
					this.dataFile, this.fieldterminator, this.lineterminated });

			/*******************************************************************
			 * 页签 Advanced
			 ******************************************************************/

			// Codepage
			List<OptionDataMeta> options1 = new ArrayList<OptionDataMeta>();
			options1 = new ArrayList<OptionDataMeta>();
			options1.add(new OptionDataMeta("codepage", "OEM"));
			options1.add(new OptionDataMeta("codepage", "ACP"));
			options1.add(new OptionDataMeta("codepage", "RAW"));
			options1.add(new OptionDataMeta("codepage", "Specific"));

			this.codepage = new LabelSelectMeta(id + ".codepage", "代码页：", null,
					null, null, jobEntry.getCodePage(), null,options1);
			this.codepage.setSingle(true);

			// Specific Codepage
			this.specificcodepage = new LabelInputMeta(
					id + ".specificcodepage", "特定代码页 ", null, null, "特定代码页 ",
					jobEntry.getSpecificCodePage(), null, ValidateForm
							.getInstance().setRequired(false));
			this.specificcodepage.setSingle(true);

			// Format file
			this.formatfilename = new LabelInputMeta(id + ".formatfilename", "文件格式",
					null, null, "文件格式", jobEntry.getFormatFilename(), null,
					ValidateForm.getInstance().setRequired(false));
			
			this.formatfilename.setSingle(true);

			// Fire triggers
			this.firetriggers = new LabelInputMeta(id + ".firetriggers",
					"触发器 ", null, null, null, String.valueOf(jobEntry
							.isFireTriggers()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.firetriggers.setSingle(true);

			// Check cinstraints
			this.checkconstraints = new LabelInputMeta(
					id + ".checkconstraints", "返回唯一约束 ", null, null, null,
					String.valueOf(jobEntry.isCheckConstraints()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			
			this.checkconstraints.setSingle(true);

			// Keep Nulls
			this.keepnulls = new LabelInputMeta(id + ".keepnulls", "保持为空",
					null, null, null, String.valueOf(jobEntry.isKeepNulls()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.keepnulls.setSingle(true);

			// 保持身份
			this.keepidentity  = new LabelInputMeta(id + ".keepidentity", "保持身份 ", null,
					null, null, String.valueOf(jobEntry.isKeepIdentity()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.keepidentity.setSingle(true);
			
			// Tablock 表锁定
			this.tablock = new LabelInputMeta(id + ".tablock", "表锁定 ", null,
					null, null, String.valueOf(jobEntry.isTablock()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			
			this.tablock.setSingle(true);
			
			// Start at lines 开始行
			this.startfile = new LabelInputMeta(id + ".startfile",
					"开始行  ", null, null, "0", String.valueOf(jobEntry
							.getStartFile()), null, ValidateForm.getInstance()
							.setRequired(false));
			this.startfile.setSingle(true);

			// End at lines 结束行
			this.endfile = new LabelInputMeta(id + ".endfile", "结束行",
					null, null, "0", String.valueOf(jobEntry.getEndFile()),
					null, ValidateForm.getInstance().setRequired(false));
			this.endfile.setSingle(true);

			// Order by 命令
			this.orderby = new LabelInputMeta(id + ".orderby", "命令", null,
					null, "命令", jobEntry.getOrderBy(), null, ValidateForm
							.getInstance().setRequired(false));

			this.orderby.setSingle(true);

			// orderdirection 方向
			options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta("orderdirection", "ASC"));
			options.add(new OptionDataMeta("orderdirection", " DESC"));

			this.orderdirection = new LabelSelectMeta(id + ".orderdirection", "排列顺序：", null,
					null, null, jobEntry.getOrderDirection(), null, options);
			this.orderdirection.setSingle(true);

			// Error file 错误文件
			this.errorfilename = new LabelInputMeta(id + ".errorfilename", "错误文件",
					null, null, "错误文件", jobEntry.getErrorFilename(), null,
					ValidateForm.getInstance().setRequired(false));
			this.errorfilename.setSingle(true);

			// Add date time 添加日期时间
			this.adddatetime = new LabelInputMeta(id + ".adddatetime",
					"添加日期时间 ", null, null, null, String.valueOf(jobEntry
							.isAddDatetime()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.adddatetime.setSingle(true);

			// Max errors 最大错误
			this.maxerrors = new LabelInputMeta(id + ".maxerrors", "最大错误",
					null, null, "最大错误", String.valueOf(jobEntry.getMaxErrors()),
					null, ValidateForm.getInstance().setRequired(false));
			this.maxerrors.setSingle(true);

			// Batch size 批量大小
			this.batchsize = new LabelInputMeta(id + ".batchsize", "批量大小",
					null, null, "批量大小", String.valueOf(jobEntry.getBatchSize()),
					null, ValidateForm.getInstance().setRequired(false));
			this.batchsize.setSingle(true);

			// Row per batch 连续每批
			this.rowsperbatch = new LabelInputMeta(id + ".rowsperbatch", "连续每批",
					null, null, "连续每批", String.valueOf(jobEntry
							.getRowsPerBatch()), null, ValidateForm
							.getInstance().setRequired(false));
			this.rowsperbatch.setSingle(true);

			// -----Result filenames 结果文件名
			this.filenames = new ColumnFieldsetMeta(null, "结果文件名");
			this.filenames.setSingle(true);

			// Add file to result filenames 将文件添加到结果文件名
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult", "将文件添加到结果文件名",
					null, null, null, String.valueOf(jobEntry
							.isAddFileToResult()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addfiletoresult.setSingle(true);

			this.filenames
					.putFieldsetsContent(new BaseFormMeta[] { this.addfiletoresult });

			// 页签里面添加内容

			this.meta.putTabContent(1, new BaseFormMeta[] { 
					this.codepage, this.specificcodepage, this.formatfilename,
					this.firetriggers, this.checkconstraints, this.keepnulls,
					this.tablock, this.startfile, this.endfile,
					this.orderby, this.orderdirection, this.errorfilename,
					this.adddatetime, this.maxerrors, this.batchsize,
					this.rowsperbatch, this.filenames });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
