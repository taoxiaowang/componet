package com.hikcreate.library.util;

import java.security.MessageDigest;

/**
 * @author yslei
 */
public class MD5Util {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Encode the string to MD5 string
     *
     * @param str origin
     * @return string MD5
     */
    public static String getMD5(String str) {
        return getMD5(str.getBytes());
    }

    /**
     * Encode the string to MD5 string
     *
     * @param source byte[]
     * @return string MD5
     */
    public static String getMD5(byte[] source) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {

                byte byte0 = tmp[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];

                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
