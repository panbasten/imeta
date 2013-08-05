package com.panet.imeta.trans.steps.socketreader;

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

public class SocketReaderDialog extends BaseStepDialog implements
StepDialogInterface{
	
	//套接字读者
	
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
	 * Hostname
	 */
	private LabelInputMeta hostname;
	
	/**
	 * Port
	 */
	private LabelInputMeta port;
	
	/**
	 * Buffer size
	 */
	private LabelInputMeta bufferSize;
	
	/**
	 * Compress
	 */
	private LabelInputMeta compressed;
	
	
	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public SocketReaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			SocketReaderMeta step=(SocketReaderMeta) super.getStep();
			

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// Step name 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.name.setSingle(true);

			// Hostname
			this.hostname = new LabelInputMeta ( id + ".hostname", "主机名称", null, null, 
					"主机名称必须填写", 
					step.getHostname(),
					null, ValidateForm.getInstance().setRequired(false));
			this.hostname.setSingle(true);
			
			// Port 端口
			this.port = new LabelInputMeta ( id + ".port", "端口", null, null, 
					"端口必须填写", 
					step.getPort(),
					null, ValidateForm.getInstance().setRequired(false));
			this.port.setSingle(true);
			
			// Buffer size 缓冲区大小
			this.bufferSize = new LabelInputMeta ( id + ".bufferSize", "缓冲区大小", null, null, 
					"缓冲区大小必须填写", 
					step.getBufferSize(),
					null, ValidateForm.getInstance().setRequired(false));
			this.bufferSize.setSingle(true);
			
			// Compress 压缩
			this.compressed = new LabelInputMeta ( id + ".compressed", "压缩", null, null, 
					null, 
					String.valueOf(step.isCompressed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.compressed.setSingle(true);
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.hostname, this.port, this.bufferSize, this.compressed });
				
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
