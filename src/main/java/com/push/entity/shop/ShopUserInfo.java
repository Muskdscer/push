package com.push.entity.shop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.push.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Description:
 * Create DateTime: 2020-03-26 11:11
 *
 * 

 */
@Getter
@Setter
@TableName("shop_user_info")
public class ShopUserInfo extends BaseEntity {

    private String userName;

    private String password;

    private Long matchClassifyId;

    /**
     * 商户手机号
     */
    private String phoneNumber;

    /**
     * 手机号区号，默认86
     */
    private String areaCode;

    /**
     * 商户企业名称
     */
    private String shopName;

    /**
     * 商户余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    /**
     * 费率
     */
    private Double rate;

    /**
     * 警报阀值
     */
    private Long alarmNumber;

    /**
     * 商户订单查询地址
     */
    private String querySite;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 订单过期时间
     */
    private Integer orderExpire;

    /**
     * 推送地址
     */
    private String pushSite;

    /**
     * 推送地址
     */
    private String shopCallbackSite;

    /**
     * 推送最大数量
     */
    private Integer pushNum;

    /**
     * 商户标识
     */
    private String appId;

    /**
     * 商户接口秘钥
     */
    private String appKey;

    /**
     * 白名单ip，可以配置多个，用,号隔开
     */
    private String whiteIp;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 推送开关
     */
    private Integer pushSwitch;

    @TableField(exist = false)
    private Long roleId;

}
