package com.push.common.webfacade.bo.upstream;

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
public class ModifyUpstreamBackUserInfoBO extends BaseBO {

    private Long id;

    private String phoneNumber;

    private String upstreamName;

    private String bankName;

    //可以为空
    private String branchBank;

    private String bankCardNumber;

    //可以为空
    private String cardName;

    //可以为空
    private String pushSite;

    private String whiteIp;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(upstreamName)
                || StringUtils.isBlank(bankName)
                || StringUtils.isBlank(bankCardNumber)
                || StringUtils.isBlank(cardName)
                || StringUtils.isBlank(pushSite)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
