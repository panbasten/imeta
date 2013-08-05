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

import com.panet.imeta.core.Const;

/**
 * 网格设置功能
 * 
 *@author panwei
 *@version 1.0
 *@since 2009/06/22
 */
public class GridCellDataMeta implements Cloneable, GridCellDataMetaInterface {
	public static final String CELL_TYPE_HEADER = "header";
	public static final String CELL_TYPE_CHECKBOX = "checkbox";
	public static final String CELL_TYPE_INPUT = "input";
	public static final String CELL_TYPE_SELECT = "select";
	public static final String CELL_TYPE_PASSWORD = "password";
	public static final String CELL_TYPE_BUTTON = "button";

	/**
	 * 列表头部ID
	 */
	private String id;

	/**
	 * 列表头部文字
	 */
	private String text;

	/**
	 * 列表类型-默认为头部类型
	 */
	private String type = CELL_TYPE_HEADER;

	/**
	 * cellClickFn
	 */
	private String cellClickFn;
	
	private String src;

	/**
	 * extendsMap
	 */
	private Map<String, String> extendsMap;

	/**
	 * 是否缺省值
	 */
	private boolean disabled = false;

	/**
	 * 网格单元
	 * 
	 * @param text
	 *            输入文本
	 */
	public GridCellDataMeta(String text) {
		super();
		if (!StringUtils.isEmpty(text)) {
			this.text = text;
		}
	}

	/**
	 * 网格单元
	 * 
	 * @param id
	 *            字段ID
	 * @param text
	 *            输入文本
	 */
	public GridCellDataMeta(String id, String text) {
		this(text);
		if (!StringUtils.isEmpty(id)) {
			this.id = id;
		}
	}

	/**
	 * 包含内容
	 * 
	 * @param meta
	 * @throws ID
	 * @throws 类型
	 * @throws cellClickFn
	 * @throws extendsMap
	 * 
	 */
	/**
	 * 网格单元
	 * 
	 * @param id
	 *            字段id
	 * @param text
	 *            输入文本
	 * @param type
	 *            输入类型
	 */
	public GridCellDataMeta(String id, String text, String type) {
		this(id, text);
		if (StringUtils.isNotEmpty(type)) {
			this.type = type;
		}
	}

	/**
	 * 得到json
	 * 
	 * @return
	 */
	@Override
	public JSONObject getFormJo() {
		JSONObject jo = new JSONObject();
		if (StringUtils.isNotEmpty(this.id)) {
			jo.put("id", this.id);
		}

		if (StringUtils.isNotEmpty(this.cellClickFn)) {
			jo.put("cellClickFn", this.cellClickFn);
		}
		
		if (StringUtils.isNotEmpty(this.src)) {
			jo.put("src", this.src);
		}

		jo.put("text", Const.NVL(this.text, ""));
		if (!StringUtils.isEmpty(this.type)) {
			jo.put("type", this.type);
		}
		jo.put("disabled", this.disabled);
		if (this.extendsMap != null && this.extendsMap.size() > 0) {
			JSONObject ex = new JSONObject();
			String key;
			for (Iterator<String> it = this.extendsMap.keySet().iterator(); it
					.hasNext();) {
				key = it.next();
				ex.put(key, this.extendsMap.get(key));
			}
			jo.put("extendsMap", ex);
		}
		return jo;
	}

	/**
	 * 添加延长列表
	 * 
	 * @param k
	 *            对应元素
	 * @param v
	 *            元素对应值
	 */
	public void addExtendMap(String k, String v) {
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

	public Map<String, String> getExtendsMap() {
		return extendsMap;
	}

	public void setExtendsMap(Map<String, String> extendsMap) {
		this.extendsMap = extendsMap;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCellClickFn() {
		return cellClickFn;
	}

	public void setCellClickFn(String cellClickFn) {
		this.cellClickFn = cellClickFn;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	
}
