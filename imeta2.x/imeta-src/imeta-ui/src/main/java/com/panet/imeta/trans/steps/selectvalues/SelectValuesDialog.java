package com.panet.imeta.trans.steps.selectvalues;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SelectValuesDialog extends BaseStepDialog implements
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
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;
	
	private ButtonMeta getfields;

	//	
	// private ButtonMeta cowmapping;

	private LabelInputMeta compositor;

	/**
	 * 移除的字段
	 */
	private LabelGridMeta removefields;

	private ButtonMeta getremovefields;

	/**
	 * 元数据
	 */
	private LabelGridMeta metadata;

	private ButtonMeta getmetadata;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public SelectValuesDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SelectValuesMeta step = (SelectValuesMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id,
					new String[] { "选择和修改", "移除", "元数据" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0
			 ******************************************************************/
			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 200);
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta selectName = new GridHeaderDataMeta(id
					+ "_fields.selectName", "字段名称", null, false, 100);
			selectName.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							selectName,
					new GridHeaderDataMeta(id + "_fields.selectRename", "改成名",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.selectLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.selectPrecision",
							"精度", null, false, 340), });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.selectvalues.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
	                 "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] values = step.getSelectName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getSelectName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getSelectRename()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getSelectLength()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getSelectPrecision()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			
			DivMeta bodyfieldBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getfields = new ButtonMeta(
					id + ".btn.getfields", id + ".btn.getfields",
					"获取字段", "获取字段");
			this.getfields.appendTo(bodyfieldBtn);
			this.getfields
					.addClick("jQuery.imeta.steps.selectvalues.btn.getchoosefields");
			this.getfields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getfields.putProperty("roName", super.getTransMeta()
					.getName());
			this.getfields.putProperty("elementName", super
					.getStepName());
			this.getfields.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			
			// this.cowmapping = new ButtonMeta(id + ".btn.cowmapping", id
			// + ".btn.cowmapping", "列映射", "列映射");
			// this.cowmapping.addClick("jQuery.imeta.steps.excelinput.cowmapping");
			// this.cowmapping.appendTo(div);
			this.compositor = new LabelInputMeta(id + ".compositor",
					"包含未指定的列，按名称排序", null, null, null, null,
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.compositor.setSingle(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.fields, bodyfieldBtn,
					this.compositor });

			/*******************************************************************
			 * 标签1
			 ******************************************************************/
			// 移除的字段
			this.removefields = new LabelGridMeta(id + "_removefields",
					"移除的字段", 200);
			
			RowMetaInterface ra = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNamesA = ra.getFieldNames();
			GridHeaderDataMeta deleteName = new GridHeaderDataMeta(id
					+ "_removefields.deleteName", "字段名称", null, false, 100);
			deleteName.setOptions(super.getOptionsByStringArray(
					resultNamesA, true));
			
			this.removefields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_removefields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							deleteName

			});
			this.removefields.setHasBottomBar(true);
			this.removefields.setHasAdd(true, true,
					"jQuery.imeta.steps.selectvalues.btn.removeAdd");
			this.removefields.setHasDelete(true, true,
                    "jQuery.imeta.parameter.fieldsDelete");
			this.removefields.setSingle(true);

			String[] remove = step.getDeleteName();
			if (remove != null && remove.length > 0)
				for (int i = 0; i < remove.length; i++) {

					this.removefields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getDeleteName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			DivMeta diva = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getremovefields = new ButtonMeta(
					id + ".btn.getremovefields", id + ".btn.getremovefields",
					"获取字段", "获取字段");
			this.getremovefields.appendTo(diva);
			this.getremovefields
					.addClick("jQuery.imeta.steps.selectvalues.btn.getremovefields");
			this.getremovefields.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getremovefields.putProperty("roName", super.getTransMeta()
					.getName());
			this.getremovefields.putProperty("elementName", super
					.getStepName());
			this.getremovefields.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.removefields,
					diva });

			/*******************************************************************
			 * 标签2
			 ******************************************************************/

			// 需要改变元数据的字段
			this.metadata = new LabelGridMeta(id + "_metadata", "移除的字段", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_metadata.type", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMeta.typeCodes, false));

			GridHeaderDataMeta fieldBooleanTypeDataMeta = new GridHeaderDataMeta(
					id + "_metadata.storageType", "二进制？", null, false, 80);
			fieldBooleanTypeDataMeta.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			RowMetaInterface rb = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNamesB = rb.getFieldNames();
			GridHeaderDataMeta nameMeta = new GridHeaderDataMeta(id
					+ "_metadata.name", "字段名称", null, false, 100);
			nameMeta.setOptions(super.getOptionsByStringArray(
					resultNamesB, true));
			
			this.metadata.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_metadata.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							nameMeta,
					new GridHeaderDataMeta(id + "_metadata.rename", "改成名",
							null, false, 80),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_metadata.length", "长度", null,
							false, 50),
					new GridHeaderDataMeta(id + "_metadata.precision", "精度",
							null, false, 50),
					fieldBooleanTypeDataMeta,
					new GridHeaderDataMeta(id + "_metadata.conversionMask",
							"格式", null, false, 50),
					new GridHeaderDataMeta(id + "_metadata.decimalSymbol",
							"十进制", null, false, 80),
					new GridHeaderDataMeta(id + "_metadata.groupingSymbol",
							"分组", null, false, 50),
					new GridHeaderDataMeta(id + "_metadata.currencySymbol",
							"货币", null, false, 80) });
			this.metadata.setHasBottomBar(true);
			this.metadata.setHasAdd(true, true,
					"jQuery.imeta.steps.selectvalues.btn.metaAdd");
			this.metadata.setHasDelete(true, true,
                    "jQuery.imeta.parameter.fieldsDelete");
			this.metadata.setSingle(true);

			SelectMetadataChange[] selectMetadata = step.getMeta();
			if (selectMetadata != null && selectMetadata.length > 0)
				for (int i = 0; i < selectMetadata.length; i++) {
					this.metadata.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, selectMetadata[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, selectMetadata[i].getRename(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(selectMetadata[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(selectMetadata[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(selectMetadata[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(selectMetadata[i]
											.getStorageType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, selectMetadata[i]
											.getConversionMask(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, selectMetadata[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, selectMetadata[i]
									.getGroupingSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, selectMetadata[i]
									.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			DivMeta divB = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getmetadata = new ButtonMeta(
					id + ".btn.getmetadata", id + ".btn.getmetadata",
					"获取字段", "获取字段");
			this.getmetadata.appendTo(divB);
			this.getmetadata
					.addClick("jQuery.imeta.steps.selectvalues.btn.getmetadata");
			this.getmetadata.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getmetadata.putProperty("roName", super.getTransMeta()
					.getName());
			this.getmetadata.putProperty("elementName", super
					.getStepName());
			this.getmetadata.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			
			this.meta.putTabContent(2,
					new BaseFormMeta[] { this.metadata, divB });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
