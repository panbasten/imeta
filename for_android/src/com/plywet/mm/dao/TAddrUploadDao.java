package com.plywet.mm.dao;

public class TAddrUploadDao {

	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS addr_upload ( id int  PRIMARY KEY , md5 text  , peopleid text  , uploadtime long  , realname text  , realnamepyinitial text  , realnamequanpin text  , username text  , nickname text  , nicknamepyinitial text  , nicknamequanpin text  , type int  , moblie text  , email text  , status int  , reserved1 text  , reserved2 text  , reserved3 int  , reserved4 int  ) ",
				"CREATE INDEX IF NOT EXISTS upload_time_index ON addr_upload ( uploadtime ) " };
	}
}
