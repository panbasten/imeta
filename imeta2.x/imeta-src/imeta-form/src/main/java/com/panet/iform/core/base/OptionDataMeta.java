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

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.exception.KettleException;

/**
 * 选取框数据管理功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class OptionDataMeta implements Cloneable {
	/**
	 * 值
	 */
	private String value = null;

	/**
	 * 文字
	 */
	private String text = null;

	/**
	 * 是否选定
	 */
	private boolean selected = false;

	public OptionDataMeta() {
		this("", "");
	}

	/**
	 * 选取框数据管理
	 * 
	 * @param value
	 *            初始值
	 * @param text
	 *            文本
	 */
	public OptionDataMeta(String value, String text) {
		if (!StringUtils.isEmpty(value))
			this.value = value;
		if (!StringUtils.isEmpty(text))
			this.text = text;

	}

	/**
	 * 字符串转换
	 */
	public String toString() {
		StringBuffer option = new StringBuffer();
		option.append("<option value='");
		option.append(StringUtils.trimToEmpty(this.value));
		option.append("'");
		if (this.selected) {
			option.append(" selected");
		}
		option.append(">");
		option.append(StringUtils.trimToEmpty(this.text));
		option.append("</option>");
		return option.toString();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public static List<OptionDataMeta> getOptionDataMetaList(String[] keys,
			String[] values) throws KettleException {
		List<OptionDataMeta> rtn = new ArrayList<OptionDataMeta>();
		try {
			if (keys != null) {
				for (int i = 0; i < keys.length; i++) {
					rtn.add(new OptionDataMeta(keys[i],
							(values != null) ? values[i] : keys[i]));
				}
			}
			return rtn;
		} catch (Exception ke) {
			throw new KettleException(ke.getMessage(), ke);
		}
	}

}
