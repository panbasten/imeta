package com.panet.imeta.trans.steps.tableoutput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TableOutputDialog extends BaseStepDialog implements
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
	 * 数据库连接
	 */
	private LabelSelectMeta connection;

	/**
	 * 目标模式
	 */
	private LabelInputMeta schemaName;

	/**
	 * 目标表
	 */
	private LabelInputMeta tablename;

	/**
	 * 提交记录数量
	 */
	private LabelInputMeta commitSize;

	/**
	 * 裁剪表
	 */
	private LabelInputMeta truncateTable;

	/**
	 * 特殊表
	 */
	private LabelInputMeta specifyFields;

	/**
	 * 忽略插入错误
	 */
	private LabelInputMeta ignoreErrors;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 使用批量插入
	 */
	private LabelInputMeta useBatchUpdate;

	/**
	 * 表分区数据
	 */
	private LabelInputMeta partitioningEnabled;

	/**
	 * 分区字段
	 */
	private LabelSelectMeta partitioningField;

	/**
	 * 每个月分区数据
	 */
	private LabelInputMeta partitioningMonthly;

	/**
	 * 每天分区数据
	 */
	private LabelInputMeta partitioningDaily;

	/**
	 * 表名定义在一个字段里
	 */
	private LabelInputMeta tableNameInField;

	/**
	 * 包含表名的字段
	 */
	private LabelSelectMeta tableNameField;

	/**
	 * 存储表名的字段
	 */
	private LabelInputMeta tableNameInTable;

	/**
	 * 返回一个自动产生的关键字
	 */
	private LabelInputMeta returningGeneratedKeys;

	/**
	 * 自动产生的关键字的字段的名称
	 */
	private LabelInputMeta generatedKeyField;

	/**
	 *表--插入的字段
	 */
	private LabelGridMeta fieldToInsert;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public TableOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			TableOutputMeta step = (TableOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到连接数据库
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());
			this.connection.setSingle(true);

			// 得到目标模式
			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "目标模式", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(false));
			this.schemaName.setSingle(true);

			// 目标表 + 浏览
			this.tablename = new LabelInputMeta(id + ".tablename", "目标表", null,
					null, "目标表", step.getTablename(), null, ValidateForm
							.getInstance().setRequired(false));

			this.tablename.setSingle(true);
			this.tablename.setDisabled(step.isTableNameInField());

			// 记录数量
			this.commitSize = new LabelInputMeta(id + ".commitSize", "提交记录数量",
					null, null, "提交记录数量", String.valueOf(step.getCommitSize()),
					null, ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);

			// 裁剪表
			this.truncateTable = new LabelInputMeta(id + ".truncateTable",
					"TRUNCATE表", null, null, null, String.valueOf(step
							.truncateTable()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.truncateTable.setDisabled(step.isPartitioningEnabled());

			// 忽略插入错误
			this.ignoreErrors = new LabelInputMeta(id + ".ignoreErrors",
					"忽略插入错误", null, null, null, String.valueOf(step
							.ignoreErrors()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.ignoreErrors.setDisabled(step.useBatchUpdate());

			// 自定义库表映射
			this.specifyFields = new LabelInputMeta(id + ".specifyFields",
					"自定义库表映射", null, null, null, String.valueOf(step
							.specifyFields()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			this.specifyFields
					.addClick("jQuery.imeta.steps.tableoutput.listeners.specifyFieldsListener");

			/*******************************************************************
			 * 标签开始
			 ******************************************************************/

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "主要选项", "数据库领域" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签1---------主要选项 main options
			 ******************************************************************/

			// 表分区数据
			this.partitioningEnabled = new LabelInputMeta(id
					+ ".partitioningEnabled", "表分区数据", null, null, null, String
					.valueOf(step.isPartitioningEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.partitioningEnabled
					.addClick("jQuery.imeta.steps.tableoutput.listeners.tableZoningListener");

			// 分区字段
			this.partitioningField = new LabelSelectMeta(id
					+ ".partitioningField", "分区字段", null, null, "分区字段", step
					.getPartitioningField(), null, super
					.getPrevStepResultFields());
			this.partitioningField.setSingle(true);
			this.partitioningField.setDisabled(!step.isPartitioningEnabled());

			// 每月分区数据
			this.partitioningMonthly = new LabelInputMeta(id
					+ ".partitioningMonthly", "每月分区数据", null, null, null,
					String.valueOf(step.isPartitioningMonthly()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.partitioningMonthly.setDisabled(!step.isPartitioningEnabled());

			// 每天分区数据
			this.partitioningDaily = new LabelInputMeta(id
					+ ".partitioningDaily", "每天分区数据", null, null, null, String
					.valueOf(step.isPartitioningDaily()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.partitioningDaily.setDisabled(!step.isPartitioningEnabled());

			// 使用批量插入
			this.useBatchUpdate = new LabelInputMeta(id + ".useBatchUpdate",
					"使用批量插入", null, null, null, String.valueOf(step
							.useBatchUpdate()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.useBatchUpdate
					.addClick("jQuery.imeta.steps.tableoutput.listeners.bulkInsertListener");
			this.useBatchUpdate.setDisabled(step.isReturningGeneratedKeys());

			// 表名定义在一个字段里面
			this.tableNameInField = new LabelInputMeta(
					id + ".tableNameInField", "表名定义在一个字段里", null, null, null,
					String.valueOf(step.isTableNameInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.tableNameInField
					.addClick("jQuery.imeta.steps.tableoutput.listeners.inOneFieldListener");

			// 包含表名的字段
			this.tableNameField = new LabelSelectMeta(id + ".tableNameField",
					"包含表名的字段", null, null, "包含表名的字段", step.getTableNameField(),
					null, super.getPrevStepResultFields());
			this.tableNameField.setSingle(true);
			this.tableNameField.setDisabled(step.isTableNameInField());

			// 存储表名字段
			this.tableNameInTable = new LabelInputMeta(
					id + ".tableNameInTable", "存储表名字段", null, null, null,
					String.valueOf(step.isTableNameInTable()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.tableNameInTable.setDisabled(step.specifyFields());

			// 返回一个自动产生的关键字
			this.returningGeneratedKeys = new LabelInputMeta(id
					+ ".returningGeneratedKeys", "返回一个自动产生的关键字", null, null,
					null, String.valueOf(step.isReturningGeneratedKeys()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.returningGeneratedKeys
					.addClick("jQuery.imeta.steps.tableoutput.listeners.returnKeyListener");

			// 自动产生关键字的字段名称
			this.generatedKeyField = new LabelInputMeta(id
					+ ".generatedKeyField", "返回一个自动产生的关键字", null, null, "分区字段",
					step.getGeneratedKeyField(), null, ValidateForm
							.getInstance().setRequired(false));
			this.generatedKeyField.setSingle(true);
			this.generatedKeyField.setDisabled(step.isReturningGeneratedKeys());

			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.partitioningEnabled, this.partitioningField,
					this.partitioningMonthly, this.partitioningDaily,
					this.useBatchUpdate, this.tableNameInField,
					this.tableNameField, this.tableNameInTable,
					this.returningGeneratedKeys, this.generatedKeyField });

			/*******************************************************************
			 * 标签2--------- 数据库领域---database fields
			 ******************************************************************/

			// database fields

			this.fieldToInsert = new LabelGridMeta(id + "_fieldToInsert",
					"插入的字段", 200);

			this.fieldToInsert.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldToInsert.fieldId", "#",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fieldToInsert.fieldDatabase",
							"表字段", null, false, 100),
					new GridHeaderDataMeta(id + "_fieldToInsert.fieldStream",
							"流里字段", null, false, 100) });

			String[] fileName = step.getFieldDatabase();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					this.fieldToInsert.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getFieldDatabase()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT)

					});
				}
			}

			this.fieldToInsert.setSingle(true);
			this.fieldToInsert.setHasBottomBar(true);

			this.fieldToInsert.setHasAdd(true, true,
					"jQuery.imeta.steps.tableoutput.btn.fieldAdd");
			this.fieldToInsert.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			this.meta.putTabContent(1,
					new BaseFormMeta[] { this.fieldToInsert });

			// ----------------->

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tablename,
					this.commitSize, this.truncateTable, this.ignoreErrors,
					this.specifyFields, this.meta });

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
