package com.push.common.webfacade.bo.shop;

import com.push.common.webfacade.bo.BaseBO;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020-04-07 16:14
 *
 * 

 */
@Data
public class ModifyShopBackPasswordBO extends BaseBO {

    /**
     * 新密码
     */
    private String newPwd;

    @Override
    public void validate() {
        super.validate();
    }
}
