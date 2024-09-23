package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description: 角色列表BO
 * Create DateTime: 2020-03-26 15:56
 *
 * 

 */
@Data
public class RoleListBO extends BaseBO {

    /**
     * 页号
     */
    private Long pageNo;

    /**
     * 页面大小
     */
    private Long pageSize;

    @Override
    public void validate() {
        super.validate();

        if (pageNo == null
                || pageSize == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
