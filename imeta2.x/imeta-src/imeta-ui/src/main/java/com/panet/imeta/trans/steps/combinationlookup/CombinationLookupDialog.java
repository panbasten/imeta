package com.panet.imeta.trans.steps.combinationlookup;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class CombinationLookupDialog  extends BaseStepDialog implements
		StepDialogInterface {
	public CombinationLookupDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

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
	
	/**
	 * Target schema
	 */
	private LabelInputMeta schemaName;
	
	/**
	 * 目标表
	 */
	private LabelInputMeta tablename;
	
	/**
	 * 提交数量
	 */
	private LabelInputMeta commitSize;
	
	/**
	 * 缓存大小
	 */
	private LabelInputMeta cacheSize;
	
	/**
	 * 关键字段
	 */
	private LabelGridMeta keyWords;
	
	/**
	 * 代理关键字
	 */
	private LabelInputMeta technicalKeyField;
	
	/**
	 * 代理键的创建y
	 */
	private ColumnFieldsetMeta createDeputy;
	
	/**
	 * 使用表里的最大值
	 */
	private LabelInputMeta useAutoinc;
	
	/**
	 * 使用Sequence
	 */
	private LabelInputMeta useSequence;
	private LabelInputMeta sequenceFrom;
	
	/**
	 * 使用自动增长字段
	 */
	private LabelInputMeta useAddField;
	
	/**
	 * 移除查询字段
	 */
	private LabelInputMeta replaceFields;
	
	/**
	 * 使用hashcode
	 */
	private LabelInputMeta useHash;
	
	/**
	 * Hashcode field in table
	 */
	private LabelInputMeta hashField;

	/**
	 * 获取字段
	 */
	private ButtonMeta getfields;
	
//	/**
//	 * SQL
//	 */
//	private ButtonMeta sqlBtn;
	
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			CombinationLookupMeta step = (CombinationLookupMeta)super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);
			
			// 数据库连接
			this.connection = new LabelSelectMeta(id + ".connection","数据库连接",
					null,null,null,String
					.valueOf((step.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""),
					null,super.getConnectionLine());
			this.connection.setSingle(true);
			
			// Target schema
			this.schemaName = new LabelInputMeta( id + ".schemaName", "目标类型", null, null,
					"Target schema必须填写", String.valueOf(step.getSchemaName()), null, ValidateForm.getInstance().setRequired(false));
			this.schemaName.setSingle(true);
			 
			// 目标表
			this.tablename = new LabelInputMeta( id + ".tablename", "目标表", null, null,
					"目标表必须填写", String.valueOf(step.getTablename()), null, ValidateForm.getInstance().setRequired(false));
			this.tablename.setSingle(true);
			
			// 提交数量
			this.commitSize = new LabelInputMeta ( id + ".commitSize", "提交数量", null, null,
					"提交数量必须填写", String.valueOf(step.getCommitSize()), null, ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);
			
			// 缓存大小
			this.cacheSize = new LabelInputMeta ( id + ".cacheSize", "缓存大小", null, null,
					"缓存大小必须填写", String.valueOf(step.getCacheSize()), null, ValidateForm.getInstance().setRequired(false));
			this.cacheSize.setSingle(true);
			
			// 关键字

			this.keyWords = new LabelGridMeta(id + "_keyWords", null, 200 ,0);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] keyLookupNames = r.getFieldNames();
			GridHeaderDataMeta keyLookup = new GridHeaderDataMeta(id
					+ "_keyWords.keyLookup", "流里字段", null, false, 80);
			keyLookup.setOptions(super.getOptionsByStringArray(
					keyLookupNames, true));
			
			RowMetaInterface rV = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] keyFieldNames = rV.getFieldNames();
			GridHeaderDataMeta keyField = new GridHeaderDataMeta(id
					+ "_keyWords.keyField", "维字段", null, false, 60);
			keyField.setOptions(super.getOptionsByStringArray(
					keyFieldNames, true));
			
			this.keyWords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keyWords.keysId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							keyField,
					        keyLookup
					});
			this.keyWords.setSingle(true);
			this.keyWords.setHasBottomBar(true);
			this.keyWords.setHasAdd(true, true,
					"jQuery.imeta.steps.combinationlookup.btn.keyWordsAdd");
			this.keyWords.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
			
			String[] values = step.getKeyField();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.keyWords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getKeyField()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step.getKeyLookup()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT)
							});
				}
			
			// 代理关键字
			this.technicalKeyField = new LabelInputMeta ( id + ".technicalKeyField", "代理关键字", null, null,
					"代理关键字必须填写", String.valueOf(step.getTechnicalKeyField()), null, ValidateForm.getInstance().setRequired(false));
			this.technicalKeyField.setSingle(true);
			
			// 代理键的创建y
			this.createDeputy = new ColumnFieldsetMeta(null, "代理键的创建");
			this.createDeputy.setSingle(true);
			
			// 使用表里的最大值 + 1
			this.useAutoinc = new LabelInputMeta ( id + ".techKeyCreation", " 使用表里的最大值 + 1", null, null,
					null, step.CREATION_METHOD_TABLEMAX,
					InputDataMeta.INPUT_TYPE_RADIO,ValidateForm.getInstance().setRequired(false)
					);
			this.useAutoinc.setSingle(true);
			
			// 使用sequence
			this.useSequence = new LabelInputMeta ( id + ".techKeyCreation", "使用sequence", null, null,
					null,  step.CREATION_METHOD_SEQUENCE, 
					InputDataMeta.INPUT_TYPE_RADIO, ValidateForm.getInstance().setRequired(false));
			this.useSequence.setSingle(false);
			
			this.sequenceFrom = new LabelInputMeta ( id + ".sequenceFrom", "sequence:", null, null,
					null, step.getSequenceFrom(), null, ValidateForm.getInstance().setRequired(false));
			this.sequenceFrom.setSingle(false);
			
			// 使用自动增长字段
			this.useAddField = new LabelInputMeta ( id + ".techKeyCreation", "使用自动增长字段", null, null,
					null, step.CREATION_METHOD_AUTOINC, 
					InputDataMeta.INPUT_TYPE_RADIO,ValidateForm.getInstance().setRequired(false));
			this.useAddField.setSingle(true);
			
			this.createDeputy.putFieldsetsContent(new BaseFormMeta[] {
					this.useAutoinc, this.useSequence, this.sequenceFrom, 
					this.useAddField });
			
			// 移除查询字段
		    this.replaceFields = new LabelInputMeta ( id + ".replaceFields", "移除查询字段？", null, null,
		    		null, String.valueOf(step.replaceFields()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
		    this.replaceFields.setSingle(true);
		    
		    // 使用hashcode
		    this.useHash = new LabelInputMeta ( id + ".useHash", "使用hashcode?", null,null,
		    		null, String.valueOf(step.useHash()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
		    this.useHash.setSingle(true);
		    this.useHash.addClick("jQuery.imeta.steps.combinationlookup.listeners.useHash");
		    
		    // Hashcode field in table
            this.hashField = new LabelInputMeta ( id + ".hashField", "Hashcode字段表", null, null,
            		"Hashcode字段表必须填写", step.getHashField(), null, ValidateForm.getInstance().setRequired(false));
            this.hashField.setSingle(true);
            this.hashField.setDisabled(!step.useHash());
		    
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tablename,
					this.commitSize, this.cacheSize, this.keyWords,
					this.technicalKeyField, this.createDeputy, this.replaceFields,
                    this.useHash, this.hashField
					});

			// 确定，取消  
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });
			
			this.getfields = new ButtonMeta ( id + ".btn.getfields", id + ".btn.getfields", 
					"获取字段", "获取字段");
			this.getfields.addClick("jQuery.imeta.steps.combinationlookup.btn.getfields");
			this.getfields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getfields
					.putProperty("roName", super.getTransMeta().getName());
			this.getfields.putProperty("elementName", super.getStepName());
			
//			this.sqlBtn = new ButtonMeta ( id + ".btn.sqlBtn", id + ".btn.sqlBtn",
//					"SQL", "SQL");
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.getfields });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
