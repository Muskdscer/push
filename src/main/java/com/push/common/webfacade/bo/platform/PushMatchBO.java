package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/10 11:11
 *
 *

 */
@Data
public class PushMatchBO extends BaseBO {

    private Long id;

    private Integer matchNum;

    private Integer start;

    private Integer end;

    @Override
    public void validate() {
        super.validate();
        if (id == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
