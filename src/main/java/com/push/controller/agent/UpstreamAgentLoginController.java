package com.push.controller.agent;

import com.push.common.controller.BaseLoginController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.LoginStatusEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.webfacade.bo.platform.LoginInfoBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.LoginUserVO;
import com.push.entity.agent.UpstreamAgentLogLogin;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentLogLoginService;
import com.push.service.agent.UpstreamAgentLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description: 登录Controller
 * Create DateTime: 2020-03-25 18:11
 *
 * 

 */
@Slf4j
@RestController
@RequestMapping("login")
public class UpstreamAgentLoginController extends BaseLoginController {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private UpstreamAgentLoginService upstreamAgentLoginService;

    @Resource
    private UpstreamAgentLogLoginService upstreamAgentLogLoginService;


    /**
     * 商户登录
     *
     * @param loginInfoBO 登录参数实体
     * @param request     HttpServletRequest
     * @return 登录结果
     */
    @RequestMapping(value = "loginAgentUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> loginAgentUpstream(@RequestBody LoginInfoBO loginInfoBO, HttpServletRequest request) {
        loginInfoBO.validate();
        UpstreamAgentUserInfo upstreamAgentUserInfo = upstreamAgentLoginService.upstreamAgentLogin(loginInfoBO.getUsername(), loginInfoBO.getPassword());
        if (null == upstreamAgentUserInfo) {
            throw new CommonException(ErrorEnum.USERNAME_PASSWORD_INVALID);
        }

        if (upstreamAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            throw new CommonException(ErrorEnum.ACCOUNT_DISABLED);
        }

        UpstreamAgentLogLogin login = new UpstreamAgentLogLogin();
        login.setUpstreamAgentUserId(upstreamAgentUserInfo.getId());
        login.setLoginIp(getIpAddr());

        LoginUserVO loginUserVO = new LoginUserVO();
        try {
            if (session) {
                doSessionLogin(request, upstreamAgentUserInfo, false);
            } else {
                String token = doTokenLogin(upstreamAgentUserInfo.getUserName(), upstreamAgentUserInfo.getId(), null, null, false);
                loginUserVO.setToken(token);
            }
            loginUserVO.setUserName(upstreamAgentUserInfo.getUserName());

            login.setLoginStatus(LoginStatusEnum.SUCCESS.getValue());
            upstreamAgentLogLoginService.save(login);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, loginUserVO);
        } catch (Exception e) {
            login.setLoginStatus(LoginStatusEnum.FAILED.getValue());
            upstreamAgentLogLoginService.save(login);
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }
}
