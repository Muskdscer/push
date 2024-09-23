package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description: 分配菜单BO
 * Create DateTime: 2020-03-26 16:06
 *
 *

 */
@Data
public class DistributeMenuBO extends BaseBO {

    private Long roleId;

    private String menuIds;

    @Override
    public void validate() {
        super.validate();

        if (roleId == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
