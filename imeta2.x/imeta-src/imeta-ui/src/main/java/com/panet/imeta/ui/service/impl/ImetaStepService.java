package com.panet.imeta.ui.service.impl;

import java.lang.reflect.Constructor;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;
import com.panet.imeta.ui.exception.ImetaException;
import com.panet.imeta.ui.service.ImetaStepDelegate;

@Service("imeta.ui.imetaStepService")
public class ImetaStepService implements ImetaStepDelegate {

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
			throws ImetaException {
		try {
			String name = stepMeta.getName();
			// TODO
			// 访问数据库得到step所有信息
			StepDialogInterface dialog = getStepEntryDialog(id, stepMeta,
					transMeta, name);
			return dialog.open();
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	private StepDialogInterface getStepEntryDialog(String id,
			StepMeta stepMeta, TransMeta transMeta, String stepName)
			throws KettleException {
		StepMetaInterface stepMetaInterface = stepMeta.getStepMetaInterface();
		String dialogClassName = stepMetaInterface.getDialogClassName();
		System.out.println("d:" + dialogClassName);
		Class<?> dialogClass;
		Constructor<?> dialogConstructor;
		try {
			dialogClass = stepMetaInterface.getClass().getClassLoader()
					.loadClass(dialogClassName);
			dialogConstructor = dialogClass.getConstructor(StepMeta.class,
					TransMeta.class);
			StepDialogInterface rtn = (StepDialogInterface) dialogConstructor
					.newInstance(stepMeta, transMeta);
			rtn.setId(id);
			return rtn;
		} catch (Exception e) {
			throw new KettleException(e);
		}
	}

	/**
	 * 编辑作业节点
	 * 
	 * @param id
	 * @param jobMeta
	 * @param jobEntryMeta
	 * @return
	 * @throws ImetaException
	 */
	public JSONObject editStep(String id, JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) throws ImetaException {
		try {
			String name = jobEntryMeta.getName();
			JobEntryDialogInterface dialog = getJobEntryDialog(id, jobMeta,
					jobEntryMeta, name);
			return dialog.open();
		} catch (Exception e) {
			throw new ImetaException(e);
		}
	}

	private JobEntryDialogInterface getJobEntryDialog(String id,
			JobMeta jobMeta, JobEntryCopy jobEntryMeta, String stepName)
			throws KettleException {
		JobEntryInterface jobEntryInterface = jobEntryMeta.getEntry();

		String dialogClassName = jobEntryInterface.getDialogClassName();
		System.out.println(dialogClassName);
		Class<?> dialogClass;
		Constructor<?> dialogConstructor;
		try {
			dialogClass = jobEntryInterface.getClass().getClassLoader()
					.loadClass(dialogClassName);
			dialogConstructor = dialogClass.getConstructor(JobMeta.class,
					JobEntryCopy.class);
			JobEntryDialogInterface rtn = (JobEntryDialogInterface) dialogConstructor
					.newInstance(jobMeta, jobEntryMeta);
			rtn.setId(id);
			return rtn;
		} catch (Exception e) {
			throw new KettleException(e);
		}
	}
}
