<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editChgPwdForm').form({
            url : '${ctx}/bitpay/bitpayKeychain/chagePwd/update',
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
                    parent.$.messager.alert('操作成功', result.message, 'info');
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editChgPwdForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editChgPwdForm" method="post">
        	<input name="id" type="hidden"  value="${keychain.id}">
        	<input name="type" type="hidden"  value="${keychain.type}">
            <table class="grid">
                <tr>
                    <td>旧密码<font color='red'>*</font></td>
                    <td colspan="3"><input name="oldPass" type="password" placeholder="请输入旧密码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                               data-options="required:true,validType:['length[0,20]']" value=""></td>
                </tr>
                <tr>
                    <td>新密码<font color='red'>*</font></td>
                    <td colspan="3"><input name="newPass" type="password" placeholder="请输入新密码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                               data-options="required:true,validType:['length[8,20]']" value=""></td>
                </tr>
                <tr>
                    <td>确认密码<font color='red'>*</font></td>
                    <td colspan="3"><input name="newPass2" type="password" placeholder="请输入确认密码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                               data-options="required:true,validType:['length[8,20]']"  value=""></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>