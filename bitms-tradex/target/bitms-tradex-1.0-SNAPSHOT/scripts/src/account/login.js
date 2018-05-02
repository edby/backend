var validator;
var nc = new noCaptcha();
var nc_appkey = appKey;
var nc_scene = 'login';
var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
seajs.use(['validator'], function (Validator) {
    var nc_option = {
        renderTo: '#captcha',
        appkey: nc_appkey,
        scene: nc_scene,
        token: nc_token,
        callback: function (data) {
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
    validator = new Validator({
        element: '#loginForm',
        autoSubmit: false,
        onFormValidated: function (error, results, element) {
            if (!error) {
                $.ajax({
                    url: '/login/submit',
                    type: 'post',
                    data: $('#loginForm').serialize(),
                    dataType: 'json',
                    beforeSend:function(){
                        $('.reg-pop').fadeIn();
                        $("#loginBtn").addClass('btn-dis');
                    },
                    success: function (data, textStatus, jqXHR) {
                        $('#loginForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                        if (data.code == bitms.success) {
                                jumpUrl("/wallet");
                        }else if(data.code == bitms.pwdError) {
                            remind(remindType.error, data.message);
                            $("#loginPwd").focus();
                            $('#loginPwd').val('');
                            if (data.object) {
                                $("#sig").val('');
                                $("#token").val('');
                                $("#scene").val('');
                                $("#csessionid").val('');
                                $('#loginBtn').attr('disabled', 'disabled');
                                nc.init(nc_option);
                            }
                        }else if(data.code == bitms.codeError ) {
                            remind(remindType.error, "Please enter gaCode");
                            $("#gaCode").focus();
                            $('#gaCode').val('');
                        }else if(data.code == 9999){
                            remind(remindType.error,data.message);
                            $("#gaCode").focus();
                        }else{
                            remind(remindType.error, data.message);
                        }
                    },
                    complete:function(){
                        $('.reg-pop').hide();
                        $("#loginBtn").removeClass('btn-dis');
                    }
                });
            }
        }
    }).addItem({
        element: '[name=username]',
        required: true,
        rule: 'email'
    }).addItem({
        element: '[name=password]',
        required: true
    });
    $("#loginBtn").on("click", function () {
        $("#loginForm").submit();
    });
});



