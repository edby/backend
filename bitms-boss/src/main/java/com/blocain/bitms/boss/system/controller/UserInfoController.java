/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.controller;

import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.RoleInfoService;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 用户基础信息表 控制器
 * <p>File：UserInfoController.java </p>
 * <p>Title: UserInfoController </p>
 * <p>Description:UserInfoController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
public class UserInfoController extends GenericController
{
    @Autowired(required = false)
    private UserInfoService userInfoService;
    
    @Autowired(required = false)
    private RoleInfoService roleInfoService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/user")
    @RequiresPermissions("system:setting:user:index")
    public String list() throws BusinessException
    {
        return "boss/system/user/list";
    }
    
    /**
     * 编辑页面导航
     * @param id 
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/user/modify")
    @RequiresPermissions("system:setting:user:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/user/modify");
        UserInfo user = null;
        String roleIds = null;
        if (null != id)
        {
            user = userInfoService.selectByPrimaryKey(id);
            List<RoleInfo> roles = roleInfoService.findByUserId(user.getId());
            if (ListUtils.isNotNull(roles))
            {
                StringBuffer tmpIds = new StringBuffer();
                for (RoleInfo role : roles)
                {
                    tmpIds.append(role.getId()).append(",");
                }
                roleIds = tmpIds.substring(0, tmpIds.lastIndexOf(","));
            }
        }
        mav.addObject("user", user);
        mav.addObject("roleIds", roleIds);
        return mav;
    }

    /**
     * 绑定GA界面
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/user/bindga")
    @RequiresPermissions("system:setting:user:operator")
    public ModelAndView bindGa(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/user/bindga");
        UserInfo user = null;
        String roleIds = null;
        if (null != id)
        {
            user = userInfoService.selectByPrimaryKey(id);
            List<RoleInfo> roles = roleInfoService.findByUserId(user.getId());
            if (ListUtils.isNotNull(roles))
            {
                StringBuffer tmpIds = new StringBuffer();
                for (RoleInfo role : roles)
                {
                    tmpIds.append(role.getId()).append(",");
                }
                roleIds = tmpIds.substring(0, tmpIds.lastIndexOf(","));
            }
        }
        Map<String, String> result = Maps.newHashMap();
//        String issuer = BitmsConst.PROJECT_BOSS_DEV_NAME;
//        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("production"))
//        {
//            issuer = BitmsConst.PROJECT_BOSS_NAME;
//        }
//        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("development"))
//        {
//            issuer = BitmsConst.PROJECT_BOSS_TEST_NAME;
//        }
        String issuer = BitmsConst.BITMS_PROJECT_NAME + "_BOSS";
        if(StringUtils.isBlank(user.getAuthKey()))
        {
            String secretKey = Authenticator.generateSecretKey();
            result.put("secretKey", secretKey);
            result.put("email", user.getUserName());
            result.put("gaInfo", "otpauth://totp/" + user.getUserName().toString() + "?secret=" + secretKey + "&issuer=" + issuer);

            mav.addObject("gaMap", result);
            mav.addObject("bindStatus", false);
        }
        mav.addObject("user", user);
        mav.addObject("roleIds", roleIds);
        return mav;
    }


    /**
     * 解绑GA界面
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/user/unbindga")
    @RequiresPermissions("system:setting:user:operator")
    public ModelAndView unbindga(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/user/unbindga");
        UserInfo user = null;
        String roleIds = null;
        if (null != id)
        {
            user = userInfoService.selectByPrimaryKey(id);
            List<RoleInfo> roles = roleInfoService.findByUserId(user.getId());
            if (ListUtils.isNotNull(roles))
            {
                StringBuffer tmpIds = new StringBuffer();
                for (RoleInfo role : roles)
                {
                    tmpIds.append(role.getId()).append(",");
                }
                roleIds = tmpIds.substring(0, tmpIds.lastIndexOf(","));
            }
        }
        mav.addObject("user", user);
        mav.addObject("roleIds", roleIds);
        return mav;
    }

    /**
     *  解绑GA操作
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/clearga", method = RequestMethod.POST)
    public JsonMessage saveunGa(UserInfo info, String gaCode) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        UserInfo operateUser = userInfoService.selectByPrimaryKey(principal.getId());
        if(operateUser==null || StringUtils.isBlank(operateUser.getAuthKey()))
        {
            throw new BusinessException("你未绑定GA");
        }
        String secretKey = EncryptUtils.desDecrypt(operateUser.getAuthKey());
        Authenticator authenticator = new Authenticator();
        if ( StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
        {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!authenticator.checkCode(secretKey, Double.valueOf(gaCode).longValue()))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        UserInfo userinfo = userInfoService.selectByPrimaryKey(info.getId());
        userinfo.setAuthKey("");
        userInfoService.updateByPrimaryKey(userinfo);
        return getJsonMessage(CommonEnums.SUCCESS);
    }


    /**
     *  绑定GA操作
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/savega", method = RequestMethod.POST)
    public JsonMessage saveGa(UserInfo info,String secretKey, String gaCode) throws BusinessException
    {
        Authenticator authenticator = new Authenticator();
        if ( StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
        {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!authenticator.checkCode(secretKey, Double.valueOf(gaCode).longValue()))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        UserInfo userinfo = userInfoService.selectByPrimaryKey(info.getId());
        userinfo.setAuthKey(EncryptUtils.desEncrypt(secretKey));
        userInfoService.updateByPrimaryKey(userinfo);
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
    * 操作用户基础信息表
    * @param info
    * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
    * @throws com.blocain.bitms.tools.exception.BusinessException
    */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public JsonMessage save(UserInfo info) throws BusinessException
    {
        UserInfo old =new UserInfo();
        if(info.getId()!=null)
        {
            old = userInfoService.selectByPrimaryKey(info.getId());
            if(StringUtils.isBlank(info.getPassWord()))
            {
                info.setPassWord(old.getPassWord());
            }
        }
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        userInfoService.save(info);
        return json;
    }
    
    /**
     * 查询用户基础信息表
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequiresPermissions("system:setting:user:data")
    @RequestMapping(value = "/user/data", method = RequestMethod.POST)
    public JsonMessage data(UserInfo entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<UserInfo> result = userInfoService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        userInfoService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 启用或信用用户
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/changeStatus", method = RequestMethod.POST)
    public JsonMessage changeStatus(Long id) throws BusinessException
    {
        if (null == id) throw new BusinessException("操作编号不可为空！");
        UserInfo userInfo = userInfoService.selectByPrimaryKey(id);
        userInfo.setActive(!userInfo.getActive());
        userInfoService.updateByPrimaryKey(userInfo);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
    * 重置用户密码
    * @param id
    * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
    * @throws com.blocain.bitms.tools.exception.BusinessException
    */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:user:operator")
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(Long id) throws BusinessException
    {
        if (null == id) throw new BusinessException("操作编号不可为空！");
        UserInfo userInfo = userInfoService.selectByPrimaryKey(id);
        userInfo.setPassWord(EncryptUtils.entryptPassword(BitmsConst.DEFAULT_USER_PASSWORD));
        userInfoService.updateByPrimaryKey(userInfo);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
