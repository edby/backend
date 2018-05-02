/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

/**
 * 数据典 控制器
 * <p>File：DictionaryController.java </p>
 * <p>Title: DictionaryController </p>
 * <p>Description:DictionaryController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
public class DictionaryController extends GenericController
{
    @Autowired(required = false)
    private DictionaryService dictionaryService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/dict")
    @RequiresPermissions("system:setting:dict:index")
    public String list() throws BusinessException
    {
        return "boss/common/dict/treelist";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/dict/modify")
    @RequiresPermissions("system:setting:dict:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/common/dict/modify");
        Dictionary dict = new Dictionary();
        if (null != parentId) dict.setParentId(parentId);
        if (null != id) dict = dictionaryService.selectByPrimaryKey(id);
        mav.addObject("dict", dict);
        return mav;
    }
    
    /**
     * 返回以TREEMODEL对象的所有字典数据
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/dict/tree")
    public List<TreeModel> tree(String id) throws BusinessException
    {
        return dictionaryService.findByDict(id);
    }
    
    /**
     * 根据编码取字典值
     * @param code
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/dict/get")
    public List<Dictionary> getDict(HttpServletRequest request, String code) throws BusinessException
    {
        if (StringUtils.isBlank(code)) throw new BusinessException("字典编码不可为空！");
        String lang = "zh_CN";
        return dictionaryService.findByCode(lang, code);
    }
    
    /**
     * 操作数据典
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:dict:operator")
    @RequestMapping(value = "/dict/save", method = RequestMethod.POST)
    public JsonMessage save(Dictionary info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == info.getId())
        {
            info.setCreateBy(principal.getId());
            info.setCreateDate(new Timestamp(System.currentTimeMillis()));
            info.setActive(Boolean.TRUE);
        }
        dictionaryService.save(info);
        return json;
    }
    
    /**
     * 查询数据典
     * @param entity
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("system:setting:dict:data")
    @RequestMapping(value = "/dict/data", method = RequestMethod.POST)
    public List<Dictionary> data(Dictionary entity) throws BusinessException
    {
        return dictionaryService.findList(entity);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:dict:operator")
    @RequestMapping(value = "/dict/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        dictionaryService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
