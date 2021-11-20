package com.scrm.marketing.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author fzk
 * @date 2021-11-15 22:11
 */
@SuppressWarnings("unused")
public class MyRandomUtil {
    /**
     * Random类是线程安全的，但是如果多个线程需要等待一个共享的随机数生成器，会很低效
     * 所以使用 <strong>线程局部变量<strong/>
     * Java 7 提供了一个便利类 ThreadLocalRandom
     * 如：
     * int random=ThreadLocalRandom.current().nextInt(upperBound);
     */
    //    private static final Random random;
    private static final char[] charCache;

    static {
//        random = new Random();
        charCache = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    }

    /*public static String randomStr(int length) {
        if (length < 0) throw new IllegalArgumentException("length can not 小于 0");
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch;
            switch (random.nextInt(3)) {
                case 0: // '0'对应48 '9'对应57
                    ch = (char) (48 + random.nextInt(10));
                    break;
                case 1:// 'A'对应65 'Z'对应90
                    ch = (char) (65 + random.nextInt(26));
                    break;
                default:// 'a'对应97 'z'对应122
                    ch = (char) (97 + random.nextInt(26));
            }
            builder.append(ch);
        }
        return builder.toString();
    }*/
    /*数字、小写大写字母*/
    public static String randomStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++)
            builder.append(charCache[localRandom.nextInt(62)]);
        return builder.toString();
    }

    /*数字混合大写字母*/
    public static String randomUpperStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++) {
            int index = localRandom.nextInt(62);
            if (9 < index && index < 36) index += 26;
            builder.append(charCache[index]);
        }
        return builder.toString();
    }

    /*数字混合小写字母*/
    public static String randomLowerStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++) {
            builder.append(charCache[localRandom.nextInt(0, 36)]);
        }
        return builder.toString();
    }

    /*返回纯字符串*/
    public static String randomPureStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++)
            builder.append(charCache[localRandom.nextInt(10, 62)]);
        return builder.toString();
    }

    /*返回纯大写字符串*/
    public static String randomPureUpperStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++)
            builder.append(charCache[localRandom.nextInt(36, 62)]);
        return builder.toString();
    }

    /*返回纯小写字符串*/
    public static String randomPureLowerStr(int length) {
        assert length >= 0 : "length can not be 小于 0";// 相信在上线之后不会出现 length < 0
        if (length == 0) return "";

        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();//使用本线程的ThreadLocalRandom
        for (int i = 0; i < length; i++)
            builder.append(charCache[localRandom.nextInt(10, 36)]);
        return builder.toString();
    }
}