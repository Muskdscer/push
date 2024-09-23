package com.push.entity.platform;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 *

 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TpPhoneOrderUpstreamQueryLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * appid,验签条件,系统自动生成
     */
    private String appId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0成功,1失败
     */
    private Integer status;



}
