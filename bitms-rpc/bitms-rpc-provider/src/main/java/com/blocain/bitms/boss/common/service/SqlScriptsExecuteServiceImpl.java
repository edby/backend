package com.blocain.bitms.boss.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlScriptsExecuteServiceImpl implements SqlScriptsExecuteService
{
    @Autowired
    private JdbcTemplate           jdbcTemplate;
    
    @Autowired
    private JdbcTemplateWithPaging jdbcTemplateWithPaging;
    
    public List<Map<String, Object>> executeQuery(String sql, int ... pageset)
    {
        if (pageset != null && pageset.length > 0) return jdbcTemplateWithPaging.queryPage(sql, pageset[0], pageset[1]);
        else return jdbcTemplate.queryForList(sql);
    }
    
    public void execute(String sql)
    {
        jdbcTemplate.execute(sql);
    }
    
    public String exportHtml(String sql, int offset, int length)
    {
        List<Map<String, Object>> queryResult = null;
        queryResult = executeQuery("select a.* from (" + sql + ") a ", offset, length);
        StringBuffer sbufer = new StringBuffer("<table cellspacing='0' > <caption>查询的SQL语句是: " + sql + "<br>共返回" + queryResult.size() + "条记录。</caption>");
        if (queryResult.size() > 0)
        {
            sbufer.append("<th>No</th>");
            Map<String, Object> rm = queryResult.get(0);
            for (String key : rm.keySet())
            {
                sbufer.append("<th>").append(key).append("</th>");
            }
            sbufer.append("</tr>");
            int i = 1;
            for (Map<String, Object> map : queryResult)
            {
                if (i % 2 == 0)
                {
                    sbufer.append("<tr><td class='alt' style='text-align: right' >" + i + "</td>");
                }
                else
                {
                    sbufer.append("<tr><td style='text-align: right'>" + i + "</td>");
                }
                Object cellContent = null;
                for (String key : map.keySet())
                {
                    cellContent = map.get(key);
                    if (cellContent != null && cellContent.toString().trim().equals(""))
                    {
                        cellContent = "-";
                    }
                    if (i % 2 == 0)
                    {
                        sbufer.append("<td class='alt'>").append(cellContent).append("</td>");
                    }
                    else
                    {
                        sbufer.append("<td>").append(cellContent).append("</td>");
                    }
                }
                i++;
                sbufer.append("</tr>");
            }
        }
        sbufer.append("</table>");
        return sbufer.toString();
    }
    
    public byte[] exportExcel(String sql, int ... pageset)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public byte[] exportExcel(String sql)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public byte[] exportExcel(String sql, int offset, int length)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String exportHtml(String sql)
    {
        List<Map<String, Object>> queryResult = null;
        queryResult = executeQuery(sql);
        StringBuffer sbufer = new StringBuffer("<table cellspacing='0' > <caption>查询的SQL语句是: " + sql + "<br>共返回" + queryResult.size() + "条记录。</caption>");
        if (queryResult.size() > 0)
        {
            sbufer.append("<th>No</th>");
            Map<String, Object> rm = queryResult.get(0);
            for (String key : rm.keySet())
            {
                sbufer.append("<th>").append(key).append("</th>");
            }
            sbufer.append("</tr>");
            int i = 1;
            for (Map<String, Object> map : queryResult)
            {
                if (i % 2 == 0)
                {
                    sbufer.append("<tr><td class='alt' style='text-align: right' >" + i + "</td>");
                }
                else
                {
                    sbufer.append("<tr><td style='text-align: right'>" + i + "</td>");
                }
                Object cellContent = null;
                for (String key : map.keySet())
                {
                    cellContent = map.get(key);
                    if (cellContent != null && cellContent.toString().trim().equals(""))
                    {
                        cellContent = "-";
                    }
                    if (i % 2 == 0)
                    {
                        sbufer.append("<td class='alt'>").append(cellContent).append("</td>");
                    }
                    else
                    {
                        sbufer.append("<td>").append(cellContent).append("</td>");
                    }
                }
                i++;
                sbufer.append("</tr>");
            }
        }
        sbufer.append("</table>");
        return sbufer.toString();
    }
}
