package com.push.controller.out.shop;

import com.push.common.controller.BaseController;
import com.push.common.enums.ShopTypeEnum;
import com.push.service.out.BaseShopProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/7/9 09:27
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("callback")
public class ShopCallbackOrderStatusController extends BaseController {

    @Resource
    private BaseShopProcessor baseShopProcessor;

    /**
     * 商户回调
     *
     * @param type     商户类型  {@link ShopTypeEnum} type
     * @param paramMap 商户回调参数
     * @return 处理结果
     */
    @RequestMapping("/{type}/result")
    public String shopCallbackOrderStatus(@PathVariable String type, @RequestBody Map<String, Object> paramMap) {
        return baseShopProcessor.execute(type, paramMap);
    }
}
