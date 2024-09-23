package com.push.common.webfacade.vo.platform;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/4/23 16:49
 *
 * 

 */
@Data
@Accessors(chain = true)
public class CountProbabilityVO {

    List<Map<String, Object>> yesUpList;
    List<Map<String, Object>> yesShopList;
    List<Map<String, Object>> toUpList;
    List<Map<String, Object>> toShopList;
}
