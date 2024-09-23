package com.push.dao.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.dao.mapper.platform.TpPhoneOrderCallBackMapper;
import com.push.dao.platform.TpPhoneOrderCallBackDao;
import com.push.entity.platform.TpPhoneOrderCallBack;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-10 13:15
 *
 *

 */
@Repository
public class TpPhoneOrderCallBackDaoImpl implements TpPhoneOrderCallBackDao {

    @Resource
    private TpPhoneOrderCallBackMapper tpPhoneOrderCallBackMapper;

    @Override
    public List<TpPhoneOrderCallBack> selectTpPhoneOrderCallBackByPlatformNos(List<String> platformOrderNoList) {
        LambdaQueryWrapper<TpPhoneOrderCallBack> queryWrapper = Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .in(TpPhoneOrderCallBack::getPlatformOrderNo, platformOrderNoList);
        return tpPhoneOrderCallBackMapper.selectList(queryWrapper);
    }
}
