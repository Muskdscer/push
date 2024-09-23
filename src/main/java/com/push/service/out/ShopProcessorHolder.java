package com.push.service.out;

import com.push.common.enums.ShopTypeEnum;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-21 15:02
 *
 * 

 */
@Component
public class ShopProcessorHolder {

    @Autowired
    private Map<String, ShopProcessor> shopProcessors;

    /**
     * 获取Shop处理器
     *
     * @param type 商户类别
     * @return 对应的商户处理器
     */
    public ShopProcessor findShopProcessor(ShopTypeEnum type) {
        if (type == null) {
            return null;
        }
        return findShopProcessor(type.getClassPre());
    }

    private ShopProcessor findShopProcessor(String type) {
        String className = generateClassName(type);
        ShopProcessor shopProcessor = shopProcessors.get(className);
        if (shopProcessor == null) {
            throw new CommonException(ErrorEnum.COMMON_BUSINESS_ERROR);
        }
        return shopProcessor;
    }

    private String generateClassName(String type) {
        return type + ShopProcessor.class.getSimpleName();
    }
}
