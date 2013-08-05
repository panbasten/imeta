package com.panet.imeta.trans.steps.mapping;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MappingDialog extends BaseStepDialog implements
StepDialogInterface{
	
	//映射子转换
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
	 * Mapping transformation
	 */
	private ColumnFieldsetMeta mappingTransformation;
	
	/**
	 * Use a file for the mapping transformation
	 */
	private LabelInputMeta useAFileCh;
	private LabelInputMeta useAFile;

	
	/**
	 * Use a mapping transformation from the repository
	 */
	private LabelInputMeta useAMapping;
	private LabelInputMeta useAMappingA;
	private LabelInputMeta useAMappingB;

	
	
	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/**
	 * Parameters
	 */
	private LabelGridMeta parameters;
	
	/**
	 * Inherit all variables from the parent transformation
	 */
	private LabelInputMeta inherit;
	
	/**
	 * Button
	 */
	private ButtonMeta addInputBtn;
	private ButtonMeta addOutputBtn;
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public MappingDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					" 步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.name.setSingle(true);
			
			// Mapping transformation
			this.mappingTransformation = new ColumnFieldsetMeta ( null, "映射变换");
			this.mappingTransformation.setSingle(true);
			
			// Use a file for the mapping transformation
			this.useAFileCh = new LabelInputMeta ( id + ".useAFileCh", "使用文件的映射变换", null, null,
					null, null, InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.useAFileCh.setSingle(true);
            
            this.useAFile = new LabelInputMeta ( id + ".useAFile", null, null, null,
            		"使用文件的映射变换必须填写", null, null, ValidateForm.getInstance().setRequired(false));
            
//            this.browseBtn = new ButtonMeta ( id + ".btn.browseBtn", id + ".btn.browseBtn",
//            		"浏览(B)...", "浏览(B)...");
//            
//            this.useAFile.addButton( new ButtonMeta [] { this.browseBtn});
            this.useAFile.setSingle(true);
            
            // Use a mapping transformation from the repository
            this.useAMapping = new LabelInputMeta ( id + ".useAMapping", "使用库的映射变换", null, null,
            		null, null, InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.useAMapping.setSingle(true);
            
            this.useAMappingA = new LabelInputMeta ( id + ".useAMappingA", null, null, null,
            		"使用库的映射变换必须填写", null, null, ValidateForm.getInstance().setRequired(false));
            this.useAMappingA.setSingle(false);
            
            this.useAMappingB = new LabelInputMeta ( id + ".useAMappingB", null, null, null,
            		null, null, null, ValidateForm.getInstance().setRequired(false));
            
//            this.chooseBtn = new ButtonMeta ( id + ".btn.chooseBtn", id + ".btn.chooseBtn", 
//            		"选择", "选择");
//            this.useAMappingB.addButton( new ButtonMeta [] { this.chooseBtn});
            this.useAMappingB.setSingle(false);
            
//            // 编辑 
//            this.editBtn = new ButtonMeta ( id + ".btn.editBtn", id + ".btn.editBtn",
//            		"编辑", "编辑");
//            this.editBtn.setSingle(true);
            
            this.mappingTransformation.putFieldsetsContent( new BaseFormMeta [] { this.useAFileCh, this.useAFile,
            		this.useAMapping, this.useAMappingA, this.useAMappingB});
            
            // 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "参数"});
			this.meta.setSingle(true);
			
			// Parameters
			this.parameters = new LabelGridMeta(id + ".parameters", null, 120);
			this.parameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(null, "#", null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "变量名称", null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldType", "字符串值（可以包含变量表达式）", null, false, 50),
					new GridHeaderDataMeta(null, " ", null, false, 390) });
			this.parameters.addRow(new String[] { "1", "", "", "" });
			this.parameters.setSingle(true);
		
			// Inherit all variables form the parent transformation
			this.inherit = new LabelInputMeta ( id + ".inherit", "从父转换继承所有变量", null, null,
					null, "true", InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.inherit.setSingle(true);
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.parameters, this.inherit});
			// Button
			this.addInputBtn = new ButtonMeta ( id + ".btn.addInputBtn", id + ".btn.addInputBtn",
					"增加输入", "增加输入");
			this.addOutputBtn = new ButtonMeta ( id + ".btn.addOutputBtn", id + ".btn.addOutputBtn",
					"增加输出", "增加输出");
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name, this.mappingTransformation,
					this.meta, this.addInputBtn, this.addOutputBtn});
				
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn()});
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
