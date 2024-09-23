package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 登录用户参数实体
 * Create DateTime: 2020-03-26 10:45
 *
 *

 */
@Data
public class LoginInfoBO extends BaseBO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
