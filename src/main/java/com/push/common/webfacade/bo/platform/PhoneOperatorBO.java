package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/5/8 11:14
 *
 *

 */
@Data
@Accessors(chain = true)
public class PhoneOperatorBO extends BaseBO {

    private String phoneOperator;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(phoneOperator)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
