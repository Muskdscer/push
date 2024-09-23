package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/10 16:00
 *
 * 

 */
@Data
public class PushMatchListBO extends BaseBO {

    private Long matchClassifyId;

    private String operatorType;

    private BigDecimal money;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(operatorType) || money == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
