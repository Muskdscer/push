package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.GetLoginLogVO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.service.agent.ShopAgentLogLoginService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/4/7 9:22
 *
 *

 */
@RestController
@RequestMapping("shopAgentLoginLog")
public class ShopAgentLoginLogController extends BaseController {

    @Resource
    private ShopAgentLogLoginService shopAgentLogLoginService;

    @RequestMapping(value = "listShopAgentLoginLog", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopLoginLog(@RequestBody LoginLogBO bo) {
        bo.validate();
        Page<LoginLogVO> page = shopAgentLogLoginService.getShopLoginLog(bo);
        GetLoginLogVO getLoginLogVO = new GetLoginLogVO();
        getLoginLogVO.setData(page.getRecords());
        getLoginLogVO.setTotal(page.getTotal());
        if (page != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, getLoginLogVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }


}
