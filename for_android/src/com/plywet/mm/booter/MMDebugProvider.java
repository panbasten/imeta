package com.plywet.mm.booter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.plywet.mm.platformtools.Log;

/**
 * 从服务器上获得调试参数 ！！！后门
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 上午10:21:58
 */
public class MMDebugProvider {

	public static final Uri uri = Uri
			.parse("content://com.tencent.mm.coolassist.debugprovider/config");

	private static final String PKG = "MicroMsg.Debugger";

	private String[] fields;

	private Map<String, Object> debugs;

	public MMDebugProvider(Context context) {
		fields = new String[] { "_id", "key", "type", "value" };
		debugs = new HashMap<String, Object>();
		Cursor cursor = context.getContentResolver().query(uri, fields, null,
				null, null);
		if (cursor != null) {
			Log.debug(PKG, "debugger attached");
			int keyCol = cursor.getColumnIndex("key");
			int typeCol = cursor.getColumnIndex("type");
			int valueCol = cursor.getColumnIndex("value");

			while (cursor.moveToNext()) {
				Object row = resolver(cursor.getInt(typeCol),
						cursor.getString(valueCol));
				this.debugs.put(cursor.getString(keyCol), row);
			}
			cursor.close();
		}
	}

	private Object resolver(int type, String value) {
		Object rtn = null;
		switch (type) {
		case 1:
			rtn = Integer.valueOf(value);
			break;
		case 2:
			rtn = Long.valueOf(value);
			break;
		case 3:
			break;
		case 4:
			rtn = Boolean.valueOf(value);
			break;
		case 5:
			rtn = Float.valueOf(value);
			break;
		case 6:
			rtn = Double.valueOf(value);
			break;
		default:
			Log.error(PKG, "unknown type");
			break;
		}
		return rtn;
	}

	public String getString(String key) {
		Object value = this.debugs.get(key);
		if (value instanceof String) {
			Log.debug(PKG, "getString(): key=" + key + ", value=" + value);
			return (String) value;
		} else {
			Log.error(PKG, "getString() failed");
			return null;
		}
	}

	public Integer getInteger(String key) {
		Object value = this.debugs.get(key);
		if (value instanceof Integer) {
			Log.debug(PKG, "getInteger(): key=" + key + ", value=" + value);
			return (Integer) value;
		} else {
			Log.error(PKG, "getInteger() failed");
			return null;
		}
	}

	public Boolean getBoolean(String key) {
		Object value = this.debugs.get(key);
		if (value instanceof Boolean) {
			Log.debug(PKG, "getBoolean(): key=" + key + ", value=" + value);
			return (Boolean) value;
		} else {
			Log.error(PKG, "getBoolean() failed");
			return null;
		}
	}

}
