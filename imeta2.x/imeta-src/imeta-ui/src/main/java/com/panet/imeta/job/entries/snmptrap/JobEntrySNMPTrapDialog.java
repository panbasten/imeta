package com.panet.imeta.job.entries.snmptrap;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntrySNMPTrapDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;

	//General
	/**
	 * Server
	 */
	private ColumnFieldsetMeta serverColumn;

	/**
	 * Server name/IP address
	 */
	private LabelInputMeta serverName;

	/**
	 * Server port
	 */
	private LabelInputMeta port;

	/**
	 * OID
	 */
	private LabelInputMeta oid;

	/**
	 * Test connection
	 */
//	private ButtonMeta testConnection;

	/**
	 * Advance
	 */
	private ColumnFieldsetMeta advanceColumn;

	/**
	 * Target type
	 */
	private LabelSelectMeta targettype;

	/**
	 * Community string
	 */
	private LabelInputMeta comString;

	/**
	 * User
	 */
	private LabelInputMeta user;

	/**
	 * Pass phrase
	 */
	private LabelInputMeta passphrase;

	/**
	 * Engine ID
	 */
	private LabelInputMeta engineid;

	/**
	 * Retry
	 */
	private LabelInputMeta nrretry;

	/**
	 * Timeout
	 */
	private LabelInputMeta timeout;

	/**
	 * Message
	 */
	private ColumnFieldsetMeta messageColumn;

	private LabelTextareaMeta message;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntrySNMPTrapDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntrySNMPTrap entry = (JobEntrySNMPTrap) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用"});
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Server
			this.serverColumn = new ColumnFieldsetMeta(null, "服务器");
			this.serverColumn.setSingle(true);

			// Server name/IP address
			this.serverName = new LabelInputMeta(id + ".serverName","服务器名称/ IP地址",
					null, null,null, entry.getServerName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.serverName.setSingle(true);

			// Server port
			this.port = new LabelInputMeta(id + ".port","服务设备端口",
					null, null,null, entry.getPort(), null, 
					ValidateForm.getInstance().setRequired(true));
			this.port.setSingle(true);

			// OID
			this.oid = new LabelInputMeta(id + ".oid","OID",null, null,null, 
					entry.getOID(), null, ValidateForm.getInstance().setRequired(true));
			this.oid.setSingle(true);

			// Test connection
//			DivMeta testConnectionbtn = new DivMeta(new NullSimpleFormDataMeta(), true);
//			this.testConnection = new ButtonMeta(id
//					+ ".btn.testConnection", id + ".btn.testConnection",
//					"测试连接", "测试连接");
//			this.testConnection.appendTo(testConnectionbtn);

			this.serverColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.serverName,this.port,this.oid
			});

			// Advance
			this.advanceColumn = new ColumnFieldsetMeta(null, "进展");
			this.advanceColumn.setSingle(true);

			// Target type
			String[] options = JobEntrySNMPTrap.target_type_Desc;
			List<OptionDataMeta> targettypeOptions = new ArrayList<OptionDataMeta>();
			for(int i = 0; i < options.length ; i++){
				targettypeOptions.add(new OptionDataMeta(
						JobEntrySNMPTrap.target_type_Code[i], options[i]));
			}
			this.targettype = new LabelSelectMeta(id + ".targettype",
					"目标类型", null, null, null, entry.getTargetType(),
					null, targettypeOptions);
			this.targettype.setSingle(true);
			this.targettype.addListener("change", "jQuery.imeta.jobEntries.jobentrySNMPtrap.listeners.targettypeChange");

			// Community string
			this.comString = new LabelInputMeta(id + ".comString",
					"组字符串",null, null,null, entry.getComString(), 
					null, ValidateForm.getInstance().setRequired(true));
			this.comString.setSingle(true);
			this.comString.setDisabled(entry.getTargetType().equals(entry.getTargetTypeCode("user")));

			// User
			this.user = new LabelInputMeta(id + ".user","用户",null, null,null, 
					entry.getUser(),null, ValidateForm.getInstance().setRequired(true));
			this.user.setSingle(true);
			this.user.setDisabled(!entry.getTargetType().equals(entry.getTargetTypeCode("user")));

			// Pass phrase
			this.passphrase = new LabelInputMeta(id + ".passphrase","通行短语",null, 
					null,null, entry.getPassPhrase(), InputDataMeta.INPUT_TYPE_PASSWORD, 
					ValidateForm.getInstance().setRequired(true));
			this.passphrase.setSingle(true);
			this.passphrase.setDisabled(!entry.getTargetType().equals(entry.getTargetTypeCode("user")));

			// Engine ID
			this.engineid = new LabelInputMeta(id + ".engineid","引擎编号",null, 
					null,null, entry.getEngineID(),null, ValidateForm
					.getInstance().setRequired(true));
			this.engineid.setSingle(true);
			this.engineid.setDisabled(!entry.getTargetType().equals(entry.getTargetTypeCode("user")));

			// Retry
			this.nrretry = new LabelInputMeta(id + ".nrretry","重试",null, 
					null,null, entry.getRetry(),null, ValidateForm
					.getInstance().setRequired(true));
			this.nrretry.setSingle(true);

			// Timeout
			this.timeout = new LabelInputMeta(id + ".timeout","超时",null, 
					null , null, entry.getTimeout(), null, ValidateForm.getInstance().setRequired(true));
			this.timeout.setSingle(true);

			this.advanceColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.targettype,this.comString,this.user,this.passphrase,
					this.engineid,this.nrretry,this.timeout
			});

			// Message
			this.messageColumn = new ColumnFieldsetMeta(null, "消息");
			this.messageColumn.setSingle(true);

			this.message = new LabelTextareaMeta(id + ".message", "消息", null, null,
					null, entry.getMessage(), 2, null);
			this.message.setSingle(true);

			this.messageColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.message
			});

			// 装载到页签
			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.serverColumn,this.advanceColumn,this.messageColumn
			});

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {this.name,
					this.meta});

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		}catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
