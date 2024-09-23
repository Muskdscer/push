package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020-03-31 09:26
 *
 * 

 */
@Data
public class ModifyUpstreamUserInfoBO extends BaseBO {

    private Long id;

    private Long matchClassifyId;

    private String password;

    private String phoneNumber;

    private String upstreamName;

    private Double rate;

    private String bankName;

    //可以为空
    private String branchBank;

    private String bankCardNumber;

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
        if (StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(upstreamName)
                || rate == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
