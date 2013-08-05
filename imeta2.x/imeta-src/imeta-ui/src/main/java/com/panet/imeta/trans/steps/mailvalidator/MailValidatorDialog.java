package com.panet.imeta.trans.steps.mailvalidator;

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
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.xsdvalidator.XsdValidatorMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MailValidatorDialog extends BaseStepDialog implements
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
	 * email fieldname
	 */
	private LabelSelectMeta emailfield;

	/**
	 * Settings
	 */
	private ColumnFieldsetMeta settings;

	/**
	 * SMTP check?
	 */
	private LabelInputMeta smtpCheck;

	/**
	 * Time out( milliseconds)
	 */
	private LabelInputMeta timeout;

	/**
	 * Email sender
	 */
	private LabelInputMeta emailSender;

	/**
	 * Default SMTP server
	 */
	private LabelInputMeta defaultSMTP;

	/**
	 * dynamic default
	 */
	private LabelInputMeta isdynamicDefaultSMTP;

	/**
	 * Default SMTP field
	 */
	private LabelSelectMeta defaultSMTPField;

	/**
	 * Result
	 */
	private ColumnFieldsetMeta result;

	/**
	 * Result fieldname
	 */
	private LabelInputMeta resultfieldname;

	/**
	 * Result is a string
	 */
	private LabelInputMeta resultAsString;

	/**
	 * Email is valid
	 */
	private LabelInputMeta emailValideMsg;

	/**
	 * Email is not valid
	 */
	private LabelInputMeta emailNotValideMsg;

	/**
	 * Errors field
	 */
	private LabelInputMeta errorsFieldName;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public MailValidatorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			MailValidatorMeta step = (MailValidatorMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// email fieldname
			List<OptionDataMeta> emailfieldOptions = new ArrayList<OptionDataMeta>();
			try {
				RowMetaInterface r = super.getTransMeta().getPrevStepFields(
						super.getStepName());
				if (r != null) {
					String[] resultNames = r.getFieldNames();
					for (int i = 0; i < resultNames.length; i++) {
						emailfieldOptions.add(new OptionDataMeta(String.valueOf(i), resultNames[i]));
					}
				}
			} catch (KettleException ke) {
				throw new KettleException(ke.getMessage(),ke);
			}
			this.emailfield = new LabelSelectMeta(id + ".emailfield", "电子邮件字段",
					null, null, null, step.getEmailField(), null, emailfieldOptions);
			this.emailfield.setSingle(true);
			this.emailfield.setHasEmpty(true);

			// settings
			this.settings = new ColumnFieldsetMeta(null, "设置");
			this.settings.setSingle(true);

			// SMTP check
			boolean isSMTPCheck = step.isSMTPCheck();
			this.smtpCheck = new LabelInputMeta(id + ".smtpCheck", "SMTP检查?",
					null, null, null, String.valueOf(isSMTPCheck),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.smtpCheck
					.addClick("jQuery.imeta.steps.mailValidate.listeners.smtpCheckClick");
			this.smtpCheck.setSingle(false);

			// Time out
			this.timeout = new LabelInputMeta(id + ".timeout", "超时（毫秒）", null,
					null, "超时（毫秒）必须填写", step.getTimeOut(), null, ValidateForm
							.getInstance().setRequired(false));
			this.timeout.setDisabled(!isSMTPCheck);
			this.timeout.setSingle(true);

			// Email sender
			this.emailSender = new LabelInputMeta(id + ".emailSender",
					"电子邮件发件人", null, null, "电子邮件发件人必须填写",
					step.geteMailSender(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.emailSender.setDisabled(!isSMTPCheck);
			this.emailSender.setSingle(true);

			// Default SMTP server
			this.defaultSMTP = new LabelInputMeta(id + ".defaultSMTP",
					"默认的SMTP服务器", null, null, "默认的SMTP服务器必须填写", step
							.getDefaultSMTP(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.defaultSMTP.setDisabled(!isSMTPCheck);
			this.defaultSMTP.setSingle(true);

			// dynamic default
			boolean isDynamicSMTP = step.isdynamicDefaultSMTP();
			this.isdynamicDefaultSMTP = new LabelInputMeta(id
					+ ".isdynamicDefaultSMTP", "动态默认", null, null, null, String
					.valueOf(isDynamicSMTP), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.isdynamicDefaultSMTP
					.addClick("jQuery.imeta.steps.mailValidate.listeners.isDynamicSMTPClick");
			this.isdynamicDefaultSMTP.setDisabled(!isSMTPCheck);
			this.isdynamicDefaultSMTP.setSingle(false);

			// Default SMTP field
			this.defaultSMTPField = new LabelSelectMeta(id
					+ ".defaultSMTPField", "默认SMTP字段", null, null, null, step
					.getDefaultSMTPField(), null, super.getPrevStepResultFields());
			this.defaultSMTPField.setDisabled(!isDynamicSMTP);
			this.defaultSMTPField.setSingle(true);
			this.defaultSMTPField.setHasEmpty(true);

			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.smtpCheck, this.timeout, this.emailSender,
					this.defaultSMTP, this.isdynamicDefaultSMTP,
					this.defaultSMTPField });

			// Result
			this.result = new ColumnFieldsetMeta(null, "结果");
			this.result.setSingle(true);

			// Result fieldname
			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname",
					"结果字段", null, null, "结果字段必须填写", step.getResultFieldName(),
					null, ValidateForm.getInstance().setRequired(false));
			this.resultfieldname.setSingle(true);

			// Result is a string
			boolean isResultAsString = step.isResultAsString();
			this.resultAsString = new LabelInputMeta(id + ".resultAsString",
					"结果是字符", null, null, null,
					String.valueOf(isResultAsString),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.resultAsString
					.addClick("jQuery.imeta.steps.mailValidate.listeners.isResultAsStringClick");
			this.resultAsString.setSingle(false);

			// Email is valid
			this.emailValideMsg = new LabelInputMeta(id + ".emailValideMsg",
					"有效的电子邮件", null, null, "电子邮件必须填写",
					step.getEMailValideMsg(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.emailValideMsg.setDisabled(!isResultAsString);
			this.emailValideMsg.setSingle(true);

			// Email is not valid
			this.emailNotValideMsg = new LabelInputMeta(id
					+ ".emailNotValideMsg", "无效的电子邮件", null, null, "电子邮件必须填写",
					step.getEMailNotValideMsg(), null, ValidateForm
							.getInstance().setRequired(false));
			this.emailNotValideMsg.setDisabled(!isResultAsString);
			this.emailNotValideMsg.setSingle(true);

			// Errors field
			this.errorsFieldName = new LabelInputMeta(id + ".errorsFieldName",
					"错误字段", null, null, "错误字段必须填写", step.getErrorsField(),
					null, ValidateForm.getInstance().setRequired(false));
			this.errorsFieldName.setSingle(true);

			this.result.putFieldsetsContent(new BaseFormMeta[] {
					this.resultfieldname, this.resultAsString,
					this.emailValideMsg, this.emailNotValideMsg,
					this.errorsFieldName });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.emailfield, this.settings, this.result });

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
