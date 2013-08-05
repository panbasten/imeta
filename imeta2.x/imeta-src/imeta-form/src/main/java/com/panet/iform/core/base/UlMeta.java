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
	* UL元
	* 
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class UlMeta extends SimpleFormMeta {

	/**
	 * UL生成初始化
	 * 
	 * @param dataMeta  数据元
	 * @param single    唯一
	 */
	public UlMeta(SimpleFormDataMeta dataMeta, boolean single) {
		super(dataMeta, SimpleFormMeta.UL_TAG, single);
	}

}
