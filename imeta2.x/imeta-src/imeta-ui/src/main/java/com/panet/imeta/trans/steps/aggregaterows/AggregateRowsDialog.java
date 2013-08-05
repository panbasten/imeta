package com.panet.imeta.trans.steps.aggregaterows;

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
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AggregateRowsDialog extends BaseStepDialog implements
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
	 * 字段
	 */
	private LabelGridMeta fields;

	/**
	 * Button
	 */
	private ButtonMeta getWordsBtn;

	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public AggregateRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			AggregateRowsMeta step = (AggregateRowsMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 字段
			this.fields = new LabelGridMeta(id + "_fields", null, 200, 0);
			GridHeaderDataMeta aggregateTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.aggregateType", "类型", null, false, 100);
			aggregateTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							AggregateRowsMeta.aggregateTypeDesc, false));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.fieldNewName", "新名称",
							null, false, 100), aggregateTypeDataMeta });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.aggregateRows.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			String[] fieldNames = step.getFieldName();
			if (fieldNames != null && fieldNames.length > 0)
				for (int i = 0; i < fieldNames.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, fieldNames[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldNewName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getAggregateType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields });

			this.getWordsBtn = new ButtonMeta(id + ".btn.previewRowsBtn", id
					+ ".btn.previewRowsBtn", "获取字段", "获取字段");
			this.getWordsBtn
					.addClick("jQuery.imeta.steps.aggregateRows.btn.getfields");
			this.getWordsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getWordsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getWordsBtn.putProperty("elementName", super.getStepName());
			this.getWordsBtn.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), this.getWordsBtn, super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
