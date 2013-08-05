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
package com.panet.iform.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.panet.iform.Messages;

import com.panet.iform.exception.ImetaFormException;

/**
* 表格基本元
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class BaseFormMeta implements FormMetaInterface {

	public static String LISTENER_CLICK = "click";

	public static final String TAG_CREATE = "createTag";
	public static final String TAG_ATTRITUDE = "attr";
	public static final String TAG_CONTENT = "items";
	public static final String TAG_HTML = "html";
	public static final String TAG_INIT = "init";
	public static final String TAG_LISTENERS = "listeners";
	public static final String TAG_TRANS = "trans";

	/**
	 * 表单元数据
	 */
	private BaseFormDataMeta dataMeta;

	/**
	 * 表单内容
	 */
	private List<BaseFormMeta> content;

	/**
	 * 用于描述附加属性,添加于该表单元素的根级
	 */
	private Map<String, Object> extend;

	/**
	 * 表单元素是非单行显示
	 */
	private boolean single;

	/**
	 * 初始化操作
	 */
	private String init;

	/**
	 * 监听器
	 */
	private Map<String, List<String>> listeners;

	/**
	 * 初始化
	 */
	public BaseFormMeta() {
		content = new ArrayList<BaseFormMeta>();
		extend = new HashMap<String, Object>();
		listeners = new HashMap<String, List<String>>();
		single = false;
	}
	
	/**
	 * 返回唯一
	 * @return
	 */
	public boolean isSingle() {
		return single;
	}
	
	/**
	 * 定义唯一
	 * @param single
	 */
	public void setSingle(boolean single) {
		this.single = single;
	}

	/**
	 * 得到表单的JSON代码
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		try {
			JSONArray subarr;
			Object subarrObj;
			JSONObject formJo = new JSONObject();

			if (!StringUtils.isEmpty(init)) {
				formJo.put(BaseFormMeta.TAG_INIT, init);
			}
			if (this.getContent().size() > 0) {
				JSONArray sub = new JSONArray();
				for (BaseFormMeta dataMeta : this.getContent()) {
					if (dataMeta != null) {
						if (dataMeta instanceof ComplexFormMeta) {
							if (((ComplexFormMeta) dataMeta).isMultiRoot()) {
								subarr = ((ComplexFormMeta) dataMeta)
										.getFormJa();
								for (Iterator<?> iter = subarr.iterator(); iter
										.hasNext();) {
									subarrObj = iter.next();
									if (subarrObj instanceof JSONObject) {
										sub.add(subarrObj);
									}
								}
							} else {
								sub.add(dataMeta.getFormJo());
							}
						} else {
							sub.add(dataMeta.getFormJo());
						}
					}
				}
				formJo.put(BaseFormMeta.TAG_CONTENT, sub);
			}
			if (this.getExtend().size() > 0) {
				Map.Entry<String, Object> entry;
				for (Iterator<Map.Entry<String, Object>> iter = this
						.getExtend().entrySet().iterator(); iter.hasNext();) {
					entry = iter.next();
					Object v = entry.getValue();
					if (v != null) {
						if (v instanceof BaseFormMeta) {
							formJo.put(entry.getKey(), ((BaseFormMeta) v)
									.getFormJo());
						} else if (v instanceof BaseFormMeta[]) {
							JSONArray arr = new JSONArray();
							for (BaseFormMeta subv : (BaseFormMeta[]) v) {
								arr.add(subv.getFormJo());
							}
							formJo.put(entry.getKey(), arr);
						} else {
							formJo.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
			return formJo;
		} catch (Exception ex) {
			throw new ImetaFormException(Messages
					.getString("IForm.CreateJSON.Error"), ex);
		}
	}

	/**
	 * 向扩展元素中添加内容
	 * 
	 * @param key   
	 * @param value
	 */
	public void putExtendFormData(String key, Object value) {
		extend.put(key, value);
	}
	
	/**
	 * 向表单元数据中添加内容
	 * @param key
	 * @param value
	 */
	public void putProperty(String key, Object value) {
		dataMeta.putProperty(key, value);
	}
	
	/**
	 * 定义表单元数据
	 * @param prop
	 */
	public void putProperties(Map<String, Object> prop) {
		dataMeta.putProperties(prop);
	}
	
	/**
	 * 返回该表单元素的根级
	 * @return
	 */
	public Map<String, Object> getExtend() {
		return extend;
	}
	
	/**
	 * 表单内容
	 * @return
	 */
	public List<BaseFormMeta> getContent() {
		return content;
	}
	
	/**
	 * 定义到表单内容
	 * @param content
	 */
	public void setContent(List<BaseFormMeta> content) {
		this.content = content;
	}
	
	/**
	 * 监听器返回值
	 * @return
	 */
	public Map<String, List<String>> getListeners() {
		return listeners;
	}
	
	/**
	 * 定义监听器
	 * @param listeners
	 */
	public void setListeners(Map<String, List<String>> listeners) {
		if (listeners != null)
			this.listeners = listeners;
	}
	
	/**
	 * 监听器值
	 * @param key
	 * @param value
	 */
	public void addListener(String key, List<String> value) {
		if (this.listeners.get(key) != null) {
			this.listeners.get(key).addAll(value);
		} else {
			this.listeners.put(key, value);
		}
	}
	
	/**
	 * 添加清单管理
	 * @param key
	 * @param value
	 */
	public void addListener(String key, String value) {
		List<String> l = new ArrayList<String>();
		l.add(value);
		addListener(key, l);
	}
	
	/**
	 * 添加点击
	 * @param click
	 */
	public void addClick(String click) {
		addListener(LISTENER_CLICK, click);
	}
	
	/**
	 * 设置延长
	 * @param extend
	 */
	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		try {
			return getFormJo().toString();
		} catch (ImetaFormException e) {
			return Messages.getString("IForm.CreateJSON.Error");
		}
	}

	@Override
	public void append(BaseFormMeta meta) {
		this.content.add(meta);
	}

	@Override
	public void removeAll() {
		for (int i = 0; i < this.content.size(); i++) {
			this.content.remove(i);
		}
	}

	@Override
	public void appendTo(BaseFormMeta content) {
		content.append(this);
	}

	@Override
	public void setWidth(int width) {

	}
	
	/**
	 * 表格基本数据元
	 * @return
	 */
	public BaseFormDataMeta getDataMeta() {
		return dataMeta;
	}
	
	/**
	 * 设置基本数据元
	 * @param dataMeta
	 */
	public void setDataMeta(BaseFormDataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}
	
	/**
	 * 得到标签
	 * @return
	 */
	public String getInit() {
		return init;
	}
	
	/**
	 * 设置初始化
	 * @param init
	 */
	public void setInit(String init) {
		this.init = init;
	}
	
	/**
	 * 添加初始化
	 * @param init
	 */
	public void addInit(String init) {
		if (!StringUtils.isEmpty(init)) {
			this.init = StringUtils.join(new String[] { this.init, init });
		}
	}

}
