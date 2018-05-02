package com.blocain.bitms.trade.common.controller;

import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.fund.service.SystemWalletAddrERC20Service;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private SystemWalletAddrERC20Service systemWalletAddrERC20Service;
    
    @Autowired(required = false)
    private EnableService                enableService;
    
    @Autowired(required = false)
    private StockInfoService             stockInfoService;
    
    @Autowired(required = false)
    private Erc20TokenService            erc20TokenService;
    
    @Autowired(required = false)
    private SysParameterService          sysParameterService;
    
    /**
     * 平台首页导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/")
    @ApiOperation(value = "平台首页导航", httpMethod = "GET")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        if (localeResolver == null) { throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?"); }
        localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString("en_US"));
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        ModelAndView mav = new ModelAndView("exchange/market");
        boolean isLogin = checkLogin();
        mav.addObject("label", false);
        mav.addObject("longStatus", isLogin);
        return mav;
    }
    /**
     * 检查登录
     */
    public boolean checkLogin()
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (principal == null)
        {
            return false;
        }
        else
        {
            if (principal.getId() == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
