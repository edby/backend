var validator;
seajs.use(['validator','i18n'], function (Validator,I18n) {
   $("#sendEmail").on("click", function () {
       if(validator){
           validator.destroy();
       }
       validator = new Validator({
           element: '#regForm',
           autoSubmit: false,
           onFormValidated: function (error, results, element) {
               if (!error) {
                   $.ajax({
                       url: '/register/sendMail',
                       type: 'post',
                       timeout:6000,
                       data: $('#regForm').serialize(),
                       dataType: 'json',
                       beforeSend:function(){
                           $('.reg-pop').fadeIn();
                           $('#sendEmail').attr("disabled",true);
                       },
                       success: function (data, textStatus, jqXHR) {
                           nc.init(nc_option);
                           $('#regForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                           if (data.code == bitms.success) {
                               countDown($('#sendEmail'),"Email code");
                               $('.slider-group').hide();
                               remind(remindType.success,"Success",1000);
                           } else if(data.code == bitms.captchaError) {
                               remind(remindType.error, data.message);
                           } else if(data.code == bitms.emailExists) {
                               remind(remindType.error,data.message);
                               $('#email').val("").focus();
                               nc.init(nc_option);
                           }else{
                               remind(remindType.error,data.message);
                           }
                           $('#sendEmail').attr("disabled",true);
                       },
                       complete:function(){
                           $('.reg-pop').hide();
                           $('#sendEmail').attr("disabled",false);
                       }
                   });
               }
           }
       }).addItem({
           element: '[name=email]',
           required: true,
           rule: 'registerEmail'
       });
       $("#regForm").submit();
   });

   $('#sendEmail,#regSubmit').attr("disabled",true);
   var nc = new noCaptcha();
   var nc_appkey = appKey;
   var nc_scene = 'register';
   var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
   var nc_option = {
       renderTo: '#captcha',
       appkey: nc_appkey,
       scene: nc_scene,
       token: nc_token,
       callback: function (data) {
           $('#sendEmail').attr("disabled",false);
           $("#sig").val(data.sig);
           $("#token").val(nc_token);
           $("#scene").val(nc_scene);
           $("#csessionid").val(data.csessionid);
           if($("input[name='agreeChk']").prop('checked') == true){
               $('#regSubmit').attr("disabled",false);
           } else{
               $('#regSubmit').attr("disabled",true);
           }
       },
       language: locale || 'en_US',
       foreign: 0,
       isEnabled: true
   };
   nc.init(nc_option);

  //注册提交
    $("#regSubmit").on("click", function () {
        if(validator){
            validator.destroy();
        }
        validator = new Validator({
            element: '#regForm',
            autoSubmit: false,
            onFormValidated: function (error, results, element) {
                if (!error) {
                    $.ajax({
                        url: '/register/submit',
                        type: 'post',
                        timeout:6000,
                        data: $('#regForm').serialize(),
                        dataType: 'json',
                        beforeSend:function(){
                            $('.reg-pop').fadeIn();
                            $("#regSubmit").attr("disabled",true);
                        },
                        success: function (data, textStatus, jqXHR) {
                            $('#regForm').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                            if (data.code == bitms.success) {
                                remind(remindType.success,"Success",1000,function() {
                                    jumpUrl("${ctx}/login");
                                });
                            }else if(data.code == bitms.emailCodeError){
                                   remind(remindType.error,"Email code error");
                            }else{
                                remind(remindType.error,data.message);
                            }
                        },
                        complete:function(){
                            $('.reg-pop').hide();
                            $("#regSubmit").attr("disabled",false);
                        }
                    });
                }
            }
        }).addItem({
            element:'[name=emailCode]',
            required:true
        }).addItem({
            element: '[name=loginPwd]',
            required: true,
            rule: 'password'
        }).addItem({
            element: '[name=confirmPwd]',
            required: true,
            rule: 'confirmation{target: "#loginPwd"}'
        }).addItem({
            element: '[name=agreeChk]',
            required: true
        });
        $("#regForm").submit();
    });

   $("input[name='agreeChk']").change(function () {
        if($("input[name='agreeChk']").prop('checked') == true && $("#sig").val() != ''){
            $('#regSubmit').attr("disabled",false);
        }
        else{
            $('#regSubmit').attr("disabled",true);
        }
});
});
function KeyDown() {
    var theEvent = window.event || arguments.callee.caller.arguments[0];
    var code = theEvent.keyCode;
    if (code== 13) {
        theEvent.returnValue = false;
        theEvent.cancel = true;
        $('#agreeChk').focus();
    }
}

  