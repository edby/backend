package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.model.PolicyModel;

/**
 * 帐户策略服务
 * <p>
 *     将所有安全验证策略和交易验证策略统一封装到一个服务中实现
 * </p>
 * <p>File：AccountStrategyService.java</p>
 * <p>Title: AccountStrategyService</p>
 * <p>Description: AccountStrategyService</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface AccountPolicyService
{
    /**
     * 验证帐户密码或资金密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return {@link Boolean}
     */
    boolean validPassword(String plainPassword, String password);
    
    /**
     * 验证手机验证码
     * @param phone 手机号码
     * @param validCode 验证码
     * @return {@link Boolean}
     */
    boolean validSMSCode(String phone, String validCode);
    
    /**
     * 验证GA码
     * @param authKey 私钥
     * @param validCode 难证码
     * @return {@link Boolean}
     */
    boolean validGaCode(String authKey, String validCode);
    
    /**
     * 校验邮件验证码
     * @param email 邮件
     * @param validCode 验证码
     * @return {@link Boolean}
     */
    boolean validEmailCode(String email, String validCode);
    
    /**
     * 验证安全策略，不包含密码验证
     * @param account 帐户信息
     * @param policy 策略
     * @throws BusinessException
     */
    void validSecurityPolicy(Account account, PolicyModel policy)throws BusinessException;
    
    /**
     * 验证交易策略
     * @param account 帐户信息
     * @param walletPwd 资金密码
     * @return {@link Boolean}
     */
    boolean validTradePolicy(Account account, String walletPwd);
    
    /**
     * 错误操作记数
     * <p>
     *     用于关键业务需要错误计数
     * </p>
     * @param key
     * @return {@link Integer}
     */
    int errorOperatorCounter(String key);
}
