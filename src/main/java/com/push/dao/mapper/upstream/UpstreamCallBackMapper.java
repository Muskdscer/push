package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.upstream.UpstreamCallBack;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * 

 * @since 2020-06-13
 */
public interface UpstreamCallBackMapper extends BaseMapper<UpstreamCallBack> {

    void saveBatch(@Param("orderNoList") List<String> orderNoList, @Param("upstreamCallBackStatus") Integer upstreamCallBackStatus);
}
