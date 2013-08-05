package com.plywet.mm.system;

/**
 * 字节型的帮助类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 下午07:00:40
 */
public class ByteUtils {

	/**
	 * 将4个byte转成int
	 * 
	 * @param b
	 * @return
	 */
	public static int byte2int(byte[] b) {
		return (0xFF & b[0]) << 24 | (0xFF & b[1]) << 16 | (0xFF & b[2]) << 8
				| (0xFF & b[3]) << 0;
	}

	/**
	 * 将int转成4个byte
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] int2byte(int input) {
		byte[] result = new byte[4];
		for (int i = 0;; i++) {
			if (i >= 4)
				return result;
			result[i] = (byte) (0xFF & input >> i * 8);
		}
	}

	/**
	 * 将int转成4个byte，并且倒置byte数组
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] int2byteWithInvert(int input) {
		byte[] b = int2byte(input);
		int len = b.length;
		byte[] rtn = new byte[len];
		for (int i = 0; i < len; i++) {
			rtn[i] = b[len - 1 - i];
		}
		return rtn;
	}
}
