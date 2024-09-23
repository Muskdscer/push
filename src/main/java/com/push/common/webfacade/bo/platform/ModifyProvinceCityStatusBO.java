package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 分配菜单BO
 * Create DateTime: 2020-03-26 16:06
 *
 * 

 */
@Data
public class ModifyProvinceCityStatusBO extends BaseBO {

    private String cityCodes;

    private String phoneOperator;

    @Override
    public void validate() {
        super.validate();

        if (StringUtils.isBlank(cityCodes) || StringUtils.isBlank(phoneOperator)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
