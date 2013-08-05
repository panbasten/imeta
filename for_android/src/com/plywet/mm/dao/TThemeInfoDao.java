package com.plywet.mm.dao;

public class TThemeInfoDao {
	public static final String[] sqls;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS themeinfo ( themeid int  PRIMARY KEY , themever int  , themesize int  , offset int  , status int  , themename text  , packname text  , reserved1 int  , reserved2 int  , reserved3 text  , reserved4 text  ) " };
	}
}
