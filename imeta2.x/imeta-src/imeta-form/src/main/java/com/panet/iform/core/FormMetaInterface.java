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

import com.panet.iform.exception.ImetaFormException;

import net.sf.json.JSONObject;


/**
* 表单操作接口类
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public interface FormMetaInterface {
	
	/**
	 * 得到表单对象的JSON表达式
	 * 
	 * @return
	 */
	public JSONObject getFormJo() throws ImetaFormException;

	/**
	 * 向当前表单对象中添加子元素
	 * 
	 * @param content
	 * @throws ImetaFormException
	 */
	public void append(BaseFormMeta content) throws ImetaFormException;
	
	/**
	 * 清空子元素
	 * @throws ImetaFormException
	 */
	public void removeAll()throws ImetaFormException;

	/**
	 * 将该表单对象添加到目标表单对象中
	 * 
	 * @param content
	 * @throws ImetaFormException
	 */
	public void appendTo(BaseFormMeta content) throws ImetaFormException;

	/**
	 * 设置表单对象宽度
	 * 
	 * @param width
	 */
	public void setWidth(int width);
	
	/**
	 * 字符串化
	 * @return
	 */
	public String toString();
}
