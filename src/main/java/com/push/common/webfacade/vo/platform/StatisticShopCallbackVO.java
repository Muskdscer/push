package com.push.common.webfacade.vo.platform;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/16 10:58
 *
 * 

 */
@Data
public class StatisticShopCallbackVO {
    //商户名
    private String shopUserName;
    //待反馈条数
    private Long waitCallbackNo;
    //警报数量
    private Long alarmNumber;
    //是否警报(0:不警告 1:警告)
    @TableField(exist = false)
    private Integer alarmFlag;

}
