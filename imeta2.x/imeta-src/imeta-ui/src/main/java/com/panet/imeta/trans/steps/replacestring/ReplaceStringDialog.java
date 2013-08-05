package com.panet.imeta.trans.steps.replacestring;

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

public class ReplaceStringDialog extends BaseStepDialog implements
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
	 * Fields string
	 */
	private LabelGridMeta fields;
	
	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public ReplaceStringDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			ReplaceStringMeta step = (ReplaceStringMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			//

			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null,
					null, "步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Field string
			this.fields = new LabelGridMeta(id + "_fields",
					"字段字符串", 200);
			
			GridHeaderDataMeta fieldBooleanTypeDataMetaA = new GridHeaderDataMeta(
					id + "_fields.useRegEx", "使用正则表达式", null, false, 120);
			fieldBooleanTypeDataMetaA.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			GridHeaderDataMeta fieldBooleanTypeDataMetaB = new GridHeaderDataMeta(
					id + "_fields.wholeWord", "全字符", null, false, 80);
			fieldBooleanTypeDataMetaB.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			GridHeaderDataMeta fieldBooleanTypeDataMetaC = new GridHeaderDataMeta(
					id + "_fields.caseSensitive", "区分大小写", null, false, 100);
			fieldBooleanTypeDataMetaC.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldInStreamMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldInStream", "输入流字段", null, false, 80);
			fieldInStreamMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					fieldInStreamMeta,
					new GridHeaderDataMeta(id + "_fields.fieldOutStream",
							"输出流字段", null, false, 80),
					fieldBooleanTypeDataMetaA,
					new GridHeaderDataMeta(id + "_fields.replaceString",
							"搜索", null, false, 50),
					new GridHeaderDataMeta(id + "_fields.replaceByString",
							"替换为", null, false, 50),
					fieldBooleanTypeDataMetaB,
					fieldBooleanTypeDataMetaC });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.replacestring.btn.replacestringAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] field = step.getFieldInStream();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getFieldInStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldOutStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getUseRegEx()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, step.getReplaceString()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getReplaceByString()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getWholeWord()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step.getCaseSensitive()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			
			
			
			

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.name, this.fields});

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(), 
					super.getGetfields("jQuery.imeta.steps.replacestring.btn.getfields") });
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
