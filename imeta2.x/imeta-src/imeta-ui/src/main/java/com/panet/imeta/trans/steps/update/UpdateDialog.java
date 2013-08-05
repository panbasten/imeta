package com.panet.imeta.trans.steps.update;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class UpdateDialog extends BaseStepDialog implements StepDialogInterface {
	
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
	 * 数据库连接
	 */
	private LabelSelectMeta connection;


	/**
	 * 目的模式
	 */
	private LabelInputMeta schemaName;

	/**
	 * 目标表
	 */
	private LabelInputMeta tableName;

//	private ButtonMeta skim;

	/**
	 * commit size
	 */
	private LabelInputMeta commitSize;
	
	/**
	 * Skip lookup
	 */
    private LabelInputMeta skipLookup;
    
	/**
	 * 忽略查询失败？
	 */
	private LabelInputMeta errorIgnored;
	
	private LabelInputMeta ignoreFlagField;

	/**
	 * 用来查询值的关键字：
	 */
	private LabelGridMeta keywords;

	/**
	 * 更新字段
	 */
	private LabelGridMeta refreshwords;

	/**
	 * 获得更新字段
	 */
	private ButtonMeta getrefreshwords;


	public UpdateDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			UpdateMeta step = (UpdateMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));

			this.name.setSingle(true);

			// 得到连接数据库
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());


			this.connection.setSingle(true);

			// 得到目的模式

			this.schemaName = new LabelInputMeta(id + ".schemaName", "目的模式", null,
					null, "目的模式必须填写", step.getSchemaName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.schemaName.setSingle(true);

			// 得到目标表

			this.tableName = new LabelInputMeta(id + ".tableName","目标表",null,null,"目标表必须填写",
					step.getTableName(),null, ValidateForm.getInstance().setRequired(false));
			
			this.tableName.setSingle(true);

			// 得到Commit size

			this.commitSize = new LabelInputMeta(id + ".commitSize","提交的大小",null,null,"Commit size必须填写",
					String.valueOf(step.getCommitSize()),
					null, ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);
			
			// skipLookup
			this.skipLookup = new LabelInputMeta ( id + ".skipLookup", "跳过查找", null, null,
					null, String.valueOf(step.isSkipLookup()),
							InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
            this.skipLookup.setSingle(true);
            this.skipLookup.addClick("jQuery.imeta.steps.update.listeners.skipLookup");
            
			// 忽略查询失败？
			this.errorIgnored = new LabelInputMeta(
					id + ".errorIgnored",
					"忽略查询失败？",
					null,
					null,
					null,
					String.valueOf(step.isErrorIgnored()),
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.errorIgnored.setSingle(false);
			this.errorIgnored.setDisabled(step.isSkipLookup());
			this.errorIgnored.addClick("jQuery.imeta.steps.update.listeners.errorIgnored");

			this.ignoreFlagField = new LabelInputMeta( id + ".ignoreFlagField", "标志字段", null, null,
				null, step.getIgnoreFlagField(), null,
				ValidateForm.getInstance().setRequired(false));
			this.ignoreFlagField.setSingle(true);
			this.ignoreFlagField.setDisabled(!step.isErrorIgnored());
			
			// 得到用来查询的关键字
			this.keywords = new LabelGridMeta(id + "_keywords", null, 200);
			this.keywords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_keywords.fieldId", "#",GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),			
					new GridHeaderDataMeta(id + "_keywords.keyLookup", "表字段", null, false, 100),
					new GridHeaderDataMeta(id + "_keywords.keyCondition","比较符", null, false, 100),
					new GridHeaderDataMeta(id + "_keywords.keyStream", "流里的字段1", null, false, 100),
					new GridHeaderDataMeta(id + "_keywords.keyStream2", "流里的字段2", null, false, 100)
					});
			
			String[] fileName1 = step.getKeyLookup();
			if(fileName1 != null && fileName1.length > 0){
				for(int i = 0; i < fileName1.length; i++){
					this.keywords.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,step.getKeyLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,step.getKeyCondition()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,step.getKeyStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,step.getKeyStream2()[i],
									GridCellDataMeta.CELL_TYPE_INPUT)
						
					});	
				}
			}
			
			this.keywords.setSingle(true);
			this.keywords.setHasBottomBar(true);
			this.keywords.setHasAdd(true, true, 
					"jQuery.imeta.steps.update.btn.keywordsAdd");
			this.keywords.setHasDelete(true, true,"jQuery.imeta.parameter.fieldsDelete");
			
			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.update.btn.getfields").appendTo(div);
			 
			// 得到更新字段 

			this.refreshwords = new LabelGridMeta(id + "_refreshwords", null, 200);
			
			this.refreshwords.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_refreshwords.fieldId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_refreshwords.updateLookup", "表字段", null, false, 100),
					new GridHeaderDataMeta(id + "_refreshwords.updateStream", "流里的字段", null, false, 100)
					});
			
			String[] fileName = step.getUpdateLookup();
			if(fileName != null && fileName.length > 0){
				for(int i = 0; i < fileName.length; i++){
					this.refreshwords.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,step.getUpdateLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,step.getUpdateStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT)
						
					});	
				}
			}
			
			this.refreshwords.setSingle(true);
			this.refreshwords.setHasBottomBar(true);
			this.refreshwords.setHasAdd(true, true, "jQuery.imeta.steps.update.btn.refreshwordsAdd");
			this.refreshwords.setHasDelete(true, true,"jQuery.imeta.parameter.fieldsDelete");

			this.getrefreshwords = new ButtonMeta(id + ".btn.getrefreshwords", id+ ".btn.getrefreshwords", 
					"获取-更新字段", "获取-更新字段");
			this.getrefreshwords.addClick("jQuery.imeta.steps.update.btn.getRefreshFields");
			this.getrefreshwords.putProperty("roType",RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getrefreshwords.putProperty("roName", super.getTransMeta().getName());
			this.getrefreshwords.putProperty("elementName", super.getStepName());
			this.getrefreshwords.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			this.getrefreshwords.setButtonWidthStyle(20);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { 
					this.name,this.connection, 
					this.schemaName, this.tableName, 
					this.commitSize, this.skipLookup,
					this.errorIgnored, this.ignoreFlagField,
					this.keywords,div,this.refreshwords,this.getrefreshwords});

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
