package com.panet.imeta.trans.steps.gpbulkloader;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
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

public class GPBulkLoaderDialog extends BaseStepDialog implements
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
	 * Path to the psql client
	 */
	private LabelInputMeta PsqlPath;
	// private ButtonMeta browseBBtn;

	/**
	 * Load Method
	 */
	private LabelSelectMeta loadMethod;

	/**
	 * Load action
	 */
	private LabelSelectMeta loadAction;

	/**
	 * Maximum errors
	 */
	private LabelInputMeta maxErrors;

	/**
	 * DB Name Override
	 */
	private LabelInputMeta dbNameOverride;

	/**
	 * Control file
	 */
	private LabelInputMeta controlFile;
	// private ButtonMeta browseCBtn;

	/**
	 * Data fiel
	 */
	private LabelInputMeta dataFile;
	// private ButtonMeta browseDBtn;

	/**
	 * Log file
	 */
	private LabelInputMeta logFile;
	// private ButtonMeta browseEBtn;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Erase cfg/dat files after
	 */
	private LabelInputMeta eraseFiles;

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

	public GPBulkLoaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			GPBulkLoaderMeta step = (GPBulkLoaderMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名称
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

			// Target schema
			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "目标模式必须填写", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.schemaName.setSingle(true);

			// Target table
			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "Target table必须填写", step.getTableName(), null,
					ValidateForm.getInstance().setRequired(true));
			// this.browseABtn = new ButtonMeta ( id + ".btn.browseABtn", id +
			// ".btn.browseABtn",
			// "浏览...", "浏览...");
			// this.targetTable.addButton( new ButtonMeta [] {
			// this.browseABtn});

			this.tableName.setSingle(true);

			// Path to the psql client
			this.PsqlPath = new LabelInputMeta(id + ".PsqlPath", "PSQL客户端路径",
					null, null, "PSQL客户端路径必须填写", step.getPsqlpath(), null,
					ValidateForm.getInstance().setRequired(true));
			// this.browseBBtn = new ButtonMeta ( id + ".btn.browseBBtn", id +
			// ".btn.browseBBtn",
			// "浏览...", "浏览...");
			// this.pathToThePsqlClient.addButton( new ButtonMeta[]{
			// this.browseBBtn});
			this.PsqlPath.setSingle(true);

			// Load Method
			List<OptionDataMeta> optionsLoadMethod = new ArrayList<OptionDataMeta>();
			optionsLoadMethod.add(new OptionDataMeta("0", Messages
					.getString("GPBulkLoaderDialog.AutoEndLoadMethod.Label")));
			optionsLoadMethod.add(new OptionDataMeta("1", Messages
					.getString("GPBulkLoaderDialog.ManualLoadMethod.Label")));

			this.loadMethod = new LabelSelectMeta(id + ".loadMethod", "加载方法",
					null, null, null, step.getLoadMethod(), null,
					optionsLoadMethod);

			this.loadMethod.setSingle(true);

			// Load action
			List<OptionDataMeta> optionsLoadAction = new ArrayList<OptionDataMeta>();
			optionsLoadAction.add(new OptionDataMeta("0", Messages
					.getString("GPBulkLoaderDialog.InsertLoadAction.Label")));
			optionsLoadAction.add(new OptionDataMeta("1", Messages
					.getString("GPBulkLoaderDialog.TruncateLoadAction.Label")));

			this.loadAction = new LabelSelectMeta(id + ".loadAction", "加载操作",
					null, null, null, step.getLoadAction(), null,
					optionsLoadAction);

			this.loadAction.setSingle(true);

			// Maximum errors
			this.maxErrors = new LabelInputMeta(id + ".maxErrors", "最大误差",
					null, null, "最大误差必须填写",
					String.valueOf(step.getMaxErrors()), null, ValidateForm
							.getInstance().setRequired(true));
			this.maxErrors.setSingle(true);

			// DB Name Override
			this.dbNameOverride = new LabelInputMeta(id + ".dbNameOverride",
					"数据库名称重写", null, null, "数据库名称必须填写", step
							.getDbNameOverride(), null, ValidateForm
							.getInstance().setRequired(true));
			this.dbNameOverride.setSingle(true);

			// Control file
			this.controlFile = new LabelInputMeta(id + ".controlFile", "控制文件",
					null, null, "控制文件必须填写", step.getControlFile(), null,
					ValidateForm.getInstance().setRequired(true));
			// this.browseCBtn = new ButtonMeta ( id + ".btn.browseCBtn", id +
			// ".btn.browseCBtn",
			// "浏览...", "浏览...");
			// this.controlFile.addButton( new ButtonMeta [] {
			// this.browseCBtn});

			this.controlFile.setSingle(true);

			// Data file
			this.dataFile = new LabelInputMeta(id + ".dataFile", "数据文件", null,
					null, "数据文件必须填写", step.getDataFile(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.browseDBtn = new ButtonMeta ( id + ".btn.browseDBtn", id +
			// ".btn.browseDBtn",
			// "浏览...", "浏览...");
			// this.dataFile.addButton( new ButtonMeta [] { this.browseDBtn});

			this.dataFile.setSingle(true);

			// Log file
			this.logFile = new LabelInputMeta(id + ".logFile", "日志文件", null,
					null, "日志文件必须填写", step.getLogFile(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.browseEBtn = new ButtonMeta ( id + ".btn.browseEBtn", id +
			// ".btn.browseEBtn",
			// "浏览...", "浏览...");
			// this.logFile.addButton( new ButtonMeta [] { this.browseEBtn});

			this.logFile.setSingle(true);

			// Encoding
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, super.getEncoding());

			this.encoding.setSingle(true);

			// Erase cfg/dat files after use
			this.eraseFiles = new LabelInputMeta(id + ".eraseFiles",
					"在使用后除去cfg/dat文件", null, null, null, String.valueOf(step
							.isEraseFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.eraseFiles.setSingle(true);

			// Fields to load
			this.fieldsToLoad = new LabelGridMeta(id + "_fieldsToLoad", "加载字段",
					200);
			this.fieldsToLoad.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldTable",
							"表字段", null, false, 150),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldStream",
							"流字段", null, false, 150),
					(new GridHeaderDataMeta(id + "_fieldsToLoad.dateMask",
							"日期掩码", null, false, 150)).setOptions(super
									.getOptionsByStringArrayWithNumberValue(GPBulkLoaderMeta.fieldTypeDesc, false)) });

			String[] fieldTable = step.getFieldTable();
			GridCellDataMeta table, stream, datemask;
			for (int i = 0; i < fieldTable.length; i++) {
				table = new GridCellDataMeta(null, step.getFieldTable()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				stream = new GridCellDataMeta(null, step.getFieldStream()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				datemask = new GridCellDataMeta(null, step.getDateMask()[i],
						GridCellDataMeta.CELL_TYPE_SELECT);
				this.fieldsToLoad.addRow(new Object[] { String.valueOf(i + 1),
						table, stream, datemask });
			}
			this.fieldsToLoad.setHasBottomBar(true);
			this.fieldsToLoad.setHasAdd(true, true,
					"jQuery.imeta.steps.GPbulkloader.btn.fieldAdd");
			this.fieldsToLoad.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fieldsToLoad.setSingle(true);

			// Get feilds
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.GPbulkloader.btn.getfields")
					.appendTo(fieldBtn);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tableName,
					this.PsqlPath, this.loadMethod, this.loadAction,
					this.maxErrors, this.dbNameOverride, this.controlFile,
					this.dataFile, this.logFile, this.encoding,
					this.eraseFiles, this.fieldsToLoad, fieldBtn });

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
