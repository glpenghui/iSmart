package com.why.ismart.framework.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 依赖org.apache.commons.lang3.time的DateFormatUtils和DateUtils
 * 
 * @author whg
 * @date 2016年6月18日 下午4:02:49
 */
public class DateUtil {
    
    public static final String DAY = "yyyy-MM-dd";
    
    public static final String SECONDS = "HH:mm:ss";
    
    public static final String DAY_SECONDS = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern){
        return DateFormatUtils.format(date, pattern);
    }
    
    public static String format(long millis, String pattern){
        return DateFormatUtils.format(millis, pattern);
    }
    
    public static String format(Calendar calendar, String pattern){
        return DateFormatUtils.format(calendar, pattern);
    }
    
    public static Date parse(String str, String parsePattern){
        try {
            return DateUtils.parseDate(str, new String[]{parsePattern});
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static Date newDate(){
        return new Date();
    }
    
    // 日期创建
    public static Date newDate(int year, int month, int day) {
        return newDate(year, month, day, 0, 0, 0);
    }
    
    /**
     * 创建日期
     * @param year
     * @param month 月份是1-12
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date newDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
}
