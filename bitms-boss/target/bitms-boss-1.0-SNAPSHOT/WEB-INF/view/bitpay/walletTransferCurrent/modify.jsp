<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    $(function() {
        $('#walletTransferCurrentForm').form({
            url : '${ctx}/bitpay/walletTransferCurrent/save',
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
                    $('#walletTransferCurrentForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="walletTransferCurrentForm" method="post">
            <table class="grid">
                <tr>
                    <td>交易时间</td>
                    <td colspan="3">
                        <jsp:useBean id="now" class="java.util.Date" scope="page"/>
                        <input class="easyui-datetimebox" name="currentDateStr"  style="width:500px;"
                               data-options="required:true" value="<fmt:formatDate value="${!empty entity.currentDate?entity.currentDate:now}" pattern="yyyy-MM-dd HH:mm:ss"/> " style="width:150px">
                    </td>
                </tr>
                <tr>
                    <td>证券信息</td>
                    <td colspan="3">
                        <input  name="stockinfoId"  class="easyui-combobox" name="language" style="width: 500px;"
                                placeholder="请选择证券"  value="<%=FundConsts.WALLET_BTC_TYPE %>" data-options="
								url: '${ctx}/stock/info/allCanAdjustCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group',required:true"  >
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
                    <td>区块交易ID</td>
                    <td colspan="3"><input name="transId" type="text" placeholder="请输入区块交易ID" class="easyui-validatebox easyui-textbox" style="width: 500px;"
                               data-options="validType:['length[0,50]']" value="${entity.transId}"></td>
                </tr>
                <tr>
                    <td>提币目标地址</td>
                    <td colspan="3"><input name="withdrawAddr" type="text" placeholder="请输入提币目标地址" class="easyui-validatebox easyui-textbox" style="width: 500px;"
                               data-options="validType:['length[0,50]']" value="${entity.withdrawAddr}"></td>
                </tr>
                <tr>
                    <td>资产发生数量</td>
                    <td><input name="occurAmt" type="text" placeholder="请输入资产发生数量" class="easyui-numberbox" style="width: 164px;"
                               data-options="required:true,min:0.000001,precision:6" value="${entity.occurAmt}"></td>
                    <td>区块转账费用</td>
                    <td><input name="netFee" type="text" placeholder="请输入区块转账费用" class="easyui-numberbox" style="width: 164px;"
                               data-options="required:true,min:0.000001,precision:6" value="${entity.netFee}"></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                        <textarea name="remark" style="width: 500px;height: 60px;">${entity.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>