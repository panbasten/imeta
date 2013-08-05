package com.plywet.mm.dao;

public class TImgFlagDao {
	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS img_flag ( username VARCHAR(40) PRIMARY KEY , imgflag int , lastupdatetime int , reserved1 text ,reserved2 text ,reserved3 int ,reserved4 int )",
				"CREATE UNIQUE INDEX IF NOT EXISTS usernameindex ON img_flag ( username )" };
	}
}
