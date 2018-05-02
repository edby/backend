//表单提交
var validator;
var nc = new noCaptcha();
var nc_appkey = appKey;  // 应用标识,不可更改
var nc_scene = 'login';  //场景,不可更改
var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
seajs.use(['validator',], function (Validator) {
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
    validator = new Validator({
        element: '#loginForm',
        autoSubmit: false,// 当验证通过后不自动提交
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
                    success: function (data) {
                        $('#loginForm').find('input[name="csrf"]').val(data.csrf);
                        if (data.code == bitms.success) {
                            jumpUrl("/dispatch");
                        } else if (data.code == bitms.gaValid) {
                            jumpUrl("/login/check");
                        } else {
                            remind(remindType.error, data.message);
                            $('#loginPwd').val('');
                            if (data.object) {
                                $("#sig").val('');
                                $("#token").val('');
                                $("#scene").val('');
                                $("#csessionid").val('');
                                $('#loginBtn').attr('disabled', 'disabled');
                                nc.init(nc_option);
                            }
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



