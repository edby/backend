package com.blocain.bitms.trade.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.entity.Feedback;
import com.blocain.bitms.boss.common.service.FeedbackNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.AmazonS3Utils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 问题反馈控制器
 * <p>File：FeedBackController.java</p>
 * <p>Title: FeedBackController</p>
 * <p>Description: FeedBackController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
//@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "问题反馈")
public class FeedBackController extends GenericController
{
    public static final Logger logger = LoggerFactory.getLogger(FeedBackController.class);
    
    @Autowired(required = false)
    private AccountService     accountService;
    
    @Autowired(required = false)
    private FeedbackNoSql      feedbackNoSql;
    
    /**
     * 问题反馈页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/feedback")
    @ApiOperation(value = "帐户设置页面导航", httpMethod = "GET")
    public ModelAndView feedback() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/feedback");
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        return mav;
    }
    
    /**
     * 提交问题反馈
     * @param feedback
     * @return @{@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/feedback/submit", method = RequestMethod.POST)
    @ApiOperation(value = "提交问题反馈", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage feedbackSubmit(@ModelAttribute Feedback feedback) throws BusinessException
    {
        logger.debug("Feedback:" + feedback.toString());
        if (StringUtils.isNotBlank(feedback.getAttachments()))
        {
            String[] attachments = feedback.getAttachments().split(",");
            for (String attachment : attachments)
            {
                AmazonS3Utils.moveObject(attachment);// 将临时空间中的文件转移到正式空间
            }
        }
        feedbackNoSql.save(feedback);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}
