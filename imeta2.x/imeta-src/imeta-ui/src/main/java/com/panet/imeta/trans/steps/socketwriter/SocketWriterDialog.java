package com.panet.imeta.trans.steps.socketwriter;

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

public class SocketWriterDialog extends BaseStepDialog implements
StepDialogInterface{
	
	//套接字写者
	
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * Step name
	 */
	private LabelInputMeta name;
	
	/**
	 * Port
	 */
	private LabelInputMeta port;
	
	/**
	 * Buffer size
	 */
	private LabelInputMeta bufferSize;
	
	/**
	 * Flush interval
	 */
	private LabelInputMeta flushInterval;
	
	/**
	 * Compress data
	 */
	private LabelInputMeta compressed;
	
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public SocketWriterDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			SocketWriterMeta step=(SocketWriterMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.name.setSingle(true);
			
			// Port
			this.port = new LabelInputMeta ( id + ".port", "端口", null, null, 
					"端口必须填写",
					step.getPort(),
					null, ValidateForm.getInstance().setRequired(false));
			this.port.setSingle(true);
			
			// Buffer size
			this.bufferSize = new LabelInputMeta ( id + ".bufferSize", "缓冲区大小", null, null, 
					"缓冲区大小必须填写", 
					step.getBufferSize(),
					null, ValidateForm.getInstance().setRequired(false));
			this.bufferSize.setSingle(true);
			
			// Flush interval
			this.flushInterval = new LabelInputMeta ( id + ".flushInterval", "刷新间隔", null, null, 
					"刷新间隔必须填写", 
					step.getFlushInterval(),
					null, ValidateForm.getInstance().setRequired(false));
			this.flushInterval.setSingle(true);
			
			// Compress data
			this.compressed = new LabelInputMeta ( id + ".compressed", "压缩数据", null, null, 
					null, 
					String
					.valueOf(step.isCompressed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.compressed.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.port, this.bufferSize, this.flushInterval, this.compressed });
				
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
