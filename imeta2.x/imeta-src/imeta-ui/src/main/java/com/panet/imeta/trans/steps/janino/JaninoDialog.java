package com.panet.imeta.trans.steps.janino;

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
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class JaninoDialog extends BaseStepDialog implements
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
	 * 域
	 */
	private LabelGridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JaninoDialog (StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JaninoMeta step = (JaninoMeta)super.getStep();
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"Janino", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
             
			//域
			this.fields = new LabelGridMeta(id + "_fields", "域", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.valueType", "值类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMeta.typeCodes, false));
			
			RowMetaInterface rV = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultValueNames = rV.getFieldNames();
			GridHeaderDataMeta replaceField = new GridHeaderDataMeta(id
					+ "_fields.replaceField", "取代值", null, false, 80);
			replaceField.setOptions(super.getOptionsByStringArray(
					resultValueNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "新域",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.formula", "formula",
							null, false, 100),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.valueLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.valuePrecision", "精度",
							null, false, 50),
							replaceField});
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.janino.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");

			JaninoMetaFunction[] janinoMetaFunction = step.getFormula();
			if (janinoMetaFunction != null && janinoMetaFunction.length > 0)
				for (int i = 0; i < janinoMetaFunction.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, janinoMetaFunction[i]
							        	.getFieldName(),
							        	GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
							        	.valueOf(janinoMetaFunction[i].getFormula()),
							        	GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
							            String.valueOf(janinoMetaFunction[i]
							        				.getValueType()),
							        	GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
							        	.valueOf(janinoMetaFunction[i]
							        				.getValueLength()),
							        	GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
						        	.valueOf(janinoMetaFunction[i]
						        				.getValuePrecision()),
						        	GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
						        	.valueOf(janinoMetaFunction[i]
						        				.getReplaceField()),
						        	GridCellDataMeta.CELL_TYPE_INPUT)

					});
			}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields});

		
			
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
