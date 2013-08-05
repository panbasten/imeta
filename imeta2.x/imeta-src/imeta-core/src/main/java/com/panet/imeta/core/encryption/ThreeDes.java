package com.panet.imeta.core.encryption;

/*
 字符串 DESede(3DES) 加密
 */
import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ThreeDes {

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用

	private static final int RADIX = 16;

	/**
	 * DES,DESede,Blowfish
	 * 
	 * @param keybyte
	 *            为加密密钥，长度为24字节
	 * @param src
	 *            为被加密的数据缓冲区（源）
	 * 
	 */
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * @param keybyte
	 *            为加密密钥，长度为24字节
	 * @param src
	 *            为加密后的缓冲区
	 */
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 *            为加密密钥，长度为24字节（超过24字节截取前24字节，不足24字节补充空格字节）
	 * @param src
	 *            为被加密的数据缓冲区（源）
	 * @param encode
	 *            编码类型
	 * @return
	 */
	public static String encrypt(String key, String src, String encode) {
		try {
			byte[] keybyte = string2byte(key, encode, 24, (byte) 0x32);
			byte[] srcbyte = string2byte(src, encode);

			byte[] rtnbtye = encryptMode(keybyte, srcbyte);

			if (rtnbtye != null) {
				BigInteger rtnbi = new BigInteger(rtnbtye);
				return rtnbi.toString(RADIX);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 *            为加密密钥，长度为24字节（超过24字节截取前24字节，不足24字节补充空格字节）
	 * @param src
	 *            为加密后的缓冲区
	 * @param encode
	 *            编码类型
	 * @return
	 */
	public static String decrypt(String key, String src, String encode) {
		try {
			boolean hasEncode = (encode != null && !"".equals(encode)) ? true
					: false;
			byte[] keybyte = string2byte(key, encode, 24, (byte) 0x32);
			BigInteger srcbi = new BigInteger(src, RADIX);
			byte[] srcbyte = srcbi.toByteArray();

			byte[] rtnbtye = decryptMode(keybyte, srcbyte);
			if (rtnbtye != null) {
				return (hasEncode) ? new String(rtnbtye, encode) : new String(
						rtnbtye);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return null;
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

	/**
	 * 转换成十六进制字符串
	 */
	public static String byte2string(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) throws Exception {
		// 添加新安全算法,如果用JCE就要把它添加进去

		final byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10,
				0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD,
				0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36,
				(byte) 0xE2 }; // 24字节的密钥

		final String key2 = "1234567890123456789012";
		final byte[] keyBytes2 = string2byte(key2, "utf-8", 24, (byte) 32);
		System.out.println(keyBytes2[23]);

		String szSrc = "This is a 3DES test. 测试";

		System.out.println("加密前的字符串:" + szSrc);

		byte[] encoded = encryptMode(keyBytes, szSrc.getBytes());
		System.out.println("加密后的字符串:" + new String(encoded));

		byte[] srcBytes = decryptMode(keyBytes, encoded);
		System.out.println("解密后的字符串:" + (new String(srcBytes)));

		byte[] encoded2 = encryptMode(keyBytes2, szSrc.getBytes());
		System.out.println("加密后的字符串:" + new String(encoded2));

		byte[] srcBytes2 = decryptMode(keyBytes2, encoded2);
		System.out.println("解密后的字符串:" + (new String(srcBytes2)));

		String en = encrypt("1234567890123456789012",
				"This is a 3DES test. 测试2", "");
		System.out.println(en);

		String de = decrypt("1234567890123456789012", en, "");
		System.out.println(de);

	}
}
