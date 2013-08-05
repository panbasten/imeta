package com.plywet.mm.platformtools.utils;

/**
 * 通用的工具类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-9 下午02:46:08
 */
public class CommonUtils {

	/**
	 * 将null替换成空字符串返回
	 * 
	 * @param str
	 * @return
	 */
	public static String NVL(String str) {
		if (str == null)
			str = "";
		return str;
	}

	public static boolean isEmpty(byte[] b) {
		if (b == null || b.length <= 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String s) {
		if (s == null || s.length() <= 0) {
			return true;
		}
		return false;
	}
}
