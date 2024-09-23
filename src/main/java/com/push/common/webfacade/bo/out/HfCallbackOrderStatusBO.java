package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020/5/22 9:40
 *
 * 

 */
@Data
public class HfCallbackOrderStatusBO extends BaseBO {

    //资源编号
    private String api_code;
    //商户订单号
    private String orderid;
    //平台订单号
    private String cpparam;
    //订单状态 0=成功  1=失败
    private Integer status;
    //失败返回详细错误
    private String message;
    //支付凭证
    private String spref_url;
    //签名
    private String sign;
    //网厅流水号
    private String order_no;

    private String paysapi_id;


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(api_code)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
