package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.GetLoginLogVO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.service.agent.UpstreamAgentLogLoginService;
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
@RequestMapping("upstreamAgentLoginLog")
public class UpstreamAgentLoginLogController extends BaseController {

    @Resource
    private UpstreamAgentLogLoginService upstreamAgentLogLoginService;

    @RequestMapping(value = "listUpstreamAgentLoginLog", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUpstreamLoginLog(@RequestBody LoginLogBO bo) {
        bo.validate();
        Page<LoginLogVO> page = upstreamAgentLogLoginService.getUpstreamAgentLoginLog(bo);
        GetLoginLogVO getLoginLogVO = new GetLoginLogVO();
        getLoginLogVO.setData(page.getRecords());
        getLoginLogVO.setTotal(page.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, getLoginLogVO);
    }
}