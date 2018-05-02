<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp" %>
<jsp:useBean id="Timestamp" class="java.util.Date"/>
<script type="text/javascript">
    $(function() {
        $('#accountCollectAddrForm').form({
            url : '${ctx}/fund/accountCollectAddr/approval/approval',
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
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#accountCollectAddrForm').find('input[name="csrf"]').val(result.csrf);
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
        $("#accountCollectAddrForm").parent().css("overflow-y","scroll");
    });
</script>
<div id="approvalWin" class="easyui-layout" data-options="fit:true,border:false" style="">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="accountCollectAddrForm" method="post">
            <input type="hidden" id="status" name="status" value=""/>
            <input type="hidden" id="id" name="id" value="${accountCollectAddr.id}" />
            <table class="grid">
                <tr>
                    <td colspan="6" align="center"><h3>基本信息</h3></td>
                </tr>
                <tr>
                    <td align="right">UID：</td>
                    <td>${account.unid} </td>
                    <td align="right">姓名：</td>
                    <td>${certification.surname}&nbsp;${certification.realname}&nbsp;${account.accountName}</td>
                    <td align="right">注册时间：</td>
                    <td>
                        <c:set target="${Timestamp}" property="time" value="${account.createDate}"/>
                        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${Timestamp}" type="both"/>
                            </td>
                </tr>
                <tr>
                    <td align="right">地区代码：</td>
                    <td>${certification.regionId}</td>
                    <td align="right">护照编号：</td>
                    <td>${certification.passportId}</td>
                    <td align="right">证件审核状态：</td>
                    <td>${certification.status==1?"已通过":certification.status==2?"未通过":"待审核"}</td>
                </tr>
                <tr>
                    <td align="right">提币地址：</td>
                    <td>${accountCollectAddr.collectAddr}</td>
                    <td align="right">提币地址证券ID：</td>
                    <td>${accountCollectAddr.stockinfoId}</td>
                    <td align="right">提币地址激活状态：</td>
                    <td  class="stateClass">${accountCollectAddr.isActivate}</td>
                </tr>
                <tr>
                    <td align="right">审核备注：</td>
                    <td colspan="5">
                        <input  class="easyui-textbox" data-options="multiline:true,validType:['length[0,120]']"  name="remark" style="width: 100%;height: 50px;"></input>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center"><h3>审核记录</h3></td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <table  class="table"  cellspacing="0" cellpadding="0"width="100%">
                            <thead>
                            <tr>
                                <th>时间</th>
                                <th>账户名</th>
                                <th>真实姓名</th>
                                <th>账户ID</th>
                                <th width="320px">内容</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${!empty logList}">
                                <c:forEach var="item" items="${logList }"  varStatus="status" >
                                            <tr class="test">
                                                <td>
                                                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createDate}" type="both"/>
                                                </td>
                                                <td>${item.userName}</td>
                                                <td>${item.trueName}</td>
                                                <td>${item.createBy}</td>
                                                <td width="320px" style="word-wrap:break-word;word-break:break-all;">${item.remark}</td>
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