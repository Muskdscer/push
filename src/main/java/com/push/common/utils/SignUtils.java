package com.push.common.utils;

import cn.hutool.crypto.SecureUtil;
import com.push.common.enums.SignTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Description: 签名工具类
 * Create DateTime: 2020-03-27 15:14
 *
 *

 */
@Slf4j
public class SignUtils {

    /**
     * 判断签名是否正确。使用MD5签名。
     *
     * @param data 结构体
     * @param sign 客户端签名
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValidWithMD5(final TreeMap<String, String> data, String sign) throws Exception {
        return isSignatureValid(data, sign, SignTypeEnum.MD5);
    }

    /**
     * 判断签名是否正确。使用SHA256签名。
     *
     * @param data 结构体
     * @param sign 客户端签名
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValidWithSha256(final TreeMap<String, String> data, String sign) throws Exception {
        return isSignatureValid(data, sign, SignTypeEnum.SHA256);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data     结构体
     * @param sign     客户端签名
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(final TreeMap<String, String> data, String sign, SignTypeEnum signType) throws Exception {
        if (data.isEmpty()) {
            return false;
        }
        return generateSignature(data, signType).equals(sign);
    }

    /**
     * 生成签名.
     *
     * @param data     待签名数据
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final TreeMap<String, String> data, SignTypeEnum signType) {
        return executeSignature(data, signType);
    }

    public static String syGenerateSignature(final TreeMap<String, String> data, SignTypeEnum signType, String appkey) {
        StringBuffer sb = new StringBuffer();
        data.forEach((key, value) -> {
            if (StringUtils.isBlank(value)) {
                log.error("【签名】签名生成失败：获取{}值为null，全部数据为{}", key, data.toString());
                throw new NullPointerException(String.format("%s relation value is null", key));
            }
            if (value.trim().length() > 0) {
                sb.append(key.trim()+"="+value.trim()+"&");
            }
        });
        sb.append(appkey);
        log.info("验签字符串:{}",sb.toString());
        if (SignTypeEnum.MD5.equals(signType)) {
            return SecureUtil.md5(sb.toString());
        } else if (SignTypeEnum.SHA256.equals(signType)) {
            return SecureUtil.sha256(sb.toString());
        } else {
            log.error("【签名】生成签名失败：无效的加密方式！");
            throw new RuntimeException(String.format("Invalid sign_type: %s", signType));
        }
    }

    public static String executeSignature(SortedMap<String, String> data, SignTypeEnum signType) {
        StringBuffer sb = new StringBuffer();
        data.forEach((key, value) -> {
            if (StringUtils.isBlank(value)) {
                log.error("【签名】签名生成失败：获取{}值为null，全部数据为{}", key, data.toString());
                throw new NullPointerException(String.format("%s relation value is null", key));
            }
            if (value.trim().length() > 0) {
                sb.append(value.trim());
            }
        });

        if (SignTypeEnum.MD5.equals(signType)) {
            return SecureUtil.md5(sb.toString());
        } else if (SignTypeEnum.SHA256.equals(signType)) {
            return SecureUtil.sha256(sb.toString());
        } else {
            log.error("【签名】生成签名失败：无效的加密方式！");
            throw new RuntimeException(String.format("Invalid sign_type: %s", signType));
        }
    }
}
