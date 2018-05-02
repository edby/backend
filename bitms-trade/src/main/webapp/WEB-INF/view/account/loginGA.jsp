<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body style="background:url('${imagesPath}/bitms/home.jpg')">
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<div class="row my0">
	<div class="col-xs-12 login pt15">
		<a class="login-logo" href="/" title="bitms"><img  class="img-responsive"  src="${imagesPath}/bitms/bitms.svg" alt="bitms" onclick="location.reload();" /></a>
		<a href="/register" class="text-success pull-right fs16" <%--onclick="remind(remindType.normal,  '<fmt:message key="register.noOpen" />')" --%> title="SIGN UP">SIGN UP</a>
		<hr class=" pull-right login-hr">
		<a href="/login" class="text-success pull-right fs16 text-center loginBtn"  data-toggle="modal" href="javascript:void(0)" >LOG IN</a>
		<div class="col-xs-12 bitms-nofloat my0 bitms-max-input" style="margin-top: 100px" id="loginGA-con" role="dialog" data-backdrop="false">
			<form:form data-widget="validator" id="loginSecondForm" name="loginSecond">
				<div class="h2 text-center mb40 cf">2-Factor Authentication</div>
				<c:choose>
					<c:when test="${token.ga == true}">
						<div class="form-group safe2-con  ui-form-item">
							<input type="text" name="ga" class="form-control input-lg" onkeydown="KeyDown($('#loginSecondSub'));"  data-display='GA code' placeholder='GA code'>
						</div>
					</c:when>
					<c:otherwise>
						<div class="safe1-con mb5">
							<div class="text-left">Phone Number&nbsp;<label class="text-success mobNoVal"></label></div>
						</div>
						<div class="form-group safe1-con ui-form-item">
							<div class="input-group">
								<input type="text" name="sms" class="form-control input-lg" onkeydown="KeyDown($('#loginSecondSub'));" data-display='SMS verification' placeholder='SMS verification'>
								<span class="input-group-btn">
									<button class="btn btn-primary safeSendSms btn-lg fs16" type="button" id="loginSendSms">Send SMS</button>
								</span>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="form-group">
					<button type="button" class="btn btn-primary bitms-width btn-lg" id="loginSecondSub">Submit</button>
				</div>
			</form:form>
		</div>
	</div>
</div>
<footer class="navbar-fixed-bottom bitms-con" style="margin-top: 200px;">
	<div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<script>
    var validator;
    seajs.use(['validator', 'i18n'], function (Validator, I18n) {
        $('.pageLoader').hide();
        var mobNo = "${token.mobNo}";
        $('.mobNoVal').text(mobNo);
        <%--获取安全验证策略，控制显示--%>
		var bindGa = "${token.ga}";
        var safe ="";
        if(bindGa == "true"){
            safe = 2;
		}else{
            safe = 1;
		}
        <%--获取手机短信验证码--%>
        $('#loginSendSms').on('click', function () {
            $.ajax({
                url: '/common/login/sendSms',
                type: 'post',
                dataType: 'json',
                beforeSend: function () {
                    $('.reg-pop').fadeIn();
                    $('#loginSendSms').attr("disabled",true);
                },
                success: function (data) {
                    if (data.code == bitms.success) {
                        countDown($('#loginSendSms'), I18n.prop('generic.sendsms'));
                        remind(remindType.success, I18n.prop('setting.sendsuccess'));
                    } else {
                        remind(remindType.error, data.message);
                    }
                },
                complete:function(){
                    $('.reg-pop').hide();
                    $('#loginSendSms').attr("disabled",false);
                }
            });
        });
        <%--二次登录--%>
        validator = new Validator({
            element: '#loginSecondForm',
			autoSubmit: false,<%--当验证通过后不自动提交--%>
            onFormValidated: function (error, results, element) {
                if (!error) {
                    $.ajax({
                        url: '/login/check/submit',
                        type: 'post',
                        data: $('#loginSecondForm').serialize(),
                        dataType: 'json',
                        beforeSend:function(){
                            $('.reg-pop').fadeIn();
                            $("#loginSecondSub").attr("disabled",true);
                        },
                        success: function (data, textStatus, jqXHR) {
                            $('#loginSecondForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                            if (data.code == bitms.success) {
                                jumpUrl("/spot/leveragedSpotTrade?exchangePair=btc2usd");
                            } else {
                                if(data.code == bitms.smsError){
                                    if(safe==1){
                                        remind(remindType.error,I18n.prop("error.sescode"));
                                        $("#loginSecondForm input[name='sms']").val("").focus();
                                    }else if(safe==2){
                                        remind(remindType.error, I18n.prop("error.gaCode"));
                                        $("#loginSecondForm input[name='ga']").val("").focus();
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }else{
                                   remind(remindType.error, data.message);
                                }
                            }
                        },
                        complete:function(){
                            $('.reg-pop').hide();
                            $("#loginSecondSub").attr("disabled",false);
                        }
                    });
                }
            }
        });
         if(safe==1){
                validator.addItem({
                    element: '#loginSecondForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#loginSecondForm [name=ga]',
                    required: true,
					rule:'verificationCode'
                })
            }
        });
    $("#loginSecondSub").on("click", function () {
        $("#loginSecondForm").submit();
    });
    $(function(){
        $("#loginGA-con").modal('show');
    	<%--浏览器返回刷新被返回的页面--%>
        var counter = 0;
        if (window.history && window.history.pushState) {
            $(window).on('popstate', function () {
                window.history.pushState('forward', null, '#');
                window.history.forward(1);
                location.replace(document.referrer);<%--刷新--%>
            });
        }
        window.history.pushState('forward', null, '#'); <%--在IE中必须得有这两行--%>
        window.history.forward(1);
    })
</script>
</body>
</html>