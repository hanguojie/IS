package com.tospur.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author liukai
 */
public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static final String EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

	public static Date parseDateFromString(String input) {
		return parseDateFromString(input, null);
	}

	public static Date parseDateFromString(String input, String simpleformat) {
		if (StringUtils.isEmpty(simpleformat)) {
			simpleformat = "yyyy-MM-dd";
		}
		if (StringUtils.isEmpty(input)) {
			return null;
		}
		try {
			DateFormat format = new SimpleDateFormat(simpleformat);
			Date date = format.parse(input);
			return date;
		} catch (Exception e) {
			LOGGER.error("input time format error , input: " + input + ", format:" + simpleformat, e);
			throw new RuntimeException();
		}
	}

	public static String formatTime(long input, String simpleformat) {
		if (StringUtils.isEmpty(simpleformat)) {
			simpleformat = "yyyy-MM-dd";
		}
		SimpleDateFormat formate = new SimpleDateFormat(simpleformat);
		return formate.format(input);
	}

	@SuppressWarnings("deprecation")
	public static Date getTimeByDateFormat(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		String dateString = dateFormat.format(date);
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			LOGGER.error("time format error , input date : " + date.toLocaleString() + ", input format : " + format, e);
			throw new RuntimeException();
		}
	}

	/**
	 * 获取当前日期的前后几天的日期
	 *
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDayBydateAndKey(Date date, int day) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	public static String dateToString(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String dateToStringToIos(Date date) {
		return dateToString(new Date(), EXPANDED_DATE_FORMAT);
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 按类型获取日、周、月的开始结束时间
	 * 
	 * @param timeType
	 *            时间类型 1、天 2、周 3、月
	 * @param isNow
	 *            true为今天，false取前天、上周、上月的开始结束时间
	 * @return
	 */
	public static Map<String, Date> getQueryDate(Integer timeType, boolean isNow) {
		Calendar cal = Calendar.getInstance();
		int field = 0, field2 = 0, valueStar = 0, valueEnd = 0, amount = -1;
		Map<String, Date> map = new HashMap<String, Date>();
		if (timeType == 1) {
			field2 = Calendar.DATE;
			cal.setTime(new Date());
		} else if (timeType == 2) {
			field = Calendar.DAY_OF_WEEK;
			field2 = Calendar.WEEK_OF_YEAR;
			valueStar = Calendar.MONDAY;
			valueEnd = Calendar.SUNDAY;
		} else if (timeType == 3) {
			field = Calendar.DATE;
			field2 = Calendar.MONTH;
			valueStar = cal.getActualMinimum(Calendar.DATE);
			valueEnd = cal.getActualMaximum(Calendar.DATE);
		}
		if (timeType == 2 || timeType == 3) {
			cal.set(field, valueStar);
		}

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (!isNow) {
			cal.add(field2, amount);
		}
		map.put("starDate", cal.getTime());
		if (timeType == 2 || timeType == 3) {
			cal.set(field, valueEnd);
		}
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		if (timeType == 2) {
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		map.put("endDate", cal.getTime());
		return map;
	}

	public static String StringFormat(String str, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = f.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return f.format(date);
	}

	/**
	 * 根据日期取得对应周周一日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(Date date) {
		Calendar monday = Calendar.getInstance();
		monday.setTime(date);
		monday.setFirstDayOfWeek(Calendar.MONDAY);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return monday.getTime();
	}

	/**
	 * 获取月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得季度第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfSeason(Date date) {
		return getFirstDateOfMonth(getSeasonDate(date)[0]);
	}

	/**
	 * 取得季度月
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getSeasonDate(Date date) {
		Date[] season = new Date[3];
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int nSeason = getSeason(date);
		if (nSeason == 1) {// 第一季度
			c.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = c.getTime();
		} else if (nSeason == 2) {// 第二季度
			c.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MAY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = c.getTime();
		} else if (nSeason == 3) {// 第三季度
			c.set(Calendar.MONTH, Calendar.JULY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = c.getTime();
		} else if (nSeason == 4) {// 第四季度
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = c.getTime();
		}
		return season;
	}

	/**
	 * 
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {
		int season = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.valueOf(String.valueOf(between_days));
	}
}
