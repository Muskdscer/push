package com.push.controller.shop;

import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.platform.ExportShopBillVO;
import com.push.service.shop.impl.ShopBillInfoServiceImpl;
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
 * Create DateTime: 2020/5/21 9:23
 *
 *

 */
@RestController
@Slf4j
@RequestMapping("back/shopBillExport")
public class ShopBackBillExportController extends BaseController {

    @Resource
    private ShopBillInfoServiceImpl shopBillInfoServiceImpl;

    private static final String SHOP_FILE_NAME = "账单.xlsx";

    @RequestMapping(value = "shopExport", method = RequestMethod.GET)
    public void shopExportBill(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //List<ExportShopBillVO> vos = shopBillInfoService.getExportField(bo);
        bo.setId(getUserId());
        EasyExcelUtils.exportBigExcel("账单信息", ExportShopBillVO.class, shopBillInfoServiceImpl, bo, SHOP_FILE_NAME, request, response);
    }

}
