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

import java.util.ArrayList;
import java.util.List;

import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;

/**
* 表的实现功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class TableMeta extends SimpleFormMeta {
	
	/**
	  * 表格头部
	  */
	private SimpleFormMeta tbody;
	
	/**
	  * trs
	  */
	private List<SimpleFormMeta> trs;
	
	/**
	  * tds
	  */
	private List<List<SimpleFormMeta>> tds;

	/**
	 * 表格功能实现
	 * 
	 * @param dataMeta  数据元
	 * @param single   唯一
	 */
	public TableMeta(TableDataMeta dataMeta, boolean single) {
		super(dataMeta, SimpleFormMeta.TABLE_TAG, single);
		this.tbody = new SimpleFormMeta(NullSimpleFormDataMeta.getInstance(),
				"<tbody></tbody>", false);
		this.append(this.tbody);

		initTable(dataMeta.getRowNum(), dataMeta.getColumnNum());

	}
	
	/**
	 * 预制表格
	 * @param rowNum   行数
	 * @param columnNum  列数
	 */
	public void initTable(int rowNum, int columnNum) {
		this.trs = new ArrayList<SimpleFormMeta>();
		this.tds = new ArrayList<List<SimpleFormMeta>>();
		SimpleFormMeta tr, td;
		List<SimpleFormMeta> tdList;
		for (int i = 0; i < rowNum; i++) {
			tr = new SimpleFormMeta(NullSimpleFormDataMeta.getInstance(),
					"<tr></tr>", false);
			this.trs.add(tr);
			tdList = new ArrayList<SimpleFormMeta>();
			this.tds.add(tdList);
			for (int j = 0; j < columnNum; j++) {
				td = new SimpleFormMeta(NullSimpleFormDataMeta.getInstance(),
						"<td></td>", false);
				tr.append(td);
				tdList.add(td);
			}
		}
	}
	
/**
 * putCell生成
 * 
 * @param rowIndex  行索引
 * @param columnIndex  列索引
 */
	public void putCell(int rowIndex, int columnIndex) {

	}
}
