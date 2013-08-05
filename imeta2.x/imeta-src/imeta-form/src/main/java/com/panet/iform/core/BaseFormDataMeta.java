/**
 * @(#)InputMeta.java 1.00 2009/06/22
 *
 * Copyright (c) 2009 中科软科技股份有限公司 版权所有
 * Sinosoft Co.,LTD. All rights reserved.
 * 
 * This software was developed by Sinosoft Corporation. 
 * You shall not disclose and decompile such software 
 * information or code and shall use it only in accordance 
 * with the terms of the contract agreement you entered 
 * into with Sinosoft.
 */
package com.panet.iform.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.exception.ImetaFormException;

	/**
	* 表格基本数据元
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class BaseFormDataMeta implements Cloneable, FormDataMetaInterface {

	/**
	 * ID
	 */
	private String id = null;

	/**
	 * 属性集
	 */
	private Map<String, Object> properties;

	/**
	 * 初始化
	 * 
	 * @param id
	 */
	public BaseFormDataMeta(String id) {
		properties = new HashMap<String, Object>();
		this.id = id;
		if (!StringUtils.isEmpty(id))
			properties.put("id", id);
	}

	/**
	 * 添加属性
	 * 
	 * @param key
	 * @param value
	 */
	@Override
	public void putProperty(String key, Object value) {
		if (value != null) {
			properties.put(key, value);
		} else {
			properties.remove(key);
		}

	}

	/**
	 * 添加属性集
	 * 
	 * @param prop
	*/
	
	@Override
	public void putProperties(Map<String, Object> prop) {
		properties.putAll(prop);
	}
	
	/**
	 * 返回id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 定义id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 得到属性的JSON表达式
	 */
	@Override
	public JSONObject getPropertiesJo() throws ImetaFormException {
		JSONObject jo = new JSONObject();
		Map.Entry<String, Object> entry;
		if (this.properties.size() == 0) {
			return null;
		}
		for (Iterator<Map.Entry<String, Object>> iter = this.properties
				.entrySet().iterator(); iter.hasNext();) {
			entry = iter.next();
			if (entry.getValue() != null) {
				if (entry.getValue() instanceof String) {
					if (!StringUtils.isEmpty((String) entry.getValue())) {
						jo.put(entry.getKey(), ((String) entry.getValue()));
					}
				} else {
					jo.put(entry.getKey(), entry.getValue());
				}
			}

		}

		return jo;
	}
 
	public Map<String, Object> getProperties() {
		return properties;
	}
 
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
