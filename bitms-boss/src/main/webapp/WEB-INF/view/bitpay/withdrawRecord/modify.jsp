<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editWithdrawRecordForm').form({
            url : '${ctx}/stock/info/save',
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
                    $('#editStockInfoForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
        
        $("#stockTypeTd").html(dictDropDownOptions('stockType','stockType', 'stockType','${stockInfo.stockType}', 'required:true,', 'width:142px'));
    	$("#stockType").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
        
        $("#canRechargeTd").html(dictDropDownOptions('canRecharge','canRecharge', 'yesOrNo','${stockInfo.canRecharge}', 'required:true,', 'width:142px'));
    	$("#canRecharge").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	
    	$("#canWithdrawTd").html(dictDropDownOptions('canWithdraw','canWithdraw', 'yesOrNo','${stockInfo.canWithdraw}', 'required:true,', 'width:142px'));
    	$("#canWithdraw").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});

    	$("#canTradeTd").html(dictDropDownOptions('canTrade','canTrade', 'yesOrNo','${stockInfo.canTrade}', 'required:true,', 'width:142px'));
    	$("#canTrade").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editWithdrawRecordForm" method="post">
            <table class="grid">
                <tr>
                    <td>证券代码</td>
                    <td colspan="3">
                        <input name="id" type="hidden"  value="${stockInfo.id}">
                        <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                           data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                    </td>
                </tr>
                <tr>
                    <td>证券名称</td>
                    <td colspan="3"><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}"></td>
                </tr>
                <tr>
                    <td>证券类型</td>
                    <td id="stockTypeTd">
                    </td>
                    <td>能否充值</td>
                    <td id="canRechargeTd">
                    </td>
                </tr>
                 <tr>
                    <td>是否提现</td>
                    <td id="canWithdrawTd">
                    </td>
                    <td>是否交易</td>
                    <td id="canTradeTd">
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                        <textarea name="remark" style="width: 374px;height: 60px;">${stockInfo.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>