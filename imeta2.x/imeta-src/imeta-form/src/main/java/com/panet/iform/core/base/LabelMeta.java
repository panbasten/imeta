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

import org.apache.commons.lang.StringUtils;

import com.panet.iform.FormLoader;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.imeta.core.Const;

/**
*  标签功能
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class LabelMeta extends SimpleFormMeta {

	public static final String LABEL_TEXT_DIRECTION_LEFT = "ltr";
	public static final String LABEL_TEXT_DIRECTION_RIGHT = "rtl";

	/**
	 * 元数据
	 */
	private LabelDataMeta dataMeta;

	private String labelDirection = LABEL_TEXT_DIRECTION_LEFT;

	public LabelDataMeta getDataMeta() {
		return dataMeta;
	}

	public void setDataMeta(LabelDataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}

	/**
	 * 标签功能
	 * 
	 * @param dataMeta
	 *                元数据
	 * @param single
	 *              是否单独显示
	 */
	public LabelMeta(LabelDataMeta dataMeta, boolean single) {
		super(dataMeta, SimpleFormMeta.INPUT_TAG, single);
		this.dataMeta = dataMeta;
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.getSimpleFormDataMeta() != null) {
			String id = this.dataMeta.getId();
			String value = this.dataMeta.getValue();
			if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(value)) {
				super.setInit("$('[id=" + id + "]').val(\"" + FormLoader.replaceJavascriptString(value) + "\");");
			}

			if (LABEL_TEXT_DIRECTION_RIGHT.equals(this.labelDirection)) {
				this.dataMeta.setStyle("direction", this.labelDirection);
			}
			
			if(StringUtils.isEmpty(this.dataMeta.getTitle())){
				this.dataMeta.setTitle(Const.NVL(this.dataMeta.getValue(), ""));
			}
		}

		return super.getFormJo();
	}

	public void setLabelTextDirection(String d) {
		this.dataMeta.setStyle("direction", d);
	}

	public String getLabelDirection() {
		return labelDirection;
	}

	public void setLabelDirection(String labelDirection) {
		this.labelDirection = labelDirection;
	}

}
