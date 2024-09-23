package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:25
 *
 * 

 */
@Data
public class ShopEditAgentUserInfoBO extends BaseBO {
    /**
     * id
     */
    private Long id;

    /**
     * 商户代理商拥有者姓名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 商户代理商企业名称
     */
    private String shopAgentName;

    /**
     * 商户代理商手机号
     */
    private String phoneNumber;


    @Override
    public void validate() {
        super.validate();
        if (id == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
