package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * 

 * @since 2020-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TpPhoneOrderCallBackLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 推送数量
     */
    private Integer pushNum;

    /**
     * 商户的id
     */
    private Long shopUserId;

    /**
     * appid,验签条件,系统自动生成
     */
    private String appId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 0成功,1失败
     */
    private Integer status;


}
