package com.push.controller.platform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.push.common.constants.DateConstant;
import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.vo.platform.ExportPhoneOrderRecordVO;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.platform.impl.PhoneOrderRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/23 9:17
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("phoneOrder")
public class PhoneOrderRecordExportController extends BaseController {

    private static final String fileName = "订单.xlsx";

    @Resource
    private PhoneOrderRecordServiceImpl phoneOrderRecordServiceImpl;


    @RequestMapping(value = "recordExport", method = {RequestMethod.GET, RequestMethod.POST})
    public void recordExport(@RequestParam(required = false) String platformOrderNo,
                             @RequestParam(required = false) String phoneNum,
                             @RequestParam(required = false) String upstreamOrderNo,
                             @RequestParam(required = false) String shopOrderNo,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime,
                             @RequestParam(required = false) String phoneOperator,
                             @RequestParam(required = false) Integer platformOrderStatus,
                             @RequestParam(required = false) Integer upstreamCallbackStatus,
                             @RequestParam(required = false) Integer shopCallbackStatus,
                             HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateConstant.DATE_FORMAT_NORMAL);

        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderRecord.class);

        if (StringUtils.isNotBlank(startTime)) {
            Date sDate = sdf.parse(startTime);
            queryWrapper.ge(PhoneOrderRecord::getCreateTime, sDate);
        }

        if (StringUtils.isNotBlank(endTime)) {
            Date eDate = sdf.parse(endTime);
            queryWrapper.lt(PhoneOrderRecord::getCreateTime, eDate);
        }

        if (StringUtils.isNotBlank(upstreamOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamOrderNo, upstreamOrderNo);
        }

        if (StringUtils.isNotBlank(phoneNum)) {
            queryWrapper.eq(PhoneOrderRecord::getPhoneNum, phoneNum);
        }

        if (StringUtils.isNotBlank(platformOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderNo, platformOrderNo);
        }

        if (StringUtils.isNotBlank(shopOrderNo)) {
            queryWrapper.eq(PhoneOrderRecord::getShopOrderNo, shopOrderNo);
        }
        if (StringUtils.isNotBlank(phoneOperator)) {
            queryWrapper.eq(PhoneOrderRecord::getPhoneOperator, phoneOperator);
        }
        if (platformOrderStatus != null) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderStatus, platformOrderStatus);
        }
        if (upstreamCallbackStatus != null) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamCallbackStatus, upstreamCallbackStatus);
        }
        if (shopCallbackStatus != null) {
            queryWrapper.eq(PhoneOrderRecord::getShopCallbackStatus, shopCallbackStatus);
        }
        EasyExcelUtils.exportBigExcel("订单信息", ExportPhoneOrderRecordVO.class, phoneOrderRecordServiceImpl,
                queryWrapper, fileName, request, response);
    }
}
