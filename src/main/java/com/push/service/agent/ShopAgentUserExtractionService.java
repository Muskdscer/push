package com.push.service.agent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ShopAgentExtractionRecordBO;
import com.push.common.webfacade.vo.platform.statistics.ShopAgentCheckExtractionVO;
import com.push.entity.agent.ShopAgentUserExtraction;
import com.push.entity.agent.ShopAgentUserInfo;

/**
 * <p>
 * 商户代理商提现表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface ShopAgentUserExtractionService extends IService<ShopAgentUserExtraction> {

    boolean saveExtractionAndUpdateUserInfo(ShopAgentUserInfo shopAgentUserInfo, ShopAgentUserExtraction shopAgentUserExtraction);

    Page<ShopAgentCheckExtractionVO> getShopAgentExtractionToDoList(Integer code, ShopAgentExtractionRecordBO bo);

    Boolean operatorSuccess(ShopAgentUserExtraction shopAgentUserExtraction, ShopAgentUserInfo shopAgentUserInfo);

    boolean failShopAgentUserExtraction(ShopAgentUserExtraction shopAgentUserExtraction);
}
