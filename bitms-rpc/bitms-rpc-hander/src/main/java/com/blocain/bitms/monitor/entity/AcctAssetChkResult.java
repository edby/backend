package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;

/***************************************************
 * 提现资产检查结果
 * <p>File：WithdrawChkResult.java</p>
 * <p>Title: WithdrawChkResult</p>
 * <p>Description:WithdrawChkResult</p>
 * <p>Copyright: Copyright (c) 2017年9月8日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class AcctAssetChkResult extends GenericEntity
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private String            chekMsg;              // 检查提示信息
    
    private Integer           returnCode;           // 存储过程执行返回代码 (成功为1，失败为-1)
    
    private Integer           chekResult;           // 对账结果 (账务平衡为1，账务不平为-1)
    
    public String getChekMsg()
    {
        return chekMsg;
    }
    
    public void setChekMsg(String chekMsg)
    {
        this.chekMsg = chekMsg;
    }
    
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
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AcctAssetChkResult{");
        sb.append("chekMsg='").append(chekMsg).append('\'');
        sb.append(", returnCode=").append(returnCode);
        sb.append(", chekResult=").append(chekResult);
        sb.append('}');
        return sb.toString();
    }
}
