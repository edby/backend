 var validator;
   seajs.use(['validator','i18n'], function (Validator,I18n) {
       validator = new Validator();
       $("#regSubmit2").on("click", function () {
           validator.destroy();
           validator = new Validator({
               element: '#reg-step3',
               autoSubmit: false,
               onFormValidated: function (error, results, element) {
                   if (!error) {
                       $.ajax({
                           url: '/register/confirm/submit',
                           type: 'post',
                           timeout:6000,
                           data: $('#reg-step3').serialize(),
                           dataType: 'json',
                           beforeSend:function(){
                        	     $('.reg-pop').fadeIn();
                                $("#regSubmit2").attr("disabled",true);
                           },
                           success: function (data, textStatus, jqXHR) {
                               $('#reg-step3').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                               if (data.code == bitms.success) {
                            	   $('#reg-step3').addClass('hidden');
                               	   $('#reg-step4').removeClass('hidden');
                               	   setTimeout(function () {
                               		jumpUrl("/login");
								    }, 2000);
                               	  }else{
                               		remind(remindType.error,data.message);
                               	 }
                               
                            },
                           complete:function(){
                               $('.reg-pop').hide();
                               $("#regSubmit2").attr("disabled",false);
                           }
                       });
                   }
               }
           }).addItem({
                element: '[name=loginPwd]',
                required: true,
                rule: 'password'
            }).addItem({
                element: '[name=confirmPwd]',
                required: true,
                rule: 'confirmation{target: "#loginPwd"}'
            });
           $("#reg-step3").submit();
       });
   });
