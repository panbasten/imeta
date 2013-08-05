package com.panet.imeta.trans.steps.blockingstep;

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

public class BlockingStepDialog extends BaseStepDialog implements
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
	 * Pass all rows?
	 */
	
	private LabelInputMeta passAllRows;
	
	/**
	 * Spool directory
	 */
	private LabelInputMeta directory;
	
//	private ButtonMeta browse;

	/**
	 * Spool-file prefix
	 */
	private LabelInputMeta prefix;
	
	/**
	 * Cache size
	 */
	private LabelInputMeta cacheSize;
	
	/**
	 * Compress spool files
	 */
	private LabelInputMeta compressFiles;
	
	public BlockingStepDialog(StepMeta stepMeta, TransMeta transMeta) {
	super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			BlockingStepMeta step = (BlockingStepMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// Pass all rows
			this.passAllRows = new LabelInputMeta(id + ".passAllRows", "传递所有行?", null,
					null, null, String.valueOf(step.isPassAllRows()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.passAllRows.setSingle(true);
			this.passAllRows.addClick("jQuery.imeta.steps.blockingstep.listeners.passAllRows");
			
            // Spool directory
			String dir;
			if(step.isPassAllRows()==true){
				dir=String.valueOf(step.getDirectory());
			}else{
				dir="%%java.io.tmpdir%%";
			}
			this.directory = new LabelInputMeta(id + ".directory", "排序目录", null, null,
					"排序目录必须填写", dir, null, ValidateForm
							.getInstance().setRequired(true));

//			this.browse = new ButtonMeta(id + ".btn.browse", id
//					+ ".btn.browse", "浏览", "浏览");
//			
//			this.directory.addButton(new ButtonMeta[] { this.browse});
			
			this.directory.setSingle(true);
			this.directory.setDisabled(!step.isPassAllRows());
			
			// Spool-file prefix
			String spo;
			if(step.isPassAllRows()==true){
				spo=String.valueOf(step.getPrefix());
			}else{
				spo="block";
			}
			this.prefix = new LabelInputMeta(id + ".prefix", "后台打印文件的前缀", null, null,
					"后台打印文件的前缀必须填写", spo, null, ValidateForm
							.getInstance().setRequired(true));
			this.prefix.setSingle(true);
			this.prefix.setDisabled(!step.isPassAllRows());
			
			// Cache size
			String cac;
			if(step.isPassAllRows()==true){
				cac=String.valueOf(step.getCacheSize());
			}else{
				cac="5000";
			}
			this.cacheSize = new LabelInputMeta(id + ".cacheSize", "缓存范围", null, null,
					"缓存范围必须填写", cac, null, ValidateForm
							.getInstance().setRequired(true));
			this.cacheSize.setSingle(true);
			this.cacheSize.setDisabled(!step.isPassAllRows());
			
			// Compress spool files
			String com;
			if(step.isPassAllRows()==true){
				com=String.valueOf(step.getCompress());
			}else{
				com="true";
			}
			this.compressFiles = new LabelInputMeta(id + ".compressFiles", "压缩后台打印文件？", null,
					null, null, com, InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.compressFiles.setSingle(true);
			this.compressFiles.setDisabled(!step.isPassAllRows());
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.passAllRows,this.directory,this.prefix,this.cacheSize,this.compressFiles
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
