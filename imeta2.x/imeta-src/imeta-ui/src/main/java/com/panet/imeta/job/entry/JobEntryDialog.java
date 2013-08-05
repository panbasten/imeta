package com.panet.imeta.job.entry;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;

public class JobEntryDialog implements Cloneable {

	private String id;

	private JobMeta jobMeta;

	private JobEntryCopy jobEntryMeta;

	protected Repository rep;

	/**
	 * 名称
	 */
	private String jobEntryName;

	private long jobEntryId;

	/**
	 * 确定按钮,取消按钮
	 */
	private ButtonMeta okBtn, cancelBtn;

	public JobEntryInterface getJobEntry() {
		return this.jobEntryMeta.getEntry();
	}

	public long getJobEntryAttributeInteger(String code) throws KettleException {
		if (jobEntryId <= 0)
			return 0;
		return rep.getJobEntryAttributeInteger(jobEntryId, code);
	}

	public String getJobEntryAttributeString(String code)
			throws KettleException {
		if (jobEntryId <= 0)
			return "";
		return rep.getJobEntryAttributeString(jobEntryId, code);
	}

	public boolean getJobEntryAttributeBoolean(String code)
			throws KettleException {
		if (jobEntryId <= 0)
			return false;
		return rep.getJobEntryAttributeBoolean(jobEntryId, code);
	}

	public int countNrJobEntryAttributes(String code) throws KettleException {
		if (jobEntryId <= 0)
			return 0;
		return rep.countNrJobEntryAttributes(jobEntryId, code);
	}

	public JobEntryDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		this.jobMeta = jobMeta;
		this.jobEntryMeta = jobEntryMeta;
		this.jobEntryId = jobEntryMeta.getID();
		this.jobEntryName = jobEntryMeta.getEntry().getName();

		this.rep = jobMeta.getRep();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JobMeta getJobMeta() {
		return jobMeta;
	}

	public void setJobMeta(JobMeta jobMeta) {
		this.jobMeta = jobMeta;
	}

	public JobEntryCopy getJobEntryMeta() {
		return jobEntryMeta;
	}

	public void setJobEntryMeta(JobEntryCopy jobEntryMeta) {
		this.jobEntryMeta = jobEntryMeta;
	}

	public long getJobEntryId() {
		return jobEntryId;
	}

	public void setJobEntryId(long jobEntryId) {
		this.jobEntryId = jobEntryId;
	}

	public ButtonMeta getOkBtn() {
		this.okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
		this.okBtn.putProperty("jobsName", this.jobMeta.getName());
		this.okBtn.putProperty("jobEntryName", this.jobEntryName);
		this.okBtn.putProperty("directoryId", this.jobMeta.getDirectory()
				.getID());
		this.okBtn.addClick("jQuery.imeta.jobEntries.btn.ok");
		return this.okBtn;
	}

	public ButtonMeta getCancelBtn() {
		this.cancelBtn = new ButtonMeta(id + ".btn.cancel", id + ".btn.cancel",
				"取消", "取消");
		this.cancelBtn.addClick("jQuery.imeta.jobEntries.btn.cancel");
		return this.cancelBtn;
	}

	public String getJobEntryName() {
		return jobEntryName;
	}

	public void setJobEntryName(String jobEntryName) {
		this.jobEntryName = jobEntryName;
	}

	/**
	 * 得到日志级别列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getLoglevel() {
		List<OptionDataMeta> loglevel = new ArrayList<OptionDataMeta>();
		for (int i = 0; i < LogWriter.log_level_desc_long.length; i++) {
			loglevel.add(new OptionDataMeta(String.valueOf(i),
					LogWriter.log_level_desc_long[i]));
		}
		return loglevel;
	}

	/**
	 * 得到数据库连接的列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getConnectionLine() {
		List<OptionDataMeta> conn = new ArrayList<OptionDataMeta>();
		DatabaseMeta ci;

		for (int i = 0; i < jobMeta.nrDatabases(); i++) {
			ci = jobMeta.getDatabase(i);
			conn.add(new OptionDataMeta(String.valueOf(ci.getID()), ci
					.getName()));
		}
		return conn;
	}
	
	/**
	 * 得到集群服务器列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getSlaveServer() {
		List<OptionDataMeta> slaveServerList = new ArrayList<OptionDataMeta>();
		String[] slaveServer = SlaveServer.getSlaveServerNames(jobMeta
				.getSlaveServers());
		for (int i = 0; i < slaveServer.length; i++) {
			slaveServerList.add(new OptionDataMeta(slaveServer[i],
					slaveServer[i]));
		}
		return slaveServerList;
	}
	
	/**
	 * 得到编码列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getEncoding() {
		List<OptionDataMeta> encoding = new ArrayList<OptionDataMeta>();
		List<Charset> values = new ArrayList<Charset>(Charset
				.availableCharsets().values());
		for (int i = 0; i < values.size(); i++) {
			Charset charSet = (Charset) values.get(i);
			encoding.add(new OptionDataMeta(charSet.name(), charSet
					.displayName()));
		}
		return encoding;
	}

	public String getOptionsByStringArrayWithNumberValue(String[] arr,
			boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				rtn.append(i);
				rtn.append(":");
				rtn.append(arr[i]);
				rtn.append(";");
			}
		}
		return rtn.toString();
	}
	
	/**
	 * 得到日期格式
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getDateFormats() {
		List<OptionDataMeta> dateFormat = new ArrayList<OptionDataMeta>();
		String dats[] = Const.getDateFormats();
		for (String date : dats) {
			dateFormat.add(new OptionDataMeta(date, date));
		}
		return dateFormat;
	}

}


