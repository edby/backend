<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    $(function() {
        $('#clearUserForm').form({
            url : '${ctx}/system/user/clearga',
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
                result = $.parseJSON(result);
                if (result.code == 200) {
                    parent.$.messager.alert('成功', result.message, 'info');
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#clearUserForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });

        $('#code').qrcode({
            width: 200, //宽度
            height:200, //高度
            text: '${gaMap.gaInfo}'});
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="clearUserForm" method="post">
            <table class="grid">
                <tr>
                    <td>登录名</td>
                    <td>
                        <input name="id" type="hidden"  value="${user.id}">
                        <input name="secretKey" type="hidden"  value="${gaMap.secretKey}">
                            ${user.userName}
                    </td>
                    <td>姓名</td>
                    <td>${user.trueName}</td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        GA验证码：<input name="gaCode" type="text" placeholder="请输入GA验证码" class="easyui-numberbox"
                               data-options="required:true,precision:0" value="">
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>