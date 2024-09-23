package com.push.common.webfacade.bo.upstream;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/3/30 15:38
 *
 * 

 */
@Data
public class UpstreamBackUserRechargeBO extends BaseBO {

    private Long upstreamUserId;

    private BigDecimal money;

    @Override
    public void validate() {
        super.validate();
        if (money == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
