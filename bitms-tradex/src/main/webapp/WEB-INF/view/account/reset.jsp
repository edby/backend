<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/global/header.jsp" %>
<body>
<div style="padding:15px;padding-top:0;">
    <div class="row">
        <%@ include file="/global/topnav.jsp" %>
        <div class="text-center loginCon">
            <h2 class="loginTit">Reset Password</h2>
            <hr>
           <form:form class="form-horizontal" data-widget="validator" id="changepwdForm">
               <input type="hidden" name="unid" value="${unid}">
               <input type="hidden" name="oid" value="${oid}">
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input type="password"  data-rule="password" class="form-control" id="setNewPwd" name="loginPwd"  placeholder="Your Password" data-display="Password" >
                    </div>
                </div>
                <div class="form-group ui-form-item">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-lock" style="top:0;"></span>
                        <input type="password" data-rule="password" class="form-control" onkeydown="KeyDown($('#changePwdSubmit'));" id="repeatPwd" placeholder="Confirm password" name="reloginpwd" data-display="Confirm password" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 input-group">
                        <button type="button" class="btn btn-primary btn-block" id="changePwdSubmit" name="changePwdSubmit">Submit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/global/footer.jsp" %>
<script type="text/javascript">
    baseFormValidator({
        selector: "#changepwdForm",
        isAjax: true
    });
    var validator;
    seajs.use(['validator','i18n'], function (Validator,I18n) {
        validator = new Validator();
        $("#changePwdSubmit").on("click", function () {
            validator.destroy();
            validator = new Validator({
                element: '#changepwdForm',
                autoSubmit: false,<%--当验证通过后不自动提交--%>
                onFormValidated: function (error, results, element) {
                    if (!error) {
                        $.ajax({
                            url: '${ctx}/resetPass/savePass',
                            type: 'post',
                            data: element.serialize(),
                            dataType: 'json',
                            beforeSend:function(){
                                $('.reg-pop').fadeIn();
                                $("#changePwdSubmit").attr("disabled",true);
                            },
                            success: function (data, textStatus, jqXHR) {
                                $('#changepwdForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                                if (data.code == bitms.success) {
                                    remind(remindType.success,"Success",1000,function() {
                                        jumpUrl("${ctx}/login");
                                    });
                                } else {
                                    remind(remindType.error, data.message);
                                }
                            },
                            complete:function(){
                                $('.reg-pop').hide();
                                $("#changePwdSubmit").attr("disabled",false);
                            }
                        });
                    }
                }
            }).addItem({
                element: '[name=reloginpwd]',
                required: true,
                rule: 'confirmation{target: "#setNewPwd"}'
            });
            $("#changepwdForm").submit();
        })
    });
</script>
</body>
</html>