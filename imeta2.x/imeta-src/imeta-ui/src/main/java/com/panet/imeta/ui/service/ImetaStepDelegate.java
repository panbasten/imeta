package com.panet.imeta.ui.service;

import net.sf.json.JSONObject;

import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public interface ImetaStepDelegate {
	/**
	 * 编辑转换节点
	 * 
	 * @param id
	 * @param transMeta
	 * @param stepMeta
	 * @return
	 * @throws ImetaException
	 */
	public JSONObject editStep(String id, TransMeta transMeta, StepMeta stepMeta)
			throws ImetaException;

	/**
	 * 编辑任务节点
	 * 
	 * @param id
	 * @param jobMeta
	 * @param jobEntryMeta
	 * @return
	 * @throws ImetaException
	 */
	public JSONObject editStep(String id, JobMeta jobMeta, JobEntryCopy jobEntryMeta)
			throws ImetaException;
}
