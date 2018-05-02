var validator;
seajs.use(['validator','i18n'], function (Validator,I18n) {
   $('.pageLoader').hide();
   validator = new Validator({
       element: '#reg-step1',
       autoSubmit: false,
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
                       $('#regSubmit1').attr("disabled",true);
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
                       $('#regSubmit1').attr("disabled",true);
                   },
                   complete:function(){
                       $('.reg-pop').hide();
                       $('#regSubmit1').attr("disabled",false);
                   }
               });
           }
       }
   }).addItem({
       element: '[name=email]',
       required: true,
       rule: 'registerEmail'
   }).addItem({
       element: '[name=agreeChk]',
       required: true
   });
   $("#regSubmit1").on("click", function () {
       $("#reg-step1").submit();
   });

   $('#reload').click(function(){
       location.reload();
       $('#reg-step1').removeClass('hidden');
       $('#reg-step2').addClass('hidden');
       $('#email').val("");
       $('#agreeChk').removeAttr('checked');
       nc.init(nc_option);
   });

   $('#regSubmit1').attr("disabled",true);
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
           if($("input[name='agreeChk']").prop('checked') == true){
               $('#regSubmit1').attr("disabled",false);
           }
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

   $("input[name='agreeChk']").change(function () {
        if($("input[name='agreeChk']").prop('checked') == true && $("#sig").val() != ''){
            $('#regSubmit1').attr("disabled",false);
        }
        else{
            $('#regSubmit1').attr("disabled",true);
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

  