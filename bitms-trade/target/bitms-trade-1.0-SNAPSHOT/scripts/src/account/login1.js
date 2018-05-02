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
                    },
                    success: function (data) {
                        $('#loginForm').find('input[name="csrf"]').val(data.csrf);
                        if (data.code == bitms.success) {
                        	$('.reg-pop').fadeOut();
                            jumpUrl("/spot/C2CTrade");
                        } else if (data.code == bitms.gaValid) {
                        	$('.reg-pop').fadeOut();
                            jumpUrl("/login/check");
                        } else {
                        	$('.reg-pop').fadeOut();
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
    $('.lm-input').keypress(function () {
        $(this).next('.lmc-img2').show();
    })

    $('.lmc-img2').click(function () {
        $(this).prev('input').val('');
        $(this).hide();
    })
});

function KeyDown() {
    if (event.keyCode == 13) {
        event.returnValue = false;
        event.cancel = true;
        $('#loginBtn').click();
    }
}

