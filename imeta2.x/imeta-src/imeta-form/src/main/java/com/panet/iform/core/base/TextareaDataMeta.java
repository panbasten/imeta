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

import com.panet.iform.FormLoader;
import com.panet.iform.core.SimpleFormDataMeta;

/**
* 文本域数据管理功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class TextareaDataMeta extends SimpleFormDataMeta {

	private int rows = 3;

	public static final String ATTRITUDE_ROWS = "rows";
	
	/**
	 * 文本域数据管理
	 * 
	 * @param id      标签id
	 * @param name    标签名称
	 * @param clazz   标签clazz
	 * @param style   标签方式
	 * @param title   标签标题
	 * @param html    文本
	 * @param rows    行
	 */
	public TextareaDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String html, int rows) {
		super(id, name, clazz, style, title, html);
		this.rows = rows;
		super.putProperty(ATTRITUDE_ROWS, rows);
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		super.putProperty(ATTRITUDE_ROWS, rows);
	}

}
