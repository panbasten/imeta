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
* 表单元数据
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class TableDataMeta extends SimpleFormDataMeta {

	private int rowNum = 1;
    private int columnNum = 1;
	
	/**
	 *表单数据元初始化
	 *
	 * @param id   标签id
	 * @param name  标签名称
	 * @param clazz  标签clazz
	 * @param style  标签方式
	 * @param rowNum  行数
	 * @param columnNum 列数 
	 */
	public TableDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, int rowNum, int columnNum) {
		super(id, name, clazz, style, null, null);
		setRowNum(rowNum);
		setColumnNum(columnNum);
	}
	
	public int getRowNum() {
		return rowNum;
	}
	
	public void setRowNum(int rowNum) {
		if (rowNum < 1)
			rowNum = 1;
		this.rowNum = rowNum;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		if (columnNum < 1)
			columnNum = 1;
		this.columnNum = columnNum;
	}

}
