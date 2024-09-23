package com.push.common.constants;

/**
 * Description:
 * Create DateTime: 2020-04-16 13:15
 *
 *

 */
public interface ParamConstants {

    /**
     * param版本管理
     */
    String VERSION = "VERSION";
    /**
     * 邮箱收件方
     */
    String MAIL_TO = "MAIL_TO";
    /**
     * 提现费率
     */
    String EXTRACTION_SERVICE_CHARGE = "EXTRACTION_SERVICE_CHARGE";

    /**
     * 超时上限
     */
    String TIME_OUT_UPPER = "TIME_OUT_UPPER";

    /**
     * 处理异常订单开关
     */
    String EXCEPTION_ORDER_SWITCH = "EXCEPTION_ORDER_SWITCH";

    /**
     * 魁信配单stomp开关
     */
    String KX_DISTRIBUTE_ORDER_STOMP_SWITCH = "KX_DISTRIBUTE_ORDER_STOMP_SWITCH";

    /**
     * 魁信配单黑名单开关
     */
    String BLACK_LIST_FLAG = "BLACK_LIST_FLAG";

    /**
     * 黑名单过滤开关 1开 0关
     */
    String BLACK_LIST_FILTER_FLAG = "BLACK_LIST_FILTER_FLAG";

    /**
     * 黑名单自动添加开关 1开 0关
     */
    String BLACK_LIST_ADD_FLAG = "BLACK_LIST_ADD_FLAG";

}
