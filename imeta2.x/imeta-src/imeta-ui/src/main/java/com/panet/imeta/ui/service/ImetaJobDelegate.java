package com.panet.imeta.ui.service;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.exception.ImetaFormException;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.ui.exception.ImetaException;

/**
 * 元数据作业服务层接口类
 * 
 * @author Peter Pan
 * 
 */
public interface ImetaJobDelegate {

	/**
	 * 得到活动的Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 * @return
	 * @throws ImetaException
	 */
	public Job getActiveJobByName(Repository rep, long directoryId,
			String jobname) throws ImetaException;

	/**
	 * 清除Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 */
	public void clearActiveJobByName(Repository rep, long directoryId,
			String jobname);

	public void clearEditJobMetaByName(Repository rep, long directoryId,
			String jobname);

	/**
	 * 通过用户登录名清除正在编辑的转换
	 * 
	 * @param rep
	 * @param loginName
	 */
	public void clearEditJobMetaByLoginName(Repository rep, String loginName);

	/**
	 * 保存Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 * @param job
	 */
	public void putActiveJobByName(Repository rep, long directoryId,
			String jobname, Job job);

	/**
	 * 保存Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 * @return
	 * @param job
	 */
	public JobMeta getEditJobMetaByName(Repository rep, long directoryId,
			String jobname) throws ImetaException;

	/**
	 * 设置Job
	 * 
	 * @param rep
	 * @param jobMeta
	 * @throws ImetaException
	 */
	public void putEditJobMeta(Repository rep, JobMeta jobMeta)
			throws ImetaException;

	public JobMeta newJobMetaByName(Repository rep, long directoryId,
			String jobname) throws KettleException;

	/**
	 * 清除不活的的Job
	 */
	public void clearActiveJob();

	/**
	 * 加入任务的实体
	 * 
	 * @throws ImetaException
	 */
	public JSONArray getJobEntryTab() throws ImetaException;

	/**
	 * 载入目录中的Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 * @return
	 * @throws ImetaException
	 */
	public JobMeta getJobMetaByName(Repository rep, long directoryId,
			String jobname) throws ImetaException;

	/**
	 * 通过名称清除一个Job
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 */
	public void clearJobMetaByName(Repository rep, long directoryId,
			String jobname);

	/**
	 * 增加数据库
	 * 
	 * @param databaseMeta
	 */
	public void addOrReplaceDatabases(DatabaseMeta databaseMeta);

	/**
	 * 移除数据库
	 * 
	 * @param i
	 */
	public void removeDatabases(long i);

	/**
	 * 得到Job设置的JSON表达式
	 * 
	 * @param rep
	 * @param directoryId
	 * @param jobname
	 * @param winId
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getSettingContent(Repository rep, long directoryId,
			String jobname, String winId) throws ImetaFormException;

	public Map<String, Job> getActiveJobsMap();

	public Map<String, JobMeta> getJobMetaMap();

	/**
	 * 初始化自动执行JOBS
	 * 
	 * @param rep
	 * @param log
	 * 
	 * @throws ImetaException
	 */
	public void initAutoRunJobs(Repository rep, LogWriter log)
			throws ImetaException;

	public void setAutoRun();

	public boolean isAutoRun();

	public void clearAutoRunJobs();

	public Map<String, String> getAutoRunJobs();

}
