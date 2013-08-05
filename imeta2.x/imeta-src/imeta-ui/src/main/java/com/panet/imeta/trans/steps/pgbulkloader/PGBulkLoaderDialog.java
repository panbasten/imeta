package com.panet.imeta.trans.steps.pgbulkloader;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
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

public class PGBulkLoaderDialog extends BaseStepDialog implements
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
	 * Load action
	 */
	private LabelSelectMeta loadAction;

	/**
	 * DB Name Override
	 */
	private LabelInputMeta dbNameOverride;

	/**
	 * Enclosure
	 */
	private LabelInputMeta enclosure;

	/**
	 * Delimiter
	 */
	private LabelInputMeta delimiter;

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

	public PGBulkLoaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			PGBulkLoaderMeta step = (PGBulkLoaderMeta) super.getStep();

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

			// Target schema
			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "目标模式必须填写", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.schemaName.setSingle(true);

			// Target table
			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "目标表必须填写", step.getTableName(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.browseABtn = new ButtonMeta ( id + ".btn.browseABtn", id +
			// ".btn.browseABtn",
			// "浏览...", "浏览...");
			// this.targetTable.addButton( new ButtonMeta [] {
			// this.browseABtn});

			this.tableName.setSingle(true);

			// Path to the psql client
			this.PsqlPath = new LabelInputMeta(id + ".PsqlPath", "PQSL客户端路径",
					null, null, "PQSL客户端路径必须填写", step.getPsqlpath(), null,
					ValidateForm.getInstance().setRequired(true));
			// this.browseBBtn = new ButtonMeta ( id + ".btn.browseBBtn", id +
			// ".btn.browseBBtn",
			// "浏览...", "浏览...");
			// this.pathToThePsqlClient.addButton( new ButtonMeta[]{
			// this.browseBBtn});
			this.PsqlPath.setSingle(true);

			// Load action
			List<OptionDataMeta> optionsLoadAction = new ArrayList<OptionDataMeta>();
			optionsLoadAction.add(new OptionDataMeta("0", Messages
					.getString("PGBulkLoaderDialog.InsertLoadAction.Label")));
			optionsLoadAction.add(new OptionDataMeta("1", Messages
					.getString("PGBulkLoaderDialog.TruncateLoadAction.Label")));

			this.loadAction = new LabelSelectMeta(id + ".loadAction", "加载方式",
					null, null, null, step.getLoadAction(), null,
					optionsLoadAction);

			this.loadAction.setSingle(true);

			// DB Name Override
			this.dbNameOverride = new LabelInputMeta(id + ".dbNameOverride",
					"数据库名称重写", null, null, "数据库名称必须填写", step
							.getDbNameOverride(), null, ValidateForm
							.getInstance().setRequired(true));
			this.dbNameOverride.setSingle(true);

			// Enclosure
			this.enclosure = new LabelInputMeta(id + ".enclosure", "附文", null,
					null, "附文必须填写", step.getEnclosure(), null, ValidateForm
							.getInstance().setRequired(true));
			this.enclosure.setSingle(true);

			// Delimiter
			this.delimiter = new LabelInputMeta(id + ".delimiter", "定界符", null,
					null, "定界符必须填写", step.getDelimiter(), null, ValidateForm
							.getInstance().setRequired(true));
			this.delimiter.setSingle(true);

			// Fields to load
			this.fieldsToLoad = new LabelGridMeta(id + "_fieldsToLoad", "字段加载",
					200);
			this.fieldsToLoad.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldId", "#",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldTable",
							"表字段", null, false, 150),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldStream",
							"流字段", null, false, 150),
					(new GridHeaderDataMeta(id + "_fieldsToLoad.dateMask",
							"日期掩码", null, false, 150)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									PGBulkLoaderMeta.fieldTypeDesc, false)) });

			String[] fieldTable = step.getFieldTable();
			GridCellDataMeta table, stream, detemask;
			for (int i = 0; i < fieldTable.length; i++) {
				table = new GridCellDataMeta(null, step.getFieldTable()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				stream = new GridCellDataMeta(null, step.getFieldStream()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				detemask = new GridCellDataMeta(null, step.getDateMask()[i],
						GridCellDataMeta.CELL_TYPE_SELECT);
				this.fieldsToLoad.addRow(new Object[] { String.valueOf(i + 1),
						table, stream, detemask });
			}

			this.fieldsToLoad.setHasBottomBar(true);
			this.fieldsToLoad.setHasAdd(true, true,
					"jQuery.imeta.steps.pgbulkloader.btn.fieldAdd");
			this.fieldsToLoad.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fieldsToLoad.setSingle(true);

			// Get feilds
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.pgbulkloader.btn.getfields")
					.appendTo(fieldBtn);

			// 装载到form
			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name,
							this.connection, this.schemaName, this.tableName,
							this.PsqlPath, this.loadAction,
							this.dbNameOverride, this.enclosure,
							this.delimiter, this.fieldsToLoad, fieldBtn });

			// this.sqlBtn = new ButtonMeta ( id + ".btn.sqlBtn", id +
			// ".btn.sqlBtn",
			// "SQL", "SQL");

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
