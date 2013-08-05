package com.plywet.imeta.core.property;

import com.plywet.imeta.laf.Handler;

/**
 * 属性配置的持有者接口
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 上午10:45:44
 */
public interface PropertyHandler extends Handler {

	/**
	 * 外部加载属性配置文件
	 * 
	 * @param filename
	 * @return
	 */
	public boolean loadProps(String filename);

	/**
	 * 返回给定key的属性值
	 * 
	 * @param key
	 * @return null if the key is not found
	 */
	public String getProperty(String key);

	/**
	 * 从属性列表中返回给定key的属性值，在属性值没有找到的时候返回defValue
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public String getProperty(String key, String defValue);
}
