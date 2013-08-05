package com.panet.imeta.trans.steps.joinrows;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
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

public class JoinRowsDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名
	 */
	private LabelInputMeta name;
	
	/**
	 * 临时目录
	 */
	private LabelInputMeta directory;
	
	/**
	 * 临时文件前缀
	 */
	private LabelInputMeta prefix;
	
	/**
	 * 最大缓存大小
	 */
	private LabelInputMeta cacheSize;
	
	/**
	 * Main step to read from
	 */
	private LabelSelectMeta mainStepname;
	
	/**
	 * 条件
	 */
	private LabelTextareaMeta conditions;
	
	public JoinRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			JoinRowsMeta step = (JoinRowsMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 临时目录
			this.directory = new LabelInputMeta(id + ".directory", "临时目录", null, null,
					"临时目录必须填写", String.valueOf(step.getDirectory()), null, ValidateForm
							.getInstance().setRequired(true));
			this.directory.setSingle(true);
			
			// 临时文件前缀
			this.prefix = new LabelInputMeta(id + ".prefix", "临时文件前缀", null, null,
					"临时文件前缀必须填写", String.valueOf(step.getPrefix()), null, ValidateForm
							.getInstance().setRequired(true));
			this.prefix.setSingle(true);

			// 最大缓存大小
			this.cacheSize = new LabelInputMeta ( id + ".cacheSize", "最大缓存大小（记录行）", null, null,
					"最大缓存大小必须填写", String.valueOf(step.getCacheSize()), null, ValidateForm.getInstance().setRequired(true));
			this.cacheSize.setSingle(true);
			
			// Main step to read from
			this.mainStepname = new LabelSelectMeta(id + ".mainStepname","主要步骤读取",
					null,null,null,String.valueOf(step.getMainStepname()),null,super.getPrevStepNames());
			this.mainStepname.setSingle(true);
			
			// 条件
			this.conditions = new LabelTextareaMeta ( id + ".cinditions", "条件", null, null, 
					null, null, 4, null);
            this.conditions.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.directory,this.prefix,this.cacheSize,
					this.mainStepname, this.conditions
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
