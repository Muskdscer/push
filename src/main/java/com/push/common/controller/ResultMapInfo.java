package com.push.common.controller;

public enum ResultMapInfo {

	UNKNOW(2, "未知错误"),
	NOTFOUND(2, "资源不存在"),
	NOTPARAM(2, "参数不得为空"),
	NOTAUTHENTICATION(2, "认证码不得为空"),
	GETSUCCESS(1, "获取成功"),
	GETFAIL(2, "获取失败"),
	ILLEGALOPERATION(2, "非法操作"),
	ADDSUCCESS(1, "操作成功"),
	ADDFAIL(2, "操作失败"),
	NULLFILEERROR(2, "该文件没有可导入的信息"),
	FILETYPEERROR(2, "文件类型错误"),
	PASSWORDFORMATERROR(2, "密码不得小于8位"),
	OLDPASSWORDERROR(2, "原密码输入有误"),
	LONGINIPSUCCESS(1,"登入IP认证成功"),
	LONGINIPFAIL(2,"登入IP认证失败"),
	LONGINIPALLBAN(2,"用户登入IP全部被禁用"),
	LONGINIPISNULL(2,"白名单IP校验失败"),
	LOGININFOERRO(2,"用户名或密码错误"),
	NOTFINDORDER(2, "未找到该订单"),
	SUCCESSORDERNOTFAIL(2, "成功的订单无法变更为失败"),
	NOTFINDPAYMENT(2, "未找到支付账户"),
	QUERYFAIL(2, "远程查询失败"),
	NOTFINDPLATFORMACCOUNT(2, "订单信息异常"),
	SUBSTITUTEINFOERROR(2, "代付通道配置异常"),
	REMOTERESPONSEERROR(2, "远程响应异常"),
	PHONENUMBERDUPLTCATION(2, "手机号已被注册"),
	PHONENUMFORMATERROR(2, "手机号码格式错误"),
	VERIFICATIONERROR(2, "手机验证码不正确"),
	MINEXTRACTIONFAIL(2, "提现金额小于设置的最小提现金额"),
	MAXEXTRACTIONFAIL(2, "提现金额大于设置的最大提现金额"),
	EXTRACTIONPASSWORDERROR(2, "提现密码输入有误"),
	VERIFICATIONTIMEOUT(2, "手机验证码超时，请重新发送"),
	ACCOUNTDUPLICATION(2, "该账户已存在，不能重复添加"),
	TASKDUPLICATION(2, "任务名重复，修修改后再次提交"),
	PASSAGEWAYCODEDUPLICATION(2, "通道代码重复"),
	ACCOUNTNOTFIND(2, "未能找到该账户"),
	NOTFINDPASSAGEWAY(2, "未能找到该支付通道"),
	NOTFINDSUBPASSAGEWAY(2, "未能找到通道编码对应的子通道"),
	PASSAGEWAYNOTUSE(2, "支付通道不可用"),
	COUNTERNOTNULLBYJH(2, "建行的支付账户必须填写柜台号"),
	NUMBERDUPLICATION(2, "不能重复提交"),
	NUMBERDUPLICATIONBYCONFIG(2, "不能重复提交相同的通道配置信息"),
	ACCOUNTUSENOTDELETE(2, "账号正在使用中，不能删除"),
	HASBALANCENOTDELETE(2, "账号余额不为零，不能删除"),
	BALANCEDEFICIENCY(2, "余额不足"),
	EXTRACTIONMONEYLESSSERVICE(2, "提现金额小于服务费"),
	EXTRACTIONMONEYMAXERROR(2, "提现金额不得超过最大提现金额"),
	INFORMATIONINCONSISTENT(2, "信息不一致"),
	BALANCEINSUFFICIENT(2, "余额不足"),
	BALANCENOTFIND(2, "无法找到该账户"),
	STATEERROR(2, "状态类型错误"),
	PARAMERROR(2, "参数错误"),
	SHOPERROR(2, "商户信息异常"),
	AGENTERROR(2, "代理商信息异常"),
	EXTRACTIONMONEYNOTENOUGH(2,"提现金额不足"),
	EXTRACTIONERROR(2,"提现信息异常"),
	EXTRACTIONNOTFIND(2,"无法找到该提现订单"),
	SUBPYAMENTERROR(2,"代付信息异常"),
	SUBSAVEERRORSENDSUCCESS(2,"代付订单更新失败（请求已发出）"),
	ORDERSTATEERROR(2, "订单状态错误"),
	NOTFINDSUBSTITUTEPASSAGEWAY(2, "找不到可用的提现"),
	NOTFINDSUBSTITUTE(2, "找不到可用的提现账户"),
	NOTFINDMETHON(2, "找不到对应的通道处理"),
	SUBMITSUCCESS(1, "提交成功"),
	SUBMITFAIL(2, "提交失败"),
	EDITSUCCESS(1, "修改成功"),
	EDITFAIL(2, "修改失败"),
	DELETESUCCESS(1, "删除成功"),
	DELETEFAIL(2, "删除失败"),
	RELOGIN(9, "请重新登录"),
	LOGINSUCCESS(1, "登录成功"),
	LOGINFAIL(2, "登录失败"), 
	LOGOTSUCCESS(1, "退出登录成功"), 
	SENDSUCCESS(1, "发送成功"),
	SENDFAIL(2, "发送失败"),
	SHOPCONFIGERROR(2, "商户配置错误"),
	SUPPLEMENTORDERSUCCESS(1, "补单成功"),
	SUPPLEMENTORDERFAIL(2, "补单失败，请查看商户配置"),
	USERSTATEERRORBYEXTRACTION(2, "用户账户异常，无法体现，请联系管理员"), 
	SHOPUSERFROZEN(2, "用户账户被冻结，请解除冻结后再进行此项操作"),
	EXTRACTIONFAIL(2,"账户余额不足2元手续费"),
	SHOPUSEREXTRACTIONAVAILABLE(2, "用户账户被禁止提现，请解除状态后再进行此项操作"),
	EXTRACTIONSERVERERROR(2,"远程服务异常，请稍后再试"),
	LISTIFEMPTY(1,"列表为空"), 
	ORDERCANCELLATIONSUCCESS(1,"订单注销成功"),
	ORDERREFUNDSUCCESS(1,"订单退款成功"),
	ORDERCANCELLATIONFAIL(2,"订单注销失败"), 
	ORDERREFUNDFAIL(2,"订单退款失败"), 
	SUBSTITUTECOMPLETE(1,"代付订单已完成"), 
	SUBSTITUTEPASS(1,"代付订单审核通过"), 
	SUBCHANGEFAIL(1,"代付订单成功更改为失败"), 
	SUBSTITUTEREFUSE(1,"代付订单拒绝成功"), 
	SUBSTITUTEFAIL(2,"代付订单提交失败"), 
	SUBSTITUTEWAIT(2,"代付订单未完成"), 
	NONENUMBER(2,"没有可用的提现手机号"),
	ERRONUMBER(2,"不是可用的手机号"),
	ERROCODE(2,"验证码错误"),
	ADDERRORORDERCOMPLETE(2, "添加失败，订单已完成"),
	ADDERRORORDERNOTZHF(2, "添加失败，订单不是农行订单"),
	REPEATADDITION(2,"重复添加"),
	REPEATCOMMIT(2,"重复提交"),
	EXAMINEERROR(2, "审核状态异常"),
	ACCESSTIMEOUT(2, "访问超时"),
	FAILESIGN(2, "验签失败"),
	FAILAUTH(2, "谷歌验证失败"),
	NOTFINDAUTH(2, "谷歌验证未开启"),
	AUTHENTICATIONSUCCESS(1, "认证成功"),
	AUTHENTICATIONFAIL(2, "认证失败"),
	LOGINERRORCOUNTERRROR(2, "登录错误次数超出限制，请联系管理员或稍后再试"),
	NOTFINDBANKCARDINFO(2, "请先设置充值账户的银行卡信息"),
	EXTRACTIONSUBSTITUTENOTENOUGH(2, "远程异常"),
	SAVEUPSTREAMUSERSUCCESS(1,"新建用户成功"),
	UPSTREAM_NOT_EXIST(2,"上游用户不存在"),
	ACCOUNT_BAN(2,"用户被禁用"),
	APPID_HAD_EXIST(2,"appId已存在"),
	USERNAME_HAD_EXIST(2,"用户名已存在"),
	UPSTREAM_AGENT_NOT_EXIST(2,"上游代理商用户不存在"),
	UPSTREAM_AGENT_EXIST(2,"上游代理商用户已存在"),
	SHOP_NOT_EXIST(2,"商户不存在"),
	SHOP_HAD_EXIST(2,"商户已存在"),
	SHOP_AGENT_NOT_EXIST(2,"商户代理商不存在"),
	SHOP_AGENT_HAD_EXIST(2,"商户代理商已存在存在"),
	PLATFORMNO_NOT_EXIST(2,"平台订单号不存在"),
	ADDR_NOT_ACTIVE(2,"手机号归属地未开通"),
	BLACK_PHONE_NUM(2, "手机号已加入黑名单"),
	UPSTREAM_BALANCE_INSUFFICIENT(2,"授信金额不足"),
	ORDERTIMEOUT(2, "订单已超时"),
	SHOP_HAD_AGENT(2, "商户已被其他代理商代理");


    private int code;

    private String msg;

    private ResultMapInfo(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public ResultMapInfo setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return msg;
    }

    public ResultMapInfo setMessage(String message) {
        this.msg = message;
        return this;
    }
}
