<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<form id="listForm" action="">
<table class="tablelist">
	<thead>
		<tr>
			<th>钱包ID</th>
			<th>钱包名称</th>
			<th>余额</th>
			<th>已确认余额</th>
			<th>钱包类型</th>
			<th>操作</th>
		</tr>
    </thead>
    <tbody>
    <c:choose>
	   <c:when test="${empty walletList}">
	   <tr><td colspan="8" style="text-align:center;">暂无数据</td></tr>  
	   </c:when>
	   <c:otherwise>
	   <c:forEach items="${walletList}" var="bitPayModel" varStatus="vs">
		<tr>
			<td>${bitPayModel.id}</td>
			<td>${bitPayModel.label}</td>
			<td><fmt:formatNumber value="${bitPayModel.balance/100000000}" pattern="#0.00000000" /></td>
			<td><fmt:formatNumber value="${bitPayModel.confirmedBalance/100000000}" pattern="#0.00000000" /></td>
			<td><c:if test="${bitPayModel.walletType==1}">平台充值钱包</c:if><c:if test="${bitPayModel.walletType==2}">平台划拨钱包</c:if></td>
			<td>
				<a href="javascript:void(0);" class="tablelink" onclick="update('${ctx}/admin/wallet/info?id=${bitPayModel.keychainId}')"><i class="entypo-pencil"></i>编辑钱包参数</a>
				<a href="javascript:void(0);" class="tablelink" onclick="del('${ctx}/admin/wallet/delete.ajax?id=${bitPayModel.keychainId}')"><i class="entypo-cancel"></i>删除</a>
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