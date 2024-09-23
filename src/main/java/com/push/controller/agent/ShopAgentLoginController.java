package com.push.controller.agent;

import com.push.common.controller.BaseLoginController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.LoginStatusEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.webfacade.bo.platform.LoginInfoBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.LoginUserVO;
import com.push.entity.agent.ShopAgentLogLogin;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.service.agent.ShopAgentLogLoginService;
import com.push.service.agent.ShopAgentLoginService;
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
public class ShopAgentLoginController extends BaseLoginController {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private ShopAgentLoginService shopAgentLoginService;

    @Resource
    private ShopAgentLogLoginService shopAgentLogLoginService;
    

    /**
     * 商户登录
     *
     * @param loginInfoBO 登录参数实体
     * @param request     HttpServletRequest
     * @return 登录结果
     */
    @RequestMapping(value = "loginAgentShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> loginAgentShop(@RequestBody LoginInfoBO loginInfoBO, HttpServletRequest request) {
        loginInfoBO.validate();
        ShopAgentUserInfo shopAgentUserInfo = shopAgentLoginService.shopAgentLogin(loginInfoBO.getUsername(), loginInfoBO.getPassword());
        if (null == shopAgentUserInfo) {
            throw new CommonException(ErrorEnum.USERNAME_PASSWORD_INVALID);
        }

        if (shopAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            throw new CommonException(ErrorEnum.ACCOUNT_DISABLED);
        }

        ShopAgentLogLogin login = new ShopAgentLogLogin();
        login.setUserId(shopAgentUserInfo.getId());
        login.setLoginIp(getIpAddr());

        LoginUserVO loginUserVO = new LoginUserVO();
        try {
            if (session) {
                doSessionLogin(request, shopAgentUserInfo, false);
            } else {
                String token = doTokenLogin(shopAgentUserInfo.getUserName(), shopAgentUserInfo.getId(), null, null, false);
                loginUserVO.setToken(token);
            }
            loginUserVO.setUserName(shopAgentUserInfo.getUserName());

            login.setLoginStatus(LoginStatusEnum.SUCCESS.getValue());
            shopAgentLogLoginService.save(login);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, loginUserVO);
        } catch (Exception e) {
            login.setLoginStatus(LoginStatusEnum.FAILED.getValue());
            shopAgentLogLoginService.save(login);
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }
}
