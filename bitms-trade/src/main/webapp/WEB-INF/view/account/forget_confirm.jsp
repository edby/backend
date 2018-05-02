<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
    <div class="col-xs-12 mt50">
        <div class="text-center"><a href="/" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BitMS"></a></div>
        <div class="p10 bitms-con1 text-center mt10">
            <div class="bitms-con1 py20">
                <div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
                    <div class="h2 text-center mb20 cf">Reset Password</div>
                    <div class="alert alert-success clearfix" style="background-image: none;" role="alert">
                        <span class="pull-left">Please check your email to reset your password.</span>
                        <a href="${forgetPass}/forgetPass" title="resend" class="text-danger pull-right">Resend</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
    <div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<script>
    $(function(){
        $('.pageLoader').hide();
    })
</script>
</body>
</html>
