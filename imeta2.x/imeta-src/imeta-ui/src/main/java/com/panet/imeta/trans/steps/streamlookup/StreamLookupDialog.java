package com.panet.imeta.trans.steps.streamlookup;

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
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class StreamLookupDialog extends BaseStepDialog implements
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
	 * 查找步骤
	 */
	private LabelSelectMeta lookupFromStepname;

	/**
	 * 查询值所需的关键字
	 */
	private LabelGridMeta keys;

	/**
	 * 指定用来接收的字段
	 */
	private LabelGridMeta values;

	/**
	 * inputSorted
	 */
	private LabelInputMeta inputSorted;
	
	/**
	 * 保留内存（消耗cpu）
	 */
	private LabelInputMeta memoryPreservationActive;

	/**
	 * 关键值是个整数域
	 */
	private LabelInputMeta usingIntegerPair;
	/**
	 * 使用排列名单
	 */
	private LabelInputMeta usingSortedList;

	/**
	 * 获取获取查找字段
	 */
	private ButtonMeta getkeywords;

	public StreamLookupDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			StreamLookupMeta step = (StreamLookupMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"流查询", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到查询步骤

			this.lookupFromStepname = new LabelSelectMeta(id + ".lookupFromStepname",
					"查找步骤", null, null, null, step.getLookupStepname(), null,
					super.getPrevStepNames());
			this.lookupFromStepname.setSingle(true);
			this.lookupFromStepname.setHasEmpty(true);

			// 得到查询值所需的关键字
			this.keys = new LabelGridMeta(id + "_keys", "查询值所需的关键字：", 200);

			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta keystream = new GridHeaderDataMeta(id
					+ "_keys.keystream", "字段", null, false, 200);
			keystream.setOptions(super.getOptionsByStringArray(resultNames,
					true));

			RowMetaInterface rl = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultLookup = rl.getFieldNames();
			GridHeaderDataMeta keylookup = new GridHeaderDataMeta(id
					+ "_keys.keylookup", "查询字段", null, false, 200);
			keylookup.setOptions(super.getOptionsByStringArray(resultLookup,
					true));

			this.keys.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keys.keysId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					keystream, keylookup });
			this.keys.setHasBottomBar(true);
			this.keys.setHasAdd(true, true,
					"jQuery.imeta.steps.streamlookup.btn.keysAdd");
			this.keys.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.keys.setSingle(true);

			String[] values = step.getKeystream();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.keys.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getKeylookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			// 得到指定用来接收的字段
			this.values = new LabelGridMeta(id + "_values", "指定用来接收的字段：", 200);

			RowMetaInterface rV = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultValueNames = rV.getFieldNames();
			GridHeaderDataMeta valueFields = new GridHeaderDataMeta(id
					+ "_values.value", "字段", null, false, 100);
			valueFields.setOptions(super.getOptionsByStringArray(
					resultValueNames, true));

			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_values.valueDefaultType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.typeCodes, false));

			this.values.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_values.valuesId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					valueFields,
					new GridHeaderDataMeta(id + "_values.valueName", "新的名称",
							null, false, 100),
					new GridHeaderDataMeta(id + "_values.valueDefault", "默认",
							null, false, 100), fieldTypeDataMeta });
			this.values.setHasBottomBar(true);
			this.values.setHasAdd(true, true,
					"jQuery.imeta.steps.streamlookup.btn.valuesAdd");
			this.values.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.values.setSingle(true);

			String[] value = step.getValue();
			String[] valueName = step.getValueName();
			String[] valueDefault = step.getValueDefault();
			int[] valueDefaultType = step.getValueDefaultType();
			if (value != null && value.length > 0)
				for (int i = 0; i < value.length; i++) {
					GridCellDataMeta val, valname, valuedefault, defaultType;
					val = new GridCellDataMeta(null, value[i],
									GridCellDataMeta.CELL_TYPE_INPUT);
					valname = new GridCellDataMeta(null, valueName[i],
									GridCellDataMeta.CELL_TYPE_INPUT);
					valuedefault = new GridCellDataMeta(null,
							valueDefault[i],
									GridCellDataMeta.CELL_TYPE_INPUT);
					defaultType = new GridCellDataMeta(null, String.valueOf(valueDefaultType[i]),
									GridCellDataMeta.CELL_TYPE_SELECT);
					this.values.addRow(new Object[] {
							 String.valueOf(i + 1),val,valname,valuedefault,defaultType});
				}

			//输入类别
			this.inputSorted = new LabelInputMeta(id
					+ ".inputSorted", "输入类别", null, null,
					null, String.valueOf(step.isInputSorted()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.inputSorted.setSingle(true);
			
			// 保留内存（消耗cpu）
			this.memoryPreservationActive = new LabelInputMeta(id
					+ ".memoryPreservationActive", "保留内存（消耗cpu）", null, null,
					null, String.valueOf(step.isMemoryPreservationActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.memoryPreservationActive.setSingle(true);

			// 关键值是个整数域
			this.usingIntegerPair = new LabelInputMeta(
					id + ".usingIntegerPair", "关键值是个整数域", null, null, null,
					String.valueOf(step.isUsingIntegerPair()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.usingIntegerPair.setSingle(true);

			// 使用排列名单
			this.usingSortedList = new LabelInputMeta(id + ".usingSortedList",
					"使用类别名单", null, null, null, String.valueOf(step
							.isUsingSortedList()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.usingSortedList.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.lookupFromStepname, this.keys, this.values,
					this.inputSorted, this.memoryPreservationActive, this.usingIntegerPair,
					this.usingSortedList });

			this.getkeywords = new ButtonMeta(id + ".btn.getkeywords", id
					+ ".btn.streamlookup", "获取查找字段", "获取查找字段");
			this.getkeywords
					.addClick("jQuery.imeta.steps.streamlookup.btn.getkeywords");
			this.getkeywords.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getkeywords.putProperty("roName", super.getTransMeta()
					.getName());
			this.getkeywords.putProperty("elementName", super.getStepName());
			this.getkeywords.putProperty("directoryId", super.getTransMeta()
					.getDirectory().getID());
			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super.getOkBtn(),
							super.getCancelBtn(),
							super
									.getGetfields("jQuery.imeta.steps.streamlookup.btn.getfields"),
							this.getkeywords });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
