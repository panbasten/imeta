package com.plywet.mm.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.plywet.mm.platformtools.Log;
import com.plywet.mm.platformtools.Utils;
import com.plywet.mm.system.Timer;

public class SqliteDB {

	private static final String PKG = "MicroMsg.SqliteDB";

	private static Map<Integer, MMTableCreatorInterface> tableMetas = new HashMap<Integer, MMTableCreatorInterface>();

	private static int ticket = 0;

	private SQLiteDatabase db = null;

	public static void put(MMTableCreatorInterface c, int hashCode) {
		tableMetas.put(Integer.valueOf(hashCode), c);
	}

	private static boolean checkTables(SqliteDB db) {

		Iterator<MMTableCreatorInterface> iter = tableMetas.values().iterator();
		if (iter == null || !iter.hasNext()) {
			return false;
		}

		int t = db.beginTransaction();
		while (iter.hasNext()) {
			String[] sqls = iter.next().getCreateTableSqls();
			int len = sqls.length;
			for (int i = 0; i < len; i++) {
				db.execSQL(sqls[i]);
			}
		}
		db.setTransactionSuccessful(t);
		db.endTransaction(t);

		return true;
	}

	/**
	 * 开始事务
	 * 
	 * @return
	 */
	public int beginTransaction() {
		if (ticket != 0) {
			Log.error(PKG, "ERROR beginTransaction transactionTicket:" + ticket);
			return -1;
		}

		DBTestCounter.test("beginTransaction: ");

		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		this.db.beginTransaction();
		ticket = (int) Utils.getTime();
		Log.verbose(PKG, "beginTransaction succ ticket:" + ticket);
		return ticket;
	}

	/**
	 * 结束事务
	 * 
	 * @param t
	 * @return
	 */
	public int endTransaction(int t) {
		if (ticket != t) {
			Log.error(PKG, "ERROR endTransaction ticket:" + t
					+ " transactionTicket:" + ticket);
			return -1;
		}

		DBTestCounter.test("endTransaction: ");

		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		this.db.endTransaction();
		Log.verbose(PKG, "endTransaction succ ticket:" + ticket);
		ticket = 0;
		return ticket;
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		Timer t = new Timer();

		int rtn;

		try {
			rtn = this.db.update(table, values, whereClause, whereArgs);
			DBTestCounter.test("update [" + t.interval() + "] " + table);
		} catch (Exception e) {
			Log.error(PKG, "update Error :" + e.getMessage());
			rtn = -1;
		}

		return rtn;
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);

		int rtn;

		try {
			rtn = this.db.delete(table, whereClause, whereArgs);
		} catch (Exception e) {
			Log.error(PKG, "delete Error :" + e.getMessage());
			rtn = -1;
		}

		return rtn;
	}

	public long insert(String table, String column, ContentValues values) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		Timer t = new Timer();

		long rtn;

		try {
			rtn = this.db.insert(table, column, values);
			DBTestCounter.test("insert [" + t.interval() + "] " + table);
		} catch (Exception e) {
			rtn = -1L;
		}
		return rtn;
	}

	public Cursor query(String table, String selection, String[] selectionArgs,
			String orderBy) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		Timer t = new Timer();

		Cursor c = this.db.query(table, null, selection, selectionArgs, null,
				null, orderBy);

		long t1 = t.interval();

		DBTestCounter.test(c);

		long t2 = t.interval();

		String str = table + ";" + selection + ";";
		if (selectionArgs != null && selectionArgs.length > 0) {
			for (String arg : selectionArgs)
				str += arg + ",";
			str = str.substring(0, str.length() - 1);
			str += ";";
		}
		if (orderBy != null && !"".equals(orderBy)) {
			str += orderBy + ";";
		}

		DBTestCounter.test("query[" + t1 + "," + t2 + "][" + str + "]\n");
		return c;
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);
		Timer t = new Timer();

		Cursor c = this.db.rawQuery(sql, selectionArgs);

		long t1 = t.interval();

		DBTestCounter.test(c);

		long t2 = t.interval();

		String str = sql + ";";
		if (selectionArgs != null && selectionArgs.length > 0) {
			for (String arg : selectionArgs)
				str += arg + ",";
			str = str.substring(0, str.length() - 1);
			str += ";";
		}

		DBTestCounter.test("rawQuery[" + t1 + "," + t2 + "][" + str + "]");
		return c;
	}

	public int setTransactionSuccessful(int t) {
		if (ticket != t) {
			Log.error(PKG, "ERROR setTransactionSuccessful ticket:" + t
					+ " transactionTicket:" + ticket);
			return -1;
		}

		Assert.assertTrue("SQLiteDatabase is null", this.db != null);

		this.db.setTransactionSuccessful();

		Log.verbose(PKG, "setTransactionSuccessful succ transactionTicket:"
				+ ticket);

		return 0;
	}

	public long replace(String table, String nullColumnHack,
			ContentValues initialValues) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);

		long rtn;

		try {
			rtn = this.db.replace(table, nullColumnHack, initialValues);
		} catch (Exception e) {
			Log.error(PKG, "repalce  Error :" + e.getMessage());
			rtn = -1L;
		}
		return rtn;
	}

	public boolean execSQL(String sql) {
		Assert.assertTrue("SQLiteDatabase is null", this.db != null);

		Assert.assertTrue("sql is null ", Utils.isEmpty(sql));

		Timer t = new Timer();

		try {
			this.db.execSQL(sql);
			DBTestCounter.test("execSQL[" + t.interval() + "][" + sql + "]");
			return true;
		} catch (Exception e) {
			Log.error(PKG, "execSQL Error :" + e.getMessage());
			return false;
		}
	}

	@Override
	protected void finalize() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	public boolean openOrCreateDatabase(String path) {
		Assert.assertTrue("create SqliteDB dbCachePath == null ",
				Utils.isEmpty(path));
		Log.info(PKG, "InitDb :" + path);

		if (this.db != null) {
			this.db.close();
		}

		try {
			this.db = SQLiteDatabase.openOrCreateDatabase(path, null);
			if (DBVerInfoTable.checkVersion(this)) {
				Log.error(PKG, "check DB version failed");
				return false;
			}

			if (!checkTables(this)) {
				Log.error(PKG, "check Tables failed");
				return false;
			}

			return true;
		} catch (Exception e) {
			Log.error(PKG, "createDB failed: " + e.getMessage());
			return false;
		}

	}
}
