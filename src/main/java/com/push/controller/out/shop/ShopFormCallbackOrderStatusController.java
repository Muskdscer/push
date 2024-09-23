package com.push.controller.out.shop;

import com.push.common.controller.BaseController;
import com.push.common.enums.ShopTypeEnum;
import com.push.service.out.BaseShopProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/7/27 09:22
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("callback")
public class ShopFormCallbackOrderStatusController extends BaseController {

    @Resource
    private BaseShopProcessor baseShopProcessor;

    /**
     * 商户回调
     *
     * @param type     商户类型  {@link ShopTypeEnum} type
     * @param paramMap 商户回调参数
     * @return 处理结果
     */
    @RequestMapping("/{type}/form/result")
    public String shopCallbackOrderStatus(@PathVariable String type, @RequestParam Map<String, Object> paramMap) {
        return baseShopProcessor.execute(type, paramMap);
    }
}
