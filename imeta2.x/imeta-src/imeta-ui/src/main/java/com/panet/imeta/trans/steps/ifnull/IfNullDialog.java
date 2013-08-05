package com.panet.imeta.trans.steps.ifnull;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class IfNullDialog  extends BaseStepDialog implements
		StepDialogInterface {
	public IfNullDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

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
	 * Replace
	 */
	private ColumnFieldsetMeta replace;
	
	/**
	 * Replace by value
	 */
	private LabelInputMeta replaceAllByValue;
	
	/**
	 * Mask
	 */
	private LabelSelectMeta replaceAllMask;
	
	/**
	 * Select fields
	 */
	private LabelInputMeta selectFields;
	
	/**
	 * Select value type
	 */
	private LabelInputMeta selectValuesType;
	
	/**
	 * Value type
	 */
	private LabelGridMeta types;
	
	/**
	 * Fields
	 */
	private LabelGridMeta fields;
	
	/**
	 * getFields Button
	 */
	private ButtonMeta getfields;
	
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			IfNullMeta step = (IfNullMeta)super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Replace null for all fields
			this.replace = new ColumnFieldsetMeta ( null, "用字段替换空值");
			this.replace.setSingle(true);
			
			// Replace by value
			this.replaceAllByValue = new LabelInputMeta ( id + ".replaceAllByValue", "替换值", null, null, null,
					step.getReplaceAllByValue(), null, ValidateForm.getInstance().setRequired(true));
			this.replaceAllByValue.setSingle(true);
			this.replaceAllByValue.setDisabled(step.isSelectFields()|step.isSelectValuesType());
			
			// Mask
			this.replaceAllMask = new LabelSelectMeta ( id + ".replaceAllMask", "掩码（日期）", null, null,
					null, step.getReplaceAllMask(), null, super.getDateFormats());
			this.replaceAllMask.setSingle(true);
			this.replaceAllMask.setDisabled(step.isSelectFields()|step.isSelectValuesType());
			
			this.replace.putFieldsetsContent(new BaseFormMeta[] {
					this.replaceAllByValue, this.replaceAllMask
			});
			
			// Select fields
			this.selectFields = new LabelInputMeta ( id + ".selectFields", "选择字段", null, null, null,
					String.valueOf(step.isSelectFields()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.selectFields.setSingle(true);
            this.selectFields.addClick("jQuery.imeta.steps.ifnull.listeners.selectFields");
            
            // Select value type
            this.selectValuesType = new LabelInputMeta ( id + ".selectValuesType", "选择值类型", null, null, null,
            		String.valueOf(step.isSelectValuesType()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.selectValuesType.setSingle(true);
            this.selectValuesType.addClick("jQuery.imeta.steps.ifnull.listeners.selectValuesType");
            
            // Value types
            this.types = new LabelGridMeta(id + "_types", "值类型", 200);
            
            GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_types.typeName", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArray(
					ValueMeta.typeCodes, false));
			
			this.types .addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_types.typeId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_types.typereplaceValue", "替代值", null, false, 70),
					new GridHeaderDataMeta(id + "_types.typereplaceMask", "转换掩码（日期）", null, false, 400)
					});
			this.types.setHasBottomBar(true);
			this.types.setHasAdd(true, step.isSelectValuesType(),
					"jQuery.imeta.steps.ifnull.btn.ifnullTypeAdd");
			this.types.setHasDelete(true, step.isSelectValuesType(),
					"jQuery.imeta.parameter.fieldsDelete");
			this.types.setSingle(true);
			
			String[] type = step.getTypeName();
			if (type != null && type.length > 0){
				
				for (int i = 0; i < type.length; i++) {
					
					this.types.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getTypeName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), 
							new GridCellDataMeta(null, step.getTypeReplaceValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), 
							new GridCellDataMeta(null, step.getTypeReplaceMask()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			}
			
			// Field
            this.fields = new LabelGridMeta(id + "_fields", "字段", 200);
            
            RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldName", "字段", null, false, 100);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields .addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta,
					new GridHeaderDataMeta(id + "_fields.replaceValue", "替代值", null, false, 70),
					new GridHeaderDataMeta(id + "_fields.replaceMask", "转换掩码（日期）", null, false, 400)
					});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, step.isSelectFields(),
					"jQuery.imeta.steps.ifnull.btn.ifnullFieldsAdd");
			this.fields.setHasDelete(true, step.isSelectFields(),
			"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);
			
			String[] values = step.getFieldName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), 
							new GridCellDataMeta(null, step.getReplaceValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), 
							new GridCellDataMeta(null, step.getReplaceMask()[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.replace, this.selectFields, this.selectValuesType,
					this.types, this.fields
					});
			
			// 确定，取消
			
			this.getfields = new ButtonMeta(
					id + ".getfields", id + ".getfields",
					"获取字段", "获取字段");
			this.getfields
					.addClick("jQuery.imeta.steps.ifnull.btn.getfields");
			this.getfields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getfields.putProperty("roName", super.getTransMeta()
					.getName());
			this.getfields.putProperty("elementName", super
					.getStepName());
			this.getfields.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			if (!step.isSelectFields() == true| step.isSelectValuesType()== true){
				this.getfields.setDisabled(true);
			}else{
				this.getfields.setDisabled(false);
			}
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(), 
					this.getfields });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
