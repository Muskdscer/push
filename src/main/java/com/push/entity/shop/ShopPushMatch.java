package com.push.entity.shop;

import com.baomidou.mybatisplus.annotation.TableName;
import com.push.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/10 10:57
 *
 *

 */
@Getter
@Setter
@TableName("shop_push_match")
@Accessors(chain = true)
public class ShopPushMatch extends BaseEntity {

    private Long shopId;

    private Long matchClassifyId;

    private Integer start;

    private String operatorType;

    private BigDecimal money;

    private Integer end;

    private Integer matchNum;
}
