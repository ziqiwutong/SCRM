package com.scrm.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    /**
     * 获取指定范围日期值，yyyy-MM-dd，左闭右开
     *
     * @param range 日期范围，回调参数
     * @param time 日期范围名：今天，昨天，过去7天，本周，上周，本月，上月
     */
    public static void dateRange(String[] range, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        switch (time) {
            case "今天":
                range[0] = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
                range[1] = format.format(calendar.getTime());
                break;
            case "昨天":
                range[1] = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, -1);
                range[0] = format.format(calendar.getTime());
                break;
            case "过去7天":
                range[1] = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, -7);
                range[0] = format.format(calendar.getTime());
                break;
            case "本周":
                calendar.add(
                        Calendar.DATE,
                        calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ?
                                -6 : 2 - calendar.get(Calendar.DAY_OF_WEEK)
                );
                range[0] = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, 7);
                range[1] = format.format(calendar.getTime());
                break;
            case "上周":
                calendar.add(
                        Calendar.DATE,
                        calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ?
                                -13 : -5 - calendar.get(Calendar.DAY_OF_WEEK)
                );
                range[0] = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, 7);
                range[1] = format.format(calendar.getTime());
                break;
            case "本月":
                calendar.set(Calendar.DATE, 1);
                range[0] = format.format(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
                range[1] = format.format(calendar.getTime());
                break;
            case "上月":
                calendar.set(Calendar.DATE, 1);
                range[1] = format.format(calendar.getTime());
                calendar.add(Calendar.MONTH, -1);
                range[0] = format.format(calendar.getTime());
                break;
            default:
                break;
        }
    }

    /**
     * 获取指定日期前后某一天的时间戳范围(毫秒数)
     *
     * @param range 时间戳范围，回调参数
     * @param date 指定日期
     * @param offset 与指定日期偏移日数
     */
    public static void dateTimestamp(Long[] range, Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        range[0] = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        range[1] = calendar.getTimeInMillis();
    }
}
