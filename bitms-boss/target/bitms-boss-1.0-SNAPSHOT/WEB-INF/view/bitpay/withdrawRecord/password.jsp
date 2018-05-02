<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function() {
        $('#passwordForm').form({
            <c:choose>
            <c:when test="${fn:indexOf(ids,',')!=-1}">
                url : '${ctx}/bitpay/withdrawRecord/confirm/batch',
            </c:when>
            <c:otherwise>
                url : '${ctx}/bitpay/withdrawRecord/confirm',
            </c:otherwise>
            </c:choose>
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success : function(result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#passwordForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="passwordForm" method="post">
        	<input name="id" type="hidden"  value="${ids}">
        	<input name="ids" type="hidden"  value="${ids}">
            <table class="grid">
                <tr>
                    <td>密码<font color='red'>*</font></td>
                    <td colspan="3"><input name="password" type="password" placeholder="请输入密码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                               data-options="required:true,validType:['length[8,20]']"  value=""></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>