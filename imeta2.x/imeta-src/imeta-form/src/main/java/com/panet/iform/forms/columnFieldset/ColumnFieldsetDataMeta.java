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
package com.panet.iform.forms.columnFieldset;

import com.panet.iform.core.ComplexFormDataMeta;

/**
* 文本栏设置数据功能
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ColumnFieldsetDataMeta extends ComplexFormDataMeta {
	
	/**
	 * 图例
	 */
	private String legends;

	/**
	 * 初始化
	 * 
	 * @param id
	 *            字段ID
	 *            
	 * @param legends 
	 * 			  图例
	 */
	public ColumnFieldsetDataMeta(String id, String legends) {
		super(id);
		this.legends = legends;
	}

	public String getLegends() {
		return legends;
	}

	public void setLegends(String legends) {
		this.legends = legends;
	}

}
