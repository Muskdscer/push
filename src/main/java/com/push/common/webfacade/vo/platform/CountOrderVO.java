package com.push.common.webfacade.vo.platform;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/22 10:45
 *
 * 

 */
@Data
@Accessors(chain = true)
public class CountOrderVO {
    //昨天移动统计
    private Integer yesMobileSu;
    private Integer yesMobileFa;
    private Integer yesMobileRe;

    //昨天联通统计
    private Integer yesUnicomSu;
    private Integer yesUnicomFa;
    private Integer yesUnicomRe;

    //昨天电信统计
    private Integer yesTelecomSu;
    private Integer yesTelecomFa;
    private Integer yesTelecomRe;

    //今天移动统计
    private Integer todayMobileSu;
    private Integer todayMobileFa;
    private Integer todayMobileRe;

    //今天联通统计
    private Integer todayUnicomSu;
    private Integer todayUnicomFa;
    private Integer todayUnicomRe;

    //今天电信统计
    private Integer todayTelecomSu;
    private Integer todayTelecomFa;
    private Integer todayTelecomRe;
}
