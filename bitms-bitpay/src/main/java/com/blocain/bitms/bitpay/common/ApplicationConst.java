package com.blocain.bitms.bitpay.common;

/**
 * <p>File：ApplicationConst.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午1:00:03</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class ApplicationConst 
{
    // 私有构造器，防止类的实例化
    private ApplicationConst(){} 

    // 系统默认字符编码
    public static final String LANGUAGE_UTF8 = "UTF-8";
    
    //gbk编码
    public static final String LANGUAGE_GBK = "GBK";

    // 系统默认每页记录数
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    // 系统默认每页最大记录数
    public static final Integer MAX_PAGE_SIZE = DEFAULT_PAGE_SIZE * 10;

    // 系统默认当前页
    public static final Integer DEFAULT_CURRENT_PAGE = 1;
    
    // 系统年月格式，如：2010-06
    public static final String   DATE_FORMAT_YM         = "yyyy-MM";

    // 系统年月日格式，如：2010-08-19
    public static final String   DATE_FORMAT_YMD        = "yyyy-MM-dd";

    // 系统年月日格式，如：2010-08-19 05:23:20
    public static final String   DATE_FORMAT_YMDHMS     = "yyyy-MM-dd HH:mm:ss";
    
    //用户连续登录次数
    public static final Short CONTINUOUS_LOGIN_NUMBER = 7;
    
    //用户帐号锁定时间（小时）
    public static final Short ACCOUNT_LOCK_TIME = 24;
    
    //系统生成随机密码的位数
    public static final Short RAND_PASSWORD_NUMBER = 6; 
    
    
    //成功CODE编码
    public static final Integer ERROR_CODE_SUCCESS = 100;
    //错误CODE编码
    public static final Integer ERROR_CODE_FAILURE = 101;

    
    //一年的天数
    public static final Integer YEAR_DAY_NUMBER = 365;
    //一月的天数
    public static final Integer MONTH_DAY_NUMBER = 30;
    //一年的月数
    public static final Integer YEAR_MONTH_NUMBER = 12;
    
    //下单过期时间30分钟（转换成秒）
    public static final long EXPIRATION_DATE = 30*60;
    //微信二维码支付过期时间120分钟（转换成秒）
    public static final long WEIXIN_EXPIRATION_DATE = 2*60*60;
    
    //Content-Type 
    public static final String APPLICATION_JSON = "application/json";
    
    public static final String SESSION_MEMBER_KEY = "session_member_key";

}
