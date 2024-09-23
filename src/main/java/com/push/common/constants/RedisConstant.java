package com.push.common.constants;

/**
 * Description: Redis常量
 * Create DateTime: 2020-03-24 13:54
 *
 * 

 */
public interface RedisConstant {

    /**
     * session登录用户
     */
    String PUSH_ORDER_SESSION_LOGIN_USER_PRE_KEY = "push-order:loginUser:session:";

    /**
     * token登录用户
     */
    String PUSH_ORDER_TOKEN_LOGIN_USER_PRE_KEY = "push-order:loginUser:token:";

    /**
     * 系统参数key
     */
    String PUSH_ORDER_SYSTEM_PARAM = "push-order:systemParam";

    /**
     * 待推送订单
     */
    String PUSH_ORDER_WAIT_PUSH = "push-order:waitPush";

    /**
     * 待推送订单回调成功
     */
    String PUSH_ORDER_WAIT_PUSH_SUCCESS = "push-order:waitPush:success:";
    /**
     * 待推送记录
     */
    String PUSH_ORDER_WAIT_PUSH_RECORD = "push-order:waitPush:record";

    /**
     * 待推送订单回调失败
     */
    String PUSH_ORDER_WAIT_PUSH_FAILED = "push-order:waitPush:failed";

    /**
     * 待推送订单出现异常
     */
    String PUSH_ORDER_WAIT_PUSH_EXCEPTION = "push-order:waitPush:exception";

    /*
     * 数据商下单成功结果队列
     */
    String PUSH_ORDER_SJ_SUCCESS_WITH_SHOP_ORDER_NO = "push-order:waitPush:success:sj";

    /**
     * 超时待检查订单
     */
    String PUSH_ORDER_WAIT_CHECK = "push-order:waitCheck";

    /**
     * 超时待检查订单回调成功
     */
    String PUSH_ORDER_WAIT_CHECK_SUCCESS = "push-order:waitCheck:success";

    /**
     * 超时待检查订单回调失败
     */
    String PUSH_ORDER_WAIT_CHECK_FAILED = "push-order:waitCheck:failed";

    /**
     * 超时待检查订单回调处理中
     */
    String PUSH_ORDER_WAIT_CHECK_PROCESSING = "push-order:waitCheck:processing";


    /**
     * 待回调渠道订单
     */
    String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK = "push-order:upStreamWaitCallBack";

    /**
     * 平台导入订单共享数据
     */
    String PLATFORM_IMPORT_ORDER_SHARE_DATA = "push-order:importOrder:shareData";

    /**
     * 待回调渠道订单-成功
     */
    String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS = "push-order:upStreamCallBack:success";

    /**
     * 待回调渠道订单-失败
     */
    String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_FAILED = "push-order:upStreamCallBack:failed";

    /**
     * 商户回调订单状态
     */
    String PUSH_ORDER_SHOP_CALL_BACK_STATUS = "push-order:orderForShopCallBack:";

    /**
     * 回调数量警号
     */
    String PUSH_ORDER_CALL_BACK_NUMBER_ALARM = "push-order:callbackAlarm";

    /**
     * redis缓存数量统计key
     */
    String CURRENT_PUSH_NUM = "push-order:currentPushNum:";

    //===================================缓存=========================================
    /**
     * redis 缓存前缀
     */
    String PUSH_ORDER_CACHE_PRE = "push-order:cache:";

    /**
     * 所有的地区缓存KEY
     */
    String PUSH_ORDER_CACHE_ALL_AREA = PUSH_ORDER_CACHE_PRE + "allArea";

    /**
     * 所有的启用的地区缓存KEY
     */
    String PUSH_ORDER_CACHE_ALL_ENABLE_AREA = PUSH_ORDER_CACHE_PRE + "allEnableArea";

    /**
     * 当前登录用户角色下的菜单
     */
    String PUSH_ORDER_CACHE_CURRENT_ROLE_MENU_LIST = PUSH_ORDER_CACHE_PRE + "currentRoleMenuList";

    /**
     * 手机号黑名单
     */
    String PUSH_ORDER_CACHE_BLACK_PHONE_LIST = PUSH_ORDER_CACHE_PRE + "blackPhone";

}
