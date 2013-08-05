package com.plywet.mm.database;

import com.plywet.mm.platformtools.Log;

import android.database.Cursor;

/**
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 下午07:04:34
 */
public class DBTestCounter {

	private static boolean hasTest = false;
	private static int count = 1;

	private static String toString(StackTraceElement[] el) {
		String str = "";
		for (int i = 0; (i < el.length)
				&& el[i].getClassName().contains("com.plywet"); i++) {
			str += "[" + el[i].getClassName() + ":" + el[i].getMethodName()
					+ "]";
		}
		return str;
	}

	public static void test(Cursor cursor) {
		if (hasTest) {
			if (cursor != null) {
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
				}
				cursor.moveToFirst();
			}
		}
	}

	public static void test(String text) {
		if (hasTest) {
			Log.debug("dbtest", "[" + count + "]--" + text);
			Log.debug(
					"dbtest",
					"[" + count + "]--"
							+ toString(new Throwable().getStackTrace()));
			count++;
		}
	}

}
