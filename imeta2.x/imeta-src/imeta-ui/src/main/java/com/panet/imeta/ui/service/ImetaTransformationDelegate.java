package com.panet.imeta.ui.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.exception.ImetaFormException;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.exception.ImetaException;

public interface ImetaTransformationDelegate {

	/**
	 * 得到正在运行的Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public Trans getActiveTransByName(Repository rep, long directoryId,
			String transname) throws ImetaException;

	/**
	 * 清除Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 */
	public void clearActiveTransByName(Repository rep, long directoryId,
			String transname);

	/**
	 * 得到转换
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public TransMeta getTransMetaByName(Repository rep, long directoryId,
			String transname) throws ImetaException;

	public TransMeta newTransMetaByName(Repository rep, long directoryId,
			String transname) throws KettleException;

	/**
	 * 清除完成的Trans
	 */
	public void clearActiveTrans();

	/**
	 * 保存Trans
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @param trans
	 */
	public void putActiveTransByName(Repository rep, long directoryId,
			String transname, Trans trans);

	/**
	 * 通过名称名称获得Transformation
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @return
	 * @throws ImetaException
	 */
	public TransMeta getEditTransMetaByName(Repository rep, long directoryId,
			String transname) throws ImetaException;

	/**
	 * 通过名称清除一个Transformation
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 */
	public void clearTransMetaByName(Repository rep, long directoryId,
			String transname);

	/**
	 * 通过名称清除一个正在编辑的转换
	 * @param rep
	 * @param directoryId
	 * @param transname
	 */
	public void clearEditTransMetaByName(Repository rep, long directoryId,
			String transname);

	/**
	 * 通过用户登录名清除正在编辑的转换
	 * 
	 * @param rep
	 * @param loginName
	 */
	public void clearEditTransMetaByLoginName(Repository rep, String loginName);

	/**
	 * 得到转换设置的JSON表达式
	 * 
	 * @param rep
	 * @param directoryId
	 * @param transname
	 * @param winId
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getSettingContent(Repository rep, long directoryId,
			String transname, String winId) throws ImetaFormException;

	/**
	 * 加入转换的步骤
	 * 
	 * @throws ImetaException
	 */
	public JSONArray getTransStepTab() throws ImetaException;

	/**
	 * 设置一个Transformation
	 * 
	 * @param rep
	 * @param transMeta
	 * @throws ImetaException
	 */
	public void putEditTransMeta(Repository rep, TransMeta transMeta)
			throws ImetaException;

	/**
	 * 增加数据库
	 * 
	 * @param databaseMeta
	 */
	public void addOrReplaceDatabase(DatabaseMeta databaseMeta);

	/**
	 * 移除数据库
	 * 
	 * @param i
	 */
	public void removeDatabases(long i);
}
