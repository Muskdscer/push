package com.push.common.constants;

public class DateConstant {

    /**
     * GMT+8
     */
    public static final String TIME_ZONE = "GMT+8";

    /**
     * yyyyMMdd
     */
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_TWO = "yyyy-MM-dd";

    /**
     * yyyy.MM.dd
     */
    public static final String DATE_FORMAT_THREE = "yyyy.MM.dd";

    /**
     * dd/MM/yyyy
     */
    public static final String DATE_FORMAT_FOUR = "dd/MM/yyyy";

    /**
     * yyyy/MM/dd
     */
    public static final String DATE_FORMAT_FIVE = "yyyy/MM/dd";

    /**
     * dd-MM-yyyy
     */
    public static final String DATE_FORMAT_SIX = "dd-MM-yyyy";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATE_TIME_FORMAT_TWO = "yyyy-MM-dd HH:mm";

    /**
     * HH:mm
     */
    public static final String TIME_FORMAT = "HH:mm";

    /**
     * yyyyMMdd HH:mm
     */
    public static final String DATE_TIME_FORMAT_THREE = "yyyyMMdd HH:mm";

    public static final String DATE_TIME_FORMAT_SIX = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 时间格式数组（年月日）
     */
    public static final String[] DATE_FORMAT_ARRAY = {DATE_FORMAT, DATE_FORMAT_TWO, DATE_FORMAT_THREE,
            DATE_FORMAT_FIVE};

    /**
     * 时间格式数组（年月日 时分秒）
     */
    public static final String[] DATE_FORMAT_FULL = {DATE_FORMAT, DATE_FORMAT_TWO, DATE_FORMAT_THREE, DATE_FORMAT_FIVE,
            DATE_FORMAT_NORMAL, DATE_TIME_FORMAT_TWO, DATE_TIME_FORMAT_SIX};

    /**
     * 时间格式校验
     */
    public static final String DATE_FORMAT_PATTER = "([\\d]{4}(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(("
            + "(0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-8])))))|((((([02468][048])|"
            + "([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|"
            + "(3[01])))|(((0[469])|11)((0[1-9])|([12][1-9])|30))|(02((0[1-9])|(1[0-9])|(2[1-9])))))";

    /**
     * yyyyMMddHHmmssss
     */
    public static final String DATE_TIME_FORMAT = "yyyyMMddHHmmssss";

    /**
     * 精确到毫秒 yyyyMMddHHmmssSSS
     */
    public static final String DATE_TIME_FORMAT_MSEC = "yyyyMMddHHmmssSSS";

    /**
     * yyyyMMddHHmmss
     */
    public static final String DATE_TIME_FORMAT_FOUR = "yyyyMMddHHmmss";

    /**
     * MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMAT_FIVE = "MM-dd HH:mm:ss";

    /**
     * MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMAT_SEVEN = "HH:mm:ss";


}
