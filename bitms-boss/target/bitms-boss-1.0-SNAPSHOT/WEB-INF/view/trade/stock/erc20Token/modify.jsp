<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editErc20TokenForm').form({
            url : '${ctx}/stock/erc20Token/save',
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
                    $('#editErc20TokenForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        $("#tokenIsActiveTd").html(dictDropDownOptions('isActive', 'isActive', 'yesOrNo', '${erc20Token.isActive eq null ?'no':erc20Token.isActive}', 'required:true,readonly:true,', 'width:374px'));
        $("#isActive").combobox({
            valueField: 'code',
            textField: 'name'
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editErc20TokenForm" method="post">
            <table class="grid">
                <tr>
                    <td>符号</td>
                    <td>
                        <input name="id" type="hidden"  value="${erc20Token.id}">
                       <input name="symbol" type="text" placeholder="请输入符号" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                            value="${erc20Token.symbol}">
                    </td>
                </tr>
                <tr>
                    <td>全称</td>
                    <td>
                        <input name="symbolName" type="text" placeholder="请输入全称" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               value="${erc20Token.symbolName}">
                    </td>
                </tr>
                <tr>
                    <td>平台交易对</td>
                    <td>
                        <input name="pair" type="text" placeholder="请输入平台交易对" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               value="${erc20Token.pair}">
                    </td>
                </tr>
                <tr>
                    <td>激活状态</td>
                    <td id="tokenIsActiveTd">

                    </td>
                </tr>
                <tr>
                    <td>合约地址</td>
                    <td><input name="contractAddr" type="text" placeholder="请输入合约地址" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                                 value="${erc20Token.contractAddr}"></td>
                </tr>
                <tr>
                    <td>精度</td>
                    <td><input name="tokenDecimals" type="text" placeholder="请输入精度" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,min:0,max:100" value="${erc20Token.tokenDecimals}"></td>
                </tr>
                <tr>
                    <td>发行量</td>
                    <td><input name="totalSupply" type="text" placeholder="请输入发行量" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,min:0,max:100000000000" value="${erc20Token.totalSupply}"></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>