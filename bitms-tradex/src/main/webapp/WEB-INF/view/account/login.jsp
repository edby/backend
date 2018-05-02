<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Log in</h2>
            <hr>
            <form:form class="form-horizontal" data-widget="validator" id="loginForm" name="loginForm">
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <p class="alert alert-danger" style="background-color:#f2dede;border-color:#ebccd1;color:#a94442;padding:10px;background-image:none;margin-bottom:0px;">
                            <span class="glyphicon glyphicon-exclamation-sign"></span>
                            <span>Please check that you are visting</span>
                            <br>
                            <span style="border:1px solid #aaa;padding:2px;border-radius:2px;margin-top:20px;">
                                <span class="glyphicon glyphicon-lock text-success" style="top:2px;"></span>
                                <span style="padding:2px;color:#aaa;">|</span>
                                <span><span class="text-success">https</span>://www.biex.com</span>
                            </span>
                        </p>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <input autocomplete="off" type="email" class="form-control" name="username" id="email"
                               data-display="Email"
                               placeholder="Your Email"/>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input autocomplete="off" type="password" class="form-control" name="password" onkeydown="KeyDown($('#loginBtn'));" id="loginPwd" data-display="Password" placeholder="Your Password"/>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-time" style="top:0;"></span>
                        <input autocomplete="off" type="text" class="form-control" onkeydown="KeyDown($('#loginBtn'));" placeholder="Google Authenticator（If enable）" id="gaCode" name="gaCode">
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:5px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <div class="ln">
                            <div id="captcha"></div>
                            <input type='hidden' id='csessionid' name='csessionid'/>
                            <input type='hidden' id='sig' name='sig'/>
                            <input type='hidden' id='token' name='token'/>
                            <input type='hidden' id='scene' name='scene'/>
                        </div>
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:0;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block" id="loginBtn">Submit</button>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <a class="pull-left" href="/forgetPass">Forgot Password？</a>
                        <a class="pull-right" href="/register">Register</a>
                    </div>
                </div>
           </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script src="${ctx}/scripts/src/account/login.js"></script>
<script>
    <c:if test="${showCaptcha}">
    $('#loginBtn').attr('disabled', 'disabled');
    var nc_option = {
        renderTo: '#captcha',
        appkey: nc_appkey,
        scene: nc_scene,
        token: nc_token,
        callback: function (data) {// 校验成功回调
            $('#loginBtn').removeAttr('disabled');
            $("#sig").val(data.sig);
            $("#token").val(nc_token);
            $("#scene").val(nc_scene);
            $("#csessionid").val(data.csessionid);
        },
        language: locale || 'en_US',
        foreign: 0,
        isEnabled: true
    };
    nc.init(nc_option);
    </c:if>
</script>
</body>
</html>