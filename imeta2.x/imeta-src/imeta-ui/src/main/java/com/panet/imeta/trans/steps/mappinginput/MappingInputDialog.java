package com.panet.imeta.trans.steps.mappinginput;

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
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MappingInputDialog extends BaseStepDialog implements
StepDialogInterface{
	
	//映射输入
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
	 * 对于这个mapping要求的输入字段
	 */
	private LabelGridMeta fields;
	
	/**
	 * Include unspecified field,ordered by name
	 */
	private LabelInputMeta selectingAndSortingUnspecifiedFields;
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public MappingInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
            MappingInputMeta step = (MappingInputMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 对于这个mapping(sub-transformation)要求的输入字段
			this.fields = new LabelGridMeta(id + "_fields", "对于这个mapping(sub-transformation)要求的输入字段:", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMetaInterface.typeCodes, false));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50)
					});
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.mappinginput.btn.fieldAdd");
			this.fields.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
			
			String[] values = step.getFieldName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
						    new GridCellDataMeta(null, String.valueOf(step.getFieldLength()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldPrecision()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT)
							 });
				}
			
			
			// include
			this.selectingAndSortingUnspecifiedFields = new LabelInputMeta ( id + ".selectingAndSortingUnspecifiedFields", "包括未具体说明的字段，以名称排序", null, null,
					null, String.valueOf(step.isSelectingAndSortingUnspecifiedFields()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.selectingAndSortingUnspecifiedFields.setSingle(true);
			
			// 装载到form 
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields, this.selectingAndSortingUnspecifiedFields });
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn()});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
