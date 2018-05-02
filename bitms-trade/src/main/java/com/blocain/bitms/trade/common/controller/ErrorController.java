package com.blocain.bitms.trade.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * ErrorController Introduce
 * <p>Title: ErrorController</p>
 * <p>File：ErrorController.java</p>
 * <p>Description: ErrorController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends GenericController
{
    @RequestMapping("/403")
    public ModelAndView err_403() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("error/403");
        return mav;
    }
    
    @RequestMapping("/404")
    public ModelAndView err_404() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("error/404");
        return mav;
    }
    
    @RequestMapping("/500")
    public ModelAndView err_500() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("error/500");
        return mav;
    }
}
