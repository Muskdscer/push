package com.push.common.webfacade.bo.agent;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/27 9:11
 *
 * 

 */
@Data
public class UpstreamAgentBackExtractionBO extends BaseBO {

    private String bankName;

    private String branchBank;

    private String bankCardNumber;

    private String cardUserName;

    private BigDecimal extractionMoney;

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(bankName)
                || StringUtils.isBlank(branchBank)
                || StringUtils.isBlank(bankCardNumber)
                || extractionMoney == null){
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
}
