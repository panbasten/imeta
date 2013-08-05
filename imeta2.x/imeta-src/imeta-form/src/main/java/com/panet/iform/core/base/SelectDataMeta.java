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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.Messages;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 列表框数据管理功能
 * 
 *@author panwei
 *@version 1.0
 *@since 2009/06/22
 */
public class SelectDataMeta extends SimpleFormDataMeta {

	/**
	 * 初始值
	 */
	private String value = null;

	private List<OptionDataMeta> options;

	/**
	 * 列表框数据管理
	 * 
	 * @param id
	 *            ID
	 * @param name
	 *            名称
	 * @param clazz
	 *            样式类型
	 * @param style
	 *            内嵌样式类型
	 * @param value
	 *            初始值
	 * @param options
	 *            选项
	 */
	public SelectDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String value,
			List<OptionDataMeta> options) {
		super(id, name, clazz, style, null, null);

		if (!StringUtils.isEmpty(value)) {
			this.value = value;
			this.putProperty("value", value);
		}

		if (options != null) {
			this.options = options;
		} else {
			this.options = new ArrayList<OptionDataMeta>();
		}
	}

	/**
	 * 创建一个新的选择类型
	 * 
	 * @param hasEmpty
	 *            是否填写
	 * @throws ImetaFormException
	 *             抛出异常
	 */
	public void createOptions(boolean hasEmpty) throws ImetaFormException {
		try {
			StringBuffer optionSb = new StringBuffer();
			String v = StringUtils.trimToEmpty(this.value);
			if (this.options != null) {
				OptionDataMeta kv;

				if (hasEmpty) {
					kv = new OptionDataMeta("-1", "");
					optionSb.append(kv.toString());
				}

				for (Iterator<OptionDataMeta> iter = this.options.iterator(); iter
						.hasNext();) {
					kv = iter.next();
					if (v.equals(kv.getValue())) {
						kv.setSelected(true);
					}
					optionSb.append(kv.toString());
				}

				super.setHtml(optionSb.toString());
			}
		} catch (Exception ex) {
			throw new ImetaFormException(Messages
					.getString("IForm.CreateJSON.Select.Error"), ex);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void setReadonly(boolean readonly) {
		super.setDisabled(readonly);
	}

	public void appendOption(String v, String t) {
		this.options.add(new OptionDataMeta(v, t));
	}

	public void appendOption(OptionDataMeta o) {
		this.options.add(o);
	}

}
