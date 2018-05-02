/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.boss.common.model.DictModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.StringUtils;

import io.swagger.annotations.ApiOperation;

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
@Api(description = "数据字典")
public class DictionaryController extends GenericController
{
    @Autowired(required = false)
    private DictionaryService dictionaryService;
    
    /**
     * 根据编码取字典值
     * @param code 不推荐使用
     * @return {@link List}
     * @throws BusinessException
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/dict/get", method = RequestMethod.POST)
    @ApiOperation(value = "根据编码取字典值", httpMethod = "POST")
    public List<Dictionary> getDict(HttpServletRequest request,String code) throws BusinessException
    {
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if(StringUtils.isBlank(lang)) {
            lang="zh_CN";
        }
        if (StringUtils.isBlank(code)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        return dictionaryService.findByCode(lang,code);
    }
    
    /**
     * 取所有字典
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/dict/json")
    @ApiOperation(value = "取所有字典")
    public List<DictModel> getAll() throws BusinessException
    {
        return dictionaryService.findAllDict();
    }

}
