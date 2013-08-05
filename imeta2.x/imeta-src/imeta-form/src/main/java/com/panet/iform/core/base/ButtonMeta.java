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
package com.panet.iform.core.base;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.FormLoader;
import com.panet.iform.core.SimpleFormMeta;
import com.panet.iform.exception.ImetaFormException;

/**
 * 按键功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class ButtonMeta extends SimpleFormMeta {

	public static final String BUTTON_TYPE_INPUT = "input";
	public static final String BUTTON_TYPE_BUTTON = "button";

	/**
	 * 按键文本
	 */
	private String buttonText;

	/**
	 * 按键类型
	 */
	private String buttonType;

	/**
	 * 按键样式类型
	 */
	private int buttonWidthStyle = -1;

	/**
	 * 按键功能
	 * 
	 * @param id
	 *            按键ID
	 * @param name
	 *            按键名称
	 * @param text
	 *            按键文本
	 * @param title
	 *            按键注释
	 */
	public ButtonMeta(String id, String name, String text, String title) {
		super(new InputDataMeta(id, name, null, null, title, text,
				InputDataMeta.INPUT_TYPE_BUTTON), SimpleFormMeta.INPUT_TAG,
				false);
		this.buttonType = BUTTON_TYPE_INPUT;
		this.buttonText = text;
	}

	/**
	 * 初始化
	 * 
	 * @param inputDataMeta
	 *            按键样式类型
	 */
	public ButtonMeta(InputDataMeta inputDataMeta) {
		super(inputDataMeta, SimpleFormMeta.INPUT_TAG, false);
		this.buttonType = BUTTON_TYPE_INPUT;
		this.buttonText = inputDataMeta.getValue();
	}

	/**
	 * 初始化
	 * 
	 * @param buttonDataMeta
	 *            按键类型
	 */
	public ButtonMeta(ButtonDataMeta buttonDataMeta) {
		super(buttonDataMeta, SimpleFormMeta.BUTTON_TAG, false);
		this.buttonType = BUTTON_TYPE_BUTTON;
		this.buttonText = buttonDataMeta.getText();
	}

	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		if (super.getSimpleFormDataMeta() != null) {
			if (BUTTON_TYPE_INPUT.equals(this.buttonType)) {
				InputDataMeta dataMeta = (InputDataMeta) super
						.getSimpleFormDataMeta();
				if (this.buttonWidthStyle != -1) {
					dataMeta.setStyle("width", this.buttonWidthStyle + "%");
				}

				if (!StringUtils.isEmpty(dataMeta.getValue())
						&& !StringUtils.isEmpty(dataMeta.getId())) {
					super.setInit("$('[id="
							+ dataMeta.getId()
							+ "]').val(\""
							+ FormLoader
									.replaceJavascriptString(this.buttonText)
							+ "\");");
				}
			} else if (BUTTON_TYPE_BUTTON.equals(this.buttonType)) {

			}
			this.addClass("i-button");
			return super.getFormJo();
		}

		return null;
	}

	public String getButtonType() {
		return buttonType;
	}

	/**
	 * 得到按键的宽度
	 * 
	 * @return int类型按键宽度
	 */
	public int getButtonWidth() {
		int w = 0;
		if (BUTTON_TYPE_INPUT.equals(this.buttonType)) {
			InputDataMeta dataMeta = (InputDataMeta) super
					.getSimpleFormDataMeta();
			w = getLength(dataMeta.getValue());
		}
		return w;
	}

	public void setButtonWidthStyle(int buttonWidthClass) {
		this.buttonWidthStyle = buttonWidthClass;
	}

	private int getLength(String str) {
		if (StringUtils.isEmpty(str)) {
			return 0;
		} else {
			return str.getBytes().length;
		}
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

}
