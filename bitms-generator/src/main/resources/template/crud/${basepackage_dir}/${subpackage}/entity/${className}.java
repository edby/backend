<#include "/macro.include"/>
<#include "/copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.${subpackage}.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity ;
import javax.validation.constraints.NotNull;
/**
 * ${table.sqlRemark} 实体对象
 * <p>File：${className}.java</p>
 * <p>Title: ${className}</p>
 * <p>Description:${className}</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "${table.sqlRemark}")
public class ${className} extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	<@generateFields/>
	
	<@generateGetSetMethod/>
}

<#macro generateFields>
<#if table.compositeId>
	private ${className}Id id;
	<#list table.columns as column>
    <#if !column.pk >
	private ${column.javaType} ${column.columnNameLower};
		</#if>
	</#list>
<#else>
	<#list table.columns as column>
	<#if !column.pk && !column.fk && column.columnNameLower != "sign" && column.columnNameLower != "randomKey" && column.columnNameLower != "delFlag">
	/**${column.remark}*/
	<#if !column.nullable>
	@NotNull(message = "${column.remark}不可为空")
	</#if>
	@ApiModelProperty(value = "${column.remark}"<#if !column.nullable>, required = true</#if>)
	private ${column.javaType} ${column.columnNameLower};
	</#if>
	</#list>
</#if>
</#macro>

<#macro generateGetSetMethod>
	<#list table.columns as column>
	<#if !column.pk && !column.fk && column.columnNameLower != "sign" && column.columnNameLower != "randomKey" && column.columnNameLower != "delFlag">
	public ${column.javaType} get${column.columnName}()
	{
		return this.${column.columnNameLower};
	}
	
	public void set${column.columnName}(${column.javaType} ${column.columnNameLower})
	{
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	
		</#if>
	</#list>
</#macro>