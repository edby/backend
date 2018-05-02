package com.blocain.bitms.boss.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发送短信后的返回记录 Introduce
 * <p>File：SMSResult.java</p>
 * <p>Title: SMSResult</p>
 * <p>Description: SMSResult</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SMSResult implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5150361527928809082L;
    
    /**
     *101无此用户
     *102密码错
     *103提交过快（提交速度超过流速限制）
     *104系统忙（因平台侧原因，暂时无法处理提交的短信）
     *105敏感短信（短信内容包含敏感词）
     *106消息长度错（>536或<=0）
     *107包含错误的手机号码
     *108手机号码个数错（群发>50000或<=0）
     *109无发送额度（该用户可用短信数已使用完）
     *110不在发送时间内
     *113扩展码格式错（非数字或者长度不对）
     *114可用参数组个数错误（小于最小设定值或者大于1000）;变量参数组大于20个
     *116签名不合法或未带签名（用户必须带签名的前提下）
     *117IP地址认证错,请求调用的IP地址不是系统登记的IP地址
     *118用户没有相应的发送权限（账号被禁止发送）
     *119用户已过期
     *120违反防盗用策略(日发送限制)
     *123发送类型错误
     *124白模板匹配错误
     *125匹配驳回模板，提交失败
     *127定时发送时间格式错误
     *128内容编码失败
     *129JSON格式错误
     *130请求参数错误（缺少必填参数）
     */
    private String     code;
    
    /**
     * 状态码说明（成功返回空字符串）
     */
    private String     error;
    
    /**
     * 消息id
     */
    private String     msgid;
    
    /**
     * 剩余可用余额 保留三位小数点
     */
    private BigDecimal balance;
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getError()
    {
        return error;
    }
    
    public void setError(String error)
    {
        this.error = error;
    }
    
    public String getMsgid()
    {
        return msgid;
    }
    
    public void setMsgid(String msgid)
    {
        this.msgid = msgid;
    }
    
    public BigDecimal getBalance()
    {
        return balance;
    }
    
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }
}
