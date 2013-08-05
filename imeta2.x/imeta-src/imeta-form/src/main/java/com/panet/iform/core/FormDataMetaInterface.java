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

import java.util.Map;

import net.sf.json.JSONObject;

import com.panet.iform.exception.ImetaFormException;

/**
* 表格数据元接口
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public interface FormDataMetaInterface {
	
	/**
	 * 添加属性
	 * 
	 * @param key
	 * @param value
	 */
	public void putProperty(String key, Object value);

	/**
	 * 添加属性集
	 * 
	 * @param prop
	 */
	public void putProperties(Map<String, Object> prop);

	/**
	 * 得到属性的JSON表达式
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getPropertiesJo() throws ImetaFormException;
}
