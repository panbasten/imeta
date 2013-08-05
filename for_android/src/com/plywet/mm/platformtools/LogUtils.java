package com.plywet.mm.platformtools;

import java.io.PrintStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.text.format.DateFormat;

import com.plywet.mm.platformtools.utils.CommonUtils;
import com.plywet.mm.system.ByteUtils;

public class LogUtils {

	public static void write(PrintStream ps, byte[] key, String type, String msg) {
		if (ps == null || CommonUtils.isEmpty(key) || CommonUtils.isEmpty(type)
				|| CommonUtils.isEmpty(msg)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(DateFormat.format("MM-dd kk:mm:ss",
				System.currentTimeMillis()));
		sb.append(" ").append(type).append(" ").append(msg);

		String str = sb.toString();

		try {
			DESKeySpec localDESKeySpec = new DESKeySpec(key);
			SecretKey localSecretKey = SecretKeyFactory.getInstance("DES")
					.generateSecret(localDESKeySpec);
			Cipher localCipher = Cipher.getInstance("DES");
			localCipher.init(1, localSecretKey);
			byte[] rtn = localCipher.doFinal(str.getBytes());
			ps.write(ByteUtils.int2byte(rtn.length));
			ps.write(rtn);
			ps.flush();
		} catch (Exception e) {
			ps.flush();
		}
	}
}
