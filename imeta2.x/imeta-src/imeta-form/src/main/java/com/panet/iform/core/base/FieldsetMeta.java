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

import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;

/**
* 字段设置
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class FieldsetMeta extends SimpleFormMeta {

	/**
	 * 字段设置
	 * 
	 * @param dataMeta
	 *                元数据
	 * @param single
	 *              是否单独显示
	 */
	public FieldsetMeta(SimpleFormDataMeta dataMeta, boolean single) {
		super(dataMeta, SimpleFormMeta.FIELDSET_TAG, single);
	}

}
