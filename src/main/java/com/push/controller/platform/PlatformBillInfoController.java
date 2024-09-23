package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.ListPlatFormBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.PlatformBillInfoVO;
import com.push.service.platform.PlatformBillInfoService;
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
 * Create DateTime: 2020/3/30 16:44
 *
 *

 */
@RestController
@RequestMapping("platformBill")
public class PlatformBillInfoController extends BaseController {

    @Resource
    private PlatformBillInfoService platformBillInfoService;

    @RequestMapping(value = "/listPlatformBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listPlatformBill(@RequestBody ListPlatFormBillBO bo) {
        bo.validate();
        IPage<PlatformBillInfoVO> page = platformBillInfoService.listPlatformBill(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", page.getRecords());
        returnMap.put("total", page.getTotal());
        List<ListBillAllTypeStatisticsVO> vo = platformBillInfoService.platformBillStatistics(bo);
        returnMap.put("billStatisticsMoney",vo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }
}
