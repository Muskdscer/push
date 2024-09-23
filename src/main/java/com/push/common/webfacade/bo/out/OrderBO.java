package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/4/1 15:33
 *
 *

 */
@Data
public class OrderBO extends BaseBO {
    //平台订单号
    private String platformOrderNo;

    //商户订单号
    private String shopOrderNo;

    //状态
    private Integer status;

    //详情
    private String message;

    //凭证
    private String certificate;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(platformOrderNo) || StringUtils.isBlank(shopOrderNo)
                || status == null || StringUtils.isBlank(certificate)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
