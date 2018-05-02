var validator;
   seajs.use(['validator','i18n'], function (Validator,I18n) {
       $('.pageLoader').hide();
       validator = new Validator({
           element: '#reg-step1',
           autoSubmit: false,// 当验证通过后不自动提交
           onFormValidated: function (error, results, element) {
               if (!error) {
                   $.ajax({
                       url: '/register/submit',
                       type: 'post',
                       timeout:6000,
                       data: $('#reg-step1').serialize(),
                       dataType: 'json',
                       beforeSend:function(){
                           $('.reg-pop').fadeIn();
                           $("#regSubmit1").addClass('btn-dis');
                       },
                       success: function (data, textStatus, jqXHR) {
                           $('#reg-step1').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                           if (data.code == bitms.success) {
                               sendDown($('#regSubmit1'), I18n.prop('register.submit'),I18n.prop("register.submitted"));
                               var emailHost = $('#email').val();
                               var reg = /@(\w)+((\.\w+)+)$/;
                               if(reg.test(emailHost)){
                                   var m = emailHost.match(reg);
                                   var str = m[0].substr(1);
                               }
                               var mailStr = "http://mail."+str;
                               $('#reg-step1').addClass('hidden');
                               $('#reg-step2').removeClass('hidden');
                               $('#emaliHost').attr('href',mailStr).text(emailHost);
                               $('#emailStr').attr('href',mailStr);
                           } else if(data.code == bitms.captchaError) {
                               remind(remindType.error,I18n.prop("error.verificationCode"));
                           } else if(data.code == bitms.emailExists) {
                               remind(remindType.error,data.message);
                               $('#email').val("").focus();
                               nc.init(nc_option);
                           }else{
                               remind(remindType.error,data.message);
                           }

                       },
                       complete:function(){
                           $('.reg-pop').hide();
                           $("#regSubmit1").removeClass('btn-dis');
                       }
                   });
               }
           }
       }).addItem({
           element: '[name=email]',
           required: true,
           rule: 'email'
       }).addItem({
           element: '[name=agreeChk]',
           required: true
       })
       $("#regSubmit1").on("click", function () {
           $("#reg-step1").submit();
       });
       //重新发送
       $('#reload').click(function(){
    	   location.reload();
    	   $('#reg-step1').removeClass('hidden');
       	   $('#reg-step2').addClass('hidden');
       	   $('#email').val("");
       	   $('#agreeChk').removeAttr('checked');
       	   nc.init(nc_option);
       })
       //滑块验证
       var nc = new noCaptcha();
       var nc_appkey = appKey;  // 应用标识,不可更改
       var nc_scene = 'register';  //场景,不可更改
       var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
       var nc_option = {
           renderTo: '#captcha',
           appkey: nc_appkey,
           scene: nc_scene,
           token: nc_token,
           callback: function (data) {// 校验成功回调
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
   });
//回车键聚焦我已阅读
function KeyDown() {
    var theEvent = window.event || arguments.callee.caller.arguments[0];
    var code = theEvent.keyCode;
    if (code== 13) {
        theEvent.returnValue = false;
        theEvent.cancel = true;
        $('#agreeChk').focus();
    }
}

  