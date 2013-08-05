package com.plywet.imeta.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.plywet.imeta.core.exception.ImetaException;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class DateUtils {
	private static final int RECENTLY_DAYS = 15;

	/**
	 * 得到当前失效日期
	 * 
	 * @return
	 */
	public static Date getRecentlyDate() {
		return getRecentlyDate(RECENTLY_DAYS);
	}

	public static Date getRecentlyDate(int recentlyDays) {
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -recentlyDays);
		return calendar.getTime();
	}

	public static Date[] getRangeDate(Date date, int rangeDays) {
		Date[] dates = new Date[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -rangeDays);
		dates[0] = calendar.getTime();

		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, rangeDays);
		dates[1] = calendar.getTime();

		return dates;

	}

	public static XMLGregorianCalendar toXMLCalendar(Calendar cal) {
		if (cal == null) {
			cal = Calendar.getInstance();
		}
		XMLGregorianCalendarImpl xmlCal = new XMLGregorianCalendarImpl();
		// 2011年7月27日新加入
		xmlCal.setYear(cal.get(Calendar.YEAR));
		xmlCal.setMonth(cal.get(Calendar.MONTH) + 1);
		xmlCal.setDay(cal.get(Calendar.DAY_OF_MONTH));
		xmlCal.setHour(cal.get(Calendar.HOUR_OF_DAY));
		xmlCal.setMinute(cal.get(Calendar.MINUTE));
		xmlCal.setSecond(cal.get(Calendar.SECOND));

		// TODO 创建XML日历
		return xmlCal;
	}

	/**
	 * 获得XML格式的当前时间
	 * 
	 * @return
	 */
	public static XMLGregorianCalendar getCurrentXMLCalendar() {
		return toXMLCalendar(Calendar.getInstance());
	}

	/**
	 * 获得XML格式的当前失效时间
	 * 
	 * @return
	 */
	public static XMLGregorianCalendar getRecentlyXMLCalendar() {
		return getRecentlyXMLCalendar(RECENTLY_DAYS);
	}

	public static XMLGregorianCalendar getRecentlyXMLCalendar(int recentlyDays) {
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -recentlyDays);
		XMLGregorianCalendarImpl cal = new XMLGregorianCalendarImpl();
		// TODO 创建当前XML日历
		return cal;
	}

	/**
	 * 得到字符串的日期
	 * 
	 * @param val
	 * @return
	 * @throws ImetaException
	 */
	public static Date str2dat(String val) throws ImetaException {
		return str2dat(val, Const.GENERALIZED_DATE_TIME_FORMAT);
	}

	/**
	 * 得到字符串的日期
	 * 
	 * @param val
	 * @param pattern
	 * @return
	 * @throws ImetaException
	 */
	public static Date str2dat(String val, String pattern) throws ImetaException {
		SimpleDateFormat df = new SimpleDateFormat();

		if (pattern != null)
			df.applyPattern(pattern);

		try {
			return df.parse(val);
		} catch (Exception e) {
			throw new ImetaException("无法由给定字符串转换为日期类型：" + e.toString());
		}
	}

	/**
	 * 得到日期时间字符串
	 * 
	 * @param date
	 *            用于转换的日期
	 * @return
	 */
	public static String dat2str(Date date) {
		return dat2str(date, false);
	}

	/**
	 * 得到日期时间字符串
	 * 
	 * @param date
	 *            用于转换的日期
	 * @param milliseconds
	 *            是否带毫秒，格式"yyyy-MM-dd HH:mm:ss"或者"yyyy-MM-dd HH:mm:ss.SSS"
	 * @return
	 */
	public static String dat2str(Date date, boolean milliseconds) {
		if (milliseconds) {
			return dat2str(date, Const.GENERALIZED_DATE_TIME_FORMAT_MILLIS);
		}
		return dat2str(date, Const.GENERALIZED_DATE_TIME_FORMAT);
	}

	/**
	 * 得到日期时间字符串
	 * 
	 * @param date
	 *            用于转换的日期
	 * @param pattern
	 *            转换模式
	 * @return
	 */
	public static String dat2str(Date date, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 得到当前时间的字符串
	 * 
	 * @return
	 */
	public static String getFormattedDateTimeNow() {
		return dat2str(new Date(), false);
	}

	/**
	 * 得到当前时间的字符串
	 * 
	 * @param milliseconds
	 *            是否带毫秒
	 * @return
	 */
	public static String getFormattedDateTimeNow(boolean milliseconds) {
		return dat2str(new Date(), milliseconds);
	}

}
