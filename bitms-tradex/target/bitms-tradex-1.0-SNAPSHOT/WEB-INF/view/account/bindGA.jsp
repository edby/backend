<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<c:if test="${empty account.authKey}">
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Google Auth</h2>
            <hr>
            <form:form class="form-horizontal" data-widget="validator" id="bindGAForm">
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">${gaMap.email}</span>
                    </div>
                </div>
                <div class="form-group" style="margin-bottom:0;">
                    <div class="col-sm-offset-2 col-sm-3 col-xs-10 col-xs-offset-1" style="text-align:left;">
                        <div id="qrcode_img" class="phone-gaCodeImg"></div>
                    </div>
                    <div class="col-sm-5 col-xs-10 col-xs-offset-1" style="text-align:left;">
                        <p>Your google Authenticator is</p>
                        <h4>${gaMap.secretKey}</h4>
                        <input type="hidden" id="GAkey" name="secretKey">
                        <p>Be sure to backup this code!If you lose it,we can not unlock your account.</p>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-time" style="top:0;"></span>
                        <input type="text" name="gaCode" class="form-control" placeholder='GA code' data-display='GA code'/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block bingGASub">Submit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script>
    seajs.use(['validator', 'i18n', 'qrcode', 'plupload'], function (Validator, I18n, qrcode) {
        //ga二维码
        $("#qrcode_img").html("");
        $("#qrcode_img").append(new qrcode({
            render: "svg",
            width: 160, //宽度
            height: 160, //高度
            text: '${gaMap.gaInfo}'
        }));
        //ga秘钥
        $('#GAkey').val("${gaMap.secretKey}");
        //绑定GA
        validator = new Validator({
            element: '#bindGAForm',
            autoSubmit: false,//当验证通过后不自动提交
            onFormValidated: function (error, results, element) {
                if (!error) {
                    $.ajax({
                        url: '/setting/bindGoogleGA',
                        type: 'post',
                        data: element.serialize(),
                        dataType: 'json',
                        beforeSend: function () {
                            $('.reg-pop').fadeIn();
                            $(".bingGASub").addClass('btn-dis');
                        },
                        success: function (data, textStatus, jqXHR) {
                            $('#bindGAForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                            if (data.code == bitms.success) {
                                remind(remindType.success, I18n.prop('setting.bindsuccess'), 1000, function () {
                                    jumpUrl("/exchange");
                                });
                            } else {
                                remind(remindType.error, data.message);
                            }
                        },
                        complete: function () {
                            $('.reg-pop').hide();
                            $(".bingGASub").removeClass('btn-dis');
                        }
                    });
                }
            }
        }).addItem({
            element: '#bindGAForm [name=gaCode]',
            required: true,
            rule: 'verificationCode'
        }).addItem({
            element: '#bindGAForm [name=secretKey]',
            required: true
        });
        $(".bingGASub").on("click", function () {
            $("#bindGAForm").submit();
        });
    });
</script>
</body>
</c:if>
<c:if test="${!empty account.authKey}">
    <script>
        location.href="${ctx}/unbindGA";
    </script>
</c:if>
</html>