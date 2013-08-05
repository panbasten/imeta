package com.plywet.mm.platformtools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import android.os.Build;

import com.plywet.mm.platformtools.utils.CommonUtils;
import com.plywet.mm.system.MessageDigestUtils;

/**
 * 日志类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 下午05:24:50
 */
public final class Log {

	// 日志水平
	private static int logLevel = 0;

	private static String filePath;

	private static PrintStream ps;

	private static long lineNum = 0L;

	private static byte[] key = null;

	public static void setLogLevel(int l) {
		logLevel = l;
	}

	public static void error(String type, String msg) {
		if (logLevel <= 4)
			LogUtils.write(ps, key, "E/" + type, msg);
	}

	public static void warn(String type, String msg) {
		if (logLevel <= 3)
			LogUtils.write(ps, key, "W/" + type, msg);
	}

	public static void info(String type, String msg) {
		if (logLevel <= 2)
			LogUtils.write(ps, key, "I/" + type, msg);
	}

	public static void debug(String type, String msg) {
		if (logLevel <= 1)
			LogUtils.write(ps, key, "D/" + type, msg);
	}

	public static void verbose(String type, String msg) {
		if (logLevel <= 0)
			LogUtils.write(ps, key, "V/" + type, msg);
	}

	public static void setLog(String pathName, String startText, int flag) {

		if (pathName == null || pathName.length() == 0 || startText == null
				|| startText.length() == 0) {
			return;
		} else {
			try {
				filePath = pathName;
				File logFile = new File(pathName);

				if (!logFile.exists()) {
					ps = new PrintStream(new BufferedOutputStream(
							new FileOutputStream(filePath)));
					lineNum = System.currentTimeMillis();
					if (CommonUtils.isEmpty(startText)) {
						StringBuffer sb = new StringBuffer();
						sb.append(startText);
						sb.append(lineNum);
						sb.append("dfdhgc");
						key = MessageDigestUtils
								.digest(sb.toString().getBytes())
								.substring(7, 21).getBytes();
					}
				} else {
					BufferedReader localBufferedReader = new BufferedReader(
							new FileReader(filePath));
					lineNum = Long.parseLong(localBufferedReader.readLine());

					if (ps != null) {
						ps.println(1 + " " + "mm.log");
						ps.println(2 + " " + startText);
						ps.println(3 + " " + lineNum);
						ps.println(4 + " " + Integer.toHexString(flag));
						ps.println(5 + " " + Build.VERSION.RELEASE);
						ps.println(6 + " " + Build.VERSION.CODENAME);
						ps.println(7 + " " + Build.VERSION.INCREMENTAL);
						ps.println(8 + " " + Build.BOARD);
						ps.println(9 + " " + Build.DEVICE);
						ps.println(10 + " " + Build.DISPLAY);
						ps.println(11 + " " + Build.FINGERPRINT);
						ps.println(12 + " " + Build.HOST);
						ps.println(13 + " " + Build.MANUFACTURER);
						ps.println(14 + " " + Build.MODEL);
						ps.println(15 + " " + Build.PRODUCT);
						ps.println(16 + " " + Build.TAGS);
						ps.println(17 + " " + Build.TYPE);
						ps.println(18 + " " + Build.USER);
						ps.println();
						ps.flush();
					}
				}
			} catch (Exception e) {

			}
		}
	}
}
