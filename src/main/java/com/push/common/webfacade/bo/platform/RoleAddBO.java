package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 角色添加BO
 * Create DateTime: 2020-03-26 15:37
 *
 *

 */
@Data
public class RoleAddBO extends BaseBO {

    /**
     * 角色名称
     */
    private String roleName;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(roleName)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
