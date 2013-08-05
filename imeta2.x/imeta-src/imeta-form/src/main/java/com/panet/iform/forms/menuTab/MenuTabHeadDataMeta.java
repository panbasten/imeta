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
package com.panet.iform.forms.menuTab;

import com.panet.iform.core.SimpleFormDataMeta;

/**
* MenuTab头部元数据
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/
public class MenuTabHeadDataMeta extends SimpleFormDataMeta {

	/**
	 * 标签头ID
	 */
	private String value;
	
	/**
	 * 标签头显示文字
	 */
	private String label;

	/**
	 * 标签头快捷键
	 */
	private String accessKey;

	/**
	 * MenuTab头部元数据初始化
	 * 
	 * @param id 标签头ID
	 * @param value 标签头初始值
	 * @param label 标签名
	 * @param accessKey 标签头快捷键
	 */
	public MenuTabHeadDataMeta(String value, String label, String accessKey) {
		super(null, null, null, null, null, label);
		this.value = value;
		this.label = label;
		this.accessKey = accessKey;
		this.putProperty("value", this.value);
		this.putProperty("label", this.label);
		this.putProperty("accessKey", this.accessKey);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

}
