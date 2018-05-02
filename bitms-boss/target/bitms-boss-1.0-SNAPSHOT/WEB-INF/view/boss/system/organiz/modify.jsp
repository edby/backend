<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#organizEditPid').combotree({
            url : '${ctx}/system/organiz/tree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto',
            value :'${organiz.parentId}'
        });
        
        $('#editOrgForm').form({
            url : '${ctx}/system/organiz/save',
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
                    $('#editOrgForm').find('input[name="csrf"]').val(result.csrf);
                    parent.$.messager.alert('提示', result.message, 'warning');
                }
            }
        });
        
    });
</script>
<div style="padding: 3px;">
    <form:form id="editOrgForm" method="post">
        <table class="grid">
            <tr>
                <td>编号</td>
                <td>
                    <input name="id" type="hidden"  value="${organiz.id}">
                    <input name="orgCode" type="text" value="${organiz.orgCode}" placeholder="请输入机构编码"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"/>
                </td>
            </tr>
            <tr>
                <td>名称</td>
                <td><input name="orgName" type="text" value="${organiz.orgName}" placeholder="请输入机构名称"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']" >
                </td>
            </tr>
            <tr>
                <td>排序号</td>
                <td><input name="sortNum" class="easyui-numberbox" data-options="required:true,min:0,max:99" type="text" value="${organiz.sortNum != null ? organiz.sortNum : 0}" >
                </td>
            </tr>
            <tr>
                <td>上级资源</td>
                <td colspan="3">
                    <select id="organizEditPid" name="parentId" style="width: 200px; height: 29px;"></select>
                    <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a>
                </td>
            </tr>
            <tr>
                <td>资源描述</td>
                <td colspan="3">
                    <textarea name="orgDest" style="width: 300px;">${organiz.orgDest}</textarea>
                </td>
            </tr>
        </table>
    </form:form>
</div>
