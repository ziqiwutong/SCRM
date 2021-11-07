package com.scrm.marketing.util;

/**
 * @author fzk
 * @date 2021-11-07 16:56
 */
public class MyAssert {
    public static void notNull(String message, Object... args) {
        for (Object arg : args)
            if (arg == null)
                throw new IllegalArgumentException(message);
    }
}
