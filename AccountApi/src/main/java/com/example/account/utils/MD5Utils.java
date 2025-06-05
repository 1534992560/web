package com.example.account.utils;

import java.util.Base64;

import java.security.MessageDigest;

public class MD5Utils {

    /**利用MD5进行加密*/
    public static String EncoderByMd5(String str) {
        String newStr = null;
        try {
            //确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Base64.Encoder base64en = Base64.getEncoder();
            //加密后的字符串
            newStr = base64en.encodeToString(md5.digest(str.getBytes("utf-8")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return newStr;
    }
}
