package com.plywet.mm.platformtools.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-9 下午03:06:11
 */
public final class MMConst {

	private static final Map<String, String> constMap = new HashMap<String, String>();

	public static String get(String key) {
		return constMap.get(key);
	}

	public static void put(String key, String value) {
		constMap.put(key, value);
	}
}
