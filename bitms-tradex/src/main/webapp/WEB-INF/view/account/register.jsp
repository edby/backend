<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%@ include file="/global/setup_ajax.jsp" %>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Sign up</h2>
            <hr>
            <form:form class="form-horizontal" data-widget="validator" id="regForm">
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <input class="form-control" type="email" onkeydown="KeyDown();"  id="email" name="email" required
                               data-display="Email" placeholder="Your Email"/>
                    </div>
                </div>
                <div class="form-group slider-group">
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
                <div class="form-group ui-form-item" id="emailCodegroup">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <input type="text" class="form-control"  name="emailCode" placeholder="Email code">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-primary btn-block brs0" id="sendEmail">Send Email</button>
                        </div>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input  class="form-control" id="loginPwd" name="loginPwd"
                                type="password"
                                data-display="Your Password"
                                placeholder="Your Password"/>
                    </div>
                </div>
                <div class="form-group ui-form-item" style="margin-bottom:0;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input  class="form-control" type="password" onkeydown="KeyDown($('#regSubmit'));" id="confirmPwd" name="confirmPwd"
                                placeholder="Comfirm Password" data-display="Password"/>
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:10px;margin-top:10px;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 text-left" style="padding-left:0;">
                        <input type="checkbox" name="agreeChk" id="agreeChk" style="vertical-align: text-bottom;" />
                        <small>I agree to the <a href="/terms" target="_blank">"Terms of Use"</a></small>
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:0;">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block" id="regSubmit">Submit</button>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 text-right" style="padding-left:0;">
                        <small>Already Register？</small>
                        <a href="/login">Login</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script src="${ctx}/scripts/src/account/register.js"></script>
</body>
</html>