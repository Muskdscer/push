package com.push.controller.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.service.agent.ShopAgentBillInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 商户代理商账单
 * Create DateTime: 2020/4/28 10:54
 *
 * 

 */
@RestController
@RequestMapping("shopAgentBillBack")
public class ShopAgentBillBackInfoController extends BaseController {
    @Resource
    private ShopAgentBillInfoService shopAgentBillInfoService;

    @PostMapping("listBillShopAgent")
    public Map<String,Object> listBillShopAgent(@RequestBody ListAgentBillBO bo){
        bo.validate();
        bo.setUserId(getUserId());
        IPage<AgentBillInfoVO> page = shopAgentBillInfoService.listBillShopAgent(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("records",page.getRecords());
        returnMap.put("total",page.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }
}
