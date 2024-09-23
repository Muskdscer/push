package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.enums.BillInfoStatusEnum;
import com.push.common.enums.BillInfoTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.UpstreamRechargeBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.CheckRechargeVO;
import com.push.dao.mapper.upstream.CpUpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamUserRechargeMapper;
import com.push.entity.upstream.CpUpstreamBillInfo;
import com.push.entity.upstream.UpstreamBillInfo;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.entity.upstream.UpstreamUserRecharge;
import com.push.service.upstream.UpstreamUserRechargeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 上游用户充值表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class UpstreamUserRechargeServiceImpl extends ServiceImpl<UpstreamUserRechargeMapper, UpstreamUserRecharge> implements UpstreamUserRechargeService {

    @Resource
    private UpstreamUserRechargeMapper upstreamUserRechargeMapper;

    @Resource
    private CpUpstreamBillInfoMapper cpUpstreamBillInfoMapper;

    @Resource
    private UpstreamBillInfoMapper upstreamBillInfoMapper;

    @Override
    public Page<CheckRechargeVO> getUpstreamRechargeToDoList(Integer code, UpstreamRechargeBO bo) {
        Page<CheckRechargeVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        Page<CheckRechargeVO> upstreamExtractionPage = upstreamUserRechargeMapper.getUpstreamRechargeToDoList(page, code,
                bo.getUpstreamUserId(), bo.getUpstreamName(), bo.getUserMobile(), bo.getStartTime(), bo.getEndTime());
        return upstreamExtractionPage;
    }

    @Override
    @TransactionalWithRollback
    public Boolean operatorSuccess(UpstreamUserRecharge upstreamUserRecharge, UpstreamUserInfo upstreamUserInfo) {
        //更新上游提现表提现状态
        int result1 = upstreamUserRechargeMapper.updateById(upstreamUserRecharge);
        if (result1 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单事务表记录
        CpUpstreamBillInfo cpUpstreamBillInfo = new CpUpstreamBillInfo();
        cpUpstreamBillInfo.setTraderUserId(upstreamUserRecharge.getUpstreamUserId());
        cpUpstreamBillInfo.setMoney(upstreamUserRecharge.getMoney());
        cpUpstreamBillInfo.setTradeNo(upstreamUserRecharge.getOrderNo());
        cpUpstreamBillInfo.setType(BillInfoTypeEnum.RECHARGE.getCode());
        cpUpstreamBillInfo.setBalance(upstreamUserInfo.getBalance());
        cpUpstreamBillInfo.setLastBalance(upstreamUserInfo.getBalance().add(upstreamUserRecharge.getMoney()));
        cpUpstreamBillInfo.setLastTime(new Date());
        int result2 = cpUpstreamBillInfoMapper.insert(cpUpstreamBillInfo);
        if (result2 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //增加账单表
        UpstreamBillInfo upstreamBillInfo = new UpstreamBillInfo();
        BeanUtils.copyProperties(cpUpstreamBillInfo, upstreamBillInfo);
        int result3 = upstreamBillInfoMapper.insert(upstreamBillInfo);
        if (result3 != 1) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }

        return true;
    }

    @Override
    //@TransactionalWithRollback
    public boolean failUpstreamUserRecharge(UpstreamUserRecharge upstreamUserRecharge) {
        int result1 = upstreamUserRechargeMapper.updateById(upstreamUserRecharge);
        //增加账单表
        /*UpstreamBillInfo upstreamBillInfo = new UpstreamBillInfo();
        upstreamBillInfo.setTraderUserId(upstreamUserRecharge.getUpstreamUserId());
        upstreamBillInfo.setTradeNo(upstreamUserRecharge.getOrderNo());
        upstreamBillInfo.setType(BillInfoTypeEnum.RECHARGE.getCode());
        upstreamBillInfo.setMoney(upstreamUserRecharge.getMoney());
        upstreamBillInfo.setStatus(BillInfoStatusEnum.SUCCESSS.getCode());
        int result2 = upstreamBillInfoMapper.insert(upstreamBillInfo);*/
        //if (result1 == 0 || result2 == 0) {
        if (result1 == 0) {
            return false;
        }
        return true;
    }
}
