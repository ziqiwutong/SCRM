package com.scrm.marketing.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author fzk
 * @date 2021-11-18 19:32
 */
public class MyDigestUtil {
    /**
     * HmacSHA256加密
     *
     * @param message 要加密信息
     * @param key     秘钥，不能为 null 或 ""
     * @return String
     * @throws NoSuchAlgorithmException 这个不会抛异常
     * @throws InvalidKeyException      秘钥格式不对会抛出异常
     */
    public static String sha256_mac(String message, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
        return byteArrayToHexString(bytes);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                sb.append('0');
            sb.append(stmp);
        }
        return sb.toString().toLowerCase();
    }
}
