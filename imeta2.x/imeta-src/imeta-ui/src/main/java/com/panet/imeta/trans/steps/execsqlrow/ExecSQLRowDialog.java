package com.panet.imeta.trans.steps.execsqlrow;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ExecSQLRowDialog extends BaseStepDialog implements
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
	private LabelSelectMeta connectionId;
//	private ButtonMeta editBtn;
//	private ButtonMeta newBtn;

	/**
	 * Commit
	 */
	private LabelInputMeta commitSize;
	
	/**
	 * SQL field name SQL字段名称
	 */
	private LabelSelectMeta sqlField;
	
	/**
	 * Output fields
	 */
	private ColumnFieldsetMeta outputfields;

	/**
	 * Field to contain insert stats
	 */
	private LabelInputMeta insertField;
	
	/**
	 * Field to contain Update
	 */
	private LabelInputMeta updateField;
	
	/**
	 * Field to contain Delete stats
	 */
	private LabelInputMeta deleteField;
	
	/**
	 * Field to contain Read stats
	 */
	private LabelInputMeta readField;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ExecSQLRowDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ExecSQLRowMeta step = (ExecSQLRowMeta)super.getStep();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			//数据库连接
			this.connectionId= new LabelSelectMeta(id + ".connectionId","数据库连接",
					null,null,String
					.valueOf((step.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""),null,
			null, super.getConnectionLine());
			
//			this.editBtn = new ButtonMeta(id + ".btn.editBtn", id
//					+ ".btn.editBtn", "编辑...", "编辑...");
//			this.newBtn = new ButtonMeta(id + ".btn.newBtn", id
//					+ ".btn.newBtn", "新建...", "新建...");
//			
//			this.connectionId.addButton(new ButtonMeta[] { this.editBtn,this.newBtn});
			
			this.connectionId.setSingle(true);
			
			// Commit
			this.commitSize = new LabelInputMeta ( id + ".commitSize", "约束", null, null,
					"约束必须填写",
					String.valueOf(step.getCommitSize()),
					null, ValidateForm.getInstance().setRequired(true));
			this.commitSize.setSingle(true);
			
			// SQL field name SQL字段名称
			this.sqlField= new LabelSelectMeta(id + "sqlField","SQL字段名称",
					null,null,null,
					step.getSqlFieldName(),
			null, null);
		
			this.sqlField.setSingle(true);
			
			// Output fields 输出字段
			this.outputfields = new ColumnFieldsetMeta(null, "输出字段");
			this.outputfields.setSingle(true);
			
			// Field to contain insert stats 字段包含插入统计
			this.insertField = new LabelInputMeta(id + ".insertField", "字段包含插入统计", null, null,
					"字段包含插入统计必须填写",
					step.getInsertField(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.insertField.setSingle(true);
			
			// Field to contain Update 文件包含更新
			this.updateField = new LabelInputMeta(id + ".updateField", "字段包含更新", null, null,
					"字段包含更新必须填写",
					step.getUpdateField(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.updateField.setSingle(true);
			
			// Field to contain Delete stats 文件含有删除统计
			this.deleteField = new LabelInputMeta(id + ".deleteField", "字段含有删除统计", null, null,
					"字段含有删除统计必须填写", 
					step.getDeleteField(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.deleteField.setSingle(true);
			
			// Field to contain Read stats 文件包含阅览统计
			this.readField = new LabelInputMeta(id + ".readField", "字段包含阅读统计", null, null,
					"字段包含阅读统计必须填写", 
					step.getReadField(),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.readField.setSingle(true);

			this.outputfields.putFieldsetsContent(new BaseFormMeta[] {
					this.insertField,
					this.updateField,
					this.deleteField,
					this.readField
                    });
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.connectionId,this.commitSize,
					this.sqlField, this.outputfields
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
