package com.push.service.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.PlatformExtractionBO;
import com.push.common.webfacade.vo.platform.PlatformExtractionVO;
import com.push.entity.platform.PlatformBalance;
import com.push.entity.platform.PlatformExtraction;

/**
 * <p>
 * 上游用户提现表 服务类
 * </p>
 *
 *

 * @since 2020-03-30
 */
public interface PlatformExtractionService extends IService<PlatformExtraction> {

    Page<PlatformExtractionVO> getPlatformExtractionToDoList(Integer code, PlatformExtractionBO bo);

    Boolean operatorSuccess(PlatformExtraction platformExtraction, PlatformBalance platformBalance);

    boolean failPlatformUserExtraction(PlatformExtraction platformExtraction);

    boolean saveExtractionAndUpdateUserInfo(PlatformExtraction extraction, PlatformBalance platformBalance);
}
