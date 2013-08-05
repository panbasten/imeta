/**
 * @(#)InputMeta.java 1.00 2009/06/22
 *
 * Copyright (c) 2009 中科软科技股份有限公司 版权所有
 * Sinosoft Co.,LTD. All rights reserved.
 * This software was developed by Sinosoft Corporation. 
 * You shall not disclose and decompile such software 
 * information or code and shall use it only in accordance 
 * with the terms of the contract agreement you entered 
 * into with Sinosoft.
 */ 
package com.panet.iform.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
* 简单数据对象类
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class SimpleFormDataMeta extends BaseFormDataMeta implements
		FormDataMetaInterface {

	public static final String ATTRITUDE_ID = "id";
	public static final String ATTRITUDE_NAME = "name";
	public static final String ATTRITUDE_CLASS = "clazz";
	public static final String ATTRITUDE_STYLE = "style";
	public static final String ATTRITUDE_TITLE = "title";
	public static final String ATTRITUDE_DISABLED = "disabled";
	public static final String ATTRITUDE_READONLY = "readonly";
	public static final String CLASS_NOT_DISPLAY = "notDisplay";

	public static final String TAG_HTML = "html";

	/**
	 * NAME
	 */
	private String name = null;

	/**
	 * 类型
	 */
	private List<String> clazz = null;

	/**
	 * 内嵌样式
	 */
	private Map<String, String> style = null;

	/**
	 * 验证
	 */
	private String title = null;

	/**
	 * 创建标签
	 */
	private String createTag = null;

	/**
	 * html内容
	 */
	private String html = null;

	/**
	 * 是否有效
	 */
	private boolean disabled = false;

	/**
	 * 是否只读
	 */
	private boolean readonly = false;

	/**
	 * 是否显示
	 */
	private boolean display = true;

	public boolean isDisplay() {
		return display;
	}
/**
 * 定义无运行
 * @param display
 */
	public void setDisplay(boolean display) {
		this.display = display;
		if (this.clazz == null) {
			this.clazz = new ArrayList<String>();
		}

		if (display) {
			removeClazz(CLASS_NOT_DISPLAY);
		} else {
			addClazz(CLASS_NOT_DISPLAY);
		}
	}

	/**
	 * 初始化
	 * 
	 * @param id  标签id
	 * @param name  标签名称
	 * @param clazz  标签clazz
	 * @param type   标签类型
	 * @param validate  验证
	 * @param html      文本
	 */
	public SimpleFormDataMeta(String id, String name, String[] clazz,
			Map<String, String> style, String title, String html) {
		super(id);

		if (!StringUtils.isEmpty(name)) {
			this.name = name;
			super.putProperty(ATTRITUDE_NAME, name);
		}
		if (clazz != null) {
			this.clazz = new ArrayList<String>();
			for (String c : clazz) {
				this.clazz.add(c);
			}
			super.putProperty(ATTRITUDE_CLASS, getClazzStr(this.clazz));
		}
		if (style != null && style.size() > 0) {
			this.style = style;
			super.putProperty(ATTRITUDE_STYLE, getStyleStr(this.style));
		}
		if (!StringUtils.isEmpty(title)) {
			this.title = title;
			super.putProperty(ATTRITUDE_TITLE, title);
		}

		this.html = html;
	}

	/**
	 * 得到样式字符串
	 * 
	 * @param style
	 * @return
	 */
	private String getStyleStr(Map<String, String> style) {
		StringBuffer rtn = new StringBuffer();
		Map.Entry<String, String> entry;
		if (style != null && style.size() > 0) {
			for (Iterator<Map.Entry<String, String>> iter = style.entrySet()
					.iterator(); iter.hasNext();) {
				entry = iter.next();
				if (!StringUtils.isEmpty(entry.getValue())) {
					rtn.append(entry.getKey() + ":" + entry.getValue() + ";");
				}
			}
		}
		return rtn.toString();
	}

	/**
	 * 添加更新一个样式
	 * 
	 * @param key
	 * @param value
	 */
	public void setStyle(String key, String value) {
		if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
			if (this.style == null) {
				this.style = new HashMap<String, String>();
			}
			this.style.put(key, value);
		}
		super.putProperty(ATTRITUDE_STYLE, getStyleStr(this.style));
	}
/**
 * 标签名字
 * @return
 */
	public String getName() {
		return name;
	}
/**
 * 设置名字定义
 * @param name
 */
	public void setName(String name) {
		this.name = name;
		super.putProperty(ATTRITUDE_NAME, this.name);
	}
/**
 * 返回类型
 * @return
 */
	public List<String> getClazz() {
		return clazz;
	}
/**
 * 标签类型返回值
 * @param clazzList
 * @return
 */
	public String getClazzStr(List<String> clazzList) {
		String rtn = "";
		if (clazzList != null && clazzList.size() > 0) {
			rtn = "";
			for (String clazz : clazzList) {
				rtn += clazz;
				rtn += " ";
			}
		}
		return rtn.trim();
	}
/**
 * 定义类型
 * @param clazz
 */
	public void setClazz(List<String> clazz) {
		this.clazz = clazz;
		super.putProperty(ATTRITUDE_CLASS, getClazzStr(this.clazz));
	}
/**
 * 添加类型
 * @param cls
 */
	public void addClazz(String cls) {
		if (this.clazz == null) {
			this.clazz = new ArrayList<String>();
		} else {
			for (String c : this.clazz) {
				if (c != null && c.equals(cls)) {
					this.clazz.remove(c);
				}
			}
		}
		this.clazz.add(cls);
		super.putProperty(ATTRITUDE_CLASS, getClazzStr(this.clazz));
	}
/**
 * 移动类型
 * @param cls
 */
	public void removeClazz(String cls) {
		if (this.clazz != null) {
			for (String c : this.clazz) {
				if (c != null && c.equals(cls)) {
					this.clazz.remove(c);
				}
			}
			super.putProperty(ATTRITUDE_CLASS, getClazzStr(this.clazz));
		}
	}
/**
 * 返回内嵌样式
 * @return
 */
	public Map<String, String> getStyle() {
		return style;
	}
/**
 * 定义内嵌样式
 * @param style
 */
	public void setStyle(Map<String, String> style) {
		this.style = style;
	}
/**
 * 返回验证值
 * @return
 */
	public String getTitle() {
		return title;
	}
/**
 * 定义验证值
 * @param title
 */
	public void setTitle(String title) {
		this.title = title;
		super.putProperty(ATTRITUDE_TITLE, this.title);
	}
/**
 * 创建标签返回值
 * @return
 */
	public String getCreateTag() {
		return createTag;
	}
/**
 * 定义创建的标签
 * @param createTag
 */
	public void setCreateTag(String createTag) {
		this.createTag = createTag;
	}
/**
 * 返回值文本html
 * @return
 */
	public String getHtml() {
		return html;
	}
/**
 * 文本内容
 * @param html
 */
	public void setHtml(String html) {
		this.html = html;
	}
/**
 * 判断是否有效
 * @return
 */
	public boolean isDisabled() {
		return disabled;
	}
/**
 * 定义判断的方式
 * @param disabled
 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		super.putProperty(ATTRITUDE_DISABLED, disabled);
	}
/**
 * 只读
 * @param readonly
 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
		if (readonly) {
			super.putProperty(ATTRITUDE_READONLY, readonly);
			super.putProperty("onfocus", "this.blur();");
			super.putProperty("onchange", "return false;");
		} else {
			super.putProperty(ATTRITUDE_READONLY, null);
			super.putProperty("onfocus", null);
			super.putProperty("onchange", null);
		}
	}
/**
 * 是否显示
 * @return
 */
	public boolean isReadonly() {
		return readonly;
	}

}
