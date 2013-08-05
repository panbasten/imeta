package com.plywet.imeta.utils;

import java.security.MessageDigest;

/**
 * 转换哈希值的工具类
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-23 上午10:41:15
 */
public class HashUtils {
	private static String digestAlgorithm = "SHA-1";

	private static String charset = "UTF-8";

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 实用工具：转换用户密码的Hash值
	 * 
	 * @param plainTextPassword
	 *            用于转Hash值的普通不文本密码
	 */
	public static String hashPassword(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte[] rawHash = digest.digest();
			return new String(encodeHex(rawHash));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用工具：将字符串编码为Hash值。<br>
	 * 默认使用UTF-8
	 * 
	 * @param object
	 * @return
	 * @throws RuntimeException
	 */
	public static String encode(Object object) throws RuntimeException {
		return encode(object, charset);
	}

	/**
	 * 使用工具：将字符串编码为Hash值
	 * 
	 * @param object
	 *            字符串或者其他对象
	 * @param cs
	 *            字符串编码格式
	 * @return
	 * @throws RuntimeException
	 */
	public static String encode(Object object, String cs)
			throws RuntimeException {
		try {
			byte[] byteArray = object instanceof String ? ((String) object)
					.getBytes(cs) : (byte[]) object;
			return new String(encodeHex(byteArray));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

}
