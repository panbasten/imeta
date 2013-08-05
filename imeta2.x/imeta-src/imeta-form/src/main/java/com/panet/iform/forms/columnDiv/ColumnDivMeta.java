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

package com.panet.iform.forms.columnDiv;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.panet.iform.Messages;
import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.PMeta;
import com.panet.iform.exception.ImetaFormException;


/**
* 文本栏功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ColumnDivMeta extends ComplexFormMeta {

	/**
	 * 域的根
	 */
	private DivMeta root;
	
	/**
	 * div内容
	 */
	private List<BaseFormMeta> divContent;
	
	/**
	 * 数据元
	 */
	private SimpleFormDataMeta dataMeta;
	

	public ColumnDivMeta() {
		this(new NullSimpleFormDataMeta());
	}
	
	/**
	 * 数据元ID
	 */
	public ColumnDivMeta(String id) {
		this(new SimpleFormDataMeta(id, id, null, null, null, null));
	}
	
	/**
	 * 生成文本栏
	 * 
	 * @param simpleFormDataMeta
	 */
	public ColumnDivMeta(SimpleFormDataMeta simpleFormDataMeta) {
		this.dataMeta = simpleFormDataMeta;
		this.root = new DivMeta(simpleFormDataMeta, false);
		super.setRoot(this.root);
		super.setMultiRoot(false);

		this.divContent = new ArrayList<BaseFormMeta>();
	}

	/**
	 * 
	 * @param meta
	 * @throws ImetaFormException
	 */
	public void putDivContent(BaseFormMeta[] meta) throws ImetaFormException {
		try {
			List<BaseFormMeta> l = this.divContent;
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

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (!this.isSingle()) {
			this.root.setStyle("float", "left");
		}
		createColumnFormContents();
		return this.root.getFormJo();
	}

	/**
	 * 生成添加内容
	 * 
	 */
	private void createColumnFormContents() {
		List<BaseFormMeta> meta = this.divContent;
		if (meta != null && meta.size() > 0) {
			PMeta p = null;
			int width = 96;
			int perWidth = width / this.getColumnNum();
			int culumn = 0;
			BaseFormMeta baseFormMeta;

			for (int i = 0; i < meta.size(); i++) {
				if (culumn == 0) {
					p = new PMeta(new NullSimpleFormDataMeta(), false);
					p.appendTo(this.root);
				}
				baseFormMeta = meta.get(i);
				if (baseFormMeta != null) {
					// 如果是单行
					if (meta.get(i).isSingle()) {
						// 第一个
						if (culumn == 0) {
							baseFormMeta.setWidth(99);
							DivMeta elementDiv = new DivMeta(new NullSimpleFormDataMeta(),true);
							elementDiv.setStyle("float", "left");
							elementDiv.setStyle("margin", "5px");
							elementDiv.setWidth(width);
							elementDiv.append(baseFormMeta);
							p.append(elementDiv);
						}
						// 不是第一个
						else {
							culumn = 0;
							i--;
						}
					}
					// 如果不是单行
					else {
						baseFormMeta.setWidth(99);
						DivMeta elementDiv = new DivMeta(new NullSimpleFormDataMeta(),true);
						elementDiv.setStyle("float", "left");
						elementDiv.setStyle("margin", "5px");
						elementDiv.setWidth(perWidth);
						elementDiv.append(baseFormMeta);
						p.append(elementDiv);
						culumn++;
					}
				}
				if (culumn >= this.getColumnNum()) {
					culumn = 0;
				}
			}
		}
	}

	public List<BaseFormMeta> getDivContent() {
		return divContent;
	}

	public void setDivContent(List<BaseFormMeta> divContent) {
		this.divContent = divContent;
	}

	public DivMeta getRoot() {
		return root;
	}

	public void setRoot(DivMeta root) {
		this.root = root;
	}

	public SimpleFormDataMeta getDataMeta() {
		return dataMeta;
	}

	public void setDataMeta(SimpleFormDataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}

}
