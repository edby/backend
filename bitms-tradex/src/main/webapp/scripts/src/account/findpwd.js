$(function(){
	$('#findpwdForm').validate({
        errorPlacement: function (error, element) {
            error.appendTo(element.parents('.controller'));
        },
        rules: {
            email: {
                email: true
            }
        },
        submitHandler: function (form) {
            $(form).ajaxSubmit({
            	success: function(data){
            		if(JSON.parse(data).code == 200){
            			location.href = '/forgetPass/confirm';
            		}else{
            			console.log(JSON.parse(data).message);
            			$('.auth-controller').find('.error').remove();
            			$('.auth-controller').append('<p class="error">'+JSON.parse(data).message+'</p>');
            		}
            	}
            });
        }
    });
	$('#authcode1').on('focus', function(){
		$('.auth-controller').find('.error').remove();
	});
	  jQuery.validator.addMethod("regexPassword", function(value, element) {  
	        return this.optional(element) || /(?![0-9A-Z]+$)(?![0-9a-z]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,20}$/.test(value);  
	   }, $.i18n.prop('string_pass_invalid'));
	  
	$('#changepwdForm').validate({
        errorPlacement: function (error, element) {
            error.appendTo(element.parents('.controller'));
        },
        rules: {
            email: {
                email: true
            },
            loginPwd: {
	            required: true,
	            regexPassword: true,
	            rangelength: [6, 20]
	        },
	        reloginpwd: {
	            required: true,
	            equalTo: "#setNewPwd"
	        }
        },
        submitHandler: function (form) {
            $(form).ajaxSubmit({
            	success: function(data){
            		if(JSON.parse(data).code == 200){
            			location.href = '/login';
            		}else{
            			$('#findpwdForm').find('.error-tips').remove();
            			$('#findpwdForm').append('<p class="align-center error-tips>'+JSON.parse(data).message+'</p>');
            		}
            	}
            });
        }
    });
})
