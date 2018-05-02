package com.blocain.bitms.boss.common.service;

import java.util.List;
import java.util.Map;

public interface SqlScriptsExecuteService
{
    void execute(String sql);
    
    List<Map<String, Object>> executeQuery(String sql, int ... pageset);
    
    String exportHtml(String sql);
    
    String exportHtml(String sql, int offset, int length);
    
    /**
     *
     * @return excel文件字节流
     */
    byte[] exportExcel(String sql);
    
    /**
     *
     * @return excel文件字节流
     */
    byte[] exportExcel(String sql, int offset, int length);
}
