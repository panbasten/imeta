package com.panet.imeta.trans.steps.getpreviousrowfield;

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
import com.panet.imeta.ui.exception.ImetaException;

public class GetPreviousRowFieldDialog extends BaseStepDialog implements
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
	 * Fields
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
	public GetPreviousRowFieldDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			GetPreviousRowFieldMeta step = (GetPreviousRowFieldMeta) super
					.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// Step name 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Fields 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 200, 0);
			GridHeaderDataMeta inputDataMeta = new GridHeaderDataMeta(id
					+ "_fields.input", "输入流字段", null, false, 100);
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			inputDataMeta.setOptions(super.getOptionsByStringArray(resultNames,
					true));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					inputDataMeta,
					new GridHeaderDataMeta(id + "_fields.output", "输出流字段",
							null, false, 100) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.getPreviousRowField.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			String[] input = step.getFieldInStream();
			if (input != null && input.length > 0)
				for (int i = 0; i < input.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, input[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									step.getFieldOutStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields });

			this.getWordsBtn = new ButtonMeta(id + ".btn.previewRowsBtn", id
					+ ".btn.previewRowsBtn", "获取字段", "获取字段");
			this.getWordsBtn
					.addClick("jQuery.imeta.steps.getPreviousRowField.btn.getfields");
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
