package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/3/30 11:50
 *
 *

 */
@Data
public class SaveUpstreamUserInfoBO extends BaseBO {

    private Long id;

    private String userName;

    private String password;

    private String phoneNumber;

    private Long matchClassifyId;

    private String upstreamName;

    private Double rate;

    private String bankName;

    //可以为空
    private String cardName;

    //可以为空
    private String pushSite;

    private String whiteIp;
    //警告阀值
    private Long alarmNumber;

    /**
     * 账号状态
     */
    private Integer status;

    /*private String appId;

    private String appKey;*/


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(userName)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(upstreamName)
                || rate == null
                /*|| StringUtils.isBlank(appKey)
                || StringUtils.isBlank(appId)*/) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
