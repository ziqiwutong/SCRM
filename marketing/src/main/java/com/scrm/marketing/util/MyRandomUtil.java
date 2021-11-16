package com.scrm.marketing.util;

import java.util.Random;

/**
 * @author fzk
 * @date 2021-11-15 22:11
 */
@SuppressWarnings("unused")
public class MyRandomUtil {
    private final static Random random;

    static {
        random = new Random();
    }

    public static String randomStr(int length) {
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
    }
}
