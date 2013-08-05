package com.plywet.mm.dao;

public class TQqGroupDao {
	public static final String[] sqls;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS qqgroup ( grouopid int PRIMARY KEY,membernum int,weixinnum int,insert_time int,lastupdate_time int,needupdate int,updatekey text,groupname text,reserved1 text ,reserved2 text ,reserved3 int ,reserved4 int )" };
	}
}
