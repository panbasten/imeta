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
package com.panet.iform.forms.messageBox;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.exception.ImetaFormException;

/**
 * 消息功能
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class MessageBoxMeta {
	public static final String MESSAGEBOX_TYPE_CONFIRM = "confirm";
	public static final String MESSAGEBOX_TYPE_PROMPT = "prompt";
	public static final String MESSAGEBOX_TYPE_MULTIPROMPT = "multiprompt";
	public static final String MESSAGEBOX_TYPE_YNCDIALOG = "ync";
	public static final String MESSAGEBOX_TYPE_ALERT = "alert";
	public static final String MESSAGEBOX_TYPE_CUSTOM = "custom";

	public static final String MESSAGEBOX_ICON_ERROR = "error";
	public static final String MESSAGEBOX_ICON_INFO = "info";
	public static final String MESSAGEBOX_ICON_QUESTION = "question";
	public static final String MESSAGEBOX_ICON_WARNING = "warning";

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 题目
	 */
	private String title;

	/**
	 * 消息
	 */
	private String message;
	
	/**
	 * 默认值
	 */
	private String defaultValue;

	/**
	 * 按钮调用方法
	 */
	private String fn;

	/**
	 * 标注
	 */
	private boolean marded = true;

	private int oraTop = -1, oraLeft = -1, oraWidth = -1, oraHeight = -1;

	/**
	 * 消息框
	 * 
	 * @param id
	 *            字段ID
	 * @param type
	 *            Input样式类型
	 * @param icon
	 *            Input样式类型
	 * @param title
	 *            Input注释
	 * @param message
	 *            Input注释
	 * @param marded
	 *            标注
	 */
	public MessageBoxMeta(String id, String type, String icon, String title,
			String message, boolean marded) {
		super();
		this.id = id;
		this.type = type;
		this.icon = icon;
		this.title = title;
		this.message = message;
		this.marded = marded;
	}

	/**
	 * 消息框获得
	 * 
	 * @param message
	 */
	public MessageBoxMeta(String message) {
		this.message = message;
	}

	/**
	 * 得到json
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	public JSONObject getFormJo() throws ImetaFormException {
		JSONObject rtn = new JSONObject();
		rtn.put("messagebox", true);
		if (StringUtils.isNotEmpty(this.id)) {
			rtn.put("id", this.id);
		}
		if (StringUtils.isNotEmpty(this.type)) {
			rtn.put("type", this.type);
		}
		if (StringUtils.isNotEmpty(this.icon)) {
			rtn.put("icon", this.icon);
		}
		if (StringUtils.isNotEmpty(this.title)) {
			rtn.put("title", this.title);
		}
		if (StringUtils.isNotEmpty(this.message)) {
			rtn.put("message", this.message);
		}
		if (StringUtils.isNotEmpty(this.fn)) {
			rtn.put("fn", this.fn);
		}
		if (StringUtils.isNotEmpty(this.defaultValue)) {
			rtn.put("defaultValue", this.defaultValue);
		}
		rtn.put("marded", this.marded);

		if (this.oraTop != -1) {
			rtn.put("oraTop", this.oraTop);
		}
		if (this.oraLeft != -1) {
			rtn.put("oraLeft", this.oraLeft);
		}
		if (this.oraWidth != -1) {
			rtn.put("oraWidth", this.oraWidth);
		}
		if (this.oraHeight != -1) {
			rtn.put("oraHeight", this.oraHeight);
		}

		return rtn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isMarded() {
		return marded;
	}

	public void setMarded(boolean marded) {
		this.marded = marded;
	}

	public int getOraTop() {
		return oraTop;
	}

	public void setOraTop(int oraTop) {
		this.oraTop = oraTop;
	}

	public int getOraLeft() {
		return oraLeft;
	}

	public void setOraLeft(int oraLeft) {
		this.oraLeft = oraLeft;
	}

	public int getOraWidth() {
		return oraWidth;
	}

	public void setOraWidth(int oraWidth) {
		this.oraWidth = oraWidth;
	}

	public int getOraHeight() {
		return oraHeight;
	}

	public void setOraHeight(int oraHeight) {
		this.oraHeight = oraHeight;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
