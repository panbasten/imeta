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
package com.panet.iform.forms.labelSelect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.BrMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.FontMeta;
import com.panet.iform.core.base.LabelDataMeta;
import com.panet.iform.core.base.LabelMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.core.base.SelectDataMeta;
import com.panet.iform.core.base.SelectMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 多选框功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class LabelSelectMeta extends ComplexFormMeta {
	public static final String LAYOUT_NORMAL = "normal";

	public static final String LAYOUT_COLUMN = "column";

	/**
	 * 根
	 */
	private SimpleFormMeta[] root;

	/**
	 * 标签
	 */
	private LabelMeta label;

	/**
	 * 选择框
	 */
	private SelectMeta select;

	/**
	 * 星号
	 */
	private FontMeta star;

	/**
	 * 按键
	 */
	private List<ButtonMeta> buttons;

	/**
	 * 布局
	 */
	private String layout;

	/**
	 * 选择框
	 * 
	 * @param id
	 *            字段ID
	 * @param label
	 *            标签名
	 * @param clazz
	 *            Select样式类型
	 * @param style
	 *            Select内嵌样式类型
	 * @param title
	 *            Select注释
	 * @param value
	 *            Select初始值
	 * @param validate
	 *            Select验证
	 * @param options
	 *            Select选项
	 */
	public LabelSelectMeta(String id, String label, String clazz,
			Map<String, String> style, String title, String value,
			ValidateForm validate, List<OptionDataMeta> options) {
		LabelDataMeta labelDataMeta = new LabelDataMeta(id + ".label", id
				+ ".label", label, id);

		SelectDataMeta selectDataMeta = new SelectDataMeta(id, id, null, null,
				value, options);

		String color = "#FFFFFF";
		if (validate != null && validate.isRequired()) {
			color = "red";
		}
		Map<String, String> starStyle = new HashMap<String, String>();
		starStyle.put("color", color);
		this.star = new FontMeta(new SimpleFormDataMeta(null, null, null,
				starStyle, null, "*"), false);

		init(labelDataMeta, selectDataMeta, validate);
	}

	/**
	 * 选择框 初始化
	 * 
	 * @param labelDataMeta
	 *            标签
	 * @param selectDataMeta
	 *            选择框
	 * @param validate
	 *            Select验证
	 */
	public LabelSelectMeta(LabelDataMeta labelDataMeta,
			SelectDataMeta selectDataMeta, ValidateForm validate) {
		init(labelDataMeta, selectDataMeta, validate);
	}

	/**
	 * 初始化
	 * 
	 * @param labelDataMeta
	 *            标签
	 * @param selectDataMeta
	 *            选择框
	 * @param validate
	 *            Select验证
	 */
	public void init(LabelDataMeta labelDataMeta,
			SelectDataMeta selectDataMeta, ValidateForm validate) {
		super.setMultiRoot(true);
		this.label = new LabelMeta(labelDataMeta, false);
		this.select = new SelectMeta(selectDataMeta, validate, false);
		this.buttons = new ArrayList<ButtonMeta>();

		this.layout = LAYOUT_NORMAL;

	}

	public boolean isHasEmpty() {
		return this.select.isHasEmpty();
	}

	public void setHasEmpty(boolean hasEmpty) {
		this.select.setHasEmpty(hasEmpty);
	}

	@Override
	public JSONArray getFormJa() throws ImetaFormException {
		int elementNum = 3;
		if (LAYOUT_COLUMN.equals(this.layout)) {
			elementNum++;
		}
		elementNum += this.buttons.size();
		this.root = new SimpleFormMeta[elementNum];
		super.setRoots(this.root);
		int index = 0;
		this.root[index++] = label;
		if (LAYOUT_COLUMN.equals(this.layout)) {
			this.root[index++] = new BrMeta(new NullSimpleFormDataMeta());
		}
		this.root[index++] = select;
		this.root[index++] = star;
		for (int i = 0; i < this.buttons.size(); i++) {
			this.root[index + i] = this.buttons.get(i);
		}
		return super.getFormJa();
	}

	/**
	 * 添加按键
	 * 
	 * @param btn
	 *            按键
	 */
	public void addButton(ButtonMeta btn) {
		this.buttons.add(btn);
	}

	/**
	 * 添加按键
	 * 
	 * @param btn
	 *            按键
	 */
	public void addButton(ButtonMeta[] btn) {
		if (btn != null) {
			for (ButtonMeta b : btn) {
				this.buttons.add(b);
			}
		}
	}

	@Override
	public void setWidth(int width) {
		if (LAYOUT_COLUMN.equals(this.layout)) {
			if (this.label != null) {
				this.label.setWidth(width);
			}
			if (this.select != null) {
				this.select.setWidth(width - getButtonWidth());
			}
			return;
		}
		if (this.isSingle()) {
			if (this.label != null) {
				this.label.setWidth((int) (width * 0.3));
			}
			if (this.select != null) {
				int inputWidth = (int) (width * 0.65);
				this.select.setWidth(inputWidth - getButtonWidth());
			}
		} else {
			if (this.label != null) {
				this.label.setWidth((int) (width * 0.4));
			}
			if (this.select != null) {
				int inputWidth = (int) (width * 0.55);
				this.select.setWidth(inputWidth - getButtonWidth());
			}
		}
	}

	public void appendOption(String v, String t) {
		this.select.appendOption(v, t);
	}
	public void appendOption(OptionDataMeta o) {
		this.select.appendOption(o);
	}

	public void setDefaultValue(String v) {
		this.select.setDefaultValue(v);
	}

	private int getButtonWidth() {
		int i = 2;
		for (ButtonMeta b : this.buttons) {
			i += b.getButtonWidth();
			i += 2;
		}
		return i;
	}

	public void setDisabled(boolean disabled) {
		this.select.setDisabled(disabled);
	}

	public void setReadonly(boolean readonly) {
		this.select.setReadonly(readonly);
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		if (LAYOUT_COLUMN.equals(layout)) {
			this.label
					.setLabelTextDirection(LabelMeta.LABEL_TEXT_DIRECTION_LEFT);
		}
		this.layout = layout;
	}

	public LabelMeta getLabel() {
		return label;
	}

	public void setLabel(LabelMeta label) {
		this.label = label;
	}

	public SelectMeta getSelect() {
		return select;
	}

	public void setSelect(SelectMeta select) {
		this.select = select;
	}

	public void setSize(int size) {
		this.select.setSize(size);
	}

	@Override
	public void addListener(String key, String value) {
		this.select.addListener(key, value);
	}
	
	public void setDisplay(boolean display) {
		this.select.setDisplay(display);
	}
}
