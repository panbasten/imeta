package com.plywet.mm.dao;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.plywet.mm.database.SqliteDB;
import com.plywet.mm.platformtools.Utils;

public class MMDBProperties implements MMPropertiesProvider {

	private SqliteDB db;

	private Map props = new HashMap();

	public MMDBProperties(SqliteDB db) {
		this.db = db;
	}

	@Override
	public Object get(int key) {
		DBPropertyObj prop = (DBPropertyObj) this.props.get(Integer
				.valueOf(key));

		if (prop == null) {
			prop = new DBPropertyObj(this);
			Cursor cursor = this.db.query("userinfo", "id=" + key, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				prop.type = cursor.getInt(1);
				prop.value = cursor.getString(2);
			}
			cursor.close();
			this.props.put(Integer.valueOf(key), prop);
		}
		if (prop.type != -1) {
			return Utils.resolver(prop.type, prop.value);
		}

		return null;
	}

	@Override
	public void put(int key, Object value) {
		this.props.remove(Integer.valueOf(key));
		if (value == null) {
			this.db.delete("userinfo", "id=" + key, null);
			return;
		}

		ContentValues cv = new ContentValues();
		cv.put("id", Integer.valueOf(key));
		cv.put("value", value.toString());
		cv.put("type", Integer.valueOf(Utils.getType(value)));

	}

}
