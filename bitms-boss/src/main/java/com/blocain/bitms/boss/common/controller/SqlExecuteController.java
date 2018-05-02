package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.boss.common.service.SqlScriptsExecuteService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前端sql查询服务控制器
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
public class SqlExecuteController extends GenericController
{
    @Autowired(required = false)
    SqlScriptsExecuteService sqlScriptsExecuteManager;
    
    /**
     * 页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/query")
    @RequiresPermissions("monitor:setting:query:index")
    public String index() throws BusinessException
    {
        return "boss/common/sql/sql_query";
    }
    
    /**
     * 执行查询语句，返回查询结果
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("monitor:setting:query:data")
    @RequestMapping(value = "/query/data", method = RequestMethod.POST)
    public JsonMessage data(String sql, String offset, String length) throws BusinessException
    {
        String newSql = sql.trim().toLowerCase();
        if ((!newSql.startsWith("select")) || newSql.contains("delete") || newSql.contains("insert")
                || newSql.contains("update")) { return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR, "sql语句不合法"); }
        String result = "";
        if (offset != null && !offset.equals("") && length != null && !length.equals(""))
        {
            result = sqlScriptsExecuteManager.exportHtml(sql, Integer.valueOf(offset).intValue(), Integer.valueOf(length).intValue());
        }
        else
        {
            result = sqlScriptsExecuteManager.exportHtml(sql);
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
