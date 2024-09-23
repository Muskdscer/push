package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description: 平台用户列表BO
 * Create DateTime: 2020-03-26 15:14
 *
 *

 */
@Data
public class PlatformUserListBO extends BaseBO {

    private Long pageNo;

    private Long pageSize;


    @Override
    public void validate() {
        super.validate();

        if (pageNo == null
                || pageSize == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
