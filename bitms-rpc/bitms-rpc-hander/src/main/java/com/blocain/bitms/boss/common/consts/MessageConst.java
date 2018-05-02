package com.blocain.bitms.boss.common.consts;

/**
 * 消息模版常量
 * <p>File：MessageConst.java</p>
 * <p>Title: MessageConst</p>
 * <p>Description:MessageConst</p>
 * <p>Copyright: Copyright (c) 2017/7/21</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class MessageConst
{
    private MessageConst()
    {// 防止实例化
    }
    
    // 消息类型
    public static final String MESSAGE_EMAIL                           = "email";
    
    public static final String MESSAGE_SMS                             = "sms";
    
    /**
     * 邮件验证码(通用交易验证时使用)
     */
    public static final String TEMPLATE_EMAIL_GENERAL_SENDVALICODE     = "email_send_general_valid_code";
    
    /**
     *  发送激活提现地址邮件
     */
    public static final String TEMPLATE_SEND_EMAIL_ACTIVE_COLLECT_ADDR = "email_send_active_collect_addr";
    
    /**
     *  发送激活提现邮件
     */
    public static final String TEMPLATE_SEND_EMAIL_ACTIVE_RAISE        = "email_send_active_raise";
    
    /**
     * 重置密码
     */
    public static final String TEMPLATE_EMAIL_RESETPASSWORD            = "email_send_reset_pass";
    
    /**
     * 用户注册
     */
    public static final String TEMPLATE_EMAIL_REGISTER                 = "email_send_register";
    
    /**
     * TRADEX用户注册
     */
    public static final String TRADEX_TEMPLATE_EMAIL_REGISTER          = "tradex_email_send_register";
    
    /**
     * TRADEX重置密码
     */
    public static final String TRADEX_TEMPLATE_EMAIL_RESETPASSWORD     = "tradex_email_send_reset_pass";
    
    /**
     * 移动端注册
     */
    public static final String TEMPLATE_MOBILE_REGISTER                = "mobile_email_register";
    
    /**
     *  发送预警邮件
     */
    public static final String TEMPLATE_SEND_EMAIL_ALARM_GENR          = "email_send_alarm_genr";
    
    /**
     *  发送预警短信
     */
    public static final String TEMPLATE_SEND_SMS_ALARM_GENR            = "sms_send_alarm_genr";
    
    /**
     * 发送手机验证码
     */
    public static final String TEMPLATE_SMS_SENDVALICODE               = "sms_send_valid_code";
    
    /**
     * 登陆提醒
     */
    public static final String REMIND_LOGIN_EMAIL                      = "remind_login_email";
    
    public static final String REMIND_LOGIN_PHONE                      = "remind_login_phone";
    
    /**
     * 修改登陆密码提醒
     */
    public static final String REMIND_CHANGE_LOGINPASS_EMAIL           = "remind_change_loginpwd_email";
    
    public static final String REMIND_CHANGE_LOGINPASS_PHONE           = "remind_change_loginpwd_phone";
    
    /**
     * 修改资金密码提醒
     */
    public static final String REMIND_CHANGE_FUNDPASS_EMAIL            = "remind_change_fundpwd_email";
    
    public static final String REMIND_CHANGE_FUNDPASS_PHONE            = "remind_change_fundpwd_phone";
    
    /**
     * 变更手机提醒
     */
    public static final String REMIND_CHANGE_PHONE_EMAIL               = "remind_change_phone_email";
    
    public static final String REMIND_CHANGE_PHONE_PHONE               = "remind_change_phone_phone";
    
    /**
     * 修改GA提醒
     */
    public static final String REMIND_CHANGE_GOOGLE_EMAIL              = "remind_change_google_email";
    
    public static final String REMIND_CHANGE_GOOGLE_PHONE              = "remind_change_google_phone";
    
    /**
     * 强制平仓提醒
     */
    public static final String REMIND_CLOSE_POSITION_EMAIL             = "remind_close_position_email";
    
    public static final String REMIND_CLOSE_POSITION_PHONE             = "remind_close_position_phone";
    
    /**
     * 提币二次确认短信提醒
     */
    public static final String REMIND_WITHDRAWCONFIRM_PHONE            = "remind_withdrawConfirm_phone";
    
    /**
     * 新增提币地址短信提醒
     */
    public static final String REMIND_ADDWITHDRAWADDR_PHONE            = "remind_addWithdrawAddr_phone";
    
    /**
     * 钱包与合约账户互转提醒
     */
    public static final String REMIND_CONVIERSION_PHONE                = "remind_conversion_phone";
    
    /**
     * 钱包与合约账户互转提醒
     */
    public static final String REMIND_CONVIERSION_EMAIL                = "remind_conversion_email";
    
    /**
     * 账户冻结短信提醒
     */
    public static final String REMIND_FROZEN_PHONE                     = "remind_frozen_phone";
    
    /**
     * 账户冻结邮箱提醒
     */
    public static final String REMIND_FROZEN_EMAIL                     = "remind_frozen_email";
    
    /**
     * 账户解冻短信提醒
     */
    public static final String REMIND_THAW_PHONE                       = "remind_thaw_phone";
    
    /**
     * 账户解冻邮箱提醒
     */
    public static final String REMIND_THAW_EMAIL                       = "remind_thaw_email";
}
