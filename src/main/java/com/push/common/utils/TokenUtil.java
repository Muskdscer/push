package com.push.common.utils;


import cn.hutool.core.util.IdUtil;

import java.util.Date;
import java.util.UUID;


public class TokenUtil {


    public static String generateToken() {
        return generateTimeStampStr().concat(generateUUID());
    }

    public static String generateTimeStampStr() {
        return DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static String generateOrderNo() {
        return IdUtil.getSnowflake(0, 0).nextIdStr();
    }


    public static Boolean checkToken(String token) {
        String timestamp = token.substring(0, 14);
        Long time = Long.valueOf(generateTimeStampStr()) - Long.valueOf(timestamp);
        if (time > new Long(60 * 10)) {
            return false;
        }
        return true;
    }

    //
//    public static void main(String[] args) {
//        String timestamp = generateTimeStampStr();
//        checkToken("20200308130014b4efc9cdfbc646a1b9eb15eaf32fd12e");
//        System.out.println(timestamp);
//    }
}
