package com.blocain.bitms.tools.consts;

/**
 * <p>File：CacheConst.java </p>
 * <p>Title: 缓存前缀声明 </p>
 * <p>Description: CacheConst </p>
 * <p>Copyright: Copyright (c) 15/9/1</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CacheConst
{
    private CacheConst()
    {
    }

    /**
     * 一秒
     */
    public static final Integer ONE_SECONDS_CACHE_TIME     = 1;

    /**
     * 两秒
     */
    public static final Integer TWO_SECONDS_CACHE_TIME     = 2;

    /**
     * 三秒
     */
    public static final Integer THREE_SECONDS_CACHE_TIME   = 3;
    
    public static final Integer ONE_MINUTE_CACHE_TIME      = 60;
    
    /**
     * APP参数默认缓存时间(5分钟)
     */
    public static final Integer DEFAULT_CACHE_TIME         = 300;
    
    /**
     * 15分钟
     */
    public static final Integer FIFTEEN_MINUTE_CACHE_TIME  = 900;
    
    /**
     * 30分钟
     */
    public static final Integer THIRTY_MINUTE_CACHE_TIME   = 1800;
    
    /**
     * 60分钟
     */
    public static final Integer ONE_HOUR_CACHE_TIME        = 3600;
    
    /**
     * 24小时
     */
    public static final Integer TWENTYFOUR_HOUR_CACHE_TIME = 86400;
    
    /**
     * GOOGLE CODE
     */
    public static final String  GOOGLE_CODE_PERFIX         = "google_code";
    
    /**
     * 消息发送
     */
    public static final String  CACHE_SEND_SMS_PERFIX      = "message_phone";
    
    /**
    * 消息邮件
    */
    public static final String  CACHE_SEND_EMAIL_PERFIX    = "message_email";
    
    /**
     * 消息过期
     */
    public static final String  CACHE_EXPIRE_SMS_PERFIX    = "message_expire";
    
    /**
     * 操作次数记数
     */
    public static final String  OPERATION_COUNT_PREFIX     = "operator_count";
    
    /**
     * 帐户锁定
     */
    public static final String  ACCOUNT_LOCK_PREFIX        = "account_lock";
    
    /**
     * 会话对象
     */
    public static final String  REDIS_SHIRO_CACHE_PREFIX   = "session_cache";
    
    /**
     * 锁标识
     */
    public static final String  LOCK_PERFIX                = "lock_";
    
    /**
     * 安全策略
     */
    public static final String  POLICY_PERFIX              = "policy_";
    
    /**
     * 登陆
     */
    public static final String  LOGIN_PERFIX               = "login_";
    
    /**
     * 会话统计
     */
    public static final String  REDIS_SHIRO_SESSION_PREFIX = "session_statis";
    
    /**
     * 行情
     */
    public static final String  REDIS_QUOTATION_PREFIX     = "quotation";

    /**
     * 监控
     */
    public static final String  REDIS_MONITOR_PREFIX     = "monitor";

    /**
     * 平台变动扫描
     */
    public static final String  REDIS_PLATSCAN_PREFIX      = "platscan";
}
