package com.panet.imeta.trans.steps.columnexists;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ColumnExistsDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 得到步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connectionId;

	/**
	 * 得到结构名称
	 */
	private LabelInputMeta schemaname;

	/**
	 * 得到表名
	 */
	private LabelInputMeta tablename;


	/**
	 * 表名在域中
	 */
	private LabelInputMeta istablenameInfield;
	/**
	 * 表名域
	 */
	private LabelSelectMeta tablenamefield;
	/**
	 * 列名
	 */
	private LabelSelectMeta columnnamefield;
	/**
	 * 得到结果域名
	 */
	private LabelInputMeta resultfieldname;      

	public ColumnExistsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			ColumnExistsMeta step = (ColumnExistsMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(),
					null, ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			//得到数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId", "数据库连接",
					null, null, null,  String
					.valueOf((step.getDatabase() != null) ? step
							.getDatabase().getID() : ""), null, super.getConnectionLine());
			this.connectionId.setSingle(true);

			// 得到结构名称

			this.schemaname = new LabelInputMeta(id + ".schemaname", "结构名称", null,
					null, "结构名称必须填写", step.getSchemaname(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.schemaname.setSingle(true);

			// 得到表名
			this.tablename = new LabelInputMeta(id + ".tablename", "表名", null,
					null, "表名必须填写", step.getTablename(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.tablename.setSingle(true);
			this.tablename.setDisabled(!step.isTablenameInField());
			
			// 表名在域中istablenameInfield
			this.istablenameInfield = new LabelInputMeta(id + ".istablenameInfield",
					"表名在域中", null, null, String.valueOf(step.isTablenameInField()), null,
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.istablenameInfield.setSingle(false);
			this.istablenameInfield
			      .addClick("jQuery.imeta.steps.columnexists.listeners.istablenameInfield");
			// 表名域
			this.tablenamefield = new LabelSelectMeta(id + ".tablenamefield", "表名域", null, null,
					null, step.getDynamicTablenameField(), null, super.getPrevStepResultFields());
			this.tablenamefield.setSingle(true);
			this.tablenamefield.setDisabled(!step.isTablenameInField());
			this.tablenamefield.setHasEmpty(true);
			
			// 得到列名
			this.columnnamefield = new LabelSelectMeta(id + ".columnnamefield", "列名域", null, null,
					null, step.getDynamicColumnnameField(), null, super.getPrevStepResultFields());
			this.columnnamefield.setSingle(true);
			this.columnnamefield.setHasEmpty(true);

			// 得到结果域名
			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname", "结果域名", null,
					null, "结果域名必须填写", step.getResultFieldName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.resultfieldname.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connectionId, this.schemaname, this.tablename,
					this.istablenameInfield, this.tablenamefield, this.columnnamefield, this.resultfieldname });


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
