package com.panet.imeta.trans.steps.xsdvalidator;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class XsdValidatorDialog extends BaseStepDialog implements
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
	 * XML source
	 */
	private ColumnFieldsetMeta xmlSource;

	/**
	 * XML source is a file
	 */
	private LabelInputMeta xmlSourceFile;

	/**
	 * XML field
	 */
	private LabelSelectMeta xmlStream;

	/**
	 * Output Fields
	 */
	private ColumnFieldsetMeta outputFields;

	/**
	 * Result Fieldname
	 */
	private LabelInputMeta resultFieldName;

	/**
	 * Output String Field
	 */
	private LabelInputMeta outputStringField;

	/**
	 * Display if is XML valid
	 */
	private LabelInputMeta ifXmlValid;

	/**
	 * Display if is XML unvalid
	 */
	private LabelInputMeta ifXmlInvalid;

	/**
	 * Add validation msg in output
	 */
	private LabelInputMeta addValidationMessage;

	/**
	 * Validation msg field
	 */
	private LabelInputMeta validationMessageField;

	/**
	 * XML Schema Definition
	 */
	private ColumnFieldsetMeta xmlSchemaDefinition;

	/**
	 * XSD Source
	 */
	private LabelSelectMeta xsdSource;

	/**
	 * XSD Filename
	 */
	private LabelInputMeta xsdFileName;

	private ButtonMeta browse;

	/**
	 * XSD filename field
	 */
	private LabelSelectMeta xsdDefinedField;

	/**
	 * 确定按钮
	 */
	private ButtonMeta okBtn;

	/**
	 * 取消按钮
	 */
	private ButtonMeta cancelBtn;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public XsdValidatorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			XsdValidatorMeta step = (XsdValidatorMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "设置" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * 标签0
			 ******************************************************************/

			// XML source
			this.xmlSource = new ColumnFieldsetMeta(null, "XML源");
			this.xmlSource.setSingle(true);

			// XML source is a file
			this.xmlSourceFile = new LabelInputMeta(id + ".xmlSourceFile",
					"XML源是一个文件", null, null, null, String.valueOf(step
							.getXMLSourceFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.xmlSourceFile.setSingle(true);
			//得到之前步骤的resultName
			List<OptionDataMeta> xsdDefinedFieldOptions = new ArrayList<OptionDataMeta>();
			try {
				RowMetaInterface r = super.getTransMeta().getPrevStepFields(
						super.getStepName());
				if (r != null) {
					String[] resultNames = r.getFieldNames();
					for (int i = 0; i < resultNames.length; i++) {
						xsdDefinedFieldOptions.add(new OptionDataMeta(String.valueOf(i), resultNames[i]));
					}
				}
			} catch (KettleException ke) {
				throw new KettleException(ke.getMessage(),ke);
			}
			// XML field
			this.xmlStream = new LabelSelectMeta(id + ".xmlStream", "XML字段",
					null, null, null, step.getXMLStream(), null, xsdDefinedFieldOptions);
			this.xmlStream.setSingle(true);
			this.xmlStream.setHasEmpty(true);

			this.xmlSource.putFieldsetsContent(new BaseFormMeta[] {
					this.xmlSourceFile, this.xmlStream });

			// Output Fields
			this.outputFields = new ColumnFieldsetMeta(null, "输出字段");
			this.outputFields.setSingle(true);

			// Result Fieldname
			this.resultFieldName = new LabelInputMeta(id + ".resultFieldName",
					"结果字段", null, null, "结果字段必须填写", step.getResultfieldname(),
					null, ValidateForm.getInstance().setRequired(false));
			this.resultFieldName.setSingle(true);

			// Output String Field
			boolean isOupStringField = step.getOutputStringField();
			this.outputStringField = new LabelInputMeta(id
					+ ".outputStringField", "输出字符串字段", null, null, null, String
					.valueOf(step.getOutputStringField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.outputStringField
					.addClick("jQuery.imeta.steps.xsd.listeners.outStringFieldClick");
			this.outputStringField.setSingle(true);

			// Display if is XML valid
			this.ifXmlValid = new LabelInputMeta(id + ".ifXmlValid",
					"如果XML有效则显示", null, null, "如果XML有效则显示必须填写", step
							.getIfXmlValid(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.ifXmlValid.setDisabled(!isOupStringField);
			this.ifXmlValid.setSingle(true);

			// Display if is XML unvalid
			this.ifXmlInvalid = new LabelInputMeta(id + ".ifXmlInvalid",
					"如果XML无效则显示", null, null, "如果XML无效则显示必须填写", step
							.getIfXmlInvalid(), null, ValidateForm
							.getInstance().setRequired(false));
			this.ifXmlInvalid.setDisabled(!isOupStringField);
			this.ifXmlInvalid.setSingle(true);

			// Add validation msg in output
			boolean isAddValidationMSG = step.useAddValidationMessage();
			this.addValidationMessage = new LabelInputMeta(id
					+ ".addValidationMessage", "增加MSG验证", null, null, null,
					String.valueOf(step.useAddValidationMessage()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addValidationMessage
					.addClick("jQuery.imeta.steps.xsd.listeners.addMsgValidationClick");
			this.addValidationMessage.setSingle(true);

			// Validation msg field
			this.validationMessageField = new LabelInputMeta(id
					+ ".validationMessageField", "验证MSG字段", null, null,
					null, step.getValidationMessageField(), null,
					ValidateForm.getInstance().setRequired(false));
			validationMessageField.setDisabled(!isAddValidationMSG);
			this.validationMessageField.setSingle(true);

			this.outputFields.putFieldsetsContent(new BaseFormMeta[] {
					this.resultFieldName, this.outputStringField,
					this.ifXmlValid, this.ifXmlInvalid,
					this.addValidationMessage, this.validationMessageField, });

			// XML Schema Definition
			this.xmlSchemaDefinition = new ColumnFieldsetMeta(null, "XML结构定义");
			this.xmlSchemaDefinition.setSingle(true);

			// XSD Source
			List<OptionDataMeta> xsdSourceOptions = new ArrayList<OptionDataMeta>();
			xsdSourceOptions.add(new OptionDataMeta(step.SPECIFY_FILENAME, Messages
					.getString("XsdValidatorDialog.XSDSource.IS_A_FILE")));
			xsdSourceOptions.add(new OptionDataMeta(step.SPECIFY_FIELDNAME, Messages
					.getString("XsdValidatorDialog.XSDSource.IS_A_FIELD")));
			xsdSourceOptions.add(new OptionDataMeta(step.NO_NEED, Messages
					.getString("XsdValidatorDialog.XSDSource.NO_NEED")));
			this.xsdSource = new LabelSelectMeta(id + ".xsdSource", "XSD源",
					null, null, null, step.getXSDSource(), null,
					xsdSourceOptions);
			this.xsdSource.addListener("change",
					"jQuery.imeta.steps.xsd.listeners.xsdSourceChange");
			this.xsdSource.setSingle(true);

			// XSD Filename
			this.xsdFileName = new LabelInputMeta(id + ".xsdFileName",
					"XSD文件名", null, null, "XSD文件名必须填写", step.getXSDFilename(),
					null, ValidateForm.getInstance().setRequired(false));
			// this.browse = new ButtonMeta(id + ".btn.browse",
			// id + ".btn.browse", "浏览", "浏览");

			// this.xsdFileName.addButton(new ButtonMeta[] { this.browse });
			this.xsdFileName.setDisabled(!step.getXSDSource().equals(step.SPECIFY_FILENAME));
			this.xsdFileName.setSingle(true);

			// XSD filename field
			
			this.xsdDefinedField = new LabelSelectMeta(id + ".xsdDefinedField",
					"XSD文件名字段", null, null, null, step.getXSDDefinedField(),
					null, xsdDefinedFieldOptions);
			this.xsdDefinedField.setDisabled(!step.getXSDSource().equals("1"));
			this.xsdDefinedField.setSingle(true);
			this.xsdDefinedField.setHasEmpty(true);

			this.xmlSchemaDefinition.putFieldsetsContent(new BaseFormMeta[] {
					this.xsdSource, this.xsdFileName, this.xsdDefinedField });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.xmlSource,
					this.outputFields, this.xmlSchemaDefinition });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
