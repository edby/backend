package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 归档结果 实体类
 */
public class MonitorArchiveResult extends GenericEntity
{
    /**
     * 归档结果代码
     */
    private Integer archiveCode;
    
    /**
     * 归档结果描述
     */
    private String  archiveDesc;
    
    public Integer getArchiveCode()
    {
        return archiveCode;
    }
    
    public void setArchiveCode(Integer archiveCode)
    {
        this.archiveCode = archiveCode;
    }
    
    public String getArchiveDesc()
    {
        return archiveDesc;
    }
    
    public void setArchiveDesc(String archiveDesc)
    {
        this.archiveDesc = archiveDesc;
    }
    
    @Override
    public String toString()
    {
        return "MonitorArchiveResult{" + "archiveCode=" + archiveCode + ", archiveDesc='" + archiveDesc + '\'' + '}';
    }
}
