<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    $(function() {
        $('#walletCashTransferCurrentForm').form({
            url : '${ctx}/fund/walletCashTransferCurrent/save',
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
                    $('#walletCashTransferCurrentForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="walletCashTransferCurrentForm" method="post">
            <table class="grid">
                <tr>
                    <td>交易时间</td>
                    <td colspan="3">
                        <jsp:useBean id="now" class="java.util.Date" scope="page"/>
                        <input class="easyui-datetimebox" name="currentDateStr"  style="width:500px;"
                               data-options="required:true" value="" style="width:150px">
                    </td>
                </tr>
                <tr>
                    <td>证券信息</td>
                    <td colspan="3">
                        <select name="stockinfoId" class="easyui-combobox"  style="width:500px;" data-options="required:true"  >
                             <option selected value="<%=FundConsts.WALLET_EUR_TYPE %>">EUR</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>发生方向</td>
                    <td colspan="3">
                        <select id="occurDirect" name="occurDirect" class="easyui-combobox"  style="width:500px;" data-options="required:true"  >
                            <option selected value="<%=FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE %>">增加</option>
                            <option  value="<%=FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE %>">减少</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>交易ID</td>
                    <td colspan="3"><input name="transId" type="text" placeholder="请输入交易ID" class="easyui-validatebox easyui-textbox" style="width: 500px;"
                               data-options="required:true,validType:['length[0,50]']" value=""></td>
                </tr>
                <tr>
                    <td>资产发生数量</td>
                    <td><input name="occurAmt" type="text" placeholder="请输入资产发生数量" class="easyui-numberbox" style="width: 500px;"
                               data-options="required:true,min:0.000001,precision:6" value=""></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                        <textarea name="remark" style="width: 500px;height: 60px;"></textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>