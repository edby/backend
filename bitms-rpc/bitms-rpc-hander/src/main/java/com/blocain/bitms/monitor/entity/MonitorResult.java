package com.blocain.bitms.monitor.entity;

import java.io.Serializable;

/**
 * <p>File：MonitorResult.java</p>
 * <p>Title: MonitorResult</p>
 * <p>Description:监控结果对象</p>
 * <p>Copyright: Copyright (c) 2015/04/21 11:52</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class MonitorResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Integer           returnCode;           // 存储过程执行返回代码 (成功为1，失败为-1)
    
    private Integer           chekResult;           // 对账结果 (账务平衡为1，账务不平为-1)
    
    private Long              accountId;            // 账户ID
    
    private String            monitorDesc;          // 监控结果描述
    
    public Integer getReturnCode()
    {
        return returnCode;
    }
    
    public void setReturnCode(Integer returnCode)
    {
        this.returnCode = returnCode;
    }
    
    public Integer getChekResult()
    {
        return chekResult;
    }
    
    public void setChekResult(Integer chekResult)
    {
        this.chekResult = chekResult;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getMonitorDesc()
    {
        return monitorDesc;
    }
    
    public void setMonitorDesc(String monitorDesc)
    {
        this.monitorDesc = monitorDesc;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("MonitorResult{");
        sb.append("returnCode=").append(returnCode);
        sb.append(", chekResult=").append(chekResult);
        sb.append(", accountId=").append(accountId);
        sb.append(", monitorDesc='").append(monitorDesc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
