package com.scrm.manage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fzk
 * @date 2021-11-10 18:03
 */
public class MyDateTimeUtil {
    /**
     * SimpleDateFormat不是线程安全的，多个线程同时调用format方法会使得内部数据结构被并发访问破坏
     * 使用同步开销大，使用局部变量有点浪费
     * 所以使用 <strong>线程局部变量<strong/>
     */
    public static final ThreadLocal<SimpleDateFormat> timeFormat =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> dateFormat =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static String getNowDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = dateFormat.get();

        return simpleDateFormat.format(new Date());
    }

    public static String getNowTime() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = timeFormat.get();

        return simpleDateFormat.format(new Date());
    }

    /**
     * 自定义日期或时间格式
     *
     * @param pattern 自定义格式，如：yyyy-MM-dd HH:mm:ss 为24小时制;  yyyy-MM-dd HH:mm:ss 为12小时制
     * @return 返回当前的日期或时间
     */
    public static String getNowDateOrTime(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 判断是否是每月第一天
     *
     * @param date 字符串即可
     * @return 只有字符串规范化且是每月第一天才会返回true
     */
    public static boolean isFirstDay(String date) {
        if (!isDateFormat(date))
            return false;

        return (date.charAt(8) == '0' && date.charAt(9) == '1');
    }

    /**
     * 判断是否是最后一天
     *
     * @param date 字符串即可
     * @return 只有是规范化的字符串且是每月最后一天才返回true
     */
    public static boolean isLastDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(date));
            calendar.add(Calendar.DATE, 1);
            if (calendar.get(Calendar.DATE) != 1)
                return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否符合日期规范 如 2021-07-22
     *
     * @param date 字符串
     * @return 传入字符串规范化返回true
     */
    public static boolean isDateFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 用于判断开始日期和结束日期格式是否都正确且结束日期大于开始日期
     *
     * @param startDate 正确为2021-07-01这种
     * @param endDate   正确为2021-07-31这种
     * @return 正确返回true，其他情况返回false
     */
    public static boolean isStartAndEndRight(String startDate, String endDate) {
        return MyDateTimeUtil.isFirstDay(startDate) &&
                MyDateTimeUtil.isLastDay(endDate) &&
                startDate.compareTo(endDate) < 0;
    }
}
