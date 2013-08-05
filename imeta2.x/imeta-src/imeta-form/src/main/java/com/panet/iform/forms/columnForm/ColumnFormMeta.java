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
package com.panet.iform.forms.columnForm;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.Messages;
import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.FieldsetMeta;
import com.panet.iform.core.base.FormDataMeta;
import com.panet.iform.core.base.FormMeta;
import com.panet.iform.core.base.LegendMeta;
import com.panet.iform.core.base.PMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 文本栏表设置功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class ColumnFormMeta extends ComplexFormMeta {

	/**
	 * 列表的表单的根
	 */
	private FormMeta root;

	/**
	 * 列表表单的域
	 */
	private FieldsetMeta fieldset = null;
	
	private FormDataMeta formDataMeta;

	/**
	 * 列表表单域的内容
	 */
	private List<BaseFormMeta> fieldsetsContent = null;

	/**
	 * 列表表单域底部的按钮
	 */
	private List<ButtonMeta> formFootBtn;

	ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 框架表单
	 * 
	 * @param columnFormDataMeta
	 *            columnFormDataMeta样式类型
	 */
	public ColumnFormMeta(ColumnFormDataMeta columnFormDataMeta) {
		super(columnFormDataMeta);
		this.columnFormDataMeta = columnFormDataMeta;
		this.formDataMeta = new FormDataMeta(
				columnFormDataMeta.getId(), columnFormDataMeta.getId(),
				new String[] { "cmxform" }, null, null, null, null, null);
		this.root = new FormMeta(this.formDataMeta, false);
		this.formFootBtn = new ArrayList<ButtonMeta>();
		super.setRoot(this.root);
		super.setMultiRoot(false);

		String legends = columnFormDataMeta.getLegends();
		FieldsetMeta fieldset = new FieldsetMeta(new NullSimpleFormDataMeta(),
				false);
		fieldset.appendTo(this.root);
		this.fieldset = fieldset;
		this.fieldsetsContent = new ArrayList<BaseFormMeta>();
		if (!StringUtils.isEmpty(legends)) {
			LegendMeta label = new LegendMeta(new SimpleFormDataMeta(null,
					null, null, null, null, legends), false);
			label.appendTo(fieldset);
		}
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {

		if (this.fieldset != null) {
			// 加入内容层
			createColumnFormContents();

			// 加入错误提示层
			createColumnFormErrors();

			// 加入底部按钮
			createColumnFormFootBtn();
		}

		if (this.root != null) {
			if (this.columnFormDataMeta != null) {
				String id = this.columnFormDataMeta.getId();
				if (!StringUtils.isEmpty(id)) {
					this.root.setInit("$('#" + id
							+ "').validate({errorLabelContainer:$('#" + id
							+ " div.error')});");
				}
			}

			return this.root.getFormJo();
		}
		return null;
	}

	/**
	 * 添加表单底部按钮
	 * 
	 * @param meta
	 * @throws ImetaFormException
	 */
	public void putFieldsetsFootButtons(ButtonMeta[] meta)
			throws ImetaFormException {
		try {
			List<ButtonMeta> l = this.formFootBtn;
			if (meta != null && meta.length > 0) {
				for (ButtonMeta inputMeta : meta) {
					l.add(inputMeta);
				}
			}

		} catch (Exception ex) {
			throw new ImetaFormException(Messages
					.getString("IForm.CreateJSON.ColumnForm.Error"), ex);
		}
	}

	/**
	 * 添加内容
	 * 
	 * @param meta
	 * @throws ImetaFormException
	 */
	public void putFieldsetsContent(BaseFormMeta[] meta)
			throws ImetaFormException {
		try {
			List<BaseFormMeta> l = this.fieldsetsContent;
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
	 * 生成错误区
	 * 
	 */
	private void createColumnFormErrors() {
		String id = this.getDataMeta().getId();
		DivMeta div = new DivMeta(new SimpleFormDataMeta(id + "_error", id
				+ "_error", new String[] { "error" }, null, null, null), false);

		this.fieldset.append(div);
	}

	/**
	 * 生成底部按钮
	 * 
	 */
	private void createColumnFormFootBtn() {
		if (this.formFootBtn != null && this.formFootBtn.size() > 0) {
			String id = this.getDataMeta().getId();
			PMeta p = new PMeta(new SimpleFormDataMeta(id + "_btn",
					id + "_btn", new String[] { "btn" }, null, null, null),
					false);
			p.appendTo(this.fieldset);
			for (int i = 0; i < this.formFootBtn.size(); i++) {
				p.append(this.formFootBtn.get(i));
			}
		}
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
					p.appendTo(this.fieldset);
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
		}
	}

	public FormMeta getRoot() {
		return root;
	}

	public void setRoot(FormMeta root) {
		this.root = root;
	}

	public FieldsetMeta getFieldset() {
		return fieldset;
	}

	public void setFieldset(FieldsetMeta fieldset) {
		this.fieldset = fieldset;
	}

	public List<BaseFormMeta> getFieldsetsContent() {
		return fieldsetsContent;
	}

	public void setFieldsetsContent(List<BaseFormMeta> fieldsetsContent) {
		this.fieldsetsContent = fieldsetsContent;
	}

	public List<ButtonMeta> getFormFootBtn() {
		return formFootBtn;
	}

	public void setFormFootBtn(List<ButtonMeta> formFootBtn) {
		this.formFootBtn = formFootBtn;
	}

	public void putProperty(String key, String value) {
		this.formDataMeta.putProperty(key, value);
	}

}
