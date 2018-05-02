<#include "/macro.include"/>
<#include "/copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.${subpackage}.service;

import com.blocain.bitms.orm.core.GenericService;
import ${basepackage}.${subpackage}.entity.${className};
/**
 * ${table.sqlRemark} 服务接口
 * <p>File：${className}Service.java </p>
 * <p>Title: ${className}Service </p>
 * <p>Description:${className}Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface ${className}Service extends GenericService<${className}>{

}
