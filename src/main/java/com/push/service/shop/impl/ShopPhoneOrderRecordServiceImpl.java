package com.push.service.shop.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.vo.platform.ShopExportPhoneOrderRecordVO;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.shop.ShopPhoneOrderRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/5/21 18:24
 *
 * 

 */
@Service
public class ShopPhoneOrderRecordServiceImpl extends ServiceImpl<PhoneOrderRecordMapper, PhoneOrderRecord> implements ShopPhoneOrderRecordService, IExcelExportServer {

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = (LambdaQueryWrapper<PhoneOrderRecord>) queryParams;

        Page<PhoneOrderRecord> pa = new Page<>(page, 500);

        Page<PhoneOrderRecord> phoneOrderRecordPage = phoneOrderRecordMapper.selectPage(pa, queryWrapper);

        List<PhoneOrderRecord> records = phoneOrderRecordPage.getRecords();

        return records.stream()
                .map(m -> {
                    ShopExportPhoneOrderRecordVO exportPhoneOrderRecordVO = BeanUtils.copyPropertiesChaining(m, ShopExportPhoneOrderRecordVO::new);
                    exportPhoneOrderRecordVO.setPlatformOrderStatus(m.getPlatformOrderStatus().toString());
                    return exportPhoneOrderRecordVO;
                }).collect(Collectors.toList());
    }
}
