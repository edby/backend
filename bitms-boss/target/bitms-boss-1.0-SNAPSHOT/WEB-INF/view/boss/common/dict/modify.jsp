<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#dictEditPid').combotree({
            url : '${ctx}/common/dict/tree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto',
            value :'${dict.parentId}'
        });
        
        $('#editDictForm').form({
            url : '${ctx}/common/dict/save',
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
                    parent.$.messager.alert('提示', result.message, 'warning');
                    $('#editDictForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        $("#userActive").val('${dict.active}');
    });
</script>
<div style="padding: 3px;">
    <form:form id="editDictForm" method="post">
        <table class="grid">
            <tr>
                <td>编号</td>
                <td>
                    <input name="id" type="hidden"  value="${dict.id}">
                    <input name="code" type="text" value="${dict.code}" placeholder="请输入字典编码"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']"/>
                </td>
            </tr>
            <tr>
                <td>名称</td>
                <td><input name="name" type="text" value="${dict.name}" placeholder="请输入字典名称"
                           class="easyui-validatebox easyui-textbox" data-options="required:true,validType:['length[0,50]']" >
                </td>
            </tr>
            <tr>
                <td>语言</td>
                <td>
                    <select name="lang">
                         <option value="">请选择</option>
                          <option value="en_US" ${dict.lang eq 'en_US' ? "selected":""} >English</option>
                          <option value="zh_CN" ${dict.lang eq 'zh_CN' ? "selected":""}>简体中文</option>
                          <option value="zh_HK" ${dict.lang eq 'zh_HK' ? "selected":""}>繁体中文</option>
                    </select>
                </td>
            </tr>
            <td>状态</td>
            <td>
                <select id="userActive" name="active" class="easyui-combobox"
                        data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                    <option value="true">启用</option>
                    <option value="false">停用</option>
                </select>
            </td>
            <tr>
                <td>排序号</td>
                <td><input name="sortNum" class="easyui-numberbox" type="text" value="${dict.sortNum != null ? dict.sortNum : 0}" data-options="min:0,max:99" >
                </td>
            </tr>
            <tr>
                <td>上级资源</td>
                <td colspan="3">
                    <select id="dictEditPid" name="parentId" style="width: 200px; height: 29px;"></select>
                    <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a>
                </td>
            </tr>
            <tr>
                <td>资源描述</td>
                <td colspan="3">
                    <textarea name="dest" style="width: 300px;" data-options="validType:['length[0,100]']">${dict.dest}</textarea>
                </td>
            </tr>
        </table>
    </form:form>
</div>
