package com.push.controller.upstream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.ListUpstreamBillBO;
import com.push.common.webfacade.vo.platform.UpstreamBillInfoVO;
import com.push.service.upstream.UpstreamBillInfoService;
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
@RequestMapping("back/upstreamBill")
public class UpstreamBackBillInfoController extends BaseController {

    @Resource
    private UpstreamBillInfoService upstreamBillInfoService;

    @RequestMapping(value = "listUpstreamBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopBill(@RequestBody ListUpstreamBillBO bo) {
        bo.validate();
        bo.setUpstreamUserId(getUserId());
        IPage<UpstreamBillInfoVO> page = upstreamBillInfoService.listUpstreamBill(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", page.getRecords());
        returnMap.put("total", page.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }

}
