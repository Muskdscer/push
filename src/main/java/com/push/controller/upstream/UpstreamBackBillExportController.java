package com.push.controller.upstream;

import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.platform.ExportUpstreamBillVO;
import com.push.service.upstream.impl.UpstreamBillInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Create DateTime: 2020/5/20 18:21
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("back/upstreamBillExport")
public class UpstreamBackBillExportController extends BaseController {

    @Resource
    private UpstreamBillInfoServiceImpl upstreamBillInfoServiceImpl;

    private static final String UPSTREAM_FILE_NAME = "账单.xlsx";

    @RequestMapping(value = "upstreamExport", method = RequestMethod.GET)
    public void upstreamExportBill(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //List<ExportUpstreamBillVO> vos = upstreamBillInfoService.getExportField(bo);
        bo.setId(getUserId());
        EasyExcelUtils.exportBigExcel("账单信息", ExportUpstreamBillVO.class, upstreamBillInfoServiceImpl, bo, UPSTREAM_FILE_NAME, request, response);
    }


}
