<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#marginForm').form({
            url : '${ctx}/monitor/margin/approval/confirm',
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
                    $('#marginForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
    });
    $(document).ready(function(){
        $("#marginForm").parent().css("overflow-y","scroll");
    });
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="marginForm" method="post">
            <input type="hidden" id="ids" name="ids" value="${ids}" />
            <input type="hidden" id="targetStockinfoIds" name="targetStockinfoIds" value="${targetStockinfoIds}" />
            <input type="hidden" id="capitalStockinfoIds" name="capitalStockinfoIds" value="${capitalStockinfoIds}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><font color="red"><h3>请认真核对要强制平仓的数据，您确定要对以下记录进行强制平仓处理吗？
                        <br />确定强制平仓请点击右下角的【确定】按钮，如发现问题请点击【取消】按钮！</h3></font></td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <table  class="table"  cellspacing="0" cellpadding="0"width="100%">
                            <thead>
                            <tr>
                                <th>借款人账户id</th>
                                <th>借款人帐号</th>
                                <th>保证金实时比例%</th>
                                <th>风险率%</th>
                                <th>数字货币证券ID</th>
                                <th>数字货币余额</th>
                                <th>数字货币借款</th>
                                <th>法定货币证券ID</th>
                                <th>法定货币余额</th>
                                <th>法定货币借款</th>
                                <th>最新行情</th>
                            </tr>
                            </thead>
                            <tbody>
                           <c:if test="${!empty list}">
                                <c:forEach var="item" items="${list }"  varStatus="status" >
                                    <tr class="test">
                                        <td>${item.accountId} </td>
                                        <td>${item.accountName}</td>
                                        <td>${item.marginratio}</td>
                                        <td>${item.riskRate}</td>
                                        <td>${item.capitalStockinfoId}</td>
                                        <td>${item.capitalBal}</td>
                                        <td>${item.capitalDebtBal}</td>
                                        <td>${item.targetStockinfoId}</td>
                                        <td>${item.targetBal}</td>
                                        <td>${item.targetDebtBal}</td>
                                        <td>${item.platPrice}</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>