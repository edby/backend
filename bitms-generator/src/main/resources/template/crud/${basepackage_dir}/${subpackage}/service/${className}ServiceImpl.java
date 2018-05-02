<#include "/macro.include"/>
<#include "/copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.${subpackage}.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import ${basepackage}.${subpackage}.entity.${className};
import ${basepackage}.${subpackage}.mapper.${className}Mapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ${table.sqlRemark} 服务实现类
 * <p>File：${className}ServiceImpl.java </p>
 * <p>Title: ${className}ServiceImpl </p>
 * <p>Description:${className}ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class ${className}ServiceImpl extends GenericServiceImpl<${className}> implements ${className}Service
{

    protected ${className}Mapper ${classNameLower}Mapper;

    @Autowired
    public ${className}ServiceImpl(${className}Mapper ${classNameLower}Mapper)
    {
        super(${classNameLower}Mapper);
        this.${classNameLower}Mapper = ${classNameLower}Mapper;
    }
}
