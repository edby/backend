package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.boss.common.service.CacheService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 缓存管理控制器
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
public class CacheController extends GenericController
{
    @Autowired(required = false)
    private CacheService cacheService;
    
    /**
     * 清除指定缓存
     *
     * @param key
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cache/del/{key}")
    public JsonMessage delete(@PathVariable String key) throws BusinessException
    {
        cacheService.delete(key);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 清除所有缓存
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cache/clean/all")
    public JsonMessage cleanAll() throws BusinessException
    {
        cacheService.cleanAll();
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 清除Mybatis缓存
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cache/clean/mybatis")
    public JsonMessage cleanMybatis() throws BusinessException
    {
        cacheService.cleanMybatis();
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 清除会话缓存
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cache/clean/session")
    public JsonMessage cleanSession() throws BusinessException
    {
        cacheService.cleanSession();
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
