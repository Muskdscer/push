package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 添加平台用户BO
 * Create DateTime: 2020-03-26 14:47
 *
 *

 */
@Getter
@Setter
public class PlatformUserAddBO extends BaseBO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 账号状态
     */
    private Integer status;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(userName)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(realName)
                || roleId == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
