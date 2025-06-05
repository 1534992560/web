package com.example.account.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String fullDayFormat = "yyyy-MM-dd HH:mm:ss";

    public static final String dateFormat = "yyyy-MM-dd";

    public static final String monthFormat = "yyyyMM";

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDHH = "yyyyMMddHH";

    /**
     * 获取当天0点
     * @param
     */
    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);//控制时
        cal.set(Calendar.MINUTE, 0);//控制分
        cal.set(Calendar.SECOND, 0);//控制秒
        return cal.getTime();
    }

    /**
     * 获取当前日期最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        final int lastDay = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = instance.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前日期第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        final int firstDay = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date firstDate = instance.getTime();
        firstDate.setDate(firstDay);
        return firstDate;
    }

    public static Date stringToDate(String str, String format) {
        if(str == null){
            return null;
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);//注意月份是MM
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
