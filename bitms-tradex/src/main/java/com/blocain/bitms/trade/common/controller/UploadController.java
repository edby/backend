package com.blocain.bitms.trade.common.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.AmazonS3Utils;
import com.blocain.bitms.tools.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 文件上传控制器 Introduce
 * <p>File：UploadController.java</p>
 * <p>Title: UploadController</p>
 * <p>Description: UploadController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "文件上传")
public class UploadController extends GenericController
{
    /**
     * 获取文件上传策略
     * @param dir 上传的模拟地址
     * @return {@link Map}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/upload/policy")
    @ApiOperation(value = "获取文件上传策略", httpMethod = "GET")
    @ApiImplicitParam(name = "dir", value = "上传的路径地址")
    public Map<String, Object> getPostPolicy(String dir) throws BusinessException
    {
        if (null == OnLineUserUtils.getPrincipal())
        {// 加入用户登陆限制
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        if (StringUtils.isBlank(dir)) dir = "";
        return AmazonS3Utils.getPostPolicy(dir, BitmsConst.BUCKET_TEMP_URL);
    }
    
    /**
     * 转移文件存储空间
     * @param fileName 文件名
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @ApiOperation(value = "转移文件存储空间", httpMethod = "POST")
    @RequestMapping(value = "/upload/transferObject", method = RequestMethod.POST)
    @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "form")
    public JsonMessage transferObject(String fileName) throws BusinessException
    {
        if (null == OnLineUserUtils.getPrincipal())
        {// 加入用户登陆限制
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        boolean flag = AmazonS3Utils.moveObject(fileName);
        return getJsonMessage(flag ? CommonEnums.SUCCESS : CommonEnums.FAIL);
    }
    
    /**
     * 删除临时空间中的文件
     * @param fileName 文件名
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @ApiOperation(value = "删除临时空间中的文件", httpMethod = "GET")
    @RequestMapping(value = "/upload/deleteObject", method = RequestMethod.GET)
    @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "form")
    public JsonMessage deleteObject(String fileName) throws BusinessException
    {
        if (null == OnLineUserUtils.getPrincipal())
        {// 加入用户登陆限制
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        boolean flag = AmazonS3Utils.deleteObject(fileName);
        return getJsonMessage(flag ? CommonEnums.SUCCESS : CommonEnums.FAIL);
    }
}
