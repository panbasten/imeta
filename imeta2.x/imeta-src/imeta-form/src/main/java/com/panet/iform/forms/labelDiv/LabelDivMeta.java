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
package com.panet.iform.forms.labelDiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.panet.iform.core.ComplexFormMeta;
import com.panet.iform.core.SimpleFormDataMeta;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.LabelDataMeta;
import com.panet.iform.core.base.LabelMeta;
import com.panet.iform.exception.ImetaFormException;

/**
* 标签框设置功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class LabelDivMeta extends ComplexFormMeta {
	
	/**
	 * 根
	 */
	private SimpleFormMeta[] root;
	
	/**
	 * 标签
	 */
	private LabelMeta label;
	
	/**
	 * div类型
	 */
	private DivMeta div;
	
	/**
	 * 按键
	 */
	private List<ButtonMeta> buttons;

	/**
	 * 标签
	 * 
	 * @param id
	 *            字段ID
	 * @param label
	 *            标签名
	 * @param clazz
	 *            div样式类型
	 * @param style
	 *            div内嵌样式类型
	 */
	public LabelDivMeta(String id, String label, String[] clazz,
			Map<String, String> style) {
		LabelDataMeta labelDataMeta = new LabelDataMeta(id + ".label", id
				+ ".label", label, id);
		SimpleFormDataMeta divDataMeta = new SimpleFormDataMeta(id + "_div", id
				+ "_div", clazz, style, null, null);

		init(labelDataMeta, divDataMeta);
	}

	/**
	 * 标签 初始化
	 * 
	 * @param labelDataMeta
	 *            样式类型
	 * @param divDataMeta
	 *            div样式类型
	 * @param validate
	 *            验证
	 */
	public LabelDivMeta(LabelDataMeta labelDataMeta,
			SimpleFormDataMeta divDataMeta, ValidateForm validate) {
		init(labelDataMeta, divDataMeta);
	}

	/**
	 * 初始化
	 * 
	 * @param labelDataMeta
	 * 
	 * @param divDataMeta
	 */
	public void init(LabelDataMeta labelDataMeta, SimpleFormDataMeta divDataMeta) {
		super.setMultiRoot(true);
		this.label = new LabelMeta(labelDataMeta, false);
		this.div = new DivMeta(divDataMeta, false);
		this.buttons = new ArrayList<ButtonMeta>();

	}

	@Override
	public JSONArray getFormJa() throws ImetaFormException {
		int btnNum = this.buttons.size();
		this.root = new SimpleFormMeta[2 + btnNum];
		super.setRoots(this.root);
		this.root[0] = label;
		this.root[1] = div;
		for (int i = 0; i < btnNum; i++) {
			this.root[3 + i] = this.buttons.get(i);
		}
		return super.getFormJa();
	}

	/**
	 * 添加按键
	 * 
	 * @param btn
	 */
	public void addButton(ButtonMeta btn) {
		this.buttons.add(btn);
	}

	/**
	 * 添加按键
	 * 
	 * @param btn
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
		if (this.isSingle()) {
			if (this.label != null) {
				this.label.setWidth((int) (width * 0.3));
			}
			if (this.div != null) {
				int inputWidth = (int) (width * 0.65);
				this.div.setWidth(inputWidth - getButtonWidth());
			}
		} else {
			if (this.label != null) {
				this.label.setWidth((int) (width * 0.4));
			}
			if (this.div != null) {
				int inputWidth = (int) (width * 0.55);
				this.div.setWidth(inputWidth - getButtonWidth());
			}
		}
	}
	
	/**
	 * 得到按键宽度
	 * 
	 * @return
	 */
	private int getButtonWidth() {
		int i = 0;
		for (ButtonMeta b : this.buttons) {
			i += (b.getButtonWidth() * 1.1);
			i += 2;
		}
		return i;
	}

	public void setDisabled(boolean disabled) {
		this.div.setDisabled(disabled);
	}

	public void setReadonly(boolean readonly) {
		this.div.setReadonly(readonly);
	}
}
