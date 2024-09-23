package com.push.controller.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.vo.platform.UpstreamAgentMapInfoVO;
import com.push.entity.BasePageBO;
import com.push.entity.agent.UpstreamAgentMapInfo;
import com.push.service.agent.UpstreamAgentMapInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 上游代理商代理Controller
 * Create DateTime: 2020/4/26 16:48
 *
 * 

 */
@RestController
@RequestMapping("agentMapBack")
public class UpstreamAgentBackMapInfoController extends BaseController {
    /*
     * addUpstreamAgentMap 增加
     * modifyUpstreamAgentMap 修改
     * removeUpstreamAgentMap 删除
     * listUpstreamAgentMap 查询加分页
     * */
    @Resource
    private UpstreamAgentMapInfoService upstreamAgentMapInfoService;


    @PostMapping("/listUpstreamAgentMap")
    public Map<String, Object> listUpstreamAgentMap(@RequestBody BasePageBO bo) {
        Long userId = getUserId();
        Page<UpstreamAgentMapInfo> page = new Page<>(bo.getPageNo(),bo.getPageSize());
        IPage<UpstreamAgentMapInfoVO> iPage = upstreamAgentMapInfoService.pageWithUpstream(page,userId,DeleteFlagEnum.NOT_DELETE.getCode());
        return returnResultMap(ResultMapInfo.GETSUCCESS,iPage);
    }
}
