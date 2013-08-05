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
*  按键类型
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ButtonDataMeta extends SimpleFormDataMeta {

	/**
	 * 按键文本
	 */
	private String text;

	/**
	 * 按键基本类型数据
	 * 
	 * @param id
	 *          按键ID
	 * @param name
	 *            按键名称
	 * @param clazz
	 *             按键样式类型
	 * @param style
	 *             按键内嵌样式类型
	 * @param title
	 *            按键注释
	 * @param text
	 *            按键文本
	 * 
	 */
	public ButtonDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String text) {
		super(id, name, clazz, style, title, text);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
