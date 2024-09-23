package com.push.controller.agent;

import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.platform.ExportShopAgentBillVO;
import com.push.service.agent.impl.ShopAgentBillInfoServiceImpl;
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
 * Create DateTime: 2020/5/21 9:36
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("back/shopAgentBillExport")
public class ShopAgentBackBillExportController extends BaseController {

    @Resource
    private ShopAgentBillInfoServiceImpl shopAgentBillInfoServiceImpl;

    private static final String SHOP_AGENT_FILE_NAME = "账单.xlsx";

    @RequestMapping(value = "shopAgentExport", method = RequestMethod.GET)
    public void shopAgentExport(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        bo.setId(getUserId());
        EasyExcelUtils.exportBigExcel("账单信息", ExportShopAgentBillVO.class, shopAgentBillInfoServiceImpl, bo, SHOP_AGENT_FILE_NAME, request, response);
    }

}
