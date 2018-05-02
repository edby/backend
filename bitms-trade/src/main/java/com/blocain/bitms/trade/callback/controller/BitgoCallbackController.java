/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.callback.controller;

import javax.servlet.http.HttpServletResponse;

import com.blocain.bitms.payment.bitgo.model.WebhookModelV2;
import com.blocain.bitms.tools.consts.BitmsConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmService;

import io.swagger.annotations.Api;

/**
 *  BitgoCallback  控制器
 * <p>File：BitgoCallbackController.java</p>
 * <p>Title: BitgoCallbackController</p>
 * <p>Description:BitgoCallbackController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.CALLBACK)
@Api(description = "bitgoCallback")
public class BitgoCallbackController extends GenericController
{
    public static final Logger       logger = LoggerFactory.getLogger(BitgoCallbackController.class);
    
    @Autowired(required = false)
    private BlockTransConfirmService blockTransConfirmService;
    
    /**
     * bitgoCallback 交易通知回调
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月20日 上午8:35:04
     */
    @RequestMapping(value = "/bitgoCallback/webhook/v2/btc", method = RequestMethod.POST)
    public void transWebhook(@RequestBody WebhookModelV2 webhookModel, HttpServletResponse reponse) throws BusinessException
    {
        logger.info("bitgoCallback/webhook/v2/btc交易通知回调 transWebhook webhookModel:" + webhookModel.toString());
        try
        {
            if (StringUtils.isBlank(webhookModel.getType())) { throw new BusinessException("通知类型不能为空"); }
            // if (StringUtils.isBlank(webhookModel.getWalletId())) { throw new BusinessException("钱包ID不能为空"); }
            if (StringUtils.isBlank(webhookModel.getHash())) { throw new BusinessException("交易ID不能为空"); }
            if ("transfer".equals(webhookModel.getType()))
            {
                // 根据bitgoCallback 交易通知回调 插入区块交易确认表中
                // 异步传入BitGo钱包ID、区块交易唯一ID
                logger.info("bitgo交易通知回调 transWebhook webhookModel:" + webhookModel.toString());
                // blockTransConfirmService.createWalletTransRecord(webhookModel.getWalletId(), webhookModel.getHash());
                blockTransConfirmService.createBtcWalletTransRecord(webhookModel.getHash());
            }

            // 回写
            reponse.getWriter().write("ok");
        }
        catch (Exception e)
        {
            logger.error("BigGo交易通知回调失败!"+e.getLocalizedMessage());
            throw new BusinessException("BigGo交易通知回调失败");
        }
    }
}
