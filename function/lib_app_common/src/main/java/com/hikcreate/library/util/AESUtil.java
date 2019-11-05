package com.hikcreate.library.util;

import android.text.TextUtils;

import com.hikcreate.library.app.HikApplicationCreate;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密
 * 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
 * AES/CBC/NoPadding             16                          不支持
 * AES/CBC/PKCS5Padding          32                          16
 * AES/CBC/ISO10126Padding       32                          16
 * AES/CFB/NoPadding             16                          原始数据长度
 * AES/CFB/PKCS5Padding          32                          16
 * AES/CFB/ISO10126Padding       32                          16
 * AES/ECB/NoPadding             16                          不支持
 * AES/ECB/PKCS5Padding          32                          16
 * AES/ECB/ISO10126Padding       32                          16
 * AES/OFB/NoPadding             16                          原始数据长度
 * AES/OFB/PKCS5Padding          32                          16
 * AES/OFB/ISO10126Padding       32                          16
 * AES/PCBC/NoPadding            16                          不支持
 * AES/PCBC/PKCS5Padding         32                          16
 * AES/PCBC/ISO10126Padding      32                          16
 *
 * @author yslei
 * @date 2019/8/9
 * @email leiyongsheng@hikcreate.com
 */
public class AESUtil {

    private static final String KEY_CIPHER = "AES/ECB/PKCS5Padding";
    private static final String KEY_CHARSET = "UTF-8";
    private static final String KEY_AES = "AES";

    /**
     * AES加密
     *
     * @param content 加密明文
     * @param key     加密32位key
     * @return
     */
    public static String encrypt(String content, String key) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return null;
        }
        String password = key;
        byte[] enCodeFormat = password.getBytes();
        //动态根据password的长度来选择128还是256bit
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
        try {
            byte[] bytes = content.getBytes(KEY_CHARSET);
            Cipher cipher = Cipher.getInstance(KEY_CIPHER);
            cipher.init(cipher.ENCRYPT_MODE, keySpec);
            //将加密并编码后的内容解码成字节数组
            byte[] result = cipher.doFinal(bytes);
            return Base64.encode(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AES解密
     *
     * @param content 解密内容
     * @param key     解密32位key
     * @return
     */
    public static String decrypt(String content, String key) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return null;
        }
        String password = key;
        byte[] bytes = Base64.decode(content);
        byte[] enCodeFormat = password.getBytes();
        //动态根据password的长度来选择128还是256bit
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);

        try {
            Cipher cipher = Cipher.getInstance(KEY_CIPHER);
            //初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] result = cipher.doFinal(bytes);
            return new String(result, KEY_CHARSET);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
