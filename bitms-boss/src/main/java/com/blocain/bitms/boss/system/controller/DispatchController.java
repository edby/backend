package com.blocain.bitms.boss.system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 控制分发 Introduce
 * <p>File：DispatchController.java </p>
 * <p>Title: DispatchController </p>
 * <p>Description:DispatchController </p>
 * <p>Copyright: Copyright (c) 17/6/21</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
public class DispatchController extends GenericController
{
    /**
     * 默认请求
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @RequestMapping("/dispatch")
    public ModelAndView excute() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/index");
        mav.addObject("object", OnLineUserUtils.getPrincipal());
        return mav;
    }
    
    /**
     * 菜单列表请求
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/menuTree")
    public JsonMessage menuTree() throws BusinessException
    {
        List<TreeModel> data = OnLineUserUtils.getUserResources();
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    /**
     * 内容中心请求
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @RequestMapping("/content")
    public ModelAndView content() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("basic/content");
        return mav;
    }
}
