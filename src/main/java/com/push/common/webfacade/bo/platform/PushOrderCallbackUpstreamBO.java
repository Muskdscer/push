package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/5/21 15:06
 *
 *

 */
@Data
public class PushOrderCallbackUpstreamBO extends BaseBO {

    //平台订单号集合
    private List<Long> orderIdList;

    @Override
    public void validate() {
        super.validate();
        if (CollectionUtils.isEmpty(orderIdList)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
