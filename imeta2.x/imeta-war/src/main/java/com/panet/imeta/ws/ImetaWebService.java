package com.panet.imeta.ws;

import java.util.List;

import javax.jws.WebService;

@WebService
public interface ImetaWebService {

	/**
	 * 运行
	 * 
	 * @param repName
	 *            资源库名称
	 * @param path
	 *            操作对象路径
	 * @param objectType
	 *            操作类型
	 * @param objectName
	 *            操作对象名称
	 * @param args
	 *            参数
	 * @return
	 */
	public String start(String repName, String path, String objectType,
			String objectName, String args);

	/**
	 * 停止
	 * 
	 * @param repName
	 * @param path
	 * @param objectType
	 * @param objectName
	 * @return
	 */
	public String stop(String repName, String path, String objectType,
			String objectName);

	/**
	 * 暂停
	 * 
	 * @param repName
	 * @param path
	 * @param objectType
	 * @param objectName
	 * @return
	 */
	public String pause(String repName, String path, String objectType,
			String objectName);

	/**
	 * 得到活动列表
	 * 
	 * @param repName
	 * @param path
	 * @param objectType
	 * @param objectName
	 * @return
	 */
	public List<String> getActiveObjectList(String repName, String path,
			String objectType, String objectName);
}
