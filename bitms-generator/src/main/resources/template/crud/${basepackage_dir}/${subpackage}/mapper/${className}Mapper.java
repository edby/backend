<#include "/macro.include"/>
<#include "/copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.${subpackage}.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import ${basepackage}.${subpackage}.entity.${className};

/**
 * ${table.sqlRemark} 持久层接口
 * <p>File：${className}Mapper.java </p>
 * <p>Title: ${className}Mapper </p>
 * <p>Description:${className}Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface ${className}Mapper extends GenericMapper<${className}>
{

}
