package com.panet.imeta.trans.steps.nullif;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class NullIfDialog extends BaseStepDialog implements
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
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public NullIfDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			NullIfMeta step = (NullIfMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", "Null if...", null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
            // 得到字段
			
			this.fields = new LabelGridMeta(id + "_fields",
					"字段", 150);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldName", "字段", null, false, 100);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta,
					new GridHeaderDataMeta(id + "_fields.fieldValue", "需要转换成NULL的值", null, false, 503)
					
			});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.nullif.btn.nullifAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);
			
			String[] values = step.getFieldName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), 
							new GridCellDataMeta(null, step.getFieldValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),});
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.fields});

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(),
					super.getGetfields("jQuery.imeta.steps.nullif.btn.getfields")});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
