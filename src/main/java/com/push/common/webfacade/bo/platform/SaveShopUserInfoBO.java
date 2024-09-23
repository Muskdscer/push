package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:25
 *
 *

 */
@Data
public class SaveShopUserInfoBO extends BaseBO {

    private Long id;

    private String shopName;

    private String userName;

    private Long matchClassifyId;

    private String appId;

    private String appKey;

    private String password;

    private String phoneNumber;

    private Double rate;

    private String pushSite;

    private Integer pushNum;

    private String querySite;

    private String whiteIp;
    //警告阀值
    private Long alarmNumber;

    /**
     * 账号状态
     */
    private Integer status;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(userName)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(shopName)
                || rate == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
