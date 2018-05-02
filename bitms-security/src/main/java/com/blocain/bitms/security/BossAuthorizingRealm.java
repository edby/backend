package com.blocain.bitms.security;

import java.util.List;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.blocain.bitms.boss.system.entity.Resources;
import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.ResourcesService;
import com.blocain.bitms.boss.system.service.RoleInfoService;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.exception.AccountPolicyException;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：BossAuthorizingRealm.java </p>
 * <p>Title: 系统安全认证实现类 </p>
 * <p>Description: BossAuthorizingRealm </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 15:42</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class BossAuthorizingRealm extends AuthorizingRealm
{
    private UserInfoService  userInfoService;
    
    private RoleInfoService  roleInfoService;
    
    private ResourcesService resourcesService;
    
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
        if (StringUtils.isBlank(token.getUsername())) throw new UnknownAccountException("用户不存在！");
        UserInfo userInfo = userInfoService.findByUserName(token.getUsername());
        if (null == userInfo) throw new UnknownAccountException("用户不存在！");
        if (!EncryptUtils.validatePassword(String.valueOf(token.getPassword()), userInfo.getPassWord()))
        {// 验证帐户密码
            throw new AuthenticationException("密码错误！");
        }
        if (StringUtils.isNotBlank(userInfo.getAuthKey()))
        {// 绑定过GA的用户强制进行GA码校验
            if (!validGaCode(userInfo.getAuthKey(), token.getGaCode()))
            {// GA验证不通过后直接执行
                token.setId(userInfo.getId());
                throw new AccountPolicyException("policy valid error!");
            }
        }
        List<RoleInfo> roles = roleInfoService.findByUserId(userInfo.getId());
        if (ListUtils.isNotNull(roles))
        {
            for (RoleInfo role : roles)
            {
                List<Resources> resources = resourcesService.findByRoleId(role.getId());
                role.setResources(resources);
            }
        }
        UserPrincipal userPrincipal = new UserPrincipal(userInfo.getId(), userInfo.getUserName(), userInfo.getTrueName(), roles);
        return new SimpleAuthenticationInfo(userPrincipal, userInfo.getPassWord(), getName());
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
        UserPrincipal principal = (UserPrincipal) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<RoleInfo> roles = principal.getRoles();
        if (ListUtils.isNotNull(roles))
        {
            for (RoleInfo role : roles)
            {
                authorizationInfo.addRole(role.getRoleCode());
                List<Resources> resources = role.getResources();
                if (ListUtils.isNotNull(roles))
                {
                    for (Resources res : resources)
                    {
                        authorizationInfo.addStringPermission(res.getResCode());
                    }
                }
            }
        }
        return authorizationInfo;
    }
    
    /**
     * 验证GA码
     * @param authKey
     * @param validCode
     * @return
     */
    protected boolean validGaCode(String authKey, String validCode)
    {
        boolean flag = false;
        if (StringUtils.isBlank(authKey) || StringUtils.isBlank(validCode)) return flag;
        Authenticator authenticator = new Authenticator();
        if (authenticator.checkCode(EncryptUtils.desDecrypt(authKey), Long.valueOf(validCode))) flag = true;
        return flag;
    }
    
    public void setUserInfoService(UserInfoService userInfoService)
    {
        this.userInfoService = userInfoService;
    }
    
    public void setRoleInfoService(RoleInfoService roleInfoService)
    {
        this.roleInfoService = roleInfoService;
    }
    
    public void setResourcesService(ResourcesService resourcesService)
    {
        this.resourcesService = resourcesService;
    }
}
