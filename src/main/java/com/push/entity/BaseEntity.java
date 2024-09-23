package com.push.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 基类
 * Create DateTime: 2020-03-25 18:20
 *
 *

 */
@Data
public class BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer deleteFlag;

    @CreatedDate
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date createTime;

    @LastModifiedDate
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date updateTime;

}
