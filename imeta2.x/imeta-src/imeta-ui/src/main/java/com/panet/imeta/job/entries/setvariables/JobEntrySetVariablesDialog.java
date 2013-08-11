package com.panet.imeta.job.entries.setvariables;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.trans.steps.setvariable.SetVariableMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntrySetVariablesDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;
	
	/**
	 * Setting
	 */
	private ColumnFieldsetMeta settingColumn;
	
	/**
	 * Variable
	 */
	private LabelInputMeta replaceVars;
	
	/**
	 * Variables:
	 */
	private LabelGridMeta variablesGrid;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntrySetVariablesDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntrySetVariables entry = (JobEntrySetVariables) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Setting
			this.settingColumn = new ColumnFieldsetMeta(null, "设置");
			this.settingColumn.setSingle(true);
			
			// Variable
			this.replaceVars = new LabelInputMeta(id + ".replaceVars","是否可变",null, null,
					null, String.valueOf(entry.isReplaceVars()), InputDataMeta
					.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(true));
			
			this.settingColumn.putFieldsetsContent(new BaseFormMeta[] {this.replaceVars});
			
			// Variables:
			this.variablesGrid = new LabelGridMeta(id + "_variablesGrid","变量：",
					150);
			this.variablesGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_variablesGrid.fieldId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_variablesGrid.name", "变量名", null, false, 150),
					new GridHeaderDataMeta(id + "_variablesGrid.value", "变量值", null, false, 100),
					(new GridHeaderDataMeta(id + "_variablesGrid.type",
							"变量范围类型", null, false, 200))
							.setOptions(super
									.getOptionsByStringArrayWithNumberValue(
											SetVariableMeta.fieldTypeDesc,
											false))
					}) ;
			this.variablesGrid.setSingle(true);
			this.variablesGrid.setHasBottomBar(true);
			this.variablesGrid.setHasAdd(true, true, 
					"jQuery.imeta.jobEntries.jobentrysetvariables.btn.fieldAdd");
			this.variablesGrid.setHasDelete(true, true, 
					"jQuery.imeta.parameter.fieldsDelete");
			
			String[] variableName = entry.variableName;
			if(variableName != null && variableName.length > 0){
				for(int i = 0; i < variableName.length; i++){
					this.variablesGrid.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,entry.variableName[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.getVariableValue()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,String.valueOf(entry.getVariableType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
					});	
				}
			}
			
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.settingColumn ,this.variablesGrid});
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
