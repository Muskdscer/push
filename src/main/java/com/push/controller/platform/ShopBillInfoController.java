package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.ListShopBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.ShopBillInfoVO;
import com.push.service.shop.ShopBillInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/3/30 19:21
 *
 * 

 */
@RestController
@RequestMapping("shopBill")
public class ShopBillInfoController extends BaseController {


    @Resource
    private ShopBillInfoService shopBillInfoService;

    @RequestMapping(value = "/listShopBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopBill(@RequestBody ListShopBillBO bo) {
        bo.validate();
        IPage<ShopBillInfoVO> page = shopBillInfoService.listShopBill(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", page.getRecords());
        returnMap.put("total", page.getTotal());
        List<ListBillAllTypeStatisticsVO> vo = shopBillInfoService.shopBillStatistics(bo);
        returnMap.put("billStatisticsMoney",vo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }

}
