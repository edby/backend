<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editRoleForm').form({
            url : '${ctx}/system/role/save',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                if (isValid) {progressLoad();}
                return isValid;
            },
            success : function(result, textStatus, jqXHR) {
                progressClose();
                result = $.parseJSON(result);
                $('#editRoleForm').find('input[name="csrf"]').val(result.csrf);
                if (result.code == 200) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                }
            }
        });
        $("#needGa").val('${role.needGa}');
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editRoleForm" method="post">
            <table class="grid">
                <tr>
                    <td>角色编码</td>
                    <td><input name="id" type="hidden" value="${role.id}">
                        <input name="roleCode" type="text" style="width: 300px;" placeholder="请输入角色编码" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,20]']" value="${role.roleCode}"></td>
                </tr>
                <tr>
                    <td>角色名称</td>
                    <td><input name="roleName" type="text" style="width: 300px;" placeholder="请输入角色名称" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,25]']" value="${role.roleName}"></td>
                </tr>
                <tr>
                    <td>是否需要绑定GA</td>
                    <td>
                        <select id="needGa" class="easyui-combobox" name="needGa" style="width:305px;" value="${role.needGa}"  data-options="required:true">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>

                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                        <textarea name="roleDest" style="width: 300px;height: 100px;">${role.roleDest}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>