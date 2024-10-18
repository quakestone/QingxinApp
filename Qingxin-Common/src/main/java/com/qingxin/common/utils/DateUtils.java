package com.qingxin.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils{

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前月
     *
     * @return String
     */
    public static String getDateYear()
    {
        return dateTimeNow("yyyy");
    }


    /**
     * 获取当前日
     *
     * @return String
     */
    public static String getDateMonth()
    {
        return dateTimeNow("MM");
    }


    /**
     * 获取当前年
     *
     * @return String
     */
    public static String getDateDay()
    {
        return dateTimeNow("DD");
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    /**
     * 获取当前时间时间戳
     */
    public static Long getTimestamp(){
        long l = Instant.now().toEpochMilli();
        return l;
    }

    /**
     * 获取一年后的时间戳
     */
    public static Long getYearTimestamp(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // 判断是否为闰年
        boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);

        // 计算一年的天数
        int daysInYear = isLeapYear ? 366 : 365;

        // 计算一年的毫秒数
        long millisecondsInYear = daysInYear * 24L * 60 * 60 * 1000;

        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 计算一年后的时间戳
        long oneYearAgoTimestamp = currentTimestamp + millisecondsInYear;

        System.out.println("当前时间戳: " + currentTimestamp);
        System.out.println("一年前的时间戳: " + oneYearAgoTimestamp);
        return oneYearAgoTimestamp;
    }
    /**
     * 获取一个月后的时间戳
     */
    public  static  Long getMonthTimestamp(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // 计算一个月的天数
        int daysInMonth = 30;

        // 计算一个月的毫秒数
        long millisecondsInMonth = daysInMonth * 24L * 60 * 60 * 1000;

        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 计算一个月后的时间戳
        calendar.set(year, month - 1, calendar.get(Calendar.DAY_OF_MONTH));
        long oneMonthAgoTimestamp = currentTimestamp + millisecondsInMonth;

        System.out.println("当前时间戳: " + currentTimestamp);
        System.out.println("一个月前的时间戳: " + oneMonthAgoTimestamp);
        return  oneMonthAgoTimestamp;
    }

    /**
     * 获取一个季度的时间戳
     */
    public  static Long getQuarterTimestamp(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // 计算一个季度的天数
        int daysInQuarter = 90;

        // 计算一个季度的毫秒数
        long millisecondsInQuarter = daysInQuarter * 24L * 60 * 60 * 1000;

        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 计算一个季度前的时间戳
        calendar.set(year, month - 3, calendar.get(Calendar.DAY_OF_MONTH));
        long oneQuarterAgoTimestamp = currentTimestamp + millisecondsInQuarter;

        System.out.println("当前时间戳: " + currentTimestamp);
        System.out.println("一个季度后的时间戳: " + oneQuarterAgoTimestamp);
        return  oneQuarterAgoTimestamp;
    }

    /**
     * 获取一天后的时间戳
     * @return
     */
    public static  Long getDayTimestamp(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置时间为当天的午夜 00:00:00
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取当天午夜的时间戳
        long midnightTimestamp = calendar.getTimeInMillis();

        // 计算一天的毫秒数
        long millisecondsInDay = 24L * 60 * 60 * 1000;

        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 计算一天后的时间戳
        long oneDayLaterTimestamp = midnightTimestamp + millisecondsInDay;

        System.out.println("当前时间戳: " + currentTimestamp);
        System.out.println("一天后的时间戳: " + oneDayLaterTimestamp);
        return oneDayLaterTimestamp;
    }

    /**
     * 获取一个星期后的时间戳
     * @return
     */
    public static  Long getWeekTimestamp(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置时间为当天的午夜 00:00:00
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取当天午夜的时间戳
        long midnightTimestamp = calendar.getTimeInMillis();

        // 计算一天的毫秒数
        long millisecondsInDay = 24L * 60 * 60 * 1000;

        // 计算一个星期的毫秒数
        long millisecondsInWeek = millisecondsInDay * 7;

        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 计算一星期后的时间戳
        long oneWeekLaterTimestamp = midnightTimestamp + millisecondsInWeek;

        System.out.println("当前时间戳: " + currentTimestamp);
        System.out.println("一星期后的时间戳: " + oneWeekLaterTimestamp);
        return oneWeekLaterTimestamp;
    }
}
