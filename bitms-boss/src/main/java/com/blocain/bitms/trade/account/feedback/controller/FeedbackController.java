package com.blocain.bitms.trade.account.feedback.controller;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.Feedback;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.FeedbackNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 意见反馈管理 介绍
 * <p>File：FeedbackController.java </p>
 * <p>Title: FeedbackController </p>
 * <p>Description:FeedbackController </p>
 * <p>Copyright: Copyright (c) 2017/7/20 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "意见反馈管理")
public class FeedbackController extends GenericController
{
    @Autowired(required = false)
    private FeedbackNoSql           feedbackNoSql;

    @Autowired(required = false)
    private SysParameterService     sysParameterService;

    /**
     * 意见反馈管理页面导航
     * @return
     */
    @RequestMapping("/feedback/list")
    @RequiresPermissions("trade:setting:feedback:index")
    @ApiOperation(value = "消息记录查询页面导航", httpMethod = "get")
    public ModelAndView index()
    {
        ModelAndView mav = new ModelAndView("trade/account/feedback/list");
        String baseUrl = "http://bitms-release.s3.amazonaws.com";
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.OSS_FILE_BASE_URL);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter != null) { baseUrl = sysParameter.getValue(); }
        mav.addObject("baseUrl",baseUrl);
        return mav;
    }
    
    /**
     * 意见反馈管理
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/feedback/list/data")
    @RequiresPermissions("trade:setting:feedback:data")
    @ApiOperation(value = "意见反馈管理", httpMethod = "POST")
    public JsonMessage data(@ModelAttribute Feedback entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<Feedback> data = feedbackNoSql.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
}
