package com.push.common.utils;

import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;

/**
 * Description: 拼音工具类
 * Create DateTime: 2020-04-21 13:06
 *
 * 

 */
public class PinYinUtils {

    static {
        Bopomofo4j.local();
    }

    /**
     * 获取首字母大写的拼音
     */
    public static String getPinYinWithFirstUpper(String chinese) {
        return Bopomofo4j.pinyin(chinese, ToneType.WITHOUT_TONE, false, true, "");
    }

    /**
     * 获取大写的拼音
     */
    public static String getPinYinWithUpper(String chinese) {
        return Bopomofo4j.pinyin(chinese, ToneType.WITHOUT_TONE, true, true, "");
    }

    /**
     * 获取小写的拼音
     */
    public static String getPinYinWithLower(String chinese) {
        return Bopomofo4j.pinyin(chinese, ToneType.WITHOUT_TONE, false, false, "");
    }

}
