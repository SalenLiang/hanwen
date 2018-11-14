package com.fahai.cc.service.util;

import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日期操作的工具类
 * 下面这些方法都是线程安全的
 */
public class DateUtil {

    //本地的时区
    private static final String LOCAL_ZONE = "Asia/Shanghai";
    public static final String DEFAULT_PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATTERN_DATE = "yyyy-MM-dd";
    public static final String DEFAULT_PATTERN_TIME = "HH:mm:ss";

    /**
     * 获得格式化的日期时间
     * @param pattern
     * @return
     */
    public static String getFormatDateTime(String pattern){
        if(StringUtils.isBlank(pattern)){
            pattern = DEFAULT_PATTERN_DATE_TIME;
        }
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(LOCAL_ZONE));
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    /**
     * 获得格式化的日期
     * @param pattern
     * @return
     */
    public static String getFormatDate(String pattern){
        if(pattern == null || pattern.equals("")){
            pattern = DEFAULT_PATTERN_DATE;
        }
        LocalDate localDate = LocalDate.now(ZoneId.of(LOCAL_ZONE));
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    public static void main(String[] args) {
        System.out.println(getFormatDateTime("yyyy-dd-MM HH:mm:ss"));
        System.out.println(getFormatDate(null));
    }
}
