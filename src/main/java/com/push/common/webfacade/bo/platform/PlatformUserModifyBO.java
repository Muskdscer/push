package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: 修改平台用户BO
 * Create DateTime: 2020-03-26 15:08
 *
 *

 */
@Getter
@Setter
public class PlatformUserModifyBO extends PlatformUserAddBO {

    /**
     * 用户ID
     */
    private Long id;

    @Override
    public void validate() {
        if (id == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
