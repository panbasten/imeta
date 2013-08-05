package com.plywet.mm.platformtools.utils;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 本地语言的工具类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-9 下午03:26:07
 */
public class MMLocaleUtils {

	public static String getLanguage() {
		String str = CommonUtils.NVL(MMConst.get("language_key"));
		if (str.length() > 0 && !str.equals("language_default")) {

		} else {
			str = getDefaultLanguage();
		}
		return str;
	}

	public static String getLanguage(SharedPreferences sp) {
		String str = CommonUtils.NVL(sp.getString("language_key", null));
		if (str.length() > 0 && !str.equals("language_default")) {

		} else {
			str = getDefaultLanguage();
		}
		MMConst.put("language_key", str);
		return str;
	}

	private static String getDefaultLanguage() {
		String def = Locale.getDefault().getLanguage().trim();
		if (def.equals("zh")) {
			return Locale.getDefault().getLanguage().trim() + "_"
					+ Locale.getDefault().getCountry().trim();
		}
		return def;
	}

	public static void setLocale(Context paramContext, Locale paramLocale) {
		Resources localResources = paramContext.getResources();
		Configuration localConfiguration = localResources.getConfiguration();
		if (localConfiguration.locale.equals(paramLocale)) {

		} else {
			DisplayMetrics localDisplayMetrics = localResources
					.getDisplayMetrics();
			localConfiguration.locale = paramLocale;
			localResources.updateConfiguration(localConfiguration,
					localDisplayMetrics);
			Resources.getSystem().updateConfiguration(localConfiguration,
					localDisplayMetrics);
		}
	}
}
