package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:22
 *
 * 

 */
@Data
public class ModifyUpstreamAgentUserInfoBO extends BaseBO {

    private Long id;

    private String userName;

    private String password;

    private String upstreamName;

    private String phoneNumber;

    private Integer status;

    @Override
    public void validate() {
        super.validate();
        if (id == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
