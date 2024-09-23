package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/6/10 18:18
 *
 *

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChangeOrderFailedBO extends BaseBO {

    /**
     * 平台订单号集合
     */
    private List<String> platformOrderNoList;

    @Override
    public void validate() {
        super.validate();

        if (CollectionUtils.isEmpty(platformOrderNoList)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
