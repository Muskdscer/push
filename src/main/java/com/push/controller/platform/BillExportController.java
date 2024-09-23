package com.push.controller.platform;

import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.platform.*;
import com.push.service.agent.impl.ShopAgentBillInfoServiceImpl;
import com.push.service.agent.impl.UpstreamAgentBillInfoServiceImpl;
import com.push.service.platform.impl.PlatformBillInfoServiceImpl;
import com.push.service.shop.impl.ShopBillInfoServiceImpl;
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
 * Create DateTime: 2020/4/23 16:43
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("billExport")
public class BillExportController extends BaseController {

    @Resource
    private UpstreamBillInfoServiceImpl upstreamBillInfoServiceImpl;

    @Resource
    private ShopBillInfoServiceImpl shopBillInfoServiceImpl;

    @Resource
    private PlatformBillInfoServiceImpl platformBillInfoServiceImpl;

    @Resource
    private UpstreamAgentBillInfoServiceImpl upstreamAgentBillInfoServiceImpl;

    @Resource
    private ShopAgentBillInfoServiceImpl shopAgentBillInfoServiceImpl;

    private static final String UPSTREAM_FILE_NAME = "渠道账单.xlsx";

    private static final String PLATFORM_FILE_NAME = "平台账单.xlsx";

    private static final String SHOP_FILE_NAME = "商户账单.xlsx";

    private static final String SHOP_AGENT_FILE_NAME = "商户代理商账单.xlsx";

    private static final String UPSTREAM_AGENT_FILE_NAME = "渠道代理商账单.xlsx";


    @RequestMapping(value = "upstreamExport", method = RequestMethod.GET)
    public void upstreamExportBill(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //List<ExportUpstreamBillVO> vos = upstreamBillInfoService.getExportField(bo);
        EasyExcelUtils.exportBigExcel("渠道账单信息", ExportUpstreamBillVO.class, upstreamBillInfoServiceImpl, bo, UPSTREAM_FILE_NAME, request, response);
    }

    @RequestMapping(value = "platformExport", method = RequestMethod.GET)
    public void platformExportBill(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //List<ExportPlatformBillVO> vos = platformBillInfoService.getExportField(bo);
        EasyExcelUtils.exportBigExcel("平台账单信息", ExportPlatformBillVO.class, platformBillInfoServiceImpl, bo, PLATFORM_FILE_NAME, request, response);
    }

    @RequestMapping(value = "shopExport", method = RequestMethod.GET)
    public void shopExportBill(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //List<ExportShopBillVO> vos = shopBillInfoService.getExportField(bo);
        EasyExcelUtils.exportBigExcel("商户账单信息", ExportShopBillVO.class, shopBillInfoServiceImpl, bo, SHOP_FILE_NAME, request, response);
    }

    //商户代理商账单导出
    @RequestMapping(value = "shopAgentExport", method = RequestMethod.GET)
    public void shopAgentExport(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EasyExcelUtils.exportBigExcel("商户代理商账单信息", ExportShopAgentBillVO.class, shopAgentBillInfoServiceImpl, bo, SHOP_AGENT_FILE_NAME, request, response);
    }

    //渠道代理商账单导出
    @RequestMapping(value = "upstreamAgentExport", method = RequestMethod.GET)
    public void upstreamAgentExport(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EasyExcelUtils.exportBigExcel("渠道代理商账单信息", ExportUpstreamAgentBillVO.class, upstreamAgentBillInfoServiceImpl, bo, UPSTREAM_AGENT_FILE_NAME, request, response);
    }

}
