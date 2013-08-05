package com.panet.imeta.trans.steps.tableexists;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TableExistsDialog extends BaseStepDialog implements
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
	 * 模式名称
	 */
	private LabelInputMeta schemaname;

	/**
	 * 表名字段
	 */
	private LabelSelectMeta tablenamefield;
	
	/**
	 * 结果字段名
	 */
	private LabelInputMeta resultfieldname;
	
	public TableExistsDialog (StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			TableExistsMeta step = (TableExistsMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"Table exists", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 得到数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId","数据库连接",
					null,null,null, String
					.valueOf((step.getDatabase() != null) ? step
							.getDatabase().getID() : ""),null,super.getConnectionLine());
			
			this.connectionId.setSingle(true);
			
            // 得到模式名称
			this.schemaname = new LabelInputMeta(id + ".schemaname", "模式名称", null, null,
					"模式名称必须填写", step.getSchemaname(), null, ValidateForm
							.getInstance().setRequired(true));
			this.schemaname.setSingle(true);
			
			// 得到表名字段
			this.tablenamefield = new LabelSelectMeta(id + ".tablenamefield","表名字段",
					null,null,null,step.getDynamicTablenameField(),null,super.getPrevStepResultFields());
			this.tablenamefield.setHasEmpty(true);
			this.tablenamefield.setSingle(true);

            // 得到结果字段名
			
			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname", "结果字段名", null, null,
					"结果字段名必须填写", step.getResultFieldName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.resultfieldname.setSingle(true);
		
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.connectionId,this.schemaname,this.tablenamefield,this.resultfieldname});

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}


