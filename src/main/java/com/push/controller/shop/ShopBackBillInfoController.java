package com.push.controller.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.ListShopBillBO;
import com.push.common.webfacade.vo.platform.ShopBillInfoVO;
import com.push.service.shop.ShopBillInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/3/30 19:21
 *
 * 

 */
@RestController
@RequestMapping("back/shopBill")
public class ShopBackBillInfoController extends BaseController {


    @Resource
    private ShopBillInfoService shopBillInfoService;

    @RequestMapping(value = "listShopBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopBill(@RequestBody ListShopBillBO bo) {
        bo.validate();
        bo.setShopUserId(getUserId());
        IPage<ShopBillInfoVO> page = shopBillInfoService.listShopBill(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", page.getRecords());
        returnMap.put("total", page.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }

}
