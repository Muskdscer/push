package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.service.shop.ShopLogOperateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/4/7 9:23
 *
 * 

 */
@RestController
@RequestMapping("shopOperateLog")
public class ShopOperateLogController extends BaseController {

    @Resource
    private ShopLogOperateService shopLogOperateService;

    @RequestMapping(value = "/listShopOperateLog", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopOperateLog(@RequestBody OperateBO bo) {
        bo.validate();
        IPage<OperateVO> page = shopLogOperateService.listShopOperateLog(bo);
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", page.getRecords());
        returnMap.put("total", page.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }
}
