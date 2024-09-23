package com.push.common.webfacade.bo.shop;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020-03-31 09:59
 *
 *

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModifyShopBackUserInfoBO extends BaseBO {

    private String shopName;

    private String userName;

    private String password;

    private String phoneNumber;

    private Double rate;

    private String pushSite;

    private Integer pushNum;

    private String querySite;

    private String whiteIp;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(shopName)
                || StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(pushSite)
                || StringUtils.isBlank(querySite)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }

}
