/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import javax.mail.internet.InternetAddress;

import com.blocain.bitms.boss.common.entity.MsgRecord;
import com.blocain.bitms.orm.core.GenericNoSql;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;

/**
 * 短信记录表 服务接口
 * <p>File：MsgRecordService.java </p>
 * <p>Title: MsgRecordService </p>
 * <p>Description:MsgRecordService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface MsgRecordNoSql extends GenericNoSql<MsgRecord>
{
    /**
     * 发送预警短信
     * @param phone        手机号
     * @param templateId   消息模板ID
     * @param msgInfo      预警消息
     * @throws BusinessException
     */
    void sendAlarmSms(String phone, String templateId, String msgInfo) throws BusinessException;
    
    /**
     * 发送短信提醒
     * @param phone
     * @param templateKey
     * @param lang
     * @param args
     * @throws BusinessException
     */
    void sendRemindSMS(String phone, String templateKey, String lang, Object ... args) throws BusinessException;
    
    /**
     * 发送邮件提醒
     * @param email
     * @param templateKey
     * @param lang
     * @param args
     * @throws BusinessException
     */
    void sendRemindEmail(String email, String templateKey, String lang, Object ... args) throws BusinessException;
    
    /**
     * 发送手机验证码
     * @param phone 手机号
     * @param lang 语言编码
     * @throws BusinessException
     */
    void sendSms(String phone, String lang) throws BusinessException;
    
    /**
     * 验证手机验证码
     * @param phone
     * @param validCode
     * @return {@link Boolean}
     */
    boolean validSMSCode(String phone, String validCode);
    
    /**
     * 发送注册的邮件
     * @param email 邮件地址
     * @param invitCode 邀请码
     * @param lang 语言
     * @throws BusinessException
     */
    void sendRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException;

    /**
     * TRADE发送注册的邮件
     * @param email 邮件地址
     * @param invitCode 邀请码
     * @param lang 语言
     * @throws BusinessException
     */
    void sendTradexRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException;


    /**
     * 发送注册的邮件
     * @param email 邮件地址
     * @param invitCode 邀请码
     * @param lang 语言
     * @throws BusinessException
     */
    String sendMobileRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException;

    /**
     * 发送邮件找回密码的链接
     * @param email
     * @param lang
     * @throws BusinessException
     */
    void sendEmailForgetPasswordCode(String email, String lang) throws BusinessException;

    /**
     * TRADEX发送邮件找回密码的链接
     * @param email
     * @param lang
     * @throws BusinessException
     */
    void sendEmailForgetPasswordCodeTradex(String email, String lang) throws BusinessException;
    
    /**
     * 校验邮件验证码
     * @param email
     * @param code
     * @return {@link Boolean}
     * @throws BusinessException
     */
    Boolean validEmailCode(String email, String code) throws BusinessException;
    
    /**
     * 发送激活提现地址邮件
     * @param email
     * @param accountCollectAddr
     * @param lang
     * @throws BusinessException
     */
    void sendActiveCollectAddrEmail(String email, AccountCollectAddr accountCollectAddr, String lang) throws BusinessException;
    
    /**
     * 发送激活提现邮件-过期
     * @param email
     * @param accountFundCurrent
     * @param lang
     * @throws BusinessException
     */
    void sendActiveRaiseEmail(String email, AccountFundCurrent accountFundCurrent, String lang) throws BusinessException;
    
    /**
     * 发送激活提现邮件
     * @param email
     * @param accountFundWithdraw
     * @param lang
     * @throws BusinessException
     */
    void sendActiveWithdrawEmail(String email, AccountFundWithdraw accountFundWithdraw, String lang) throws BusinessException;
    
    /**
     * 发送注册的邮件
     * @param email        邮件地址
     * @param appendEmails 抄送的邮件地址
     * @param msgInfo      预警提示
     * @param templateId   消息模板ID
     * @throws BusinessException
     */
    void sendAlarmEmail(String email, InternetAddress[] appendEmails, String msgInfo, String templateId) throws BusinessException;
}
