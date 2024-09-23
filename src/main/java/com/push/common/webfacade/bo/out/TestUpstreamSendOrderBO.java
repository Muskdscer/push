package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/3/31 17:13
 *
 * 

 */
@Data
public class TestUpstreamSendOrderBO extends BaseBO {

    //appId,平台为用户分配的,且唯一
    private String appId;

    /*//验签
    private String sign;*/

    //手机运营商
    private String phoneOperator;

    //手机号
    private String phoneNum;

    //订单时间
    private Integer orderExpireTime;

    //订单号
    private String orderNo;

    //订单价格
    private BigDecimal orderPrice;

    //运营商类型
    private String type;


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(appId)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
