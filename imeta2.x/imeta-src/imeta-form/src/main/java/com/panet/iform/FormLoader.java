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
package com.panet.iform;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang.StringUtils;

import com.panet.iform.core.Form;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.util.ResolverUtil;
import com.panet.imeta.i18n.BaseMessages;

/**
 * 表单加载类
 * 
 * @author panwei
 * @version 1.0
 * @since 2009/06/22
 */
public class FormLoader {
	private static FormLoader loader = null;

	private FormPluginMeta[] forms = null;

	private static final String loaderPackage = "com.panet.iform.forms";

	public static final FormLoader getInstance() {
		if (loader == null) {
			init();
		}
		return loader;
	}

	/**
	 * 初始化表单加载类
	 */
	private static void init() {
		loader = new FormLoader();
		Collection<FormPluginMeta> mainForms = loader.scanAnnotation();
		loader.forms = mainForms.toArray(new FormPluginMeta[mainForms.size()]);
	}

	private Collection<FormPluginMeta> scanAnnotation() {
		ResolverUtil<FormPluginMeta> resolver = new ResolverUtil<FormPluginMeta>();

		// kettle-config.xml中定义的路径
		String allPackages = loaderPackage;
		// 找到所有的用EntityNode注释的对象
		resolver.find(new ResolverUtil.AnnotatedWith(Form.class),
				allPackages != null ? allPackages.split(",") : new String[] {});

		Collection<FormPluginMeta> formPluginMetas = new LinkedHashSet<FormPluginMeta>(
				resolver.size());
		for (Class<?> clazz : resolver.getClasses()) {
			// 得到该对象的注释类
			Form form = clazz.getAnnotation(Form.class);
			// 对象名称（类别）
			String nodeId = form.type();

			// 通用对象国际化包路径
			String packageName = form.i18nPackageName();
			if (Const.isEmpty(packageName))
				packageName = Messages.class.getPackage().getName();

			// 单独的对象国际化包路径，用于得到说明和提示信息等
			String altPackageName = clazz.getPackage().getName();

			// 对象描述
			// 从通用包(com.panet.ianalyse.core.entity.Messages)中取得
			String description = BaseMessages.getString(packageName, form
					.description());
			// 从通用包(com.panet.ianalyse.core.entity.node.Messages)中取得
			if (description.startsWith("!") && description.endsWith("!"))
				description = Messages.getString(form.description());
			// 从单独的对象包(com.panet.ianalyse.core.entity.nodes.*.Messages)中取得
			if (description.startsWith("!") && description.endsWith("!"))
				description = BaseMessages.getString(altPackageName, form
						.description());

			// 生成具体的数据对象元模型，并加入到返回容器中
			formPluginMetas.add(new FormPluginMeta(clazz, nodeId, description));
		}
		return (Collection<FormPluginMeta>) formPluginMetas;
	}

	/**
	 * 表单插件类型
	 * 
	 * @param type
	 * @return
	 */
	public FormPluginMeta getFormPluginMetaByType(String type) {
		LogWriter log = LogWriter.getInstance();
		for (FormPluginMeta meta : forms) {
			if (meta.type.equals(type)) {
				try {
					Class<?> formMetaInterface = Class.forName(meta.className
							.getName());

					return (FormPluginMeta) formMetaInterface.newInstance();

				} catch (ClassNotFoundException e) {
					log.logError(toString(), Messages
							.getString("IMeta.Log.RunImetaAction"));
				} catch (InstantiationException e) {
					log.logError(toString(), Messages
							.getString("IMeta.Log.RunImetaAction"));
				} catch (IllegalAccessException e) {
					log.logError(toString(), Messages
							.getString("IMeta.Log.RunImetaAction"));
				}
			}
		}
		return null;
	}

	/**
	 * 参数的转换--string
	 * 
	 * @param parameter
	 * @return
	 */
	public static String parameterToString(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return StringUtils.stripToEmpty(parameter[0]);
		}
		return "";
	}

	public static String replaceJavascriptString(String str) {
		if (str == null) {
			return "";
		}
		return str.replaceAll("\"", "\\\\\"");
	}

	public static String replaceJavascriptHTMLString(String str) {
		if (str == null) {
			return "";
		}
		return str.replaceAll("&", "&amp;").replaceAll(
				"<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "\\\\\"");
	}

	/**
	 * 参数的转换--int
	 * 
	 * @param parameter
	 * @return
	 */
	public static int parameterToInt(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return Integer.valueOf(parameter[0]);
		}
		return -1;
	}

	/**
	 * 参数的转换--Boolean
	 * 
	 * @param parameter
	 * @return
	 */
	public static boolean parameterToBoolean(String[] parameter) {
		if (parameter != null && parameter.length > 0) {
			return "on".equals(parameter[0]);
		}
		return false;
	}

}
