package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:29
 *
 *

 */
@Data
public class SaveUpstreamUserBO extends BaseBO {

    private String userName;
    private String password;
    private String repeatPassword;
    private String phoneNumber;
    private Double rate;
    private String bankName;
    //支行名称
    private String branchBank;
    //银行卡号
    private String bankCardNumber;
    //持卡人姓名
    private String cardName;
    //推送地址
    private String pushSite;
    //名单ip，可以多个，中间用,隔开
    private String whiteIp;

    @Override
    public void validate() {
        super.validate();

        if (userName == null || password == null || repeatPassword == null || phoneNumber == null || rate == null || bankName == null
                || branchBank == null || bankCardNumber == null || cardName == null || pushSite == null || whiteIp == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        if (password.equals(repeatPassword)) {
            throw new CommonException(ErrorEnum.PASSWORD_NOT_EQUAL);
        }
    }


}
