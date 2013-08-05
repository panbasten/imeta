package com.panet.imeta.trans.steps.scriptvalues_mod;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.LabelDataMeta;
import com.panet.iform.core.base.LabelMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.tree.TreeMeta;
import com.panet.iform.forms.tree.TreeNodeDataMeta;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class ScriptValuesModDialog extends BaseStepDialog implements
		StepDialogInterface {
	public static final String[] JAVASCRIPT_FUNCTION_CONSTANTS = new String[] {
			"SKIP_TRANSFORMATION", "ERROR_TRANSFORMATION",
			"CONTINUE_TRANSFORMATION" };

	public static ScriptValuesHelp scVHelp = null;

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private ScriptValuesMetaMod step;

	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	private LabelTextareaMeta js;

	/**
	 * 替换JS中的变量
	 */
	private LabelInputMeta variableReplacementActive;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 * @throws KettleXMLException
	 */
	public ScriptValuesModDialog(StepMeta stepMeta, TransMeta transMeta)
			throws KettleXMLException {
		super(stepMeta, transMeta);
		if (scVHelp == null) {
			scVHelp = new ScriptValuesHelp("jsFunctionHelp.xml");
		}
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			step = (ScriptValuesMetaMod) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			/*******************************************************************
			 * 0 基本
			 ******************************************************************/
			DivMeta jsDiv = addGeneralTab(id);

			// 替换SQL语句的变量
			this.variableReplacementActive = new LabelInputMeta(id
					+ ".variableReplacementActive", "替换Script语句的变量", null,
					null, null, String.valueOf(step
							.isVariableReplacementActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			/*******************************************************************
			 * 1 高级
			 ******************************************************************/
			addFieldsTab(id);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					jsDiv, this.variableReplacementActive, this.fields });

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

	private DivMeta addGeneralTab(String id) throws KettleStepException,
			ImetaFormException {

		// js的层
		DivMeta jsDiv = new DivMeta(new NullSimpleFormDataMeta(), true);
		jsDiv.setStyle("height", "360px");
		DivMeta jsFnDiv = new DivMeta(new SimpleFormDataMeta(null, null,
				new String[] { "toolbar-content" }, null, null, null), false);
		jsFnDiv.setStyle("width", "33%");
		jsFnDiv.setStyle("height", "350px");
		jsFnDiv.setStyle("float", "left");
		jsFnDiv.appendTo(jsDiv);
		jsFnDiv
				.addInit("$('#"
						+ id
						+ " .normal-file').mouseover(jQuery.imeta.steps.scriptvalues_mod.listeners.itemMouseOver);");
		jsFnDiv
				.addInit("$('#"
						+ id
						+ " .normal-file').mouseout(jQuery.imeta.steps.scriptvalues_mod.listeners.itemMouseOut);");
		jsFnDiv
				.addInit("$('#"
						+ id
						+ " .normal-file').click(jQuery.imeta.steps.scriptvalues_mod.listeners.itemClick);");

		LabelMeta jsFnLabel = new LabelMeta(new LabelDataMeta(id
				+ ".jsFn.label", id + ".jsFn.label", "JavaScript函数", null),
				true);
		jsFnLabel.appendTo(jsFnDiv);

		// 常量
		TreeNodeDataMeta jsFnNodesConstants = new TreeNodeDataMeta(id
				+ "_jsFnConstants", null, "转换常量", "转换常量", false, false);
		jsFnNodesConstants
				.addInit("$('[id=treeRoot."
						+ id
						+ "_jsFnConstants]').treeview({animated: 'fast',collapsed: false});");
		for (String c : JAVASCRIPT_FUNCTION_CONSTANTS) {
			TreeNodeDataMeta jsFnNodesConstantsItem = new TreeNodeDataMeta(id
					+ "_jsFnConstants." + c, null, c, c, true, true);
			jsFnNodesConstantsItem.putNodeExtend("rootId", id);
			jsFnNodesConstants.putSubNode(jsFnNodesConstantsItem);
		}

		TreeMeta jsFnConstants = new TreeMeta(
				new TreeNodeDataMeta[] { jsFnNodesConstants });
		jsFnConstants.setRootTree(true);
		jsFnConstants.appendTo(jsFnDiv);

		// 函数
		TreeNodeDataMeta jsFnNodesFunctions = new TreeNodeDataMeta(id
				+ "_jsFnFunctions", null, "转换函数", "转换函数", true, false);
		jsFnNodesFunctions
				.addInit("$('[id=treeRoot."
						+ id
						+ "_jsFnFunctions]').treeview({animated: 'fast',collapsed: false});");
		// 0.字符串函数组
		TreeNodeDataMeta jsFnNodesFunctionsString = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "字符串函数组", "字符串函数组", true, false);
		// 1.数字函数组
		TreeNodeDataMeta jsFnNodesFunctionsNumeric = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "数字函数组", "数字函数组", true, false);
		// 2.日期函数组
		TreeNodeDataMeta jsFnNodesFunctionsDate = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "日期函数组", "日期函数组", true, false);
		// 3.逻辑函数组
		TreeNodeDataMeta jsFnNodesFunctionsLogic = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "逻辑函数组", "逻辑函数组", true, false);
		// 4.特殊函数组
		TreeNodeDataMeta jsFnNodesFunctionsSpecial = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "特殊函数组", "特殊函数组", true, false);
		// 5.文件函数组
		TreeNodeDataMeta jsFnNodesFunctionsFile = new TreeNodeDataMeta(id
				+ "_jsFnFunctionsString", null, "文件函数组", "文件函数组", true, false);

		Hashtable<String, String> hatFunctions = scVHelp.getFunctionList();

		Vector<String> v = new Vector<String>(hatFunctions.keySet());
		Collections.sort(v);

		for (String strFunction : v) {
			String strFunctionType = (String) hatFunctions.get(strFunction);
			int iFunctionType = Integer.valueOf(strFunctionType).intValue();
			TreeNodeDataMeta fn = new TreeNodeDataMeta(null, null, strFunction,
					strFunction, true, true);
			fn.putNodeExtend("rootId", id);
			switch (iFunctionType) {
			case ScriptValuesAddedFunctions.STRING_FUNCTION:
				jsFnNodesFunctionsString.putSubNode(fn);
				break;
			case ScriptValuesAddedFunctions.NUMERIC_FUNCTION:
				jsFnNodesFunctionsNumeric.putSubNode(fn);
				break;
			case ScriptValuesAddedFunctions.DATE_FUNCTION:
				jsFnNodesFunctionsDate.putSubNode(fn);
				break;
			case ScriptValuesAddedFunctions.LOGIC_FUNCTION:
				jsFnNodesFunctionsLogic.putSubNode(fn);
				break;
			case ScriptValuesAddedFunctions.SPECIAL_FUNCTION:
				jsFnNodesFunctionsSpecial.putSubNode(fn);
				break;
			case ScriptValuesAddedFunctions.FILE_FUNCTION:
				jsFnNodesFunctionsFile.putSubNode(fn);
				break;
			}

		}
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsString);
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsNumeric);
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsDate);
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsLogic);
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsSpecial);
		jsFnNodesFunctions.putSubNode(jsFnNodesFunctionsFile);

		TreeMeta jsFnFunctions = new TreeMeta(
				new TreeNodeDataMeta[] { jsFnNodesFunctions });
		jsFnFunctions.setRootTree(true);
		jsFnFunctions.appendTo(jsFnDiv);

		// 输入字段
		TreeNodeDataMeta jsFnNodesInputFields = new TreeNodeDataMeta(id
				+ "_jsFnInputFields", null, "输入字段", "输入字段", true, false);
		jsFnNodesInputFields.putNodeExtend("rootId", id);
		jsFnNodesInputFields
				.addInit("$('[id=treeRoot."
						+ id
						+ "_jsFnInputFields]').treeview({animated: 'fast',collapsed: false});");

		// 输出字段
		TreeNodeDataMeta jsFnNodesOutputFields = new TreeNodeDataMeta(id
				+ "_jsFnOutputFields", null, "输出字段", "输出字段", true, false);
		jsFnNodesOutputFields.putNodeExtend("rootId", id);
		jsFnNodesOutputFields
				.addInit("$('[id=treeRoot."
						+ id
						+ "_jsFnOutputFields]').treeview({animated: 'fast',collapsed: false});");

		RowMetaInterface rowPrevStepFields = super.getTransMeta()
				.getPrevStepFields(super.getStepMeta());
		String strItemInToAdd, strItemToAddOut;
		for (int i = 0; i < rowPrevStepFields.size(); i++) {
			ValueMetaInterface val = rowPrevStepFields.getValueMeta(i);
			strItemToAddOut = val.getName() + ".setValue(var)";

			switch (val.getType()) {
			case ValueMetaInterface.TYPE_STRING:
				strItemInToAdd = val.getName() + ".getString()";
				break;
			case ValueMetaInterface.TYPE_NUMBER:
				strItemInToAdd = val.getName() + ".getNumber()";
				break;
			case ValueMetaInterface.TYPE_INTEGER:
				strItemInToAdd = val.getName() + ".getInteger()";
				break;
			case ValueMetaInterface.TYPE_DATE:
				strItemInToAdd = val.getName() + ".getDate()";
				break;
			case ValueMetaInterface.TYPE_BOOLEAN:
				strItemInToAdd = val.getName() + ".getBoolean()";
				strItemToAddOut = val.getName() + ".setValue(var)";
				break;
			case ValueMetaInterface.TYPE_BIGNUMBER:
				strItemInToAdd = val.getName() + ".getBigNumber()";
				break;
			case ValueMetaInterface.TYPE_BINARY:
				strItemInToAdd = val.getName() + ".getBytes()";
				break;
			case ValueMetaInterface.TYPE_SERIALIZABLE:
				strItemInToAdd = val.getName() + ".getSerializable()";
				break;
			default:
				strItemInToAdd = val.getName();
				strItemToAddOut = val.getName();
				break;
			}

			TreeNodeDataMeta in = new TreeNodeDataMeta( id
					+ "_jsFnInputFields." + strItemInToAdd, null,
					strItemInToAdd, strItemInToAdd, true, true);
			in.putNodeExtend("rootId", id);
			jsFnNodesInputFields.putSubNode(in);
			
			TreeNodeDataMeta out = new TreeNodeDataMeta(id
					+ "_jsFnOutputFields." + strItemToAddOut, null,
					strItemToAddOut, strItemToAddOut, true, true);
			out.putNodeExtend("rootId", id);
			jsFnNodesOutputFields.putSubNode(out);
		}

		TreeMeta jsFnInputFields = new TreeMeta(
				new TreeNodeDataMeta[] { jsFnNodesInputFields });
		jsFnInputFields.setRootTree(true);
		jsFnInputFields.appendTo(jsFnDiv);

		TreeMeta jsFnOutputFields = new TreeMeta(
				new TreeNodeDataMeta[] { jsFnNodesOutputFields });
		jsFnOutputFields.setRootTree(true);
		jsFnOutputFields.appendTo(jsFnDiv);

		// 编辑
		DivMeta jsEditDiv = new DivMeta(new NullSimpleFormDataMeta(), false);
		jsEditDiv.setStyle("width", "65%");
		jsEditDiv.setStyle("padding-left", "10px");
		jsEditDiv.setStyle("float", "left");
		jsEditDiv.appendTo(jsDiv);

		ScriptValuesScript[] jsValues = step.getJSScripts();
		String script;
		if (jsValues != null && jsValues.length > 0) {
			script = jsValues[0].getScript();
		} else {
			script = "//在这里编辑JavaScript脚本";
		}

		js = new LabelTextareaMeta(id + ".js", "JavaScript编辑", null, null,
				"JavaScript编辑", script, 22, ValidateForm.getInstance()
						.setRequired(true));
		js.setLayout(LabelTextareaMeta.LAYOUT_COLUMN);

		js.setSingle(true);
		js.setWidth(99);
		js.appendTo(jsEditDiv);

		return jsDiv;
	}

	private void addFieldsTab(String id) throws ImetaFormException {
		// 字段
		this.fields = new LabelGridMeta(id + "_fields", "字段", 200);
		GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
				+ "_fields.type", "类型", null, false, 100);
		fieldTypeDataMeta.setOptions(super
				.getOptionsByStringArrayWithNumberValue(ValueMeta.typeCodes,
						false));

		this.fields.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_fields.fieldId", "#",
						GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
				new GridHeaderDataMeta(id + "_fields.name", "字段名", null, false,
						100),
				new GridHeaderDataMeta(id + "_fields.rename", "改名为", null,
						false, 100),
				fieldTypeDataMeta,
				new GridHeaderDataMeta(id + "_fields.length", "长度", null,
						false, 50),
				new GridHeaderDataMeta(id + "_fields.precision", "精度", null,
						false, 70) });
		this.fields.setHasBottomBar(true);
		this.fields.setHasAdd(true, true,
				"jQuery.imeta.steps.scriptvalues_mod.btn.fieldAdd");
		this.fields.setHasDelete(true, true,
		        "jQuery.imeta.parameter.fieldsDelete");
		this.fields.setSingle(true);

		String[] values = step.getName();
		if (values != null && values.length > 0)
			for (int i = 0; i < values.length; i++) {

				this.fields.addRow(new Object[] {
						String.valueOf(i + 1),
						new GridCellDataMeta(null, values[i],
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, step.getRename()[i],
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String.valueOf(step
								.getType()[i]),
								GridCellDataMeta.CELL_TYPE_SELECT),
						new GridCellDataMeta(null, String.valueOf(step
								.getLength()[i]),
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, String.valueOf(step
								.getPrecision()[i]),
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}
	}

}
