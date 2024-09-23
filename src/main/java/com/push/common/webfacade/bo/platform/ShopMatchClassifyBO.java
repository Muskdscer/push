package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/4/20 16:29
 *
 *

 */
@Data
public class ShopMatchClassifyBO extends BaseBO {

    private Long id;

    private String name;

    private String value;

    private String remark;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(value))
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
    }
}
