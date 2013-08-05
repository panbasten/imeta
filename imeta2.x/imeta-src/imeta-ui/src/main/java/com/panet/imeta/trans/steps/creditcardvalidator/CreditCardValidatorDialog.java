package com.panet.imeta.trans.steps.creditcardvalidator;

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
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.mailvalidator.MailValidatorMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class CreditCardValidatorDialog extends BaseStepDialog implements
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
	 * Credit Card field
	 */
	private LabelSelectMeta fieldname;
	
	/**
	 * Get only digits?
	 */
	private LabelInputMeta onlydigits;

	/**
	 * Output Fields
	 */
	private ColumnFieldsetMeta outputFields;
	
	/**
	 * Result fieldname
	 */
	private LabelInputMeta resultfieldname;
	
	/**
	 * Credit card type field
	 */
	private LabelInputMeta cardtype;
	
	/**
	 * Not valid Message
	 */
	private LabelInputMeta notvalidmsg;

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
	public CreditCardValidatorDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			CreditCardValidatorMeta step = (CreditCardValidatorMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
            
			// Credit Card field
			this.fieldname = new LabelSelectMeta(id + ".fieldname", "信用卡字段",
					null, null, null,
					String
					.valueOf(step.getDynamicField()),
			null, super.getPrevStepResultFields());
			this.fieldname.setSingle(true);
			this.fieldname.setHasEmpty(true);
			
			// Get only digits
			this.onlydigits = new LabelInputMeta ( id + ".onlydigits", "只得到数字", null, null, 
					null, 
					String
					.valueOf(step.isOnlyDigits()),
			        InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.onlydigits.setSingle(false);
			
			// Output fields
			this.outputFields = new ColumnFieldsetMeta(null, "输出字段");
			this.outputFields.setSingle(true);
			
			// Result fieldname 
			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname", "结果字段", null, null,
					"结果字段必须填写", 
					step.getResultFieldName(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.resultfieldname.setSingle(true);
			
			// Credit card type field
			this.cardtype = new LabelInputMeta(id + ".cardtype", "信用卡类型字段", null, null,
					"信用卡类型字段必须填写",
					step.getCardType(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.cardtype.setSingle(true);
			
			// Not valid Message
			this.notvalidmsg = new LabelInputMeta(id + ".notvalidmsg", "无效的信息", null, null,
					"无效的信息必须填写", 
					step.getNotValidMsg(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.notvalidmsg.setSingle(true);
			
			this.outputFields.putFieldsetsContent(new BaseFormMeta[] {
					this.resultfieldname, 
                    this.cardtype,
                    this.notvalidmsg
			        });
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
                    this.fieldname, this.onlydigits, this.outputFields
					});
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
