package com.panet.imeta.trans.steps.stringcut;

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

public class StringCutDialog extends BaseStepDialog implements
StepDialogInterface{
	
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
	 * The fields to cut
	 */
	private LabelGridMeta fields;
	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public StringCutDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			StringCutMeta step = (StringCutMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			//
			
			// Step name
			this.name = new LabelInputMeta ( id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// The fields to cut
			this.fields = new LabelGridMeta ( id + "_fields", "剪切字段", 200);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldInStream", "输入流字段", null, false, 80);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders( new GridHeaderDataMeta [] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta,
					new GridHeaderDataMeta(id + "_fields.fieldOutStream", "输出流字段", null, false, 80),
					new GridHeaderDataMeta(id + "_fields.cutFrom", "从...剪切", null, false, 80),
					new GridHeaderDataMeta(id + "_fields.cutTo", "剪切到...", null, false, 305)
	
					});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.stringcut.btn.stringcutAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);
			
			String[] field = step.getFieldInStream();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldInStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldOutStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getCutFrom()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getCutTo()[i],
									GridCellDataMeta.CELL_TYPE_INPUT)});
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
					this.name, this.fields
					 });
			
			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(), 
					super.getGetfields("jQuery.imeta.steps.stringcut.btn.getfields") });
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
