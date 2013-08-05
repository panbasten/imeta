package com.plywet.mm.platformtools;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于设定相关模块被锁定，不能使用
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-12 下午12:44:27
 */
public class MMEntryLock {

	private static final String PKG = "MicroMsg.MMEntryLock";

	private static Set locks = new HashSet();

	public static boolean isLocked(String key) {
		return locks.contains(key);
	}

	public static boolean addLock(String key) {
		if (isLocked(key)) {
			Log.debug(PKG, "locked-" + key);
			return false;
		} else {
			locks.add(key);
			Log.debug(PKG, "lock-" + key);
			return true;
		}
	}

	public static void removeLock(String key) {
		locks.remove(key);
		Log.debug(PKG, "unlock-" + key);
	}
}
