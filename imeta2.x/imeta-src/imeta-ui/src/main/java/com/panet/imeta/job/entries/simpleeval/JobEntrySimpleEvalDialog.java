package com.panet.imeta.job.entries.simpleeval;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;


public class JobEntrySimpleEvalDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;
	
	//General ------------------------------------------------------------------
	/**
	 * Source
	 */
	private ColumnFieldsetMeta sourceColumn;
	
	/**
	 * Evaluate
	 */
	private LabelSelectMeta valuetype;
	
	/**
	 * Field name
	 */
	private LabelInputMeta fieldname;
	
	/**
	 * 
	 */
	private LabelInputMeta variablename;
	
	/**
	 * Type
	 */
	private LabelSelectMeta fieldtype;
	
	/**
	 * Success On
	 */
	private ColumnFieldsetMeta successOnColumn;
	
	/**
	 * Success condition
	 */
	private LabelSelectMeta successcondition;
	
	/**
	 * Value
	 */
	private LabelInputMeta comparevalue;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntrySimpleEvalDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntrySimpleEval entry = (JobEntrySimpleEval) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用"});
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Source
			this.sourceColumn = new ColumnFieldsetMeta(null, "源");
			this.sourceColumn.setSingle(true);
			
			// Evaluate
			String[] options = JobEntrySimpleEval.valueTypeDesc;
			List<OptionDataMeta> valuetypeOptions = new ArrayList<OptionDataMeta>();
			for(int i = 0; i < options.length ; i++){
				valuetypeOptions.add(new OptionDataMeta(
						String.valueOf(i + 1), options[i]));
			}
			this.valuetype = new LabelSelectMeta(id + ".valuetype",
					"评估", null, null, null, String.valueOf(entry.valuetype),
					null, valuetypeOptions);
			this.valuetype.setSingle(true);
			
			// Field name
			this.fieldname = new LabelInputMeta(id + ".fieldname","字段名",null,
					null, "字段名", entry.getFieldName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.fieldname.setSingle(true);
			this.fieldname.setDisplay(true);
			
			//变量名
			this.variablename = new LabelInputMeta(id + "variablename","变量名",null,
					null, "变量名", entry.getVariableName(), null,
					ValidateForm.getInstance().setRequired(true));
			
			// Type
			String[] tOptions = JobEntrySimpleEval.fieldTypeDesc;
			List<OptionDataMeta> fieldtypeOptions = new ArrayList<OptionDataMeta>();
			for(int i = 0; i < tOptions.length ; i++){
				fieldtypeOptions.add(new OptionDataMeta(
						String.valueOf(i + 1), tOptions[i]));
			}
			this.fieldtype = new LabelSelectMeta(id + ".fieldtype",
					"类型", null, null, null, String.valueOf(entry.fieldtype), 
					null, fieldtypeOptions);
			this.fieldtype.setSingle(true);
			
			this.sourceColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.valuetype,this.fieldname,this.fieldtype
			});
			
			// Success On
			this.successOnColumn = new ColumnFieldsetMeta(null, "成功");
			this.successOnColumn.setSingle(true);
			
			// Success condition
			String[] sOptions = JobEntrySimpleEval.successConditionDesc;
			List<OptionDataMeta> successconditionOption = new ArrayList<OptionDataMeta>();
			for(int i = 0; i < sOptions.length ; i++){
				successconditionOption.add(new OptionDataMeta(
						String.valueOf(i + 1), sOptions[i]));
			}
			
//			String[] snOptions = JobEntrySimpleEval.successNumberConditionDesc;
//			List<OptionDataMeta> successnumberconditionOption = new ArrayList<OptionDataMeta>();
//			for(int i = 0; i < snOptions.length ; i++){
//				successconditionOption.add(new OptionDataMeta(
//						String.valueOf(i + 1), snOptions[i]));
//			}
			this.successcondition = new LabelSelectMeta(id + ".successcondition",
					"成功的条件", null, null, null, String.valueOf(entry.successcondition), 
					null, successconditionOption);
			this.successcondition.setSingle(true);
			
			// Value
			this.comparevalue = new LabelInputMeta(id + ".comparevalue","值",null, null, null, 
					entry.getCompareValue(), null, ValidateForm
					.getInstance().setRequired(true));
			this.comparevalue.setSingle(true);
			
			this.successOnColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.successcondition,this.comparevalue});
			
			// 装载到页签
			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.sourceColumn,this.successOnColumn
					});
			
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta});
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		}catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
