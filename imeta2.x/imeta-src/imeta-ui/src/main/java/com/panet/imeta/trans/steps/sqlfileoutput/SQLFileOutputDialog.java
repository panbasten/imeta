package com.panet.imeta.trans.steps.sqlfileoutput;

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
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SQLFileOutputDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	//General-----------------------------------------------------

	/**
	 * Connection
	 */
	private ColumnFieldsetMeta conn;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;

	/**
	 * Target scheme
	 */
	private LabelInputMeta schemaName;
	
	/**
	 * Target table
	 */
	private LabelInputMeta tablename;

	/**
	 * Output File
	 */
	private ColumnFieldsetMeta outputFile;
	
	/**
	 * Add create table statement
	 */
	private LabelInputMeta createTable;
	
	/**
	 * Add truncate table statement
	 */
	private LabelInputMeta truncateTable;
	
	/**
	 * Start new line for each
	 */
	private LabelInputMeta StartNewLine;
	
	/**
	 * filename
	 */
	private LabelInputMeta fileName;

//	private ButtonMeta browseBtn;

	/**
	 * Create Parent folder
	 */
	private LabelInputMeta createparentfolder;
	
	/**
	 * Do not open create at start
	 */
	private LabelInputMeta DoNotOpenNewFileInit;
	
	/**
	 * 扩展名
	 */
	private LabelInputMeta extension;
	
	/**
	 * Include stepnr in filename
	 */
	private LabelInputMeta stepNrInFilename;
	
	/**
	 * Include date in filename
	 */
	private LabelInputMeta dateInFilename;
	
	/**
	 * Include time in filename
	 */
	private LabelInputMeta timeInFilename;
	
	/**
	 * Append
	 */
	private LabelInputMeta fileAppended;
	
	/**
	 * Split every ... rows
	 */
	private LabelInputMeta splitEvery;
	
	/**
	 * Add File to result
	 */
	private LabelInputMeta AddToResult;

	//Content-----------------------------------------------------
	/**
	 * Data Format
	 */
	private LabelSelectMeta dateformat;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public SQLFileOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SQLFileOutputMeta step = (SQLFileOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "内容" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0--General
			 ******************************************************************/
			// Connection
			this.conn = new ColumnFieldsetMeta(null, "连接");
			this.conn.setSingle(true);

			// 得到连接数据库
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());

			this.connection.setSingle(true);

			// Target scheme
			this.schemaName = new LabelInputMeta(id + ".schemaName",
					"目标计划", null, null, null, step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(false));
			this.schemaName.setSingle(true);
			// Target table
			this.tablename = new LabelInputMeta(id + ".tablename", "目标表",
					null, null, null, step.getTablename(), null, ValidateForm
							.getInstance().setRequired(false));
			
//			this.browseBtn0 = new ButtonMeta(id + ".btn.browseBtn0", id
//					+ ".btn.browseBtn0", "浏览", "浏览");
//			this.targetTable.addButton(new ButtonMeta[] { this.browseBtn0 });
			this.tablename.setSingle(true);

			this.conn.putFieldsetsContent(new BaseFormMeta[] { this.connection,
					this.schemaName, this.tablename });

			// Output File
			this.outputFile = new ColumnFieldsetMeta(null, "输出文件");
			this.outputFile.setSingle(true);

			// Add create table statement
			this.createTable = new LabelInputMeta(id + ".createTable",
					"包括内容类型", null, null, null, String.valueOf(step
							.createTable()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.createTable
					.addClick("jQuery.imeta.steps.sqlfileoutput.listeners.addCreattable");

			// Add truncate table statement
			this.truncateTable = new LabelInputMeta(
					id + ".truncateTable", "新增截断表声明", null, null, null,
					String.valueOf(step.truncateTable()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.truncateTable.setDisabled(step.createTable());

			// Start new line for each
			this.StartNewLine = new LabelInputMeta(id + ".StartNewLine", "启动新的生产线", null,
					null, null, String.valueOf(step.StartNewLine()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			// Filename
			this.fileName = new LabelInputMeta(id + ".fileName", "文件名", null,
					null, null, step.getFileName(), null, ValidateForm
							.getInstance().setRequired(false));

//			this.browseBtn = new ButtonMeta(id + ".btn.browseBtn", id
//					+ ".btn.browseBtn", "浏览", "浏览");
//			this.fileName.addButton(new ButtonMeta[] { this.browseBtn });
			
			this.fileName.setSingle(true);

			// Create Parent folder
			this.createparentfolder = new LabelInputMeta(id
					+ ".createparentfolder", "创建父文件夹", null, null, null, String
					.valueOf(step.isCreateParentFolder()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Do not open create at start
			this.DoNotOpenNewFileInit = new LabelInputMeta(
					id + ".DoNotOpenNewFileInit", "不要打开创建启动", null, null, null,
					String.valueOf(step.isDoNotOpenNewFileInit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// 扩展名
			this.extension = new LabelInputMeta(id + ".extension", "扩展名", null,
					null, null, step.getExtension(), null, ValidateForm
							.getInstance().setRequired(false));
			this.extension.setSingle(true);

			// Include stepnr in filename
			this.stepNrInFilename = new LabelInputMeta(id + ".stepNrInFilename",
					"在文件名中包含", null, null, null, String.valueOf(step
							.isStepNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Include date in filename
			this.dateInFilename = new LabelInputMeta(id + ".dateInFilename",
					"在文件名中包含日期", null, null, null, String.valueOf(step
							.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Include time in filename
			this.timeInFilename = new LabelInputMeta(id + ".timeInFilename",
					"在文件名中包含当前时间", null, null, null, String.valueOf(step
							.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Append
			this.fileAppended = new LabelInputMeta(id + ".fileAppended", "Append", null,
					null, null, String.valueOf(step.isFileAppended()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Split every ... rows
			this.splitEvery = new LabelInputMeta(id + ".splitEvery", "分开......行",
					null, null, null, String.valueOf(step.getSplitEvery()),
					null, null);
			this.splitEvery.setSingle(true);

			// Add File to result
			this.AddToResult = new LabelInputMeta(id + ".AddToResult",
					"将文件添加到结果", null, null, null, String.valueOf(step
							.AddToResult()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));

			this.outputFile.putFieldsetsContent(new BaseFormMeta[] {
					this.createTable, this.truncateTable, this.StartNewLine,
					this.fileName, this.createparentfolder,
					this.DoNotOpenNewFileInit, this.extension, this.stepNrInFilename,
					this.dateInFilename, this.timeInFilename, this.fileAppended,
					this.splitEvery, this.AddToResult });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.conn,
					this.outputFile });
			/*******************************************************************
			 * 标签1--Content
			 ******************************************************************/
			
			//dataFormat
			this.dateformat = new LabelSelectMeta(id + ".dateformat",
					"日期时间格式", null, null, null, step.getDateFormat(), null,
					super.getDateFormats());
			this.dateformat.setSingle(true);

			//Encoding

			this.encoding = new LabelSelectMeta(id + ".encoding", "编码",
					null, null, null, step.getEncoding(), null, super
							.getEncoding());
			this.encoding.setSingle(true);

			this.meta.putTabContent(1, new BaseFormMeta[] { this.dateformat,
					this.encoding });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
