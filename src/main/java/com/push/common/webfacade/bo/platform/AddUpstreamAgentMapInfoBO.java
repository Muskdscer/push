package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 17:07
 *
 * 

 */
@Data
public class AddUpstreamAgentMapInfoBO extends BaseBO {


    /**
     * 通道Id
     */
    private Long upstreamId;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 对应渠道费率
     */
    private Double rate;

    @Override
    public void validate() {
        super.validate();
        if (upstreamId == null || agentId == null || rate == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
