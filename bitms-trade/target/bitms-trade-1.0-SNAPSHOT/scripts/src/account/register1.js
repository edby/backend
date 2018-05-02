var validator;
   seajs.use(['validator','i18n'], function (Validator,I18n) {
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
                       },
                       success: function (data, textStatus, jqXHR) {
                    	   $('.reg-pop').fadeOut();
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
                               $('#reg-step1').hide();
                               $('#reg-step2').show();
                               $('#emaliHost').attr('href',mailStr).text(emailHost);
                               $('#emailStr').attr('href',mailStr);
                           } else if(data.code == bitms.captchaError) {
                        	   $('.reg-pop').fadeOut();
                               remind(remindType.error,I18n.prop("error.verificationCode"));
                           } else if(data.code == bitms.emailExists) {
                        	   $('.reg-pop').fadeOut();
                               remind(remindType.error,data.message);
                               $('#email').val("").focus();
                               nc.init(nc_option);
                           }else{
                        	   $('.reg-pop').fadeOut();
                               remind(remindType.error,data.message);
                           }

                       }
                   });
               }
               if($('#agreeChk').is(':checked')){
                   $('.reg-inner label i').removeClass('checkError');
               }else{
                   $('.reg-inner label i').addClass('checkError');
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
    	   $('#reg-step1').show();
       	   $('#reg-step2').hide();
       	   $('#email').val("");
       	   $('#agreeChk').removeAttr('checked');
       	   nc.init(nc_option);
       })
       //输入文字时出现清除按钮
       $('.reg-input').keypress(function() {
           $(this).next('.reg-img').show();
       })

       $('.reg-img').click(function() {
           $(this).prev('input').val('');
           $(this).hide();
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
    $('#agree').click(function(){
        $('#agreePop').fadeIn();
        $('body').css('overflow','hidden');
    })
  