package com.panet.imeta.trans.steps.userdefinedjavaclass;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta.FieldInfo;
import com.panet.imeta.ui.exception.ImetaException;

public class UserDefinedJavaClassDialog extends BaseStepDialog implements
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
	 * 类名
	 */
	private LabelInputMeta className;

	/**
	 * 类代码
	 */
	private LabelTextareaMeta classDef;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 页签-字段
	 */
	private LabelGridMeta fields, parameters, infoSteps, targetSteps;

	public UserDefinedJavaClassDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			UserDefinedJavaClassMeta step = (UserDefinedJavaClassMeta) super
					.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到编辑框
			List<UserDefinedJavaClassDef> def = step.getDefinitions();
			String className = "";
			String classDef = "";
			if (def == null || def.size() == 0) {
				def = new ArrayList<UserDefinedJavaClassDef>();
				def.add(new UserDefinedJavaClassDef(
						UserDefinedJavaClassDef.ClassType.TRANSFORM_CLASS,
						"Processor", UserDefinedJavaClassCodeSnippits
								.getSnippitsHelper().getDefaultCode()));
				step.replaceDefinitions(def);
			}
			className = def.get(0).getClassName();
			classDef = def.get(0).getSource();

			this.className = new LabelInputMeta(id + ".className", "类名称", null,
					null, "类名称", className, null, ValidateForm.getInstance()
							.setRequired(false));
			this.className.setSingle(true);

			this.classDef = new LabelTextareaMeta(id + ".source", "类代码", null,
					null, "类代码", classDef, 12, ValidateForm.getInstance()
							.setRequired(true));
			this.classDef.setSingle(true);

			// 页签
			this.meta = new MenuTabMeta(id, new String[] { "字段", "参数", "消息步骤",
					"目标步骤" });
			this.meta.setSingle(true);

			setFields(id, step);
			setParameters(id, step);
			setInfoSteps(id, step);
			setTargetSteps(id, step);

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.className, this.classDef, this.meta });

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

	private void setFields(String id, UserDefinedJavaClassMeta step)
			throws ImetaFormException {
		this.fields = new LabelGridMeta(id + "_fields", "字段", 200);

		GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
				+ "_fields.fieldType", "类型", null, false, 100);
		fieldTypeDataMeta.setOptions(super
				.getOptionsByStringArrayWithNumberValue(ValueMeta.typeCodes,
						false));

		this.fields.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_fields.fieldId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_fields.fieldName", "字段名", null,
						false, 150),
				fieldTypeDataMeta,
				new GridHeaderDataMeta(id + "_fields.fieldLength", "长度", null,
						false, 150),
				new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
						null, false, 150) });

		this.fields.setSingle(true);
		this.fields.setHasBottomBar(true);
		this.fields.setHasAdd(true, true,
				"jQuery.imeta.steps.userdefinedjavaclass.fields.btn.fieldAdd");
		this.fields.setHasDelete(true, true,
				"jQuery.imeta.parameter.fieldsDelete");

		List<FieldInfo> fieldsRows = step.getFieldInfo();
		if (fieldsRows != null && fieldsRows.size() > 0) {
			for (int i = 0; i < fieldsRows.size(); i++) {
				this.fields.addRow(new Object[] {
						String.valueOf(i + 1),
						new GridCellDataMeta(null, fieldsRows.get(i).name,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String.valueOf(fieldsRows
								.get(i).type),
								GridCellDataMeta.CELL_TYPE_SELECT),
						new GridCellDataMeta(null, String.valueOf(fieldsRows
								.get(i).length),
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String.valueOf(fieldsRows
								.get(i).precision),
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
		}
		this.meta.putTabContent(0, new BaseFormMeta[] { this.fields });
	}

	private void setParameters(String id, UserDefinedJavaClassMeta step)
			throws ImetaFormException {
		this.parameters = new LabelGridMeta(id + "_parameters", "参数", 200);
		this.parameters.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_parameters.parameterId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_parameters.parameterTag", "标签",
						null, false, 150),
				new GridHeaderDataMeta(id + "_parameters.parameterValue", "值",
						null, false, 150),
				new GridHeaderDataMeta(id + "_parameters.parameterDesc", "描述",
						null, false, 150) });

		this.parameters.setSingle(true);
		this.parameters.setHasBottomBar(true);
		this.parameters
				.setHasAdd(true, true,
						"jQuery.imeta.steps.userdefinedjavaclass.parameters.btn.fieldAdd");
		this.parameters.setHasDelete(true, true,
				"jQuery.imeta.parameter.fieldsDelete");

		List<UsageParameter> params = step.getUsageParameters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				this.parameters.addRow(new Object[] {
						String.valueOf(i + 1),
						new GridCellDataMeta(null, params.get(i).tag,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String
								.valueOf(params.get(i).value),
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String
								.valueOf(params.get(i).description),
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
		}
		this.meta.putTabContent(1, new BaseFormMeta[] { this.parameters });
	}

	private void setInfoSteps(String id, UserDefinedJavaClassMeta step)
			throws ImetaFormException {
		this.infoSteps = new LabelGridMeta(id + "_infoSteps", "消息步骤", 200);
		this.infoSteps.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_infoSteps.infoStepId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_infoSteps.infoStepTag", "标签",
						null, false, 150),
				new GridHeaderDataMeta(id + "_infoSteps.infoStepStep", "步骤",
						null, false, 150),
				new GridHeaderDataMeta(id + "_infoSteps.infoStepDesc", "描述",
						null, false, 150) });

		this.infoSteps.setSingle(true);
		this.infoSteps.setHasBottomBar(true);
		this.infoSteps
				.setHasAdd(true, true,
						"jQuery.imeta.steps.userdefinedjavaclass.infoSteps.btn.fieldAdd");
		this.infoSteps.setHasDelete(true, true,
				"jQuery.imeta.parameter.fieldsDelete");

		List<StepDefinition> params = step.getInfoStepDefinitions();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				this.infoSteps.addRow(new Object[] {
						String.valueOf(i + 1),
						new GridCellDataMeta(null, params.get(i).tag,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, params.get(i).stepName,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String
								.valueOf(params.get(i).description),
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
		}
		this.meta.putTabContent(2, new BaseFormMeta[] { this.infoSteps });
	}

	private void setTargetSteps(String id, UserDefinedJavaClassMeta step)
			throws ImetaFormException {
		this.targetSteps = new LabelGridMeta(id + "_targetSteps", "目标步骤", 200);
		this.targetSteps.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_targetSteps.targetStepId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_targetSteps.targetStepTag", "标签",
						null, false, 150),
				new GridHeaderDataMeta(id + "_targetSteps.targetStepStep",
						"步骤", null, false, 150),
				new GridHeaderDataMeta(id + "_targetSteps.targetStepDesc",
						"描述", null, false, 150) });

		this.targetSteps.setSingle(true);
		this.targetSteps.setHasBottomBar(true);
		this.targetSteps
				.setHasAdd(true, true,
						"jQuery.imeta.steps.userdefinedjavaclass.targetSteps.btn.fieldAdd");
		this.targetSteps.setHasDelete(true, true,
				"jQuery.imeta.parameter.fieldsDelete");

		List<StepDefinition> params = step.getTargetStepDefinitions();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				this.targetSteps.addRow(new Object[] {
						String.valueOf(i + 1),
						new GridCellDataMeta(null, params.get(i).tag,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, params.get(i).stepName,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String
								.valueOf(params.get(i).description),
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
		}
		this.meta.putTabContent(3, new BaseFormMeta[] { this.targetSteps });
	}
}
