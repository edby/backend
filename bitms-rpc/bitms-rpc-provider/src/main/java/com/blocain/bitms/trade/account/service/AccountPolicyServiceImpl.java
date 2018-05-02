package com.blocain.bitms.trade.account.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.model.PolicyModel;

/**
 * 帐户策略服务实现
 * <p>File：AccountPolicyServiceImpl.java</p>
 * <p>Title: AccountPolicyServiceImpl</p>
 * <p>Description: AccountPolicyServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountPolicyServiceImpl implements AccountPolicyService
{
    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;
    
    @Override
    public boolean validPassword(String plainPassword, String password)
    {
        boolean flag = false;
        if (StringUtils.isBlank(plainPassword) || StringUtils.isBlank(password)) return flag;
        if (EncryptUtils.validatePassword(plainPassword, password)) flag = true;
        return flag;
    }
    
    @Override
    public boolean validSMSCode(String phone, String validCode)
    {
        return msgRecordService.validSMSCode(phone, validCode);
    }
    
    @Override
    public boolean validGaCode(String authKey, String validCode)
    {
        boolean flag = false;
        if (StringUtils.isBlank(authKey) || StringUtils.isBlank(validCode)) return flag;
        Authenticator authenticator = new Authenticator();
        if (authenticator.checkCode(EncryptUtils.desDecrypt(authKey), Long.valueOf(validCode))) flag = true;
        return flag;
    }
    
    @Override
    public boolean validEmailCode(String email, String validCode)
    {
        return msgRecordService.validEmailCode(email, validCode);
    }
    
    @Override
    public void validSecurityPolicy(Account account, PolicyModel policy) throws BusinessException
    {
        if (null == policy) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (AccountConsts.SECURITY_POLICY_DEFAULT == account.getSecurityPolicy())
        {// 验证登陆密码
            if (!validPassword(policy.getPwd(), account.getLoginPwd())) { throw new BusinessException(CommonEnums.ERROR_LOGIN_PASSWORD); }
        }
        if (AccountConsts.SECURITY_POLICY_NEEDGA == account.getSecurityPolicy())
        {// GA验证
            if (!validGaCode(account.getAuthKey(), policy.getGa())) { throw new BusinessException(CommonEnums.ERROR_GA_VALID_FAILED); }
        }
        if (AccountConsts.SECURITY_POLICY_NEEDSMS == account.getSecurityPolicy())
        {// 短信验证
            StringBuffer mobile = new StringBuffer(account.getCountry()).append(account.getMobNo());
            if (!validSMSCode(mobile.toString(), policy.getSms())) { throw new BusinessException(CommonEnums.ERROR_SMS_VALID_FAILED); }
        }
        if (AccountConsts.SECURITY_POLICY_NEEDGAANDSMS == account.getSecurityPolicy())
        {// GA和短信验证
            StringBuffer mobile = new StringBuffer(account.getCountry()).append(account.getMobNo());
            if (!validSMSCode(mobile.toString(), policy.getSms())) { throw new BusinessException(CommonEnums.ERROR_SMS_VALID_FAILED); }
            if (!validGaCode(account.getAuthKey(), policy.getGa())) { throw new BusinessException(CommonEnums.ERROR_GA_VALID_FAILED); }
        }
        if (AccountConsts.SECURITY_POLICY_NEEDGAORSMS == account.getSecurityPolicy())
        {// GA或短信验证
            StringBuffer mobile = new StringBuffer(account.getCountry()).append(account.getMobNo());
            boolean flag = validSMSCode(mobile.toString(), policy.getSms()) || validGaCode(account.getAuthKey(), policy.getGa());
            if (!flag) { throw new BusinessException(CommonEnums.ERROR_SMS_VALID_FAILED); }
        }
    }
    
    @Override
    public boolean validTradePolicy(Account account, String walletPwd)
    {
        boolean flag = false;
        if (AccountConsts.TRADE_POLICY_EVERYTIME == account.getTradePolicy())
        {// 每次都验证
            flag = validPassword(account.getWalletPwd(), walletPwd);
        }
        if (AccountConsts.TRADE_POLICY_TWOHOUR == account.getTradePolicy())
        {// 每两小时验证一次
            StringBuffer cacheKey = new StringBuffer(CacheConst.POLICY_PERFIX).append(account.getId());
            if (StringUtils.isNotBlank(RedisUtils.get(cacheKey.toString())))
            {// 缓存中有验证过的标识时，不用再次验证
                flag = true;
            }
            else
            {
                flag = validPassword(account.getWalletPwd(), walletPwd);
                if (flag)
                {// 资金密码验证成功之后将入缓存
                    RedisUtils.putObject(cacheKey.toString(), "valid", CacheConst.TWENTYFOUR_HOUR_CACHE_TIME);
                }
            }
        }
        return flag;
    }
    
    @Override
    public int errorOperatorCounter(String key)
    {
        int count = 1;
        String value = RedisUtils.get(key);
        if (StringUtils.isNotBlank(value))
        {
            count = count + Integer.valueOf(value);
            RedisUtils.putObject(key, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
        }
        else
        {
            RedisUtils.putObject(key, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
        }
        return count;
    }
}
