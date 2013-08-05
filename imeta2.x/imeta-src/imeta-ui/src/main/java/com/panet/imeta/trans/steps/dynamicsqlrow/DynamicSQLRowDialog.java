package com.panet.imeta.trans.steps.dynamicsqlrow;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DynamicSQLRowDialog extends BaseStepDialog implements
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
//	private ButtonMeta editBtn;
//	private ButtonMeta newBtn;
	
	/**
	 * SQL field name
	 */
	private LabelSelectMeta sqlfieldname;
	
	/**
	 * Number of rows to return
	 */
	private LabelInputMeta rowLimit;
	
	/**
	 * Outer join
	 */
	private LabelInputMeta outerJoin;
	
	/**
	 * Replace variables
	 */
	private LabelInputMeta replacevars;
	
	/**
	 * Query only on parameters change
	 */
	private LabelInputMeta queryonlyonchange;
	
	/**
	 * Template SQL
	 */
	private LabelTextareaMeta sql;
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public DynamicSQLRowDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			DynamicSQLRowMeta step = (DynamicSQLRowMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 数据库连接
		    this.connection = new LabelSelectMeta ( id + ".connection", "数据库连接",
		    		null, null, null,
		    		String
					.valueOf((step.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""),
			null, super.getConnectionLine());
//		    this.editBtn = new ButtonMeta ( id + ".btn.editBtn", id + ".btn.editBtn",
//		    		"编辑...", "编辑...");
//		    this.newBtn = new ButtonMeta ( id + ".btn.newBtn", id + ".btn.newBtn",
//		    		"新建...", "新建...");
//		    this.connectionId.addButton( new ButtonMeta [] { this.editBtn, this.newBtn});
		    this.connection.setSingle(true);
		    
		    // SQL field name
		    this.sqlfieldname = new LabelSelectMeta(id + ".sqlfieldname","加载操作",
					null,null,null,step.getSQLFieldName(),null,null);
		    this.sqlfieldname.setSingle(true);
		    
		    // Number of rows to return 返回行数量
		    this.rowLimit = new LabelInputMeta ( id + ".rowLimit", "返回行数量", null, null,
		    		"返回行数量必须填写", 
		    		String.valueOf(step.getRowLimit()),
					null, ValidateForm.getInstance().setRequired(false));
		    this.rowLimit.setSingle(true);
		    
		    // Outer join? 外部连接？
		    this.outerJoin = new LabelInputMeta ( id + ".outerJoin", "外部联接？", null, null, 
		    		null,
		    		String.valueOf(step.isOuterJoin()),
			InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
		    this.outerJoin.setSingle(true);
		    
		    // Replace variables 替换变量
		    this.replacevars = new LabelInputMeta ( id + ".replacevars", "替换变量", null, null,
		    		null,
		    		String.valueOf(step.isVariableReplace()),
			InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
		    this.replacevars.setSingle(true);
		    
		    // Query only on parameters change  查询只对参数变化
		    this.queryonlyonchange = new LabelInputMeta ( id + ".queryonlyonchange", "查询只对参数变化", null, null,
		    		null,
		    		String.valueOf(step.isQueryOnlyOnChange()),
			InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
		    this.queryonlyonchange.setSingle(true);
		    
		    // Template SQL 模版的SQL
		    this.sql = new LabelTextareaMeta(id + ".sql", "模板的SQL（检索元数据）", null, null,
					null, step.getSql(), 3, null);
			
			this.sql.setSingle(true);
			
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					 this.connection, this.sqlfieldname, this.rowLimit,
					 this.outerJoin, this.replacevars, this.queryonlyonchange,
					 this.sql
                     });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
