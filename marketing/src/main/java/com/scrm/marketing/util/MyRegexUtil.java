package com.scrm.marketing.util;

import java.util.regex.Pattern;

/**
 * @author fzk
 * @date 2021-09-10 11:29
 * <p>
 * 我的正则工具类
 */
public class MyRegexUtil {
    /**
     * 规则：比较严格的规则，按照3大运输商的手机号规律
     * 如果为空，false
     *
     * @param phone 手机号
     * @return 匹配结果
     */
    public static boolean phone_match(String phone) {
        if (phone == null || phone.length() == 0)
            return false;
        String phone_patter = "^1(3[0-9]|8[0-9]|5[0-35-9]|9[0-35-9]|7[0-8]|4[0-14-9]|6[2567])[0-9]{8}$";
        return Pattern.matches(phone_patter, phone);
    }

    /**
     * 规则：正常规则
     * 如果为空，false
     *
     * @param email 邮箱
     * @return 匹配结果
     */
    public static boolean email_match(String email) {
        if (email == null || email.length() == 0)
            return false;
        String email_pattern = "^([a-z0-9_.-]+)@([\\da-z.-]+)\\.([a-z.]{2,6})$";
        return Pattern.matches(email_pattern, email);
    }

    /**
     * 规则：[3,16]的任意字符，除\n
     * 如果为空，false
     *
     * @param username 用户名
     * @return 匹配结果
     */
    public static boolean username_match(String username) {
        if (username == null || username.length() == 0)
            return false;
        String username_pattern = "^.{3,16}$";
        return Pattern.matches(username_pattern, username);
    }

    /**
     * 密码检测原则：[8,36]的任意字符，除\n
     * 如果为空，false
     *
     * @param password 密码
     * @return 匹配结果
     */
    public static boolean password_match(String password) {
        if (password == null || password.length() == 0)
            return false;
        String password_patter = "^.{8,36}";
        return Pattern.matches(password_patter, password);
    }
}
