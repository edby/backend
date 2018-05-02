<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<form id="listForm" action="">
<input type="hidden" id="password" name="password"/> 
<table class="tablelist">
	<thead>
		<tr>
			<th><input name="" id="allcheck" type="checkbox" value="" checked="checked"/></th>
			<th>提现记录ID</th>
			<th>帐户ID</th>
			<th>提现地址</th>
			<th>提现金额</th>
			<th>手续费</th>
			<th>提现状态</th>
			<th>提现时间</th>
			<th>操作</th>
		</tr>
    </thead>
    <tbody>
    <c:choose>
	   <c:when test="${pageInfo.total <= 0}">
	   <tr><td colspan="10" style="text-align:center;">暂无数据</td></tr>  
	   </c:when>
	   <c:otherwise>
	   <c:forEach items="${pageInfo.list}" var="withdraw" varStatus="vs">
		<tr>
			<td><input name="idAry" type="checkbox" value="${withdraw.id}" /></td>
			<td>${withdraw.id}</td>
			<td>${withdraw.accountId}</td>
			<td>${withdraw.withdrawAddr}</td>
			<td>${withdraw.occurAmt}</td>
			<td>${withdraw.netFee}</td>
			<td>
			<c:if test="${withdraw.state==0}">未提现</c:if>
			<c:if test="${withdraw.state==1}">已提现未导出</c:if>
			<c:if test="${withdraw.state==2}">已导出</c:if>
			</td>
			<td>
				<jsp:useBean id="Timestamp" class="java.util.Date"/> 
				<c:set target="${Timestamp}" property="time" value="${withdraw.createDate}"/> 
				<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${Timestamp}" type="both"/> 
			</td>
			<td>
				<a href="javascript:void(0);" class="tablelink" onclick="withdraw('${ctx}/admin/withdraw/confirm.ajax?id=${withdraw.id}')"><i class="entypo-pencil"></i><c:if test="${withdraw.state==0}">提现</c:if></a>
			</td>
		</tr>
		</c:forEach>
	   </c:otherwise>
	</c:choose>
    </tbody>
</table>
</form>

<%@ include file="/WEB-INF/views/admin/home/page.jsp" %>
<script type="text/javascript">
$(function(){
	$('.tablelist tbody tr:odd').addClass('odd');
	$("#allcheck").click(function(){
		var isChecked = $(this).prop("checked");
	    $("input[name='idAry']").prop("checked", isChecked);
	});
});
</script>