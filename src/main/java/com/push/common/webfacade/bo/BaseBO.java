package com.push.common.webfacade.bo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 基础BO
 * Create DateTime: 2020-03-15 00:28
 *
 *
 */
@Data
public class BaseBO {

    /**
     * 平台类型(iOS/Android/独立浏览器browser)
     */
    private String platform;

    /**
     * 应用版本
     */
    private String version;

    /**
     * 检查参数是否正确
     */
    public void validate() {
        if (StringUtils.isBlank(platform)
                || StringUtils.isBlank(version)) {
            //throw new CommonException(ErrorEnum.COMMON_PARAMS_MISSING);
        }
    }
}
