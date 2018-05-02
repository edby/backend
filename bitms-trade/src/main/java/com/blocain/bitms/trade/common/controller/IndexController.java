package com.blocain.bitms.trade.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.IPUtil;
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.tools.utils.StringUtils;

import io.swagger.annotations.ApiOperation;

import java.util.List;


/**
 * IndexController Introduce
 * <p>File：IndexController.java</p>
 * <p>Title: IndexController</p>
 * <p>Description: IndexController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping("")
public class IndexController extends GenericController
{

    @Autowired(required = false)
    StockInfoService stockInfoService;

    /**
     * 平台首页导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/")
    @ApiOperation(value = "平台首页导航", httpMethod = "GET")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        String ipStr = IPUtil.getOriginalIpAddr(request);
        String[] tmpIps = ipStr.split(",");
        for (String tmpIp : tmpIps)
        {//防止代码服务将IP变更掉了
            String ipInfo = GeoIPUtils.getInstance().getCountryCode(tmpIp);
            if ("CN".equals(ipInfo)) { return new ModelAndView("account/ban"); }
        }
        String locale = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(locale) && null != localeResolver)
        {
            locale = LanguageUtils.getLang(request);
            if (locale.length() <= 2)
            {
                if (locale.equalsIgnoreCase("zh")) locale = "zh_CN";
                else locale = "en_US";
            }
            localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(locale));
        }

        ModelAndView mav = new ModelAndView("account/home");
        /*List<StockInfo> list = stockInfoService.findLIstByDistinctCaptalId();
        mav.addObject("stockinfos",list);*/
        return mav;
    }
}
