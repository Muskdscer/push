package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 17:45
 *
 *

 */
@Data
public class AddShopAgentMapBO extends BaseBO {
    //代理商id
    private Long agentId;
    //商户id
    private Long shopId;
    //费率
    private Double rate;

    @Override
    public void validate() {
        super.validate();
        if (agentId == null || shopId == null || rate == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}