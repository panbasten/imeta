package com.panet.imeta.trans.steps.databaselookup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DatabaseLookupDialog extends BaseStepDialog implements
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

	// private ButtonMeta editconncet;
	
	// private ButtonMeta newconncet;

	/**
	 * 查找模式
	 */
	private LabelInputMeta schemaName;

	/**
	 * 查询的表
	 */
	private LabelInputMeta tablename;

	/**
	 * 使用缓存
	 */
	private LabelInputMeta cached;

	/**
	 * 缓存大小
	 */
	private LabelInputMeta cacheSize;

	/**
	 * 从表中加载所有数据
	 */
	private LabelInputMeta loadingAllDataInCache;

	/**
	 * 用来查询所需的关键字
	 */
	private LabelGridMeta keywords;
	/**
	 * 查询表返回的值
	 */
	private LabelGridMeta queryvalue;
	/**
	 * 如果查询失败，不要忽略这一行
	 */
	private LabelInputMeta eatingRowOnLookupFailure;
	/**
	 * 失去多个结果
	 */
	private LabelInputMeta failingOnMultipleResults;
	/**
	 * 排序
	 */
	private LabelInputMeta orderByClause;

	/**
	 * 获取查询字段
	 */
	private ButtonMeta getQueryValue;

	public DatabaseLookupDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			DatabaseLookupMeta step = (DatabaseLookupMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"数据库查询", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到数据库连接
			this.connection = new LabelSelectMeta(id + ".connection", "数据库连接",
					null, null, null, String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());
			// this.editconncet = new ButtonMeta(id + ".btn.editconncet", id
			// + ".btn.editconncet", "编辑", "编辑");
			// this.newconncet = new ButtonMeta(id + ".btn.newconncet", id
			// + ".btn.newconncet", "新建", "新建");
			//
			// this.dbconncet.addButton(new ButtonMeta[] { this.editconncet,
			// this.newconncet });

			this.connection.setSingle(true);

			// 得到查找模式

			this.schemaName = new LabelInputMeta(id + ".schemaName", "查找模式",
					null, null, "查找模式", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.schemaName.setSingle(true);

			// 查询的表

			this.tablename = new LabelInputMeta(id + ".tablename", "查询的表",
					null, null, "查询的表", step.getTablename(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.skim = new ButtonMeta(id + ".btn.skim", id + ".btn.skim",
			// "浏览", "浏览");
			//
			// this.obtable.addButton(new ButtonMeta[] { this.skim });
			this.tablename.setSingle(true);
			// 使用缓存
			this.cached = new LabelInputMeta(id + ".cached", "使用缓存", null,
					null, null, String.valueOf(step.isCached()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.cached.setSingle(false);
			this.cached
					.addClick("jQuery.imeta.steps.databaselookup.listeners.cachedListeners");

			// 得到缓存大小
			this.cacheSize = new LabelInputMeta(id + ".cacheSize", "缓存大小",
					null, null, "缓存大小", String.valueOf(step.getCacheSize()),
					null, ValidateForm.getInstance().setRequired(true));
			this.cacheSize.setSingle(true);
			this.cacheSize.setDisabled(!step.isCached());

			// 从表中加载所有数据
			this.loadingAllDataInCache = new LabelInputMeta(id
					+ ".loadingAllDataInCache", "从表中加载所有数据", null, null, null,
					String.valueOf(step.isLoadingAllDataInCache()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.loadingAllDataInCache.setSingle(false);
			this.loadingAllDataInCache.setDisabled(!step.isCached());

			// 得到查询所需的关键字

			this.keywords = new LabelGridMeta(id + "_keywords", "查询所需的关键字：",
					100);
			this.keywords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keywords.keywordsId", "#",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_keywords.tableKeyField",
							"表字段", null, false, 100)).setOptions(super
									.getOptionsByStringArray(super.getTransMeta()
											.getPrevStepFields(super.getStepMeta())
											.getFieldNames(), true)),
					(new GridHeaderDataMeta(id + "_keywords.keyCondition", "比较操作符",
							null, false, 100)).setOptions(super
									.getOptionsByStringArrayWithNumberValue(
											DatabaseLookupMeta.fieldTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_keywords.streamKeyField1",
							"字段1", null, false, 100)).setOptions(super
									.getOptionsByStringArray(super.getTransMeta()
											.getPrevStepFields(super.getStepMeta())
											.getFieldNames(), true)),
					(new GridHeaderDataMeta(id + "_keywords.streamKeyField2",
							"字段2", null, false, 100)).setOptions(super
									.getOptionsByStringArray(super.getTransMeta()
											.getPrevStepFields(super.getStepMeta())
											.getFieldNames(), true)) });
			this.keywords.setHasBottomBar(true);
			this.keywords.setHasAdd(true, true,
					"jQuery.imeta.steps.databaselookup.keywords.btn.fieldAdd");
			this.keywords.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.keywords.setSingle(true);

			String[] tableKeyField = step.getTableKeyField();
			if (tableKeyField != null && tableKeyField.length > 0) {
				for (int i = 0; i < tableKeyField.length; i++) {
					this.keywords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getTableKeyField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getKeyCondition()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, step
									.getStreamKeyField1()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getStreamKeyField2()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			// 查询表返回的值

			this.queryvalue = new LabelGridMeta(id + "_queryvalue", "查询表返回的值：",
					100);
			this.queryvalue.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_queryvalue.queryvalueId",
							"#", null, false, 50),
					(new GridHeaderDataMeta(
							id + "_queryvalue.returnValueField", "字段", null,
							false, 100)).setOptions(super
							.getOptionsByStringArray(super.getTransMeta()
									.getPrevStepFields(super.getStepMeta())
									.getFieldNames(), true)),
					new GridHeaderDataMeta(id
							+ "_queryvalue.returnValueNewName", "新的名称", null,
							false, 100),
					new GridHeaderDataMeta(id
							+ "_queryvalue.returnValueDefault", "默认", null,
							false, 100),
					(new GridHeaderDataMeta(id
							+ "_queryvalue.returnValueDefaultType", "类型", null,
							false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)) });
			this.queryvalue.setHasBottomBar(true);
			this.queryvalue
					.setHasAdd(true, true,
							"jQuery.imeta.steps.databaselookup.queryvalue.btn.fieldAdd");
			this.queryvalue.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.queryvalue.setSingle(true);

			String[] returnValueField = step.getReturnValueField();
			if (returnValueField != null && returnValueField.length > 0) {
				for (int i = 0; i < returnValueField.length; i++) {
					this.queryvalue.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step
									.getReturnValueField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getReturnValueNewName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getReturnValueDefault()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getReturnValueDefaultType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			}

			// 如果查询失败，不要忽略这一行
			this.eatingRowOnLookupFailure = new LabelInputMeta(id
					+ ".eatingRowOnLookupFailure", "如果查询失败，不要忽略这一行", null,
					null, null, String.valueOf(step
							.isEatingRowOnLookupFailure()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.eatingRowOnLookupFailure.setSingle(true);

			// 失去多个结果
			this.failingOnMultipleResults = new LabelInputMeta(id
					+ ".failingOnMultipleResults", "失去多个结果", null, null, null,
					String.valueOf(step.isFailingOnMultipleResults()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.failingOnMultipleResults.setSingle(true);
			this.failingOnMultipleResults.setDisabled(step.isCached());
			this.failingOnMultipleResults
					.addClick("jQuery.imeta.steps.databaselookup.listeners.resultsListeners");
			// 排序
			this.orderByClause = new LabelInputMeta(id + ".orderByClause",
					"排序", null, null, "排序", step.getOrderByClause(), null,
					null);
			this.orderByClause.setSingle(true);
			this.orderByClause.setDisabled(step.isFailingOnMultipleResults());

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tablename,
					this.cached, this.cacheSize, this.loadingAllDataInCache,
					this.keywords, this.queryvalue,
					this.eatingRowOnLookupFailure,
					this.failingOnMultipleResults, this.orderByClause });

			this.getQueryValue = new ButtonMeta(id + ".btn.getQueryValue", id
					+ ".btn.getQueryValue", "获取查询字段", "获取查询字段");
			this.getQueryValue
					.addClick("jQuery.imeta.steps.databaselookup.btn.getqueryvalue");
			this.getQueryValue.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getQueryValue.putProperty("roName", super.getTransMeta()
					.getName());
			this.getQueryValue.putProperty("elementName", super.getStepName());
			this.getQueryValue.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							super.getCancelBtn(),
							super
									.getGetfields("jQuery.imeta.steps.databaselookup.btn.getkeywords"),
							this.getQueryValue });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
