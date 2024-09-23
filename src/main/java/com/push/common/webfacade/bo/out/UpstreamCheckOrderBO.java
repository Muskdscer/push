package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * Description:
 * Create DateTime: 2020/4/28 15:43
 *
 * 

 */
@Data
public class UpstreamCheckOrderBO extends BaseBO {

    private String appId;

    private String orderNo;

    private String sign;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(appId)){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
