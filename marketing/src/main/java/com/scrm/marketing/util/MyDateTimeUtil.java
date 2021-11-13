package com.scrm.marketing.util;


import com.scrm.marketing.exception.MyException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fzk
 * @date 2021-07-15 21:34
 */
public class MyDateTimeUtil {
    public static String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(new Date());
    }

    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 自定义日期或时间格式
     *
     * @param pattern 自定义格式，如：yyyy-MM-dd HH:mm:ss // 24小时制
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
     * @return 正确返回true，其他情况抛异常
     */
    public static boolean isStartAndEndRight(String startDate, String endDate) throws MyException {
        if (!MyDateTimeUtil.isFirstDay(startDate) || !MyDateTimeUtil.isLastDay(endDate))
            throw new MyException(400, "日期格式不正确，正确格式：" +
                    "开始日期2021-07-01，结束日期2021-07-31；" +
                    "或开始日期不是每月第一天；" +
                    "或结束日期不是每月最后一天");

        if (startDate.compareTo(endDate) > 0)
            throw new MyException(400, "开始日期：" + startDate + " 大于结束日期：" + endDate);

        return true;
    }
}
