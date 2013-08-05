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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.Messages;
import com.panet.iform.exception.ImetaFormException;

	/**
	* 复杂表格元
	* 
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class ComplexFormMeta extends BaseFormMeta implements FormMetaInterface {

	/**
	 * 单根对象
	 */
	private SimpleFormMeta root = null;

	/**
	 * 多根对象
	 */
	private BaseFormMeta[] roots = null;

	/**
	 * 列表列数
	 */
	private int columnNum = 2;

	private ComplexFormDataMeta complexFormDataMeta;

	/**
	 * 是否多根
	 */
	private boolean isMultiRoot = false;
	
	public ComplexFormMeta(){
		
	}
	
	/**
	 * 复杂表格元初始化
	 * 
	 * @param complexFormDataMeta
	 */
	public ComplexFormMeta(ComplexFormDataMeta complexFormDataMeta) {
		this.complexFormDataMeta = complexFormDataMeta;
		super.setDataMeta(complexFormDataMeta);
	}
	/**
	 * 多根时返回
	 * 
	 * @return
	 */
	public boolean isMultiRoot() {
		return isMultiRoot;
	}
	
	/**
	 * 定义多根时
	 * @param isMultiRoot
	 */
	public void setMultiRoot(boolean isMultiRoot) {
		this.isMultiRoot = isMultiRoot;
	}
	
	/**
	 * 单根时返回
	 * @return
	 */
	public BaseFormMeta[] getRoots() {
		return roots;
	}
	
	/**
	 * 定义单根时
	 * @param roots
	 */
	public void setRoots(BaseFormMeta[] roots) {
		this.roots = roots;
	}

	public BaseFormMeta getRoot() {
		return root;
	}

	public void setRoot(SimpleFormMeta root) {
		this.root = root;
	}

	/**
	 * 得到单根JSON对象
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.root != null) {
			return this.root.getFormJo();
		}
		return null;
	}

	/**
	 * 得到多根JSON数组对象
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONArray getFormJa() throws ImetaFormException {
		if (this.roots != null) {
			JSONArray rtn = new JSONArray();
			for (BaseFormMeta meta : this.roots) {
				rtn.add(meta.getFormJo());
			}
			return rtn;
		}
		return null;
	}

	public void append(BaseFormMeta content) {
		if (content != null) {
			this.getContent().add(content);
		}
	}

	public void appendTo(BaseFormMeta content) {
		if (content != null)
			content.append(this);
	}

	@Override
	public String toString() {
		try {
			if (this.isMultiRoot) {
				return getFormJa().toString();
			} else {
				return getFormJo().toString();
			}
		} catch (ImetaFormException e) {
			return Messages.getString("IForm.CreateJSON.Error");
		}
	}
	
	/**
	 * 列数返回
	 * @return
	 */
	public int getColumnNum() {
		return columnNum;
	}
	
	/**
	 * 定义列数
	 * @param columnNum
	 */
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	@Override
	public void setWidth(int width) {
		if (this.root != null) {
			this.root.setWidth(width);
		}
	}
	
	/**
	 * 返回复杂表元数据
	 * @return
	 */
	public ComplexFormDataMeta getComplexFormDataMeta() {
		return complexFormDataMeta;
	}
	
	/**
	 * 定义复杂表元数据
	 * @param complexFormDataMeta
	 */
	public void setComplexFormDataMeta(ComplexFormDataMeta complexFormDataMeta) {
		this.complexFormDataMeta = complexFormDataMeta;
	}
}
