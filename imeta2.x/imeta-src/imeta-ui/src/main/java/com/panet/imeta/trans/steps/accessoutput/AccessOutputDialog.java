package com.panet.imeta.trans.steps.accessoutput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AccessOutputDialog extends BaseStepDialog implements
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
	 * dbName
	 */
	private LabelInputMeta filename;

	/**
	 * create file at start
	 */
	private LabelInputMeta tableTruncated;

	/**
	 * create DB
	 */
	private LabelInputMeta fileCreated;

	/**
	 * target Table
	 */
	private LabelInputMeta tablename;

	/**
	 * create Table
	 */
	private LabelInputMeta tableCreated;

	/**
	 * commit Size
	 */
	private LabelInputMeta commitSize;

	/**
	 * 添加文件名
	 */
	private LabelInputMeta addToResultFilenames;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public AccessOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			AccessOutputMeta step = (AccessOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 数据库文件名字
			this.filename = new LabelInputMeta(id + ".filename", "数据库名字", null,
					null, null,step.getFilename(), null,
					ValidateForm.getInstance().setRequired(false));

			this.filename.setSingle(true);

			// Do not create file at
			this.tableTruncated = new LabelInputMeta(id + ".tableTruncated",
					"不建立档案", null, null, null, String.valueOf(step
							.isDoNotOpenNewFileInit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// create DB
			this.fileCreated = new LabelInputMeta(id + ".fileCreated", "创建数据库", null,
					null, null, String.valueOf(step.isFileCreated()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// Target Table
			this.tablename = new LabelInputMeta(id + ".tablename", "目标表",
					null, null, null, step.getTablename(), null, ValidateForm
							.getInstance().setRequired(false));

			this.tablename.setSingle(true);

			// create Table
			this.tableCreated = new LabelInputMeta(id + ".tableCreated", "创建表",
					null, null, null, String.valueOf(step.isTableCreated()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.tableCreated.setSingle(true);

			// Commit Size
			this.commitSize = new LabelInputMeta(id + ".commitSize", "提交大小",
					null, null, "Commit Size", 
					String.valueOf(step.getCommitSize()), 
					null, ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);

			// Add File Name to result
			this.addToResultFilenames = new LabelInputMeta(id + ".addToResultFilenames",
					"添加文件名结果", null, null, null, String.valueOf(step
							.isAddToResultFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addToResultFilenames.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filename, this.tableTruncated, this.fileCreated,
					this.tablename, this.tableCreated, this.commitSize,
					this.addToResultFilenames });

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
