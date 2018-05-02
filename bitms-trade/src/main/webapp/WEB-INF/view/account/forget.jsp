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
        <div class="text-center"><a href="/" title="BitMS"><img src="${imagesPath}/bitms/bitms.svg" alt="BitMS"></a></div>
        <div class="p10 bitms-con1 text-center mt10">
            <div class="bitms-con1 py20">
                <div class="col-xs-12 bitms-nofloat my0 bitms-max-input">
                    <form:form data-widget="validator" id="findpwdForm">
                    <div class="h2 text-center mb20 cf">Reset Password</div>
                        <div class="form-group ui-form-item">
                            <input type="text"  class="form-control input-lg" id="forgetEmail" name="email" required
                                   data-rule="email"
                                   data-display="Email"
                                   placeholder="Email"/>
                        </div>
                        <div class="form-group ui-form-item">
                            <input type="text"  class="form-control input-lg" name="phone" id="phone"
                                   placeholder="Phone"
                                   data-display="Phone number"/>
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
                        <div class="form-group ui-form-item">
                            <div class="input-group">
                                <input type="text"  class="form-control input-lg" onkeydown="KeyDown($('#verifySubmit'));" name="vlidCode"
                                       placeholder="SMS code"
                                       data-display="SMS code"/>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary safeSendSms btn-lg fs16" type="button" id="sendSMS">Send SMS</button>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="button" class="btn btn-primary bitms-width btn-lg" id="verifySubmit" name="verifySubmit">Submit</button>
                        </div>
                        <div class="form-group mt-5 fs14 text-right login-link">Already have an account？<a href="${ctx}/login" title="login" class="text-primary">LOG IN</a></div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="navbar-fixed-bottom bitms-con">
    <div class="fs12 text-center bitms-footer">© 2017 BITMS.com All Rights Reserved BITMS LIMITED</div>
</footer>
<script>
    var validator;
    seajs.use(['validator', 'i18n'], function (Validator, I18n) {
        $('.pageLoader').hide();
        validator = new Validator();
        <%--滑块验证--%>
        $('#verifySubmit').attr("disabled",true);
        var nc = new noCaptcha();
        var nc_appkey = appKey;  <%--应用标识,不可更改--%>
        var nc_scene = 'other';  <%--场景,不可更改--%>
        var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
        var nc_option = {
            renderTo: '#captcha',
            appkey: nc_appkey,
            scene: nc_scene,
            token: nc_token,
            callback: function (data) {  <%--校验成功回调--%>
                $('#verifySubmit').attr("disabled",false);
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
        $("#verifySubmit").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#findpwdForm',
                autoSubmit: false, <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '${ctx}/forgetPass/submit',
                            type: 'post',
                            data: $('#findpwdForm').serialize(),
                            dataType: 'json',
                            beforeSend:function(){
                                $('.reg-pop').fadeIn();
                                $("#verifySubmit").addClass('btn-dis');
                            },
                            success: function (data) {
                                $('#findpwdForm').find('input[name="csrf"]').val(data.csrf);
                                nc.init(nc_option);
                                if (data.code == bitms.success) {
                                    jumpUrl("${ctx}/forgetPass/confirm");
                                } else {
                                    if (data.code == bitms.noUser) {
                                        $('#forgetEmail').val('').focus();
                                        remind(remindType.error, data.message);
                                    } else if (data.code == bitms.captchaError) {
                                        remind(remindType.error, I18n.prop("error.captchaCode"));
                                    } else if(data.code == bitms.smsError){
                                        remind(remindType.error,I18n.prop("error.sescode"));
                                    } else {
                                        remind(remindType.error, data.message);
                                    }
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#verifySubmit").removeClass('btn-dis');
                            }
                        });
                    }
                }
            }).addItem({
                element: '[name=email]',
                required: true,
                rule: 'email'
            });
            $("#findpwdForm").submit();
        });

        $("#sendSMS").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#findpwdForm',
                autoSubmit: false, <%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '${ctx}/forgetPass/sendSMS',
                            type: 'post',
                            data: $('#findpwdForm').serialize(),
                            dataType: 'json',
                            beforeSend:function(){
                                $('.reg-pop').fadeIn();
                                $('#verifySubmit').attr("disabled",true);
                            },
                            success: function (data) {
                                nc.init(nc_option);
                                $('#verifySubmit').attr("disabled",true);
                                $('#findpwdForm').find('input[name="csrf"]').val(data.csrf);
                                if (data.code == bitms.success) {
                                    countDown($('#sendSMS'), I18n.prop('generic.sendsms'));
                                    remind(remindType.success, I18n.prop('setting.sendsuccess'));
                                } else if (data.code == bitms.captchaError) {
                                    remind(remindType.error, I18n.prop("error.captchaCode"));
                                }else if (data.code == bitms.codeError) {
                                    remind(remindType.error, I18n.prop("error.phoneErr"));
                                } else {
                                    remind(remindType.error, data.message);
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $('#verifySubmit').attr("disabled",false);
                            }
                        });
                    }
                }
            }).addItem({
                element: '[name=email]',
                required: true,
                rule: 'email'
            }).addItem({
                element: '[name=phone]',
                required: true,
                rule: 'phone'
            });
            $("#findpwdForm").submit();
        });

        $('.login-link').click(function(){
            changeLanguageLogin('en_US');
        })

    });
</script>
</body>
</html>
