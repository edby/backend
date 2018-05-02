<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function() {
        $('#editSysParameterForm').form({
            url : '${ctx}/system/params/save',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                <c:if test="${fn:contains(sysParameter.type,'多选')}">
	                var chk_value =[];
	                $('input[name="value"]:checked').each(function(){  
	                    chk_value.push($(this).val());
	                }); 
	                if(chk_value.length == 0){
	                	parent.$.messager.alert('错误', sys_parameter_modify_page_err, 'error');
	                	return false;
	                }
                </c:if>
                <c:if test="${fn:contains(sysParameter.type,'单选')}">
	                var key = $("input[name='value']:checked").val();
	                if(key == '' || key == null){
	                	parent.$.messager.alert('错误', sys_parameter_modify_page_err, 'error');
	                	return false;
	                }
            	</c:if>
                if (isValid) {
                	progressLoad();
                	}
                return isValid;
            },
            success : function(result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editSysParameterForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        <c:if test="${fn:contains(sysParameter.type,'多选')}">
        setCheckBoxValue();
		</c:if>
    });
    function setCheckBoxValue(){
    	var values='${sysParameter.value}';
    	var sel=values.split(";");
    	for(var i=0;i<sel.length;i++){  
            $("input[name='value']").each(function(){   
                if ($(this).val() == sel[i]) {  
                    this.checked=true;  
                }   
            });  
        }  
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editSysParameterForm" method="post">
            <table class="grid">
                <tr>
                    <td>系统名称</td>
                    <td>
                    	<input name="id" type="hidden" value="${sysParameter.id}">
                    	${sysParameter.systemName}
                    </td>
                </tr>
                <tr>
                    <td>参数名称</td>
                    <td>
                    	${sysParameter.parameterName}
                    </td>
                </tr>
                <tr>
                    <td>参数描述</td>
                    <td>
                    	${sysParameter.describe}
                    </td>
                </tr>
                <tr>
                    <td>参数值</td>
                    <td>
	                   <c:if test="${fn:contains(sysParameter.type,'文本')}">
						   	<input name="value" type="text" style="width: 300px;" placeholder="请输入参数值" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,64]']" value="${sysParameter.value}">
						</c:if>
						<c:if test="${fn:contains(sysParameter.type,'单选')}">
							<span>
								<c:forEach items="${fn:split(sysParameter.valueBound,';')}" var="val" >
									<input type="radio" name="value" value="${val}"
									<c:if test = "${val eq sysParameter.value}">checked</c:if>>${val}</input>
								</c:forEach>
				            </span>
						</c:if>
						
						<c:if test="${fn:contains(sysParameter.type,'多选')}">
						   <span>
								<c:forEach items="${fn:split(sysParameter.valueBound,';')}" var="val" >
									<input type="checkbox" name="value" value="${val}">${val}</input>
								</c:forEach>
				            </span>
						</c:if>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td >
                        <textarea name="remark" style="width: 300px;height: 60px;">${sysParameter.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>