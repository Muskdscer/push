package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/4/26 14:45
 *
 *

 */
@Data
public class AddUpstreamAgentUserInfoBO extends BaseBO {

    private String userName;
    private String password;
    private String upstreamName;
    private String phoneNumber;
    private Integer status;


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(userName)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(upstreamName)
                || StringUtils.isBlank(phoneNumber)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
