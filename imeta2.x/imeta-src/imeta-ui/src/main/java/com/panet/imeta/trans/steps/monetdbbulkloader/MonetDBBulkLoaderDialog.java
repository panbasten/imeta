package com.panet.imeta.trans.steps.monetdbbulkloader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MonetDBBulkLoaderDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * Step name
	 */
	private LabelInputMeta name;

	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
	// private ButtonMeta editBtn;
	// private ButtonMeta newBtn;

	/**
	 * Target schema
	 */
	private LabelInputMeta schemaName;

	/**
	 * Target table
	 */
	private LabelInputMeta tableName;
	// private ButtonMeta browseABtn;

	/**
	 * Path to the mclient
	 */
	private LabelInputMeta mClientPath;
	// private ButtonMeta browseBBtn;

	/**
	 * Buffer size(rows)
	 */
	private LabelInputMeta bufferSize;

	/**
	 * Log file
	 */
	private LabelInputMeta logFile;
	// private ButtonMeta browseCBtn;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Fields to load
	 */
	private LabelGridMeta fieldsToLoad;

	/**
	 * SQL
	 */
	// private ButtonMeta sqlBtn;
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */

	public MonetDBBulkLoaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			MonetDBBulkLoaderMeta step = (MonetDBBulkLoaderMeta) super
					.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 数据库连接
			this.connection = new LabelSelectMeta(id + ".connection", "数据库连接",
					null, null, null, String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());

			// this.editBtn = new ButtonMeta(id + ".btn.editBtn", id
			// + ".btn.editBtn", "编辑...", "编辑...");
			// this.newBtn = new ButtonMeta(id + ".btn.newBtn", id
			// + ".btn.newBtn", "新建...", "新建...");
			//			
			// this.dbConnect.addButton(new ButtonMeta[] {
			// this.editBtn,this.newBtn});

			this.connection.setSingle(true);

			// Target schema 目标模式
			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "目标模式必须填写", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.schemaName.setSingle(true);

			// Target table 目标表
			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "目标表必须填写", step.getTableName(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.browseABtn = new ButtonMeta ( id + ".btn.browseABtn", id +
			// ".btn.browseABtn",
			// "浏览...", "浏览...");
			// this.targetTable.addButton( new ButtonMeta [] {
			// this.browseABtn});

			this.tableName.setSingle(true);

			// Path to the mclient mclient路径
			this.mClientPath = new LabelInputMeta(id + ".mClientPath",
					"mclient路径", null, null, "mclient路径必须填写", step
							.getMClientPath(), null, ValidateForm.getInstance()
							.setRequired(true));
			// this.browseBBtn = new ButtonMeta ( id + ".btn.browseBBtn", id +
			// ".btn.browseBBtn",
			// "浏览...", "浏览...");
			// this.pathToTheMclient.addButton( new ButtonMeta[]{
			// this.browseBBtn});
			this.mClientPath.setSingle(true);

			// Buffer size 缓冲区大小
			this.bufferSize = new LabelInputMeta(id + ".bufferSize",
					"缓冲区大小（行）", null, null, "缓冲区大小必须填写", step.getBufferSize(),
					null, ValidateForm.getInstance().setRequired(true));
			this.bufferSize.setSingle(true);

			// Log file 日志文件
			this.logFile = new LabelInputMeta(id + ".logFile", "日志文件", null,
					null, "日志文件必须填写", step.getLogFile(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.browseCBtn = new ButtonMeta ( id + ".btn.browseCBtn", id +
			// ".btn.browseCBtn",
			// "浏览...", "浏览...");
			// this.logFile.addButton( new ButtonMeta [] { this.browseCBtn});
			this.logFile.setSingle(true);

			// encoding 编码
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, super.getEncoding());

			this.encoding.setSingle(true);

			// Fields to load 加载字段
			this.fieldsToLoad = new LabelGridMeta(id + "_fieldsToLoad", "加载字段",
					200);
			this.fieldsToLoad.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldId", "#",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldTable",
							"表字段", null, false, 150),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldStream",
							"流字段", null, false, 150),
					(new GridHeaderDataMeta(id + "_fieldsToLoad.fieldFormatOk",
							"字段格式正确", null, false, 150)).setOptions(super
									.getOptionsByYAndN(true)) });

			String[] fieldTable = step.getFieldTable();
			GridCellDataMeta table, stream, formatOk;
			for (int i = 0; i < fieldTable.length; i++) {
				table = new GridCellDataMeta(null, step.getFieldTable()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				stream = new GridCellDataMeta(null, step.getFieldStream()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				formatOk = new GridCellDataMeta(null, String.valueOf(step
						.getFieldFormatOk()[i]),
						GridCellDataMeta.CELL_TYPE_SELECT);
				this.fieldsToLoad.addRow(new Object[] { String.valueOf(i + 1),
						table, stream, formatOk });
			}

			this.fieldsToLoad.setHasBottomBar(true);
			this.fieldsToLoad.setHasAdd(true, true,
					"jQuery.imeta.steps.monetDBbulkloader.btn.fieldAdd");
			this.fieldsToLoad.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fieldsToLoad.setSingle(true);

			// Get feilds
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields(
					"jQuery.imeta.steps.monetDBbulkloader.btn.getfields")
					.appendTo(fieldBtn);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tableName,
					this.mClientPath, this.bufferSize, this.logFile,
					this.encoding, this.fieldsToLoad, fieldBtn });

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
