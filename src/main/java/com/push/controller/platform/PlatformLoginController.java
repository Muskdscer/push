package com.push.controller.platform;

import com.push.common.controller.BaseLoginController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.LoginStatusEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.webfacade.bo.platform.LoginInfoBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.LoginUserVO;
import com.push.entity.platform.PlatformLogLogin;
import com.push.entity.platform.PlatformUserInfo;
import com.push.service.platform.PlatformLogLoginService;
import com.push.service.platform.PlatformLoginService;
import com.push.service.platform.RoleMenuService;
import com.push.sso.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Description: 管理员登录Controller
 * Create DateTime: 2020-03-25 18:11
 *
 *

 */
@Slf4j
@RestController
@RequestMapping("login")
public class PlatformLoginController extends BaseLoginController {

    @Value("${sso.session}")
    private Boolean session;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.start}")
    private String tokenStart;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private PlatformLoginService platformLoginService;

    @Resource
    private PlatformLogLoginService platformLogLoginService;

    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 平台管理员登录
     *
     * @param loginInfoBO 登录参数实体
     * @param request     HttpServletRequest
     * @return 登录结果
     */
    @RequestMapping(value = "loginAdmin", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> loginAdmin(@RequestBody LoginInfoBO loginInfoBO, HttpServletRequest request) {
        loginInfoBO.validate();

        PlatformUserInfo platformUserInfo = platformLoginService.platformLogin(loginInfoBO.getUsername(), loginInfoBO.getPassword());
        //参数校验
        if (null == platformUserInfo) {
            throw new CommonException(ErrorEnum.USERNAME_PASSWORD_INVALID);
        }

        if (platformUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            throw new CommonException(ErrorEnum.ACCOUNT_DISABLED);
        }

        Set<String> list = roleMenuService.queryMenuInterfaceUriByRoleId(platformUserInfo.getRoleId());
        platformUserInfo.setInterfaceList(list);

        PlatformLogLogin platformLogLogin = new PlatformLogLogin();
        platformLogLogin.setUserId(platformUserInfo.getId());
        platformLogLogin.setLoginIp(getIpAddr());

        LoginUserVO loginUserVO = new LoginUserVO();
        try {
            if (session) {
                doSessionLogin(request, platformUserInfo, true);
            } else {
                String token = doTokenLogin(platformUserInfo.getUserName(), platformUserInfo.getId(),
                        platformUserInfo.getRoleId(), platformUserInfo.getInterfaceList(), true);
                loginUserVO.setToken(token);
            }

            loginUserVO.setUserName(platformUserInfo.getUserName());
            loginUserVO.setRoleId(platformUserInfo.getRoleId());

            platformLogLogin.setLoginStatus(LoginStatusEnum.SUCCESS.getValue());
            platformLogLoginService.save(platformLogLogin);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, loginUserVO);
        } catch (Exception e) {
            platformLogLogin.setLoginStatus(LoginStatusEnum.FAILED.getValue());
            platformLogLoginService.save(platformLogLogin);
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }

    /**
     * 退出登录
     *
     * @param request HttpServletRequest
     * @return 登出结果
     */
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> logout(HttpServletRequest request) {
        if (session) {
            doSessionLogout(request);
        } else {
            String requestHeader = request.getHeader(tokenHeader);
            String headerStart = tokenStart + " ";
            if (requestHeader != null && requestHeader.startsWith(headerStart)) {
                String authToken = requestHeader.substring(headerStart.length());
                String username = jwtTokenUtils.getUsernameFromToken(authToken);
                doTokenLogout(username);
            }
        }
        return returnResultMap(ResultMapInfo.LOGOTSUCCESS);
    }

}
