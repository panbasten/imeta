package com.plywet.mm.dao;

public class TInviteFriendOpenDao {
	public static final String[] sqls;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS invitefriendopen ( username text  PRIMARY KEY , friendtype int  , updatetime int  , reserved1 int  , reserved2 int  , reserved3 text  , reserved4 text  ) " };
	}
}
