package com.panet.imeta.trans.steps.mondrianinput;

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

public class MondrianInputDialog extends BaseStepDialog implements
StepDialogInterface{
	
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 步骤名称
	 */
	private LabelInputMeta stepName;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connection;
//	private ButtonMeta dbConEdit;
//	private ButtonMeta dbConNew;
	
	/**
	 * MDX Query
	 */
	private LabelTextareaMeta sql;
	
	/**
	 * Replace variable in
	 */
	private LabelInputMeta variableReplacementActive;
	
	/**
	 * 目录位置
	 */
	private LabelInputMeta catalog;
	
	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public MondrianInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			MondrianInputMeta step = (MondrianInputMeta) super.getStep();
			

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			//Step name
			this.stepName = new LabelInputMeta(id + ".stepName", "步骤名", null, null,
					null, super.getStepMeta().getName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.stepName.setSingle(true);
			
			//数据库连接
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());
			this.connection.setSingle(true);
//			this.dbConEdit = new ButtonMeta(id+ ".btn.dbConEdit", id +
//					".btn.dbConEdit","编辑...", "编辑...");
//			this.dbConNew = new ButtonMeta(id+ ".btn.dbConNew", id +
//					".btn.dbConNew","新建...", "新建...");
//			this.connection.addButton(new ButtonMeta[] {this.dbConEdit,
//					this.dbConNew});
			
			//MDX Query
			this.sql = new LabelTextareaMeta(id + ".sql", "MDX查询", null, 
					null, null, String.valueOf(step.getSQL()), 6, null);
			this.sql.setSingle(true);
			
			//Replace variable in
			this.variableReplacementActive = new LabelInputMeta(id + ".variableReplacementActive", 
					"替换变量", null, null,null, String
					.valueOf(step.isVariableReplacementActive()), InputDataMeta.
					INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(true));
			
			//目录位置
			this.catalog = new LabelInputMeta(id + ".catalog", 
					"目录位置", null, null,null,step.getCatalog(), null, null);
			this.catalog.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {this.stepName,
					this.connection,this.sql,this.variableReplacementActive,
					this.catalog});

			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
