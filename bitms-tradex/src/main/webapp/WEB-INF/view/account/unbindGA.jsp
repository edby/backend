<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<c:if test="${empty account.authKey}">
    <script>
        location.href="${ctx}/bindGA";
    </script>
</c:if>
<c:if test="${!empty account.authKey}">
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Google Auth</h2>
            <hr>
            <form:form class="form-horizontal" data-widget="validator" id="clearGAForm">
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">${account.email}</span>
                    </div>
                </div>
                <div class="form-group">
                    <p>Your google Authenticator is enable.</p>
                    <p>To disable,please enter the GA code.</p>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-time" style="top:0;"></span>
                        <input type="text" class="form-control" name="gaCode" placeholder="GA code">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block clearGASub">Submit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script>
    seajs.use(['validator', 'i18n', 'qrcode'], function (Validator, I18n, qrcode) {
        //解绑GA
        validator = new Validator({
            element:'#clearGAForm',
            autoSubmit: false,//当验证通过后不自动提交
            onFormValidated: function (error, results, element) {
                if (!error) {
                    $.ajax({
                        url: '/setting/unBindGoogle',
                        type: 'post',
                        data: element.serialize(),
                        dataType: 'json',
                        beforeSend: function () {
                            $('.reg-pop').fadeIn();
                            $(".clearGASub").addClass('btn-dis');
                        },
                        success: function (data, textStatus, jqXHR) {
                            $('#clearGAForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                            if (data.code == bitms.success) {
                                remind(remindType.success, I18n.prop('setting.bindsuccess'),1000,function(){
                                    jumpUrl("/exchange");
                                });
                            } else {
                                remind(remindType.error, data.message);
                            }
                        },
                        complete:function(){
                            $('.reg-pop').hide();
                            $(".clearGASub").removeClass('btn-dis');
                        }
                    });
                }
            }
        }).addItem({
            element: '#clearGAForm [name=gaCode]',
            required: true,
            rule:'verificationCode'
        })
        $(".clearGASub").on("click", function () {
            $("#clearGAForm").submit();
        });
    });
</script>
</body>
</c:if>
</html>