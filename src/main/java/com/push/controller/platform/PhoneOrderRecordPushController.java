package com.push.controller.platform;

import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.webfacade.bo.platform.PushOrderCallbackUpstreamBO;
import com.push.service.platform.PhoneOrderRecordPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/5/21 15:03
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("phoneOrderPush")
public class PhoneOrderRecordPushController extends BaseController {

    @Resource
    private PhoneOrderRecordPushService phoneOrderRecordPushService;

    @PostMapping("pushOrderCallbackUpstream")
    public Map<String, Object> pushOrderCallbackUpstream(@RequestBody PushOrderCallbackUpstreamBO bo) {
        phoneOrderRecordPushService.pushOrderCallbackUpstream(bo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

}
