package com.push.entity.platform;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.push.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 菜单实体
 * Create DateTime: 2020-03-25 17:31
 *
 *

 */
@Getter
@Setter
@TableName("menu_info")
public class MenuInfo extends BaseEntity implements Serializable {

    /**
     * 菜单父ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 对应菜单下的接口
     */
    private String interfaceUri;

    @TableField(exist = false)
    private List<MenuInfo> children;

}
