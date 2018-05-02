<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp" %>
<jsp:useBean id="Timestamp" class="java.util.Date"/>

<style>
    #code{
        background: #fff;
        border: 10px solid white;
        float: left;
    }
</style>
<script type="text/javascript">
    $(function() {
        $('#editerctokenWithdrawAppForm').form({
            url : '${ctx}/fund/erctokenwithdraw/approval/approval',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                <c:if test="${needSign eq true}">
                    if($("#approveStatus").val()=='<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>')
                    {
                        if(!checkCodeFunc()){
                            alert('数据校验错误：请重新扫描！');
                            isValid = false;
                        }
                    }
                    try{progressClose();}catch(err){}
                </c:if>
                return isValid;
            },
            success : function(result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editerctokenWithdrawAppForm').find('input[name="csrf"]').val(result.csrf);
                }
                parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                parent.$.modalDialog.handler.dialog('close');
            }
        });
        $('#code').qrcode({
            width: 280, //宽度
            height:280, //高度
            text: '${signStr}'
        });
    });
    $(document).ready(function(){
        $(".stateClass").each(function(index,obj){
            $(obj).html(getDictValueByCode($(obj).text()));
        });
        $("#editerctokenWithdrawAppForm").parent().css("overflow-y","scroll");
    });
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editerctokenWithdrawAppForm" method="post">
            <input type="hidden" id="approveStatus" name="approveStatus" value=""/>
            <input type="hidden" id="id" name="id" value="${accountFundCurrent.id}" />
            <input type="hidden" id="stockinfoId" name="stockinfoId" value="${accountFundCurrent.stockinfoId}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><h3>基本信息</h3></td>
                </tr>
                <tr>
                    <td colspan="6" align="right">WITHDRAW ID: ${accountFundCurrent.id}</td>
                </tr>
                <tr>
                    <td align="right">UID：</td>
                    <td>${account.unid} </td>
                    <td align="right">姓名：</td>
                    <td>${certification.realname}&nbsp;${certification.surname}</td>
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
                    <td><fmt:formatNumber value="${enableModel.enableAmount}" pattern="#,##0.00000000"  /><br />（冻结：<fmt:formatNumber value="${enableModel.frozenAmt}" pattern="#,##0.00000000"  />）</td>
                    <td align="right">总充值额：</td>
                    <td><fmt:formatNumber value="${chargeAmt}" pattern="#,##0.00000000"  /></td>
                    <td align="right">总提现额：</td>
                    <td><fmt:formatNumber value="${usedAmt}" pattern="#,##0.00000000"  /></td>
                </tr>
                <tr>
                    <td colspan="6" align="center"><h3>当前申请提现记录</h3></td>
                </tr>
                <tr>
                    <td colspan="6">
                        <table id="rowPage0"  cellspacing="0" cellpadding="0" class="table" width="100%">
                            <thead>
                            <tr>
                                <th><fmt:message key='fund.raise.withdrawalTime' /></th>
                                <th style="width: 10%;">address</th>
                                <th><fmt:message key='fund.raise.withdrawalNum' /></th>
                                <th><fmt:message key='fund.raise.netFees' /></th>
                                <th><fmt:message key='fund.raise.approvalStatus' /></th>
                                <th><fmt:message key='fund.raise.transferStatus' /></th>
                            </tr>
                            </thead>
                            <tbody id="list_emement0">
                            <tr class="test">
                                <td>
                                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${accountFundCurrent.currentDate}"/>
                                </td>
                                <td>${accountFundCurrent.withdrawAddr}</td>
                                <td>
                                    <fmt:formatNumber value="${accountFundCurrent.occurAmt-accountFundCurrent.fee}" pattern="#,##0.00000000"  />
                                </td>
                                <td>
                                    <fmt:formatNumber value="${accountFundCurrent.fee}" pattern="#,##0.00000000"  />
                                </td>
                                <td class="stateClass">${accountFundCurrent.approveStatus}</td>
                                <td class="stateClass">${accountFundCurrent.transferStatus}</td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center"><h3>最近10条提现记录</h3></td>
                </tr>
                <tr>
                    <td colspan="6">
                        <table id="rowPage"  cellspacing="0" cellpadding="0" class="table" width="100%">
                            <thead>
                            <tr>
                                <th><fmt:message key='fund.raise.withdrawalTime' /></th>
                                <th style="width: 10%;">address</th>
                                <th><fmt:message key='fund.raise.withdrawalNum' /></th>
                                <th><fmt:message key='fund.raise.netFees' /></th>
                                <th><fmt:message key='fund.raise.approvalStatus' /></th>
                                <th><fmt:message key='fund.raise.transferStatus' /></th>
                            </tr>
                            </thead>
                            <tbody id="list_emement">
                            <c:if test="${!empty currList}">
                                <c:forEach var="item" items="${currList }"  varStatus="status" >
                                    <c:choose>
                                        <c:when test="${item.id != accountFundCurrent.id }">
                                            <tr class="test">
                                                <td>
                                                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.currentDate}"/>
                                                </td>
                                                <td>${item.withdrawAddr}</td>
                                                <td>
                                                    <fmt:formatNumber value="${item.occurAmt-item.fee}" pattern="#,##0.00000000"  />
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${item.fee}" pattern="#,##0.00000000"  />
                                                </td>
                                                <td class="stateClass">${item.approveStatus}</td>
                                                <td class="stateClass">${item.transferStatus}</td>
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
                    <td colspan="6" align="center"><h3>最近10条充值记录</h3></td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <table  class="table"  cellspacing="0" cellpadding="0"width="100%">
                            <thead>
                            <tr>
                                <th>时间</th>
                                <th>地址</th>
                                <th>数量</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${!empty chargeList}">
                                <c:forEach var="item" items="${chargeList }"  varStatus="status" >
                                    <tr class="test">
                                        <td>
                                            <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createDate}"/>
                                        </td>
                                        <td>${item.walletAddr}</td>
                                        <td>${item.amount}</td>
                                        <td  class="stateClass">${item.status}</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <c:if test="${needSign eq true}">
                    <tr>
                        <td colspan="6" align="center"><h3>请扫描二维码</h3></td>
                    </tr>
                    <tr>
                        <td colspan="6">
                        <div id="code"></div>
                            <%--<input id="signedTransactionData0"
                                       name="signedTransactionData0" type="text" placeholder="请输入"
                                       class="easyui-validatebox easyui-textbox" style="width: 400px;height:350px; float:left;margin-left: 0px;"
                                       data-options="multiline:true" value="">--%>
                            <textarea id="signedTransactionData0" name="signedTransactionData0"  scrolling="no" style="resize:none;font-size: 25px;width: 643px;height:275px; float:left;margin-left: 0px;margin-top:10px;margin-right:10px;"></textarea>
                            <input id="signedTransactionData" name="signedTransactionData" type="hidden" value="">
                        </td>
                    </tr>
                </c:if>
            </table>
        </form:form>
    </div>
</div>
<script>
    function checkCodeFunc() {
        var orgmdt5 = '${oldMd5}';
        var address = "${accountFundCurrent.withdrawAddr}".toLowerCase();
        var checkCode = $("#signedTransactionData0").val();
        if(checkCode.length<1){return false;}
        var ackMd5arry = checkCode.match(/ackMd5=(\S*)&signature/);
        var ackMd5 = ackMd5arry[1];
        if((orgmdt5+"")!=(ackMd5+"")){return false;}
        var substr2 = checkCode.match(/signature=(\S*)&md5/);
        var substrAddr = substr2[1];
        $("#signedTransactionData").val((substrAddr+""));
        var realAddr =(checkCode+"").substring(9,51).toLowerCase();
        if((address+"")!=(realAddr+"")){return false;}
        var msg = (checkCode+"").substring(0,(checkCode+"").indexOf("&md5"));
        var md5Str = (checkCode+"").substring((checkCode+"").indexOf("&md5")+5);
        if((md5(msg)+"") != (md5Str+"")){return false;}
        return true;
    }
</script>