package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListUpstreamBillBO;
import com.push.common.webfacade.vo.platform.ExportUpstreamBillVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.UpstreamBillInfoVO;
import com.push.entity.upstream.UpstreamBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上游账单表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamBillInfoMapper extends BaseMapper<UpstreamBillInfo> {

    IPage<UpstreamBillInfoVO> listPlatformBill(@Param("page") IPage<UpstreamBillInfoVO> page, @Param("bo") ListUpstreamBillBO bo);

    List<ExportUpstreamBillVO> getExportField(@Param("bo") ExportBillBO bo, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    void saveBatch(@Param("upstreamBillInfos") List<UpstreamBillInfo> upstreamBillInfos);

    List<ListBillAllTypeStatisticsVO> upstreamBillStatistics(@Param("bo") ListUpstreamBillBO bo);
}
