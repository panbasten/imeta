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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.imeta.core.Const;

/**
 * 网格行数据设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class GridRowDataMeta implements Cloneable {

	/**
	 * 行点击事件触发的方法
	 */
	private String rowClickFn;

	private boolean display = true;

	private String style = "";

	private String cls = "";

	/**
	 * extendsMap
	 */
	private Map<String, String> extendsMap;

	/**
	 * cellMetas
	 */
	private GridCellDataMetaInterface[] cellMetas;

	public void putStyle(String key, String value) {
		if (!Const.isEmpty(key) && !Const.isEmpty(value)) {
			style = Const.NVL(style, "") + key + ":" + value + ";";
		}
	}

	/**
	 * 生成添加内容
	 * 
	 * @param rowClickFn
	 * @param meta
	 */
	/**
	 * 得到json
	 * 
	 * @return
	 */
	public JSONObject getFormJo() {
		JSONObject jo = new JSONObject();
		if (StringUtils.isNotEmpty(this.rowClickFn)) {
			jo.put("rowClickFn", this.rowClickFn);
		}
		if (StringUtils.isNotEmpty(this.style)) {
			jo.put("style", this.style);
		}
		if (StringUtils.isNotEmpty(this.cls)) {
			jo.put("clazz", this.cls);
		}
		jo.put("rowDisplay", this.display);
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
		if (this.cellMetas != null && this.cellMetas.length > 0) {
			JSONArray cell = new JSONArray();
			for (GridCellDataMetaInterface c : this.cellMetas) {
				cell.add(c.getFormJo());
			}
			jo.put("cell", cell);
		}
		return jo;
	}

	public String getRowClickFn() {
		return rowClickFn;
	}

	public void setCell(int n, GridCellDataMetaInterface cell) {
		this.cellMetas[n] = cell;
	}

	public void setCell(int n, String cell) {
		this.cellMetas[n] = new GridCellDataMeta(cell);
	}

	public void addExtendMap(String k, String v) {
		if (this.extendsMap == null) {
			this.extendsMap = new HashMap<String, String>();
		}
		this.extendsMap.put(k, v);
	}

	public GridRowDataMeta(int rowNum) {
		this.cellMetas = new GridCellDataMeta[rowNum];
	}

	public void setRowClickFn(String rowClickFn) {
		this.rowClickFn = rowClickFn;
	}

	public GridCellDataMetaInterface[] getCellMetas() {
		return cellMetas;
	}

	public void setCellMetas(GridCellDataMeta[] cellMetas) {
		this.cellMetas = cellMetas;
	}

	public Map<String, String> getExtendsMap() {
		return extendsMap;
	}

	public void setExtendsMap(Map<String, String> extendsMap) {
		this.extendsMap = extendsMap;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}
}
