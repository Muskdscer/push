package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.dao.mapper.platform.TpPhoneOrderCallBackMapper;
import com.push.dao.platform.TpPhoneOrderCallBackDao;
import com.push.entity.platform.TpPhoneOrderCallBack;
import com.push.service.platform.TpPhoneOrderCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 回调订单临时表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-01
 */
@Slf4j
@Service
public class TpPhoneOrderCallBackServiceImpl extends ServiceImpl<TpPhoneOrderCallBackMapper, TpPhoneOrderCallBack> implements TpPhoneOrderCallBackService {

    @Resource
    private TpPhoneOrderCallBackDao tpPhoneOrderCallBackDao;

    @Resource
    private TpPhoneOrderCallBackMapper tpPhoneOrderCallBackMapper;

    @Override
    public List<TpPhoneOrderCallBack> getTpPhoneOrderCallBackByPlatformNos(List<String> platformOrderNoList) {
        return tpPhoneOrderCallBackDao.selectTpPhoneOrderCallBackByPlatformNos(platformOrderNoList);
    }

    @Override
    @TransactionalWithRollback
    public boolean saveOrUpdateByPlatformOrderNo(TpPhoneOrderCallBack tpPhoneOrderCallBack) {
        boolean result = false;
        try {
            tpPhoneOrderCallBackMapper.saveOrUpdateByPlatformOrderNo(tpPhoneOrderCallBack);
            result = true;
        } catch (Exception e) {
            log.error("saveOrUpdateByPlatformOrderNo失败,异常为:",e);
        }
        return result;
    }
}
