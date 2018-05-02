<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<c:if test="${pageInfo.total > 0}">
<div class="pagin">
	<div class="message">共<i class="blue">${pageInfo.total}</i>条记录，当前显示第&nbsp;<i class="blue">${pageInfo.pageNum}&nbsp;</i>页</div>
    <ul class="paginList">
    <li class="paginItem"><a href="javascript:void(0);" onclick="page(${pageInfo.pageNum <= 1 ? 1:(pageInfo.pageNum - 1)})"><span class="pagepre"></span></a></li>
	<c:if test="${pageInfo.firstPage > 1}">  
		<li class="paginItem"><a href="javascript:void(0);" onclick="page(1)">1</a></li>
	   	<li class="paginItem more"><a href="javascript:void(0);">...</a></li>
	</c:if>
    <c:forEach items="${pageInfo.navigatepageNums}" var="s">
		<c:choose>
		   <c:when test="${pageInfo.pageNum == s}">  
		         <li class="paginItem current"><a href="javascript:void(0);">${s}</a></li>
		   </c:when>
		   <c:otherwise> 
			     <li class="paginItem"><a href="javascript:void(0);" onclick="page(${s})">${s}</a></li>
		   </c:otherwise>
		</c:choose>
	</c:forEach>
    <c:if test="${pageInfo.lastPage < pageInfo.pages}">  
    	<li class="paginItem more"><a href="javascript:void(0);">...</a></li>
		<li class="paginItem"><a href="javascript:void(0);" onclick="page(${pageInfo.pages})">${pageInfo.pages}</a></li>
	</c:if>
    <li class="paginItem"><a href="javascript:void(0);" onclick="page(${pageInfo.pageNum >= pageInfo.pages ? pageInfo.pages:(pageInfo.pageNum + 1)})"><span class="pagenxt"></span></a></li>
    </ul>
</div> 
<br/>
</c:if>