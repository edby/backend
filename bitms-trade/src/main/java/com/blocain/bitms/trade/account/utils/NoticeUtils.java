package com.blocain.bitms.trade.account.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.orm.utils.ServletsUtils;
import com.blocain.bitms.orm.utils.SpringContext;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Notice;
import com.blocain.bitms.trade.account.service.NoticeService;

/**
 * 公告信息 介绍
 * <p>File：NoticeUtils.java </p>
 * <p>Title: NoticeUtils </p>
 * <p>Description:NoticeUtils </p>
 * <p>Copyright: Copyright (c) 2017/7/19 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class NoticeUtils
{
    public static final Logger logger        = LoggerFactory.getLogger(NoticeUtils.class);
    
    static NoticeService       noticeService = SpringContext.getBean(NoticeService.class);
    
    /**
     * 取最新一条公告信息
     * @return
     */
    public static Notice geLatestNotice()
    {
        try
        {
            String lang = OnLineUserUtils.getPrincipal().getLang();
            if (StringUtils.isBlank(lang)) lang = CookieUtils.get(ServletsUtils.getRequest(), BitmsConst.COOKIE_LOCALE);
            if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(ServletsUtils.getRequest());
            return noticeService.findLatestNotice(lang);
        }
        catch (RuntimeException e)
        {
            LoggerUtils.logError(logger, "取最新一条公告信息", e.getCause());
        }
        return null;
    }


}
