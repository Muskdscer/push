package com.push.service.upstream;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.UpstreamExtractionBO;
import com.push.common.webfacade.vo.platform.UpstreamCheckExtractionVO;
import com.push.entity.upstream.UpstreamExtraction;
import com.push.entity.upstream.UpstreamUserInfo;

/**
 * <p>
 * 上游用户提现表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface UpstreamExtractionService extends IService<UpstreamExtraction> {

    //高级查询上游各种提现状态列表
    Page<UpstreamCheckExtractionVO> getUpstreamExtractionToDoList(Integer code, UpstreamExtractionBO bo);

    //更改提现提现订单状态，修改上游用户冻结金额，增加账单事务表记录
    boolean operatorSuccess(UpstreamExtraction upstreamExtraction, UpstreamUserInfo upstreamUserInfo);

    boolean failUpstreamUserExtraction(UpstreamExtraction upstreamExtraction);

    boolean saveExtractionAndUpdateUserInfo(UpstreamExtraction extraction, UpstreamUserInfo upstreamUserInfo);
}
