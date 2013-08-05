package com.panet.imeta.trans.steps.tableinput;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TableInputDialog extends BaseStepDialog implements
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

	/**
	 * sql
	 */
	private LabelTextareaMeta sql;

	/**
	 * 允许延迟转换
	 */
	private LabelInputMeta lazyConversionActive;

	/**
	 * 替换SQL的变量
	 */
	private LabelInputMeta variableReplacementActive;

	/**
	 * 从步骤插入数据
	 */
	private LabelSelectMeta lookupFromStepname;

	/**
	 * 执行每一行
	 */
	private LabelInputMeta executeEachInputRow;

	/**
	 * 记录数量限制
	 */
	private LabelInputMeta rowLimit;

	private LabelGridMeta parameters;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public TableInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			TableInputMeta step = (TableInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到连接数据库
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());
			this.connection.setSingle(true);

			// SQL语句
			this.sql = new LabelTextareaMeta(id + ".sql", "SQL语句", null, null,
					null, step.getSQL(), 4, null);
			this.sql.setSingle(true);

			// 允许延时转换
			this.lazyConversionActive = new LabelInputMeta(id
					+ ".lazyConversionActive", "允许延时转换", null, null, null,
					String.valueOf(step.isLazyConversionActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 替换SQL语句的变量
			this.variableReplacementActive = new LabelInputMeta(id
					+ ".variableReplacementActive", "替换SQL语句的变量", null, null,
					null, String.valueOf(step.isVariableReplacementActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 记录数量限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "记录数量限制",
					null, null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			// 从步骤插入数据
			this.lookupFromStepname = new LabelSelectMeta(id
					+ ".lookupFromStepname", "从步骤插入数据", null, null, null, step
					.getLookupStepname(), null, super.getPrevStepNames(true));
			this.lookupFromStepname.setSingle(true);

			// 执行每一行
			this.executeEachInputRow = new LabelInputMeta(id
					+ ".executeEachInputRow", "执行每一行", null, null, null, String
					.valueOf(step.isExecuteEachInputRow()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 参数
			this.parameters = new LabelGridMeta(id + "_parameters", "参数", 150);
			this.parameters.setSingle(true);
			this.parameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_parameters.parameterId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_parameters.parameterName",
							"参数名", null, false, 150) });

			this.parameters.setHasBottomBar(true);
			this.parameters.setHasAdd(true, true,
					"jQuery.imeta.steps.tableinput.btn.parametersAdd");
			this.parameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] parametersName = step.getParametersName();
			if (parametersName != null && parametersName.length > 0) {
				for (int i = 0; i < parametersName.length; i++) {
					this.parameters.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, parametersName[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.sql, this.lazyConversionActive,
					this.variableReplacementActive, this.lookupFromStepname,
					this.executeEachInputRow, this.rowLimit, this.parameters });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
