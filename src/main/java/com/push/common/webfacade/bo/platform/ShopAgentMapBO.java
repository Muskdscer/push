package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.entity.BasePageBO;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 17:21
 *
 *

 */
@Data
public class ShopAgentMapBO extends BasePageBO {
    private Long id;

    @Override
    public void validate() {
        super.validate();
        if (id == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
