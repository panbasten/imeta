package com.plywet.imeta.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 统一国际化Message的帮助类
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 下午03:38:45
 */
public class GlobalMessageUtil {

	/**
	 * 获得格式化后的Message的输出信息，从默认的配置中获得
	 * 
	 * @param key
	 *            错误信息主键
	 * @return
	 */
	public static String formatErrorMessage(String key) {
		return formatErrorMessage(key, BaseMessages.getString(key));
	}

	/**
	 * 获得格式化后的Message的输出信息
	 * 
	 * @param key
	 *            错误信息主键，例如：UUIDUtil.ERROR_0001_LOADING_ETHERNET_ADDRESS
	 * @param msg
	 *            错误消息
	 * @return
	 */
	public static String formatErrorMessage(String key, String msg) {
		String s2 = key.substring(0, key.indexOf('.') + "ERROR_0000".length()
				+ 1);
		return BaseMessages.getString("MESSUTIL.ERROR_FORMAT_MASK", s2, msg);
	}

	/**
	 * 通过资源绑定类和Key获得值
	 * 
	 * @param bundle
	 * @param key
	 * @return
	 * @throws MissingResourceException
	 */
	public static String getString(ResourceBundle bundle, String key)
			throws MissingResourceException {
		return MessageFormat.format(bundle.getString(key), new Object[] {});
	}

	/**
	 * 通过资源绑定类和Key获得格式化好的错误信息
	 * 
	 * @param bundle
	 * @param key
	 * @return
	 */
	public static String getErrorString(ResourceBundle bundle, String key) {
		return formatErrorMessage(key, getString(bundle, key));
	}

	/**
	 * 通过资源绑定类、Key和参数获得值
	 * 
	 * @param bundle
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getString(ResourceBundle bundle, String key,
			String... parameters) {
		try {
			if (parameters == null) {
				return MessageFormat.format(bundle.getString(key),
						new Object[] {});
			} else {
				return MessageFormat.format(bundle.getString(key),
						(Object[]) parameters);
			}
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * 通过资源绑定类、Key和参数获得格式化好的错误信息
	 * 
	 * @param bundle
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getErrorString(ResourceBundle bundle, String key,
			String... parameters) {
		return formatErrorMessage(key, getString(bundle, key, parameters));
	}

}