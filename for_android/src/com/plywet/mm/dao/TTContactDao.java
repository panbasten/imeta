package com.plywet.mm.dao;

public class TTContactDao {
	public static final String[] sqls;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS tcontact ( username text  PRIMARY KEY , extupdateseq long  , imgupdateseq long  , needupdate int  , reserved1 int  , reserved2 int  , reserved3 int  , reserved4 int  , reserved5 text  , reserved6 text  , reserved7 text  , reserved8 text  ) " };
	}
}
