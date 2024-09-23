package com.push.service.schedule.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.BillInfoTypeConstant;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.*;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.vo.out.UpstreamCallBackVO;
import com.push.common.webfacade.vo.platform.TpShopUpdateOrderVO;
import com.push.dao.mapper.agent.CpShopAgentBillInfoMapper;
import com.push.dao.mapper.agent.CpUpstreamAgentBillInfoMapper;
import com.push.dao.mapper.agent.ShopAgentBillInfoMapper;
import com.push.dao.mapper.agent.UpstreamAgentBillInfoMapper;
import com.push.dao.mapper.platform.*;
import com.push.dao.mapper.shop.CpShopBillInfoMapper;
import com.push.dao.mapper.shop.ShopBillInfoMapper;
import com.push.dao.mapper.upstream.CpUpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamBillInfoMapper;
import com.push.dao.mapper.upstream.UpstreamCallBackMapper;
import com.push.entity.agent.*;
import com.push.entity.platform.*;
import com.push.entity.shop.CpShopBillInfo;
import com.push.entity.shop.ShopBillInfo;
import com.push.entity.shop.ShopUserInfo;
import com.push.entity.upstream.CpUpstreamBillInfo;
import com.push.entity.upstream.UpstreamBalance;
import com.push.entity.upstream.UpstreamBillInfo;
import com.push.service.agent.ShopAgentMapInfoService;
import com.push.service.agent.ShopAgentUserInfoService;
import com.push.service.agent.UpstreamAgentMapInfoService;
import com.push.service.platform.PhoneOrderAvailableService;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.PlatformBalanceService;
import com.push.service.platform.UpstreamAisleConfigService;
import com.push.service.schedule.ShopCallbackOrderStatusService;
import com.push.service.shop.ShopUserInfoService;
import com.push.service.upstream.UpstreamAgentUserBalanceService;
import com.push.service.upstream.UpstreamBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/4/2 9:22
 *
 * 

 */
@Service
@Slf4j
public class ShopCallbackOrderStatusServiceImpl implements ShopCallbackOrderStatusService {

    @Resource
    private PhoneOrderAvailableService phoneOrderAvailableService;
    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;
    @Resource
    private PlatformBalanceService platformBalanceService;
    @Resource
    private ShopUserInfoService shopUserInfoService;
    @Resource
    private ShopAgentMapInfoService shopAgentMapInfoService;
    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;
    @Resource
    private UpstreamAgentMapInfoService upstreamAgentMapInfoService;
    @Resource
    private PhoneOrderAvailableMapper phoneOrderAvailableMapper;
    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;
    @Resource
    private CpTpPhoneOrderCallBackMapper cpTpPhoneOrderCallBackMapper;
    @Resource
    private CpUpstreamBillInfoMapper cpUpstreamBillInfoMapper;
    @Resource
    private UpstreamBillInfoMapper upstreamBillInfoMapper;
    @Resource
    private CpShopBillInfoMapper cpShopBillInfoMapper;
    @Resource
    private ShopBillInfoMapper shopBillInfoMapper;
    @Resource
    private CpUpstreamAgentBillInfoMapper cpUpstreamAgentBillInfoMapper;
    @Resource
    private UpstreamAgentBillInfoMapper upstreamAgentBillInfoMapper;
    @Resource
    private ShopAgentBillInfoMapper shopAgentBillInfoMapper;
    @Resource
    private CpShopAgentBillInfoMapper cpShopAgentBillInfoMapper;
    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;
    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;
    @Resource
    private PhoneOrderUnavailableMapper phoneOrderUnavailableMapper;
    @Resource
    private UpstreamAisleConfigService upstreamAisleConfigService;
    @Resource
    private UpstreamBalanceService upstreamBalanceService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Value("${handler.count}")
    private Long handlerCount;
    @Resource
    private UpstreamCallBackMapper upstreamCallBackMapper;

    List<Integer> orderStatus = new ArrayList<>();

    @PostConstruct
    public void addOrderStatus() {
        orderStatus.add(OrderStatusCodeEnum.PUSH_FAILED.getCode());
        orderStatus.add(OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode());
        orderStatus.add(OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode());
    }

    @Resource
    private UpstreamAgentUserBalanceService upstreamAgentUserBalanceService;

    //消费商户回调成功
    @Override
    @Transactional
    public void handleShopCallbackSuccessOrder() {

        //更新渠道回调状态
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS);

        if (size > handlerCount) {
            size = handlerCount;
        }
        if (size > 0) {
            List<UpstreamCallBackVO> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String upstreamCallback = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS);
                UpstreamCallBackVO upstreamCallBackVO = JSONObject.parseObject(upstreamCallback, UpstreamCallBackVO.class);
                if (upstreamCallBackVO != null) {
                    list.add(upstreamCallBackVO);
                }
            }

            if (CollectionUtils.isNotEmpty(list)) {
                List<String> orderNoList = list.stream().map(UpstreamCallBackVO::getBizOrderNo).collect(Collectors.toList());
                try {
                    phoneOrderRecordMapper.updateByOrderNo(orderNoList);
                    upstreamCallBackMapper.saveBatch(orderNoList, UpstreamCallbackEnum.SUCCESS.getCode());
                } catch (Exception e) {
                    log.info("死锁订单:{}", orderNoList.toString());
                    list.forEach(upstreamCallBackVO -> {
                        redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS, JSON.toJSONString(upstreamCallBackVO));
                    });
                    throw e;

                }
                log.info("【全部流程结束】:{},最后一个订单号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), orderNoList.get(orderNoList.size() - 1));
                log.info("【回调渠道】订单号:{}", orderNoList);
            }

        }

        //商户回调处理
        List<CpTpPhoneOrderCallBack> dataList1 = cpTpPhoneOrderCallBackMapper.selectList(Wrappers.lambdaQuery(CpTpPhoneOrderCallBack.class)
                .eq(CpTpPhoneOrderCallBack::getStatus, ShopCallbackOrderStatusEnum.SUCCESS.getCode()).last("limit 500"));
        if (CollectionUtils.isEmpty(dataList1)) {
            return;
        }

        //筛选出平台订单号并从订单表查询该订单
        List<String> cpTpPlatformOrderNo = dataList1.stream()
                .map(CpTpPhoneOrderCallBack::getPlatformOrderNo).collect(Collectors.toList());
        List<PhoneOrderRecord> orderBatchByOrderNo = phoneOrderRecordMapper.getOrderBatchByOrderNo(cpTpPlatformOrderNo);

        //筛选出是终态的订单
        List<PhoneOrderRecord> finalStatusOrders = orderBatchByOrderNo.stream()
                .filter(order -> orderStatus.contains(order.getPlatformOrderStatus())).collect(Collectors.toList());

        List<CpTpPhoneOrderCallBack> dataList;
        if (CollectionUtils.isNotEmpty(finalStatusOrders)) {
            List<String> finalStatusCpTpOrderNo = finalStatusOrders.stream()
                    .map(PhoneOrderRecord::getPlatformOrderNo).collect(Collectors.toList());
            cpTpPhoneOrderCallBackMapper.deleteBatchOrder(finalStatusCpTpOrderNo);
            dataList = dataList1.stream()
                    .filter(cpTp -> !finalStatusCpTpOrderNo.contains(cpTp.getPlatformOrderNo())).collect(Collectors.toList());
        } else {
            dataList = dataList1;
        }

        if (CollectionUtils.isNotEmpty(dataList)) {
            //删除可用表
            List<String> platformOrderNos = dataList.stream()
                    .map(CpTpPhoneOrderCallBack::getPlatformOrderNo).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(platformOrderNos)) {
                phoneOrderAvailableMapper.deleteBatchByOrderNo(platformOrderNos);
            }
            //更新总记录表
            List<TpShopUpdateOrderVO> orderVOs = new ArrayList<>();
            dataList.forEach(tp -> {
                TpShopUpdateOrderVO vo = new TpShopUpdateOrderVO();
                vo.setOrderSn(tp.getOrderSn()).setPlatformOrderNo(tp.getPlatformOrderNo())
                        .setShopOrderNo(tp.getShopOrderNo());
                orderVOs.add(vo);
            });
            if (CollectionUtils.isNotEmpty(orderVOs)) {
                phoneOrderRecordMapper.updateBatchByOrderNo(orderVOs);
            }

            //查出tp所涉及的订单
            List<PhoneOrderRecord> recordList = phoneOrderRecordMapper.getOrderBatchByOrderNo(platformOrderNos);
            //批量增加渠道和商户cp和账单
            List<CpUpstreamBillInfo> cpUpstreamBillInfos = new ArrayList<>(recordList.size());
            List<UpstreamBillInfo> upstreamBillInfos = new ArrayList<>(recordList.size());
            List<CpShopBillInfo> cpShopBillInfos = new ArrayList<>(recordList.size());
            List<ShopBillInfo> shopBillInfos = new ArrayList<>(recordList.size());

            /**
             * =============================开始记账=====================================
             */
            recordList.forEach(phoneOrderRecord -> {
                /*0818修改 createCpUpstreamBillInfo 为  newCreateCpUpstreamBillInfo
                CpUpstreamBillInfo cpUpstreamBillInfo = createCpUpstreamBillInfo(phoneOrderRecord);*/
                CpUpstreamBillInfo cpUpstreamBillInfo = newCreateCpUpstreamBillInfo(phoneOrderRecord);
                UpstreamBillInfo upstreamBillInfo = BeanUtils.copyPropertiesChaining(cpUpstreamBillInfo, UpstreamBillInfo::new);
                cpUpstreamBillInfos.add(cpUpstreamBillInfo);
                upstreamBillInfos.add(upstreamBillInfo);
                CpShopBillInfo cpShopBillInfo = createCpShopBillInfo(phoneOrderRecord);
                ShopBillInfo shopBillInfo = BeanUtils.copyPropertiesChaining(cpShopBillInfo, ShopBillInfo::new);
                cpShopBillInfos.add(cpShopBillInfo);
                shopBillInfos.add(shopBillInfo);
            });
            if (CollectionUtils.isNotEmpty(cpUpstreamBillInfos)) {
                cpUpstreamBillInfoMapper.saveBatch(cpUpstreamBillInfos);
            }
            if (CollectionUtils.isNotEmpty(upstreamBillInfos)) {
                upstreamBillInfoMapper.saveBatch(upstreamBillInfos);
            }
            if (CollectionUtils.isNotEmpty(cpShopBillInfos)) {
                cpShopBillInfoMapper.saveBatch(cpShopBillInfos);
            }
            if (CollectionUtils.isNotEmpty(shopBillInfos)) {
                shopBillInfoMapper.saveBatch(shopBillInfos);
            }
            //筛选出有渠道代理商的订单
            List<PhoneOrderRecord> orderHaveUpstreamAgent = recordList.stream().filter(phoneOrderRecord -> {
                int count = upstreamAgentMapInfoService.count(Wrappers.lambdaQuery(UpstreamAgentMapInfo.class)
                        .eq(UpstreamAgentMapInfo::getUpstreamId, phoneOrderRecord.getUpstreamUserId())
                        .eq(UpstreamAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
                return count > 0;
            }).collect(Collectors.toList());
            List<CpUpstreamAgentBillInfo> cpUpstreamAgentBillInfos = new ArrayList<>(recordList.size());
            List<UpstreamAgentBillInfo> upstreamAgentBillInfos = new ArrayList<>(recordList.size());
            List<UpstreamAgentMapInfo> upstreamAgentMapInfos = upstreamAgentMapInfoService.
                    list(Wrappers.lambdaQuery(UpstreamAgentMapInfo.class)
                            .eq(UpstreamAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
            orderHaveUpstreamAgent.forEach(phoneOrderRecord -> {
                UpstreamAgentMapInfo upstreamAgentMapInfo = upstreamAgentMapInfos.stream()
                        .filter(mapInfo -> mapInfo.getUpstreamId().equals(phoneOrderRecord.getUpstreamUserId()))
                        .filter(mapInfo -> mapInfo.getAisleId().equals(phoneOrderRecord.getAisleId()))
                        .findFirst().get();
                //增加渠道代理商账单事务表及其账单表
                CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo = createCpUpstreamAgentBillInfo(phoneOrderRecord, upstreamAgentMapInfo);
                UpstreamAgentBillInfo upstreamAgentBillInfo = BeanUtils.copyPropertiesChaining(cpUpstreamAgentBillInfo, UpstreamAgentBillInfo::new);
                cpUpstreamAgentBillInfos.add(cpUpstreamAgentBillInfo);
                upstreamAgentBillInfos.add(upstreamAgentBillInfo);
            });
            if (CollectionUtils.isNotEmpty(cpUpstreamAgentBillInfos)) {
                cpUpstreamAgentBillInfoMapper.saveBatch(cpUpstreamAgentBillInfos);
            }
            if (CollectionUtils.isNotEmpty(upstreamAgentBillInfos)) {
                upstreamAgentBillInfoMapper.saveBatch(upstreamAgentBillInfos);
            }

            //筛选出有商户代理商的订单
            List<PhoneOrderRecord> orderHaveShopAgent = recordList.stream().filter(phoneOrderRecord -> {
                int count = shopAgentMapInfoService.count(Wrappers.lambdaQuery(ShopAgentMapInfo.class)
                        .eq(ShopAgentMapInfo::getShopId, phoneOrderRecord.getShopUserId())
                        .eq(ShopAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
                return count > 0;
            }).collect(Collectors.toList());
            //增加商户代理商cp和账单
            List<ShopAgentBillInfo> shopAgentBillInfos = new ArrayList<>(recordList.size());
            List<CpShopAgentBillInfo> cpShopAgentBillInfos = new ArrayList<>(recordList.size());
            if (CollectionUtils.isNotEmpty(orderHaveShopAgent)) {
                List<ShopAgentMapInfo> shopAgentMapInfos = shopAgentMapInfoService.list(Wrappers.lambdaQuery(ShopAgentMapInfo.class).eq(ShopAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
                orderHaveShopAgent.forEach(phoneOrderRecord -> {
                    ShopAgentMapInfo shopAgentMapInfo = shopAgentMapInfos.stream()
                            .filter(mapInfo -> mapInfo.getShopId().equals(phoneOrderRecord.getShopUserId())).findFirst().get();

                    CpShopAgentBillInfo cpShopAgentBillInfo = createCpShopAgentBillInfo(phoneOrderRecord, shopAgentMapInfo);
                    ShopAgentBillInfo shopAgentBillInfo = BeanUtils.copyPropertiesChaining(cpShopAgentBillInfo, ShopAgentBillInfo::new);
                    shopAgentBillInfos.add(shopAgentBillInfo);
                    cpShopAgentBillInfos.add(cpShopAgentBillInfo);
                });
            }
            if (CollectionUtils.isNotEmpty(cpShopAgentBillInfos)) {
                cpShopAgentBillInfoMapper.saveBatch(cpShopAgentBillInfos);
            }
            if (CollectionUtils.isNotEmpty(shopAgentBillInfos)) {
                shopAgentBillInfoMapper.saveBatch(shopAgentBillInfos);
            }

            List<CpPlatformBillInfo> cpPlatformBillInfos = new ArrayList<>(recordList.size());
            List<PlatformBillInfo> platformBillInfos = new ArrayList<>(recordList.size());
            //平台余额
            PlatformBalance platformBalance = platformBalanceService.list().get(0);
            //所有有效的商户代理商映射关系
            List<ShopAgentMapInfo> shopAgentMapInfos = shopAgentMapInfoService.list(Wrappers.lambdaQuery(ShopAgentMapInfo.class)
                    .eq(ShopAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
            //upstreamAgentMapInfos渠道代理商映射信息
            //orderHaveShopAgent拥有渠道代理商的订单
            //orderHaveUpstreamAgent拥有商户代理商的订单
            //recordList总订单
            //有渠道代理商无商户代理商的订单集合
            List<PhoneOrderRecord> collect1 = orderHaveUpstreamAgent.stream().filter(order -> !orderHaveShopAgent.contains(order)).collect(Collectors.toList());
            collect1.forEach(order -> {
                UpstreamAgentMapInfo upstreamAgentMapInfo = upstreamAgentMapInfos.stream()
                        .filter(userInfo -> order.getUpstreamUserId().equals(userInfo.getUpstreamId()))
                        .filter(userInfo -> order.getAisleId().equals(userInfo.getAisleId()))
                        .findAny().get();
                CpPlatformBillInfo cpPlatformBillInfo = createCpPlatformBillUpAgentExistShopAgentNotExist(order, upstreamAgentMapInfo, platformBalance);
                PlatformBillInfo platformBillInfo = BeanUtils.copyPropertiesChaining(cpPlatformBillInfo, PlatformBillInfo::new);
                cpPlatformBillInfos.add(cpPlatformBillInfo);
                platformBillInfos.add(platformBillInfo);
            });
            //有渠道代理商有商户代理商
            List<PhoneOrderRecord> collect2 = orderHaveUpstreamAgent.stream().filter(orderHaveShopAgent::contains).collect(Collectors.toList());
            collect2.forEach(order -> {
                UpstreamAgentMapInfo upstreamAgentMapInfo = upstreamAgentMapInfos.stream()
                        .filter(userInfo -> order.getUpstreamUserId().equals(userInfo.getUpstreamId()))
                        .filter(userInfo -> order.getAisleId().equals(userInfo.getAisleId()))
                        .findAny().get();
                ShopAgentMapInfo shopAgentMapInfo = shopAgentMapInfos.stream()
                        .filter(mapInfo -> mapInfo.getShopId().equals(order.getShopUserId())).findAny().get();
                CpPlatformBillInfo cpPlatformBillInfo = createCpPlatformBillUpAgentExistShopAgentExist(order, upstreamAgentMapInfo, shopAgentMapInfo, platformBalance);
                PlatformBillInfo platformBillInfo = BeanUtils.copyPropertiesChaining(cpPlatformBillInfo, PlatformBillInfo::new);
                cpPlatformBillInfos.add(cpPlatformBillInfo);
                platformBillInfos.add(platformBillInfo);
            });
            //无渠道代理商有商户代理商
            List<PhoneOrderRecord> collect3 = orderHaveShopAgent.stream().filter(order -> !orderHaveUpstreamAgent.contains(order)).collect(Collectors.toList());
            collect3.forEach(order -> {
                ShopAgentMapInfo shopAgentMapInfo = shopAgentMapInfos.stream()
                        .filter(mapInfo -> mapInfo.getShopId().equals(order.getShopUserId())).findAny().get();
                CpPlatformBillInfo cpPlatformBillInfo = createCpPlatformBillUpAgentNotExistShopAgentExist(order, shopAgentMapInfo, platformBalance);
                PlatformBillInfo platformBillInfo = BeanUtils.copyPropertiesChaining(cpPlatformBillInfo, PlatformBillInfo::new);
                cpPlatformBillInfos.add(cpPlatformBillInfo);
                platformBillInfos.add(platformBillInfo);
            });
            //List<PhoneOrderRecord> collect4 = orderHaveShopAgent.stream().filter(orderHaveUpstreamAgent::contains).collect(Collectors.toList());
            recordList.removeAll(collect1);
            recordList.removeAll(collect2);
            recordList.removeAll(collect3);
            //此时集合就是无渠道代理商无商户代理商
            recordList.forEach(order -> {
                CpPlatformBillInfo cpPlatformBillInfo = createCpPlatformBillUpAgentNotExistShopAgentNotExist(order, platformBalance);
                PlatformBillInfo platformBillInfo = BeanUtils.copyPropertiesChaining(cpPlatformBillInfo, PlatformBillInfo::new);
                cpPlatformBillInfos.add(cpPlatformBillInfo);
                platformBillInfos.add(platformBillInfo);
            });
            if (CollectionUtils.isNotEmpty(cpPlatformBillInfos)) {
                cpPlatformBillInfoMapper.saveBatch(cpPlatformBillInfos);
            }
            if (CollectionUtils.isNotEmpty(platformBillInfos)) {
                platformBillInfoMapper.saveBatch(platformBillInfos);
            }


            List<Long> ids = dataList.stream().map(CpTpPhoneOrderCallBack::getId).collect(Collectors.toList());
            cpTpPhoneOrderCallBackMapper.deleteBatchByOrderNo(ids);
        }
        /**
         * =============================记账结束=====================================
         */
    }

    /**
     * =========================================平台记账开始================================================================
     */
    //渠道代理商不存在商户代理商存在增加平台记账的方法
    private CpPlatformBillInfo createCpPlatformBillUpAgentNotExistShopAgentExist(PhoneOrderRecord phoneOrderRecord
            , ShopAgentMapInfo shopAgentMapInfo, PlatformBalance platformBalance) {
        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpPlatformBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE);
        //渠道-商户代理商
        Double upstreamRate = upstreamAisleConfigService.getRateByUpstreamUserIdAndAisleIdAndOperator(phoneOrderRecord.getUpstreamUserId(), phoneOrderRecord.getAisleId(), phoneOrderRecord.getPhoneOperator());
        cpPlatformBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamRate))
                .subtract(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(shopAgentMapInfo.getRate()))));
        cpPlatformBillInfo.setBalance(platformBalance.getPlatformBalance());
        cpPlatformBillInfo.setLastBalance(platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney()));
        cpPlatformBillInfo.setLastTime(new Date());
        cpPlatformBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpPlatformBillInfo;
    }

    //渠道代理商不存在商户代理商不存在增加平台记账的方法
    private CpPlatformBillInfo createCpPlatformBillUpAgentNotExistShopAgentNotExist(PhoneOrderRecord phoneOrderRecord
            , PlatformBalance platformBalance) {
        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpPlatformBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE);
        //渠道-商户
        Double upstreamRate = upstreamAisleConfigService.getRateByUpstreamUserIdAndAisleIdAndOperator(phoneOrderRecord.getUpstreamUserId(), phoneOrderRecord.getAisleId(), phoneOrderRecord.getPhoneOperator());
        cpPlatformBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamRate))
                .subtract(phoneOrderRecord.getShopPrice()));
        cpPlatformBillInfo.setBalance(platformBalance.getPlatformBalance());
        cpPlatformBillInfo.setLastBalance(platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney()));
        cpPlatformBillInfo.setLastTime(new Date());
        cpPlatformBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpPlatformBillInfo;
    }

    //渠道代理商存在商户代理商存在增加平台记账的方法
    private CpPlatformBillInfo createCpPlatformBillUpAgentExistShopAgentExist(PhoneOrderRecord phoneOrderRecord
            , UpstreamAgentMapInfo upstreamAgentMapInfo, ShopAgentMapInfo shopAgentMapInfo, PlatformBalance platformBalance) {

        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpPlatformBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE);
        //渠道代理商-商户代理商
        cpPlatformBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamAgentMapInfo.getRate()))
                .subtract(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(shopAgentMapInfo.getRate()))));
        cpPlatformBillInfo.setBalance(platformBalance.getPlatformBalance());
        cpPlatformBillInfo.setLastBalance(platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney()));
        cpPlatformBillInfo.setLastTime(new Date());
        cpPlatformBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpPlatformBillInfo;
    }

    //渠道代理商存在商户代理商不存在增加平台记账的方法
    private CpPlatformBillInfo createCpPlatformBillUpAgentExistShopAgentNotExist(PhoneOrderRecord phoneOrderRecord
            , UpstreamAgentMapInfo mapInfo, PlatformBalance platformBalance) {
        CpPlatformBillInfo cpPlatformBillInfo = new CpPlatformBillInfo();
        cpPlatformBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpPlatformBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE);
        //渠道代理商-商户
        cpPlatformBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(mapInfo.getRate()))
                .subtract(phoneOrderRecord.getShopPrice()));
        cpPlatformBillInfo.setBalance(platformBalance.getPlatformBalance());
        cpPlatformBillInfo.setLastBalance(platformBalance.getPlatformBalance().add(cpPlatformBillInfo.getMoney()));
        cpPlatformBillInfo.setLastTime(new Date());
        cpPlatformBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpPlatformBillInfo;
    }

    /**
     * =========================================平台记账结束================================================================
     */


    private CpUpstreamAgentBillInfo createCpUpstreamAgentBillInfo(PhoneOrderRecord phoneOrderRecord, UpstreamAgentMapInfo upstreamAgentMapInfo) {
        // 获取代理的费率
        Double upstreamRate = upstreamAisleConfigService.getRateByUpstreamUserIdAndAisleIdAndOperator(phoneOrderRecord.getUpstreamUserId(), phoneOrderRecord.getAisleId(), phoneOrderRecord.getPhoneOperator());
        // 获取代理商的余额
        UpstreamAgentUserBalance upstreamAgentUserBalance = upstreamAgentUserBalanceService.getOne(Wrappers.lambdaQuery(UpstreamAgentUserBalance.class)
                .eq(UpstreamAgentUserBalance::getUpstreamAgentUserId, upstreamAgentMapInfo.getAgentId())
                .eq(UpstreamAgentUserBalance::getAisleId, upstreamAgentMapInfo.getAisleId()));

        CpUpstreamAgentBillInfo cpUpstreamAgentBillInfo = new CpUpstreamAgentBillInfo();
        cpUpstreamAgentBillInfo.setTraderUserId(upstreamAgentMapInfo.getAgentId())
                .setMapUserId(phoneOrderRecord.getUpstreamUserId())
                .setTradeNo(phoneOrderRecord.getPlatformOrderNo())
                .setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE)
                .setMoney((phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamRate)))
                        .subtract(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamAgentMapInfo.getRate()))));
        cpUpstreamAgentBillInfo.setBalance(upstreamAgentUserBalance.getBalance())
                .setLastBalance(upstreamAgentUserBalance.getBalance().add(cpUpstreamAgentBillInfo.getMoney()))
                .setLastTime(new Date()).setStatus(BillInfoStatusEnum.TODO.getCode());
        cpUpstreamAgentBillInfo.setAisleId(phoneOrderRecord.getAisleId());
        cpUpstreamAgentBillInfo.setOperator(phoneOrderRecord.getPhoneOperator());
        return cpUpstreamAgentBillInfo;
    }

    private CpShopAgentBillInfo createCpShopAgentBillInfo(PhoneOrderRecord phoneOrderRecord, ShopAgentMapInfo shopAgentMapInfo) {
        CpShopAgentBillInfo cpShopAgentBillInfo = new CpShopAgentBillInfo();
        cpShopAgentBillInfo.setTraderUserId(shopAgentMapInfo.getAgentId())
                .setMapUserId(phoneOrderRecord.getShopUserId())
                .setTradeNo(phoneOrderRecord.getPlatformOrderNo())
                .setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE)
                .setMoney((phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(shopAgentMapInfo.getRate()))).subtract(phoneOrderRecord.getShopPrice()));
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(shopAgentMapInfo.getAgentId());
        cpShopAgentBillInfo.setBalance(shopAgentUserInfo.getBalance())
                .setLastBalance(shopAgentUserInfo.getBalance().add((phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(shopAgentMapInfo.getRate()))).subtract(phoneOrderRecord.getShopPrice())))
                .setLastTime(new Date()).setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpShopAgentBillInfo;
    }

    private CpShopBillInfo createCpShopBillInfo(PhoneOrderRecord phoneOrderRecord) {
        CpShopBillInfo cpShopBillInfo = new CpShopBillInfo();
        cpShopBillInfo.setTraderUserId(phoneOrderRecord.getShopUserId());
        cpShopBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpShopBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_SAVE);
        cpShopBillInfo.setMoney(phoneOrderRecord.getShopPrice());
        ShopUserInfo shopUserInfo = shopUserInfoService.getById(phoneOrderRecord.getShopUserId());
        cpShopBillInfo.setBalance(shopUserInfo.getBalance());
        cpShopBillInfo.setLastBalance(shopUserInfo.getBalance().add(phoneOrderRecord.getShopPrice()));
        cpShopBillInfo.setLastTime(new Date());
        cpShopBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        return cpShopBillInfo;
    }

    private CpUpstreamBillInfo newCreateCpUpstreamBillInfo(PhoneOrderRecord phoneOrderRecord) {
        // 使用渠道余额来进行对应的记账
        double rate = upstreamAisleConfigService.getRateByUpstreamUserIdAndAisleIdAndOperator
                (phoneOrderRecord.getUpstreamUserId(), phoneOrderRecord.getAisleId(), phoneOrderRecord.getPhoneOperator());
        UpstreamBalance upstreamBalance = upstreamBalanceService.getOne(Wrappers.lambdaQuery(UpstreamBalance.class)
                .eq(UpstreamBalance::getUpstreamUserId, phoneOrderRecord.getUpstreamUserId())
                .eq(UpstreamBalance::getAisleId, phoneOrderRecord.getAisleId()));
        CpUpstreamBillInfo cpUpstreamBillInfo = new CpUpstreamBillInfo();
        cpUpstreamBillInfo.setTraderUserId(phoneOrderRecord.getUpstreamUserId());
        cpUpstreamBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
        cpUpstreamBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_REDUCE);
        cpUpstreamBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(rate)));
        cpUpstreamBillInfo.setBalance(upstreamBalance.getBalance());
        cpUpstreamBillInfo.setLastBalance(upstreamBalance.getBalance().subtract(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(rate))));
        cpUpstreamBillInfo.setLastTime(new Date());
        cpUpstreamBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
        cpUpstreamBillInfo.setAisleId(phoneOrderRecord.getAisleId());
        cpUpstreamBillInfo.setOperator(phoneOrderRecord.getPhoneOperator());
        return cpUpstreamBillInfo;
    }
//    private CpUpstreamBillInfo createCpUpstreamBillInfo(PhoneOrderRecord phoneOrderRecord) {
//        CpUpstreamBillInfo cpUpstreamBillInfo = new CpUpstreamBillInfo();
//        cpUpstreamBillInfo.setTraderUserId(phoneOrderRecord.getUpstreamUserId());
//        cpUpstreamBillInfo.setTradeNo(phoneOrderRecord.getPlatformOrderNo());
//        cpUpstreamBillInfo.setType(BillInfoTypeConstant.BILLINFO_TYPE_REDUCE);
//        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(phoneOrderRecord.getUpstreamUserId());
//        cpUpstreamBillInfo.setMoney(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamUserInfo.getRate())));
//        cpUpstreamBillInfo.setBalance(upstreamUserInfo.getBalance());
//        cpUpstreamBillInfo.setLastBalance(upstreamUserInfo.getBalance().subtract(phoneOrderRecord.getOrderPrice().multiply(BigDecimal.valueOf(upstreamUserInfo.getRate()))));
//        cpUpstreamBillInfo.setLastTime(new Date());
//        cpUpstreamBillInfo.setStatus(BillInfoStatusEnum.TODO.getCode());
//        return cpUpstreamBillInfo;
//    }

    /*private PhoneOrderRecord updatePhoneOrderRecordSuccess(PhoneOrderRecord phoneOrderRecord, TpPhoneOrderCallBack tpPhoneOrderCallBack) {
        //添加商户订单号
        phoneOrderRecord.setShopOrderNo(tpPhoneOrderCallBack.getShopOrderNo());
        //添加网厅流水号
        phoneOrderRecord.setOrderSn(tpPhoneOrderCallBack.getOrderSn());
        //更改商户回调状态
        phoneOrderRecord.setShopCallbackStatus(ShopCallbackOrderLogEnum.SUCCESSS.getCode());
        //更改订单状态
        phoneOrderRecord.setPlatformOrderStatus(OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode());
        //更改订单记账状态
        phoneOrderRecord.setShopBillStatus(ShopCallbackBillStatusEnum.BILLED.getCode());
        phoneOrderRecord.setPlatformBillStatus(PlatformBillStatusEnum.BILLED.getCode());
        phoneOrderRecord.setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.BILLED.getCode());
        //设置网厅流水号
        phoneOrderRecord.setOrderSn(tpPhoneOrderCallBack.getOrderSn());
        phoneOrderRecord.setUpBackNum(1);
        return phoneOrderRecord;
    }*/

    //消费商户回调失败
    @Override
    @Transactional
    public void handleShopCallbackFailOrder() {
        //查询需要处理的数据
        /*List<TpPhoneOrderCallBack> dataList = tpPhoneOrderCallBackService.list(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getDeleteFlag, DeleteFlagConstant.UNDELETE)
                .eq(TpPhoneOrderCallBack::getStatus, ShopCallBackStatusConstant.FAIL));*/
        List<CpTpPhoneOrderCallBack> dataList = cpTpPhoneOrderCallBackMapper.selectList(Wrappers.lambdaQuery(CpTpPhoneOrderCallBack.class)
                .eq(CpTpPhoneOrderCallBack::getStatus, ShopCallbackOrderStatusEnum.FAIL.getCode()).last("limit 500"));
        List<String> platformOrderNos = dataList.stream().map(CpTpPhoneOrderCallBack::getPlatformOrderNo).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        //查询出所有可用表
        List<PhoneOrderAvailable> phoneOrderAvailableList = phoneOrderAvailableMapper.getBatchByPlatformOrderNo(platformOrderNos);
        List<PhoneOrderUnavailable> phoneOrderUnavailableList = new ArrayList<>();
        phoneOrderAvailableList.forEach(phoneOrderAvailable -> {
            CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack = dataList.stream()
                    .filter(cp -> cp.getPlatformOrderNo().equals(phoneOrderAvailable.getPlatformOrderNo())).findFirst().get();
            PhoneOrderUnavailable phoneOrderUnavailableFail = createPhoneOrderUnavailableFail(phoneOrderAvailable, cpTpPhoneOrderCallBack);
            phoneOrderUnavailableList.add(phoneOrderUnavailableFail);
        });

        List<Long> ids = phoneOrderAvailableList.stream().map(PhoneOrderAvailable::getId).collect(Collectors.toList());
        //删除可用表
        if (CollectionUtils.isNotEmpty(ids)) {
            phoneOrderAvailableService.removeByIds(ids);
        }
        //增加不可用表
        /*if (CollectionUtils.isNotEmpty(phoneOrderUnavailableList)) {
            phoneOrderUnavailableMapper.saveBatch(phoneOrderUnavailableList);
        }*/
        //更新总记录表
        List<PhoneOrderRecord> phoneOrderRecordList = phoneOrderRecordMapper.getBatchByPlatformOrderNo(platformOrderNos);
        phoneOrderRecordList.forEach(phoneOrderRecord -> {
            CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack = dataList.stream()
                    .filter(cp -> cp.getPlatformOrderNo().equals(phoneOrderRecord.getPlatformOrderNo())).findFirst().get();
            phoneOrderRecord.setShopOrderNo(cpTpPhoneOrderCallBack.getShopOrderNo());
            //更改商户回调状态
            phoneOrderRecord.setShopCallbackStatus(ShopCallbackOrderLogEnum.FAIL.getCode());
            //更改订单状态
            phoneOrderRecord.setPlatformOrderStatus(OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode());
            //更改订单记账状态
            phoneOrderRecord.setShopBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
            phoneOrderRecord.setPlatformBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
            phoneOrderRecord.setUpstreamBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
        });
        phoneOrderRecordMapper.updateBatchOrderFailById(phoneOrderRecordList);

        List<Long> cpTpShopCallIds = dataList.stream().map(CpTpPhoneOrderCallBack::getId).collect(Collectors.toList());
        cpTpPhoneOrderCallBackMapper.deleteBatchByOrderNo(cpTpShopCallIds);
        //List<String> platformOrderNos = dataList.stream().map(CpTpPhoneOrderCallBack::getPlatformOrderNo).collect(Collectors.toList());

        /*if (CollectionUtils.isNotEmpty(dataList)) {
            for (CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack : dataList) {
                shopCallbackOrderStatusService.handleOrderCallBackFail(cpTpPhoneOrderCallBack);
            }
        }*/
    }


    private PhoneOrderUnavailable createPhoneOrderUnavailableFail(PhoneOrderAvailable phoneOrderAvailable, CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack) {
        PhoneOrderUnavailable phoneOrderUnavailable = BeanUtils.copyPropertiesChaining(phoneOrderAvailable, PhoneOrderUnavailable::new);
        phoneOrderUnavailable.setShopOrderNo(cpTpPhoneOrderCallBack.getShopOrderNo());
        //更改商户回调状态
        phoneOrderUnavailable.setShopCallbackStatus(ShopCallbackOrderLogEnum.FAIL.getCode());
        //更改订单状态
        phoneOrderUnavailable.setPlatformOrderStatus(OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode());
        //更改订单记账状态
        phoneOrderUnavailable.setShopBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
        phoneOrderUnavailable.setPlatformBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
        phoneOrderUnavailable.setUpstreamBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
        return phoneOrderUnavailable;
    }


}
