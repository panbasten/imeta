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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.exception.ImetaFormException;

	/**
	* 简单表单操作对象类
	* 
	*@author panwei
	*@version 1.0 
	*@since 2009/06/22
	*/ 
public class SimpleFormMeta extends BaseFormMeta implements FormMetaInterface {

	public static final String A_TAG = "<a></a>";
	public static final String INPUT_TAG = "<input>";
	public static final String BR_TAG = "<br>";
	public static final String SELECT_TAG = "<select></select>";
	public static final String DIV_TAG = "<div></div>";
	public static final String UL_TAG = "<ul></ul>";
	public static final String LI_TAG = "<li></li>";
	public static final String FORM_TAG = "<form></form>";
	public static final String LABEL_TAG = "<label></label>";
	public static final String FIELDSET_TAG = "<fieldset></fieldset>";
	public static final String LEGEND_TAG = "<legend></legend>";
	public static final String P_TAG = "<p></p>";
	public static final String TABLE_TAG = "<table></table>";
	public static final String FONT_TAG = "<font></font>";
	public static final String TEXTAREA_TAG = "<textarea></textarea>";
	public static final String SPAN_TAG = "<span></span>";
	public static final String BUTTON_TAG = "<button></button>";

	/**
	 * 用于描述表单自身属性
	 */
	private SimpleFormDataMeta simpleFormDataMeta;

	/**
	 * 表单的标签属性
	 */
	private String tagCreate;

	/**
	 * 转换操作
	 */
	private String trans;
	
	/**
	 * 简单表单初始化
	 * 
	 * @param dataMeta   数据元
	 * @param tagCreate    创建标签
	 * @param single        唯一定义
	 */
	public SimpleFormMeta(SimpleFormDataMeta dataMeta, String tagCreate,
			boolean single) {
		super();
		this.simpleFormDataMeta = dataMeta;
		super.setDataMeta(dataMeta);
		this.tagCreate = tagCreate;
		super.setSingle(single);
	}
	
	/**
	 * 得到表单的JSON代码
	 * 
	 * @return
	 * @throws ImetaFormException
	 */
	@Override
	public JSONObject getFormJo() throws ImetaFormException {
		JSONObject formJo = super.getFormJo();
		if (!StringUtils.isEmpty(this.tagCreate))
			formJo.put(BaseFormMeta.TAG_CREATE, this.tagCreate);
		if (this.simpleFormDataMeta != null) {
			JSONObject propertiesJo = this.simpleFormDataMeta.getPropertiesJo();
			if (propertiesJo != null) {
				formJo.put(BaseFormMeta.TAG_ATTRITUDE, propertiesJo);
			}

			if (!StringUtils.isEmpty(this.simpleFormDataMeta.getHtml())) {
				formJo.put(BaseFormMeta.TAG_HTML, this.simpleFormDataMeta
						.getHtml());
			}
		}

		if (this.getListeners().size() > 0) {
			JSONObject lRoot = new JSONObject();
			Map.Entry<String, List<String>> entry;
			String key;
			List<String> value;
			for (Iterator<Map.Entry<String, List<String>>> iter = this
					.getListeners().entrySet().iterator(); iter.hasNext();) {
				entry = iter.next();
				key = entry.getKey();
				value = entry.getValue();
				JSONArray la = new JSONArray();
				for (String l : value) {
					la.add(l);
				}

				lRoot.put(key, la);
			}
			formJo.put(BaseFormMeta.TAG_LISTENERS, lRoot);
		}

		if (!StringUtils.isEmpty(trans)) {
			formJo.put(BaseFormMeta.TAG_TRANS, trans);
		}

		return formJo;
	}
	
	/**
	 * 简单表单返回值
	 * @return
	 */
	public SimpleFormDataMeta getSimpleFormDataMeta() {
		return simpleFormDataMeta;
	}
	
	/**
	 * 定义简单表单
	 * @param simpleFormDataMeta
	 */
	public void setSimpleFormDataMeta(SimpleFormDataMeta simpleFormDataMeta) {
		this.simpleFormDataMeta = simpleFormDataMeta;
	}
	
	/**
	 * 返回创建表单
	 * @return
	 */
	public String getTagCreate() {
		return tagCreate;
	}
	
	/**
	 * 定义创建表单
	 * @param tagCreate
	 */
	public void setTagCreate(String tagCreate) {
		this.tagCreate = tagCreate;
	}
	/**
	 * 返回转换操作
	 * @return
	 */
	public String getTrans() {
		return trans;
	}
	
	/**
	 * 定义转换操作
	 * @param trans
	 */
	public void setTrans(String trans) {
		this.trans = trans;
	}
	
	/**
	 * 定义如何是否有效
	 * @param disabled
	 */
	public void setDisabled(boolean disabled) {
		simpleFormDataMeta.setDisabled(disabled);
	}
	/**
	 * 定义只读
	 * @param readonly
	 */
	public void setReadonly(boolean readonly) {
		simpleFormDataMeta.setReadonly(readonly);
	}
	
	/**
	 * 定义如何是否显示
	 * @param display
	 */
	public void setDisplay(boolean display) {
		simpleFormDataMeta.setDisplay(display);
	}

	@Override
	public void setWidth(int width) {
		simpleFormDataMeta.setStyle("width", width + "%");
	}
	
	/**
	 * 定义唯一
	 * @param key    主要标签
	 * @param value   标签值
	 */
	public void setStyle(String key, String value) {
		simpleFormDataMeta.setStyle(key, value);
	}
	
	/**
	 * 定义添加类型
	 * @param clazz
	 */
	public void addClass(String clazz) {
		simpleFormDataMeta.addClazz(clazz);
	}

}
