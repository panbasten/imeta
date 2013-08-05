package com.panet.imeta.trans.steps.filestoresult;

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
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class FilesToResultDialog extends BaseStepDialog implements
		StepDialogInterface {
	public FilesToResultDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

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
	 * 文件名字段
	 */
	private LabelSelectMeta filenameField;

	/**
	 * 文件类型设
	 */
	private LabelSelectMeta fileType;

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			FilesToResultMeta step = (FilesToResultMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 文件名字段
			this.filenameField = new LabelSelectMeta(id + ".filenameField",
					"文件名字段", null, null, null, step.getFilenameField(), null,
					super.getPrevStepResultFields());
			this.filenameField.setSingle(true);

			// 文件类型
			List<OptionDataMeta> optionsDate = new ArrayList<OptionDataMeta>();
			optionsDate.add(new OptionDataMeta("0", "一般"));
			optionsDate.add(new OptionDataMeta("1", "日志"));
			optionsDate.add(new OptionDataMeta("2", "错误行"));
			optionsDate.add(new OptionDataMeta("3", "错误"));
			optionsDate.add(new OptionDataMeta("4", "警告"));
			this.fileType = new LabelSelectMeta(id + ".fileType", "文件类型:",
					null, null, null, String.valueOf(step.getFileType()), null, optionsDate);
//			this.fileType.setLayout(LabelSelectMeta.LAYOUT_COLUMN);
//			this.fileType.setSize(10);
			this.fileType.setSingle(true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filenameField, this.fileType });

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
