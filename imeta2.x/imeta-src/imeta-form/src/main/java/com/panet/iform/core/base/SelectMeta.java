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

import net.sf.json.JSONObject;

import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.exception.ImetaFormException;

/**
* 列表框功能
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class SelectMeta extends SimpleFormMeta {

	/**
	 * 验证信息
	 */
	private ValidateForm validate = null;

	/**
	 * 元数据
	 */
	private SelectDataMeta dataMeta = null;

	/**
	 * 是否为空
	 */
	private boolean hasEmpty = false;

	/**
	 * 数量
	 */
	private int size = 1;

	/**
	 * 列表框功能实现
	 * 
	 * @param dataMeta
	 *                元数据
	 * @param validate 
	 *                验证信息
	 * @param single
	 *              是否单独显示
	 */
	public SelectMeta(SelectDataMeta dataMeta, ValidateForm validate,
			boolean single) {
		super(dataMeta, SimpleFormMeta.SELECT_TAG, single);
		this.dataMeta = dataMeta;
		this.validate = validate;
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.dataMeta != null) {
			dataMeta.createOptions(hasEmpty);

			if (this.validate != null) {
				this.dataMeta.addClazz(this.validate.getValidateStr());
			}

			if (size > 1) {
				this.dataMeta.putProperty("size", size);
			}
		}
		return super.getFormJo();
	}

	@Override
	public void setReadonly(boolean readonly) {
		dataMeta.setReadonly(readonly);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isHasEmpty() {
		return hasEmpty;
	}

	public void setHasEmpty(boolean hasEmpty) {
		this.hasEmpty = hasEmpty;
	}

	/**
	 * 添加选项
	 * 
	 * @param v  String类型
	 * @param t  String类型
	 */
	public void appendOption(String v, String t) {
		this.dataMeta.appendOption(v, t);
	}
	
	public void appendOption(OptionDataMeta o) {
		this.dataMeta.appendOption(o);
	}

	/**
	 * 设置默认值
	 * 
	 * @param v	 String类型
	 */
	public void setDefaultValue(String v) {
		this.dataMeta.setValue(v);
	}
}
