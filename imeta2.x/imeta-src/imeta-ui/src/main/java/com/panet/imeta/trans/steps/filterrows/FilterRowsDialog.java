package com.panet.imeta.trans.steps.filterrows;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class FilterRowsDialog extends BaseStepDialog implements
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
	 * 发送True数据给步骤
	 */
	private LabelSelectMeta sendTrueStepname;
	
	/**
	 * 发送FALSE数据给步骤
	 */
	private LabelSelectMeta sendFalseStepname;
	
	/**
	 * 条件
	 */
	private LabelTextareaMeta condition;
	
	public FilterRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			FilterRowsMeta step = (FilterRowsMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 发送True数据给步骤
			this.sendTrueStepname = new LabelSelectMeta(id + ".sendTrueStepname","发送True数据给步骤：",
					null,null,null, step.getSendTrueStepname(),null,super.getNextStepNames());
			this.sendTrueStepname.setSingle(true);
			this.sendTrueStepname.setHasEmpty(true);
	
			// 发送false数据给步骤
			this.sendFalseStepname = new LabelSelectMeta(id + ".sendFalseStepname","发送false数据给步骤：",
					null,null,null, step.getSendFalseStepname(),null,super.getNextStepNames());
			this.sendFalseStepname.setSingle(true);
			this.sendFalseStepname.setHasEmpty(true);
	
			// 条件
			this.condition = new LabelTextareaMeta(id + ".condition", "条件", null, null,
					null, String.valueOf(step.getCondition()), 8, ValidateForm.getInstance().setNumber(true));
			
			this.condition.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.sendTrueStepname,this.sendFalseStepname,
					this.condition
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
