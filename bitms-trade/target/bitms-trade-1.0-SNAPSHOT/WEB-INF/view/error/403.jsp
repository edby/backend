<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
	 <div class="bitms-con error-bg">
	  <div class="sys-error text-center sys-error-403">
		<div class="sys-error-txt fs26"><fmt:message key='error.massage'/></div>
		<a class="error-back cf fs22 brs2" href="${ctx}/login" title="bitms"><fmt:message key='error.goback'/></a>
	  </div>
	</div>
</body>
</html>

