<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<%@ include file="/global/lang.jsp" %>
<div class="bitms-con error-bg">
	<div class="sys-error tac sys-error-500">
		<div class="sys-error-txt fs26"><fmt:message key='error.massage'/></div>
		<a class="error-back cf fs22 brs2" href="${ctx}/login" title="bitms"><fmt:message key='error.goback'/></a>
	</div>
</div>
</body>