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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.FormLoader;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.exception.ImetaFormException;

/**
 * 输入
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class InputMeta extends SimpleFormMeta {

	/**
	 * 验证信息
	 */
	private ValidateForm validate = null;

	/**
	 * 元数据
	 */
	private InputDataMeta dataMeta = null;

	public ValidateForm getValidate() {
		return validate;
	}

	/**
	 * 设置
	 * 
	 * @param validate
	 *            验证信息
	 */
	public void setValidate(ValidateForm validate) {
		this.validate = validate;
	}

	/**
	 * 初始化
	 * 
	 * @param dataMeta
	 *            元数据
	 * @param validate
	 *            验证信息
	 * @param single
	 *            是否单独显示
	 */
	public InputMeta(InputDataMeta dataMeta, ValidateForm validate,
			boolean single) {
		super(dataMeta, SimpleFormMeta.INPUT_TAG, single);
		this.dataMeta = dataMeta;
		this.validate = validate;
	}

	/**
	 * 初始化
	 * 
	 * @param id
	 *            ID
	 * @param name
	 *            名称
	 * @param clazz
	 *            样式类型
	 * @param style
	 *            内嵌样式类型
	 * @param title
	 *            注释
	 * @param value
	 *            初始值
	 * @param type
	 *            类型
	 * @param validate
	 *            验证信息
	 * @param single
	 *            是否单独显示
	 */
	public InputMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String value, String type,
			ValidateForm validate, boolean single) {
		this(new InputDataMeta(id, name, clazz, style, title, value, type),
				validate, single);
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.dataMeta != null) {
			if (!StringUtils.isEmpty(this.dataMeta.getValue())
					&& !StringUtils.isEmpty(this.dataMeta.getId())) {

				super.setInit("$('[id=" + this.dataMeta.getId()
						+ "]').val($('[id=" + this.dataMeta.getId() + "]').attr('val'));");
			}
			if (InputDataMeta.INPUT_TYPE_CHECKBOX.equals(this.dataMeta
					.getType())
					|| InputDataMeta.INPUT_TYPE_RADIO.equals(this.dataMeta
							.getType())) {
				this.dataMeta.setStyle("border", "none");

				if ("true".equalsIgnoreCase(this.dataMeta.getValue())) {
					super.setInit("$('[id=" + this.dataMeta.getId()
							+ "]').attr('checked',true);");
				} else {
					super.setInit("$('[id=" + this.dataMeta.getId()
							+ "]').attr('checked',false);");
				}

			}

			if (this.validate != null) {
				this.dataMeta.addClazz(this.validate.getValidateStr());
			}
		}

		return super.getFormJo();
	}

	public String getType() {
		return this.dataMeta.getType();
	}

}
