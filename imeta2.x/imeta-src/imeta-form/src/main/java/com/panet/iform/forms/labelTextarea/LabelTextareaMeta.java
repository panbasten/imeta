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
 */package com.panet.iform.forms.labelTextarea;

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
import com.panet.iform.core.base.TextareaDataMeta;
import com.panet.iform.core.base.TextareaMeta;
import com.panet.iform.exception.ImetaFormException;

/**
* 文本框功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class LabelTextareaMeta extends ComplexFormMeta {
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
	 * 文本域
	 */
	private TextareaMeta textarea;

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
	 * 标示
	 */
	private boolean labelAfter = false;

	public boolean isLabelAfter() {
		return labelAfter;
	}

	public void setLabelAfter(boolean labelAfter) {
		this.labelAfter = labelAfter;
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

	/**
	 * 文本域
	 * 
	 * @param id
	 *            字段ID
	 * @param label
	 *            标签名
	 * @param clazz
	 *            Input样式类型
	 * @param style
	 *            Input内嵌样式类型
	 * @param title
	 *            Input注释
	 * @param html
	 *            Input初始值
	 * @param validate
	 *            Input验证
	 */
	public LabelTextareaMeta(String id, String label, String clazz,
			Map<String, String> style, String title, String html, int rows,
			ValidateForm validate) {
		LabelDataMeta labelDataMeta = new LabelDataMeta(id + ".label", id
				+ ".label", label, id);

		TextareaDataMeta textareaDataMeta = new TextareaDataMeta(id, id, null,
				null, title, html, rows);

		String color = "#FFFFFF";
		if (validate != null && validate.isRequired()) {
			color = "red";
		}
		Map<String, String> starStyle = new HashMap<String, String>();
		starStyle.put("color", color);
		this.star = new FontMeta(new SimpleFormDataMeta(null, null, null,
				starStyle, null, "*"), false);

		init(labelDataMeta, textareaDataMeta, validate);
	}


	/**
	 * 文本域 初始化
	 * 
	 * @param labelDataMeta
	 *            标签名
	 * @param textareaDataMeta
	 *            Input样式类型
	 * @param validate
	 *            Input验证
	 */
	public LabelTextareaMeta(LabelDataMeta labelDataMeta,
			TextareaDataMeta textareaDataMeta, ValidateForm validate) {
		init(labelDataMeta, textareaDataMeta, validate);
	}

	/**
	 * 初始化
	 * 
	 * @param labelDataMeta
	 *            标签名
	 * @param textareaDataMeta
	 *            Input样式类型
	 * @param validate
	 *            Input验证
	 */
	public void init(LabelDataMeta labelDataMeta,
			TextareaDataMeta textareaDataMeta, ValidateForm validate) {
		super.setMultiRoot(true);
		this.label = new LabelMeta(labelDataMeta, false);
		this.textarea = new TextareaMeta(textareaDataMeta, validate, false);
		this.buttons = new ArrayList<ButtonMeta>();

		this.layout = LAYOUT_NORMAL;
	}

	@Override
	public JSONArray getFormJa() throws ImetaFormException {
		int btnNum = this.buttons.size();
		int elementNum = 3 + btnNum;
		if (LAYOUT_COLUMN.equals(this.layout)) {
			elementNum++;
		}
		this.root = new SimpleFormMeta[elementNum];
		super.setRoots(this.root);
		int index = 0;
		if (this.labelAfter) {
			this.root[index++] = textarea;
			if (LAYOUT_COLUMN.equals(this.layout)) {
				this.root[index++] = new BrMeta(new NullSimpleFormDataMeta());
			}
			this.root[index++] = label;
		} else {
			this.root[index++] = label;
			if (LAYOUT_COLUMN.equals(this.layout)) {
				this.root[index++] = new BrMeta(new NullSimpleFormDataMeta());
			}
			this.root[index++] = textarea;
		}

		this.root[index++] = star;
		for (int i = 0; i < btnNum; i++) {
			this.root[index++] = this.buttons.get(i);
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
			if (this.labelAfter) {
				if (this.textarea != null) {
					this.textarea.setWidth(width);
				}
				if (this.label != null) {
					this.label.setWidth(width - getButtonWidth());
				}
			} else {
				if (this.label != null) {
					this.label.setWidth(width);
				}
				if (this.textarea != null) {
					this.textarea.setWidth(width - getButtonWidth());
				}
			}
			return;
		}

		double l, i;
		if (this.isSingle()) {
			l = 0.3;
			i = 0.65;
		} else {
			l = 0.4;
			i = 0.55;
		}

		if (this.labelAfter) {
			if (this.textarea != null) {
				this.textarea.setWidth((int) (width * i));
			}
			if (this.label != null) {
				int inputWidth = (int) (width * l);
				this.label.setWidth(inputWidth - getButtonWidth());
			}
		} else {
			if (this.label != null) {
				this.label.setWidth((int) (width * l));
			}
			if (this.textarea != null) {
				int inputWidth = (int) (width * i);
				this.textarea.setWidth(inputWidth - getButtonWidth());
			}
		}
	}

	/**
	 * 得到按键宽度
	 * 
	 * @return
	 */
	private int getButtonWidth() {
		int i = 2;
		for (ButtonMeta b : this.buttons) {
			i += b.getButtonWidth();
			i += 2;
		}
		return i;
	}

	public void setRoot(SimpleFormMeta[] root) {
		this.root = root;
	}

	public LabelMeta getLabel() {
		return label;
	}

	public void setLabel(LabelMeta label) {
		this.label = label;
	}

	public TextareaMeta getTextarea() {
		return textarea;
	}

	public void setTextarea(TextareaMeta textarea) {
		this.textarea = textarea;
	}

	public FontMeta getStar() {
		return star;
	}

	public void setStar(FontMeta star) {
		this.star = star;
	}

	public List<ButtonMeta> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonMeta> buttons) {
		this.buttons = buttons;
	}

	public void setDisabled(boolean disabled) {
		this.textarea.setDisabled(disabled);
	}

	public void setReadonly(boolean readonly) {
		this.textarea.setReadonly(readonly);
	}
}
