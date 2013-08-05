package com.plywet.imeta.platform.view;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class DesktopAppBean {

	public static final String DIALOG_ATTRIBUTE_DRAGGABLE = "draggable";
	public static final String DIALOG_ATTRIBUTE_RESIZABLE = "resizable";
	public static final String DIALOG_ATTRIBUTE_MODAL = "modal";
	public static final String DIALOG_ATTRIBUTE_WIDTH = "width";
	public static final String DIALOG_ATTRIBUTE_HEIGHT = "height";
	public static final String DIALOG_ATTRIBUTE_MIN_WIDTH = "minWidth";
	public static final String DIALOG_ATTRIBUTE_MIN_HEIGHT = "minHeight";
	public static final String DIALOG_ATTRIBUTE_CLOSABLE = "closable";
	public static final String DIALOG_ATTRIBUTE_MINIMIZABLE = "minimizable";
	public static final String DIALOG_ATTRIBUTE_MAXIMIZABLE = "maximizable";

	// 应用ID
	private int appId;

	// 应用类型（app-应用，dir-目录）
	private String appType = "app";

	// 应用标题
	private String appTitle;

	// 应用说明
	private String appDesc;

	// 图片路径
	private String iconSrc;

	// 是否可以移除
	private boolean removable = true;

	// 窗口属性
	private JSONObject attr = new JSONObject();

	public DesktopAppBean() {

	}

	public String getAttr() {
		return attr.toString();
	}

	public DesktopAppBean addAttr(String key, Object value)
			throws JSONException {
		this.attr.put(key, value);
		return this;
	}

	public DesktopAppBean addDefaultAttrs() throws JSONException {
		this.attr.put(DIALOG_ATTRIBUTE_WIDTH, 600);
		this.attr.put(DIALOG_ATTRIBUTE_HEIGHT, 400);
		this.attr.put(DIALOG_ATTRIBUTE_MIN_WIDTH, 600);
		this.attr.put(DIALOG_ATTRIBUTE_MIN_HEIGHT, 400);
		this.attr.put(DIALOG_ATTRIBUTE_CLOSABLE, true);
		this.attr.put(DIALOG_ATTRIBUTE_MINIMIZABLE, true);
		this.attr.put(DIALOG_ATTRIBUTE_MAXIMIZABLE, true);
		return this;
	}

	public DesktopAppBean(int appId, String appTitle, String appDesc,
			String iconSrc) {
		this.appId = appId;
		this.appTitle = appTitle;
		this.appDesc = appDesc;
		this.iconSrc = iconSrc;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public boolean isRemovable() {
		return removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public String getIconSrc() {
		return iconSrc;
	}

	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}

}
