package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description: 角色菜单BO
 * Create DateTime: 2020-03-30 14:10
 *
 *

 */
@Data
public class RoleMenuBO extends BaseBO {

    /**
     * 角色ID
     */
    private Long roleId;

    @Override
    public void validate() {
        super.validate();

        if (roleId == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
