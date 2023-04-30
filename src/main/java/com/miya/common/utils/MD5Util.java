package com.miya.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }


    /**
     * 为了和前端的盐进行统一；
     */
    public static final String SALT = "1a2b3c4d";


    public static String inputPassToFromPass(String inputPass){
        String str = String.valueOf(SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4));
        return md5(str);
    }


    public static String formPassToDBPas(String formPass, String salt){
        String str = String.valueOf(salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4));
        return md5(str);
    }


    public static String inputPassTODBPass(String inputPass, String salt){
        String formPass = inputPassToFromPass(inputPass);
        String DBPass = formPassToDBPas(formPass, salt);
        return DBPass;
    }

    public static void main(String[] args) {
        String pass = "123456";
        System.out.println(inputPassToFromPass(pass));
        System.out.println(formPassToDBPas(inputPassToFromPass(pass), "1a2b3c4d"));
        System.out.println(inputPassTODBPass(pass, "1a2b3c4d"));
    }
}
