package com.blocain.bitms.security;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.exception.AccountPolicyException;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;

/**
 * <p>File：TradeAuthorizingRealm.java </p>
 * <p>Title: 系统安全认证实现类 </p>
 * <p>Description: TradeAuthorizingRealm </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 15:42</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class TradeAuthorizingRealm extends AuthorizingRealm
{
    private AccountService       accountService;
    
    private AccountPolicyService accountPolicyService;
    
    // 凭证ID
    private static final String  PRINCIPALS_ID = "principalId";
    
    /**
     * 认证回调函数, 登录时调用
     * <p>
     *  一次性将用户认证、操作权限等信息放到用户会话中
     * </p>
     * @param authToken
     * @return {@link AuthenticationInfo}
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException
    {
        AccountToken token = (AccountToken) authToken;
        Account account = accountService.findByName(token.getUsername().toLowerCase());
        if (null == account) throw new UnknownAccountException("user not exists");
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()// 加入冻结的用户筛选
                || !account.verifySignature())
            throw new LockedAccountException("data error");
        if (!EncryptUtils.validatePassword(String.valueOf(token.getPassword()), account.getLoginPwd()))
        {// 密码连续错误
            throw new IncorrectCredentialsException("password error!");
        }
        if (account.getSecurityPolicy() > AccountConsts.SECURITY_POLICY_DEFAULT)
        {// 说明用户启用了其它安全验证策略
            try
            {
                accountPolicyService.validSecurityPolicy(account, token.getPolicy());
            }
            catch (BusinessException e)
            {
                token.setId(account.getId());
                token.setGa(null != account.getAuthKey());
                token.setPhone(null != account.getMobNo());
                token.setLevel(account.getSecurityPolicy());
                token.setMobNo(account.getMobNo());
                throw new AccountPolicyException("policy valid error!");
            }
        }
        cleanOtherUsers(account.getId());
        UserPrincipal userPrincipal = new UserPrincipal(account.getId(), account.getUnid(), account.getAccountName(), account.getAccountName(), account.getMobNo(), account.getEmail(),account.getLang(), account.getCountry(),account.getAuthKey());
        return new SimpleAuthenticationInfo(userPrincipal, account.getLoginPwd(), getName());
    }
    
    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     *
     * @param principals
     * @return {@link AuthenticationInfo}
     * @throws AuthenticationException
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        return new SimpleAuthorizationInfo();
    }
    
    /**
     * 清理其它用户，保障一个帐户同时只能登陆一个
     * @param accountId
     */
    void cleanOtherUsers(Long accountId)
    {
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        CustomSessionManager sessionManager = (CustomSessionManager) securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        for (Session session : sessions)
        {
            if (accountId.equals(Long.valueOf(String.valueOf(session.getAttribute(PRINCIPALS_ID)))))
            { // 清除该用户以前登录时保存的session
                sessionManager.getSessionDAO().delete(session);
            }
        }
    }
    
    public void setAccountService(AccountService accountService)
    {
        this.accountService = accountService;
    }
    
    public void setAccountPolicyService(AccountPolicyService accountPolicyService)
    {
        this.accountPolicyService = accountPolicyService;
    }
}
