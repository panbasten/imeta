package com.plywet.mm.dao;

public class TVUserInfoDao {
	public static final String[] sqls;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS vuserpicinfo ( id int  PRIMARY KEY , desc text  , reserved1 int  , reserved2 int  , reserved3 text  , reserved4 text  ) " };
	}
}
