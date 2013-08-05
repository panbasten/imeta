package com.panet.imeta.trans.steps.xmljoin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class XMLJoinDialog extends BaseStepDialog implements
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
	 * Target stream properties
	 */
	private ColumnFieldsetMeta targetStream;

	/**
	 * Target XML step
	 */
	private LabelSelectMeta targetXMLstep;

	/**
	 * Target XML field
	 */
	private LabelInputMeta targetXMLfield;

	/**
	 * Source stream properties
	 */
	private ColumnFieldsetMeta sourceStream;

	/**
	 * Source XML step
	 */
	private LabelSelectMeta sourceXMLstep;

	/**
	 * Source XML field
	 */
	private LabelInputMeta sourceXMLfield;

	/**
	 * Source XML not include root
	 */
	private LabelInputMeta sourceNotIncludeRoot;

	/**
	 * Join condition properties
	 */
	private ColumnFieldsetMeta joinConditionProperties;

	/**
	 * XPath Statement
	 */
	private LabelInputMeta targetXPath;

	/**
	 * Complex join
	 */
	private LabelInputMeta complexJoin;

	/**
	 * Join comparison
	 */
	private LabelInputMeta joinCompareField;

	/**
	 * Result stream properties
	 */
	private ColumnFieldsetMeta resultStream;

	/**
	 * Result XML field
	 */
	private LabelInputMeta valueXMLfield;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Omit XML header
	 */
	private LabelInputMeta omitXMLHeader;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public XMLJoinDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			XMLJoinMeta step = (XMLJoinMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Target stream properties
			this.targetStream = new ColumnFieldsetMeta(null, "目标流属性");
			this.targetStream.setSingle(true);

			// Target XML step
			this.targetXMLstep = new LabelSelectMeta(id + ".targetXMLstep",
					"目标XML步骤", null, null, null, step.getTargetXMLstep(), null,
					super.getPrevStepNames());
			this.targetXMLstep.setSingle(true);

			// Target XML field
			this.targetXMLfield = new LabelInputMeta(id + ".targetXMLfield",
					"目标XML字段", null, null, null, step.getTargetXMLfield(),
					null, ValidateForm.getInstance().setRequired(true));
			this.targetXMLfield.setSingle(true);

			this.targetStream.putFieldsetsContent(new BaseFormMeta[] {
					this.targetXMLstep, this.targetXMLfield });

			// Source stream properties
			this.sourceStream = new ColumnFieldsetMeta(null, "来源流属性");
			this.sourceStream.setSingle(true);

			// Source XML step
			this.sourceXMLstep = new LabelSelectMeta(id + ".sourceXMLstep",
					"来源XML步骤", null, null, null, step.getSourceXMLstep(), null,
					super.getPrevStepNames());
			this.sourceXMLstep.setSingle(true);

			// Source XML field
			this.sourceXMLfield = new LabelInputMeta(id + ".sourceXMLfield",
					"来源XML字段", null, null, null, step.getSourceXMLfield(),
					null, ValidateForm.getInstance().setRequired(true));
			this.sourceXMLfield.setSingle(true);

			this.sourceNotIncludeRoot = new LabelInputMeta(id
					+ ".sourceNotIncludeRoot", "来源XML不包含根节点", null, null, null,
					String.valueOf(step.isSourceNotIncludeRoot()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.sourceStream.putFieldsetsContent(new BaseFormMeta[] {
					this.sourceXMLstep, this.sourceXMLfield,
					this.sourceNotIncludeRoot });

			// Join condition properties
			this.joinConditionProperties = new ColumnFieldsetMeta(null,
					"连接条件属性");
			this.joinConditionProperties.setSingle(true);

			// XPath Statement
			this.targetXPath = new LabelInputMeta(id + ".targetXPath",
					"XPath语句", null, null, null, step.getTargetXPath(), null,
					ValidateForm.getInstance().setRequired(true));
			this.targetXPath.setSingle(true);

			// Complex join
			boolean isComplexJoin = step.isComplexJoin();
			this.complexJoin = new LabelInputMeta(id + ".complexJoin", "复杂连接",
					null, null, null, String.valueOf(isComplexJoin),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.complexJoin
					.addClick("jQuery.imeta.steps.xmljoin.listeners.isComplexJoinClick");

			// Join Comparison
			this.joinCompareField = new LabelInputMeta(
					id + ".joinCompareField", "连接比较字段", null, null, null, step
							.getJoinCompareField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.joinCompareField.setSingle(true);
			this.joinCompareField.setDisabled(!isComplexJoin);

			this.joinConditionProperties
					.putFieldsetsContent(new BaseFormMeta[] { this.targetXPath,
							this.complexJoin, this.joinCompareField });

			// Result stream properties
			this.resultStream = new ColumnFieldsetMeta(null, "结果流属性");
			this.resultStream.setSingle(true);

			// Result XML field
			this.valueXMLfield = new LabelInputMeta(id + ".valueXMLfield",
					"结果XML字段", null, null, null, step.getValueXMLfield(), null,
					ValidateForm.getInstance().setRequired(true));
			this.valueXMLfield.setSingle(true);

			// Encoding
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, super.getEncoding());
			this.encoding.setSingle(true);

			// Omit XML header
			this.omitXMLHeader = new LabelInputMeta(id + ".omitXMLHeader",
					"省略XML标题", null, null, null, String.valueOf(step
							.isOmitXMLHeader()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.resultStream.putFieldsetsContent(new BaseFormMeta[] {
					this.valueXMLfield, this.encoding, this.omitXMLHeader });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.targetStream, this.sourceStream,
					this.joinConditionProperties, this.resultStream });

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
