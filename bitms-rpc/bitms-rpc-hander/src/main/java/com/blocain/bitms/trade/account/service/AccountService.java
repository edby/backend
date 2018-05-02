/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;

import java.util.List;

/**
 * 账户表 服务接口
 * <p>File：AccountService.java </p>
 * <p>Title: AccountService </p>
 * <p>Description:AccountService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountService extends GenericService<Account>
{
    /**
     * 根据帐户名取数据
     * @param accountName
     * @return {@link Account}
     */
    Account findByName(String accountName) throws BusinessException;
    
    /**
    * 根据帐户名取正常状态的数据
    * @param accountName
    * @return {@link Account}
    */
    Account findByNameAndNormal(String accountName) throws BusinessException;
    
    /**
     * 验证帐号是否存在
     * @param email
     * @return {@link Account}
     * @throws BusinessException
     */
    Boolean valiEmail(String email) throws BusinessException;
    
    /**
     * 根据邮件地址和手机短信取用户信息
     * @param email
     * @param phone
     * @return {@link Account}
     * @throws BusinessException
     */
    Account findByEmailAndMob(String email, String phone) throws BusinessException;
    
    /**
     * 根据手机号判断是否已绑定
     * @param phone
     * @return
     * @throws BusinessException
     */
    Boolean checkBindPhone(String phone) throws BusinessException;
    
    /**
     * 根据UNID查找用户并重置密码
     * @param account
     * @return
     * @throws BusinessException
     */
    Boolean resetPass(Account account) throws BusinessException;
    
    /**
     * 注册用户
     * @param account
     * @param lang
     * @throws BusinessException
     */
    void register(Account account, String lang, String requestIP) throws BusinessException;
    
    /**
     * 手机端注册用户
     * @param account
     * @param lang
     * @throws BusinessException
     */
    String mobileRegister(Account account, String lang, String requestIP) throws BusinessException;
    
    /**
     * 注册确认
     * @param account
     * @throws BusinessException
     */
    void registerConfirm(Account account) throws BusinessException;
    
    /**
     * TRADEx注册确认
     * @param account
     * @throws BusinessException
     */
    void registerConfirmTradex(Account account) throws BusinessException;

    /**
     * TRADEX 检查账户
     * @param account
     * @throws BusinessException
     */
    void checkWalletassetTradex(Account account) throws BusinessException;
    
    /**
     * 冻结用户
     * @param accountId
     * @param reason
     * @throws BusinessException
     */
    void modifyAccountStatusToFrozen(Long accountId, String reason) throws BusinessException;
    
    /**
     * 解冻用户
     * @throws BusinessException
     */
    void modifyAccountByTask() throws BusinessException;
    
    /**
     * 修改登陆密码
     * @param id
     * @param origPass
     * @param newPass
     * @return {@link Integer}
     * @throws BusinessException
     */
    int changeLoginPwd(Long id, String origPass, String newPass) throws BusinessException;
    
    /**
     * 修改资金密码
     * @param id
     * @param fundPwd
     * @return {@link Integer}
     * @throws BusinessException
     */
    int changeFundPwd(Long id, String fundPwd) throws BusinessException;
    
    /**
     * 根据UNID得到唯一的账户信息
     * @param unid
     * @return {@link Account}
     * @throws BusinessException
     */
    Account getAccountByUnid(Long unid) throws BusinessException;
    
    /**
     * 异常账户批量冻结，冻结后的账户需要手工解冻
     * @param accountIdList
     * @return
     */
    int abNormalAcctFrozenBatch(List<Long> accountIdList) throws BusinessException;
    
    /**
     * 根据账户ID列表获取相关账户列表
     * @param accountIdList
     * @return
     * @throws BusinessException
     */
    List<Account> findListByAcctIds(List<Long> accountIdList) throws BusinessException;
}
