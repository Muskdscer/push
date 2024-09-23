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
public class YiChengCallbackOrderStatusBO extends BaseBO {

    /**
     * appId
     */
    private String userId;

    /**
     * 业务类型
     */
    private String bizId;

    /**
     * 商户订单号
     */
    private String ejId;

    /**
     * 平台订单号
     */
    private String downstreamSerialno;

    /**
     * 状态
     */
    private String status;

    /**
     * 签名
     */
    private String sign;

    /**
     * 网厅流水号
     */
    private String voucher;


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(sign)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
