<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#resourceEditPid').combotree({
            url : '${ctx}/system/resource/tree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto',
            value :'${resource.parentId}'
        });
        
        $('#editResForm').form({
            url : '${ctx}/system/resource/save',
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
                    parent.$.modalDialog.openner_treeGrid.treegrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                	$('#editResForm').find('input[name="csrf"]').val(result.csrf);
                    parent.$.messager.alert('提示', result.message, 'warning');
                }
                  
            }
        });
        
    });
</script>
<div style="padding: 3px;">
    <form:form id="editResForm" method="post">
        <table class="grid">
            <tr>
                <td>编码</td>
                <td>
                    <input name="id" type="hidden"  value="${resource.id}">
                    <input name="resCode" type="text" value="${resource.resCode}" placeholder="请输入菜单编码"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"/>
                </td>
                <td>名称</td>
                <td>
                    <input name="resName" type="text" value="${resource.resName}" placeholder="请输入菜单名称"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,20]']" >
                </td>
            </tr>
            <tr>
                <td>地址</td>
                <td colspan="3">
                    <input name="resUrl" type="text" style="width: 360px;" value="${resource.resUrl}" placeholder="请输入菜单编码"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,100]']"/>
                </td>
            </tr>
            <tr>
                <td>上级资源</td>
                <td colspan="3">
                    <select id="resourceEditPid" name="parentId" style="width: 360px; height: 29px;"></select>
                    <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a>
                </td>
            </tr>
            <tr>
                <td>图标</td>
                <td>
                    <input name="icon" type="text" value="${resource.icon}" >
                </td>
                <td>类型</td>
                <td>
                    <input name="type" type="text" value="${resource.type}" >
                </td>
            </tr>

            <tr>
                <td>资源描述</td>
                <td colspan="3">
                    <textarea name="resDest" style="width: 360px;height: 100px;">${resource.resDest}</textarea>
                </td>
            </tr>
            <tr>
                <td>排序号</td>
                <td colspan="3">
                    <input name="sortNum" type="text" value="${resource.sortNum}" >
                </td>
            </tr>
        </table>
    </form:form>
</div>
