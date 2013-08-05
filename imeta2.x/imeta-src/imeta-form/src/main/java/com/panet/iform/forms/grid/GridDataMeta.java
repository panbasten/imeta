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

import com.panet.iform.core.ComplexFormDataMeta;

/**
 * 网格数据设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class GridDataMeta extends ComplexFormDataMeta {

	/**
	 * 表头宽度
	 */
	private int widthPercent = -1;

	/**
	 * 表头高度
	 */
	private int height = -1;

	/**
	 * 首页
	 */
	private boolean pageFirst = false;

	/**
	 * 上页
	 */
	private boolean pagePrev = false;

	/**
	 * 下页
	 */
	private boolean pageNext = false;

	/**
	 * 末页
	 */
	private boolean pageLast = false;

	/**
	 * 页面索引
	 */
	private int pageIndex = 1;
	/**
	 * 页面号
	 */
	private int pageNum = 1;

	/**
	 * 下页面号
	 */
	private int pagePerNum = 20;

	/**
	 * 记录页面号
	 */
	private int recordNum = 0;

	/**
	 * 页面数
	 */
	private boolean hasPages = false;

	/**
	 * 已添加
	 */
	private boolean hasAdd = false;

	/**
	 * 添加
	 */
	private boolean add = false;

	/**
	 * 添加完成
	 */
	private String addFn;

	/**
	 * 已删除
	 */
	private boolean hasDelete = false;

	/**
	 * 删除
	 */
	private boolean delete = false;

	/**
	 * 删除完成
	 */
	private String deleteFn;

	/**
	 * 按钮栏
	 */
	private boolean hasBottomBar = false;
	/**
	 * 包含内容
	 * 
	 * @param meta
	 * @throws 标题
	 * @throws 高度
	 * @throws 记录数据
	 * @throws extendsMap
	 * @throws 页数
	 */
	/**
	 * 表格头部列表
	 */
	private List<GridHeaderDataMeta> header;

	/**
	 * 网格框
	 * 
	 * @param id
	 *            获得id
	 */
	public GridDataMeta(String id) {
		super(id);
		this.header = new ArrayList<GridHeaderDataMeta>();
	}

	/**
	 * 网格框
	 * 
	 * @param id
	 *            主键id
	 * @param height
	 *            高度输入
	 */
	public GridDataMeta(String id, int height) {
		this(id);
		this.height = height;
	}

	/**
	 * 网格框
	 * 
	 * @param id
	 *            取得的id
	 * @param height
	 *            高度
	 * @param recordNum
	 *            记录数
	 */
	public GridDataMeta(String id, int height, int recordNum) {
		this(id, height);
		this.recordNum = (recordNum < 0) ? 0 : recordNum;
		this.pageNum = this.recordNum / this.pagePerNum + 1;

	}

	/**
	 * 页面内容
	 * 
	 * @param meta
	 * @throws 首页
	 * @throws 上页
	 * @throws 下页
	 * @throws 末页
	 * @throws 页面记录
	 */
	public void setPage(boolean pageFirst, boolean pagePrev, boolean pageNext,
			boolean pageLast, int pageIndex) {
		this.pageFirst = pageFirst;
		this.pagePrev = pagePrev;
		this.pageNext = pageNext;
		this.pageLast = pageLast;
		this.pageIndex = pageIndex;

	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPagePerNum() {
		return pagePerNum;
	}

	public void setPagePerNum(int pagePerNum) {
		this.pagePerNum = pagePerNum;
	}

	public int getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}

	public void addHeader(GridHeaderDataMeta dataMeta) {
		this.header.add(dataMeta);
	}

	public void addHeaders(GridHeaderDataMeta[] dataMeta) {
		if (dataMeta != null && dataMeta.length > 0) {
			for (GridHeaderDataMeta d : dataMeta) {
				this.header.add(d);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<GridHeaderDataMeta> getHeader() {
		return header;
	}

	public void setHeader(List<GridHeaderDataMeta> header) {
		this.header = header;
	}

	public int getWidthPercent() {
		return widthPercent;
	}

	public void setWidthPercent(int widthPercent) {
		this.widthPercent = widthPercent;
	}

	public boolean isPageFirst() {
		return pageFirst;
	}

	public void setPageFirst(boolean pageFirst) {
		this.pageFirst = pageFirst;
	}

	public boolean isPagePrev() {
		return pagePrev;
	}

	public void setPagePrev(boolean pagePrev) {
		this.pagePrev = pagePrev;
	}

	public boolean isPageNext() {
		return pageNext;
	}

	public void setPageNext(boolean pageNext) {
		this.pageNext = pageNext;
	}

	public boolean isPageLast() {
		return pageLast;
	}

	public void setPageLast(boolean pageLast) {
		this.pageLast = pageLast;
	}

	public boolean isHasPages() {
		return hasPages;
	}

	public void setHasPages(boolean hasPages) {
		this.hasPages = hasPages;
	}

	public boolean isHasAdd() {
		return hasAdd;
	}

	public void setHasAdd(boolean hasAdd) {
		this.hasAdd = hasAdd;
	}

	public boolean isHasDelete() {
		return hasDelete;
	}

	public void setHasDelete(boolean hasDelete) {
		this.hasDelete = hasDelete;
	}

	public boolean isHasBottomBar() {
		return hasBottomBar;
	}

	public void setHasBottomBar(boolean hasBottomBar) {
		this.hasBottomBar = hasBottomBar;
	}

	public String getAddFn() {
		return addFn;
	}

	public void setAddFn(String addFn) {
		this.addFn = addFn;
	}

	public String getDeleteFn() {
		return deleteFn;
	}

	public void setDeleteFn(String deleteFn) {
		this.deleteFn = deleteFn;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}
