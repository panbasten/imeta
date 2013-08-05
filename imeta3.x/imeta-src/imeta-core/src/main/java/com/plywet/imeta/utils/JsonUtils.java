package com.plywet.imeta.utils;

import java.util.Date;
import java.util.Map;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.plywet.imeta.core.exception.ImetaException;

public class JsonUtils {

	public static void putStringAttribute(JSONObject jo,
			Map<String, Object> map, String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (map.containsKey(f)) {
					if (map.get(f) instanceof String) {
						jo.put(f, map.get(f));
					} else {
						jo.put(f, String.valueOf(map.get(f)));
					}
				}
			}
	}

	public static void getStringAttribute(Map<String, Object> map,
			JSONObject jo, String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (jo.has(f)) {
					if (jo.opt(f) instanceof String) {
						map.put(f, jo.opt(f));
					} else {
						map.put(f, String.valueOf(jo.opt(f)));
					}
				}
			}
	}

	public static void putLongAttribute(JSONObject jo, Map<String, Object> map,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (map.containsKey(f)) {
					if (map.get(f) instanceof Long) {
						jo.put(f, map.get(f));
					} else if (map.get(f) instanceof String) {
						jo.put(f, Long.valueOf((String) map.get(f)));
					}
				}
			}
	}

	public static void getLongAttribute(Map<String, Object> map, JSONObject jo,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (jo.has(f)) {
					if (jo.opt(f) instanceof Long) {
						map.put(f, jo.opt(f));
					} else if (jo.opt(f) instanceof String) {
						map.put(f, Long.valueOf(jo.optString(f)));
					}
				}
			}
	}

	public static void putIntAttribute(JSONObject jo, Map<String, Object> map,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (map.containsKey(f)) {
					if (map.get(f) instanceof Integer) {
						jo.put(f, map.get(f));
					} else if (map.get(f) instanceof String) {
						jo.put(f, Long.valueOf((String) map.get(f)));
					}
				}
			}
	}

	public static void getIntAttribute(Map<String, Object> map, JSONObject jo,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (jo.has(f)) {
					if (jo.opt(f) instanceof Integer) {
						map.put(f, jo.opt(f));
					} else if (jo.opt(f) instanceof String) {
						map.put(f, Integer.valueOf(jo.optString(f)));
					}
				}
			}
	}

	public static void putBooleanAttribute(JSONObject jo,
			Map<String, Object> map, String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (map.containsKey(f)) {
					if (map.get(f) instanceof Boolean) {
						jo.put(f, map.get(f));
					} else if (map.get(f) instanceof String) {
						jo.put(f, Boolean.valueOf((String) map.get(f)));
					}
				}
			}
	}

	public static void getBooleanAttribute(Map<String, Object> map,
			JSONObject jo, String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (jo.has(f)) {
					if (jo.opt(f) instanceof Boolean) {
						map.put(f, jo.opt(f));
					} else if (jo.opt(f) instanceof String) {
						map.put(f, Boolean.valueOf(jo.optString(f)));
					}
				}
			}
	}

	public static void putDateAttribute(JSONObject jo, Map<String, Object> map,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (map.containsKey(f)) {
					if (map.get(f) instanceof Date) {
						jo.put(f, DateUtils.dat2str((Date) map.get(f), true));
					} else if (map.get(f) instanceof String) {
						jo.put(f, (String) map.get(f));
					}
				}
			}
	}

	public static void getDateAttribute(Map<String, Object> map, JSONObject jo,
			String[] fields) throws JSONException {
		if (fields != null)
			for (String f : fields) {
				if (jo.has(f)) {
					if (jo.opt(f) instanceof String) {
						try {
							map.put(f, DateUtils.str2dat(jo.getString(f),
									Const.GENERALIZED_DATE_TIME_FORMAT_MILLIS));
						} catch (ImetaException e) {
							map.put(f, jo.getString(f));
						}
					}
				}
			}
	}

}
