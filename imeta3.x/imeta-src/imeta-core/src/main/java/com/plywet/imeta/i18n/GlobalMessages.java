package com.plywet.imeta.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.plywet.imeta.core.exception.ImetaException;
import com.plywet.imeta.core.log.Log;
import com.plywet.imeta.core.property.BasePropertyHandler;
import com.plywet.imeta.utils.Const;

/**
 * 统一Messages配置对象
 * 
 * @since 1.0 2010-1-27
 * @author 潘巍（Peter Pan）
 * 
 */
public class GlobalMessages extends AbstractMessageHandler {

	private static final Log log = Log.getLog(GlobalMessages.class.getName());

	protected static final ThreadLocal<Locale> threadLocales = new ThreadLocal<Locale>();

	// 语言选择器
	protected static final LanguageChoice langChoice = LanguageChoice
			.getInstance();

	// 系统默认的messages目录放置的路径
	protected static final String SYSTEM_BUNDLE_PACKAGE;
	static {
		String val = BasePropertyHandler
				.getProperty("Message.Default.Package");
		if (Const.isEmpty(val)) {
			val = GlobalMessages.class.getPackage().getName();
		} else if ("root".equalsIgnoreCase(val)) {
			val = "";
		}
		SYSTEM_BUNDLE_PACKAGE = val;

	}

	protected static final String BUNDLE_NAME = "messages.messages";

	protected static final Map<String, ResourceBundle> locales = Collections
			.synchronizedMap(new HashMap<String, ResourceBundle>());

	public static final String[] localeCodes = { "en_US", "nl_NL", "zh_CN",
			"es_ES", "fr_FR", "de_DE", "pt_BR", "pt_PT", "es_AR", "no_NO",
			"it_IT", "ja_JP", "ko_KR" };

	public static final String[] localeDescr = { "English (US)", "Nederlands",
			"简体中文", "Espa\u00F1ol (Spain)", "Fran\u00E7ais", "Deutsch",
			"Portuguese (Brazil)", "Portuguese (Portugal)",
			"Espa\u00F1ol (Argentina)", "Norwegian (Norway)",
			"Italian (Italy)", "Japanese (Japan)", "Korean (Korea)", };

	protected static GlobalMessages GMinstance = null;

	public GlobalMessages() {
	}

	public synchronized static MessageHandler getInstance() {
		if (GMinstance == null) {
			GMinstance = new GlobalMessages();
		}
		return (MessageHandler) GMinstance;
	}

	protected static Map<String, ResourceBundle> getLocales() {
		return locales;
	}

	public synchronized static Locale getLocale() {
		Locale rtn = threadLocales.get();
		if (rtn != null) {
			return rtn;
		}

		setLocale(langChoice.getDefaultLocale());
		return langChoice.getDefaultLocale();
	}

	public synchronized static void setLocale(Locale newLocale) {
		threadLocales.set(newLocale);
	}

	protected static String getLocaleString(Locale locale) {
		String locString = locale.toString();
		if (locString.length() == 5 && locString.charAt(2) == '_') // 强调大小写格式
		{
			locString = locString.substring(0, 2).toLowerCase() + "_"
					+ locString.substring(3).toUpperCase();
		}
		return locString;
	}

	protected static String buildHashKey(Locale locale, String packageName) {
		return packageName + "_" + getLocaleString(locale);
	}

	protected static String buildBundleName(String packageName) {
		return packageName + "." + BUNDLE_NAME;
	}

	/**
	 * 通过类的包路径获得资源绑定对象
	 * 
	 * @param packageName
	 *            类的包路径，该类的同级目录包含messages目录
	 * @return
	 * @throws MissingResourceException
	 */
	public static ResourceBundle getBundle(String packageName)
			throws MissingResourceException {
		return getBundle(packageName, GlobalMessages.getInstance().getClass());
	}

	/**
	 * 通过类的包路径获得资源绑定对象<br>
	 * 
	 * @param packageName
	 * @param resourceClass
	 *            指明默认的资源路径的类（一般是GlobalMessages类对象）
	 * @return
	 * @throws MissingResourceException
	 */
	public static ResourceBundle getBundle(String packageName,
			Class<?> resourceClass) throws MissingResourceException {
		ResourceBundle bundle;
		try {
			// 然后尝试加载语言选择器中默认的语言区域
			bundle = getBundle(LanguageChoice.getInstance().getDefaultLocale(),
					packageName, resourceClass);
			return bundle;
		} catch (MissingResourceException e2) {
			try {
				// 然后尝试加载语言选择器中偏好的语言区域
				bundle = getBundle(LanguageChoice.getInstance()
						.getFailoverLocale(), packageName, resourceClass);
				return bundle;
			} catch (MissingResourceException e3) {
				throw new MissingResourceException("无法在下列语言中找到message配置文件："
						+ LanguageChoice.getInstance().getDefaultLocale() + ","
						+ LanguageChoice.getInstance().getFailoverLocale(),
						packageName, packageName);
			}
		}
	}

	/**
	 * 通过区域和类的包路径获得资源绑定对象
	 * 
	 * @param locale
	 *            区域
	 * @param packageName
	 *            类的包路，该类的同级目录包含messages目录
	 * @return
	 * @throws MissingResourceException
	 */
	public static ResourceBundle getBundle(Locale locale, String packageName)
			throws MissingResourceException {
		return getBundle(locale, packageName, GlobalMessages.getInstance()
				.getClass());
	}

	/**
	 * 通过区域和类的包路径获得资源绑定对象
	 * 
	 * @param locale
	 *            区域
	 * @param packageName
	 *            类的包路，该类的同级目录包含messages目录
	 * @param resourceClass
	 *            指明默认的资源路径的类（一般是GlobalMessages类对象）
	 * @return
	 * @throws MissingResourceException
	 */
	public static ResourceBundle getBundle(Locale locale, String packageName,
			Class<?> resourceClass) throws MissingResourceException {
		String filename = buildHashKey(locale, packageName);
		filename = "/" + filename.replace('.', '/') + ".properties";

		try {
			ResourceBundle bundle = locales.get(filename);
			if (bundle == null) {
				InputStream inputStream = resourceClass
						.getResourceAsStream(filename);
				if (inputStream == null) {
					inputStream = ClassLoader
							.getSystemResourceAsStream(filename);
				}
				if (inputStream != null) {
					bundle = new PropertyResourceBundle(inputStream);
					locales.put(filename, bundle);
				} else {
					throw new MissingResourceException("无法找到配置文件[" + filename
							+ "]", locale.toString(), packageName);
				}
			}
			return bundle;
		} catch (IOException e) {
			throw new MissingResourceException("无法找到配置文件[" + filename + "] : "
					+ e.toString(), locale.toString(), packageName);
		}
	}

	protected String findString(String packageName, Locale locale, String key,
			Object[] parameters) throws MissingResourceException {
		return findString(packageName, locale, key, parameters, GlobalMessages
				.getInstance().getClass());
	}

	protected String findString(String packageName, Locale locale, String key,
			Object[] parameters, Class<?> resourceClass)
			throws MissingResourceException {
		try {
			ResourceBundle bundle = getBundle(locale, packageName + "."
					+ BUNDLE_NAME, resourceClass);
			String unformattedString = bundle.getString(key);
			String string = MessageFormat.format(unformattedString, parameters);
			return string;
		} catch (IllegalArgumentException e) {
			String message = "配置值格式化出现问题，key=[" + key + "], locale=[" + locale
					+ "], package=" + packageName + " : " + e.toString();
			log.error(message);
			log.error(Const.getStackTracker(e));
			throw new MissingResourceException(message, packageName, key);
		}
	}

	protected String calculateString(String packageName, String key,
			Object[] parameters) {
		return calculateString(packageName, key, parameters, GlobalMessages
				.getInstance().getClass());
	}

	protected String calculateString(String packageName, String key,
			Object[] parameters, Class<?> resourceClass) {
		String string = null;

		// 1.首先尝试语言选择器中的默认语言区域
		try {
			string = findString(packageName, langChoice.getDefaultLocale(),
					key, parameters, resourceClass);
		} catch (MissingResourceException e) {
		}
		if (string != null)
			return string;

		try {
			string = findString(SYSTEM_BUNDLE_PACKAGE, langChoice
					.getDefaultLocale(), key, parameters, resourceClass);
		} catch (MissingResourceException e) {
		}
		if (string != null)
			return string;

		// 2.最后尝试语言选择器中的偏好语言区域
		try {
			string = findString(packageName, langChoice.getFailoverLocale(),
					key, parameters, resourceClass);
		} catch (MissingResourceException e) {
		}
		if (string != null)
			return string;

		try {
			string = findString(SYSTEM_BUNDLE_PACKAGE, langChoice
					.getFailoverLocale(), key, parameters, resourceClass);
		} catch (MissingResourceException e) {
		}
		if (string != null)
			return string;

		string = key;
		String message = "Message国际化配置文件中没有找到该值: key=[" + key + "], package="
				+ packageName;
		log.warn(Const.getStackTracker(new ImetaException(message)));

		return string;
	}

	/**
	 * 通过key获得属性<br>
	 * 从默认的配置中取得（GlobalMessages）
	 * 
	 * @param key
	 *            KEY
	 */
	public String getString(String key) {
		Object[] parameters = null;
		return calculateString(SYSTEM_BUNDLE_PACKAGE, key, parameters);
	}

	/**
	 * 通过包名和key获得属性
	 * 
	 * @param packageName
	 *            包名
	 * @param key
	 *            KEY
	 */
	public String getString(String packageName, String key) {
		Object[] parameters = new Object[] {};
		return calculateString(packageName, key, parameters);
	}

	/**
	 * 通过包名和key获得属性，可替换参数
	 * 
	 * @param packageName
	 *            包名
	 * @param key
	 *            KEY
	 * @param parameters
	 *            替换参数，用于替换配置中的{0},{1},{2}....
	 */
	public String getString(String packageName, String key,
			String... parameters) {
		return calculateString(packageName, key, parameters);
	}

	/**
	 * 通过包名和key获得属性，可替换参数
	 * 
	 * @param packageName
	 *            包名
	 * @param key
	 *            KEY
	 * @param resourceClass
	 *            默认资源的同级类
	 * @param parameters
	 *            替换参数，用于替换配置中的{0},{1},{2}....
	 * 
	 */
	public String getString(String packageName, String key,
			Class<?> resourceClass, String... parameters) {
		return calculateString(packageName, key, parameters, resourceClass);
	}

}
