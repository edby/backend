<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div class="pageLoader"><div class="rp-inner loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>
<%-- 此段必须要引入 t为小时级别的时间戳 --%>
<link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet" />
<script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
<%-- 引入结束 --%>
<%-- 此段必须要引入 --%>
<div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
<%-- 引入结束 --%>
<div class="container">
    <%--头部--%>
    <%@ include file="/global/topNavBar.jsp"%>
    <div class="row">
        <%@ include file="/global/topPublicNav.jsp" %>
        <div class="col-sm-12 column">
            <%--开始代码位置--%>
            <div class="panel col-md-12 pb0">
                <c:set var="regions" value="${fns:getRegions()}" />
                <%--策略设置
                <div class="bitms-panel col-md-12 bitms-con">
                    <%@ include file="setting-policy.jsp" %>
                </div>
                --%>
                <div class="clearfix"></div>
                <div class="row">
                    <div class="col-sm-3 col-md-3 pr0 prxs12">
                        <%--手机设置--%>
                        <div class="thumbnail bitms-b bitms-bg1 mt10 bitms-con pb15">
                            <div class="caption">
                                <div class="bitms-bg2 bitms-height1 px10"><fmt:message key="account.setting.bindPhone"/></div>
                                <div class="col-md-1 col-sm-2 col-xs-1 mt15">
                                    <i class="iconfont text-success fs30">&#xe618;</i>
                                </div>
                                <div class="col-md-6 col-sm-10 col-xs-6 ml15 mt20 pt5 pr0">
                                    <c:choose>
                                        <c:when test="${account.mobNo == null}">
                                            <div class="bitms-c1 bindPhoneTxt1"><fmt:message key="account.setting.unbounded"/></div>
                                            <%--<div class="bindPhoneTxt2"><fmt:message key="account.setting.yet"/>&nbsp;<fmt:message key="account.setting.bindPhone"/></div>--%>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-success bindPhoneTxt1"><fmt:message key="account.setting.Bound"/>&nbsp;<span class="mobNoVal"></span></div>
                                            <%--<div class="bindPhoneTxt2"><fmt:message key="account.setting.BoundPhone"/><span class="mobNoVal"></span></div>--%>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                   <c:choose>
                                       <c:when test="${account.mobNo == null}">
                                           <button class="btn btn-primary col-sm-12 col-xs-3 col-md-3 mt20 bpBtn1 pull-right" data-toggle="modal" data-target=".bindPhone"><fmt:message key="account.setting.bind"/></button>
                                       </c:when>
                                       <c:otherwise>
                                           <button class="btn btn-primary col-sm-12 col-xs-3 col-md-3 mt20 bpBtn1 pull-right btn-dis" data-toggle="modal"><fmt:message key="account.setting.Bound"/></button>
                                           <button class="btn btn-primary col-sm-12 col-xs-3 col-md-3 mt20  bpBtn2 bitms-hide pull-right" data-toggle="modal" data-target=".changePhone"><fmt:message key="account.setting.changebind"/></button>
                                       </c:otherwise>
                                   </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3 col-md-3 pr0 prxs12">
                        <%--GA 设置--%>
                        <div class="thumbnail bitms-b bitms-bg1 mt10 bitms-con pb15">
                            <div class="caption">
                                <div class="bitms-bg2 bitms-height1 px10"><fmt:message key="account.setting.bindgoogle"/></div>
                                <div class="col-md-1 col-sm-2 col-xs-1 mt15">
                                    <i class="iconfont text-success fs30">&#xe61d;</i>
                                </div>
                                <div class="col-md-6 col-sm-10 col-xs-6 ml15 mt20 pt5">
                                    <c:choose>
                                        <c:when test="${account.authKey== null}">
                                            <div class="bitms-c1 bindGATxt1"><fmt:message key="account.setting.unbounded"/></div>
                                            <%--<div class="bindGATxt2"><fmt:message key="account.setting.yet"/><fmt:message key="account.setting.bind"/><fmt:message key="account.setting.ga"/></div>--%>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-success bindGATxt1"><fmt:message key="account.setting.Bound"/></div>
                                            <%--<div class="bindGATxt2"><fmt:message key="account.setting.BoundGA"/></div>--%>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <c:choose>
                                    <c:when test="${account.authKey == null}">
                                        <c:choose>
                                            <c:when test="${account.mobNo== null}">
                                                <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 bGABtn1 pull-right disabled" data-toggle="modal"><fmt:message key="account.setting.bind"/></button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 bGABtn1 pull-right" data-toggle="modal" data-target=".bindGA"><fmt:message key="account.setting.bind"/></button>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 bGABtn1 pull-right btn-dis" data-toggle="modal"><fmt:message key="account.setting.Bound"/></button>
                                        <button class="btn btn-primary col-sm-12 col-xs-3 col-md-3 mt20  bGABtn2 bitms-hide pull-right" data-toggle="modal" data-target=".unbindGA"><fmt:message key="account.setting.unbind"/></button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3 col-md-3 pr0 prxs12">
                        <%--登陆密码设置--%>
                        <div class="thumbnail bitms-b bitms-bg1 mt10 bitms-con pb15 mb15">
                            <div class="caption">
                                <div class="bitms-bg2 bitms-height1 px10">
                                    <fmt:message key="account.setting.login" />
                                </div>
                                <div class="col-md-1 col-sm-2 col-xs-1 mt15">
                                    <i class="iconfont text-success fs30">&#xe61b;</i>
                                </div>
                                <div class="col-md-6 col-sm-10 col-xs-6 ml15 mt20 pt5">
                                    <div class="text-success"><fmt:message key="account.setting.fundset"/></div>
                                    <%--<div><fmt:message key="account.setting.loginTxt"/></div>--%>
                                </div>
                                <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 pull-right" data-toggle="modal" data-target=".resetLoginPwd"><fmt:message key="account.setting.reset"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3 col-md-3 pr0 prxs12">
                        <%--资金密码--%>
                        <div class="thumbnail bitms-b bitms-bg1 mt10 bitms-con pb15 mb15">
                            <div class="caption">
                                <div class="bitms-bg2 bitms-height1 px10"><fmt:message key="account.setting.fund"/></div>
                                <div class="col-md-1 col-sm-2 col-xs-1 mt15">
                                    <i class="iconfont text-success fs30">&#xe61b;</i>
                                </div>
                                <div class="col-md-6 col-sm-10 col-xs-6 ml15 mt20 pt5">
                                    <c:choose>
                                        <c:when test="${account.walletPwd == null}">
                                            <div class="bitms-c1 capitalTxt"><fmt:message key="account.setting.noSet"/></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-success capitalTxt"><fmt:message key="account.setting.fundset"/></div>
                                        </c:otherwise>
                                    </c:choose>
                                    <%-- <div><fmt:message key="account.setting.fondTip"/></div>--%>
                                </div>
                                <c:choose>
                                    <c:when test="${account.walletPwd== null}">
                                        <c:choose>
                                            <c:when test="${account.mobNo== null}">
                                                <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 setCaptialBtn pull-right disabled" data-toggle="modal"><fmt:message key="account.setting.set"/></button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 setCaptialBtn pull-right" data-toggle="modal" data-target=".capitalPwd"><fmt:message key="account.setting.set"/></button>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-primary col-md-3 col-sm-12 col-xs-3 mt20 setCaptialBtn pull-right" data-toggle="modal" data-target=".capitalPwd"><fmt:message key="account.setting.reset"/></button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="clearfix"></div>
                <%--<div class="row">
                    <div class="col-sm-6 col-md-6 pr0 prxs12">
                        <%@ include file="setting-auth.jsp" %>
                        &lt;%&ndash;实名认证&ndash;%&gt;
                    </div>
                </div>--%>
            </div>
            <div class="panel col-md-12 pt5 pb0">
                <%@ include file="setting-history.jsp" %>
                <%--历史信息查询--%>
            </div>
        </div>
    </div>
    <%--所有弹出框合集--%>
    <%@ include file="setting-modal.jsp" %>
</div>
<script>
    <%--弹框关闭，表单清空--%>
    $('.pop-close').click(function(){
        $(this).parent().parent().find('form').find('input[type="text"]').val('');
        $(this).parent().parent().find('form').find('input[type="password"]').val('');
        $(this).parent().parent().find('form').find('select').val('');
        $(this).parent().parent().find('form').find('.phoneLocation').text('');
    });
    $('.safeStrategy .pop-close,.tradeStrategy .pop-close').click(function(){
        location.reload();
    });
    <%--安全验证策略联动效果，默认为登录密码--%>
    var safe ="${account.securityPolicy}";
    <%--获取安全验证策略，控制显示--%>
    var bindGa = "${account.authKey}";
    if(bindGa == "" || bindGa == null){
        safe = 1;
    }else{
        safe = 2;
    }
    if(safe == 0){
        var  safe0Txt= $('.safe0').parent().text();
        $('.safeTxt').text(safe0Txt);
        $('.safe1,.safe2,.safe3,.safe4').removeAttr('checked');
        $('.safe0').prop('checked',true);
        $('.safe0-con').removeClass('bitms-hide');
        $('.safe1-con,.safe2-con,.safe-select').addClass('bitms-hide');
    }else if(safe == 1){
        var  safe1Txt= $('.safe1').parent().text();
        $('.safeTxt').text(safe1Txt);
        $('.safe0,.safe2,.safe3,.safe4').removeAttr('checked');
        $('.safe1').prop('checked',true);
        $('.safe1-con').removeClass('bitms-hide');
        $('.safe0-con,.safe2-con,.safe-select').addClass('bitms-hide');
    }else if(safe == 2) {
        var  safe2Txt= $('.safe2').parent().text();
        $('.safeTxt').text(safe2Txt);
        $('.safe0,.safe1,.safe3,.safe4').removeAttr('checked');
        $('.safe2').prop('checked',true);
        $('.safe2-con').removeClass('bitms-hide');
        $('.safe0-con,.safe1-con,.safe-select').addClass('bitms-hide');
    }else if(safe == 4){
        var  safe4Txt= $('.safe4').parent().text();
        $('.safeTxt').text(safe4Txt);
        $('.safe0,.safe1,safe2,.safe3').removeAttr('checked');
        $('.safe4').prop('checked',true);
        $('.safe0-con,.safe-select').addClass('bitms-hide');
        $('.safe1-con,.safe2-con').removeClass('bitms-hide');
    }else{
        var  safe3Txt= $('.safe3').parent().text();
        $('.safeTxt').text(safe3Txt);
        $('.safe0,.safe1,safe2,.safe4').removeAttr('checked');
        $('.safe3').prop('checked',true);
        $('.safe0-con,.safe1-con').addClass('bitms-hide');
        $('.safe-select,.safe2-con').removeClass('bitms-hide');
        <%--下拉框默认值:GA绑定的时候为ga,反之为手机--%>
        if("${account.authKey}"== ""  || "${account.authKey}"== null){
            $(".checkType").val('sms');
            $('.safe1-con').removeClass('bitms-hide');
            $('.safe2-con').addClass('bitms-hide');
        }else{
            $(".checkType").val('ga');
            $('.safe1-con').addClass('bitms-hide');
            $('.safe2-con').removeClass('bitms-hide');
        }
        $(".checkType").on("click", function () {
            if ("sms" == $(this).val()) {
                $('.safe1-con').removeClass('bitms-hide');
                $('.safe2-con').addClass('bitms-hide');
            }else {
                <%--如果ga没绑定提示--%>
                if("${account.authKey}"== ""  || "${account.authKey}"== null){
                    $(this).val('sms');
                    remind(remindType.error,I18n.prop("setting.unbindGa"));
                }else{
                    $('.safe1-con').addClass('bitms-hide');
                    $('.safe2-con').removeClass('bitms-hide');
                }
            }
        });
    }
    var validator;
    <%--滑块验证--%>
    var nc = new noCaptcha();
    var nc_appkey = appKey;  <%--应用标识,不可更改--%>
    var nc_scene = 'message';  <%--场景,不可更改--%>
    var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
    seajs.use(['validator', 'i18n', 'qrcode','plupload'], function (Validator, I18n, qrcode) {
        $('.pageLoader').hide();
        <%--手机没绑定--%>
        if("${account.mobNo}"== ""  || "${account.mobNo}"== null){
            <%--点击绑定GA或资金密码提示绑定手机--%>
            $('.bGABtn1,.setCaptialBtn').click(function(){
                remind(remindType.normal, I18n.prop("setting.bindPhone"));
            })
        }else{
            <%--手机号码显示处理--%>
            var mobNo = "${account.mobNo}";
            $('.mobNoVal').text(mobNo);
        }
        validator = new Validator();
        <%--绑定手机--%>
        <%--显示绑定手机模块--%>
        $('.bpBtn1').click(function() {
            $("#sendSMS").attr("disabled",true);
            var nc_option = {
                renderTo: '#bindPhoneCaptcha',
                appkey: nc_appkey,
                scene: nc_scene,
                token: nc_token,
                callback: function (data) {
                    $("#sendSMS").attr("disabled",false);
                    $("#bindPhoneForm [name=sig]").val(data.sig);
                    $("#bindPhoneForm [name=token]").val(nc_token);
                    $("#bindPhoneForm [name=scene]").val(nc_scene);
                    $("#bindPhoneForm [name=csessionid]").val(data.csessionid);
                },
                language: locale || 'en_US',
                foreign: 0,
                isEnabled: true
            };
            nc.init(nc_option);
            <%--if (locale == "cn" || locale == "zh_CN" || locale == "zh_HK" || locale == "en_US") {*/
                //地区下拉框
               /* $("#bindPhoneForm [name = location]").val(86);
                $(".phoneLocation").text(86);*/
                $('select.selectLocation').change(function () {
                    $('.phoneLocation').text($(this).val());
                });
          /*  }--%>
        });
        <%--发送绑定手机短信--%>
        $("#sendSMS").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#bindPhoneForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        var location = $('#bindPhoneForm [name = location] option:selected').text();
                       confirmDialog("Phone attribution：" + location, function () {
                            $.ajax({
                                url: '/common/sendSms',
                                type: 'post',
                                data: element.serialize(),
                                dataType: 'json',
                                beforeSend: function () {
                                    $('.reg-pop').fadeIn();
                                },
                                success: function (data) {
                                   $('#bindPhoneForm').find('input[name="csrf"]').val(data.csrf);
                                    var nc_option = {
                                        renderTo: '#bindPhoneCaptcha',
                                        appkey: nc_appkey,
                                        scene: nc_scene,
                                        token: nc_token,
                                        callback: function (data) {// 校验成功回调
                                            $("#bindPhoneForm [name=sig]").val(data.sig);
                                            $("#bindPhoneForm [name=token]").val(nc_token);
                                            $("#bindPhoneForm [name=scene]").val(nc_scene);
                                            $("#bindPhoneForm [name=csessionid]").val(data.csessionid);
                                        },
                                        language: locale || 'en_US',
                                        foreign: 0,
                                        isEnabled: true
                                    };
                                    nc.init(nc_option);
                                    if (data.code == bitms.success) {
                                        $('.slider-group').hide();
                                        countDown($('#sendSMS'), I18n.prop('generic.sendsms'));
                                        remind(remindType.success, I18n.prop('setting.sendsuccess'));
                                    } else {
                                        if(data.code == bitms.codeError){
                                            remind(remindType.error, I18n.prop('validate.inputError'));
                                        }else if(data.code == bitms.smsSendError){
                                            remind(remindType.error,  I18n.prop('generic.sendsmsRepeat'));
                                        }else{
                                            remind(remindType.error, data.message);
                                        }
                                    }
                                }
                            });
                      });
                    }
                }
            }).addItem({
                element: '#bindPhoneForm [name=location]',
                required: true
            }).addItem({
                element: '#bindPhoneForm [name=phone]',
                required: true,
                rule: 'phone'
            })
            $("#bindPhoneForm").submit();
        });
        <%--绑定手机--%>
        $("#bindPhoneSub").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#bindPhoneForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/bindPhone',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                                $("#bindPhoneSub").addClass('btn-dis');
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#bindPhoneForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.bindsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    var nc_option = {
                                        renderTo: '#bindPhoneCaptcha',
                                        appkey: nc_appkey,
                                        scene: nc_scene,
                                        token: nc_token,
                                        callback: function (data) {// 校验成功回调
                                            $("#bindPhoneForm [name=sig]").val(data.sig);
                                            $("#bindPhoneForm [name=token]").val(nc_token);
                                            $("#bindPhoneForm [name=scene]").val(nc_scene);
                                            $("#bindPhoneForm [name=csessionid]").val(data.csessionid);
                                        },
                                        language: locale || 'en_US',
                                        foreign: 0,
                                        isEnabled: true
                                    };
                                    if(data.code == bitms.codeError){
                                        remind(remindType.error, I18n.prop('validate.inputError'));
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#bindPhoneSub").removeClass('btn-dis');
                            }
                        });
                    }
                }
            }).addItem({
                element: '#bindPhoneForm [name=location]',
                required: true
            }).addItem({
                element: '#bindPhoneForm [name=phone]',
                required: true,
                rule: 'phone'
            }).addItem({
                element: '#bindPhoneForm [name=vlidCode]',
                required: true,
                rule:'verificationCode'
            });
            $("#bindPhoneForm").submit();
        });

        <%--换绑手机--%>
        <%--显示换绑手机模块--%>
        <%--
        $('.bpBtn2').click(function(){
            var nc_option = {
                renderTo: '#changePhoneCaptcha',
                appkey: nc_appkey,
                scene: nc_scene,
                token: nc_token,
                callback: function (data) {// 校验成功回调
                    $("#changePhoneForm [name=sig]").val(data.sig);
                    $("#changePhoneForm [name=token]").val(nc_token);
                    $("#changePhoneForm [name=scene]").val(nc_scene);
                    $("#changePhoneForm [name=csessionid]").val(data.csessionid);
                },
                language: locale || 'en_US',
                foreign: 0,
                isEnabled: true
            };
            nc.init(nc_option) ;
            if (locale == "cn" || locale == "zh_CN" || locale == "zh_HK") {
                //地区下拉框
                $("#changePhoneForm [name = location]").val(86);
                $(".phoneLocation").text(86);
                $('select.selectLocation').change(function () {
                    $('.phoneLocation').text($(this).val());
                });
            }
        })
        --%>
        <%--换绑手机--发送手机短信--%>
     /*   $("#newSendSMS").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#changePhoneForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        var location = $('#changePhoneForm [name = location] option:selected').text();
                        confirmDialog("Phone attribution：" + location, function () {
                            $.ajax({
                                url: '/common/sendSms',
                                type: 'post',
                                data: element.serialize(),
                                dataType: 'json',
                                beforeSend: function () {
                                    $('.reg-pop').fadeIn();
                                },
                                success: function (data) {
                                    $('#changePhoneForm').find('input[name="csrf"]').val(data.csrf);
                                    var nc_option = {
                                        renderTo: '#changePhoneCaptcha',
                                        appkey: nc_appkey,
                                        scene: nc_scene,
                                        token: nc_token,
                                        callback: function (data) {// 校验成功回调
                                            $("#changePhoneForm [name=sig]").val(data.sig);
                                            $("#changePhoneForm [name=token]").val(nc_token);
                                            $("#changePhoneForm [name=scene]").val(nc_scene);
                                            $("#changePhoneForm [name=csessionid]").val(data.csessionid);
                                        },
                                        language: locale || 'en_US',
                                        foreign: 0,
                                        isEnabled: true
                                    };
                                    nc.init(nc_option);
                                    if (data.code == bitms.success) {
                                        countDown($('#newSendSMS'), I18n.prop('generic.sendsms'));
                                        remind(remindType.success, I18n.prop('setting.sendsuccess'));
                                    }else if(data.code == bitms.captchaError) {
                                        remind(remindType.error, I18n.prop("error.captchaCode"));
                                    }else {
                                        remind(remindType.error, data.message);
                                    }
                                }
                            });
                        });
                    }
                }
            }).addItem({
                element: '#changePhoneForm [name=phone]',
                required: true
            }).addItem({
                element: '#changePhoneForm [name=location]',
                required: true
            })
            $("#changePhoneForm").submit();
        });*/
        <%--换绑手机
        <%--
        $('#changePhoneSub').on("click", function () {
            var typeValue = $('#changePhoneForm  .checkType').val();
            validator.destroy();
            validator = new Validator({
                element: '#changePhoneForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/changeBindPhone',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#changePhoneForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.changebindsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    var nc_option = {
                                        renderTo: '#changePhoneCaptcha',
                                        appkey: nc_appkey,
                                        scene: nc_scene,
                                        token: nc_token,
                                        callback: function (data) {// 校验成功回调
                                            $("#changePhoneForm [name=sig]").val(data.sig);
                                            $("#changePhoneForm [name=token]").val(nc_token);
                                            $("#changePhoneForm [name=scene]").val(nc_scene);
                                            $("#changePhoneForm [name=csessionid]").val(data.csessionid);
                                        },
                                        language: locale || 'en_US',
                                        foreign: 0,
                                        isEnabled: true
                                    };
                                    nc.init(nc_option);
                                    if(data.code == bitms.codeError){
                                        if(safe==0){
                                            remind(remindType.error, I18n.prop("error.loginPwd"));
                                            $("#changePhoneForm input[name='pwd']").val("").focus();
                                        }else if(safe==1){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#changePhoneForm input[name='sms']").val("").focus();
                                        }else if(safe==2){
                                            remind(remindType.error, I18n.prop("error.gaCode"));
                                            $("#changePhoneForm input[name='ga']").val("").focus();
                                        }else if(safe==3){
                                            if(typeValue == 'sms'){
                                                remind(remindType.error, I18n.prop("error.sescode"));
                                                $("#changePhoneForm input[name='sms']").val("").focus();
                                            }else{
                                                remind(remindType.error, I18n.prop("error.gaCode"));
                                                $("#changePhoneForm input[name='ga']").val("").focus();
                                            }
                                        }else if(safe==4){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#changePhoneForm input[name='sms']").val("").focus();
                                        }else{
                                            remind(remindType.error, data.message);
                                        }
                                    }else if(data.code == bitms.captchaError){
                                        remind(remindType.error, I18n.prop("error.captchaCode"));
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            }
                        });
                    }
                }
            }).addItem({
                element: '#changePhoneForm [name=vlidCode]',
                required: true,
                rule:'verificationCode'
            }).addItem({
                element: '#changePhoneForm [name=phone]',
                required: true,
                rule: 'mobile'
            }).addItem({
                element: '#changePhoneForm [name=location]',
                required: true
            })
            if(safe==0){
                validator.addItem({
                    element: '#changePhoneForm [name=pwd]',
                    required: true,
                    rule:'password'
                })
            }else if(safe==1){
                validator.addItem({
                    element: '#changePhoneForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#changePhoneForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                })
            } else if(safe ==3){
                if(typeValue == 'sms'){
                    validator.addItem({
                        element: '#changePhoneForm [name=sms]',
                        required: true,
                        rule:'verificationCode'
                    })
                }else{
                    validator.addItem({
                        element: '#changePhoneForm [name=ga]',
                        required: true,
                        rule:'verificationCode'
                    })
                }
            } else if(safe == 4){
                validator.addItem({
                    element: '#changePhoneForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                }).addItem({
                    element: '#changePhoneForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }
            $('#changePhoneForm').submit();
        })
        --%>
        <%--设置安全验证策略--%>
        <%--
        $("#safeBtnSub").on("click", function () {
            var typeValue = $('#safeForm  .checkType').val();
            validator.destroy();
            validator = new Validator({
                element: '#safeForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/policy/security/save',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                            },
                            success: function (data) {
                                $('#safeForm').find('input[name="csrf"]').val(data.csrf);
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.setsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    if(data.code == bitms.codeError){
                                        if(safe==0){
                                            remind(remindType.error, I18n.prop("error.loginPwd"));
                                            $("#safeForm input[name='pwd']").val("").focus();
                                        }else if(safe==1){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#safeForm input[name='sms']").val("").focus();
                                        }else if(safe==2){
                                            remind(remindType.error, I18n.prop("error.gaCode"));
                                            $("#safeForm input[name='ga']").val("").focus();
                                        }else if(safe==3){
                                            if(typeValue == 'sms'){
                                                remind(remindType.error, I18n.prop("error.sescode"));
                                                $("#safeForm input[name='sms']").val("").focus();
                                            }else{
                                                remind(remindType.error,I18n.prop("error.gaCode"));
                                                $("#safeForm input[name='ga']").val("").focus();
                                            }
                                        }else if(safe==4){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#safeForm input[name='sms']").val("").focus();
                                        }else{
                                            remind(remindType.error, data.message);
                                        }
                                    }else if(data.code == bitms.gaError) {
                                        remind(remindType.error, data.message);
                                        $("#safeForm input[name='ga']").val("").focus();
                                    }else if(data.code == bitms.codeError){
                                        remind(remindType.error, I18n.prop('validate.inputError'));
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            }

                        });
                    }
                }
            }).addItem({
                element: '#safeForm [name=level]',
                required: true
            })
            if(safe==0){
                validator.addItem({
                    element: '#safeForm [name=pwd]',
                    required: true,
                    rule:'password'
                })
            }else if(safe==1){
                validator.addItem({
                    element: '#safeForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#safeForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                })
            } else if(safe ==3){
                if(typeValue == 'sms'){
                    validator.addItem({
                        element: '#safeForm [name=sms]',
                        required: true,
                        rule:'verificationCode'
                    })
                }else{
                    validator.addItem({
                        element: '#safeForm [name=ga]',
                        required: true,
                        rule:'verificationCode'
                    })
                }
            } else if(safe == 4){
                validator.addItem({
                    element: '#safeForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                }).addItem({
                    element: '#safeForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }
            $("#safeForm").submit();
        });
        --%>
        <%--安全验证策略  获取已绑定手机验证码--%>
        $('.safeSendSms').on('click', function () {
            $.ajax({
                url: '/common/bind/sendSms',
                type: 'post',
                dataType: 'json',
                beforeSend: function () {
                    $('.reg-pop').fadeIn();
                },
                success: function (data) {
                    if (data.code == bitms.success) {
                        countDown($('.safeSendSms'), I18n.prop('generic.sendsms'));
                        remind(remindType.success, I18n.prop('setting.sendsuccess'));
                    } else if(data.code == bitms.smsSendError){
                        remind(remindType.error,  I18n.prop('generic.sendsmsRepeat'));
                    }else {
                        remind(remindType.error, data.message);
                    }
                }
            });
        });
        <%--设置交易验证策略--%>
        <%--
        $("#tradeBtnSub").on("click", function () {
            var typeValue = $('#tradeForm  .checkType').val();
            validator.destroy();
            validator = new Validator({
                element: '#tradeForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/policy/trade/save',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                            },
                            success: function (data) {
                                $('#tradeForm').find('input[name="csrf"]').val(data.csrf);
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.setsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    if(data.code == bitms.codeError){
                                        if(safe==0){
                                            remind(remindType.error, I18n.prop("error.loginPwd"));
                                            $("#tradeForm input[name='pwd']").val("").focus();
                                        }else if(safe==1){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#tradeForm input[name='sms']").val("").focus();
                                        }else if(safe==2){
                                            remind(remindType.error, I18n.prop("error.gaCode"));
                                            $("#tradeForm input[name='ga']").val("").focus();
                                        }else if(safe==3){
                                            if(typeValue == 'sms'){
                                                remind(remindType.error, I18n.prop("error.sescode"));
                                                $("#tradeForm input[name='sms']").val("").focus();
                                            }else{
                                                remind(remindType.error, I18n.prop("error.gaCode"));
                                                $("#tradeForm input[name='ga']").val("").focus();
                                            }
                                        }else if(safe==4){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#tradeForm input[name='sms']").val("").focus();
                                        }else{
                                            remind(remindType.error, data.message);
                                        }
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            }

                        });
                    }
                }
            }).addItem({
                element: '#tradeForm [name=level]',
                required: true
            })
            if(safe==0){
                validator.addItem({
                    element: '#tradeForm [name=pwd]',
                    required: true,
                    rule:'password'
                })
            }else if(safe==1){
                validator.addItem({
                    element: '#tradeForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#tradeForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                })
            } else if(safe ==3){
                if(typeValue == 'sms'){
                    validator.addItem({
                        element: '#tradeForm [name=sms]',
                        required: true,
                        rule:'verificationCode'
                    })
                }else{
                    validator.addItem({
                        element: '#tradeForm [name=ga]',
                        required: true,
                        rule:'verificationCode'
                    })
                }
            } else if(safe == 4){
                validator.addItem({
                    element: '#tradeForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                }).addItem({
                    element: '#tradeForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }
            $("#tradeForm").submit();
        });
        --%>
        <%--绑定GA--%>
        <%--获取GAKEY--%>
        $(".bGABtn1").on("click", function () {
            $.ajax({
                url: '/common/buildGAKey',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data.code == bitms.success) {
                        var json = data.object;
                        var secretKey = json.secretKey;
                        $('#GAkey,#GAkeySave').val(secretKey);
                        $('.gaKeyLabel').text(secretKey);
                        $("#qrcode_img").html("");
                        $("#qrcode_img").append(new qrcode({
                            render: "svg",
                            width: 150, //宽度
                            height: 150, //高度
                            text: json.gaInfo
                        }));
                        $("#qrcode_img").addClass('gaCodeImg');
                    } else {
                        remind(remindType.error, data.message);
                    }
                }
            });
        });

        <%--绑定GA下一步--%>
        $(".bingGANext").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element:'#bindGAForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/bindGoogleGA',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                               /* $(".bingGANext").addClass('btn-dis');*/
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#bindGAForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.bindsuccess'),2000,function(){
                                        location.reload();
                                    });
                                   /* $('.bingGa-con1').hide();
                                    $('.bingGa-con2').show();*/
                                } else {
                                    remind(remindType.error, data.message);
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                               /* $(".bingGANext").removeClass('btn-dis');*/
                            }
                        });
                    }
                }
            }).addItem({
                element: '#bindGAForm [name=gaCode]',
                required: true,
                rule:'verificationCode'
            }).addItem({
                element: '#bindGAForm [name=validCode]',
                required: true,
                rule:'verificationCode'
            }).addItem({
                element: '#bindGAForm [name=secretKey]',
                required: true
            });
            $("#bindGAForm").submit();
        });
        <%--//确定保存GA
        /!*$('#gaSaveBtn').click(function(){
            $('.bingGa-con2').hide();
            $('.bingGa-con3').show();
        })*!/
        //关闭保存GA弹框
        /!*$('.bingGa-con2 .saveClose').click(function(){
            $('.bingGa-con2').hide();
            location.reload();
        })*!/
        //下一步弹框关闭
        /!*$('.bingGa-con3 .close').click(function(){
            location.reload();
        })*!/

        //返回上一步
      /!*  $('#bindGALast').click(function(){
            $('.bingGa-con3').hide();
            $('.bingGa-con2').show();
        })*!/
        //绑定GA
       /!* $("#bindGASub").on("click", function () {
            //获取输入的ga密钥值
            var valStr = '';
            $('#gakeyEnter input').each(function(){
                valStr += $(this).val();
            })
            $('#secretKey').val(valStr);
            var key1 = $('#GAkey').val();
            $('#GAkeyVal').val(key1);
            validator.destroy();
            validator = new Validator({
                element:'#bindGAFormConfirm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/bindGoogle/confirm',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                                $("#bindGASub").removeClass('btn-dis');
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#bindGAFormConfirm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.bindsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    remind(remindType.error, data.message);
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#bindGASub").removeClass('btn-dis');
                            }
                        });
                    }
                }
            }).addItem({
                element: '#bindGAFormConfirm [name=secretKey]',
                required: true,
                rule: 'confirmation{target: "#GAkeyVal"}'
            });
            $("#bindGAFormConfirm").submit();
        });*!/
        //解绑ga
        <%--
        $('#unbindGASub').on("click", function () {
            var typeValue = $('#unbindGAForm  .checkType').val();
            validator.destroy();
            validator = new Validator({
                element: '#unbindGAForm',
                autoSubmit: false,
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/unBindGoogle',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#unbindGAForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.unbindsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    if(data.code == bitms.codeError){
                                        if(safe==0){
                                            remind(remindType.error,I18n.prop("error.loginPwd"));
                                            $("#unbindGAForm input[name='pwd']").val("").focus();
                                        }else if(safe==1){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#unbindGAForm input[name='sms']").val("").focus();
                                        }else if(safe==2){
                                            remind(remindType.error, I18n.prop("error.gaCode"));
                                            $("#unbindGAForm input[name='ga']").val("").focus();
                                        }else if(safe==3){
                                            if(typeValue == 'sms'){
                                                remind(remindType.error, I18n.prop("error.sescode"));
                                                $("#unbindGAForm input[name='sms']").val("").focus();
                                            }else{
                                                remind(remindType.error, I18n.prop("error.gaCode"));
                                                $("#unbindGAForm input[name='ga']").val("").focus();
                                            }
                                        }else if(safe==4){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#unbindGAForm input[name='sms']").val("").focus();
                                        }else{
                                            remind(remindType.error, data.message);
                                        }
                                    }else if(data.code == bitms.gaError){
                                        remind(remindType.error, data.message);
                                        $("#unbindGAForm input[name='ga']").val("").focus();
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            }
                        });
                    }
                }
            })
            /!*.addItem({
            element: '#unbindGAForm [name=gaCode]',
            required: true
        });*!/
            if(safe==0){
                validator.addItem({
                    element: '#unbindGAForm [name=pwd]',
                    required: true,
                    rule:'password'
                })
            }else if(safe==1){
                validator.addItem({
                    element: '#unbindGAForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#unbindGAForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                })
            } else if(safe ==3){
                if(typeValue == 'sms'){
                    validator.addItem({
                        element: '#unbindGAForm [name=sms]',
                        required: true,
                        rule:'verificationCode'
                    })
                }else{
                    validator.addItem({
                        element: '#unbindGAForm [name=ga]',
                        required: true,
                        rule:'verificationCode'
                    })
                }
            } else if(safe == 4){
                validator.addItem({
                    element: '#unbindGAForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                }).addItem({
                    element: '#unbindGAForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }
            $('#unbindGAForm').submit();
        })
        --%>
        <%--重置登录密码--%>
        $("#resetLoginSub").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#resetLoginForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/changeLoginPwd',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                                $("#resetLoginSub").addClass('btn-dis');
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#resetLoginForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.modifypwdSuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    if(data.code == bitms.codeError){
                                        remind(remindType.error, I18n.prop('validate.inputError'));
                                    }else{
                                        remind(remindType.error, data.message);
                                    }
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#resetLoginSub").removeClass('btn-dis');
                            }
                        });
                    }
                }
            }).addItem({
                element: '#resetLoginForm [name=origPass]',
                required: true
            }).addItem({
                element: '#resetLoginForm [name=newPass]',
                required: true,
                rule: 'different{target: "#origPass"},password'
            }).addItem({
                element: '[name=newrePass]',
                required: true,
                rule: 'confirmation{target: "#loginNewPass"}'
            });
            $("#resetLoginForm").submit();
        });
        <%--设置/重置资金密码--%>
        $("#setCapticalSub").on("click", function () {
            var typeValue = $('#setCapticalForm  .checkType').val();
            validator.destroy();
            validator = new Validator({
                element: '#setCapticalForm',
                autoSubmit: false,//当验证通过后不自动提交
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '/account/setting/changeFundPwd',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend: function () {
                                $('.reg-pop').fadeIn();
                                $("#setCapticalSub").addClass('btn-dis');
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#setCapticalForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success, I18n.prop('setting.setsuccess'),2000,function(){
                                        location.reload();
                                    });
                                } else {
                                    if(data.code == bitms.codeError){
                                        if(safe==0){
                                            remind(remindType.error, I18n.prop("error.loginPwd"));
                                            $("#setCapticalForm input[name='pwd']").val("").focus();
                                        }else if(safe==1){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#setCapticalForm input[name='sms']").val("").focus();
                                        }else if(safe==2){
                                            remind(remindType.error, I18n.prop("error.gaCode"));
                                            $("#setCapticalForm input[name='ga']").val("").focus();
                                        }else if(safe==3){
                                            if(typeValue == 'sms'){
                                                remind(remindType.error, I18n.prop("error.sescode"));
                                                $("#setCapticalForm input[name='sms']").val("").focus();
                                            }else{
                                                remind(remindType.error,I18n.prop("error.gaCode"));
                                                $("#setCapticalForm input[name='ga']").val("").focus();
                                            }
                                        }else if(safe==4){
                                            remind(remindType.error, I18n.prop("error.sescode"));
                                            $("#setCapticalForm input[name='sms']").val("").focus();
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
                                $("#setCapticalSub").removeClass('btn-dis');
                            }
                        });
                    }
                }
            }).addItem({
                element: '#setCapticalForm [name=fundPwd]',
                required: true,
                rule: 'fundPwd'
            }).addItem({
                element: '#setCapticalForm [name="fondrePass"]',
                required: true,
                rule: 'confirmation{target: "#fundPwd"}'
            });
            if(safe==0){
                validator.addItem({
                    element: '#setCapticalForm [name=pwd]',
                    required: true,
                    rule:'password'
                })
            }else if(safe==1){
                validator.addItem({
                    element: '#setCapticalForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }else if(safe==2){
                validator.addItem({
                    element: '#setCapticalForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                })
            } else if(safe ==3){
                if(typeValue == 'sms'){
                    validator.addItem({
                        element: '#setCapticalForm [name=sms]',
                        required: true,
                        rule:'verificationCode'
                    })
                }else{
                    validator.addItem({
                        element: '#setCapticalForm [name=ga]',
                        required: true,
                        rule:'verificationCode'
                    })
                }
            } else if(safe == 4){
                validator.addItem({
                    element: '#setCapticalForm [name=ga]',
                    required: true,
                    rule:'verificationCode'
                }).addItem({
                    element: '#setCapticalForm [name=sms]',
                    required: true,
                    rule:'verificationCode'
                })
            }
            $("#setCapticalForm").submit();
        });
        <%--输入ga密钥--%>
        <%--var txts =gakeyEnter.getElementsByTagName("input");
        for(var i = 0; i<txts.length;i++){
            var t = txts[i];
            t.index = i;
            t.setAttribute("readonly", true);
            t.onkeyup=function(e){
                if (e.keyCode == 8) {
                    this.value=this.value.replace(/^(.).*$/,'$1');
                    var before = this.index - 1;
                    if(before > txts.length +1) return;
                    txts[before].removeAttribute("readonly");
                    txts[before].focus();
                }else{
                    var textStr = /^[a-zA-Z\d]+$/;
                    this.value=this.value.replace(/^(.).*$/,'$1');
                    if(textStr.test(this.value)){
                        var next = this.index + 1;
                        if(next > txts.length - 1) return;
                        txts[next].removeAttribute("readonly");
                        txts[next].focus();
                    }else{
                        this.value = this.value.replace(/\D/g,'');
                    }
                }
            }
        }
        txts[0].removeAttribute("readonly");--%>
    });
    <%--input光标位置--%>
    $(function(){
        $('.bindPhone,.resetLoginPwd,.capitalPwd,.bindGA').on('shown.bs.modal', function () {
            $('body').css({'position': 'fixed', 'width': '100%'});
        })
        $('.bindPhone,.resetLoginPwd,.capitalPwd,.bindGA').on('hidden.bs.modal', function () {
            $('body').css({'position': 'relative'});
        })
    })
</script>
</body>
</html>
