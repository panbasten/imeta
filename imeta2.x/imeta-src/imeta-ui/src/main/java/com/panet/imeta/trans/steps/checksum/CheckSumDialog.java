package com.panet.imeta.trans.steps.checksum;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class CheckSumDialog extends BaseStepDialog implements
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
	 * Type
	 */
	private LabelSelectMeta checksumtype;

	/**
	 * Result fields
	 */
	private LabelInputMeta resultfieldName;

	/**
	 * Fields used in checksum
	 */
	private LabelGridMeta fields;

	public CheckSumDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			CheckSumMeta step = (CheckSumMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Type
			List<OptionDataMeta> optionsType = new ArrayList<OptionDataMeta>();
			optionsType.add(new OptionDataMeta("CRC32", Messages.getString("CheckSumDialog.Type.CRC32")));
			optionsType.add(new OptionDataMeta("ADLER32", Messages.getString("CheckSumDialog.Type.ADLER32")));
			optionsType.add(new OptionDataMeta("MD5", Messages.getString("CheckSumDialog.Type.MD5")));
			optionsType.add(new OptionDataMeta("SHA1", Messages.getString("CheckSumDialog.Type.SHA1")));
			this.checksumtype = new LabelSelectMeta(id + ".checksumtype", "类型",
					null, null, null, String.valueOf(step.getCheckSumType()), null, optionsType);
			this.checksumtype.setSingle(true);
			// Result fields
			this.resultfieldName = new LabelInputMeta(id + ".resultfieldName",
					"返回字段", null, null, "返回字段必须填写", step.getResultFieldName(),
					null, ValidateForm.getInstance().setRequired(true));
			this.resultfieldName.setSingle(true);

			// Fields used in checksum

			this.fields = new LabelGridMeta(id + "_fields", "字段用于校验", 200);
	
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
					fieldMeta

			});
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.checksum.btn.checksumAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] values = step.getFieldName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT)});
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.checksumtype, this.resultfieldName, this.fields });
			
			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(), 
					super.getGetfields("jQuery.imeta.steps.checksum.btn.getwords") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
