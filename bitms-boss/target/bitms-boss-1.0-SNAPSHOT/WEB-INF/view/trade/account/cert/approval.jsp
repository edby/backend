<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp" %>
<jsp:useBean id="Timestamp" class="java.util.Date"/>
<script type="text/javascript">
    $(function() {
        $('#editCertApprovalForm').form({
            url : '${ctx}/account/cert/approval/pass',
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
                    $('#editCertApprovalForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
    });
    $(document).ready(function(){
        $(".stateClass").each(function(index,obj){
            $(obj).html(getDictValueByCode($(obj).text()));
        });
        $("#editCertApprovalForm").parent().css("overflow-y","scroll");
    });
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editCertApprovalForm" method="post">
            <input type="hidden" id="status" name="status" value="${certification.status}"/>
            <input type="hidden" id="id" name="id" value="${certification.id}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><h3>基本信息</h3></td>
                </tr>
                <tr>
                    <td align="right" width="80px">帐号：</td>
                    <td width="240px">${account.accountName} </td>
                    <td align="right" width="80px">姓氏：</td>
                    <td width="240px">${certification.surname}</td>
                    <td align="right" width="80px">名字：</td>
                    <td width="240px"> ${certification.realname}</td>
                </tr>
                <tr>
                    <td align="right">地区：</td>
                    <td>${guojia}(代码：${certification.regionId})</td>
                    <td align="right">护照编号：</td>
                    <td>${certification.passportId}</td>
                    <td align="right">审核状态：</td>
                    <td>${certification.status==1?"已通过":certification.status==2?"未通过":"待审核"}</td>
                </tr>
                <tr>
                    <td colspan="6">
                        <c:if test="${!empty files}">
                            <c:if test="${!empty files.cover}">
                                <div style="text-align:center;">
                                    <img src="${baseUrl}/${files.cover}" width="80%" alt="封面"/>
                                    <center><h1>封面</h1></center>
                                </div>
                            </c:if>
                            <c:if test="${!empty files.frontage}">
                                <div style=" text-align:center;">
                                    <img src="${baseUrl}/${files.frontage}" width="80%" alt="正面" />
                                    <br />
                                    <center><h1>正面</h1></center>
                                </div>
                            </c:if>
                            <c:if test="${!empty files.opposite}">
                                <div style=" text-align:center;">
                                    <img src="${baseUrl}/${files.opposite}" width="80%" alt="反面" />
                                    <br />
                                    <center><h1>反面</h1></center>
                                </div>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>