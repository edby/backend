<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editSystemWalletERC20DetailForm').form({
            url : '${ctx}/fund/systemwalleterc20/addr/save',
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
                    $('#editSystemWalletERC20DetailForm').find('input[name="csrf"]').val(result.csrf);

                }
            }
        });
        //$("#walletUsageType").val('${systemWalletERC20.walletUsageType}');

    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editSystemWalletERC20DetailForm" method="post">
            <table class="grid">
                <tr>
                <tr>
                    <td>证券信息</td>
                    <td>
                        <input name="id" type="hidden" value="${systemWalletAddrERC20.id}">
                        <input  id="stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language" style="width: 305px;"
                                placeholder="请选择证券"  value="${systemWalletAddrERC20.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockName', groupField:'group',required:true" readonly >*只读
                    </td>
                </tr>
                <tr>
                    <td>钱包ID</td>
                    <td><input name="walletId" type="text" style="width: 300px;" placeholder="请输入钱包ID" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,64]']" value="${systemWalletAddrERC20.walletId}" readonly>*只读</td>
                </tr>
                <tr>
                    <td>账户名</td>
                    <td>
                        <input  id="accountId" name="accountId"  class="easyui-combobox" name="language" style="width: 305px;"
                                placeholder="请选择账户名"  value="${systemWalletAddrERC20.accountId}" data-options="
								url: '${ctx}/account/account/all', method: 'get', valueField:'id',
								textField:'accountName', required:true"  >
                    </td>
                </tr>
                <tr>
                    <td>钱包地址</td>
                    <td><input name="walletAddr" type="text" style="width: 300px;" placeholder="请输入钱包地址" class="easyui-validatebox easyui-textbox"
                               data-options="required:true,validType:['length[0,65]']" value="${systemWalletAddrERC20.walletAddr}"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input name="remark" type="text" style="width: 300px;" placeholder="请输入备注" class="easyui-validatebox easyui-textbox"
                               data-options="validType:['length[0,32]']"    value="${systemWalletERC20.remark}"></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>