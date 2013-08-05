package com.panet.imeta.job;

import java.util.ArrayList;
import java.util.List;

import com.panet.iform.core.base.OptionDataMeta;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.repository.Repository;

public class BaseJobDialog implements Cloneable {
	private JobMeta jobMeta;

	protected Repository rep;

	public void setRepository(Repository repository) {
		this.rep = repository;
	}

	public BaseJobDialog(JobMeta jobMeta) {
		this.jobMeta = jobMeta;
	}

	public String getId() {
		return "job_" + this.jobMeta.getID();
	}

	public JobMeta getJobMeta() {
		return jobMeta;
	}

	public void setJobMeta(JobMeta jobMeta) {
		this.jobMeta = jobMeta;
	}

	public Repository getRep() {
		return rep;
	}

	public void setRep(Repository rep) {
		this.rep = rep;
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
}
