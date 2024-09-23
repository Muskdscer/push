package com.push.common.utils;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * Create DateTime: 2020-03-16 15:01
 *
 * 

 */
public class ValidateUtil {

    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        String regexp = "^1[3-9]\\d{9}$";
        return validate(regexp, mobile);
    }

    public static boolean isValidNickname(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            return false;
        }
        if (nickname.length() < 2 || nickname.length() > 16) {
            return false;
        }
        String regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$";
        return validate(regexp, nickname);
    }

    public static boolean isValidPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            return false;
        }
        String regexp = "([\\d]*[a-zA-Z]+[\\d]*)+";
        return validate(regexp, password);
    }

    public static boolean isRealName(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if (name.length() < 2 || name.length() > 16) {
            return false;
        }
        return isChinese(name);
    }


    public static boolean isChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (str.contains("·")) {
            str = str.replace("·", "");
        }
        return str.matches("^[\u4E00-\u9FA5]+$");
    }

    public static boolean isBankCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return false;
        }
        return cardNumber.matches("^\\d{15,25}$");
    }


    public static boolean validate(String regexp, String str) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isIdentityNumber(String number) {
        if ((StringUtils.isBlank(number)) || (!number.toUpperCase().matches("^[0-9]{17}[0-9X]$"))) {
            return false;
        }
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String[] parityBits = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int power = 0;
        for (int i = 0; i < 17; i++) {
            power += Integer.parseInt(number.charAt(i) + "") * weights[i];
        }
        Pattern p = Pattern.compile("^(\\d{6})(\\d{8})[0-9]{3}[0-9X]$");
        Matcher m = p.matcher(number);
        if (m.find()) {
            DateUtil.parse(m.group(2), "yyyyMMdd");
        } else {
            return false;
        }
        return parityBits[(power % 11)].equals(number.substring(17));
    }


    public static boolean isEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            return email.matches("^[0-9a-zA-Z_\\-]{1,}@[0-9a-zA-Z_\\-]{1,}\\.\\w{1,5}(\\.\\w{1,5})?$");
        } else {
            return false;
        }
    }

    /**
     * 是否为整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
