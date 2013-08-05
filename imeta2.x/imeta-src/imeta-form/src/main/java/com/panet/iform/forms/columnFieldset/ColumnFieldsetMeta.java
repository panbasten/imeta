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
package com.panet.iform.forms.columnFieldset;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.Messages;
import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.FieldsetMeta;
import com.panet.iform.core.base.LegendMeta;
import com.panet.iform.exception.ImetaFormException;

/**
* 文本栏设置功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class ColumnFieldsetMeta extends ComplexFormMeta {

	/**
	 * 域的根
	 */
	private DivMeta root;

	/**
	 * 域
	 */
	private FieldsetMeta field;

	/**
	 * 标签
	 */
	private LegendMeta label;
	
	/**
	 * 文件内容列表
	 */
	private List<BaseFormMeta> fieldsetsContent;

	/**
	 * 框架生成
	 * 
	 * @param columnFieldsetDataMeta
	 *                        列表表单
	 */
	public ColumnFieldsetMeta(ColumnFieldsetDataMeta columnFieldsetDataMeta) {
		super(columnFieldsetDataMeta);
		String id = columnFieldsetDataMeta.getId();
		String legends = columnFieldsetDataMeta.getLegends();
		this.root = new DivMeta(new SimpleFormDataMeta(id, id, null, null,
				null, null), false);
		super.setRoot(this.root);
		super.setMultiRoot(false);

		this.field = new FieldsetMeta(new SimpleFormDataMeta(id + "_con", id
				+ "_con", null, null, null, null), false);
		this.field.appendTo(this.root);

		this.fieldsetsContent = new ArrayList<BaseFormMeta>();
		if (!StringUtils.isEmpty(legends)) {
			this.label = new LegendMeta(new SimpleFormDataMeta(null, null,
					null, null, null, legends), false);
			this.label.appendTo(this.field);
		}
	}

	/**
	 * 框架生成
	 * 
	 * @param id
	 *          列表表单的ID
	 * @param legends
	 *               列表表单的图例
	 */
	public ColumnFieldsetMeta(String id, String legends) {
		this(new ColumnFieldsetDataMeta(id, legends));
	}

	/**
	 * 添加内容
	 * 
	 * @param meta
	 *            
	 * @throws ImetaFormException
	 *                             
	 */
	public void putFieldsetsContent(BaseFormMeta[] meta)
			throws ImetaFormException {
		try {
			if (meta != null && meta.length > 0) {
				for (BaseFormMeta baseFormMeta : meta) {
					this.fieldsetsContent.add(baseFormMeta);
				}
			}

		} catch (Exception ex) {
			throw new ImetaFormException(Messages
					.getString("IForm.CreateJSON.ColumnForm.Error"), ex);
		}
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		createColumnFormContents();
		return this.root.getFormJo();
	}

	/**
	 * 生成添加内容
	 * 
	 */
	private void createColumnFormContents() {
		List<BaseFormMeta> meta = this.fieldsetsContent;
		if (meta != null && meta.size() > 0) {
			DivMeta p = null;
			int width = 99;
			int perWidth = width / this.getColumnNum();
			int culumn = 0;
			BaseFormMeta baseFormMeta;

			for (int i = 0; i < this.fieldsetsContent.size(); i++) {
				if (culumn == 0) {
					p = new DivMeta(new NullSimpleFormDataMeta(), false);
					p.appendTo(this.field);
				}
				baseFormMeta = meta.get(i);
				if (baseFormMeta != null) {
					// 如果是单行
					if (meta.get(i).isSingle()) {
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
		}
	}

	public List<BaseFormMeta> getFieldsetsContent() {
		return fieldsetsContent;
	}

	public void setFieldsetsContent(List<BaseFormMeta> fieldsetsContent) {
		this.fieldsetsContent = fieldsetsContent;
	}

	public DivMeta getRoot() {
		return root;
	}

	public void setRoot(DivMeta root) {
		this.root = root;
	}

	public FieldsetMeta getField() {
		return field;
	}

	public void setField(FieldsetMeta field) {
		this.field = field;
	}

	public LegendMeta getLabel() {
		return label;
	}

	public void setLabel(LegendMeta label) {
		this.label = label;
	}

}
