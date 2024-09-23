package com.push.service.platform.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.*;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.agent.AgentPhoneOrderRecordBO;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.common.webfacade.vo.platform.*;
import com.push.common.webfacade.vo.platform.statistics.StatisticUpstreamCallbackFailVO;
import com.push.dao.mapper.platform.CpTpPhoneOrderCallBackMapper;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.platform.TpPhoneOrderUpstreamQueryMapper;
import com.push.dao.mapper.upstream.UpstreamUserMapper;
import com.push.dao.platform.PhoneOrderRecordDao;
import com.push.entity.platform.CpTpPhoneOrderCallBack;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.TpPhoneOrderUpstreamQuery;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.upstream.UpstreamCallBackService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 可用订单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class PhoneOrderRecordServiceImpl extends ServiceImpl<PhoneOrderRecordMapper, PhoneOrderRecord> implements PhoneOrderRecordService, IExcelExportServer {

    @Resource
    private PhoneOrderRecordDao phoneOrderRecordDao;
    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;
    @Resource
    private TpPhoneOrderUpstreamQueryMapper tpPhoneOrderUpstreamQueryMapper;

    @Resource
    private CpTpPhoneOrderCallBackMapper cpTpPhoneOrderCallBackMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UpstreamUserMapper upstreamUserMapper;

    @Resource
    private UpstreamCallBackService upstreamCallBackService;


    @Override
    public List<PhoneOrderRecord> getPhoneOrderRecordByPlatformOrderNos(List<String> platformOrderNos) {
        return phoneOrderRecordDao.selectPhoneOrderRecordByPlatformOrderNos(platformOrderNos);
    }

    @Override
    public List<StatisticShopCallbackVO> getStatisticShop() {
        return phoneOrderRecordMapper.getStatisticShop();
    }

    @Override
    public List<StatisticUpstreamCallbackVO> getStatisticUpstream() {
        return phoneOrderRecordMapper.getStatisticUpstream();
    }

    @Override
    public List<StatisticUpstreamCallbackFailVO> getStatisticUpstreamCallBackFail() {
        return phoneOrderRecordMapper.getStatisticUpstreamCallBackFail();
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = (LambdaQueryWrapper<PhoneOrderRecord>) queryParams;

        Page<PhoneOrderRecord> pa = new Page<>(page, 500);

        Page<PhoneOrderRecord> phoneOrderRecordPage = phoneOrderRecordMapper.selectPage(pa, queryWrapper);

        List<PhoneOrderRecord> records = phoneOrderRecordPage.getRecords();

        return records.stream()
                .map(m -> {
                    ExportPhoneOrderRecordVO exportPhoneOrderRecordVO = BeanUtils.copyPropertiesChaining(m, ExportPhoneOrderRecordVO::new);
                    exportPhoneOrderRecordVO.setPlatformOrderStatus(m.getPlatformOrderStatus().toString());
                    return exportPhoneOrderRecordVO;
                }).collect(Collectors.toList());
    }

    @Override
    public IPage<PhoneOrderRecordVO> listByShopIds(AgentPhoneOrderRecordBO bo, List<Long> ids) {
        Page<PhoneOrderRecordVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return phoneOrderRecordMapper.listByShopIds(page, bo.getPhoneNum(), bo.getPhoneOperator(), bo.getPlatformOrderStatus(), bo.getUpstreamOrderNo(), bo.getShopOrderNo(), bo.getPlatformOrderNo(), bo.getStartTime(), bo.getEndTime(), ids);
    }

    @Override
    public IPage<PhoneOrderRecordVO> listByUpstreamIds(AgentPhoneOrderRecordBO bo, List<Long> ids) {
        Page<PhoneOrderRecordVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return phoneOrderRecordMapper.listByUpstreamIds(page, bo.getPhoneNum(), bo.getPhoneOperator(), bo.getPlatformOrderStatus(), bo.getUpstreamOrderNo(), bo.getShopOrderNo(), bo.getPlatformOrderNo(), bo.getStartTime(), bo.getEndTime(), ids);
    }

    @Override
    public UpstreamUserInfo getUpstreamUserInfoByPlatformOrderNo(String platformOrderNo) {
        return phoneOrderRecordMapper.selectUpstreamUserInfoByPlatformOrderNo(platformOrderNo);
    }

    @Override
    @Transactional
    public void operatorSuccess(PhoneOrderRecord phoneOrderRecord) {
        //如果订单是亏损状态，将平台订单状态改为消费完成，上游回调状态改为成功，上游记账状态改为已记账
        if (phoneOrderRecord.getPlatformOrderStatus() == OrderStatusCodeEnum.UPSTREAM_RESPONSE_FAIL.getCode()) {
            TpPhoneOrderUpstreamQuery tpPhoneOrderUpstreamQuery = new TpPhoneOrderUpstreamQuery();
            tpPhoneOrderUpstreamQuery.setBeforeQueryOrderStatus(phoneOrderRecord.getPlatformOrderStatus());
            PhoneOrderRecord record = new PhoneOrderRecord();
            record.setId(phoneOrderRecord.getId());
            record.setPlatformOrderStatus(OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode())
                    .setUpstreamCallbackStatus(UpstreamCallbackEnum.SUCCESS.getCode())
                    .setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.BILLED.getCode());
            phoneOrderRecordMapper.updateById(record);
            tpPhoneOrderUpstreamQuery.setAfterQueryOrderStatus(record.getPlatformOrderStatus())
                    .setPlatformOrderNo(phoneOrderRecord.getPlatformOrderNo())
                    .setUpstreamOrderNo(phoneOrderRecord.getUpstreamOrderNo());
            tpPhoneOrderUpstreamQueryMapper.insert(tpPhoneOrderUpstreamQuery);
        }
    }

    @Override
    public List<UpOrderCountGroupVO> getSumOrderCount(Long upstreamUserId, Double rate) {
        return phoneOrderRecordMapper.getSumOrderCount(upstreamUserId, rate);
    }

    @Override
    public List<DownOrderCountGroupVO> getSumOrderCountToShop(Date startTime, DateTime endTime) {
        return phoneOrderRecordMapper.getSumOrderCountToShop(startTime,endTime);
    }

    @Override
    public void changeOrderFailed(List<String> platformOrderNoList) {
        List<CpTpPhoneOrderCallBack> cpTpPhoneOrderCallBacks = platformOrderNoList.stream().map(orderNo -> {
            CpTpPhoneOrderCallBack back = new CpTpPhoneOrderCallBack();
            back.setPlatformOrderNo(orderNo);
            back.setStatus(ShopCallbackOrderStatusEnum.FAIL.getCode());
            return back;
        }).collect(Collectors.toList());

        cpTpPhoneOrderCallBackMapper.saveBatch(cpTpPhoneOrderCallBacks);


        List<PhoneOrderRecord> phoneOrderRecords = phoneOrderRecordMapper.selectList(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .in(PhoneOrderRecord::getPlatformOrderNo, platformOrderNoList));

        List<Long> upstreamUserIdList = phoneOrderRecords.stream().map(PhoneOrderRecord::getUpstreamUserId).collect(Collectors.toList());

        List<UpstreamUserInfo> upstreamUserInfos = upstreamUserMapper.selectList(Wrappers.lambdaQuery(UpstreamUserInfo.class).in(UpstreamUserInfo::getId, upstreamUserIdList));

        phoneOrderRecords.forEach(phoneOrderRecord -> {
            UpstreamUserInfo upstreamUserInfo = upstreamUserInfos.stream()
                    .filter(userInfo -> userInfo.getId().equals(phoneOrderRecord.getUpstreamUserId()))
                    .findFirst().get();

            upstreamCallBackService.callbackUpstream(upstreamUserInfo, phoneOrderRecord, "", "", ShopCallbackOrderStatusEnum.FAIL.getCode());
        });
    }

    @Override
    public void batchUpdateShopOrderNoByPlatformOrderNo(List<PhoneOrderRecord> phoneOrderRecordList) {
        phoneOrderRecordMapper.batchUpdateShopOrderNoByPlatformOrderNo(phoneOrderRecordList);
    }

}
