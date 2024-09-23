package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/7/24 18:25
 *
 *

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SjCallbackOrderStatusBO extends BaseBO {

    private Integer code;

    private String msg;

    /**
     * 平台订单号
     */
    private String tradeNoThird;

    /**
     * 运营商订单号
     */
    private String tradeNo;

    private String signstr;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(signstr)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
