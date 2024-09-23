package com.push.entity;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/3/27 19:03
 *
 *

 */
@Data
public class BasePageBO extends BaseBO {

    private Long pageNo;
    private Long pageSize;

    @Override
    public void validate() {
        super.validate();
        if (pageNo == null || pageSize == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
