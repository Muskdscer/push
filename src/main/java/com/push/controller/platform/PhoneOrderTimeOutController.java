package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.PhoneOrderTimeOutListBO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.platform.PhoneOrderTimeOutService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/6/10 17:46
 *
 * 

 */
@RestController
@RequestMapping("orderTimeOut")
public class PhoneOrderTimeOutController extends BaseController {

    @Resource
    private PhoneOrderTimeOutService phoneOrderTimeOutService;

    /**
     * 分页查询超时订单列表
     *
     * @param bo PhoneOrderTimeOutListBO
     * @return 超时订单列表
     */
    @RequestMapping("listOrder")
    public Map<String, Object> listOrder(@RequestBody PhoneOrderTimeOutListBO bo) {
        bo.validate();

        Page<PhoneOrderRecord> page = phoneOrderTimeOutService.queryOrderTimeOutList(bo.getPlatformOrderNo(),
                bo.getUpstreamOrderNo(), bo.getPhoneNum(), bo.getPushStatus(), bo.getShopName(), bo.getUpstreamName(),
                bo.getStartTime(), bo.getEndTime(), bo.getPageNo(), bo.getPageSize());
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }
}
