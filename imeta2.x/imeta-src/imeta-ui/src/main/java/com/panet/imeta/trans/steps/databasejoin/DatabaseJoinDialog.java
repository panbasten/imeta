package com.panet.imeta.trans.steps.databasejoin;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DatabaseJoinDialog extends BaseStepDialog implements
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
	 * sql
	 */
	private LabelTextareaMeta sql;

	/**
	 * 得到行数
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 外部链接
	 */
	private LabelInputMeta outerJoin;

	/**
	 * 替换变量
	 */
	private LabelInputMeta replacevars;

	/**
	 * 启用参数
	 */
	private LabelGridMeta param;

	public DatabaseJoinDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			DatabaseJoinMeta step = (DatabaseJoinMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"数据库连接", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId",
					"数据库连接", null, null, null, String.valueOf((step
							.getDatabaseMeta() != null) ? step
							.getDatabaseMeta().getID() : ""), null, super
							.getConnectionLine());

			this.connectionId.setSingle(true);

			// 得到数据库

			this.sql = new LabelTextareaMeta(id + ".sql", "SQL", null, null,
					null, step.getSql(), 2, null);
			this.sql.setSingle(true);

			// 得到行数

			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "行数", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowLimit.setSingle(true);

			// 外部链接
			this.outerJoin = new LabelInputMeta(id + ".outerJoin", "外部链接?",
					null, null, null, String.valueOf(step.isOuterJoin()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.outerJoin.setSingle(true);

			// 替换参数
			this.replacevars = new LabelInputMeta(id + ".replacevars", "替换参数?",
					null, null, null, String.valueOf(step.isVariableReplace()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.replacevars.setSingle(true);

			// 启用参数
			this.param = new LabelGridMeta(id + "_param", "启用参数：", 200);
			this.param.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_param.paramId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_param.parameterField",
							"参数字段", null, false, 200),
					(new GridHeaderDataMeta(id + "_param.parameterType",
							"参数类型", null, false, 200)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)) });
			this.param.setHasBottomBar(true);
			this.param.setHasAdd(true, true,
					"jQuery.imeta.steps.databasejoin.btn.fieldAdd");
			this.param.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.param.setSingle(true);

			String[] parameterField = step.getParameterField();
			if (parameterField != null && parameterField.length > 0) {
				for (int i = 0; i < parameterField.length; i++) {
					this.param.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getParameterField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getParameterType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT), });
				}
			}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connectionId, this.sql, this.rowLimit, this.outerJoin,
					this.replacevars, this.param });

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							this.getCancelBtn(),
							super
									.getGetfields("jQuery.imeta.steps.databasejoin.btn.getparam") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
