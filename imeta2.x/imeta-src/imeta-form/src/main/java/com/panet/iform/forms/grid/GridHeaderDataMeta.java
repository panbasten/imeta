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
package com.panet.iform.forms.grid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.exception.ImetaFormException;

/**
 * 网格头部设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class GridHeaderDataMeta implements Cloneable {

	public static final String HEADER_TYPE_NORMAL = "normal";
	public static final String HEADER_TYPE_NUMBER = "number";
	public static final String HEADER_TYPE_CHECKBOX = "checkbox";
	public static final String HEADER_TYPE_INPUT = "input";
	public static final String HEADER_TYPE_SELECT = "select";

	/**
	 * 列表头部ID
	 */
	private String id;

	/**
	 * 列表头部文字
	 */
	private String text;

	/**
	 * 列表头部类型
	 */
	private String type = HEADER_TYPE_NORMAL;

	private String options;

	/**
	 * 列表头部是否有效
	 */
	private boolean enabled = false;

	/**
	 * 列表头部宽带
	 */
	private int width = 20;

	private Map<String, String> extendsMap;

	/**
	 * 网格头部框
	 * 
	 * @param id
	 *            得到的id
	 * @param text
	 *            输入文本
	 * @param type
	 *            输入类型
	 * @param enabled
	 *            操作标示
	 * @param width
	 *            宽度
	 */
	public GridHeaderDataMeta(String id, String text, String type,
			boolean enabled, int width) {
		super();
		if (!StringUtils.isEmpty(id))
			this.id = id;
		if (!StringUtils.isEmpty(text))
			this.text = text;
		if (!StringUtils.isEmpty(type))
			this.type = type;
		this.enabled = enabled;
		if (width > 50 && width < 1000)
			this.width = width;
		else
			this.width = 50;
	}

	/**
	 * 得到json
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getFormJo() throws ImetaFormException {
		JSONObject rtn = new JSONObject();
		if (!StringUtils.isEmpty(this.id))
			rtn.put("id", this.id);
		if (!StringUtils.isEmpty(this.text))
			rtn.put("text", this.text);
		if (!StringUtils.isEmpty(this.type))
			rtn.put("type", this.type);
		rtn.put("enabled", this.enabled);
		rtn.put("width", this.width);
		if (!StringUtils.isEmpty(this.options)) {
			rtn.put("options", options);
		}
		if (this.extendsMap != null && this.extendsMap.size() > 0) {
			JSONObject ex = new JSONObject();
			String key;
			for (Iterator<String> it = this.extendsMap.keySet().iterator(); it
					.hasNext();) {
				key = it.next();
				ex.put(key, this.extendsMap.get(key));
			}
			rtn.put("extendsMap", ex);
		}
		return rtn;
	}

	/**
	 * 添加延长列表
	 * 
	 * @param k
	 *            对应元素
	 * @param v
	 *            元素对应值
	 */
	public void addExtendsMap(String k, String v) {
		if (this.extendsMap == null) {
			this.extendsMap = new HashMap<String, String>();
		}
		this.extendsMap.put(k, v);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width > 50 && width < 500)
			this.width = width;
		else
			this.width = 50;
	}

	public Map<String, String> getExtendsMap() {
		return extendsMap;
	}

	public void setExtendsMap(Map<String, String> extendsMap) {
		this.extendsMap = extendsMap;
	}

	public String getOptions() {
		return options;
	}

	public GridHeaderDataMeta setOptions(String options) {
		this.options = options;
		return this;
	}
}
