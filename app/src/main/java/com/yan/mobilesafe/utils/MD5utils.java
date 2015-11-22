package com.yan.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * Created by yan on 2015/11/22.
 */
public class MD5utils {
    /**
     * Md5加密
     */
    public static String encode(String password)  {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = messageDigest.digest(password.getBytes());

        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : digest) {
            int i = b & 0xff; //获取字节的低八位
            String hexString = Integer.toHexString(i);  //转换成16进制

            if (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }
}
