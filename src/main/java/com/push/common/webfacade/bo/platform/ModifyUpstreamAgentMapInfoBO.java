package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 17:51
 *
 * 

 */
@Data
public class ModifyUpstreamAgentMapInfoBO extends BaseBO {

    private Long id;

    private Double rate;

    @Override
    public void validate() {
        super.validate();
        if (id == null || rate == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
