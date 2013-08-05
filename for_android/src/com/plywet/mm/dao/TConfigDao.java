package com.plywet.mm.dao;

import com.plywet.mm.database.DBListenerProxy;
import com.plywet.mm.database.SqliteDB;

public class TConfigDao extends DBListenerProxy {

	public static final String[] sqls;

	private MMPropertiesProvider systemInfoProps = null;

	static {
		sqls = new String[] { "CREATE TABLE IF NOT EXISTS userinfo ( id INTEGER PRIMARY KEY, type INT, value TEXT )" };
	}

	public TConfigDao(SqliteDB db) {
		this.systemInfoProps = new MMDBProperties(db);
	}

	public TConfigDao(String path) {
		this.systemInfoProps = new MMFileProperties(path);
	}

	public final Object get(int key) {
		return this.systemInfoProps.get(key);
	}

	public final void put(int key, Object value) {
		this.systemInfoProps.put(key, value);
		this.deal("" + key);
	}

}
