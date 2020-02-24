package com.ly.add.sponge.common.utils;

import com.ly.add.sponge.common.time.Sigan;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间处理工具类
 *
 * @author hxf14879
 */
public class TimeUtil {

    /**
     * yyyy-MM-dd
     */
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    /**
     * yyyyMMdd
     */
    public final static String FORMAT_DATE_EASY = "yyyyMMdd";
    /**
     * hh:mm
     */
    public final static String FORMAT_TIME = "hh:mm";
    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * MM月dd日 hh:mm
     */
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm";

    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    private static final long ST_ONE_DAY = 24 * 60 * 60 * 1000;

    private static ThreadLocal<SimpleDateFormat> threadlocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    public static SimpleDateFormat getDateformat() {
        return (SimpleDateFormat) threadlocal.get();
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
     *
     * @param timestamp 时间戳 单位为毫秒
     * @param format    指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        if (format == null || format.trim().equals("")) {
            getDateformat().applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(getDateformat().format(new Date(timestamp)).substring(0, 4));
            System.out.println("currentYear: " + currentYear);
            System.out.println("year: " + year);
            if (currentYear == year) {// 如果为今年则不显示年份
                getDateformat().applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                getDateformat().applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            getDateformat().applyPattern(format);
        }
        Date date = new Date(timestamp);
        return getDateformat().format(date);
    }

    /**
     * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
     *
     * @param timestamp      时间戳 单位是毫秒
     * @param partionSeconds 时间分割线，当现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
     * @param format
     * @return
     */
    public static String getMixTimeFromTimestamp(long timestamp, long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            getDateformat().applyPattern(FORMAT_DATE_TIME);
        } else {
            getDateformat().applyPattern(format);
        }
        return getDateformat().format(new Date());
    }

    /**
     * 按指定格式返回当前时间的昨天时间字符串
     *
     * @param format
     * @return
     */
    public static String getYestoday(String format) {
        return getBeforeYestoday(format, 1);
    }

    /**
     * 获取今天前day天的日期时间字符串
     *
     * @param format
     * @param day
     * @return
     */
    public static String getBeforeYestoday(String format, int day) {
        String today = getCurrentTime(format);
        return dateAdd(today, -1 * day, format);
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param format 指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (format == null || format.trim().equals("")) {
            getDateformat().applyPattern(FORMAT_DATE_TIME);
        } else {
            getDateformat().applyPattern(format);
        }
        try {
            return getDateformat().parse(timeStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param time   日期
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getStringFromTime(Date time, String format) {
        if (format == null || format.trim().equals("")) {
            getDateformat().applyPattern(FORMAT_DATE_TIME);
        } else {
            getDateformat().applyPattern(format);
        }
        return getDateformat().format(time);
    }

    /**
     * 给日期字符串加日期
     *
     * @return
     */
    public static String dateAdd(String dateStr, int addDays) {
        return dateAdd(dateStr, addDays, FORMAT_DATE);
    }

    /**
     * 给日期字符串加日期
     *
     * @param dateStr
     * @param addDays
     * @param format
     * @return
     */
    public static String dateAdd(String dateStr, int addDays, String format) {
        getDateformat().applyPattern(format);
        try {
            Date date = getDateformat().parse(dateStr);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DAY_OF_MONTH, addDays);
            return getStringFromTime(cl.getTime(), format);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 给日期字符串加分钟
     *
     * @param dateStr
     * @param addMinutes
     * @param format
     * @return
     */
    public static String minuteAdd(String dateStr, int addMinutes, String format) {
        getDateformat().applyPattern(format);
        try {
            Date date = getDateformat().parse(dateStr);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.MINUTE, addMinutes);
            return getStringFromTime(cl.getTime(), format);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把一个日期格式字符串转换为另一个格式字符串
     *
     * @param date
     * @param fromFormat
     * @param toFormat
     * @return
     */
    public static String convertDateStr(String date, String fromFormat, String toFormat) {
        Date d = getTimeFromString(date, fromFormat);
        return getStringFromTime(d, toFormat);
    }

    /**
     * 把一个日期字符串转为Unix时间戳
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static long convertUnixTimestamp(String date, String format) throws ParseException {
        Date d = new SimpleDateFormat(format).parse(date);
        return d.getTime();
    }

    /**
     * 把一个日期字符串转为10位的Unix时间戳
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    @Deprecated//QY --> string2Timestamp
    public static long convertUnixTimestampTenBit(String date, String format) throws ParseException {
        Date d = new SimpleDateFormat(format).parse(date);
        return (long) ((d.getTime()) / 1000);
    }

    /**
     * 把时间戳转为日期字符串
     *
     * @param timestamp
     * @param format
     * @return
     */
    @Deprecated//QY --> timestamp2String
    public static String timestamp2string(long timestamp, String format) {
        DateFormat dfs = new SimpleDateFormat(format);
        return dfs.format(timestamp);
    }

    /**
     * 2 -> 02:00:00
     *
     * @param hour
     * @return
     */
    public static String formatHourStart(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException();
        }
        if (hour < 10) {
            return "0" + hour + ":00:00 000";
        } else {
            return hour + ":00:00 000";
        }
    }

    /**
     * 2 -> 02:00:00
     *
     * @param hour
     * @return
     */
    public static String formatHourEnd(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException();
        }
        if (hour < 10) {
            return "0" + hour + ":59:59 999";
        } else {
            return hour + ":59:59 999";
        }
    }

    /**
     * 计算距离当前时间多久
     *
     * @return
     */
    public static long tillNow(long timeMillion) {
        long now = System.currentTimeMillis();
        return now - timeMillion;
    }

    /**
     * 获取相对于今天偏移一段年月的日期
     *
     * @param format
     * @param yearAdd
     * @param monthAdd
     * @return
     */
    public static String getYearMonth(String format, int yearAdd, int monthAdd) {
        Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        if (yearAdd != 0) {
            cl.add(Calendar.YEAR, yearAdd);
        }
        if (monthAdd != 0) {
            cl.add(Calendar.MONTH, monthAdd);
        }
        return getStringFromTime(cl.getTime(), format);
    }

    /**
     * 比较orig和dest，返回是否orig比dest晚（orig > dest）
     *
     * @param orig
     * @param dest
     * @param format
     * @return
     */
    public static boolean after(String orig, String dest, String format) {
        Date origDate = getTimeFromString(orig, format);
        Date destDate = getTimeFromString(dest, format);
        return origDate.after(destDate);
    }

    public static String getFormatDateFromTimestamp(Long timestamp, String format) {
        String str = "";
        String dateFormat = StringUtils.isEmpty(format) ? FORMAT_DATE : format;
        if (null != timestamp) {
            int length = timestamp.toString().length();
            if (length < 13) {
                timestamp = timestamp * (int) Math.pow(10, 13 - length);
            }
            getDateformat().applyPattern(dateFormat);
            Date date = new Date(timestamp);
            str = getDateformat().format(date);
        }
        return str;
    }

    public static Long getMidnightTimestamp() {
        return LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Long getMidnightTimestamp(int dayAgo) {
        return LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400 * 1000L * dayAgo;
    }

    public static Long timeByMonth(int n) {
        return LocalDateTime.now().plusMonths(n).toEpochSecond(ZoneOffset.of("+8"));

    }

    public static Long dayBegin() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(System.currentTimeMillis() / 1000), ZoneOffset.systemDefault())
                .withHour(0).withMinute(0).withSecond(0).atZone(ZoneOffset.systemDefault()).toEpochSecond();
    }

    public static LocalDateTime unixTimestamp2LocalDateTime(String unixTimestamp) {
        if (StringUtils.isEmpty(unixTimestamp))
            return null;
        long time = Long.parseLong(unixTimestamp);
        return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(8));
    }

   /* public static void main(String[] args) {
        System.out.println(timestamp2String(beginOfMonth(2), yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(timestamp2String(endOfMonth(2), yyyy_MM_dd_HH_mm_ss_SSS));
    }*/

    public static Long beginOfMonth() {
        return beginOfMonth(LocalDate.now().getMonthValue());
    }

    /**
     * QY: begin of month in the same year
     *
     * @param month month
     * @return Unix TimeStamp(second)(10len) OR
     * Standard TimeStamp(millisecond)(13len)(default)
     */
    public static Long beginOfMonth(Integer month) {
        return beginOfMonth(month, false);
    }

    public static Long beginOfMonth(Integer month, Boolean unix) {
        LocalDateTime beginOfMonth = LocalDateTime.of(LocalDate.now().getYear(), month, 1, 0, 0, 0, 0);
        return ldt2ts(beginOfMonth, unix);
    }

    /**
     * QY: end of month in the same year
     *
     * @param month month
     * @return Unix TimeStamp(second)(10len) OR
     * Standard TimeStamp(millisecond)(13len)(default)
     */
    public static Long endOfMonth(Integer month) {
        return endOfMonth(month, false);
    }

    public static Long endOfMonth(Integer month, Boolean unix) {
        int year = LocalDate.now().getYear();
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, Month.of(month).length(year % 4 == 0), 23, 59, 59, 999);
        return ldt2ts(endOfMonth, unix);
    }


    /**
     * QY:
     */
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String HHmmss = "HHmmss";
    public static final String HH_mm_ss_SSS = "HH:mm:ss.SSS";
    public static final String HHmmssSSS = "HHmmssSSS";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    /**
     * Timestamp -> String
     */
    public static String ts2str(Long timestamp) {
        return ts2str(timestamp, yyyy_MM_dd_HH_mm_ss);
    }

    public static String ts2str(Long timestamp, String format) {
        String str = "";
        if (null != timestamp) {
            int length = timestamp.toString().length();
            if (length < 13) {
                timestamp = timestamp * (int) Math.pow(10, 13 - length);
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            LocalDateTime d = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            str = d.format(dtf);
        }
        return str;
    }

    /**
     * Timestamp -> LocalDateTime
     */
    public static LocalDateTime ts2ldt(Long timestamp) {
        int length = timestamp.toString().length();
        if (length < 13) {
            timestamp = timestamp * (int) Math.pow(10, 13 - length);
        }
        return new Date(timestamp).toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime -> Timestamp
     */
    public static Long ldt2ts(LocalDateTime localDateTime) {
        return ldt2ts(localDateTime, false);
    }

    public static Long ldt2ts(LocalDateTime localDateTime, Boolean unix) {
        if (unix) {
            return localDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();
        } else {
            return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        }
    }

    /**
     * LocalDateTime -> String
     */
    public static String ldt2str(LocalDateTime ldt, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(ldt);
    }

    /**
     * String -> Timestamp
     * String -> LocalDateTime -> Timestamp
     */
    public static Long str2ts(String date, String format) {
        return str2ts(date, format, false);
    }

    public static Long str2ts(String date, String format, Boolean unix) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        LocalDateTime ldt = str2ldt(date, format);
        return ldt2ts(ldt, unix);
    }

    /**
     * String -> LocalDateTime
     */
    public static LocalDateTime str2ldt(String date, String format) {
        final DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                .appendPattern(format)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, LocalDate.now().getYear())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalDate.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, LocalDate.now().getDayOfMonth())
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
        return LocalDateTime.parse(date, dtf);
    }


    public static String obj2Str(Object input) {
        Long ts13 = obj2ts(input);
        return ts2str(ts13, yyyy_MM_dd_HH_mm_ss);
    }

    public static Long obj2ts(Object input) {
        return obj2ts(input, "");
    }

    public static Long obj2ts(Object input, String pattern) {
        String result = "";
        Long ret = null;
        if (input instanceof String) {
            String input2 = (String) input;
            if (StringUtils.isNumeric(input2)) {
                long inputLong = Long.parseLong(input2);
                result = ts2str(inputLong);
            }
            String input3 = input2.replaceAll("\\D", "_");
            for (int i = 0; i < 3; i++) {
                input3 = input3.replaceAll("__", "_");
            }
            String[] splits = input3.split("_");
            StringBuilder ndate = new StringBuilder();
            for (String split : splits) {
                int length = split.length();
                switch (length) {
                    case 1:
                        ndate.append("0").append(split);
                        break;
                    case 2:
                        ndate.append(split);
                        break;
                    case 3:
                        ndate.append(split);
                        break;
                    case 4:
                        ndate.append(split);
                        break;
                    default:
                        ndate.append("_").append(split).append("_");
                }
            }
            result = ndate.toString();
        } else if (input instanceof Long) {
            Long input2 = (Long) input;
            String s = ts2str(input2);
        }

        if (StringUtils.isNotBlank(pattern)) {
            String s = pattern.replaceAll("\\D", "");
            ret = str2ts(result, s);
        } else {

            if (result.length() == 17) {
                ret = str2ts(result, yyyyMMddHHmmssSSS);
            }
            if (result.length() == 14) {
                ret = str2ts(result, yyyyMMddHHmmss);
            }
            if (result.length() == 8) {
                ret = str2ts(result, yyyyMMdd);
            }
            if (result.length() == 6) {
                ret = str2ts(result, HHmmss);
            }
            if (result.length() == 9) {
                ret = str2ts(result, HHmmssSSS);
            }
        }
        return ret;
    }

    public static LocalDateTime obj2ldt(Object input) {
        Long ts13 = obj2ts(input);
        return ts2ldt(ts13);
    }

    public static Integer getRemainSecondsOneDay(Date currentDate) {
        //获取当前时间，到今晚12点的剩余秒
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    public static void main(String[] args) {

        String mixTimeFromTimestamp = getMixTimeFromTimestamp(1578642056495L, 1495076993166L, null);
        System.out.println(mixTimeFromTimestamp);

        //时间戳毫秒转LocalDateTime
        LocalDateTime ldt = new Date(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        System.out.println(ldt);

        System.out.println(getRemainSecondsOneDay(new Date()));

        LocalDateTime localDateTime = str2ldt("19:00:00", "HH:mm:ss");
        System.out.println(ts2str(ldt2ts(localDateTime)));

        LocalDateTime rightNow = LocalDateTime.now();
        String date = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(rightNow);
        System.out.println(date);

        System.out.println("------------------------------");

        List<Object> sampleList = new ArrayList<>();
        sampleList.add("2019年10月1日 9点30分0秒");
        sampleList.add("2019-10-1 9:30:0");

        for (Object sample : sampleList) {
            System.out.print(sample.toString() + "---------->");
            String s = obj2Str(sample);
            System.out.println(s);
        }

        System.out.println("------------------");

        for (Object sample : sampleList) {
            System.out.print(sample.toString() + "---------->");
            String y = Sigan.custom(sample)
//                    .plus(1, "y")
                    .plus(3, "M")
                    .toStr();
            System.out.println(y);

        }


    }

}