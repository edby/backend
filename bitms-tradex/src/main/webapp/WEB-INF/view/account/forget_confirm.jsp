<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Reset Password</h2>
            <hr>
            <div class="alert alert-success clearfix" style="background-image: none;" role="alert">
                <span class="pull-left">Please check your email to reset your password.</span>
                <a href="${forgetPass}/forgetPass" title="resend" class="text-danger pull-right">Resend</a>
            </div>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
</body>
</html>
