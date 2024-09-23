package com.push.service.out.impl;

import com.push.common.enums.UserStatusEnum;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.out.ShopCommonProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/7/9 15:37
 *
 *

 */
@Service("shopCommonProcessor")
public class AllShopCommonProcessor implements ShopCommonProcessor {

    @Value("${whiteIp.allBan}")
    private String allBanWhiteIp;

    @Value("${whiteIp.allPass}")
    private String allPassWhiteIp;

    @Override
    public boolean validateShopStatus(ShopUserInfo shopUserInfo) {
        return !shopUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue());
    }

    @Override
    public boolean validateWhiteIp(String whiteIp, String ipAddr) {
        if (StringUtils.isBlank(whiteIp)) {
            return false;
        }
        if (!whiteIp.contains(allPassWhiteIp)) {
            if (whiteIp.contains(allBanWhiteIp)) {
                return false;
            }
            return !StringUtils.isBlank(whiteIp) && whiteIp.contains(ipAddr);
        }
        return true;
    }

    @Override
    public boolean validateOrderExpire(Date nowDate, Date createTime, Integer orderExpireTime, Integer upBackNum) {
        Date date = DateUtils.addSeconds(createTime, orderExpireTime);
        if (nowDate.after(date)) {
            return upBackNum == 0;
        }
        return true;
    }
}
