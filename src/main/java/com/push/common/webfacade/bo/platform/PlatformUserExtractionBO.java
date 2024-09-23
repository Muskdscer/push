package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/3/30 15:22
 *
 * 

 */
@Data
public class PlatformUserExtractionBO extends BaseBO {

    //银行名称
    private String bankName;
    //支行名称
    private String branchBank;
    //银行卡号
    private String bankCardNumber;
    //持卡人姓名
    private String cardUserName;
    //金额
    private BigDecimal extractionMoney;
    //平台提现人手机号
    private String userMobile;

    @Override
    public void validate() {
        super.validate();
        if (extractionMoney == null
                || StringUtils.isBlank(bankName)
                || userMobile == null
                || StringUtils.isBlank(branchBank)
                || StringUtils.isBlank(bankCardNumber)
                || StringUtils.isBlank(cardUserName)) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
