package com.blocain.bitms.trade.account.consts;

/**
 * 日志类型常量
 * <p>File：AccountLogConsts.java </p>
 * <p>Title: AccountLogConsts </p>
 * <p>Description:AccountLogConsts </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountLogConsts
{
    private AccountLogConsts()
    {
    }
    
    // 默认记录
    public static final String LOG_TYPE_DEFAULT = "default";
    
    // 安全设置
    public static final String LOG_TYPE_SETTING = "setting";
    
    // 登陆
    public static final String LOG_TYPE_LOGIN   = "login";
}
