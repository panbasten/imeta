package com.panet.imeta.trans.steps.synchronizeaftermerge;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SynchronizeAfterMergeDialog extends BaseStepDialog implements
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
	 * 页签
	 */
	private MenuTabMeta meta;

	/***************************************************************************
	 * General
	 **************************************************************************/

	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connectionId;
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
	// private ButtonMeta browseBtn;

	/**
	 * Commit size
	 */
	private LabelInputMeta commitSize;

	/**
	 * Use batch update
	 */
	private LabelInputMeta useBatchUpdate;

	/**
	 * Tablename is defined in a field
	 */
	private LabelInputMeta tablenameInField;

	/**
	 * Tablename field
	 */
	private LabelInputMeta tablenameField;

	/**
	 * The key(s) to look up the value(s):
	 */
	private LabelGridMeta keys;

	/**
	 * update fields
	 */
	private LabelGridMeta values;
	private ButtonMeta getUpdateFields;

	/***************************************************************************
	 * Advanced
	 **************************************************************************/

	/**
	 * Operation
	 */
	private ColumnFieldsetMeta operation;

	/**
	 * Operation fieldname
	 */
	private LabelSelectMeta operationOrderField;

	/**
	 * Insert when value equal
	 */
	private LabelInputMeta OrderInsert;

	/**
	 * Update when value equal
	 */
	private LabelInputMeta OrderUpdate;

	/**
	 * Delect when value equal
	 */
	private LabelInputMeta OrderDelete;

	/**
	 * Perform lookup
	 */
	private LabelInputMeta performLookup;

	/**
	 * SQL
	 */
	// private ButtonMeta sqlBtn;
	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public SynchronizeAfterMergeDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SynchronizeAfterMergeMeta step = (SynchronizeAfterMergeMeta) super
					.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "普通", "高级" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0
			 ******************************************************************/

			// 数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId",
					"数据库连接", null, null, null, String.valueOf((step
							.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""), null, super
							.getConnectionLine());
			this.connectionId.setSingle(true);

			// Target schema 目标模式
			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "目标模式必须填写", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.schemaName.setSingle(true);

			// Target table 目标表
			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "目标表必须填写", step.getTableName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.tableName.setSingle(true);
			this.tableName.setDisabled(step.istablenameInField());

			// Commit size 提交数量
			this.commitSize = new LabelInputMeta(id + ".commitSize", "提交数量",
					null, null, "提交数量必须填写", String
							.valueOf(step.getCommitSize()), null, ValidateForm
							.getInstance().setRequired(true));
			this.commitSize.setSingle(true);

			// Use batch update 使用批处理更新
			this.useBatchUpdate = new LabelInputMeta(id + ".useBatchUpdate",
					"使用批处理更新", null, null, null, String.valueOf(step
							.useBatchUpdate()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.useBatchUpdate.setSingle(true);

			// Tablename is defined in a field 用字段获得表名
			this.tablenameInField = new LabelInputMeta(
					id + ".tablenameInField", "使用字段定义表名", null, null, null,
					String.valueOf(step.istablenameInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.tablenameInField.setSingle(true);
			this.tablenameInField.addClick("jQuery.imeta.steps.synchronizeaftermerge.listeners.tablenameInFieldListeners");

			// Tabelname field
			this.tablenameField = new LabelInputMeta(id + ".tablenameField",
					"表名字段", null, null, "Tablename field必须填写", step
							.gettablenameField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.tablenameField.setSingle(true);
			this.tablenameField.setDisabled(!step.istablenameInField());

			// The key(s) to look up the value(s):
			this.keys = new LabelGridMeta(id + "_keys", "通过关键字来查找结果：", 200);
			this.keys.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keys.keyId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_keys.keyLookup", "表字段",
							null, false, 120),
					(new GridHeaderDataMeta(id + "_keys.keyCondition", "比较符",
							null, false, 120)).setOptions(super
									.getOptionsByStringArrayWithNumberValue(
											SynchronizeAfterMergeMeta.fieldTypeDesc, false)),
					new GridHeaderDataMeta(id + "_keys.keyStream", "流字段1", null,
							false, 120),
					new GridHeaderDataMeta(id + "_keys.keyStream2", "流字段2",
							null, false, 120) });
			this.keys.setHasBottomBar(true);
			this.keys.setHasAdd(true, true,
					"jQuery.imeta.steps.synchronizeaftermerge.btn.keysAdd");
			this.keys.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.keys.setSingle(true);

			String[] field = step.getKeyLookup();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.keys.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getKeyLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getKeyCondition()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, step.getKeyStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getKeyStream2()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			DivMeta diva = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields(
					"jQuery.imeta.steps.synchronizeaftermerge.btn.getFields")
					.appendTo(diva);

			// Update fields
			this.values = new LabelGridMeta(id + "_values", "更新字段：", 200);
			this.values.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_values.valueId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_values.updateLookup", "表字段",
							null, false, 150),
					new GridHeaderDataMeta(id + "_values.updateStream", "流字段",
							null, false, 150),
					(new GridHeaderDataMeta(id + "_values.update", "更新", null,
							false, 150)).setOptions(super.getOptionsByYAndN(true)) });
			this.values.setHasBottomBar(true);
			this.values.setHasAdd(true, true,
					"jQuery.imeta.steps.synchronizeaftermerge.btn.valuesAdd");
			this.values.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.values.setSingle(true);

			String[] value = step.getUpdateLookup();
			if (value != null && value.length > 0)
				for (int i = 0; i < value.length; i++) {
					this.values.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getUpdateLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getUpdateStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getUpdate()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			DivMeta divb = new DivMeta(new NullSimpleFormDataMeta(), true);
			this.getUpdateFields = new ButtonMeta(id + ".btn.getUpdateFields",
					id + ".btn.getUpdateFields", "获取目标字段", "获取目标字段");
			this.getUpdateFields
					.addClick("jQuery.imeta.steps.synchronizeaftermerge.btn.getUpdateFields");
			this.getUpdateFields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getUpdateFields.putProperty("roName", super.getTransMeta()
					.getName());
			this.getUpdateFields
					.putProperty("elementName", super.getStepName());
			this.getUpdateFields.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			this.getUpdateFields.appendTo(divb);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.connectionId,
					this.schemaName, this.tableName, this.commitSize,
					this.useBatchUpdate, this.tablenameInField,
					this.tablenameField, this.keys, diva, this.values, divb });

			/*******************************************************************
			 * Advanced
			 ******************************************************************/

			// Operation
			this.operation = new ColumnFieldsetMeta(null, "操作");
			this.operation.setSingle(true);

			// Operation fieldname
			this.operationOrderField = new LabelSelectMeta(id
					+ ".operationFieldname", "操作字段", null, null, null, step
					.getOperationOrderField(), null, super.getPrevStepResultFields());
			this.operationOrderField.setSingle(true);
			this.operationOrderField.setHasEmpty(true);

			// Insert when value equal
			this.OrderInsert = new LabelInputMeta(id + ".OrderInsert",
					"当值相等时插入", null, null, "Insert when value equal必须填写", step
							.getOrderInsert(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.OrderInsert.setSingle(true);

			// Update when value equal
			this.OrderUpdate = new LabelInputMeta(id + ".OrderUpdate",
					"当值相等时更新", null, null, "Update when value equal必须填写", step
							.getOrderUpdate(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.OrderUpdate.setSingle(true);

			// Delect when value equal
			this.OrderDelete = new LabelInputMeta(id + ".OrderDelete",
					"当值相等时删除", null, null, "Delete when value equal必须填写", step
							.getOrderDelete(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.OrderDelete.setSingle(true);

			// Perform lookup
			this.performLookup = new LabelInputMeta(id + ".performLookup",
					"执行查找", null, null, null, String.valueOf(step
							.isPerformLookup()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.performLookup.setSingle(true);

			this.operation.putFieldsetsContent(new BaseFormMeta[] {
					this.operationOrderField, this.OrderInsert,
					this.OrderUpdate, this.OrderDelete, this.performLookup });
			this.meta.putTabContent(1, new BaseFormMeta[] { this.operation });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
