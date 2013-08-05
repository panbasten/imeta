package com.panet.imeta.trans.steps.groupby;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class GroupByDialog extends BaseStepDialog implements
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
	 * 包括所有行
	 */
	private LabelInputMeta passAllRows;

	/**
	 * 排序目录
	 */
	private LabelInputMeta directory;

	private ButtonMeta browse;

	/**
	 * 临时文件前缀
	 */
	private LabelInputMeta prefix;

	/**
	 * 增加行号，每组重新开始
	 */
	private LabelInputMeta addingLineNrInGroup;

	/**
	 * 行号列名
	 */
	private LabelInputMeta lineNrInGroupField;

	/**
	 * 总返回一个结果行
	 */
	private LabelInputMeta alwaysGivingBackOneRow;

	/**
	 * 构成分组的字段
	 */
	private LabelGridMeta fields;
	private ButtonMeta fieldsButton;

	/**
	 * 聚合
	 */
	private LabelGridMeta aggregation;
	private ButtonMeta aggregationButton;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	public GroupByDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			GroupByMeta step = (GroupByMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 包括所有行
			boolean isPassAllRows = step.passAllRows();
			this.passAllRows = new LabelInputMeta(id + ".passAllRows",
					"包括所有的行？", null, null, null, String.valueOf(isPassAllRows),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.passAllRows
					.addClick("jQuery.imeta.steps.group.listeners.isPassAllRowsClick");
			this.passAllRows.setSingle(false);

			// 排序目录

			this.directory = new LabelInputMeta(id + ".directory", "排序目录",
					null, null, "排序目录必须填写", step.getDirectory(), null,
					ValidateForm.getInstance().setRequired(false));

			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览", "浏览");

			// this.directory.addButton(new ButtonMeta[] { this.browse });
			this.directory.setDisabled(!isPassAllRows);
			this.directory.setSingle(true);

			// 临时文件前缀
			this.prefix = new LabelInputMeta(id + ".prefix", "临时文件前缀", null,
					null, "临时文件前缀必须填写", step.getPrefix(), null, ValidateForm
							.getInstance().setRequired(false));
			this.prefix.setDisabled(!isPassAllRows);
			this.prefix.setSingle(true);

			// 增加行号，每组重新开始
			boolean isAddingLineNrInGroup = step.isAddingLineNrInGroup();
			this.addingLineNrInGroup = new LabelInputMeta(id
					+ ".addingLineNrInGroup", "增加行号，每组重新开始", null, null, null,
					String.valueOf(isAddingLineNrInGroup),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addingLineNrInGroup.setDisabled(!isPassAllRows);
			this.addingLineNrInGroup
					.addClick("jQuery.imeta.steps.group.listeners.isAddingLineNrInGroupClick");
			this.addingLineNrInGroup.setSingle(false);

			// 行号列名
			this.lineNrInGroupField = new LabelInputMeta(id
					+ ".lineNrInGroupField", "行号列名", null, null,
					"未使用的内存限值必须填写", step.getLineNrInGroupField(), null,
					ValidateForm.getInstance().setRequired(false));
			lineNrInGroupField.setDisabled(!isAddingLineNrInGroup);
			this.lineNrInGroupField.setSingle(true);

			// 总返回一个结果行
			this.alwaysGivingBackOneRow = new LabelInputMeta(id
					+ ".alwaysGivingBackOneRow", "总返回一个结果行", null, null, null,
					String.valueOf(step.isAlwaysGivingBackOneRow()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.alwaysGivingBackOneRow.setSingle(false);

			// 构成分组的字段
			this.fields = new LabelGridMeta(id + "_fields", "构成分组的字段：", 200, 0);
			GridHeaderDataMeta fieldNameDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldName", "分组字段", null, false, 100);
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			fieldNameDataMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					fieldNameDataMeta });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.group.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
			"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] fieldNames = step.getGroupField();
			if (fieldNames != null && fieldNames.length > 0)
				for (int i = 0; i < fieldNames.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getGroupField()[i],
									GridCellDataMeta.CELL_TYPE_SELECT), });
				}

			this.fieldsButton = new ButtonMeta(id + ".btn.fieldsButton", id
					+ ".btn.fieldsButton", "获取字段", "获取字段");
			this.fieldsButton
					.addClick("jQuery.imeta.steps.group.btn.getfields");
			this.fieldsButton.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.fieldsButton.putProperty("roName", super.getTransMeta()
					.getName());
			this.fieldsButton.putProperty("elementName", super.getStepName());
			this.fieldsButton.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			// 集合
			this.aggregation = new LabelGridMeta(id + "_aggregation", "聚合：",
					200, 0);
			GridHeaderDataMeta aggregateFieldMeta = new GridHeaderDataMeta(id
					+ "_aggregation.aggregateField", "名称", null, false, 100);
			aggregateFieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));

			GridHeaderDataMeta subjectFieldMeta = new GridHeaderDataMeta(id
					+ "_aggregation.subjectField", "题目", null, false, 100);
			subjectFieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));

			GridHeaderDataMeta aggregateTypeMeta = new GridHeaderDataMeta(id
					+ "_aggregation.aggregateType", "类型", null, false, 100);
			aggregateTypeMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							GroupByMeta.typeGroupLongDesc, false));
			this.aggregation.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_aggregation.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					aggregateFieldMeta,
					subjectFieldMeta,
					aggregateTypeMeta,
					new GridHeaderDataMeta(id + "_aggregation.valueField", "值",
							null, false, 100) });
			this.aggregation.setHasBottomBar(true);
			this.aggregation.setHasAdd(true, true,
					"jQuery.imeta.steps.group.btn.aggregationAdd");
			this.aggregation.setHasDelete(true, true,
			"jQuery.imeta.parameter.fieldsDelete");
			this.aggregation.setSingle(true);

			String[] aggregateField = step.getAggregateField();
			if (aggregateField != null && aggregateField.length > 0)
				for (int i = 0; i < aggregateField.length; i++) {
					this.aggregation.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getAggregateField()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									step.getSubjectField()[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getAggregateType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, step.getValueField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			this.aggregationButton = new ButtonMeta(id
					+ ".btn.aggregationButton", id + ".btn.aggregationButton",
					"获取查询字段", "获取查询字段");
			this.aggregationButton
					.addClick("jQuery.imeta.steps.group.btn.getAggregationfields");
			this.aggregationButton.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.aggregationButton.putProperty("roName", super.getTransMeta()
					.getName());
			this.aggregationButton.putProperty("elementName", super
					.getStepName());
			this.aggregationButton.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			// 装载到form
			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name,
							this.passAllRows, this.directory, this.prefix,
							this.addingLineNrInGroup, this.lineNrInGroupField,
							this.alwaysGivingBackOneRow, this.fields,
							this.fieldsButton, this.aggregation,
							this.aggregationButton });

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
