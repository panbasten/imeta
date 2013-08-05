package com.plywet.mm.system;

import java.security.MessageDigest;

/**
 * 消息加密类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 下午06:07:50
 */
public class MessageDigestUtils {
	public static final String digest(byte[] bytes) {
		int i = 0;
		char[] password = new char[16];
		password[0] = 48;
		password[1] = 49;
		password[2] = 50;
		password[3] = 51;
		password[4] = 52;
		password[5] = 53;
		password[6] = 54;
		password[7] = 55;
		password[8] = 56;
		password[9] = 57;
		password[10] = 97;
		password[11] = 98;
		password[12] = 99;
		password[13] = 100;
		password[14] = 101;
		password[15] = 102;
		String str;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte[] digestBytes = md.digest();
			int j = digestBytes.length;
			char[] rtn = new char[j * 2];
			int k = i;
			while (true) {
				if (i >= j) {
					str = new String(rtn);
					break;
				}
				int m = digestBytes[i];
				int n = k + 1;
				rtn[k] = password[(0xF & m >>> 4)];
				k = n + 1;
				rtn[n] = password[(m & 0xF)];
				i++;
			}
		} catch (Exception localException) {
			str = null;
		}
		return str;
	}
}
