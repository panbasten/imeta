package com.panet.imeta.trans.steps.http;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class HTTPDialog extends BaseStepDialog implements StepDialogInterface {
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
	 * 网址
	 */
	private LabelInputMeta url;

	/**
	 * 网址域名
	 */
	private LabelInputMeta urlInField;

	/**
	 * 域名结果
	 */
	private LabelSelectMeta urlField;

	/**
	 * 接受网址
	 */
	private LabelInputMeta fieldName;

	/**
	 * 参数
	 */
	private LabelGridMeta fields;

	public HTTPDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			HTTPMeta step = (HTTPMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"HTTP client", super.getStepName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 网址

			this.url = new LabelInputMeta(id + ".url", "URL", null, null, "URL",
					step.getUrl(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.url.setSingle(true);
			this.url.setDisabled(step.isUrlInField());

			// 从字段获取URL

			this.urlInField = new LabelInputMeta(id + ".urlInField", "从字段获取URL",
					null, null, null, String.valueOf(step.isUrlInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.urlInField.setSingle(true);
			this.urlInField
					.addClick("jQuery.imeta.steps.http.listeners.urlInField");

			// URL字段名
			this.urlField = new LabelSelectMeta(id + ".urlField", "URL字段名",
					null, null, null, step.getUrlField(), null, super
							.getPrevStepResultFields());
			this.urlField.setSingle(true);
			this.urlField.setHasEmpty(true);
			this.urlField.setDisabled(!step.isUrlInField());

			// 结果字段名
			this.fieldName = new LabelInputMeta(id + ".fieldName", "结果字段名",
					null, null, null, step.getFieldName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fieldName.setSingle(true);

			// 得到参数

			this.fields = new LabelGridMeta(id + "_fields", "参数：", 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.argumentField", "名称",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.argumentParameter",
							"参数", null, false, 300) });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.http.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] field = step.getArgumentField();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, field[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step
									.getArgumentParameter()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });

				}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.url, this.urlInField, this.urlField, this.fieldName,
					this.fields });

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							super.getCancelBtn(),
							super
									.getGetfields("jQuery.imeta.steps.http.btn.getfields") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
