package com.push.controller.upstream;

import com.push.common.controller.BaseLoginController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.LoginStatusEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.webfacade.bo.platform.LoginInfoBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.LoginUserVO;
import com.push.entity.upstream.UpstreamLogLogin;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamLogLoginService;
import com.push.service.upstream.UpstreamLoginService;
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
public class UpstreamLoginController extends BaseLoginController {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private UpstreamLoginService upstreamLoginService;

    @Resource
    private UpstreamLogLoginService upstreamLogLoginService;

    /**
     * 上游渠道登录
     *
     * @param loginInfoBO 登录参数实体
     * @param request     HttpServletRequest
     * @return 登录结果
     */
    @RequestMapping(value = "loginUpstream", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> loginChannel(@RequestBody LoginInfoBO loginInfoBO, HttpServletRequest request) {
        loginInfoBO.validate();
        UpstreamUserInfo upstreamUserInfo = upstreamLoginService.upstreamLogin(loginInfoBO.getUsername(), loginInfoBO.getPassword());
        if (null == upstreamUserInfo) {
            throw new CommonException(ErrorEnum.USERNAME_PASSWORD_INVALID);
        }

        if (upstreamUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            throw new CommonException(ErrorEnum.ACCOUNT_DISABLED);
        }

        UpstreamLogLogin upstreamLogLogin = new UpstreamLogLogin();
        upstreamLogLogin.setUpstreanUserId(upstreamUserInfo.getId());
        upstreamLogLogin.setLoginIp(getIpAddr());

        LoginUserVO loginUserVO = new LoginUserVO();
        try {
            if (session) {
                doSessionLogin(request, upstreamUserInfo, false);
            } else {
                String token = doTokenLogin(upstreamUserInfo.getUserName(), upstreamUserInfo.getId(), upstreamUserInfo.getRoleId(), null, false);
                loginUserVO.setToken(token);
            }

            loginUserVO.setUserName(upstreamUserInfo.getUserName());
            loginUserVO.setRoleId(upstreamUserInfo.getRoleId());

            upstreamLogLogin.setLoginStatus(LoginStatusEnum.SUCCESS.getValue());
            upstreamLogLoginService.save(upstreamLogLogin);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, loginUserVO);
        } catch (Exception e) {
            upstreamLogLogin.setLoginStatus(LoginStatusEnum.FAILED.getValue());
            upstreamLogLoginService.save(upstreamLogLogin);
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }
}
