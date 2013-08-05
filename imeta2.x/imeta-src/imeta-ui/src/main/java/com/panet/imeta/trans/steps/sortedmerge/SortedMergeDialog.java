package com.panet.imeta.trans.steps.sortedmerge;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SortedMergeDialog extends BaseStepDialog implements
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
	 * 获取字段
	 */
	private ButtonMeta getWordsBtn;

	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */

	public SortedMergeDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			SortedMergeMeta step = (SortedMergeMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到字段

			this.fields = new LabelGridMeta(id + "_fields", "字段", 150, 0);
			GridHeaderDataMeta ascendingDataMeta = new GridHeaderDataMeta(id
					+ "_fields.ascending", "上升", null, false, 100);
			ascendingDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							Messages.getString("System.Combo.Yes"),
							Messages.getString("System.Combo.No") }, false));
			GridHeaderDataMeta fieldNameDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldName", "字段名称", null, false, 100);
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();

			fieldNameDataMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					fieldNameDataMeta, ascendingDataMeta });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.sortedmerge.btn.fieldsAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] fieldName = step.getFieldName();
			if (fieldName != null && fieldName.length > 0)
				for (int i = 0; i < fieldName.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, fieldName[i],
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step
									.getAscending()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.fields });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			
			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] { super
							.getGetfields("jQuery.imeta.steps.sortedmerge.btn.getfields") });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
