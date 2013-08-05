package com.plywet.mm.dao;

public class TShakeItemDao {
	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS shakeitem1 ( shakeItemID INTEGER PRIMARY KEY, username TEXT, nickname TEXT, province TEXT, city TEXT, signature TEXT, distance TEXT, sex INT, imgstatus INT, hasHDImg INT, insertBatch INT, reserved1 INT, reserved2 INT, reserved3 TEXT, reserved4 TEXT )",
				"CREATE UNIQUE INDEX IF NOT EXISTS shakeItemUsernameIndex ON shakeitem1 ( username )" };
	}
}
