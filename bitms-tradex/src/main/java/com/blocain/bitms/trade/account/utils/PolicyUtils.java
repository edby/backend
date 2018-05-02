package com.blocain.bitms.trade.account.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import com.blocain.bitms.orm.utils.ServletsUtils;
import com.blocain.bitms.orm.utils.SpringContext;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.account.enums.SecurityPolicyEnums;
import com.blocain.bitms.trade.account.enums.TradePolicyEnums;

/**
 * 策略辅助工具类
 * <p>File：PolicyUtils.java</p>
 * <p>Title: PolicyUtils</p>
 * <p>
 *     Description:通过策略编码取策略描述文本 
 * </p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class PolicyUtils
{
    public static final Logger                 logger         = LoggerFactory.getLogger(PolicyUtils.class);
    
    private static ResourceBundleMessageSource bundleMessage  = SpringContext.getBean(ResourceBundleMessageSource.class);
    
    private static LocaleResolver              localeResolver = SpringContext.getBean(LocaleResolver.class);
    
    /**
     * 取安全验证策略
     * @param policy
     * @return
     */
    public static String getSecurityPolicy(Integer policy)
    {
        String securityPolicy = SecurityPolicyEnums.getMessage(policy);
        return getBundleMessage(securityPolicy);
    }
    
    /**
     * 取交易验证策略
     * @param policy
     * @return
     */
    public static String getTradePolicy(Integer policy)
    {
        String tradePolicy = TradePolicyEnums.getMessage(policy);
        return getBundleMessage(tradePolicy);
    }
    
    /**
     * 取Spring国际化资源
     * @param key
     * @return
     */
    private static String getBundleMessage(String key)
    {
        HttpServletRequest request = ServletsUtils.getRequest();
        String message;
        try
        {
            message = bundleMessage.getMessage(key, null, localeResolver.resolveLocale(request));
        }
        catch (NoSuchMessageException e)
        {
            LoggerUtils.logError(logger, e.getLocalizedMessage());
            message = key;
        }
        return message;
    }
}
