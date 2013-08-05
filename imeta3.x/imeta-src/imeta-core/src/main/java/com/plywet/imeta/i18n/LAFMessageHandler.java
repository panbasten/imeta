package com.plywet.imeta.i18n;

import java.util.MissingResourceException;

import com.plywet.imeta.core.property.BasePropertyHandler;
import com.plywet.imeta.utils.Const;

/**
 * 默认的message国际化配置访问的实现类
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 */
public class LAFMessageHandler extends GlobalMessages {

	// 待替换的包路径
	private static String replace = "com.plywet.imeta";
	// 替换用的路径
	private static String replaceWith = null;
	private static int offset = -1;
	// 替换后的系统默认的配置文件位置（GlobalMessages同级）
	private static String replaceSysBundle = null;

	private static final String REPLACE_WITH_PATH = "Sino.Message.ReplaceWith.Path";

	static {
		replaceWith = BasePropertyHandler.getProperty(REPLACE_WITH_PATH);
	}

	public LAFMessageHandler() {
		super();
		reinit();
	}

	public synchronized static MessageHandler getInstance() {
		if (GMinstance == null) {
			GMinstance = new LAFMessageHandler();
		}
		return (MessageHandler) GMinstance;
	}

	protected void reinit() {
		replaceWith = BasePropertyHandler.getProperty(REPLACE_WITH_PATH);
		replaceSysBundle = replacePackage(SYSTEM_BUNDLE_PACKAGE);
		offset = -1;
	}

	/**
	 * 替代应用包名为合适的目标，比如：替代com.sinosoft.dsp.*指向新的包结构。
	 * 
	 * @param packageName
	 * @return
	 */
	private String replacePackage(String packageName) {
		if (Const.isEmpty(replaceWith)) {
			return packageName;
		}
		if (offset < 0) {
			offset = packageName.indexOf(replace);
			if (offset >= 0) {
				offset = replace.length();
			}
		}
		return new String(replaceWith + packageName.substring(offset));
	}

	private String internalCalc(String packageName, String global, String key,
			Object[] parameters) {
		String string = null;

		// 首先尝试从原始包中查找，使用系统的首选语言
		try {
			string = findString(packageName, langChoice.getDefaultLocale(),
					key, parameters);
		} catch (MissingResourceException e) {
		}
		if (string != null) {
			// System.out.println("found: "+key+"/"+string+" in "+packageName+" lang "+langChoice.getDefaultLocale());
			return string;
		}

		// 然后尝试从i18n包中查找，使用系统的首选语言
		try {
			string = findString(global, langChoice.getDefaultLocale(), key,
					parameters);
		} catch (MissingResourceException e) {
		}
		if (string != null) {
			// System.out.println("found: "+key+"/"+string+" in "+global+" lang "+langChoice.getDefaultLocale());
			return string;
		}

		// 然后尝试从原始包中查找，使用错误覆盖的语言
		try {
			string = findString(packageName, langChoice.getFailoverLocale(),
					key, parameters);
		} catch (MissingResourceException e) {
		}
		if (string != null) {
			// System.out.println("found: "+key+"/"+string+" in "+packageName+" lang "+langChoice.getFailoverLocale());
			return string;
		}

		// 然后尝试从i18n包中查找，使用错误覆盖的语言
		try {
			string = findString(global, langChoice.getFailoverLocale(), key,
					parameters);
		} catch (MissingResourceException e) {
		}
		// System.out.println("found: "+key+"/"+string+" in "+global+" lang "+langChoice.getFailoverLocale());
		return string;
	}

	protected String calculateString(String packageName, String key,
			Object[] parameters) {
		String string = null;
		string = internalCalc(replacePackage(packageName), replaceSysBundle,
				key, parameters);
		if (string != null)
			return string;

		string = internalCalc(packageName, SYSTEM_BUNDLE_PACKAGE, key,
				parameters);
		if (string != null)
			return string;

		string = key;
		return string;
	}

}
