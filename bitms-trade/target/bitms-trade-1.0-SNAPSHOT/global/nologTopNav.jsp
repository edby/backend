<%@ page import="com.blocain.bitms.security.OnLineUserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="certificationInfo" value="${fns:getCertificationInfo()}"/>
<div class="row clearfix">
    <div class="col-md-12 column">
        <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button>
                <a class="navbar-brand" name="155555555502" href="${ctx}/login">
                    <img src="${imagesPath}/bitms/bitms.svg" />
                </a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a data-toggle="modal" data-target="#login-con">
                            <span class="text-success">LOG IN</span>
                        </a>
                    </li>
                    <li>
                        <a href="register"  <%--onclick="remind(remindType.normal, '<fmt:message key="register.noOpen" />');"--%>>
                            <span class="text-success">SIGN UP</span>
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
</div>
 <div class="modal fade login-bg" id="login-con"  role="dialog">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-body">
                 <form:form data-widget="validator" id="loginForm" name="loginForm">
                     <div class="h2 text-center mb20 cf">LOG IN</div>
                     <div class="text-center login-tip fs14 mb10">
                         <span class="text-danger">Important：</span><span>Please check that you are visiting</span><br>
                         <span class="login-addr"><span class="glyphicon glyphicon-lock text-success fs12" aria-hidden="true"></span><span class="text-success">https</span>://www.bitms.com</span>
                     </div>
                     <div class="form-group ui-form-item">
                         <input type="email" class="form-control input-lg" name="username" id="email"
                                data-display="Email"
                                placeholder="Email"/>
                     </div>
                     <div class="form-group ui-form-item">
                         <input type="password" class="form-control input-lg" name="password" onkeydown="KeyDown($('#loginBtn'));" id="loginPwd"
                                data-display="Password"
                                placeholder="Password"/>
                     </div>
                     <div class="form-group">
                         <div class="ln">
                             <div id="captcha"></div>
                             <input type='hidden' id='csessionid' name='csessionid'/>
                             <input type='hidden' id='sig' name='sig'/>
                             <input type='hidden' id='token' name='token'/>
                             <input type='hidden' id='scene' name='scene'/>
                         </div>
                     </div>
                     <a class="pull-left mb20 mt-10 fs14" href="${ctx}/forgetPass" title="Forgot your password?">Forgot your password?</a>
                     <a href="/register" class="text-info pull-right mt-10 fs14"  <%--onclick="remind(remindType.normal, '<fmt:message key="register.noOpen" />');"--%>  title="SIGN UP">SIGN UP</a>
                     <div class="form-group">
                         <button type="button" class="btn btn-primary btn-lg bitms-width" id="loginBtn">LOG IN</button>
                     </div>
                 </form:form>
             </div>
         </div>
     </div>
</div>
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
    $(function(){
        $('.load-pop').hide();
        $('.login-txt1,.login-txt2').addClass('reveal-landing');
    });
</script>

