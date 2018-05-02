<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp" %>
<jsp:useBean id="Timestamp" class="java.util.Date"/>
<script type="text/javascript">
    $(function() {
        $('#editRechargeAppForm').form({
            url : '${ctx}/fund/bankRecharge/approval/approval',
            onSubmit : function() {
                if($("#approveStatus").val()=='<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>')
                {
                    if($("#gaCode").val()==''){
                        alert('请输入GA验证码');
                        return false;
                    }
                }
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
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editRechargeAppForm').find('input[name="csrf"]').val(result.csrf);
                }
                parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                parent.$.modalDialog.handler.dialog('close');
            }
        });
    });
    $(document).ready(function(){
        $(".stateClass").each(function(index,obj){
            $(obj).html(getDictValueByCode($(obj).text()));
        });
        $("#editRechargeAppForm").parent().css("overflow-y","scroll");
    });
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editRechargeAppForm" method="post">
            <input type="hidden" id="approveStatus" name="status" value=""/>
            <input type="hidden" id="id" name="id" value="${accountFundCurrent.id}" />
            <input type="hidden" id="stockinfoId" name="stockinfoId" value="${accountFundCurrent.stockinfoId}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><h3>基本信息</h3></td>
                </tr>
                <tr>
                    <td colspan="6" align="right">TRANS ID: ${accountFundCurrent.transId}</td>
                </tr>
                <tr>
                    <td align="right">UID：</td>
                    <td>${account.unid} </td>
                    <td align="right">姓名：</td>
                    <td>${certification.surname}&nbsp;${certification.realname}</td>
                    <td align="right">注册时间：</td>
                    <td>
                        <c:set target="${Timestamp}" property="time" value="${account.createDate}"/>
                        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${Timestamp}" type="both"/>
                            </td>
                </tr>
                <tr>
                    <td align="right">邮箱：</td>
                    <td>${account.email}</td>
                    <td align="right">护照编号：</td>
                    <td>${certification.passportId}</td>
                    <td align="right">审核状态：</td>
                    <td>${certification.status==1?"已通过":certification.status==2?"未通过":"待审核"}</td>
                </tr>
                <tr>
                    <td align="right">账户剩余可用：</td>
                    <td><fmt:formatNumber value="${enableModel.enableAmount}" pattern="#,##0.00000000"  /></td>
                    <td align="right">总充值额：</td>
                    <td><fmt:formatNumber value="${chargeAmt}" pattern="#,##0.00000000"  /></td>
                    <td align="right">总提现额：</td>
                    <td><fmt:formatNumber value="${usedAmt}" pattern="#,##0.00000000"  /></td>
                </tr>

                    <tr>
                            <td colspan="6" align="center"><h3>最近10条充值记录</h3></td>
                        </tr>
                        <tr>
                            <td colspan="6" align="center">
                                <table  class="table"  cellspacing="0" cellpadding="0"width="100%">
                                    <thead>
                                    <tr>
                                        <th>时间</th>
                                        <th>交易ID</th>
                                        <th>数量</th>
                                        <th>状态</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr class="test" style="background-color: #008855;">
                                        <td>
                                            <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${accountFundCurrent.createDate}"/>
                                        </td>
                                        <td>${accountFundCurrent.transId}</td>
                                        <td>${accountFundCurrent.amount}</td>
                                        <td  class="stateClass">${accountFundCurrent.status}</td>
                                    </tr>
                                   <c:if test="${!empty chargeList}">
                                        <c:forEach var="item" items="${chargeList }"  varStatus="status" >
                                            <c:choose>
                                            <c:when test="${item.id != accountFundCurrent.id }">
                                            <tr class="test">
                                                <td>
                                                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createDate}"/>
                                                </td>
                                                <td>${item.transId}</td>
                                                <td>${item.amount}</td>
                                                <td  class="stateClass">${item.status}</td>
                                            </tr>
                                            </c:when>
                                            <c:otherwise>
                                            </c:otherwise>
                                        </c:choose>
                                        </c:forEach>
                                    </c:if>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                <tr>
                    <td colspan="6" align="center">
                       GA验证码： <input id="gaCode" name="gaCode" type="text" placeholder="请输入GA验证码" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,validType:['length[6,6]'],precision:0" value="">
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>