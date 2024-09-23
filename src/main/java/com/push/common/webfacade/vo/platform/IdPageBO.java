package com.push.common.webfacade.vo.platform;

import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.entity.BasePageBO;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:10
 *
 *

 */
@Data
public class IdPageBO extends BasePageBO {

    private Long agentId;

    @Override
    public void validate() {
        super.validate();
        if (agentId == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
