<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editSystemWalletForm').form({
            url : '${ctx}/fund/systemwallet/save',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                if (isValid) {progressLoad();}
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
                    $('#editSystemWalletForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
        $("#walletUsageTypeTd").html(dictDropDownOptions('walletUsageType','walletUsageType', 'walletUsageType','${systemWallet.walletUsageType}', 'required:true,', 'width:305px'));
    	$("#walletUsageType").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editSystemWalletForm" method="post">
            <table class="grid">
                <tr>
                   <tr>
                    <td>证券代码</td>
                    <td>
                    	<input name="id" type="hidden" value="${systemWallet.id}">
                        <input  id="stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 305px;"  
                        placeholder="请选择证券"  value="${systemWallet.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockName', groupField:'group',required:true"  >
                </tr>
                </tr>
                <tr>
                    <td>钱包ID</td>
                    <td><input name="walletId" type="text" style="width: 300px;" placeholder="请输入钱包ID" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,64]']" value="${systemWallet.walletId}"></td>
                </tr>
                <tr>
                    <td>钱包名称</td>
                    <td><input name="walletName" type="text" style="width: 300px;" placeholder="请输入钱包名称" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,32]']" value="${systemWallet.walletName}"></td>
                </tr>
                <tr>
                    <td>钱包用途</td>
                    <td id="walletUsageTypeTd"> 
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input name="remark" type="text" style="width: 300px;" placeholder="请输入备注" class="easyui-validatebox easyui-textbox"
                           data-options="validType:['length[0,32]']"     value="${systemWallet.remark}"></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>