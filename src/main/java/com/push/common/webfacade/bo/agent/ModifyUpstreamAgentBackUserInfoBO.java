package com.push.common.webfacade.bo.agent;

import com.push.common.webfacade.bo.BaseBO;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:22
 *
 *

 */
@Data
public class ModifyUpstreamAgentBackUserInfoBO extends BaseBO {

    private String userName;

    private String password;

    private String upstreamName;

    private String phoneNumber;

    @Override
    public void validate() {
        super.validate();
    }
}
