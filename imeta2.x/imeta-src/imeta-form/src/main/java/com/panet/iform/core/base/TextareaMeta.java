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
	* 文本域功能
	* 
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class TextareaMeta extends SimpleFormMeta {
	
	/**
	 * 验证信息
	 */
	private ValidateForm validate = null;
  
	/**
     * 数据信息
     */
	private TextareaDataMeta dataMeta = null;

	/**
	 * 文本域生成
	 * 
	 * @param dataMeta  数据
	 * @param validate  验证
	 * @param single    唯一
	 */
	public TextareaMeta(TextareaDataMeta dataMeta, ValidateForm validate,
			boolean single) {
		super(dataMeta, SimpleFormMeta.TEXTAREA_TAG, single);
		this.validate = validate;
		this.dataMeta = dataMeta;
	}

	/**
	 * 得到json
	 * 
	 * @return 
	 * @throws ImetaFormException
	 */
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.dataMeta != null) {
			if (this.validate != null) {
				this.dataMeta.addClazz(this.validate.getValidateStr());
			}
		}
		return super.getFormJo();
	}
	
	/**
	 * 返回验证信息
	 * 
	 * @return
	 */
	public ValidateForm getValidate() {
		return validate;
	}

	/**
	 * 定义验证信息
	 * 
	 * @param validate
	 */
	public void setValidate(ValidateForm validate) {
		this.validate = validate;
	}

	public TextareaDataMeta getDataMeta() {
		return dataMeta;
	}
	
	/**
	 * 定义数据信息
	 * 
	 * @param dataMeta
	 */
	public void setDataMeta(TextareaDataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}
}
