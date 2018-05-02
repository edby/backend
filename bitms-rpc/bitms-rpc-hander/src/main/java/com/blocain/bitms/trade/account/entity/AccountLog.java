/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * 账户日志表 实体对象
 * <p>File：AccountLog.java</p>
 * <p>Title: AccountLog</p>
 * <p>Description:AccountLog</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Document(collection = "AccountLog")
@ApiModel(description = "帐户操作日志")
public class AccountLog extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**系统名称*/
    private String            systemName;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long              accountId;
    
    /**账户名称*/
    @NotNull(message = "账户名称不可为空")
    private String            accountName;
    
    /**操作类型 login:登陆，setting:安全设置，default:默认*/
    private String            opType;
    
    /**IP地址*/
    @NotNull(message = "IP地址不可为空")
    private String            ipAddr;
    
    /**区域名称*/
    private String            rigonName;
    
    /**URL地址*/
    private String            url;
    
    /**内容*/
    private String            content;
    
    /**备注*/
    private String            remark;
    
    /**创建时间*/
    private Long              createDate;
    
    /**用户界面传值 查询开始时间 */
    private Long              timeStart;
    
    /**用户界面传值 查询结束时间 */
    private Long              timeEnd;
    
    public AccountLog()
    {
    }
    
    public AccountLog(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getSystemName()
    {
        return systemName;
    }
    
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getIpAddr()
    {
        return this.ipAddr;
    }
    
    public void setIpAddr(String ipAddr)
    {
        this.ipAddr = ipAddr;
    }
    
    public String getOpType()
    {
        return opType;
    }
    
    public void setOpType(String opType)
    {
        this.opType = opType;
    }
    
    public String getRigonName()
    {
        return rigonName;
    }
    
    public void setRigonName(String rigonName)
    {
        this.rigonName = rigonName;
    }
    
    public String getUrl()
    {
        return this.url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getContent()
    {
        return this.content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public Long getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(Long timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public Long getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(Long timeEnd)
    {
        this.timeEnd = timeEnd;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountLog{");
        sb.append("id=").append(id);
        sb.append(", systemName='").append(systemName).append('\'');
        sb.append(", accountId=").append(accountId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", opType='").append(opType).append('\'');
        sb.append(", ipAddr='").append(ipAddr).append('\'');
        sb.append(", rigonName='").append(rigonName).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", timeStart=").append(timeStart);
        sb.append(", timeEnd=").append(timeEnd);
        sb.append('}');
        return sb.toString();
    }
}
