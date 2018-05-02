<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/global/header.jsp"%>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
	<div class="col-xs-12 mt50">
		<div class="text-center"><a href="/" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BitMS"></a></div>
		<div class="p10 bitms-con1 text-center mt10">
			<div class="bitms-con1 py20">
				<div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
				<form:form data-widget="validator" id="reg-step3">
					    <input type="hidden" name="id" value="${id}">
					    <input type="hidden" name="oid" value="${oid}">
						<div class="h2 text-center mb20 cf">Login Password</div>
						<div class="form-group">
							<input  class="form-control input-lg" id="loginPwd" name="loginPwd"
								   type="password"
								   data-display="Password"
								   placeholder="Password"/>
							<p class="fs12 mt5 text-left">At least 8 characters, must contain both capital letters and numbers, and cannot contain spaces</p>
						</div>
						<div class="form-group ui-form-item">
							<input  class="form-control input-lg" type="password" onkeydown="KeyDown($('#regSubmit2'));" id="confirmPwd" name="confirmPwd"
								   placeholder="Confirm your password" data-display="Password"/>
						</div>
						<div class="form-group mt30">
							<button type="button" class="btn btn-primary bitms-width btn-lg" id="regSubmit2" name="regSubmit2">Sign Up</button>
						</div>
					</form:form>
					<div class="py20 hidden" id="reg-step4">
						<span class="glyphicon glyphicon-ok-circle text-success fs50" aria-hidden="true"></span>
						<div class="text-success fs25 mt15">Successful</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
	<div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<script src="${ctx}/scripts/src/account/register_confirm.js"></script>
</body>
</html>
