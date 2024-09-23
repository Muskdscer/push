package com.push.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.constants.DateConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.PhoneOrderRecordBO;
import com.push.common.webfacade.vo.platform.PhoneOrderRecordVO;
import com.push.common.webfacade.vo.platform.ShopExportPhoneOrderRecordVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.shop.impl.ShopPhoneOrderRecordServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 订单列表controller
 * Create DateTime: 2020/3/31 9:26
 *
 *

 */
@RestController
@RequestMapping("back/shopOrder")
public class ShopBackOrderController extends BaseController {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private ShopPhoneOrderRecordServiceImpl shopPhoneOrderRecordServiceImpl;

    private static final String fileName = "订单.xlsx";

    /**
     * 商户订单导出
     */
    @RequestMapping(value = "recordExport", method = {RequestMethod.GET, RequestMethod.POST})
    public void recordExport(@RequestParam(required = false) String platformOrderNo,
                             @RequestParam(required = false) String upstreamOrderNo,
                             @RequestParam(required = false) String shopOrderNo,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime,
                             HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateConstant.DATE_FORMAT_NORMAL);

        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderRecord.class);
        queryWrapper.eq(PhoneOrderRecord::getShopUserId, getUserId());
        if (StringUtils.isNotBlank(startTime)) {
            Date sDate = sdf.parse(startTime);
            queryWrapper.gt(PhoneOrderRecord::getCreateTime, sDate);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Date eDate = sdf.parse(endTime);
            queryWrapper.lt(PhoneOrderRecord::getCreateTime, eDate);
        }

        if (StringUtils.isNotBlank(upstreamOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamOrderNo, upstreamOrderNo);
        }
        if (StringUtils.isNotBlank(platformOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderNo, platformOrderNo);
        }
        if (StringUtils.isNotBlank(shopOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getShopOrderNo, shopOrderNo);
        }
        EasyExcelUtils.exportBigExcel("订单信息", ShopExportPhoneOrderRecordVO.class, shopPhoneOrderRecordServiceImpl, queryWrapper, fileName, request, response);
    }

    @RequestMapping(value = "listOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listOrder(@RequestBody PhoneOrderRecordBO bo) {
        bo.validate();
        IPage<PhoneOrderRecord> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderRecord.class);
        queryWrapper.eq(PhoneOrderRecord::getShopUserId, getUserId());
        if (StringUtils.isNotBlank(bo.getPlatformOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderNo, bo.getPlatformOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamOrderNo, bo.getUpstreamOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getShopOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getShopOrderNo, bo.getShopOrderNo());
        }
        if (bo.getStartTime() != null) {
            queryWrapper.ge(PhoneOrderRecord::getCreateTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            queryWrapper.lt(PhoneOrderRecord::getCreateTime, bo.getEndTime());
        }
        queryWrapper.orderByDesc(PhoneOrderRecord::getCreateTime);
        IPage<PhoneOrderRecord> recordIPage = phoneOrderRecordService.page(page, queryWrapper);
        List<PhoneOrderRecordVO> vos = recordIPage
                .getRecords()
                .stream()
                .map(p -> BeanUtils.copyPropertiesChaining(p, PhoneOrderRecordVO::new))
                .collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("total", recordIPage.getTotal());
        returnMap.put("list", vos);
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }

}
