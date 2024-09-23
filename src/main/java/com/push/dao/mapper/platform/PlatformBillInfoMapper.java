package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListPlatFormBillBO;
import com.push.common.webfacade.vo.platform.ExportPlatformBillVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.PlatformBillInfoVO;
import com.push.entity.platform.PlatformBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 账单表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface PlatformBillInfoMapper extends BaseMapper<PlatformBillInfo> {

    IPage<PlatformBillInfoVO> listPlatformBill(@Param("iPage") IPage<PlatformBillInfoVO> iPage, @Param("bo") ListPlatFormBillBO bo);

    List<ExportPlatformBillVO> getExportField(@Param("bo") ExportBillBO bo, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    void saveBatch(@Param("platformBillInfos") List<PlatformBillInfo> platformBillInfos);

    List<ListBillAllTypeStatisticsVO> platformBillStatistics(@Param("bo") ListPlatFormBillBO bo);
}
