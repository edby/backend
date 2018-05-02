<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
	<div class="col-xs-12 mt50">
		<div class="text-center"><a href="/" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BITMS"></a></div>
		<div class="p10 bitms-con1 text-center mt10">
			<div class="bitms-con1 py20 clearfix">
				<div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
					<form:form data-widget="validator" id="loginForm" name="loginForm">
						<div class="h2 text-center mb20 cf">LOG IN</div>
						<div class="text-center login-tip fs14 mb10 alert alert-danger">
							<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
							 <span>Please check that you are visiting</span><br class="visible-xs"/>
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
						<a class="pull-left mb20 mt-5 fs14" href="${ctx}/forgetPass" title="Forgot your password?">Forgot your password?</a>
						<a href="/register" class="text-info pull-right mt-5 fs14"  &lt;%&ndash;onclick="remind(remindType.normal, '<fmt:message key="register.noOpen" />');"&ndash;%&gt;  title="SIGN UP">SIGN UP</a>
						<div class="form-group">
							<button type="button" class="btn btn-primary btn-lg bitms-width" id="loginBtn">LOG IN</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
	<div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
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
		$('.pageLoader').hide();
	})
</script>
</body>
</html>
