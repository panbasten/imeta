package com.panet.imeta.trans.steps.mergerows;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MergeRowsDialog extends BaseStepDialog implements
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
	 * 旧数据源
	 */
	private LabelSelectMeta referenceStepName;

	/**
	 * 新数据源
	 */
	private LabelSelectMeta compareStepName;

	/**
	 * 标志字段
	 */
	private LabelInputMeta flagField;

	/**
	 * 比配的关键字
	 */
	private LabelGridMeta matchkeywords;
	private ButtonMeta getKeywordsBtn;

	/**
	 * 数据字段
	 */
	private LabelGridMeta dateField;
	private ButtonMeta getValuewordsBtn;

	public MergeRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			MergeRowsMeta step = (MergeRowsMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"", super.getStepName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.name.setSingle(true);

			// 旧数据源
			this.referenceStepName = new LabelSelectMeta(id
					+ ".referenceStepName", "旧数据源", null, null, null, step
					.getReferenceStepName(), null, super.getPrevStepNames());
			this.referenceStepName.setSingle(true);

			// 新数据源
			this.compareStepName = new LabelSelectMeta(id + ".compareStepName",
					"新数据源", null, null, null, step.getCompareStepName(), null,
					super.getPrevStepNames());
			this.compareStepName.setSingle(true);

			// 标志字段
			this.flagField = new LabelInputMeta(id + ".flagField", "标志字段",
					null, null, "标志字段必须填写", step.getFlagField(), null,
					ValidateForm.getInstance().setRequired(false));
			this.flagField.setSingle(true);

			// 匹配的关键字
			this.matchkeywords = new LabelGridMeta(id + "_matchkeywords",
					"匹配的关键字", 200, 0);
			this.matchkeywords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_matchkeywords.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_matchkeywords.fieldName",
							"关键字段", null, false, 100) });
			this.matchkeywords.setHasBottomBar(true);
			this.matchkeywords.setHasAdd(true, true,
					"jQuery.imeta.steps.mergerows.btn.matchkeywordsAdd");
			this.matchkeywords.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			// this.matchkeywords.setHasDelete(true, true,
			// "jQuery.imeta.steps.mergerows.btn.matchkeywordsDelete");
			this.matchkeywords.setSingle(true);
			String[] keys = step.getKeyFields();
			if (keys != null && keys.length > 0)
				for (int i = 0; i < keys.length; i++) {
					this.matchkeywords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, keys[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}

			// 数据字段
			this.dateField = new LabelGridMeta(id + "_dateField", "数据字段", 200,
					0);
			this.dateField.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_dateField.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_dateField.valueFields",
							"数据字段", null, false, 100) });
			this.dateField.setHasBottomBar(true);
			this.dateField.setHasAdd(true, true,
					"jQuery.imeta.steps.mergerows.btn.dateFieldAdd");
			this.dateField.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			// this.dateField.setHasDelete(true, true,
			// "jQuery.imeta.steps.mergerows.btn.matchkeywordsDelete");
			this.dateField.setSingle(true);
			String[] datas = step.getValueFields();
			if (datas != null && datas.length > 0)
				for (int i = 0; i < datas.length; i++) {
					this.dateField.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, datas[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}

			DivMeta diva = new DivMeta(new NullSimpleFormDataMeta(), true);
//			 super.getGetfields("jQuery.imeta.steps.mergerows.btn.getKeywords")
//			 .appendTo(diva);

			this.getKeywordsBtn = new ButtonMeta(id + ".btn.getfields", id
					+ ".btn.getfields", "获取字段", "获取字段");
			this.getKeywordsBtn
					.addClick("jQuery.imeta.steps.mergerows.btn.getKeywords");
			this.getKeywordsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getKeywordsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getKeywordsBtn.putProperty("elementName", super.getStepName());
			this.getKeywordsBtn.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			this.getKeywordsBtn.appendTo(diva);
			DivMeta divb = new DivMeta(new NullSimpleFormDataMeta(), true);

			this.getValuewordsBtn = new ButtonMeta(id + ".btn.getfields1", id
					+ ".btn.getfields1", "获取字段", "获取字段");
			this.getValuewordsBtn
					.addClick("jQuery.imeta.steps.mergerows.btn.getValue");
			this.getValuewordsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getValuewordsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getValuewordsBtn.putProperty("elementName", super
					.getStepName());
			this.getValuewordsBtn.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			this.getValuewordsBtn.appendTo(divb);
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.referenceStepName, this.compareStepName,
					this.flagField, this.matchkeywords, diva, this.dateField,
					divb });

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
