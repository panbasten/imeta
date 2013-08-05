package com.plywet.mm.database;

import com.plywet.mm.platformtools.Log;

import android.content.ContentValues;
import android.database.Cursor;

public class DBVerInfoTable {

	private static final String PKG = "MicroMsg.DBVerInfoTable";

	public static boolean checkVersion(SqliteDB db) {

		if (db == null) {
			return true;
		}

		boolean rtn = false;

		String sql = "CREATE TABLE IF NOT EXISTS DBInfoTableV2 ( key TEXT, version TEXT )";
		if (!db.execSQL(sql)) {
			replaceVersion(db);
			rtn = false;
		}

		String ver = "0";
		Cursor c = db.query("DBInfoTableV2", null, null, null);
		if (c != null && c.moveToFirst()) {
			ver = c.getString(1);
		}
		c.close();

		Log.debug(PKG, "dbVersion" + ver);

		if ("1.1".equals(ver)) {
			rtn = true;
		} else if ("".equals(ver) || "0".equals(ver)) {
			replaceVersion(db);
			rtn = true;
		} else {
			String[] vers = ver.split("\\.");
			if (vers.length != 2) {
				replaceVersion(db);
				rtn = true;
			} else {
				int i = Integer.parseInt(vers[0]);
				Log.debug(PKG, "majorVerAtDB : " + i);
				if (i != 0 && i > 1) {
					rtn = false;
				} else {
					replaceVersion(db);
					rtn = true;
				}
			}
		}

		return rtn;
	}

	private static void replaceVersion(SqliteDB db) {
		ContentValues values = new ContentValues();
		values.put("version", "1.1");
		db.replace("DBInfoTableV2", null, values);
	}
}
