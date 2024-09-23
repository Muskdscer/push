package com.push.controller.platform;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.annotation.Operation;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.PushMatchBO;
import com.push.common.webfacade.bo.platform.PushMatchListBO;
import com.push.common.webfacade.vo.platform.ShopPushMatchVO;
import com.push.entity.shop.ShopPushMatch;
import com.push.service.shop.ShopPushMatchService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/4/10 11:08
 *
 * 

 */
@RestController
@RequestMapping("pushMatch")
public class ShopPushMatchController extends BaseController {

    @Resource
    private ShopPushMatchService shopPushMatchService;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_MATCH, operationDescribe = "平台配比修改")
    @RequestMapping(value = "/updatePush", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updatePush(@RequestBody List<PushMatchBO> bos) {
        bos.forEach(PushMatchBO::validate);
        List<ShopPushMatch> collect = bos.stream().map(b -> BeanUtils.copyPropertiesChaining(b, ShopPushMatch::new)).collect(Collectors.toList());
        boolean update = shopPushMatchService.updateBatchById(collect);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @RequestMapping(value = "/getPushMatchForList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getPushMatchForList(@RequestBody PushMatchListBO bo) {
        bo.validate();
        List<ShopPushMatchVO> list = shopPushMatchService.listWithShopName(bo.getMatchClassifyId(), bo.getOperatorType(), bo.getMoney());
        int count = shopPushMatchService.count(Wrappers
                .lambdaQuery(ShopPushMatch.class)
                .eq(ShopPushMatch::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                .eq(ShopPushMatch::getOperatorType, bo.getOperatorType())
                .eq(ShopPushMatch::getMoney, bo.getMoney())
                .eq(ShopPushMatch::getMatchClassifyId, bo.getMatchClassifyId()));
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        list.forEach(a -> {
            String key = RedisConstant.CURRENT_PUSH_NUM + date + ":" + a.getShopId() + ":" + bo.getOperatorType() + ":" + a.getMoney();
            Integer currentNum = redisTemplate.opsForValue().get(key);
            a.setCurrentNum(currentNum);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", count);
        return returnResultMap(ResultMapInfo.GETSUCCESS, map);
    }
}
