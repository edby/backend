package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.blocain.bitms.boss.common.entity.MsgRecord;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 消息发送记录 介绍
 * <p>File：MsgRecordController.java </p>
 * <p>Title: MsgRecordController </p>
 * <p>Description:MsgRecordController </p>
 * <p>Copyright: Copyright (c) 2017/7/20 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "消息发送记录")
public class MsgRecordController extends GenericController
{
    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;

    /**
     * 消息记录查询页面导航
     * @return
     */
    @RequestMapping("/msgRecord")
    @RequiresPermissions("trade:setting:msgRecord:index")
    @ApiOperation(value = "消息记录查询页面导航", httpMethod = "get")
    public ModelAndView index()
    {
        ModelAndView mav = new ModelAndView("trade/account/msgRecord/list");
        return mav;
    }
    
    /**
     * 查询消息记录
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/msgRecord/data")
    @RequiresPermissions("trade:setting:msgRecord:data")
    @ApiOperation(value = "查询消息记录", httpMethod = "POST")
    public JsonMessage data(@ModelAttribute MsgRecord entity, @ModelAttribute Pagination pagin ,String timeStart, String timeEnd, String statusString) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(DateUtils.parseDate(timeStart).getTime());
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(DateUtils.parseDate(timeEnd).getTime());
        }
        //状态特殊处理
        if( StringUtils.isBlank(statusString))
        {
            entity.setStatus(null);
        }
        else
        {
            if(StringUtils.equalsIgnoreCase(statusString,"0"))
            {
                entity.setStatus(true);
            }
            else
            {
                entity.setStatus(false);
            }
        }
        PaginateResult<MsgRecord> data = msgRecordService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
}
