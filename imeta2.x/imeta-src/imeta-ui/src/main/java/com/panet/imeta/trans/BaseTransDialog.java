package com.panet.imeta.trans;

import java.util.ArrayList;
import java.util.List;

import com.panet.iform.core.base.OptionDataMeta;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.step.StepMeta;

public class BaseTransDialog implements Cloneable {
	private TransMeta transMeta;

	private TransExecutionConfiguration configuration;

	protected Repository rep;

	public TransMeta getTransMeta() {
		return transMeta;
	}

	public void setTransMeta(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	public void setRepository(Repository repository) {
		this.rep = repository;
	}

	public BaseTransDialog(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	public String getId() {
		return "trans_" + this.transMeta.getID();
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

		for (int i = 0; i < transMeta.nrDatabases(); i++) {
			ci = transMeta.getDatabase(i);
			conn.add(new OptionDataMeta(String.valueOf(ci.getID()), ci
					.getName()));
		}
		return conn;
	}

	/**
	 * 得到所有步骤名列表项
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getAllStepNames() {
		List<OptionDataMeta> stepOptions = new ArrayList<OptionDataMeta>();
		StepMeta stepMeta;
		for (int i = 0; i < transMeta.nrSteps(); i++) {
			stepMeta = transMeta.getStep(i);
			stepOptions.add(new OptionDataMeta(getStepName(stepMeta),
					getStepName(stepMeta)));
		}
		return stepOptions;
	}

	public String getStepName(StepMeta stepMeta) {
		if (stepMeta != null) {
			return stepMeta.getName();
		}
		return "";
	}

	public TransExecutionConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(TransExecutionConfiguration configuration) {
		this.configuration = configuration;
	}
}
