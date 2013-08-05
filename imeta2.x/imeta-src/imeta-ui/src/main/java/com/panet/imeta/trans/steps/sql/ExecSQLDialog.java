package com.panet.imeta.trans.steps.sql;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnDiv.ColumnDivMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ExecSQLDialog extends BaseStepDialog implements
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
	private LabelSelectMeta connectionId;
	
	/**
	 * 数据库脚本执行
	 */
	private LabelTextareaMeta sql;
	
	private ColumnFieldsetMeta columnF;
	
	/**
	 * 
	 */
//	private ColumnFieldsetMeta fields;

	/**
	 * 包含插入状态的字段
	 */
	private LabelInputMeta insertField;

	/**
	 * 包含更新状态的字段
	 */
	private LabelInputMeta updateField;
	
	/**
	 * 包含删除状态的字段
	 */
	private LabelInputMeta deleteField;

	/**
	 * 包含读状态的字段
	 */
	private LabelInputMeta readField;
	
	/**
	 * 执行每一行？
	 */
	private LabelInputMeta executedEachInputRow;

	/**
	 * 变量替换
	 */
	private LabelInputMeta replaceVariables;

	/**
	 * 参数
	 */
	private LabelGridMeta fields;
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ExecSQLDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			ExecSQLMeta step = (ExecSQLMeta)super.getStep();
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"执行SQL脚本", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId", "数据库连接",
					null, null, null, 
					String
					.valueOf((step.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""), null, this.getConnectionLine());
    		this.connectionId.setSingle(true);

			// 得到数据库脚本执行

			this.sql = new LabelTextareaMeta(id + ".sql", "SQL脚本执行，问号将被参数代替", null, null,
					null, step.getSql(), 2, null);
			this.sql.setSingle(true);
			
			sql = new LabelTextareaMeta(id + ".js", "JavaScript编辑", null, null,
					"JavaScript编辑", null, 2, ValidateForm.getInstance()
							.setRequired(false));
			sql.setLayout(LabelTextareaMeta.LAYOUT_COLUMN);

			sql.setSingle(true);
			sql.setWidth(99);
			
			this.columnF = new ColumnFieldsetMeta(null, "");
			this.columnF.setSingle(true);
			
			// 执行每一行？
			this.executedEachInputRow = new LabelInputMeta(id + ".executedEachInputRow",
					"执行每一行？", null, null, null, String.valueOf(step.isExecutedEachInputRow()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.executedEachInputRow.setSingle(true);
			
			// 变量替换
			this.replaceVariables = new LabelInputMeta(id + ".replaceVariables",
					"变量替换", null, null, null, String.valueOf(step.isReplaceVariables()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.replaceVariables.setSingle(true);
			
			// 参数
			ColumnDivMeta columnDivMeta = new ColumnDivMeta();
			this.fields = new LabelGridMeta(id + "_fields", "参数:", 200);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.arguments", "作为参数的字段", null, false, 80);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta });
			this.fields.setSingle(false);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.sql.btn.argsAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			String[] values = step.getArguments();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			
			columnDivMeta.putDivContent(new BaseFormMeta[] { this.fields });
			columnDivMeta.getRoot().setStyle("float", "left");
			
			// 得到包含插入状态的字段
			this.insertField = new LabelInputMeta(id + ".insertField", "包含插入状态的字段", null,
					null, "包含插入状态的字段", step.getInsertField(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.insertField.setSingle(false);

			// 得到包含更新状态的字段
			this.updateField = new LabelInputMeta(id + ".updateField", "包含更新状态的字段",
					null, null, "包含更新状态的字段", step.getUpdateField(), null, ValidateForm
							.getInstance().setRequired(true));
            this.updateField.setSingle(false);
            
			// 得到包含删除状态的字段
			this.deleteField= new LabelInputMeta(id + ".deleteField", "包含删除状态的字段",
					null, null, "包含删除状态的字段", step.getDeleteField(), null, ValidateForm
							.getInstance().setRequired(true));
            this.deleteField.setSingle(false);
            
			// 得到包含读状态的字段
			this.readField = new LabelInputMeta(id + ".readField", "包含读状态的字段",
					null, null, "包含读状态的字段", step.getReadField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.readField.setSingle(false);
			
			this.columnF.putFieldsetsContent(new BaseFormMeta[] {
					this.executedEachInputRow, this.replaceVariables,
					columnDivMeta,
					this.insertField,
					this.updateField,this.deleteField,this.readField});
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connectionId, this.sql, this.columnF
					//fields
					 });
		
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(), 
					super.getGetfields("jQuery.imeta.steps.sql.btn.getfields")});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
