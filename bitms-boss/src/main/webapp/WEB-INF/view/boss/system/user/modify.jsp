<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#userOrganizId').combotree({
            url : '${ctx}/system/organiz/tree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto',
            value : '${user.orgId}'
        });

        $('#userEditRoleIds').combotree({
            url : '${ctx}/system/role/tree',
            parentField : 'id',
            lines : true,
            panelHeight : 'auto',
            multiple : true,
            required : true,
            cascadeCheck : false,
            value : '${roleIds}'
        });

        $('#editUserForm').form({
            url : '${ctx}/system/user/save',
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
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editUserForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
        $("#userGender").val('${user.gender}');
        $("#userActive").val('${user.active}');
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editUserForm" method="post">
            <div class="light-info" style="overflow: hidden;padding: 3px;">
                <div>账户修改密码不可逆，编辑时需要重新输入密码。</div>
            </div>
            <table class="grid">
                <tr>
                    <td>登录名</td>
                    <td>
                        <input name="id" type="hidden"  value="${user.id}">
                        <input name="userName" type="text" placeholder="请输入登录名称" class="easyui-validatebox easyui-textbox"
                           data-options="required:true" value="${user.userName}">
                    </td>
                    <td>姓名</td>
                    <td><input name="trueName" type="text" placeholder="请输入姓名" class="easyui-validatebox easyui-textbox"
                               data-options="required:true" value="${user.trueName}"></td>
                </tr>
                <tr>
                    <td>部门</td>
                    <td>
                        <select id="userOrganizId" name="orgId" style="width: 140px; height: 29px;"
                                class="easyui-validatebox easyui-textbox" data-options="required:true"></select>
                    </td>
                    <td>角色</td>
                    <td>
                        <input  id="userEditRoleIds" name="roleIds" style="width: 140px; height: 29px;"/>
                    </td>
                </tr>
                <tr>
                    <td>密码</td>
                    <td><input type="text" name="passWord"  class="easyui-validatebox easyui-textbox" data-options=""/></td>
                    <td>性别</td>
                    <td>
                        <select id="userGender" name="gender" class="easyui-combobox"
                            data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="true">男</option>
                            <option value="false">女</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>身份证</td>
                    <td><input type="text" name="idCard" value="${user.idCard}"/></td>
                    <td>状态</td>
                    <td>
                        <select id="userActive" name="active" class="easyui-combobox"
                                data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="true">启用</option>
                            <option value="false">停用</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>描述</td>
                    <td colspan="3">
                        <textarea name="userDest" style="width: 374px;height: 135px;">${user.userDest}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>