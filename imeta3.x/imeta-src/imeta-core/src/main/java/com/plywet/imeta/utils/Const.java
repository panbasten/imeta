package com.plywet.imeta.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.plywet.imeta.i18n.GlobalMessages;

/**
 * 公共类常量类，涉及系统用的的常量<br>
 * 事件常量
 * 
 * @since 1.0 2009-12-17
 * @author 潘巍（Peter Pan）
 * 
 */
public class Const {

	/**
	 * 默认环境的locale（系统默认）
	 */
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();

	public static final String DEFAULT_ICON_ROOT_PATH = "./img/";

	public static final String DEFAULT_ICON_DIR_NODE = "modal/default-document-open.png";

	public static final String DEFAULT_ICON_DIR_LEAF = "modal/default_large_file.png";

	public static final String DEFAULT_ICON_TREE_NODE = "icons/folder.png";

	public static final String DEFAULT_ICON_TREE_LEAF = "icons/page_white.png";

	public static final String DEFAULT_IMAGE16 = "icons/page_white.png";

	public static final String USER_INFO_ENTITY_CLASSPATH = "com.sinosoft.dsp.system.entity.UserInfo";

	/**
	 * 系统有效/无效
	 */
	public static final String SYS_VALID = "VALID";
	public static final String SYS_INVALID = "INVALID";

	/**
	 * 在该操作系统中的系统文件分隔符
	 */
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	/**
	 * 默认的小数分割 （.或者,）
	 */
	public static final char DEFAULT_DECIMAL_SEPARATOR = (new DecimalFormatSymbols(
			DEFAULT_LOCALE)).getDecimalSeparator();

	/**
	 * 默认的分组分割 （.或者,）
	 */
	public static final char DEFAULT_GROUPING_SEPARATOR = (new DecimalFormatSymbols(
			DEFAULT_LOCALE)).getGroupingSeparator();

	/**
	 * 默认的货币符号
	 */
	public static final String DEFAULT_CURRENCY_SYMBOL = (new DecimalFormatSymbols(
			DEFAULT_LOCALE)).getCurrencySymbol();

	/**
	 * 默认的数字格式
	 */
	public static final String DEFAULT_NUMBER_FORMAT = ((DecimalFormat) (NumberFormat
			.getInstance())).toPattern();

	/**
	 * 在该操作系统中的路径分隔符
	 */
	public static final String PATH_SEPARATOR = System
			.getProperty("path.separator");

	/**
	 * CR：在该操作系统中指定的回车
	 */
	public static final String CR = System.getProperty("line.separator");

	/**
	 * 系统版本
	 */
	public static final String VERSION = "2.0.1";

	/**
	 * 系统默认Messages
	 */
	public static final String PKGNM = GlobalMessages.class.getPackage()
			.getName();

	/**
	 * DOSCR：MS-DOS指定的回车
	 */
	public static final String DOSCR = "\n\r";

	/**
	 * 空字符串（""）
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * 环境属性文件名称，属于系统的本身属性的配置文件
	 */
	public static final String ENVIRONMENT_PROPERTIES = "imeta.cfg.properties";

	/**
	 * 在缓冲区满时的等待时间
	 */
	public static final int TIMEOUT_PUT_MILLIS = 50;

	/**
	 * 在缓冲区空时的等待时间
	 */
	public static final int TIMEOUT_GET_MILLIS = 50;

	/**
	 * 通用的date/time格式 see also method StringUtil.getFormattedDateTime()
	 */
	public static final String GENERALIZED_DATE_FORMAT = "yyyy-MM-dd";
	public static final String GENERALIZED_TIME_FORMAT = "HH:mm:ss";
	public static final String GENERALIZED_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String GENERALIZED_DATE_TIME_FORMAT_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * XML的编码格式
	 */
	public static final String XML_ENCODING = "UTF-8";

	/**
	 * 默认的页面显示列数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	public static final int DEFAULT_COLUMN_WIDTH = 100;

	public static final int DEFAULT_MAX_LAZY_SIZE = 50;

	/**
	 * 默认字符串表示的Null（空）字符串值
	 */
	public static final String NULL_STRING = "";

	/**
	 * 默认字符串表示的Null（空）数字值
	 */
	public static final String NULL_NUMBER = "";

	/**
	 * 默认字符串表示的Null（空）日期值
	 */
	public static final String NULL_DATE = "";

	/**
	 * 默认字符串表示的Null（空）大数字值
	 */
	public static final String NULL_BIGNUMBER = "";

	/**
	 * 默认字符串表示的Null（空）布尔值
	 */
	public static final String NULL_BOOLEAN = "";

	/**
	 * 默认字符串表示的Null（空）整型值
	 */
	public static final String NULL_INTEGER = "";

	/**
	 * 默认字符串表示的Null（空）二进制值
	 */
	public static final String NULL_BINARY = "";

	/**
	 * 默认字符串表示的Null（空）未定义值
	 */
	public static final String NULL_NONE = "";

	/**
	 * 错误页面
	 */
	public static final String EVENT_ERROR_PAGES = "errorPages";

	/**
	 * 事件
	 */
	// public static final String EVENT_ADD_SCENE = "addScene";
	// public static final String EVENT_ADD_SCENE_TO_SET = "addSceneToSet";
	// public static final String EVENT_ADD_SCENE_IGNORE = "addSceneIgnore";
	// public static final String EVENT_ADD_SCENE_TO_SET_IGNORE =
	// "addSceneToSetIgnore";
	//
	// public static final String EVENT_ACTIVE_MAINVIEW_TAB_BY_ID =
	// "activeMainviewTabById";
	// public static final String EVENT_CLOSE_MAINVIEW_TAB_BY_ID =
	// "closeMainviewTabById";
	// public static final String EVENT_CREATE_LOGIN_WINDOW =
	// "createLoginWindow";
	// public static final String EVENT_CREATE_OR_UPDATE_WINDOW =
	// "createOrUpdateWindow";
	// public static final String EVENT_REMOVE_WINDOW = "removeWindow";
	// public static final String CHECK_USER_EXPIRED_EVENT = "checkUser";
	// public static final String START_REGISTER_EVENT = "startRegisterEvent";
	// public static final String UPDATE_MAIN_AREA_EVENT = "updateMainArea";
	// public static final String AUTHENTICATED_EVENT = "authenticateEvent";
	// public static final String ADD_ERROR_EVENT = "addErrorEvent";
	// public static final String FILE_DELETED_EVENT = "fileDeleted";
	// public static final String FILE_EDITED_EVENT = "fileEdited";
	// public static final String USER_ADDED_EVENT = "userAddedEvent";
	// public static final String USER_DELETED_EVENT = "userDeletedEvent";
	// public static final String ALBUM_DRAGGED_EVENT = "albumDraggedEvent";
	// public static final String RESOURCE_DRAGGED_EVENT =
	// "resourceDraggedEvent";

	/**
	 * 菜单显示状态
	 */
	public static final String STATE_MENU_ENABLED = "enabled";
	public static final String STATE_MENU_ACTIVE = "active";
	public static final String STATE_MENU_DISPLAY = "display";

	/**
	 * 页面常量
	 */
	public static final String ROOT_SCENE = "root";
	public static final String PORTAL_MASK_LABEL = "portal.mask.label";
	public static final String LAYOUT_PORTAL_ID = "portal";
	public static final String DEFAULT_APPLICATION_ID = "app_default";
	public static final String MAIN_MENU_ID = "navi_mainMenu";
	public static final String FAST_MENU_ID = "navi_fastMenu";

	public static final int CONST_MAINVIEW_TAB_HOME_MODULESET_COLUMN = 2;

	// 结果
	public static final String LOGOUT_OUTCOME = "logout";
	public static final String REGISTER_OUTCOME = "register";
	public static final String MAIN_OUTCOME = "main";
	public static final String INDEX_OUTCOME = "index";

	// 上下文变量
	public static final String FILE_MANAGER_COMPONENT = "fileManager";
	public static final String AVATAR_DATA_COMPONENT = "avatarData";
	public static final String AVATAR_FILE = "avatar.xml";
	public static final int AVATAR_SIZE = 80;
	public static final int DEFAULT_FILE_TYPEVALUE = 0;
	public static final String SLASH = "/";
	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String JPEG = "jpeg";
	public static final String JPG = "JPG";
	public static final String USER_VARIABLE = "user";
	public static final String UPLOAD_ROOT_COMPONENT_NAME = "uploadRoot";
	public static final String UPLOAD_ROOT_PATH_COMPONENT_NAME = "uploadRootPath";
	public static final String PAGE_ERROR_URL = "error.seam";

	// 服务常量
	public static final int MAX_RESULTS = 20;
	public static final String PERCENT = "%";
	public static final String USER_EXIST_QUERY = "user-exist";
	public static final String USER_LOGIN_QUERY = "user-login";
	public static final String LOGIN_PARAMETER = "login";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String USERNAME_PARAMETER = "username";
	public static final String USER_PARAMETER = "user";
	public static final String DATE_PARAMETER = "date";
	public static final String EMAIL_EXIST_QUERY = "email-exist";
	public static final String EMAIL_PARAMETER = "email";
	public static final String REGISTER_LOGIN_NAME_ID = "mainform:loginName";
	public static final String REGISTER_CONFIRM_PASSWORD_ID = "mainform:confirmPassword";
	public static final String REGISTER_EMAIL_ID = "mainform:email";
	public static final String TAG_SUGGEST_QUERY = "tag-suggest";
	public static final String TAG_POPULAR_QUERY = "tag-popular";
	public static final String TAG_PARAMETER = "tag";
	public static final String TAG_BY_NAME_QUERY = "tag-byName";
	public static final String RESOURCE_PATH_EXIST_QUERY = "resource-exist";
	public static final String RESOURCE_IDENTICAL_QUERY = "resource-countIdenticalImages";
	public static final String PATH_PARAMETER = "path";
	public static final String ALBUM_PARAMETER = "album";
	public static final String USER_COMMENTS_QUERY = "user-comments";
	public static final String AUTHOR_PARAMETER = "author";

	/* 通用方法 */

	/**
	 * 判断是否一个字符被认为是空白。<br>
	 * 本系统中认为空格、制表符、换行、回车都认为是空白
	 * 
	 * @param c
	 * @return
	 */
	public static final boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\r' || c == '\n';
	}

	/**
	 * 判断给定的字符串是否只是空白
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean onlySpaces(String str) {
		for (int i = 0; i < str.length(); i++)
			if (!isSpace(str.charAt(i)))
				return false;
		return true;
	}

	/**
	 * 左修剪：移除字符串左边的空白
	 * 
	 * @param str
	 * @return
	 */
	public static String ltrim(String source) {
		if (source == null)
			return null;
		int from = 0;
		while (from < source.length() && isSpace(source.charAt(from)))
			from++;

		return source.substring(from);
	}

	/**
	 * 右修剪：移除字符串右边的空白
	 * 
	 * @param str
	 * @return
	 */
	public static String rtrim(String source) {
		if (source == null)
			return null;

		int max = source.length();
		while (max > 0 && isSpace(source.charAt(max - 1)))
			max--;

		return source.substring(0, max);
	}

	/**
	 * 左右修剪：移除字符串头部和尾部的空白
	 * 
	 * @param str
	 * @return
	 */
	public static final String trim(String str) {
		if (str == null)
			return null;

		int max = str.length() - 1;
		int min = 0;

		while (min <= max && isSpace(str.charAt(min)))
			min++;
		while (max >= 0 && isSpace(str.charAt(max)))
			max--;

		if (max < min)
			return "";

		return str.substring(min, max + 1);
	}

	/**
	 * 右修饰字符串：增加空格直到到达指定长度。<br>
	 * 如果字符串长度大于限制的长度，字符串将被截取。
	 * 
	 * @param ret
	 * @param limit
	 * @return
	 */
	public static final String rightPad(String ret, int limit) {
		if (ret == null)
			return rightPad(new StringBuffer(), limit);
		else
			return rightPad(new StringBuffer(ret), limit);
	}

	/**
	 * 右修饰字符串：增加空格直到到达指定长度。<br>
	 * 如果字符串长度大于限制的长度，字符串将被截取。
	 * 
	 * @param ret
	 * @param limit
	 * @return
	 */
	public static final String rightPad(StringBuffer ret, int limit) {
		int len = ret.length();
		int l;

		if (len > limit) {
			ret.setLength(limit);
		} else {
			for (l = len; l < limit; l++)
				ret.append(' ');
		}
		return ret.toString();
	}

	/**
	 * 检查提供的字符串是否为空。<br>
	 * 当它为null或者长度为0时认为是空的。
	 * 
	 * @param string
	 * @return
	 */
	public static final boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * 检查提供的stringBuffer是否为空。<br>
	 * 当它为null或者长度为0时认为是空的。
	 * 
	 * @param string
	 * @return
	 */
	public static final boolean isEmpty(StringBuffer string) {
		return string == null || string.length() == 0;
	}

	/**
	 * 检查提供的字符串数组是否为空。<br>
	 * 当它为null或者长度为0时认为是空的。
	 * 
	 * @param string
	 * @return
	 */
	public static final boolean isEmpty(String[] strings) {
		return strings == null || strings.length == 0;
	}

	/**
	 * 检查提供的对象数组是否为空。<br>
	 * 当它为null或者长度为0时认为是空的。
	 * 
	 * @param array
	 * @return
	 */
	public static final boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 实现Oracle风格的NVL函数
	 * 
	 * @param source
	 *            源参数
	 * @param def
	 *            默认值，如果源参数为null或者字符串长度为0，返回默认值
	 * @return
	 */
	public static final String NVL(String source, String def) {
		if (source == null || source.length() == 0)
			return def;
		return source;
	}

	public static final boolean isJSEmpty(String string) {
		return isEmpty(string) || "null".equals(string)
				|| "undefined".equals(string);
	}

	public static final int indexOfString(String lookup, String array[]) {
		if (array == null)
			return -1;
		if (lookup == null)
			return -1;

		for (int i = 0; i < array.length; i++) {
			if (lookup.equalsIgnoreCase(array[i]))
				return i;
		}
		return -1;
	}

	public static final int[] indexsOfFoundStrings(String lookup[],
			String array[]) {
		List<Integer> indexesList = new ArrayList<Integer>();
		for (int i = 0; i < lookup.length; i++) {
			int idx = indexOfString(lookup[i], array);
			if (idx >= 0)
				indexesList.add(Integer.valueOf(idx));
		}
		int[] indexes = new int[indexesList.size()];
		for (int i = 0; i < indexesList.size(); i++)
			indexes[i] = (indexesList.get(i)).intValue();
		return indexes;
	}

	public static final int[] indexsOfStrings(String lookup[], String array[]) {
		int[] indexes = new int[lookup.length];
		for (int i = 0; i < indexes.length; i++)
			indexes[i] = indexOfString(lookup[i], array);
		return indexes;
	}

	public static final int indexOfString(String lookup, List<String> list) {
		if (list == null)
			return -1;

		for (int i = 0; i < list.size(); i++) {
			String compare = list.get(i);
			if (lookup.equalsIgnoreCase(compare))
				return i;
		}
		return -1;
	}

	public static final String[] sortStrings(String input[]) {
		Arrays.sort(input);
		return input;
	}

	/**
	 * 通过separator转换string为String数组
	 * <p>
	 * <code>
	 * Example: a;b;c;d    ==>  new String[] { a, b, c, d }
	 * </code>
	 * 
	 * 注意：与String.split()不同的是，该方法没有使用正则表达式
	 * 
	 * @param string
	 * @param separator
	 * @return
	 */
	public static final String[] splitString(String string, String separator) {
		/*
		 * 0123456 Example a;b;c;d --> new String[] { a, b, c, d }
		 */
		// System.out.println("splitString ["+path+"] using ["+separator+"]");
		List<String> list = new ArrayList<String>();

		if (string == null || string.length() == 0) {
			return new String[] {};
		}

		int sepLen = separator.length();
		int from = 0;
		int end = string.length() - sepLen + 1;

		for (int i = from; i < end; i += sepLen) {
			if (string.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
				list.add(NVL(string.substring(from, i), ""));
				from = i + sepLen;
			}
		}

		if (from + sepLen <= string.length()) {
			list.add(NVL(string.substring(from, string.length()), ""));
		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * 通过separator转换string为String数组
	 * <p>
	 * <code>
	 * Example: a;b;c;d    ==  new String[] { a, b, c, d }
	 * </code>
	 * 
	 * @param string
	 * @param separator
	 * @return
	 */
	public static final String[] splitString(String string, char separator) {
		/*
		 * 0123456 Example a;b;c;d --> new String[] { a, b, c, d }
		 */
		// System.out.println("splitString ["+path+"] using ["+separator+"]");
		List<String> list = new ArrayList<String>();

		if (string == null || string.length() == 0) {
			return new String[] {};
		}

		int from = 0;
		int end = string.length();

		for (int i = from; i < end; i += 1) {
			if (string.charAt(i) == separator) {
				list.add(NVL(string.substring(from, i), ""));
				from = i + 1;
			}
		}

		if (from + 1 <= string.length()) {
			list.add(NVL(string.substring(from, string.length()), ""));
		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * 通过separator将路径转换为字符串数组
	 * <p>
	 * <code>
	 *   Example /a/b/c --> new String[] { a, b, c }
	 * </code>
	 * 
	 * @param path
	 * @param separator
	 * @return
	 */
	public static final String[] splitPath(String path, String separator) {
		//
		// Example /a/b/c --> new String[] { a, b, c }
		//
		// 确保"/"被移除
		//
		// Example /a/b/c/ --> new String[] { a, b, c }
		//

		// 检查空路径
		//
		if (path == null || path.length() == 0 || path.equals(separator)) {
			return new String[] {};
		}

		// 移除最后的"/"
		//
		while (path.endsWith(separator)) {
			path = path.substring(0, path.length() - 1);
		}

		int sepLen = separator.length();
		int nr_separators = 1;
		int from = path.startsWith(separator) ? sepLen : 0;

		for (int i = from; i < path.length(); i += sepLen) {
			if (path.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
				nr_separators++;
			}
		}

		String spath[] = new String[nr_separators];
		int nr = 0;
		for (int i = from; i < path.length(); i += sepLen) {
			if (path.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
				spath[nr] = path.substring(from, i);
				nr++;

				from = i + sepLen;
			}
		}
		if (nr < spath.length) {
			spath[nr] = path.substring(from);
		}

		// 
		// a --> { a }
		//
		if (spath.length == 0 && path.length() > 0) {
			spath = new String[] { path };
		}

		return spath;
	}

	/**
	 * 排序字符串数组，确保唯一字符串
	 * 
	 * @param strings
	 * @return 排序的唯一存在的数组
	 */
	public static final String[] getDistinctStrings(String[] strings) {
		if (strings == null)
			return null;
		if (strings.length == 0)
			return new String[] {};

		String[] sorted = sortStrings(strings);
		List<String> result = new ArrayList<String>();
		String previous = "";
		for (int i = 0; i < sorted.length; i++) {
			if (!sorted[i].equalsIgnoreCase(previous)) {
				result.add(sorted[i]);
			}
			previous = sorted[i];
		}

		return result.toArray(new String[result.size()]);
	}

	/**
	 * 用其他值替代原始字符串中的值
	 * 
	 * @param string
	 *            原始字符串
	 * @param repl
	 *            要替换的文本
	 * @param with
	 *            替换的文本
	 * @return
	 */
	public static final String replace(String string, String repl, String with) {
		StringBuffer str = new StringBuffer(string);
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.substring(i).startsWith(repl)) {
				str.delete(i, i + repl.length());
				str.insert(i, with);
			}
		}
		return str.toString();
	}

	/**
	 * 四舍五入双精度数f到任意位数
	 * 
	 * @param f
	 * @param places
	 *            小数位数
	 * @return
	 */

	public static final double round(double f, int places) {
		java.math.BigDecimal bdtemp = java.math.BigDecimal.valueOf(f);
		bdtemp = bdtemp.setScale(places, java.math.BigDecimal.ROUND_HALF_EVEN);
		return bdtemp.doubleValue();
	}

	/**
	 * 将String转换成integer。如果转换出错，返回默认值
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static final int toInt(String str, int def) {
		int retval;
		try {
			retval = Integer.parseInt(str);
		} catch (Exception e) {
			retval = def;
		}
		return retval;
	}

	/**
	 * 将String转换成long integer。如果转换出错，返回默认值
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static final long toLong(String str, long def) {
		long retval;
		try {
			retval = Long.parseLong(str);
		} catch (Exception e) {
			retval = def;
		}
		return retval;
	}

	/**
	 * 将String转换成double。如果转换出错，返回默认值
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static final double toDouble(String str, double def) {
		double retval;
		try {
			retval = Double.parseDouble(str);
		} catch (Exception e) {
			retval = def;
		}
		return retval;
	}

	/**
	 * 返回一个指定异常的堆栈轨迹字符串
	 * 
	 * @param e
	 *            异常
	 * @return
	 */
	public static final String getStackTracker(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		String string = stringWriter.getBuffer().toString();
		try {
			stringWriter.close();
		} catch (IOException ioe) {
		}
		return string;
	}

	/**
	 * 转换JSON数组为String数组
	 * 
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static final String[] convertJSONArrayToStringArray(JSONArray ja)
			throws JSONException {
		int n = ja.length();
		String[] rtn = new String[n];
		for (int i = 0; i < n; i++) {
			rtn[i] = ja.getString(i);
		}
		return rtn;
	}

	/**
	 * 转换JSON数组为int数组
	 * 
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static final int[] convertJSONArrayToIntArray(JSONArray ja)
			throws JSONException {
		int n = ja.length();
		int[] rtn = new int[n];
		for (int i = 0; i < n; i++) {
			rtn[i] = ja.getInt(i);
		}
		return rtn;
	}

	/**
	 * 将Map转换成JSON对象
	 * 
	 * @param data
	 * @return
	 */
	public static String convertMaptoJSONObject(Map<String, String> data) {

		JSONObject dataToJSON = new JSONObject();
		try {
			String key;
			for (Iterator<String> iter = data.keySet().iterator(); iter
					.hasNext();) {
				key = iter.next();
				dataToJSON.put(key, data.get(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dataToJSON.toString();
	}

	/**
	 * 转换字符串数组为一个字符串
	 * 
	 * @param data
	 *            待转换字符串数组
	 * @param split
	 *            分隔符
	 * @param hasQuot
	 *            是否有引号
	 * @return
	 */
	public static String convertStringArrayToString(String[] data,
			String split, boolean hasQuot) {
		if (split == null)
			split = "";
		if (data != null && data.length > 0) {
			StringBuffer rtn = new StringBuffer();
			for (String d : data) {
				if (hasQuot)
					rtn.append("'");
				rtn.append(d);
				if (hasQuot)
					rtn.append("'");
				rtn.append(split);
			}
			return rtn.toString().substring(0, rtn.length() - split.length());
		}
		return null;
	}

	/**
	 * 获得图标
	 * 
	 * @param single
	 *            是否采用通过数据对象单独定义的图标
	 * @param icon
	 *            数据对象定义图标图标
	 * @param customIcon
	 *            外部自定义图标
	 * @param defaultIcon
	 *            系统默认图标
	 * @return
	 */
	public static final String getIcon(String icon, String customIcon,
			String defaultIcon) {
		if (!Const.isEmpty(icon)) {
			return DEFAULT_ICON_ROOT_PATH + icon;
		}
		if (!Const.isEmpty(customIcon)) {
			return DEFAULT_ICON_ROOT_PATH + customIcon;
		}
		return DEFAULT_ICON_ROOT_PATH + defaultIcon;
	}

}
