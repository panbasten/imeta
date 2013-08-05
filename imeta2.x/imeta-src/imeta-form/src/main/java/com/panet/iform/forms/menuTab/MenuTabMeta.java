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
package com.panet.iform.forms.menuTab;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.panet.iform.Messages;
import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.LiMeta;
import com.panet.iform.core.base.UlMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 菜单表设置功能
 * 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class MenuTabMeta extends ComplexFormMeta {

	public static final String TAB_ROOT_CLS = "iformtab_div";

	/**
	 * Tab根目录
	 */
	private DivMeta root;

	/**
	 * Tab头部信息
	 */
	private UlMeta tabHead;

	/**
	 * Tab头部标签
	 */
	private List<LiMeta> tabHeadTags;

	/**
	 * Tab标签目录
	 */
	private List<DivMeta> tabTagContents;

	/**
	 * Tab目录
	 */
	private List<List<BaseFormMeta>> tabContents;

	/**
	 * 初始化标签
	 * 
	 * @param id
	 *            Tab的ID
	 * @param tabHeads
	 *            Tab的头部信息
	 */
	public MenuTabMeta(String id, String[] tabHeads) {
		tabHeadTags = new ArrayList<LiMeta>();
		tabTagContents = new ArrayList<DivMeta>();
		tabContents = new ArrayList<List<BaseFormMeta>>();

		// 生成根
		this.root = new DivMeta(new SimpleFormDataMeta(null, null,
				new String[] { TAB_ROOT_CLS }, null, null, null), false);
		super.setRoot(this.root);

		// 生成标签头部
		this.tabHead = new UlMeta(new SimpleFormDataMeta(id + "_itab", null,
				null, null, null, null), false);
		this.tabHead.appendTo(this.root);
		this.tabHead.setInit("$('[id=" + id + "_itab]').iformtab();");

		// 生成标签头部详细
		int tabIndex = 0;
		for (String tabHead : tabHeads) {
			LiMeta tabHeadLi = new LiMeta(new SimpleFormDataMeta(id + ".itab."
					+ tabIndex, id + ".itab." + tabIndex, null, null, null,
					tabHead), false);
			this.tabHeadTags.add(tabHeadLi);
			tabHeadLi.appendTo(this.tabHead);
			tabHeadLi.putExtendFormData("type", "menu");

			// 生成标签头部对应内容
			SimpleFormMeta q = new SimpleFormMeta(null, "<q></q>", false);
			q.toString();
			q.appendTo(tabHeadLi);
			DivMeta tabContent = new DivMeta(new SimpleFormDataMeta(id
					+ "-itabc-" + (tabIndex++), null, null, null, null, null),
					false);
			this.tabTagContents.add(tabContent);
			this.tabContents.add(new ArrayList<BaseFormMeta>());
			q.getContent().add(tabContent);
		}

	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (this.tabHeadTags != null) {

			List<BaseFormMeta> c;
			for (int i = 0; i < this.tabHeadTags.size(); i++) {
				c = this.tabContents.get(i);
				createTabContent(i, c);
			}
		}

		if (this.root != null) {
			return this.root.getFormJo();
		}
		return null;
	}

	public DivMeta getRoot() {
		return root;
	}

	public void setRoot(DivMeta root) {
		this.root = root;
	}

	public UlMeta getTabHead() {
		return tabHead;
	}

	public void setTabHead(UlMeta tabHead) {
		this.tabHead = tabHead;
	}

	public List<LiMeta> getTabHeadTags() {
		return tabHeadTags;
	}

	public void setTabHeadTags(List<LiMeta> tabHeadTags) {
		this.tabHeadTags = tabHeadTags;
	}

	public List<DivMeta> getTabTagContents() {
		return tabTagContents;
	}

	public void setTabTagContents(List<DivMeta> tabTagContents) {
		this.tabTagContents = tabTagContents;
	}

	/**
	 * 添加的标签内容
	 * 
	 * @param tabIndex Tab索引
	 * @param meta
	 * @throws ImetaFormException 
	 */
	public void putTabContent(int tabIndex, BaseFormMeta meta)
			throws ImetaFormException {
		putTabContent(tabIndex, new BaseFormMeta[] { meta });
	}

	/**
	 * 添加的标签内容
	 * 
	 * @param tabIndex
	 * @param meta
	 * @throws ImetaFormException
	 */
	public void putTabContent(int tabIndex, BaseFormMeta[] meta)
			throws ImetaFormException {
		try {
			List<BaseFormMeta> l = this.tabContents.get(tabIndex);
			if (meta != null && meta.length > 0) {
				for (BaseFormMeta baseFormMeta : meta) {
					l.add(baseFormMeta);
				}
			}

		} catch (Exception ex) {
			throw new ImetaFormException(Messages
					.getString("IForm.CreateJSON.ColumnForm.Error"), ex);
		}
	}

	/**
	 * 生成添加的标签内容
	 * 
	 * @param tabIndex
	 * @param meta
	 */
	private void createTabContent(int tabIndex, List<BaseFormMeta> meta) {
		int maxTab = this.tabTagContents.size();
		if (maxTab > 0) {
			if (tabIndex < 0)
				tabIndex = 0;
			if (tabIndex > maxTab - 1)
				tabIndex = maxTab - 1;
		}
		DivMeta divMeta = this.tabTagContents.get(tabIndex);
		if (meta != null && meta.size() > 0) {
			divMeta.removeAll();
			DivMeta p = null;
			int width = 99;
			int perWidth = width / this.getColumnNum();
			int culumn = 0;
			BaseFormMeta baseFormMeta;

			for (int i = 0; i < meta.size(); i++) {
				if (culumn == 0) {
					p = new DivMeta(new NullSimpleFormDataMeta(), false);
					//p.setWidth(width);
					p.appendTo(divMeta);
					p.addClass("clearfix");
				}
				baseFormMeta = meta.get(i);
				if (baseFormMeta != null) {
					// 如果是单行
					if (baseFormMeta.isSingle()) {
						// 第一个
						if (culumn == 0) {
							baseFormMeta.setWidth(width);
							p.append(baseFormMeta);
						}
						// 不是第一个
						else {
							culumn = 0;
							i--;
						}

					}
					// 如果不是单行
					else {
						baseFormMeta.setWidth(perWidth);
						p.append(baseFormMeta);
						culumn++;
					}

				}
				if (culumn >= this.getColumnNum()) {
					culumn = 0;
				}
			}

		} else {
			divMeta.getSimpleFormDataMeta().setHtml("");
		}
	}

}
