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

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 网格设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class GridMeta extends ComplexFormMeta {

	/**
	 * 数据元
	 */
	private GridDataMeta dataMeta = null;

	/**
	 * 列数据
	 */
	private List<GridRowDataMeta> rowMeta = null;

	/**
	 * 生成网格
	 * 
	 * @param id
	 *            取得的id
	 * @param height
	 *            高度
	 * @param recordNum
	 *            记录数值
	 * @param single
	 *            是否单行排列
	 */
	public GridMeta(String id, int height, int recordNum, boolean single) {
		this(new GridDataMeta(id, height, recordNum), single);
	}

	/**
	 * 添加生成数据元
	 * 
	 * 
	 * @param meta
	 * @throws 行数据
	 */
	/**
	 * 网格
	 * 
	 * @param dataMeta
	 *            GridDataMeta类型元素
	 * @param single
	 *            是否单行排列
	 */
	public GridMeta(GridDataMeta dataMeta, boolean single) {
		super(dataMeta);
		this.dataMeta = dataMeta;
		this.rowMeta = new ArrayList<GridRowDataMeta>();
		this.setSingle(single);
	}

	/**
	 * 设置按键框
	 * 
	 * @param hasBottomBar
	 *            按键框标示
	 */
	public void setHasBottomBar(boolean hasBottomBar) {
		this.dataMeta.setHasBottomBar(hasBottomBar);
	}

	/**
	 * 添加
	 * 
	 * @param hasAdd
	 *            是否添加标示
	 * @param disabled
	 *            默认
	 * @param fn
	 *            内容
	 */
	public void setHasAdd(boolean hasAdd, boolean disabled, String fn) {
		this.dataMeta.setHasAdd(hasAdd);
		this.dataMeta.setAdd(disabled);
		this.dataMeta.setAddFn(fn);
	}

	/**
	 * 删除
	 * 
	 * @param hasDelete
	 *            是否删除标示
	 * @param disabled
	 *            默认
	 * @param fn
	 *            内容
	 */
	public void setHasDelete(boolean hasDelete, boolean disabled, String fn) {
		this.dataMeta.setHasDelete(hasDelete);
		this.dataMeta.setDelete(disabled);
		this.dataMeta.setDeleteFn(fn);
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		JSONObject formJo = new JSONObject();
		JSONObject gridJo = new JSONObject();
		if (!StringUtils.isEmpty(dataMeta.getId()))
			gridJo.put("id", dataMeta.getId());
		if (dataMeta.getWidthPercent() != -1)
			gridJo.put("widthPercent", dataMeta.getWidthPercent());
		if (dataMeta.getHeight() != -1)
			gridJo.put("height", dataMeta.getHeight());
		gridJo.put("hasBottomBar", dataMeta.isHasBottomBar());

		gridJo.put("single", this.isSingle());

		if (dataMeta.isHasBottomBar()) {
			gridJo.put("hasAdd", dataMeta.isHasAdd());
			gridJo.put("addFn", dataMeta.getAddFn());
			gridJo.put("add", dataMeta.isAdd());
			gridJo.put("hasDelete", dataMeta.isHasDelete());
			gridJo.put("deleteFn", dataMeta.getDeleteFn());
			gridJo.put("del", dataMeta.isDelete());
			gridJo.put("hasPage", dataMeta.isHasPages());
			gridJo.put("pageFirst", dataMeta.isPageFirst());
			gridJo.put("pagePrev", dataMeta.isPagePrev());
			gridJo.put("pageNext", dataMeta.isPageNext());
			gridJo.put("pageLast", dataMeta.isPageLast());

			gridJo.put("pageIndex", dataMeta.getPageIndex());
			gridJo.put("pageNum", dataMeta.getPageNum());
			gridJo.put("pagePerNum", dataMeta.getPagePerNum());
			gridJo.put("recordNum",
					(dataMeta.getRecordNum() > rowMeta.size()) ? dataMeta
							.getRecordNum() : rowMeta.size());
		}

		JSONArray headerJa = new JSONArray();
		for (GridHeaderDataMeta hdm : dataMeta.getHeader()) {
			headerJa.add(hdm.getFormJo());
		}
		gridJo.put("header", headerJa);

		JSONArray rowJa = new JSONArray();
		for (GridRowDataMeta r : rowMeta) {
			rowJa.add(r.getFormJo());
		}
		gridJo.put("rows", rowJa);

		formJo.put("grid", gridJo);
		return formJo;
	}

	@Override
	public void setWidth(int width) {
		this.dataMeta.setWidthPercent(width);
	}

	public void addHeader(GridHeaderDataMeta dataMeta) {
		this.dataMeta.addHeader(dataMeta);
	}

	public void addHeaders(GridHeaderDataMeta[] dataMeta) {
		this.dataMeta.addHeaders(dataMeta);
	}

	public GridDataMeta getDataMeta() {
		return dataMeta;
	}

	public void setDataMeta(GridDataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}

	public List<GridRowDataMeta> getRowMeta() {
		return rowMeta;
	}

	public void setRowMeta(List<GridRowDataMeta> rowMeta) {
		this.rowMeta = rowMeta;
	}

	/**
	 * 添加行
	 * 
	 * @param dataMeta
	 *            内容
	 */
	public void addRow(String[] dataMeta, boolean display) {
		GridRowDataMeta row = new GridRowDataMeta(dataMeta.length);
		if (dataMeta != null && dataMeta.length > 0) {
			for (int i = 0; i < dataMeta.length; i++) {
				row.setCell(i, new GridCellDataMeta(dataMeta[i]));
			}
		}
		row.setDisplay(display);
		this.rowMeta.add(row);
	}

	public void addRow(String[] dataMeta) {
		addRow(dataMeta, true);
	}

	/**
	 * 添加行
	 * 
	 * @param dataMeta
	 *            GridCellDataMeta类型元素
	 */
	public void addRow(GridCellDataMeta[] dataMeta, boolean display) {
		GridRowDataMeta row = new GridRowDataMeta(dataMeta.length);
		if (dataMeta != null && dataMeta.length > 0) {
			for (int i = 0; i < dataMeta.length; i++) {
				row.setCell(i, dataMeta[i]);
			}
		}
		row.setDisplay(display);
		this.rowMeta.add(row);
	}

	public void addRow(GridCellDataMeta[] dataMeta) {
		addRow(dataMeta, true);
	}

	/**
	 * 添加行
	 * 
	 * @param dataMeta
	 *            内容
	 */
	public void addRow(Object[] dataMeta, boolean display) {
		GridRowDataMeta row = new GridRowDataMeta(dataMeta.length);
		if (dataMeta != null && dataMeta.length > 0) {
			for (int i = 0; i < dataMeta.length; i++) {
				if (dataMeta[i] instanceof String) {
					row.setCell(i, new GridCellDataMeta((String) dataMeta[i]));
				} else if (dataMeta[i] instanceof GridCellDataMetaInterface) {
					row.setCell(i, (GridCellDataMetaInterface) dataMeta[i]);
				} else {
					row
							.setCell(i, new GridCellDataMeta(dataMeta[i]
									.toString()));
				}

			}
		}
		row.setDisplay(display);
		this.rowMeta.add(row);
	}

	public void addRow(Object[] dataMeta) {
		addRow(dataMeta, true);
	}

	/**
	 * 添加行
	 * 
	 * @param row
	 *            GridRowDataMeta类型元素
	 */
	public void addRow(GridRowDataMeta row) {
		this.rowMeta.add(row);
	}

}
