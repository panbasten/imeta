package com.panet.imeta.core.encryption;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



/**
 * 
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 * 
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String
 * getDesString(String strMi)对strMin进行解密,返回String明文
 * 
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 * byteD)byte[]型的解密
 */

public class Des {
	Key key;

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 */
	public void getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			this.key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @return
	 */
	public String getEncString(String strMing) {
		return getEncString(strMing, null);
	}

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @param encode
	 * @return
	 */
	public String getEncString(String strMing, String encode) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = (encode != null && !"".equals(encode)) ? strMing
					.getBytes(encode) : strMing.getBytes("UTF-8");
			byteMi = this.getEncCode(byteMing);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDesString(String strMi) {
		return getDesString(strMi, null);
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @param encode
	 * @return
	 */
	public String getDesString(String strMi, String encode) {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = this.getDesCode(byteMi);
			strMing = (encode != null && !"".equals(encode)) ? new String(
					byteMing, encode) : new String(byteMing, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;

	}

	/**
	 * 
	 * @param key
	 * @param src
	 * @param encode
	 * @return
	 */
	public static String encrypt(String key, String src, String encode) {
		Des des = new Des();
		des.getKey(key);
		return des.getEncString(src, encode);
	}

	/**
	 * 
	 * @param key
	 * @param src
	 * @param encode
	 * @return
	 */
	public static String decrypt(String key, String src, String encode) {
		Des des = new Des();
		des.getKey(key);
		return des.getDesString(src, encode);
	}

	public static void main(String[] args) {

		System.out.println("des demo");
		Des des = new Des();// 实例化一个对像
		des.getKey("MYKEY11");// 生成密匙
		System.out.println("key=MYKEY11");
		String strEnc = des.getEncString("CHINA测试", "gbk");// 加密字符串,返回String的密文
		System.out.println("密文=" + strEnc);

		String strDes = des.getDesString(strEnc, "gbk");// 把String 类型的密文解密
		System.out.println("明文=" + strDes);

	}

}
