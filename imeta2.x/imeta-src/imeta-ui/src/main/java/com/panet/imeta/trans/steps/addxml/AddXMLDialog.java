package com.panet.imeta.trans.steps.addxml;

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
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AddXMLDialog extends BaseStepDialog implements StepDialogInterface {
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
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Output Value
	 */
	private LabelInputMeta valueName;

	/**
	 * Root XML element
	 */
	private LabelInputMeta rootNode;

	/**
	 * Omit XML header
	 */
	private LabelInputMeta omitXMLheader;

	private LabelInputMeta attributeNode;

	/**
	 * 字段
	 */
	private GridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public AddXMLDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			AddXMLMeta step = (AddXMLMeta) super.getStep();
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0
			 ******************************************************************/
			// Encoding
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, super.getEncoding());
			this.encoding.setSingle(true);

			// Output Value
			this.valueName = new LabelInputMeta(id + ".valueName", "输出值", null,
					null, null, step.getValueName(), null, null);
			this.valueName.setSingle(true);

			// Root XML element
			this.rootNode = new LabelInputMeta(id + ".rootNode", "XML根节点",
					null, null, null, step.getRootNode(), null, null);
			this.rootNode.setSingle(true);

			// Omit XML header
			this.omitXMLheader = new LabelInputMeta(id + ".omitXMLheader",
					"是否省略XML标题", null, null, null, String.valueOf(step
							.isOmitXMLheader()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.attributeNode = new LabelInputMeta(id + ".attributeNode",
					"是否属性片段", null, null, null, String.valueOf(step
							.isAttributeNode()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.encoding,
					this.valueName, this.rootNode, this.omitXMLheader,
					this.attributeNode });

			/*******************************************************************
			 * 标签1
			 ******************************************************************/
			// 字段
			this.fields = new GridMeta(id + "_fields", 200, 0, true);

			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArray(
					ValueMeta.typeCodes, false));

			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "字段名",
							null, false, 150),
					new GridHeaderDataMeta(id + "_fields.fieldElement", "元素名称",
							null, false, 100),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精密",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency",
							"货币类型", null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "小数",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldNullif", "空字符串",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldAttribute",
							"属性", null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)),
					new GridHeaderDataMeta(id
							+ "_fields.fieldAttributeParentName", "父属性名", null,
							false, 100) });

			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.addxml.btn.fieldsAdd");
			this.fields.setHasDelete(true, true,
	                "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			XMLField[] outputFields = step.getOutputFields();
			for (int i = 0; i < outputFields.length; i++) {
				XMLField field = outputFields[i];
				this.fields
						.addRow(new Object[] {
								String.valueOf(i + 1),
								new GridCellDataMeta(null,
										field.getFieldName(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field
										.getElementName(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field.getTypeDesc(),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, field.getFormat(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getLength()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getPrecision()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field
										.getCurrencySymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field
										.getDecimalSymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field
										.getGroupingSymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, field
										.getNullString(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.isAttribute()),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, field
										.getAttributeParentName(),
										GridCellDataMeta.CELL_TYPE_INPUT) });
			}

			// Get Fields
			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.addxml.btn.getfields")
					.appendTo(div);

			this.meta.putTabContent(1, new BaseFormMeta[] { this.fields, div });

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
