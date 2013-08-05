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
package com.panet.iform.forms.labelGrid;

import net.sf.json.JSONArray;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.LabelDataMeta;
import com.panet.iform.exception.ImetaFormException;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;

/**
 * 标签形网格设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class LabelGridMeta extends ComplexFormMeta {

	/**
	 * 根
	 */
	private BaseFormMeta[] root;

	/**
	 * 标签
	 */
	private DivMeta label;

	/**
	 * 网格
	 */
	private GridMeta grid;

	/**
	 * 初始化
	 * 
	 * @param id
	 *            字段ID
	 * @param label
	 *            标签名
	 * @param height
	 *            Grid的高度
	 * @param recordNum
	 *            记录条数
	 */
	public LabelGridMeta(String id, String label, int height) {
		SimpleFormDataMeta dataMeta = new SimpleFormDataMeta(id + ".label", id
				+ ".label", null, null, null, label);

		GridDataMeta gridDataMeta = new GridDataMeta(id, height);

		init(dataMeta, gridDataMeta);
	}

	/**
	 * 标签网格
	 * 
	 * @param id
	 *            字段ID
	 * @param label
	 *            标签名
	 * @param height
	 *            Grid的高度
	 * @param recordNum
	 *            记录条数
	 */
	public LabelGridMeta(String id, String label, int height, int recordNum) {
		SimpleFormDataMeta dataMeta = new SimpleFormDataMeta(id + ".label", id
				+ ".label", null, null, null, label);

		GridDataMeta gridDataMeta = new GridDataMeta(id, height, recordNum);

		init(dataMeta, gridDataMeta);
	}

	/**
	 * 标签 初始化
	 * 
	 * @param labelDataMeta
	 *            标签
	 * @param gridDataMeta
	 *            网格
	 */
	public LabelGridMeta(LabelDataMeta labelDataMeta, GridDataMeta gridDataMeta) {
		init(labelDataMeta, gridDataMeta);
	}

	/**
	 * 初始化
	 * 
	 * @param dataMeta
	 *            数据
	 * @param gridDataMeta
	 *            网格
	 */
	public void init(SimpleFormDataMeta dataMeta, GridDataMeta gridDataMeta) {
		super.setMultiRoot(true);
		this.label = new DivMeta(dataMeta, false);
		this.grid = new GridMeta(gridDataMeta, false);

		this.label.setStyle("font-weight", "bold");
	}

	@Override
	public JSONArray getFormJa() throws ImetaFormException {
		this.root = new BaseFormMeta[2];
		this.root[0] = label;
		this.root[1] = this.grid;
		super.setRoots(this.root);
		return super.getFormJa();
	}

	@Override
	public void setWidth(int width) {

		if (this.label != null) {
			this.label.setWidth(width);
		}
		if (this.grid != null) {
			this.grid.setWidth(width);
		}
	}

	public DivMeta getLabel() {
		return label;
	}

	public void setLabel(DivMeta label) {
		this.label = label;
	}

	public GridMeta getGrid() {
		return grid;
	}

	public void setGrid(GridMeta grid) {
		this.grid = grid;
	}

	public void addHeader(GridHeaderDataMeta dataMeta) {
		this.grid.addHeader(dataMeta);
	}

	public void addHeaders(GridHeaderDataMeta[] dataMeta) {
		this.grid.addHeaders(dataMeta);
	}

	public void addRow(String[] dataMeta) {
		this.grid.addRow(dataMeta, true);
	}

	public void addRow(String[] dataMeta, boolean display) {
		this.grid.addRow(dataMeta, display);
	}

	public void addRow(GridCellDataMeta[] dataMeta) {
		this.grid.addRow(dataMeta, true);
	}

	public void addRow(GridCellDataMeta[] dataMeta, boolean display) {
		this.grid.addRow(dataMeta, display);
	}

	public void addRow(Object[] dataMeta) {
		this.grid.addRow(dataMeta, true);
	}

	public void addRow(Object[] dataMeta, boolean display) {
		this.grid.addRow(dataMeta, display);
	}

	public void addRow(GridRowDataMeta dataMeta) {
		this.grid.addRow(dataMeta);
	}

	public void setHasBottomBar(boolean hasBottomBar) {
		this.grid.setHasBottomBar(hasBottomBar);
	}

	public void setHasAdd(boolean hasAdd, boolean disabled, String fn) {
		this.grid.setHasAdd(hasAdd, disabled, fn);
	}

	public void setHasDelete(boolean hasDelete, boolean disabled, String fn) {
		this.grid.setHasDelete(hasDelete, disabled, fn);
	}
	
	public void setSingle(boolean single) {
		super.setSingle(single);
		this.label.setSingle(single);
		this.grid.setSingle(single);
	}
}
