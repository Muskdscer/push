package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description: 平台用户详情BO
 * Create DateTime: 2020-03-26 15:25
 *
 * 

 */
@Data
public class PlatformUserDetailBO extends BaseBO {

    /**
     * 用户ID
     */
    private Long id;

    @Override
    public void validate() {
        super.validate();

        if (id == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
