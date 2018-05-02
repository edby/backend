 var validator;
   seajs.use(['validator','i18n'], function (Validator,I18n) {
       validator = new Validator();
       $("#regSubmit2").on("click", function () {
           validator.destroy();
           validator = new Validator({
               element: '#reg-step3',
               autoSubmit: false,// 当验证通过后不自动提交
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
                           },
                           success: function (data, textStatus, jqXHR) {
                               if (data.code == bitms.success) {
                            	   $('.reg-pop').fadeOut();
                            	   $('#reg-step3').hide();
                               	   $('#reg-step4').show();
                               	   setTimeout(function () {
                               		jumpUrl("/login");
								    }, 2000);
                               	  }else{
                               		$('.reg-pop').fadeOut();
                               		$('#reg-step3').find('input[name="csrf"]').val(jqXHR.getResponseHeader("csrf"));
                               		remind(remindType.error,data.message);
                               	 }
                               
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
   $(function() {
		//输入文字时出现清除按钮
		$('.reg-input').keypress(function() {
			$(this).next('.reg-img').show();
		})

		$('.reg-img').click(function() {
			$(this).prev('input').val('');
			$(this).hide();
		})

		/*密码强度*/
		$('#loginPwd').keyup(function() {
			var strongRegex = new RegExp("^(?=.{10,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
			var mediumRegex = new RegExp("^(?=.{9,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
			var enoughRegex = new RegExp("(?=.{8,}).*", "g");

			if(false == enoughRegex.test($(this).val())) {
				$('.pw-weak').removeClass('pw-bar-weak');
				$('.pw-medium').removeClass('pw-bar-medium');
				$('.pw-strong').removeClass('pw-bar-strong');
				//密码小于八位的时候，密码强度图片都为灰色 
			} else if(strongRegex.test($(this).val())) {
				$('.pw-weak').addClass('pw-bar-weak');
				$('.pw-medium').addClass('pw-bar-medium');
				$('.pw-strong').addClass('pw-bar-strong');
				//密码为八位及以上并且字母数字特殊字符三项都包括,强度最强 
			} else if(mediumRegex.test($(this).val())) {
				$('.pw-weak').addClass('pw-bar-weak');
				$('.pw-medium').addClass('pw-bar-medium');
				$('.pw-strong').removeClass('pw-bar-strong');
				//密码为八位及以上并且字母、数字、特殊字符三项中有两项，强度是中等 
			} else {
				$('.pw-strength').show();
				$('.pw-weak').addClass('pw-bar-weak');
				$('.pw-medium').removeClass('pw-bar-medium');
				$('.pw-strong').removeClass('pw-bar-strong');
				//如果密码为八为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的 
			}
			return true;
		});
		$('#loginPwd').next('.reg-img').click(function(){
			$('.pw-strength').hide();
		})
	})