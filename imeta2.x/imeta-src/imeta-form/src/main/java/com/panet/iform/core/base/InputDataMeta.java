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
* 输入样式类型
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class InputDataMeta extends SimpleFormDataMeta {

	public static final String INPUT_TYPE_TEXT = "text";
	public static final String INPUT_TYPE_BUTTON = "button";
	public static final String INPUT_TYPE_RADIO = "radio";
	public static final String INPUT_TYPE_CHECKBOX = "checkbox";
	public static final String INPUT_TYPE_PASSWORD = "password";
	public static final String INPUT_TYPE_SUBMIT = "submit";
	public static final String INPUT_TYPE_HIDDEN = "hidden";
	public static final String INPUT_TYPE_FILE = "file";

	/**
	 * 初始值
	 */
	private String value = null;

	/**
	 * 类型
	 */
	private String type = "text";

	/**
	 * 输入类型数据管理
	 * 
	 * @param id
	 *          ID
	 * @param name
	 *            标签名
	 * @param clazz
	 *             样式类型
	 * @param style
	 *             内嵌样式类型
	 * @param title
	 *             注释
	 * @param value
	 *             初始值
	 * @param type
	 *            类型
	 */
	public InputDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String value, String type) {
		super(id, name, clazz, style, title, null);

		if (!StringUtils.isEmpty(value)) {
			this.value = value;
			this.putProperty("value", value);
		}

		if (!StringUtils.isEmpty(type)) {
			this.type = type;
		}
		this.putProperty("type", this.type);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		this.putProperty("value", value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.putProperty("type", this.type);
	}

}
