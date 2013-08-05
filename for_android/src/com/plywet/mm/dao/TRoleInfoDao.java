package com.plywet.mm.dao;

public class TRoleInfoDao {
	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS role_info ( id TEXT PRIMARY KEY, name TEXT, status INT, text_reserved1 TEXT, text_reserved2 TEXT, text_reserved3 TEXT, text_reserved4 TEXT, int_reserved1 INT, int_reserved2 INT, int_reserved3 INT, int_reserved4 INT )",
				"CREATE INDEX IF NOT EXISTS nameIndex ON role_info ( id )" };
	}
}
