package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.AgentPassCheckExtractionBO;
import com.push.common.webfacade.bo.platform.UpstreamAgentExtractionBO;
import com.push.common.webfacade.vo.platform.AgentPassCheckExtractionVO;
import com.push.common.webfacade.vo.platform.AgentSuAndFaPassCheckExtractionVO;
import com.push.entity.agent.UpstreamAgentExtraction;
import com.push.entity.agent.UpstreamAgentUserInfo;

/**
 * <p>
 * 上游代理商用户提现表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentExtractionService extends IService<UpstreamAgentExtraction> {

    boolean saveExtractionAndUpdateUser(UpstreamAgentUserInfo userInfo, UpstreamAgentExtractionBO bo);

    boolean updateByIdAndSaveBill(UpstreamAgentExtraction extraction);

    boolean updateByIdAndUpdateUser(UpstreamAgentExtraction extraction);

    IPage<AgentPassCheckExtractionVO> passCheckList(AgentPassCheckExtractionBO bo, Integer code, Long userId);

    IPage<AgentSuAndFaPassCheckExtractionVO> passSuAndFaCheckList(AgentPassCheckExtractionBO bo, Integer code, Long userId);
}
