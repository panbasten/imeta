package com.plywet.mm.platformtools;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Utils {

	private static final String PKG = "MicroMsg.Utils";

	private static final TimeZone local;

	static {
		local = TimeZone.getTimeZone("GMT");
	}

	public static String defaultString(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public static String defaultString(String s, String def) {
		if (s == null) {
			return def;
		}
		return s;
	}

	public static int defaultInteger(Integer i) {
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}

	public static int defaultInteger(Integer i, int def) {
		if (i == null) {
			return def;
		}
		return i.intValue();
	}

	public static int getInteger(Object obj) {
		int i = 0;
		if (obj == null) {
			return i;
		}

		if (obj instanceof Integer) {
			return ((Integer) obj).intValue();
		} else if (obj instanceof Long) {
			return ((Long) obj).intValue();
		}

		return i;
	}

	public static long defaultLong(Long l) {
		if (l == null) {
			return 0L;
		}
		return l.longValue();
	}

	public static long defaultLong(Long l, long def) {
		if (l == null) {
			return def;
		}
		return l.longValue();
	}

	public static boolean defaultBoolean(Boolean b) {
		if (b == null) {
			return true;
		}
		return b.booleanValue();
	}

	public static boolean defaultBoolean(Boolean b, boolean def) {
		if (b == null) {
			return def;
		}
		return b.booleanValue();
	}

	public static long getTime() {
		return System.currentTimeMillis();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public static Object resolver(int type, String value) {
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

	public static int getType(Object obj) {
		if (obj == null) {
			return -1;
		}
		int type = 1;
		if (obj instanceof Integer) {
			type = 1;
		} else if (obj instanceof Long) {
			type = 2;
		} else if (obj instanceof String) {
			type = 3;
		} else if (obj instanceof Boolean) {
			type = 4;
		} else if (obj instanceof Float) {
			type = 5;
		} else if (obj instanceof Double) {
			type = 6;
		} else {
			Log.error("PKG", "unresolve failed, unknown type="
					+ obj.getClass().toString());
			type = 0;
		}

		return type;
	}

	public static List stringArray2List(String[] strs) {
		if (strs == null || strs.length == 0) {
			return null;
		} else {
			List rtn = new ArrayList();
			for (String str : strs) {
				rtn.add(str);
			}
			return rtn;
		}
	}

	public static String filterString(String str) {
		if (str != null) {
			str = str.replace("\\[", "[[]").replace("%", "").replace("\\^", "")
					.replace("'", "").replace("\\{", "").replace("\\}", "")
					.replace("\"", "");
		}
		return str;
	}

}
