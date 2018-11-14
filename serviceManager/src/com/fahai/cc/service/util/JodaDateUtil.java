package com.fahai.cc.service.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * 
 * @ClassName: JodaDateUtil
 * @Description: 时间格式化工具类
 * @author: wudapeng;
 * @date: 2017年3月21日 下午6:01:28
 */
public class JodaDateUtil {
	private JodaDateUtil() {
	}

	public static SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** 年月日的日期格式化 */
	public static String df_YYYY_MM_DD = "yyyy-MM-dd";

	/** 年月的日期格式化 */
	public static String df_yyyy_MM = "yyyy-MM";

	/** 年月日时分秒的日期格式化 */
	public static String df_MM_dd_yyyy_HH_mm_ss_sprit = "MM/dd/yyyy HH:mm:ss";
	/** 年月日时分秒的日期格式化 */
	public static String df_MM_dd_yyyy_sprit = "MM/dd/yyyy";

	/** 年月日时分秒大写HH表示的是24小时进行限制 */
	public static String df_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

	/** 年月日时分秒大写HH表示的是12小时进行限制 */
	public static String df_yyyy_MM_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";

	/** 年月日时分秒大写HH表示的是12小时进行限制 */
	public static String df_yyyy_MM_dd = "yyyy-MM-dd";

	/** 年月日时分秒大写HH表示的是12小时进行限制 */
	public static String df_yyyy_MM_hh_mm = "yyyy-MM-dd hh:mm";

	/** 年月日时分秒大写HH表示的是12小时进行限制 */
	public static String df_yyyy_MM_HH_mm = "yyyy-MM-dd HH:mm";

	public static String df_yyyyMMddhhmmss = "yyyyMMddhhmmss";

	public static String df_yyyyMMddHHmmss = "yyyyMMddHHmmss";

	public static String df_yyyyMMddHHmm = "yyyyMMddHHmm";
	public static String dfyyyyMM = "yyyyMM";

	/** 小时分钟的格式化方法 */
	public static String df_HH_mm = "HH:mm";

	public static String df_hh_mm = "hh:mm";

	/** 小时分钟的格式化方法 */
	public static String df_HH_mm_ss = "HH:mm:ss";
	public static String df_hh_mm_ss = "hh:mm:ss";
	/** 亚洲上海时区 */
	public static DateTimeZone datetime_zone_id = DateTimeZone.forID(buildMap().get("CTT"));


	public static String getCurrentTimeByString() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.toString(df_yyyy_MM_dd_HH_mm_ss);
	}

	public static Date getCurrentTimeByDate() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.toDate();
	}

	public static DateTime getCurrentTimeByDateTime() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime;
	}
	
	public static String getCurrentDay() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.toString(df_yyyy_MM_dd);
	}

	/**
	 * 
	 * @Title: getCurrentDay
	 * @Description:获取当前日期的几天前日期 yyyy_MM_dd
	 * @param num
	 * @return
	 * @return: String
	 */
	public static String getCurrentDay(Integer num) {
		DateTime d = DateTime.now(datetime_zone_id);
		DateTime dateTimeBefore1 = JodaDateUtil.plusDate(d, 0, 0, num, 0, 0, 0);
		dateTimeBefore1.toString(JodaDateUtil.df_yyyy_MM_dd);
		return dateTimeBefore1.toString(JodaDateUtil.df_yyyy_MM_dd);
	}

	/**
	 * 
	 * @Title: getCurrentDay
	 * @Description:获取当前日期的几天前日期 yyyy_MM_dd
	 * @param num
	 * @param day
	 * @return
	 * @return: String
	 */
	public static String getCurrentDay(Integer num, String day) {
		DateTime d = DateTime.parse(day);
		DateTime dateTimeBefore1 = JodaDateUtil.plusDate(d, 0, 0, num, 0, 0, 0);
		dateTimeBefore1.toString(JodaDateUtil.df_yyyy_MM_dd);
		return dateTimeBefore1.toString(JodaDateUtil.df_yyyy_MM_dd);
	}


	public static String getCurrentDayDirect() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.toString(dfyyyyMM);
	}

	public static String getCurrentDayDirect(Date date) {
		String xxx = parseDateToString(date, df_yyyy_MM_dd_HH_mm_ss);
		DateTime dateTime = JodaDateUtil.parseStringToDateTime(xxx, df_yyyy_MM_dd_HH_mm_ss);
		return dateTime.toString(dfyyyyMM);
	}

	public static String parseDateToString(Date date, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmts);
		String str = sdf.format(date);
		return str;
	}

	public static String getDateTimeByStringFmt(String formatter) {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.toString(formatter);
	}

	/**
	 * @return 获取当前是第几个月
	 */
	public static Integer getDayOfMonth() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.getDayOfMonth();
	}

	/**
	 * @return 获取星期几
	 */
	public static Integer getDayOfWeek() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.getDayOfWeek();
	}

	/**
	 * @return 一年中的第几天
	 */
	public static Integer getDayOfYear() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.getDayOfYear();
	}

	/**
	 * @return 获取当前年
	 */
	public static Integer getYear() {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.getYear();
	}

	/**
	 * @return 增加年月日时分秒
	 */
	public static DateTime plus(int year, int months, int days, int hours, int minutes, int seconds) {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.plusYears(year).plusMonths(months).plusDays(days).plusHours(hours).plusMinutes(minutes)
				.plusSeconds(seconds);
	}

	/**
	 * @return 增加年月日时分秒 根据当前日期
	 */
	public static DateTime plusDate(DateTime dateTime, int year, int months, int days, int hours, int minutes,
			int seconds) {
		return dateTime.plusYears(year).plusMonths(months).plusDays(days).plusHours(hours).plusMinutes(minutes)
				.plusSeconds(seconds);
	}

	/**
	 * @return 将String类型的日期转换成dateTime类型
	 */
	public static DateTime parseStringToDateTime(String date, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime ss = DateTime.parse(date, format);
		return ss;
	}

	/**
	 * @return 将String类型的日期转换成date类型
	 */
	public static Date parseStringToDate(String date, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime ss = DateTime.parse(date, format);
		return ss.toDate();
	}

	/**
	 * @return 计算两个时间之间的相差天数
	 */
	public static int daysBetween(String startDate, String endDate, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime dstart = format.parseDateTime(startDate);
		DateTime dend = format.parseDateTime(endDate);
		return Days.daysBetween(dstart, dend).getDays();
	}

	/**
	 * @return 计算两个时间之间的相差年数
	 */
	public static int yearsBetween(String startDate, String endDate, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime dstart = format.parseDateTime(startDate);
		DateTime dend = format.parseDateTime(endDate);
		return Years.yearsBetween(dstart, dend).getYears();
	}

	/**
	 * @return 计算两个时间之间的相差月份数
	 */
	public static int monthsBetween(String startDate, String endDate, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime dstart = format.parseDateTime(startDate);
		DateTime dend = format.parseDateTime(endDate);
		return Months.monthsBetween(dstart, dend).getMonths();
	}

	/**
	 * @return 计算两个时间之间的相差分钟数
	 */
	public static int minutesBetween(String startDate, String endDate, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime dstart = format.parseDateTime(startDate);
		DateTime dend = format.parseDateTime(endDate);
		return Minutes.minutesBetween(dstart, dend).getMinutes();
	}

	/**
	 * @return 计算两个时间之间的相差分钟数
	 */
	public static int minutesBetween(DateTime startDate, DateTime endDate, String fmt) {
		return Minutes.minutesBetween(startDate, endDate).getMinutes();
	}

	/**
	 * @return 计算两个时间之间的相差秒数
	 */
	public static int secondsBetween(String startDate, String endDate, String fmt) {
		String fmts = df_yyyy_MM_dd_HH_mm_ss;
		if (fmt != null) {
			fmts = fmt;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern(fmts);
		DateTime dstart = format.parseDateTime(startDate);
		DateTime dend = format.parseDateTime(endDate);
		return Seconds.secondsBetween(dstart, dend).getSeconds();
	}

	private static Map<String, String> buildMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("GMT", "UTC");
		map.put("WET", "WET");
		map.put("CET", "CET");
		map.put("MET", "CET");
		map.put("ECT", "CET");
		map.put("EET", "EET");
		map.put("MIT", "Pacific/Apia");
		map.put("HST", "Pacific/Honolulu"); // JDK 1.1 compatible
		map.put("AST", "America/Anchorage");
		map.put("PST", "America/Los_Angeles");
		map.put("MST", "America/Denver"); // JDK 1.1 compatible
		map.put("PNT", "America/Phoenix");
		map.put("CST", "America/Chicago");
		map.put("EST", "America/New_York"); // JDK 1.1 compatible
		map.put("IET", "America/Indiana/Indianapolis");
		map.put("PRT", "America/Puerto_Rico");
		map.put("CNT", "America/St_Johns");
		map.put("AGT", "America/Argentina/Buenos_Aires");
		map.put("BET", "America/Sao_Paulo");
		map.put("ART", "Africa/Cairo");
		map.put("CAT", "Africa/Harare");
		map.put("EAT", "Africa/Addis_Ababa");
		map.put("NET", "Asia/Yerevan");
		map.put("PLT", "Asia/Karachi");
		map.put("IST", "Asia/Kolkata");
		map.put("BST", "Asia/Dhaka");
		map.put("VST", "Asia/Ho_Chi_Minh");
		map.put("CTT", "Asia/Shanghai");
		map.put("JST", "Asia/Tokyo");
		map.put("ACT", "Australia/Darwin");
		map.put("AET", "Australia/Sydney");
		map.put("SST", "Pacific/Guadalcanal");
		map.put("NST", "Pacific/Auckland");
		return Collections.unmodifiableMap(map);// 此处返回不可修改的Map
	}

	/**
	 * 得到时间字符串,格式为 yyyy-MM-dd HH:mm:ss
	 * @return 返回格式化后的时间字符串
	 * @since 0.1
	 */
	public static String getTimeNormalString(Date date) {
		DateFormat fmt = new SimpleDateFormat(df_yyyy_MM_dd_HH_mm_ss);
		return fmt.format(date);
	}

	/**
	 * 
	 * Description: 获取毫秒数
	 * 
	 * @param
	 * @return String
	 * @throws @Author
	 *             Administrator Create Date: 2016年8月1日 下午4:47:14
	 */
	public static String getHaomiaoString() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String myTime = sdFormat.format(date);
		return myTime;
	}

	/**
	 * 
	 * Description: 获取前几天或者后几天的时间
	 * 
	 * @param
	 * @return Date
	 * @throws @Author
	 *             Administrator Create Date: 2016年9月5日 下午5:29:54
	 */
	public static Date getBeforeOrAfterDay(Date date, Integer num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 
	 * Description: 获取前几分钟或者后几分钟的时间
	 * 
	 * @param
	 * @return Date
	 * @throws @Author
	 *             Administrator Create Date: 2016年9月5日 下午5:29:54
	 */
	public static Date getBeforeOrAfterMin(Date date, Integer num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, num);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 
	 * Description: 获取结束时间
	 * 
	 * @param
	 * @return String
	 * @throws @Author
	 *             Administrator Create Date: 2016年8月1日 下午4:47:14
	 */
	public static String getEndTime(String endTime) {
		String result = "";
		if (endTime != null && !"".equals(endTime)) {
			result = endTime.split(" ")[0];
			result = result + " 23:59:59";
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: getTodayStartTime
	 * @Description:获取当天开始时间戳
	 * @return
	 * @return: Long
	 */
	public static Long getTodayStartTime() {
		 Calendar c1 = new GregorianCalendar();
		    c1.set(Calendar.HOUR_OF_DAY, 0);
		    c1.set(Calendar.MINUTE, 0);
		    c1.set(Calendar.SECOND, 0);
		 return c1.getTime().getTime();
	}
	
	/**
	 * 
	 * @Title: getTodayEndTime
	 * @Description:获取当天时间结束时间
	 * @return
	 * @return: Long
	 */
	public static Long getTodayEndTime() {
	    Calendar c2 = new GregorianCalendar();
	    c2.set(Calendar.HOUR_OF_DAY, 23);
	    c2.set(Calendar.MINUTE, 59);
	    c2.set(Calendar.SECOND, 59);
		return c2.getTime().getTime();
	}
	
	/**
	 * 
	 * @Title: getYesToday
	 * @Description:获取当前日期前一天
	 * @return
	 * @return: String
	 */
	public static String getYesToday(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/*
	 * 前daycnt天
	 */
	public static Date getLastTimeByDate(int daycnt) {
		DateTime dateTime = DateTime.now(datetime_zone_id);
		return dateTime.minusDays(daycnt).toDate();
	}
	
	/**
	 * 
	 * @Title: getToday
	 * @Description:获取当前日期
	 * @return
	 * @return: String
	 */
	public static String getToday(int num){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, num); 
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	
	/**
	 * 
	 * @Title: getToday
	 * @Description:获取当周周一日期
	 * @return
	 * @return: String
	 */
	public static DateTime getFirstDayInNowWeek() {
        return DateTime.now().dayOfWeek().withMinimumValue();
    }

    public static DateTime getLastDayInNowWeek() {
        return DateTime.now().dayOfWeek().withMaximumValue();
    }
    
    /**
     * 一周中的第几天。周一：１　周二：２ ...
     *
     * @return
     */
    public static int getDayInWeek() {
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        return new DateTime(calendar).getDayOfWeek();
    }
    
    /**
     * 日期在年中第几周
     *
     * @return
     */
    public static String getWeekInYear(Date date) {
        return  getYear()+""+ new DateTime(date).getWeekOfWeekyear();
    }
    
	public static void main(String[] args) {
		
		/*JodaDateUtil jodaDateUtil = new JodaDateUtil();
		System.out.println(jodaDateUtil.getFirstDayInNowWeek().toDate());
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		DateTime dt = new DateTime(); 
		System.out.println( getWeekInYear(getLastTimeByDate(7)));


		System.out.println(JodaDateUtil.getWeekInYear(JodaDateUtil.getLastTimeByDate(7)));

		System.out.println(JodaDateUtil.getWeekInYear(JodaDateUtil.getLastTimeByDate(0)));
		
		//String = "[{"url":"","regex":"/zxxx/sxrdetail.htm?sxrId="}]";
		//System.out.println(JSONArray.parseArray("");
		
		JSONArray jsonArray = new JSONArray();  
        
        HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("url", "ddd");
	    hashMap.put("regex", "KKJJK");
	    jsonArray.add(hashMap);
	    
	    HashMap<String, String> hashMap1 = new HashMap<String, String>();
		hashMap1.put("url", "ddd");
	    hashMap1.put("regex", "KKJJK");
	    jsonArray.add(hashMap1);
	   System.out.println(jsonArray);
	    
	    
	    String saa =  "dff/133";
	    
	    String [] jj = saa.split(",");
	    
	    System.out.println(jj.length);
	    
	    */

	}
	
	
    
	/**
	 * 
	 * @Title: getDate
	 * @Description:
	 * @param amount 得到那天的时间 得到前一天为-1
	 * @param simple yyyyMMdd
	 * @return
	 * @return: String
	 */
    
	public static String getDate(int amount, String simple){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, amount); 
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	/**
	 * JodaDateUtil.getDateWeek(-1, "yyyyww")
	 * @Title: getDateWeek
	 * @Description:获取当前第几周
	 * @param amount -1代表上周
	 * @return
	 * @return: String
	 */
	public static String getDateWeek(int amount, String simple){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, amount); 
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat(simple);
		return df.format(date);
	}


}
