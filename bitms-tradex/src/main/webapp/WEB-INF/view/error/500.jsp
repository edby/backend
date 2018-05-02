<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
	<div class="sys-error text-center sys-error-500">
		<div class="sys-error-txt fs26">
			<c:choose>
				<c:when test="${message != null}">
					${code} : ${message}!
				</c:when>
				<c:otherwise>
					I'm sorry, I'm lost!
				</c:otherwise>
			</c:choose>
		</div>
		<a class="error-back fs22 brs2" href="${ctx}/login" title="bitms">Return</a>
	</div>
</body>