package com.push.common.webfacade.bo.agent;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/3/30 14:53
 *
 *

 */
@Data
public class ShopAgentBackExtractionBO extends BaseBO {

    //卡号
    private String bankCardNumber;

    //银行名称
    private String bankName;

    //支行名称
    private String branchBank;

    /**
     * 拥有者姓名
     */
    private String cardUserName;

    //金额
    private BigDecimal extractionMoney;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(bankCardNumber)
                || StringUtils.isBlank(bankName)
                || StringUtils.isBlank(branchBank)
                || StringUtils.isBlank(cardUserName)
                || extractionMoney == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
