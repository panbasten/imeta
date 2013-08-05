package com.panet.imeta.trans.steps.dbproc;

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
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DBProcDialog extends BaseStepDialog implements StepDialogInterface {
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
	 * 存储过程名称
	 */
	private LabelInputMeta procedure;

	/**
	 * 启用自动提交
	 */
	private LabelInputMeta autoCommit;

	/**
	 * 返回值名称
	 */
	private LabelInputMeta resultName;

	/**
	 * 返回值类型
	 */
	private LabelSelectMeta resultType;

	/**
	 * 参数
	 */
	private LabelGridMeta keywords;

	public DBProcDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			DBProcMeta step = (DBProcMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"调用DB存储过程必须填写", super.getStepName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId",
					"数据库连接", null, null, null, String.valueOf((step
							.getDatabase() != null) ? step.getDatabase()
							.getID() : ""), null, super.getConnectionLine());

			this.connectionId.setSingle(true);

			// 储存过程名称
			this.procedure = new LabelInputMeta(id + ".procedure", "存储过程名称",
					null, null, "储存过程名称必须填写", step.getProcedure(), null,
					ValidateForm.getInstance().setRequired(true));
			this.procedure.setSingle(true);

			// 启用自动提交
			this.autoCommit = new LabelInputMeta(id + ".autoCommit", "启用自动提交",
					null, null, null, String.valueOf(step.isAutoCommit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.autoCommit.setSingle(false);

			// 得到返回值名称
			this.resultName = new LabelInputMeta(id + ".resultName", "返回值名称",
					null, null, "返回值名称必须填写", step.getResultName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.resultName.setSingle(true);

			// 得到返回值类型
			this.resultType = new LabelSelectMeta(id + ".resultType", "返回值类型",
					null, null, null, String.valueOf(step.getResultType()),
					null, super.getValueTypeByIndex());
			this.resultType.setSingle(true);

			// 参数：
			this.keywords = new LabelGridMeta(id + "_keywords", "参数：", 200);
			this.keywords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keywords.keywordsId", "#",
							null, false, 50),
					new GridHeaderDataMeta(id + "_keywords.argument", "名称",
							null, false, 150),
					(new GridHeaderDataMeta(id + "_keywords.argumentDirection",
							"方向", null, false, 150)).setOptions(super
									.getOptionsByStringArray(DBProcMeta.fieldTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_keywords.argumentType",
							"类型", null, false, 150)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false))

			});
			this.keywords.setHasBottomBar(true);
			this.keywords.setHasAdd(true, true,
					"jQuery.imeta.steps.dbproc.btn.dbprocAdd");
			this.keywords.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.keywords.setSingle(true);

			String[] argument = step.getArgument();
			String[] argumentDirection = step.getArgumentDirection();
			int[] argumentType = step.getArgumentType();

			if (argument != null && argument.length > 0){
				GridCellDataMeta name, direction, type;
				for (int i = 0; i < argument.length; i++) {
					name =	new GridCellDataMeta(null, argument[i],
									GridCellDataMeta.CELL_TYPE_INPUT);
					direction = new GridCellDataMeta(null, argumentDirection[i],
									GridCellDataMeta.CELL_TYPE_SELECT);
					type = new GridCellDataMeta(null, String.valueOf(argumentType[i]),
									GridCellDataMeta.CELL_TYPE_SELECT);
					this.keywords.addRow(new Object[] {
							String.valueOf(i + 1),name, direction, type
					});
				}
			}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connectionId, this.procedure, this.autoCommit,
					this.resultName, this.resultType, this.keywords });

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							super.getCancelBtn(),
							super
									.getGetfields("jQuery.imeta.steps.dbproc.btn.getfields") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
