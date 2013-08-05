package com.panet.imeta.trans.steps.xslt;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class XsltDialog extends BaseStepDialog implements
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
	 * XML Field name
	 */
	private LabelSelectMeta fieldName;
	
	/**
	 * Result Fields
	 */
	private ColumnFieldsetMeta resultFields;
	
	/**
	 * Result Fieldname
	 */
	private LabelInputMeta resultFieldname;
	
	/**
	 * XSL Fields
	 */
	private ColumnFieldsetMeta xslFields;
	
	/**
	 * XSL filename defined in a
	 */
	private LabelInputMeta xslFileFieldUse;
	
	/**
	 * XSL Filename field
	 */
	private LabelSelectMeta xslFileField;
	
	/**
	 * XSL Filename
	 */
	private LabelInputMeta xslFilename;
	
//	private ButtonMeta browse;
	
	/**
	 * XSLT Factory
	 */
	private LabelSelectMeta xslFactory;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public XsltDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			XsltMeta step = (XsltMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签a
			this.meta = new MenuTabMeta(id, new String[] { "设置"});
			this.meta.setSingle(true);
			/*******************************************************************
			 * 标签0
			 ******************************************************************/
			
			//XML Field name
			this.fieldName = new LabelSelectMeta(id + ".fieldName","XML字段名",
					null,null,null,
					String
					.valueOf(step.getFieldname()),
			null, super.getPrevStepResultFields());	
			this.fieldName.setSingle(true);
			
			// Result Fields
			this.resultFields = new ColumnFieldsetMeta(null, "返回字段");
			this.resultFields.setSingle(true);
			
			// Result Fieldname
			this.resultFieldname = new LabelInputMeta(id + ".resultFieldname", "返回字段名", null, null,
					"字段名必须填写", 
					String.valueOf(step.getResultfieldname()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.resultFieldname.setSingle(true);
			
			this.resultFields.putFieldsetsContent(new BaseFormMeta[] {
					this.resultFieldname });
			
			// XSL Fields
			this.xslFields = new ColumnFieldsetMeta(null, "XSL字段");
			this.xslFields.setSingle(true);
			
			// XSL filename defined in a
			this.xslFileFieldUse = new LabelInputMeta(id + ".xslFileFieldUse", "XSL文件定义", null,
					null, null, 
					String
					.valueOf(step.useXSLFileFieldUse()),
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.xslFileFieldUse.setSingle(false);
			this.xslFileFieldUse.addClick("jQuery.imeta.steps.xslt.listeners.xslFileFieldUse");
			
			//XSL Fieldname field
			this.xslFileField = new LabelSelectMeta(id + ".xslFileField","XSL字段域",
					null,null,null,
					String
					.valueOf(step.getXSLFileField()),
			null, super.getPrevStepResultFields());	
			this.xslFileField.setSingle(true);
			this.xslFileField.setDisabled(!step.useXSLFileFieldUse());
			
			// XSL Fieldname
			this.xslFilename = new LabelInputMeta(id + ".xslFilename", "XSL字段名", null, null,
					"XSL Fieldname必须填写", step.getXslFilename(), null, ValidateForm
							.getInstance().setRequired(true));
			this.xslFilename.setSingle(true);
			this.xslFilename.setDisabled(step.useXSLFileFieldUse());
			
			List<OptionDataMeta> optionsXSLT = new ArrayList<OptionDataMeta>();
			optionsXSLT.add(new OptionDataMeta("xslFactory", "JAXP"));
			optionsXSLT.add(new OptionDataMeta("xslFactory", "SAXON"));
			this.xslFactory = new LabelSelectMeta(id + ".xslFactory", "XSLT Factory",
					null, null, null, step.getXSLFactory(), null, optionsXSLT);
			this.xslFactory.setSingle(true);
			
			this.xslFields.putFieldsetsContent(new BaseFormMeta[] {
					this.xslFileFieldUse,this.xslFileField,
					this.xslFilename,this.xslFactory });
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.fieldName, this.resultFields, this.xslFields 
					});

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta
					});

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
