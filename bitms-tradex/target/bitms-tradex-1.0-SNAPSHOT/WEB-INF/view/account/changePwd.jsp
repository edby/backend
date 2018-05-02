<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Change Password</h2>
            <hr>
            <form:form data-widget="validator" class="form-horizontal" id="resetLoginForm">
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-envelope" style="top:0;"></span>
                        <span class="form-control" style="background-color:#eee;cursor:no-drop;">${account.accountName}</span>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input type="password"   class="form-control" name="origPass" id="origPass" placeholder='Old Password' data-display='The current password'>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input type="password" data-rule="password" class="form-control" name="newPass" id="loginNewPass" placeholder='New Password'  data-display='The new password'>
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input type="password" data-rule="password" name="newrePass" onkeydown="KeyDown($('#resetLoginSub'));" class="form-control" placeholder='Comfirm New Password'  data-display='Confirm password'>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block" id="resetLoginSub">Submit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script>
    <%--重置登录密码--%>
    var validator;
    seajs.use(['validator','i18n'], function (Validator,I18n) {
            validator = new Validator({
            element: '#resetLoginForm',
            autoSubmit: false,//当验证通过后不自动提交
            onFormValidated: function (error, results, element) {
                if (!error) {
                    $.ajax({
                        url: '/setting/changeLoginPwd',
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
                                    remind(remindType.success, I18n.prop('setting.modifypwdSuccess'),1000,function(){
                                        jumpUrl("/login");
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
        $("#resetLoginSub").on("click", function () {
            $("#resetLoginForm").submit();
        });
    });
</script>
</body>
</html>