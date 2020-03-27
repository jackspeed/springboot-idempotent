package com.ycj.idempotent.utils;

import com.ycj.idempotent.constants.JConstant;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * AES对称加密
 */
public class AESUtil {

//    public static void main(String[] args) {
//        String ss = "111111";
//        System.out.println(encryptPassword(ss));
//        System.out.println(decryptPassword(encryptPassword(ss)));
//    }

    // 密码加密
    public static String encryptPassword(String value) {
        return encrypt(JConstant.AES_SECRET_PASSWORD, value);
    }

    //密码解密
    public static String decryptPassword(String value) {
        return decrypt(JConstant.AES_SECRET_PASSWORD, value);
    }

    //token解密
    public static String decryptToken(String value) {
        return decrypt(JConstant.AES_SECRET_TOKEN, value);
    }

    // token加密
    public static String encryptToken(String value) {
        return encrypt(JConstant.AES_SECRET_TOKEN, value);
    }

    public static String encrypt(String value) {
        return encrypt(JConstant.AES_SECRET, value);
    }

    public static String decrypt(String value) {
        return decrypt(JConstant.AES_SECRET, value);
    }

    /**
     * 加密
     *
     * @param secret 密钥
     * @param value  待加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String secret, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        SecretKeySpec keySpec = getKey(secret);
        IvParameterSpec iv = new IvParameterSpec(JConstant.AES_ENCRYPT_SALT.getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            return parseByte2HexStr(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param secret 密钥
     * @param value  待解密字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String secret, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        SecretKeySpec keySpec = getKey(secret);
        IvParameterSpec iv = new IvParameterSpec(JConstant.AES_ENCRYPT_SALT.getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] encrypted1 = parseHexStr2Byte(value);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成加密的密钥，保证长度为16位
     *
     * @param secret 用户的密钥
     * @return 生成的密钥
     */
    private static SecretKeySpec getKey(String secret) {
        byte[] bytes;
        try {
            bytes = secret.getBytes("UTF-8");
            return new SecretKeySpec(Arrays.copyOf(bytes, 16), "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}