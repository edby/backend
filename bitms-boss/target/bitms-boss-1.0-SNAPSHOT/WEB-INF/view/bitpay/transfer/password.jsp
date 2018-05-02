<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    $(function() {
        $('#passwordTransferForm').form({
            <c:choose>
            <c:when test="${fn:indexOf(ids,',')!=-1}">
                url : '${ctx}/bitpay/transfer/confirm/batch',
            </c:when>
            <c:otherwise>
                url : '${ctx}/bitpay/transfer/confirm',
            </c:otherwise>
            </c:choose>
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
                    $('#passwordTransferForm').find('input[name="csrf"]').val(result.csrf);
                    
                }
            }
        });
    });
    $("#passwordTransferForm").parent().css("overflow-y","scroll");
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="passwordTransferForm" method="post">
        	<input name="id" type="hidden"  value="${ids}">
        	<input name="ids" type="hidden"  value="${ids}">
            <table class="grid">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>账户</th>
                        <th>提现地址</th>
                        <th>金额</th>
                        <th>手续费</th>
                        <th>审核时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${!empty list}">
                        <c:forEach var="item" items="${list }" varStatus="status" >
                                    <tr class="test">
                                        <td>
                                                ${status.index + 1}
                                        </td>
                                        <td>
                                                ${item.accountName}
                                        </td>
                                        <td>${item.targetWalletAddr}</td>
                                        <td>
                                            <fmt:formatNumber value="${item.transferAmt}" pattern="#,##0.00000000"  />
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${item.transferFee}" pattern="#,##0.00000000"  />
                                        </td>
                                        <td>${item.transferTime}</td>
                                    </tr>

                        </c:forEach>
                    </c:if>
                    </tbody>
            </table>
            <table class="grid">
                <tr>
                    <td>密码<font color='red'>*</font></td>
                    <td colspan="2"><input name="password" type="password" placeholder="请输入密码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                               data-options="required:true,validType:['length[8,20]']"  value=""></td>
                    <td>钱包GA码<font color='red'>*</font></td>
                    <td colspan="2"><input name="gaCode" type="type" placeholder="请输入验证码" class="easyui-validatebox easyui-textbox" style="width: 274px;"
                                           data-options="required:true,validType:['length[6,6]'],precision:0"  value=""></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>