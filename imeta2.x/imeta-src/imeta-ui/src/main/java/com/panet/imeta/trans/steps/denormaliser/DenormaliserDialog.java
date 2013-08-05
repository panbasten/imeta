package com.panet.imeta.trans.steps.denormaliser;

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
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class DenormaliserDialog extends BaseStepDialog implements
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
	 * 关键字段
	 */
	private LabelSelectMeta keyField;
	
	/**
	 * 构成分组的字段
	 */
	private LabelGridMeta consistwords;
	private ButtonMeta getBodyFieldsBtn;

	/**
	 * 目标字段
	 */
	private LabelGridMeta obwords;
	private ButtonMeta getQueryFieldsBtn;
	
	public DenormaliserDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			DenormaliserMeta step = (DenormaliserMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 关键字段
			this.keyField = new LabelSelectMeta(id + ".keyField", "关键字段", null, null,
					null, step.getKeyField(), null, super.getPrevStepResultFields());
			this.keyField.setSingle(true);
			
            // 构成分组的字段
			
			this.consistwords = new LabelGridMeta(id + "_consistwords", "构成分组的字段：", 200);
			this.consistwords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_consistwords.fieldId", "#",GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_consistwords.groupField", "分组字段", null, false,535),
					});
			this.consistwords.setHasBottomBar(true);
			this.consistwords.setHasAdd(true, true,
					"jQuery.imeta.steps.denormaliser.btn.consistwordsAdd");
			this.consistwords.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.consistwords.setSingle(true);
			
			String[] field = step.getGroupField();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.consistwords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getGroupField()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			
			DivMeta bodyfieldBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getBodyFieldsBtn = new ButtonMeta(
					id + ".btn.getBodyFieldsBtn", id + ".btn.getBodyFieldsBtn",
					"获取字段", "获取字段");
			this.getBodyFieldsBtn.appendTo(bodyfieldBtn);
			this.getBodyFieldsBtn
					.addClick("jQuery.imeta.steps.denormaliser.btn.getwords");
			this.getBodyFieldsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getBodyFieldsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getBodyFieldsBtn.putProperty("elementName", super
					.getStepName());
			this.getBodyFieldsBtn.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());

			
            // 目标字段
			
			this.obwords = new LabelGridMeta(id + "_obwords", "目标字段：", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_obwords.targetType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMeta.typeCodes, false));
			
			this.obwords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_obwords.fieldId", "#",GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetName", "目标字段", null, false, 80),
					new GridHeaderDataMeta(id + "_obwords.fieldName", "值字段", null, false, 80),
					new GridHeaderDataMeta(id + "_obwords.keyValue", "关键字值", 	 null, false, 80),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_obwords.targetFormat", "格式化", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetLength", "长度", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetPrecision", "精度", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetCurrencySymbol", "通用", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetDecimalSymbol", "十进制", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetGroupingSymbol", "分组", null, false, 50),
					new GridHeaderDataMeta(id + "_obwords.targetNullString", "如果为空", null, false, 80),
					(new GridHeaderDataMeta(id + "_obwords.targetAggregationType",
							"聚合", null, false, 120))
							.setOptions(super
									.getOptionsByStringArrayWithNumberValue(
											DenormaliserTargetField.typeAggrDescs,
											false))
					});
			this.obwords.setHasBottomBar(true);
			this.obwords .setHasAdd(true, true,
					"jQuery.imeta.steps.denormaliser.btn.obwordsAdd");
			this.obwords.setHasDelete(true, true,
	                "jQuery.imeta.parameter.fieldsDelete");
			this.obwords.setSingle(true);
			
			DenormaliserTargetField[] denormaliserTargetField = step.getDenormaliserTargetField();
			if (denormaliserTargetField != null && denormaliserTargetField.length > 0)
				for (int i = 0; i < denormaliserTargetField.length; i++) {
					this.obwords.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetName(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getFieldName(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getKeyValue(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(denormaliserTargetField[i].getTargetType()),GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetFormat(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(denormaliserTargetField[i].getTargetLength()),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(denormaliserTargetField[i].getTargetPrecision()),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetCurrencySymbol(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetDecimalSymbol(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetGroupingSymbol(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, denormaliserTargetField[i].getTargetNullString(),GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(denormaliserTargetField[i].getTargetAggregationType()),GridCellDataMeta.CELL_TYPE_SELECT)
					});
				}

			DivMeta queryfieldBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getQueryFieldsBtn = new ButtonMeta(id
					+ ".btn.getQueryFieldsBtn", id + ".btn.getQueryFieldsBtn",
					"获取字段", "获取字段");
			this.getQueryFieldsBtn.appendTo(queryfieldBtn);
			this.getQueryFieldsBtn
					.addClick("jQuery.imeta.steps.denormaliser.btn.getdemandwords");
			this.getQueryFieldsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getQueryFieldsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getQueryFieldsBtn.putProperty("elementName", super
					.getStepName());
			this.getQueryFieldsBtn.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
//			 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.keyField, this.consistwords,
					bodyfieldBtn, this.obwords, 
					queryfieldBtn});

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
