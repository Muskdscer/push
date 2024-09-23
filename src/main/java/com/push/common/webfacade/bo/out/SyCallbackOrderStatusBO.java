package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/6/10 15:08
 *
 *

 */
@Data
public class SyCallbackOrderStatusBO extends BaseBO {

    //商户id
    private String coopId;
    //平台订单号
    private String tranid;
    //response类型
    private String responseType;
    //充值结果
    private String failedCode;
    //失败原因
    private String failedReason;
    //电信等通信公司的官方单号
    private String orderNo;
    //签名
    private String sign;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(coopId)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
