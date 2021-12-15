package com.scrm.service.util.order;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成订单号类
 */
@Component
public class GenerateNum {

    // 全局自增数
    private static int count = 0;

    // 每毫秒秒最多生成多少订单（最好是像9999这种准备进位的值）
    private static final int total = 9999;

    // 格式化的时间字符串
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    // 获取当前时间年月日时分秒毫秒字符串
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }

    // 记录上一次的时间，用来判断是否需要递增全局数
    private static String now = null;

    /*
     * 生成一个订单号
     */
    public synchronized String GenerateOrder() {
        String dataStr = getNowDateStr();
        if (dataStr.equals(now)) {
            count++;// 自增
        } else {
            count = 1;
            now = dataStr;
        }
        if (count >= total) {
            count = 1;
        }
        return dataStr + String.format("%04d", count);
    }

}
