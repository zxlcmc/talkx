/*
 * Copyright 2016 bianxianmao.com All right reserved. This software is the confidential and proprietary information of
 * textile.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with bianxianmao.com.
 */

package com.bxm.warcar.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h3>时间帮助类</h3>
 *
 * @author allen
 * @since V1.0.0 2017/12/08
 */
public final class DateHelper {
    private DateHelper() {}

    public static final long SECONDS_OF_DAY = 24 * 60 * 60;

    /**
     * 8位的时间格式
     */
    public static final String PATTERN_STR8 = "yyyyMMdd";
    /**
     * 10位时间格式
     */
    public static final String PATTERN_STR10 = "yyyy-MM-dd";
    /**
     * 14位时间格式
     */
    public static final String PATTERN_STR14 = "yyyyMMddHHmmss";
    /**
     * 19位时间格式
     */
    public static final String PATTERN_STR19 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 返回今天剩余秒数
     * @return
     */
    public static long getRemainSecondsOfToday() {
        return getRemainSecondsOfDay(1);
    }

    public static long getRemainSecondsOfDay(int day) {
        return (SECONDS_OF_DAY * day) - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
    }

    /**
     * yyyyMMdd
     * @return
     */
    public static String getDate() {
        return format("yyyyMMdd");
    }

    public static String format(String pattern) {
        return DateFormatUtils.format(Calendar.getInstance(), pattern);
    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, new String[] { pattern });
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取前一日
     * @see DateHelper#PATTERN_STR10 日期格式固定
     * @author JandMin
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getPreDate(String date) throws ParseException {
        return getDate(date,-1);
    }

    /**
     *  获取后一日
     * @see DateHelper#PATTERN_STR10 日期格式固定
     * @author JandMin
     * @date: 2018/7/25
     * @param date
     * @return java.lang.String
     * @throws 
     */
    public static String getNextDate(String date) throws ParseException {
        return getDate(date,1);
    }

    /**
     * 获取指定日期的前后日期
     * @see DateHelper#PATTERN_STR10 日期格式固定
     * @author JandMin
     * @date: 2018/7/25
     * @param date 日期
     * @param count 相差的天数
     * @return java.lang.String
     * @throws ParseException
     */
    public static String getDate(String date, int count) throws ParseException {
        return getDate(date,count,PATTERN_STR10);
    }

    /**
     * 根据指定的格式，获取指定日期的前后日期
     * 注意：pattern就是date的日期格式
     * @author JandMin
     * @date: 2018/7/25
     * @param date 日期
     * @param count 相差天数
     * @param pattern 日期格式
     * @return java.lang.String
     * @throws ParseException
     */
    public static String getDate(String date, int count, String pattern) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat parser = new SimpleDateFormat(pattern);
        calendar.setTime(parser.parse(date));
        calendar.add(Calendar.DATE, count);
        return parser.format(calendar.getTime());
    }

    /**
     * 指定时间格式返回某一日的时间字符串
     * @author JandMin
     * @date: 2018/7/25
     * @param count 与当前时间相差得天数：0为当天，负数为之前的日期，正数为未来的日期
     * @param pattern 时间格式
     * @return java.lang.String
     */
    public static String getDateFromToday(int count,String pattern){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, count);
        return DateFormatUtils.format(calendar.getTime(),pattern);
    }

    /**
     * 根据日期格式将字符串日期转换为日期
     * @author JandMin
     * @date: 2018/8/30
     * @param strDate 日期
     * @param pattern 日期格式
     * @return java.util.Date
     * @throws
     */
    public static Date convertStringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate) || StringUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Date 转string 默认 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return java.lang.String
     * @throws
     * @author kk.xie
     * @date 2018/9/3 15:24
     */
    public static String convertDateToString(Date date){
        return convertDateToString(date, PATTERN_STR19);
    }

    /**
     * date 转 String 指定转换格式
     *
     * @param date
     * @param pattern
     * @return java.lang.String
     * @throws
     * @author kk.xie
     * @date 2018/9/3 15:24
     */
    public static String convertDateToString(Date date, String pattern){
        if (date == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
