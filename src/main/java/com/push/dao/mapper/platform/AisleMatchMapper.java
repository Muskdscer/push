package com.push.dao.mapper.platform;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.AisleMatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 渠道通道配置表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-06-23
 */
public interface AisleMatchMapper extends BaseMapper<AisleMatch> {

    /**
     * 获取商户id集合
     * @param aisleClassifyId 通道id
     * @param code 未删除
     * @param value 启用
     * @return ids
     */
    List<Long> selectShopIds(@Param("aisleClassifyId") Long aisleClassifyId, @Param("code") int code, @Param("value") Integer value);
}
