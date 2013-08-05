package com.plywet.imeta.i18n;

import java.util.Locale;

import com.plywet.imeta.core.property.BasePropertyHandler;
import com.plywet.imeta.utils.Const;

/**
 * 语言选择器
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 下午01:27:17
 */
public class LanguageChoice {

	private static final String STRING_FAILOVER_LOCALE = "LocaleFailover";
	private static final String STRING_DEFAULT_LOCALE = "LocaleDefault";

	private static LanguageChoice choice;

	// 默认区域
	private Locale defaultLocale;

	// 偏好区域
	private Locale failoverLocale;

	private LanguageChoice() {
		defaultLocale = getLocale(BasePropertyHandler
				.getProperty(STRING_DEFAULT_LOCALE));
		if (defaultLocale == null) {
			defaultLocale = Const.DEFAULT_LOCALE;
		}

		failoverLocale = getLocale(BasePropertyHandler
				.getProperty(STRING_FAILOVER_LOCALE));
		if (failoverLocale == null) {
			failoverLocale = Locale.US;
		}
	}

	private Locale getLocale(String str) {
		if (Const.isEmpty(str)) {
			return null;
		} else {
			String[] defArr = str.split("_");
			if (defArr.length == 2) {
				return new Locale(defArr[0], defArr[1]);
			} else {
				return new Locale(str);
			}
		}
	}

	public static final LanguageChoice getInstance() {
		if (choice != null)
			return choice;

		choice = new LanguageChoice();

		return choice;
	}

	/**
	 * @return 返回默认区域
	 */
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * @param defaultLocale
	 *            设置默认区域
	 */
	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * @return 返回偏好区域
	 */
	public Locale getFailoverLocale() {
		return failoverLocale;
	}

	/**
	 * @param failoverLocale
	 *            设置偏好区域
	 */
	public void setFailoverLocale(Locale failoverLocale) {
		this.failoverLocale = failoverLocale;
	}

}
