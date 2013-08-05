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

	/**
	* 非一般表格数据元
	* 
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class NullSimpleFormDataMeta extends SimpleFormDataMeta {

	public NullSimpleFormDataMeta() {
		super(null, null, null, null, null, null);
	}
	
	/**
	 * 非一般数据元定义
	 * 
	 * @return
	 */
	public static NullSimpleFormDataMeta getInstance() {
		return new NullSimpleFormDataMeta();
	}

}
