package com.panet.imeta.core.encryption;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Xor {

	private static final int RADIX = 16;

	public static BigInteger xor(byte[] by_r0, byte[] by_key) {
		int len_r0 = by_r0.length;
		int len_key = by_key.length;
		byte[] new_by_key = new byte[len_r0];

		for (int i = 0; i < len_r0; i++) {
			new_by_key[i] = by_key[i % len_key];
		}

		return (new BigInteger(by_r0)).xor(new BigInteger(new_by_key));
	}

	public static String encrypt(String key, String src, String encode) {
		if (src == null || src.length() == 0)
			return "";

		try {
			BigInteger bi_r1 = xor(string2byte(src, encode), string2byte(key,
					null));

			return bi_r1.toString(RADIX);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src;
	}

	public static final String decrypt(String key, String src, String encode) {
		if (src == null || src.length() == 0)
			return "";

		try {
			src = src.replaceAll("\r", "").replaceAll("\n", "");
			BigInteger bi_r1 = new BigInteger(src, RADIX);
			BigInteger bi_r0 = xor(bi_r1.toByteArray(), string2byte(key, null));

			return byte2string(bi_r0.toByteArray(), encode);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将byte转成字符串
	 * 
	 * @param src
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String byte2string(byte[] src, String encode)
			throws UnsupportedEncodingException {
		if (encode == null || encode.length() == 0) {
			return new String(src);
		} else {
			return new String(src, encode);
		}
	}

	/**
	 * 将字符串转成byte
	 * 
	 * @param src
	 *            源字符串
	 * @param encode
	 *            源字符串编码格式
	 * @param d
	 *            截取位数（当d>0时有效）
	 * @param fix
	 *            当位数不足时填充的byte
	 * @return
	 * @throws Exception
	 */
	public static byte[] string2byte(String src, String encode, int d, byte fix)
			throws Exception {
		byte[] bsrc = null;

		if (encode != null && !"".equals(encode)) {
			bsrc = src.getBytes(encode);
		} else {
			bsrc = src.getBytes();
		}
		if (d > 0) {
			byte[] rsrc = new byte[d];
			int len = bsrc.length;
			for (int i = 0; i < d && i < len; i++) {
				rsrc[i] = bsrc[i];
			}
			for (int i = len; i < d; i++) {
				rsrc[i] = fix;
			}
			return rsrc;
		}
		return bsrc;
	}

	public static byte[] string2byte(String src, String encode)
			throws Exception {
		return string2byte(src, encode, 0, (byte) 0x32);
	}

	public static void main(String[] args) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String encodin = "utf-8";
		try {
			FileInputStream fis = new FileInputStream("d:/a.xml");
			InputStreamReader isr = new InputStreamReader(fis, encodin);
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String src = buffer.toString();
		String key = "uaap";
		String d = Xor.encrypt(key, src, encodin);
		System.out.println(d);
		String e = Xor.decrypt(key, d, encodin);
		System.out.println(e);

	}
}
