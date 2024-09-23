package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/4/1 13:48
 *
 *

 */
@Data
@ToString
public class TestCallbackOrderStatusBO extends BaseBO {

    //appId,平台为用户分配的,且唯一
    private String appId;

    //验签
    private String sign;

    //平台订单号
    private String userOrderNumber;

    //商户订单号
    private String demandNumber;

    //状态
    private Integer status;

    //网厅流水号
    private String orderSn;

    //支付凭证
    private String certificate;

    //测试信息
    private String detailMessage;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(sign)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
