/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.entity.MsgRecord;
import com.blocain.bitms.boss.common.entity.MsgTemplate;
import com.blocain.bitms.boss.common.enums.MessageEnums;
import com.blocain.bitms.boss.common.mapper.MsgTemplateMapper;
import com.blocain.bitms.boss.common.model.EmailModel;
import com.blocain.bitms.boss.common.model.SMSResult;
import com.blocain.bitms.boss.common.utils.SMSClient;
import com.blocain.bitms.orm.core.GenericNoSqlImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.mapper.AccountMapper;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;
import com.blocain.bitms.trade.mail.SendMail;
import com.blocain.bitms.trade.mail.WithdrawalMail;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.mapper.StockInfoMapper;

/**
 * 短信记录表 服务实现类
 * <p>File：MsgRecordServiceImpl.java </p>
 * <p>Title: MsgRecordServiceImpl </p>
 * <p>Description:MsgRecordServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class MsgRecordNoSqlImpl extends GenericNoSqlImpl<MsgRecord> implements MsgRecordNoSql
{
    public static final Logger logger = LoggerFactory.getLogger(MsgRecordNoSqlImpl.class);
    
    @Autowired
    private MsgTemplateMapper  msgTemplateMapper;
    
    @Autowired
    private StockInfoMapper    stockInfoMapper;
    
    @Autowired
    private AccountMapper      accountMapper;
    
    /**
     * 邮件发送服务
     */
    @Autowired(required = false)
    private SendMail           sendMail;
    
    /**
     * 提现邮件发送服务
     */
    @Autowired(required = false)
    private WithdrawalMail     withdrawalMail;
    
    public MsgRecordNoSqlImpl()
    {
        super(MsgRecord.class);
    }
    
    @Override
    public void sendSms(String phone, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(phone)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        String expireKey = new StringBuffer(CacheConst.CACHE_EXPIRE_SMS_PERFIX).append(BitmsConst.SEPARATOR).append(phone).toString();
        if (StringUtils.isNotBlank(RedisUtils.get(expireKey)))
        { // 一分钟内只允许发送一次短信
            throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        }
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_SMS_SENDVALICODE, MessageConst.MESSAGE_SMS, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String cacheKey = new StringBuffer(CacheConst.CACHE_SEND_SMS_PERFIX).append(BitmsConst.SEPARATOR).append(phone).toString();
        String randomKey = SerialnoUtils.randomNum(6);
        RedisUtils.putObject(cacheKey, randomKey, CacheConst.FIFTEEN_MINUTE_CACHE_TIME);
        RedisUtils.putObject(expireKey, phone, 60);// 加入一分钟限制
        String content = String.format(template.getContent(), randomKey);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_SMS, phone, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                SMSResult result = SMSClient.sendIntSMS(phone, content);
                if (StringUtils.isNotBlank(result.getMsgid()))
                {// 表示发送成功
                    record.setStatus(Boolean.TRUE);
                }
            }
            catch (BusinessException e)
            {
                LoggerUtils.logError(logger, e.getMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public boolean validSMSCode(String phone, String validCode)
    {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(validCode)) return false;
        String cacheKey = new StringBuffer(CacheConst.CACHE_SEND_SMS_PERFIX).append(BitmsConst.SEPARATOR).append(phone).toString();
        String sysCode = RedisUtils.get(cacheKey);
        boolean flag = StringUtils.equalsIgnoreCase(validCode, sysCode) ? true : false;
        if (flag)
        {
            String expireKey = new StringBuffer(CacheConst.CACHE_EXPIRE_SMS_PERFIX).append(BitmsConst.SEPARATOR).append(phone).toString();
            RedisUtils.del(cacheKey);// 用完之后清除缓存
            RedisUtils.del(expireKey);// 清除发送发送次数缓存
        }
        return flag;
    }
    
    @Override
    public void sendRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByName(email);// 排除已经存在的邮件地址
        if (null != account) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_EMAIL_REGISTER, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        Long uid = SerialnoUtils.buildPrimaryKey();
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheAddress = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(email);
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(uid);
        RedisUtils.putObject(cacheKey.toString(), new EmailModel(email, invitCode, randomKey, requestIP), CacheConst.THIRTY_MINUTE_CACHE_TIME);
        RedisUtils.putObject(cacheAddress.toString(), cacheKey.toString(), CacheConst.THIRTY_MINUTE_CACHE_TIME);
        String confirmUrl = BitmsConst.HOST_URL + "/register/confirm?uid=" + uid + "&oid=" + randomKey;
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, email, confirmUrl, confirmUrl);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendTradexRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TRADEX_TEMPLATE_EMAIL_REGISTER, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(email);
        String randNum = SerialnoUtils.randomNum(6);
        RedisUtils.putObject(cacheKey.toString(), randNum, CacheConst.THIRTY_MINUTE_CACHE_TIME);
        String content = String.format(template.getContent().replace("%;", "%%;"), randNum);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public String sendMobileRegisterEmail(String email, String invitCode, String lang, String requestIP) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByName(email);// 排除已经存在的邮件地址
        if (null != account) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_MOBILE_REGISTER, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randNum = SerialnoUtils.randomNum(6);
        StringBuffer cacheAddress = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(email);
        RedisUtils.putObject(cacheAddress.toString(), new EmailModel(email, invitCode, randNum, requestIP), CacheConst.THIRTY_MINUTE_CACHE_TIME);
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, email, randNum);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
        return cacheAddress.toString();
    }
    
    @Override
    public void sendEmailForgetPasswordCode(String email, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByNameAndStatus(email, AccountConsts.ACCOUNT_STATUS_NORMAL);
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_EMAIL_RESETPASSWORD, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getUnid());
        RedisUtils.putObject(cacheKey.toString(), randomKey, CacheConst.DEFAULT_CACHE_TIME);
        String resetPassUrl = BitmsConst.HOST_URL + "/resetPass?unid=" + account.getUnid() + "&op_id=" + randomKey;
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, email, resetPassUrl, resetPassUrl);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendEmailForgetPasswordCodeTradex(String email, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByNameAndStatus(email, AccountConsts.ACCOUNT_STATUS_NORMAL);
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TRADEX_TEMPLATE_EMAIL_RESETPASSWORD, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getUnid());
        RedisUtils.putObject(cacheKey.toString(), randomKey, CacheConst.DEFAULT_CACHE_TIME);
        String resetPassUrl = BitmsConst.HOST_URL + "/resetPass?unid=" + account.getUnid() + "&op_id=" + randomKey;
        String content = String.format(template.getContent().replace("%;", "%%;"), email, resetPassUrl, resetPassUrl);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public Boolean validEmailCode(String email, String code) throws BusinessException
    {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) return false;
        String cacheKey = RedisUtils.get(email);
        return StringUtils.equalsIgnoreCase(code, cacheKey);
    }
    
    /**
     * 发送激活提现地址邮件
     * @param email
     * @param accountCollectAddr
     * @param lang
     * @throws BusinessException
     */
    @Override
    public void sendActiveCollectAddrEmail(String email, AccountCollectAddr accountCollectAddr, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByNameAndStatus(email, AccountConsts.ACCOUNT_STATUS_NORMAL);
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_SEND_EMAIL_ACTIVE_COLLECT_ADDR, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(accountCollectAddr.getId());
        RedisUtils.putObject(cacheKey.toString(), randomKey, CacheConst.THIRTY_MINUTE_CACHE_TIME);
        String baseUrl = BitmsConst.HOST_URL;
        String address = baseUrl + "/fund/withdraw/activeCollectAddr?id=" + accountCollectAddr.getId() + "&uid=" + accountCollectAddr.getAccountId() + "&op_id="
                + randomKey;
        String collectAddr = EncryptUtils.desDecrypt(accountCollectAddr.getCollectAddr().toString());
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, // 给logo用
                email, // 用户
                // des解密
                collectAddr, collectAddr, collectAddr, address, address);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                withdrawalMail.initialize();
                withdrawalMail.setTo(email);
                withdrawalMail.setSubject(template.getTitle());
                withdrawalMail.setText(content, true);
                withdrawalMail.send();
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendActiveRaiseEmail(String email, AccountFundCurrent accountFundCurrent, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByNameAndStatus(email, AccountConsts.ACCOUNT_STATUS_NORMAL);
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_SEND_EMAIL_ACTIVE_RAISE, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(accountFundCurrent.getId());
        RedisUtils.putObject(cacheKey.toString(), randomKey, CacheConst.THIRTY_MINUTE_CACHE_TIME);
        StockInfo info = stockInfoMapper.selectByPrimaryKey(accountFundCurrent.getStockinfoId());
        String baseUrl = BitmsConst.HOST_URL;
        String address = baseUrl + "/fund/withdraw/activeWithdraw?id=" + accountFundCurrent.getId() + "&uid=" + accountFundCurrent.getAccountId() + "&op_id=" + randomKey;
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, // 给logo用
                email, // 用户
                // 发送邮件 进行des解密
                EncryptUtils.desDecrypt(accountFundCurrent.getWithdrawAddr().toString()),
                new DecimalFormat(",##0.0000").format(accountFundCurrent.getOccurAmt().setScale(8, BigDecimal.ROUND_HALF_UP).subtract(accountFundCurrent.getNetFee())),
                info.getStockCode(), address, address, address);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendActiveWithdrawEmail(String email, AccountFundWithdraw accountFundWithdraw, String lang) throws BusinessException
    {
        if (StringUtils.isBlank(email)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.findByNameAndStatus(email, AccountConsts.ACCOUNT_STATUS_NORMAL);
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(MessageConst.TEMPLATE_SEND_EMAIL_ACTIVE_RAISE, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String randomKey = SerialnoUtils.buildUUID();
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(accountFundWithdraw.getId());
        RedisUtils.putObject(cacheKey.toString(), randomKey, CacheConst.THIRTY_MINUTE_CACHE_TIME);
        StockInfo info = stockInfoMapper.selectByPrimaryKey(accountFundWithdraw.getStockinfoId());
        String baseUrl = BitmsConst.HOST_URL;
        String address = baseUrl + "/fund/withdraw/activeWithdraw?id=" + accountFundWithdraw.getId() + "&uid=" + accountFundWithdraw.getAccountId() + "&op_id=" + randomKey;
        String content = String.format(template.getContent().replace("%;", "%%;"), BitmsConst.HOST_EMAIL_LOGO_URL, // 给logo用
                email, // 用户
                DateUtils.getDateFormat(new Date(), DateConst.DATE_FORMAT_YMDHMS),
                // 发送邮件 进行des解密
                EncryptUtils.desDecrypt(accountFundWithdraw.getWithdrawAddr().toString()),
                new DecimalFormat(",##0.0000").format(accountFundWithdraw.getWithdrawAmt().setScale(8, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())),
                info.getStockCode(), address, address, address);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendAlarmSms(String phone, String templateId, String msgInfo) throws BusinessException
    {
        if (StringUtils.isBlank(phone)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // 2. 获取消息模板，并将消息内容根据消息模板格式化
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(templateId, MessageConst.MESSAGE_SMS, "zh_CN");
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String content = String.format(template.getContent(), msgInfo);
        String[] phones = phone.toString().split(",");
        List<String> list = java.util.Arrays.asList(phones);
        StringBuffer tel = new StringBuffer();
        for (int i = 0; i < list.size(); i++)
        {
            // 初始化消息推送记录流水对象
            MsgRecord record = new MsgRecord(MessageConst.MESSAGE_SMS, phone, content, Boolean.FALSE);
            record.setId(SerialnoUtils.buildPrimaryKey());
            record.setCreateDate(CalendarUtils.getCurrentLong());
            tel.setLength(0);
            tel.append(list.get(i));
            new Thread(() -> {
                try
                {
                    SMSResult result = SMSClient.sendIntSMS(tel.toString(), content);
                    if (StringUtils.isNotBlank(result.getMsgid()))
                    {// 表示发送成功
                        record.setStatus(Boolean.TRUE);
                    }
                }
                catch (BusinessException e)
                {
                    LoggerUtils.logError(logger, e.getMessage());
                }
                finally
                {
                    mongoTemplate.insert(record);
                }
            }).start();
        }
    }
    
    @Override
    public void sendRemindSMS(String phone, String templateKey, String lang, Object ... args) throws BusinessException
    {
        if (StringUtils.isBlank(phone)) return;
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(templateKey, MessageConst.MESSAGE_SMS, lang);
        if (null == template) return;
        String content = String.format(template.getContent(), args);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_SMS, phone, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                SMSResult result = SMSClient.sendIntSMS(phone, content);
                if (StringUtils.isNotBlank(result.getMsgid()))
                {// 表示发送成功
                    record.setStatus(Boolean.TRUE);
                }
            }
            catch (BusinessException e)
            {
                LoggerUtils.logError(logger, e.getMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendRemindEmail(String email, String templateKey, String lang, Object ... args) throws BusinessException
    {
        if (StringUtils.isBlank(email)) return;
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(templateKey, MessageConst.MESSAGE_EMAIL, lang);
        if (null == template) return;
        String content = String.format(template.getContent(), args);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    sendMail.setText(content, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public void sendAlarmEmail(String email, InternetAddress[] appendEmails, String msgInfo, String templateId) throws BusinessException
    {
        if (StringUtils.isBlank(email)) return;
        MsgTemplate template = msgTemplateMapper.findByKeyAndLang(templateId, MessageConst.MESSAGE_EMAIL, "zh_CN");
        if (null == template) throw new BusinessException(MessageEnums.ERROR_TEMPLATE_NOTEXISTS);
        String content = String.format(template.getContent().replace("%;", "%%;"), msgInfo);
        MsgRecord record = new MsgRecord(MessageConst.MESSAGE_EMAIL, email, content, Boolean.FALSE);
        record.setId(SerialnoUtils.buildPrimaryKey());
        record.setCreateDate(CalendarUtils.getCurrentLong());
        new Thread(() -> {
            try
            {
                if (BitmsConst.EMAIL_PROVIDER_AMAZON.equals(BitmsConst.EMAIL_SENDER_PROVIDER))
                {// 亚马逊邮件推送服务
                    AmazonSESUtils.sendMail(template.getTitle(), content, email, appendEmails);
                }
                else
                {
                    sendMail.initialize();
                    sendMail.setTo(email);
                    sendMail.setSubject(template.getTitle());
                    if (appendEmails != null) sendMail.setBcc(appendEmails);
                    sendMail.setText(msgInfo, true);
                    sendMail.send();
                }
                record.setStatus(Boolean.TRUE);
            }
            catch (MessagingException e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
                mongoTemplate.insert(record);
            }
        }).start();
    }
    
    @Override
    public PaginateResult<MsgRecord> search(Pagination pagin, MsgRecord entity) throws BusinessException
    {
        Query query = new Query();
        if (StringUtils.isNotBlank(entity.getType()))
        {
            query.addCriteria(Criteria.where("type").is(entity.getType()));
        }
        if (StringUtils.isNotBlank(entity.getObject()))
        {
            query.addCriteria(Criteria.where("object").regex(entity.getObject()));
        }
        if (null != entity.getStatus())
        {
            query.addCriteria(Criteria.where("status").is(entity.getStatus()));
        }
        pagin.setTotalRows(mongoTemplate.count(query, MsgRecord.class));
        query.with(new PageRequest(pagin.getPage() - 1, pagin.getRows()));// 分页
        Sort.Direction direction = Sort.Direction.DESC;
        if (pagin.getOrder().equalsIgnoreCase(BitmsConst.DEFAULT_SORT_ASC)) direction = Sort.Direction.ASC;
        query.with(new Sort(direction, pagin.getSort()));// 排序
        List<MsgRecord> data = mongoTemplate.find(query, MsgRecord.class);
        return new PaginateResult<>(pagin, data);
    }
}
