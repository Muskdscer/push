package com.push.common.webfacade.bo.platform;

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
public class ShopAgentExtractionBO extends BaseBO {

    private Long id;

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

    /**
     * 用户手机号
     */
    private String userMobile;


    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(bankCardNumber)
                || StringUtils.isBlank(bankName)
                || StringUtils.isBlank(branchBank)
                || StringUtils.isBlank(cardUserName)
                || id == null
                || userMobile == null
                || extractionMoney == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
