package com.panet.imeta.trans.steps.analyticquery;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.groupby.GroupByMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AnalyticQueryDialog extends BaseStepDialog implements
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
	 * The fields that make up the group
	 */
	private LabelGridMeta fields;
	private ButtonMeta getFields;

	/**
	 * Analytic
	 */
	private LabelGridMeta analyticFunctions;
	private ButtonMeta getLookupFields;

	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */

	public AnalyticQueryDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			AnalyticQueryMeta step = (AnalyticQueryMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// The fields that make up the group
			this.fields = new LabelGridMeta(id + "_group", "字段构成类：", 200, 0);
			GridHeaderDataMeta fieldNameDataMeta = new GridHeaderDataMeta(id
					+ "_group.groupField", "分组字段", null, false, 100);
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			fieldNameDataMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_group.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					fieldNameDataMeta });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.group.btn.analyticFieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			// this.getFields = new ButtonMeta(id + ".btn.getFields", id
			// + ".btn.getFields", "获取字段", "获取字段");
			this.getFields = super
					.getGetfields("jQuery.imeta.steps.group.btn.getAnalyticFields");
			// this.getFields
			// .addClick("jQuery.imeta.steps.group.btn.getAnalyticFields");
			// this.getFields.putProperty("roType",
			// RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			// this.getFields
			// .putProperty("roName", super.getTransMeta().getName());
			// this.getFields.putProperty("elementName", super.getStepName());

			String[] fieldNames = step.getGroupField();
			if (fieldNames != null && fieldNames.length > 0)
				for (int i = 0; i < fieldNames.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getGroupField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}

			// Analytic Functions
			this.analyticFunctions = new LabelGridMeta(id
					+ "_analyticFunctions", "解析函数：", 200, 0);
			GridHeaderDataMeta newFieldMeta = new GridHeaderDataMeta(id
					+ "_analyticFunctions.name", "新字段名称", null, false, 100);
			newFieldMeta.setOptions(super.getOptionsByStringArray(resultNames,
					true));

			GridHeaderDataMeta subjectFieldMeta = new GridHeaderDataMeta(id
					+ "_analyticFunctions.subject", "主题", null, false, 100);
			subjectFieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));

			GridHeaderDataMeta typeMeta = new GridHeaderDataMeta(id
					+ "_analyticFunctions.type", "类型", null, false, 100);
			typeMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					AnalyticQueryMeta.typeGroupLongDesc, false));

			this.analyticFunctions.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_analyticFunctions.fieldId",
							"#", GridHeaderDataMeta.HEADER_TYPE_NUMBER, false,
							50),
					newFieldMeta,
					subjectFieldMeta,
					typeMeta,
					new GridHeaderDataMeta(id + "_analyticFunctions.value",
							"N", null, false, 100) });
			this.analyticFunctions.setHasBottomBar(true);
			this.analyticFunctions.setHasAdd(true, true,
					"jQuery.imeta.steps.group.btn.analyticFunctionsAdd");
			this.analyticFunctions.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.analyticFunctions.setSingle(true);
			this.analyticFunctions.setSingle(true);
			String[] aggregateField = step.getAggregateField();
			if (aggregateField != null && aggregateField.length > 0)
				for (int i = 0; i < aggregateField.length; i++) {
					this.analyticFunctions.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getAggregateField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getSubjectField()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getAggregateType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getValueField()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			this.getLookupFields = new ButtonMeta(id + ".btn.getLookupFields",
					id + ".btn.getLookupFields", "获取查询字段", "获取查询字段");
			this.getLookupFields
					.addClick("jQuery.imeta.steps.group.btn.getAnalyticFunctions");
			this.getLookupFields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getLookupFields.putProperty("roName", super.getTransMeta()
					.getName());
			this.getLookupFields
					.putProperty("elementName", super.getStepName());
			this.getLookupFields.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields, this.getFields, this.analyticFunctions,
					this.getLookupFields });
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
