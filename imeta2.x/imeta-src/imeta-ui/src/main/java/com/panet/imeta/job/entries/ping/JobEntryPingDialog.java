package com.panet.imeta.job.entries.ping;

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
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryPingDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	private final static String CLASSICPING = Messages.getString("JobPing.ClassicPing.Label");

	private final static String SYSTEMPING = Messages.getString("JobPing.SystemPing.Label");

	private final static String SYSTEMANDCLASSIC = Messages.getString("JobPing.BothPings.Label");

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * Jop entry name
	 */
	private LabelInputMeta name;

	/**
	 * Host name/IP
	 */
	private LabelInputMeta hostname;

	/**
	 * Ping type
	 */
	private LabelSelectMeta pingtype;

	/**
	 * TimeOut(ms)
	 */
	private LabelInputMeta timeout;

	/**
	 * Nr packets to send
	 */
	private LabelInputMeta nbrPackets;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryPingDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryPing job = (JobEntryPing) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Job entry name
			this.name = new LabelInputMeta(id + ".name", "工作项目名称：", null, null,
					"工作项目名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Host name/IP
			this.hostname = new LabelInputMeta(id + ".hostname", "主机名称/IP：",
					null, null, "主机名称/IP必须填写", job.getHostname(), null,
					ValidateForm.getInstance().setRequired(true));
			this.hostname.setSingle(true);

			// 日志级别
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			options.add(new OptionDataMeta(job.classicPing, CLASSICPING));
			options.add(new OptionDataMeta(job.systemPing, SYSTEMPING));
			options.add(new OptionDataMeta(job.bothPings, SYSTEMANDCLASSIC));
			this.pingtype = new LabelSelectMeta(id + ".pingtype", "Ping类型：",
					null, null, null, job.pingtype, null, options);
			this.pingtype.setSingle(true);
			this.pingtype.addListener("change", 
			"jQuery.imeta.jobEntries.ping.pingChange");
			// TimeOut(ms)
			this.timeout = new LabelInputMeta(id + ".timeout", "超时时间(ms)：",
					null, null, "超时时间必须填写", String.valueOf(job.getTimeOut()),
					null, ValidateForm.getInstance().setRequired(true));
			this.timeout.setSingle(true);
			
			this.timeout.setDisabled(job.pingtype.equals(job.classicPing));
			
			

			// Nr packets to send
			this.nbrPackets = new LabelInputMeta(id + ".nbrPackets",
					"Nr数据包发送：", null, null, "Nr数据包发送必须填写", String.valueOf(job
							.getNbrPackets()), null, ValidateForm.getInstance()
							.setRequired(true));
			this.nbrPackets.setSingle(true);
			this.nbrPackets.setDisabled(job.pingtype.equals(job.systemPing));

			// 装载到form
			columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.name,
							this.hostname, this.pingtype, this.timeout,
							this.nbrPackets });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
