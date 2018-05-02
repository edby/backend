/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import java.io.UnsupportedEncodingException;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;

import io.swagger.annotations.ApiModelProperty;

/**
 * 账户表 实体对象
 * <p>File：Account.java</p>
 * <p>Title: Account</p>
 * <p>Description:Account</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class Account extends SignableEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不可为空")
    @ApiModelProperty(value = "用户编号")
    private Long              unid;

    /**账户名*/
    @NotNull(message = "账户名不可为空")
    @ApiModelProperty(value = "账户名")
    private String            accountName;

    /**登录密码*/
    @JSONField(serialize = false)
    @NotNull(message = "登录密码不可为空")
    @ApiModelProperty(value = "登录密码")
    private String            loginPwd;

    /**资金密码*/
    @JSONField(serialize = false)
    @ApiModelProperty(value = "资金密码")
    private String            walletPwd;

    /**邮箱*/
    @NotNull(message = "邮箱不可为空")
    @ApiModelProperty(value = "邮箱")
    private String            email;

    /**邮箱验证码*/
    @ApiModelProperty(value = "邮箱验证码")
    private String            code;

    /**国家地区(国家地区数据字典)*/
    @ApiModelProperty(value = "国家地区")
    private String            country;

    /**手机号*/
    @ApiModelProperty(value = "手机号")
    private String            mobNo;

    /**google验证器私钥*/
    @ApiModelProperty(value = "google验证器私钥")
    private String            authKey;

    /**状态(0:正常、1:冻结、2:注销)*/
    @NotNull(message = "状态不可为空")
    @ApiModelProperty(value = "状态(0:正常、1:冻结、2:注销)")
    private Integer           status;

    /**解冻时间*/
    @ApiModelProperty(value = "解冻时间")
    private Long              thawTime;

    @ApiModelProperty(value = "注册所在地或IP")
    private String            location;

    /**备注*/
    @ApiModelProperty(value = "备注")
    private String            remark;

    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间")
    private Long              createDate;

    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private Long              updateDate;

    /**语言标识*/
    @ApiModelProperty(value = "语言标识")
    private String            lang;

    /**交易验证策略*/
    @ApiModelProperty(value = "交易验证策略")
    private Integer           tradePolicy;

    /**安全验证策略*/
    @ApiModelProperty(value = "安全验证策略")
    private Integer           securityPolicy;

    /**自动借贷默认策略不开启0开启1*/
    @ApiModelProperty(value = "自动借贷策略默认不开启0开启1")
    private Integer           autoDebit;

    /**邀请码(UNID)*/
    @ApiModelProperty(value = "邀请码(UNID)")
    private Long              invitCode;

    /** 是否已奖励糖果 0未奖励 1已奖励 */
    @ApiModelProperty(value = "是否已奖励糖果")
    private Integer           chargeAward = 0;

    // 操作ID
    private String            oid;

    public Account()
    {
    }

    public Account(String email)
    {
        this.email = email;
    }

    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.unid);
        signValue.append(this.accountName).append(this.authKey);
        signValue.append(this.loginPwd).append(this.walletPwd).append(this.status);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }

    public Long getUnid()
    {
        return unid;
    }

    public void setUnid(Long unid)
    {
        this.unid = unid;
    }

    public String getAccountName()
    {
        return this.accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getLoginPwd()
    {
        return this.loginPwd;
    }

    public void setLoginPwd(String loginPwd)
    {
        this.loginPwd = loginPwd;
    }

    public String getWalletPwd()
    {
        return this.walletPwd;
    }

    public void setWalletPwd(String walletPwd)
    {
        this.walletPwd = walletPwd;
    }

    public String getEmail()
    {
        return this.email == null ? "" : this.email.toLowerCase();
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCountry()
    {
        return this.country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getMobNo()
    {
        return this.mobNo;
    }

    public void setMobNo(String mobNo)
    {
        this.mobNo = mobNo;
    }

    public String getAuthKey()
    {
        return authKey;
    }

    public void setAuthKey(String authKey)
    {
        this.authKey = authKey;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Long getThawTime()
    {
        return thawTime;
    }

    public void setThawTime(Long thawTime)
    {
        this.thawTime = thawTime;
    }

    public Long getCreateDate()
    {
        return this.createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }

    public Long getUpdateDate()
    {
        return this.updateDate;
    }

    public void setUpdateDate(Long updateDate)
    {
        this.updateDate = updateDate;
    }

    public String getLang()
    {
        return this.lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public Long getInvitCode()
    {
        return invitCode;
    }

    public void setInvitCode(Long invitCode)
    {
        this.invitCode = invitCode;
    }

    public String getOid()
    {
        return oid;
    }

    public void setOid(String oid)
    {
        this.oid = oid;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getTradePolicy()
    {
        return tradePolicy;
    }

    public void setTradePolicy(Integer tradePolicy)
    {
        this.tradePolicy = tradePolicy;
    }

    public Integer getSecurityPolicy()
    {
        return securityPolicy;
    }

    public void setSecurityPolicy(Integer securityPolicy)
    {
        this.securityPolicy = securityPolicy;
    }

    public Integer getAutoDebit()
    {
        return autoDebit;
    }

    public void setAutoDebit(Integer autoDebit)
    {
        this.autoDebit = autoDebit;
    }

    public Integer getChargeAward()
    {
        return chargeAward;
    }

    public void setChargeAward(Integer chargeAward)
    {
        this.chargeAward = chargeAward;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id=").append(id);
        sb.append(", sign='").append(sign).append('\'');
        sb.append(", unid=").append(unid);
        sb.append(", randomKey='").append(randomKey).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", loginPwd='").append(loginPwd).append('\'');
        sb.append(", walletPwd='").append(walletPwd).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", mobNo='").append(mobNo).append('\'');
        sb.append(", authKey='").append(authKey).append('\'');
        sb.append(", status=").append(status);
        sb.append(", thawTime=").append(thawTime);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", lang='").append(lang).append('\'');
        sb.append(", tradePolicy=").append(tradePolicy);
        sb.append(", securityPolicy=").append(securityPolicy);
        sb.append(", autoDebit=").append(autoDebit);
        sb.append(", invitCode=").append(invitCode);
        sb.append(", oid=").append(oid);
        sb.append('}');
        return sb.toString();
    }
}
