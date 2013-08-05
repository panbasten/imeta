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

import com.panet.iform.core.SimpleFormDataMeta;

/**
* 表单数据处理
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ADataMeta extends SimpleFormDataMeta {

	/**
	 * 链接源
	 */
	private String href;

	/**
	 * 表单数据处理
	 * 
	 * @param id
	 *          链接源ID
	 * @param name
	 *            链接源名称
	 * @param clazz
	 *             链接源样式类型
	 * @param style
	 *             链接源内嵌样式类型
	 * @param title
	 *             链接源注释
	 * @param href
	 *            链接源路径         
	 * @param text
	 *            链接源文本
	 */
	public ADataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String href, String text) {
		super(id, name, clazz, style, title, text);
		this.href = href;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
