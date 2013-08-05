package com.panet.imeta.trans.steps.dimensionlookup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DimensionLookupDialog  extends BaseStepDialog implements
		StepDialogInterface {
	public DimensionLookupDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

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
	 * 更新维度吗？
	 */
	private LabelInputMeta update;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
	
	/**
	 * Target schema
	 */
	private LabelInputMeta schemaName;
	
	/**
	 * 目标表
	 */
	private LabelInputMeta tableName;
	
	/**
	 * 提交数量
	 */
	private LabelInputMeta commitSize;
	
	/**
	 * 缓存大小
	 */
	private LabelInputMeta cacheSize;
	
	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/**********************
	 * 关键字
	 **********************/
	/**
	 * 关键字段
	 */
	private LabelGridMeta keyWords;
	
	/***********************
	 * 字段
	 ***********************/
	/**
	 * 查询/更新字段
	 */
	private LabelGridMeta queryUpdateField;
	
	/**
	 * 代理关键字
	 */
	private LabelInputMeta keyField;
	private LabelInputMeta keyRename;
	
	/**
	 * 创建一个代理键
	 */
	private ColumnFieldsetMeta createDeputy;
	
	/**
	 * 使用表里的最大值
	 */
	private LabelInputMeta useTable;
	
	/**
	 * 使用Sequence
	 */
	private LabelInputMeta useSequence;
	private LabelInputMeta sequenceName;
	
	/**
	 * 使用字段增长字段
	 */
	private LabelInputMeta autoIncrement;

    /**
     * Version字段
     */
	private LabelInputMeta versionField;
	
	/**
	 * Steam日期字段
	 */
	private LabelInputMeta dateField;
	
	/**
	 * Date range start field
	 */
	private LabelInputMeta dateFrom;
	
	/**
	 * 最小年份
	 */
	private LabelInputMeta minYear;
	
	/**
	 * Table date range end
	 */
	private LabelInputMeta dateTo;
	
	/**
	 * Max.year
	 */
	private LabelInputMeta maxYear;

	
//	/**
//	 * SQL
//	 */
//	private ButtonMeta sqlBtn;
	
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			DimensionLookupMeta step = (DimensionLookupMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
			
			// 更新维度吗？
			this.update = new LabelInputMeta ( id + ".update", "更新维度吗？", null, null, 
					null, 
					String
					.valueOf(step.isUpdate()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.update.setSingle(true);
			
			// 数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null,
					String.valueOf(step.getDatabaseMeta()
							),
			null, super.getConnectionLine());
			this.connection.setSingle(true);
			
			// Target schema
			this.schemaName = new LabelInputMeta( id + ".schemaName", "目标类型", null, null,
					"Target schema必须填写", 
					String.valueOf(step.getSchemaName()), 
					null, ValidateForm.getInstance().setRequired(false));
			this.schemaName.setSingle(true);
			
			// 目标表
			this.tableName = new LabelInputMeta( id + ".tableName", "目标表", null, null,
					"目标表必须填写",
					String.valueOf(step.getTableName()), 
					null, ValidateForm.getInstance().setRequired(false));
			this.tableName.setSingle(true);
			
			// 提交数量
			this.commitSize = new LabelInputMeta ( id + ".commitSize", "提交数量", null, null,
					"提交数量必须填写", 
					String
					.valueOf(step.getCommitSize()),
					null, ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);
			
			// 缓存大小
			this.cacheSize = new LabelInputMeta ( id + ".cacheSize", "缓存大小(0=缓存所有)", null, null,
					"缓存大小必须填写", 
					String
					.valueOf(step.getCacheSize()),
					null, ValidateForm.getInstance().setRequired(false));
			this.cacheSize.setSingle(true);
	
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "关键字", "字段"});
			this.meta.setSingle(true);
			/*******************************************************************
			 * 标签0
			 ******************************************************************/

			this.keyWords = new LabelGridMeta( id + "_keys","关键字字段", 200);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] keyStreamNames = r.getFieldNames();
			GridHeaderDataMeta valueFields = new GridHeaderDataMeta(id
					+ "_keys.keyStream", "维字段", null, false, 80);
			valueFields.setOptions(super.getOptionsByStringArray(
					keyStreamNames, true));
			
			RowMetaInterface rV = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultValueNames = rV.getFieldNames();
			GridHeaderDataMeta keyLookup = new GridHeaderDataMeta(id
					+ "_keys.keyLookup", "流里字段", null, false, 80);
			keyLookup.setOptions(super.getOptionsByStringArray(
					resultValueNames, true));
			
			this.keyWords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keys.keysId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							valueFields,
							keyLookup
					});
			this.keyWords.setHasBottomBar(true);
			this.keyWords.setHasAdd(true, true,
					"jQuery.imeta.steps.dimensionlookup.btn.keyWordsAdd");
			this.keyWords.setHasDelete(true, true,"jQuery.imeta.parameter.fieldsDelete");
			this.keyWords.setSingle(true);
			
			String[] value = step.getKeyStream();
			if (value != null && value.length > 0)
				for (int i = 0; i < value.length; i++) {

					this.keyWords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, value[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, step.getKeyLookup()[i],
									GridCellDataMeta.CELL_TYPE_SELECT)
							});
				}
			this.meta.putTabContent(0, new BaseFormMeta[] { this.keyWords
			});
			
			/*****************************************
			 * 字段
			 *****************************************/
			
			this.queryUpdateField = new LabelGridMeta(id + "_fields", "查询/更新字段", 200);
			
			GridHeaderDataMeta fieldUpdateType = new GridHeaderDataMeta(id
					+ "_fields.fieldUpdate", "更新维类型", null, false, 100);
			fieldUpdateType.setOptions(super.getOptionsByStringArray(
					ValueMetaInterface.DIM_TYPE_UPDATE, false));
			
			this.queryUpdateField.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldsId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldStream", "维字段", null, false, 60),
					new GridHeaderDataMeta(id + "_fields.fieldLookup", "比较流字段", null, false, 100),
					fieldUpdateType
					});
		
			String[] values = step.getFieldStream();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.queryUpdateField.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldUpdate()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT)
							});
				}
			
			this.queryUpdateField.setHasBottomBar(true);
			this.queryUpdateField.setHasAdd(true, true,
					"jQuery.imeta.steps.dimensionlookup.btn.queryUpdateFieldAdd");
			this.queryUpdateField.setHasDelete(true, true,"jQuery.imeta.parameter.fieldsDelete");
			this.queryUpdateField.setSingle(true);
			
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.queryUpdateField
			});
			
			// 代理关键字
			this.keyField = new LabelInputMeta ( id + ".keyField", "代理关键字段", null, null,
					"代理关键字段必须填写",
					String.valueOf(step.getKeyField()), 
					null, ValidateForm.getInstance().setRequired(false));
			this.keyField.setSingle(false);
			this.keyRename = new LabelInputMeta ( id + ".keyRename", "新的名称", null, null,
					"新的名称必须填写",
					String.valueOf(step.getKeyRename()), 
					null, ValidateForm.getInstance().setRequired(false));
			this.keyRename.setSingle(false);
			// 创建一个代理键
			this.createDeputy = new ColumnFieldsetMeta(null, "创建一个代理键");
			this.createDeputy.setSingle(true);
			
			// 使用表里的最大值 + 1
			this.useTable = new LabelInputMeta ( id + ".techKeyCreation", "使用表里的最大值 + 1", null, null,
					null, step.CREATION_METHOD_TABLEMAX,
					InputDataMeta.INPUT_TYPE_RADIO,ValidateForm.getInstance().setRequired(false));
			this.useTable.setSingle(true);
			
			// 使用sequence
			this.useSequence = new LabelInputMeta ( id + ".techKeyCreation", "使用sequence", null, null,
					null, step.CREATION_METHOD_SEQUENCE,
					InputDataMeta.INPUT_TYPE_RADIO, ValidateForm.getInstance().setRequired(false));
			this.useSequence.setSingle(false);
			
			this.sequenceName = new LabelInputMeta ( id + ".sequenceName", null, null, null,
					null, step.getSequenceName(),
					null, ValidateForm.getInstance().setRequired(false));
			this.sequenceName.setSingle(false);
			
			// 使用自动增长字段
			this.autoIncrement = new LabelInputMeta ( id + ".techKeyCreation", "使用自动增长字段", null, null,
					null, 
					step.CREATION_METHOD_AUTOINC,
					InputDataMeta.INPUT_TYPE_RADIO, ValidateForm.getInstance().setRequired(false));
			this.autoIncrement.setSingle(true);
			
			this.createDeputy.putFieldsetsContent(new BaseFormMeta[] {
					this.useTable, this.useSequence, this.sequenceName, 
					this.autoIncrement });
			
			// Version 字段
			this.versionField = new LabelInputMeta ( id + ".versionField", "Version字段", null, null,
					"Version字段必须填写",
					String.valueOf(step.getVersionField()),
					null, ValidateForm.getInstance().setRequired(false));
			this.versionField.setSingle(true);
			
			// Stream日期字段
			this.dateField = new LabelInputMeta ( id + ".dateField", "Steam日期字段", null, null,
					"日期字段必须填写", 
					String.valueOf(step.getDateField()),
					null, ValidateForm.getInstance().setRequired(false));
			this.dateField.setSingle(true);
			
			// Date range start field
			this.dateFrom = new LabelInputMeta ( id + ".dateFrom", "日期开始范围", null, null, 
					"日期开始范围必须填写", 
					String.valueOf(step.getDateFrom()),
					null, ValidateForm.getInstance().setRequired(false) );
			this.dateFrom.setSingle(false);
			
			// 最小年份
			this.minYear = new LabelInputMeta ( id + ".minYear", "最小年份", null, null,
					"最小年份必须填写", 
					String
					.valueOf(step.getMinYear()),
					null, ValidateForm.getInstance().setRequired(false));
			this.minYear.setSingle(false);
			
			//Table dateRange end
			this.dateTo = new LabelInputMeta ( id + ".dateTo", "日期结束范围", null, null,
					"日期结束范围必须填写", 
					String.valueOf(step.getDateTo()),
					null, ValidateForm.getInstance().setRequired(false));
			this.dateTo.setSingle(false);
			
			// 最大年份
			this.maxYear = new LabelInputMeta ( id + ".maxYear", "最大年份", null, null,
					"最大年份必须填写",
					String
					.valueOf(step.getMaxYear()),
					null, ValidateForm.getInstance().setRequired(false));
			this.maxYear.setSingle(false);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.update,
					this.connection, this.schemaName, this.tableName,
					this.commitSize, this.cacheSize,
					this.meta,
					this.keyField, this.keyRename, this.createDeputy, this.versionField,
                    this.dateField, this.dateFrom, this.minYear,
                    this.dateTo, this.maxYear
					});

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getGetfields("jQuery.imeta.steps.update.btn.getfields")});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
