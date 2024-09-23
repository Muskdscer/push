package com.push.controller.agent;

import com.push.common.controller.BaseController;
import com.push.common.utils.EasyExcelUtils;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.platform.ExportUpstreamAgentBillVO;
import com.push.service.agent.impl.UpstreamAgentBillInfoServiceImpl;
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
 * Create DateTime: 2020/5/21 9:32
 *
 *

 */
@RestController
@Slf4j
@RequestMapping("back/upstreamAgentBillExport")
public class UpstreamAgentBackBillExportController extends BaseController {

    @Resource
    private UpstreamAgentBillInfoServiceImpl upstreamAgentBillInfoServiceImpl;

    private static final String UPSTREAM_AGENT_FILE_NAME = "账单.xlsx";

    @RequestMapping(value = "upstreamAgentExport", method = RequestMethod.GET)
    public void upstreamAgentExport(ExportBillBO bo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        bo.setId(getUserId());
        EasyExcelUtils.exportBigExcel("账单信息", ExportUpstreamAgentBillVO.class, upstreamAgentBillInfoServiceImpl, bo, UPSTREAM_AGENT_FILE_NAME, request, response);
    }
}