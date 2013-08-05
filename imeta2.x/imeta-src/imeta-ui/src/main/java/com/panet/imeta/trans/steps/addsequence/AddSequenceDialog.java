package com.panet.imeta.trans.steps.addsequence;

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

public class AddSequenceDialog extends BaseStepDialog implements
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
	 * 值的名称
	 */
	private LabelInputMeta valuename;

	/**
	 * 使用数据库来生成序列
	 */
	private ColumnFieldsetMeta usedatabase;
	
	/**
	 * 使用DB来获取sequence
	 */
	private LabelInputMeta useDatabase;
	
	/**
	 * 数据库连接
	 */
	private LabelSelectMeta connectionId;
    
//	private ButtonMeta editconncet;
//	
//	private ButtonMeta newconncet;
	
	/**
	 * 模式名称
	 */
	private LabelInputMeta schemaName;
	
	/**
	 * sequence名称
	 */
	private LabelInputMeta sequenceName;
	
	/**
	 * 使用转换计数器来生成序列
	 */
	private ColumnFieldsetMeta conversionCounter;

	/**
	 * 使用计数器来计算sequence
	 */
	private LabelInputMeta useCounter;
	
	/**
	 * 计算器名称
	 */
	private LabelInputMeta counterName;
	
	/**
	 * 起始值
	 */
	private LabelInputMeta startAt;
	
	/**
	 * 增长依据
	 */
	private LabelInputMeta incrementBy;
	
	/**
	 * 最大值
	 */
	private LabelInputMeta maxValue;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public AddSequenceDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
            AddSequenceMeta step = (AddSequenceMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
            //值的名称
			this.valuename = new LabelInputMeta(id + ".valuename", "值的名称", null, null,
					"值的名称必须填写", 
					String.valueOf(step.getValuename()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.valuename.setSingle(true);
			
			// 使用数据库来生成序列
			this.usedatabase = new ColumnFieldsetMeta(null, "使用数据库来生成序列");
			this.usedatabase.setSingle(true);
			
			// 使用DB来获取sequence
			this.useDatabase = new LabelInputMeta(id + ".useDatabase", "使用DB来获取sequence?",
					null,
					null,
					null,
					String
							.valueOf(step.isDatabaseUsed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.useDatabase.setSingle(true);
			this.useDatabase.addClick("jQuery.imeta.steps.addsequence.listeners.useDatabase");
			
			//数据库连接
			this.connectionId = new LabelSelectMeta(id + ".connectionId", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabase() != null) ? step
									.getDatabase().getID() : ""), null,
					super.getConnectionLine());
			this.connectionId.setSingle(true);
			this.connectionId.setDisabled(!step.isDatabaseUsed());
//			this.editconncet = new ButtonMeta(id + ".btn.editconncet", id
//					+ ".btn.editconncet", "编辑", "编辑");
//			this.newconncet = new ButtonMeta(id + ".btn.newconncet", id
//					+ ".btn.newconncet", "新建", "新建");
//			
//			this.connectionId.addButton(new ButtonMeta[] { this.editconncet,this.newconncet});
			
			//模式名称
			this.schemaName = new LabelInputMeta(id + ".schemaName", "模式名称", null, null,
					"模式名称必须填写", 
					String.valueOf(step.getSchemaName()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.schemaName.setSingle(true);
			this.schemaName.setDisabled(!step.isDatabaseUsed());
			
			//Sequence名称
			this.sequenceName = new LabelInputMeta(id + ".sequenceName", "Sequence名称", null, null,
					"Sequence名称必须填写",
					String.valueOf(step.getSequenceName()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.sequenceName.setSingle(true);
			this.sequenceName.setDisabled(!step.isDatabaseUsed());
	
			this.usedatabase.putFieldsetsContent(new BaseFormMeta[] {
					this.useDatabase,this.connectionId,this.schemaName,this.sequenceName });
			
			// 使用转换计数器来生成序列
			this.conversionCounter = new ColumnFieldsetMeta(null, "使用转换数据库来生成序列");
			this.conversionCounter.setSingle(true);
			// 使用转换计数器来获取sequence
			this.useCounter = new LabelInputMeta(id + ".useCounter", "使用转换计数器来获取sequence?", null,
					null, null, 
					String
					.valueOf(step.isCounterUsed()),
			InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
			this.useCounter.setSingle(true);
			this.useCounter.addClick("jQuery.imeta.steps.addsequence.listeners.useCounter");
			
			//计数器名称
			this.counterName = new LabelInputMeta(id + ".counterName", "计数器名称(可选)", null, null,
					"Sequence名称必须填写", 
					step.getCounterName(),
					null, ValidateForm
							.getInstance().setRequired(false));
			this.counterName.setSingle(true);
			this.counterName.setDisabled(!step.isCounterUsed());
			
			//起始值
			this.startAt = new LabelInputMeta(id + ".startAt", "起始值", null, null,
					"起始值必须填写", 
					String.valueOf(step.getStartAt()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.startAt.setSingle(true);
			this.startAt.setDisabled(!step.isCounterUsed());
			
			//增长依据
			this.incrementBy = new LabelInputMeta(id + ".incrementBy", "增长依据", null, null,
					"增长依据必须填写", 
					String.valueOf(step.getIncrementBy()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.incrementBy.setSingle(true);
			this.incrementBy.setDisabled(!step.isCounterUsed());
			
 		    //最大值
			this.maxValue = new LabelInputMeta(id + ".maxValue", "最大值", null, null,
					"最大值必须填写", 
					String.valueOf(step.getMaxValue()),
					null, ValidateForm
							.getInstance().setRequired(true));
			this.maxValue.setSingle(true);
			this.maxValue.setDisabled(!step.isCounterUsed());

			this.conversionCounter.putFieldsetsContent(new BaseFormMeta[] {
					this.useCounter,
					this.counterName,this.startAt,this.incrementBy,this.maxValue});
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.valuename,this.usedatabase,
					this.conversionCounter
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
