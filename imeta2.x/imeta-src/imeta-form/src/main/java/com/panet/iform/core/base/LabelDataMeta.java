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

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.SimpleFormDataMeta;

/**
*  标签数据
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class LabelDataMeta extends SimpleFormDataMeta {

	/**
	 * 标记
	 */
	private String forTag = null;

	/**
	 * 初始值
	 */
	private String value = null;

	public String getValue() {
		return value;
	}

	/**
	 * 初始化
	 * 
	 * @param value 初始值
	 */
	public void setValue(String value) {
		this.value = value;
		this.putProperty("value", this.value);
	}

	/**
	 * 初始化
	 * 
	 * @param id
	 *          ID
	 * @param name
	 *            名称
	 * @param value
	 *             初始值
	 * @param forTag
	 *              标签
	 */
	public LabelDataMeta(String id, String name, String value, String forTag) {
		super(id, name, new String[] { "label" }, null, null, null);

		if (!StringUtils.isEmpty(value)) {
			this.value = value;
			this.putProperty("value", this.value);
		}

		if (!StringUtils.isEmpty(forTag)) {
			this.forTag = forTag;
			this.putProperty("for", forTag);
		}

		this.putProperty("readonly", true);
	}

	public String getForTag() {
		return forTag;
	}

	public void setForTag(String forTag) {
		this.forTag = forTag;
		this.putProperty("for", forTag);
	}

}
