package com.push.controller.platform;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.constants.ParamConstants;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ParamAddBO;
import com.push.common.webfacade.bo.platform.ParamListBO;
import com.push.common.webfacade.bo.platform.ParamUpdateBO;
import com.push.entity.platform.SystemParam;
import com.push.service.platform.SystemParamService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-17 19:02
 *
 * 

 */
@RestController
@RequestMapping("param")
public class ParamController extends BaseController {

    @Resource
    private SystemParamService systemParamService;

    /**
     * 保存参数
     *
     * @param paramAddBO
     * @return
     */
    @RequestMapping(value = "saveParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveParam(@RequestBody ParamAddBO paramAddBO) {
        paramAddBO.validate();
        SystemParam systemParam = BeanUtils.copyPropertiesChaining(paramAddBO, SystemParam::new);
        systemParamService.modifyVersion();
        systemParamService.save(systemParam);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 更新参数
     *
     * @param paramUpdateBO
     * @return
     */
    @RequestMapping(value = "updateParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateParam(@RequestBody ParamUpdateBO paramUpdateBO) {
        paramUpdateBO.validate();
        SystemParam systemParam = BeanUtils.copyPropertiesChaining(paramUpdateBO, SystemParam::new);
        systemParamService.updateById(systemParam);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 分页查询参数列表
     *
     * @param paramListBO
     * @return
     */
    @RequestMapping(value = "listParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listParam(@RequestBody ParamListBO paramListBO) {
        paramListBO.validate();
        Page<SystemParam> page = new Page<>(paramListBO.getPageNo(), paramListBO.getPageSize());
        Page<SystemParam> resultPage = systemParamService.page(page, Wrappers.lambdaQuery(SystemParam.class)
                .ne(SystemParam::getName, ParamConstants.VERSION).orderByDesc(SystemParam::getId));
        return returnResultMap(ResultMapInfo.GETSUCCESS, resultPage);
    }

    /**
     * 根据参数ID获取参数详情
     *
     * @param bo
     * @return
     */
    @RequestMapping(value = "getParamById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getParamById(@RequestBody IdBO bo) {
        bo.validate();
        SystemParam systemParam = systemParamService.getById(bo.getId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, systemParam);
    }
}
