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
package com.panet.iform.core.base;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.SimpleFormDataMeta;

/**
* 表单数据管理
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class FormDataMeta extends SimpleFormDataMeta {

	/**
	 * 方法
	 */
	private String method = "post";

	/**
	 * 功能
	 */
	private String action = null;

	/**
	 * 表格数据管理
	 * 
	 * @param id
	 *          ID
	 * @param name
	 *            名称
	 * @param clazz
	 *             样式类型
	 * @param style
	 *             内嵌样式类型
	 * @param title
	 *             注释
	 * @param html
	 *            标签显示
	 * @param method
	 *              方法
	 * @param action
	 *              功能
	 */
	public FormDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String html,
			String method, String action) {
		super(id, name, clazz, style, title, html);

		if (!StringUtils.isEmpty(method)) {
			this.method = method;
			this.putProperty("method", method);
		}

		if (!StringUtils.isEmpty(action)) {
			this.action = action;
			this.putProperty("action", action);
		}
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
