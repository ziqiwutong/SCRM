package com.scrm.manage.util;

import com.scrm.manage.exception.MyException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fzk
 * @date 2021-11-10 18:03
 */
public class MyDateTimeUtil {
    public static String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(new Date());
    }

    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
        if (!isDateFormat(date))
            return false;

        String[] split = date.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        switch (month) {
            // 31天的那种
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day != 31)
                    return false;
                break;
            case 2:
                if (year % 4 == 0 && day != 29)
                    return false;
                else if (year % 4 != 0 && day != 28)
                    return false;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day != 30)
                    return false;
                break;
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
        if (date == null || "".equals(date))
            return false;

        // 1.判断是否是纯数字+'-'
        char[] chars = date.toCharArray();
        for (char ch : chars) {
            if ((ch != '-') && (ch < '0' || ch > '9'))
                return false;
        }
        // 2.根据'-'分割
        if (!date.contains("-"))
            return false;

        String[] split = date.split("-");
        if (split.length != 3)
            return false;

        if (split[0].length() != 4 || split[1].length() != 2 || split[2].length() != 2)
            return false;

        // 3.分别判断年月日
        int year = Integer.parseInt(split[0]);
        if (year < 1970 || year > 2070)
            return false;
        if (split[1].compareTo("01") < 0 || split[1].compareTo("12") > 0)
            return false;
        if (split[2].compareTo("01") < 0 || split[2].compareTo("31") > 0)
            return false;

        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        switch (month) {
            // 31天的那种
//            case 1:
//            case 3:
//            case 5:
//            case 7:
//            case 8:
//            case 10:
//            case 12:
//                return true;
            case 2:
                if (year % 4 == 0 && day > 29)
                    return false;
                else if (year % 4 != 0 && day > 28)
                    return false;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day > 30)
                    return false;
                break;
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
