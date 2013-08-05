package com.plywet.mm.dao;

/**
 * 通用属性提供者接口类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-12 上午07:45:18
 */
public interface MMPropertiesProvider {

	public Object get(int key);

	public void put(int key, Object value);
}
