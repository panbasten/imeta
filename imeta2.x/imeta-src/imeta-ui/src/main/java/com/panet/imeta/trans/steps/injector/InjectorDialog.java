package com.panet.imeta.trans.steps.injector;

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
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class InjectorDialog extends BaseStepDialog implements
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
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public InjectorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
            InjectorMeta step = (InjectorMeta)super.getStep();
			// 得到form 
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称 
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

            // 字段
			
			this.fields = new LabelGridMeta(id + "_fields","字段", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.type", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMetaInterface.typeCodes, false));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.name", "字段名称",
							null, false, 80),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.length", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.precision", "精度",
							null, false, 50),
					});
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.injector.btn.injectorAdd");
			this.fields.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
			
			String[] values = step.getName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
						    new GridCellDataMeta(null, String.valueOf(step.getLength()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getPrecision()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields });

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

