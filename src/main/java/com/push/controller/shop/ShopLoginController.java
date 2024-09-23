package com.push.controller.shop;

import com.push.common.controller.BaseLoginController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.LoginStatusEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.webfacade.bo.platform.LoginInfoBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.LoginUserVO;
import com.push.entity.shop.ShopLogLogin;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopLogLoginService;
import com.push.service.shop.ShopLoginService;
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
public class ShopLoginController extends BaseLoginController {

    @Value("${sso.session}")
    private Boolean session;

    @Resource
    private ShopLoginService shopLoginService;

    @Resource
    private ShopLogLoginService shopLogLoginService;

    /**
     * 商户登录
     *
     * @param loginInfoBO 登录参数实体
     * @param request     HttpServletRequest
     * @return 登录结果
     */
    @RequestMapping(value = "loginShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> loginShop(@RequestBody LoginInfoBO loginInfoBO, HttpServletRequest request) {
        loginInfoBO.validate();
        ShopUserInfo shopUserInfo = shopLoginService.shopLogin(loginInfoBO.getUsername(), loginInfoBO.getPassword());
        if (null == shopUserInfo) {
            throw new CommonException(ErrorEnum.USERNAME_PASSWORD_INVALID);
        }

        if (shopUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            throw new CommonException(ErrorEnum.ACCOUNT_DISABLED);
        }

        ShopLogLogin shopLogLogin = new ShopLogLogin();
        shopLogLogin.setUserId(shopUserInfo.getId());
        shopLogLogin.setLoginIp(getIpAddr());

        LoginUserVO loginUserVO = new LoginUserVO();
        try {
            if (session) {
                doSessionLogin(request, shopUserInfo, false);
            } else {
                String token = doTokenLogin(shopUserInfo.getUserName(), shopUserInfo.getId(), shopUserInfo.getRoleId(), null, false);
                loginUserVO.setToken(token);
            }
            loginUserVO.setUserName(shopUserInfo.getUserName());
            loginUserVO.setRoleId(shopUserInfo.getRoleId());

            shopLogLogin.setLoginStatus(LoginStatusEnum.SUCCESS.getValue());
            shopLogLoginService.save(shopLogLogin);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, loginUserVO);
        } catch (Exception e) {
            shopLogLogin.setLoginStatus(LoginStatusEnum.FAILED.getValue());
            shopLogLoginService.save(shopLogLogin);
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }
}
