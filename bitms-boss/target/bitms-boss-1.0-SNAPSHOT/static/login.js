// 判断时候在Iframe框架内,在则刷新父页面
if (self != top) {
    parent.location.reload(true);
    if (!!(window.attachEvent && !window.opera)) {
        document.execCommand("stop");
    } else {
        window.stop();
    }
}

$(function () {
    var nc = new noCaptcha();
    var nc_appkey = "FFFF000000000177330E";  // 应用标识,不可更改
    var nc_scene = 'login';  //场景,不可更改
    var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
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
            $('.lm-tit').css('line-height', '43px');
        },
        foreign: 0,
        isEnabled: true
    };
    nc.init(nc_option);
    // 得到焦点
    $("#password").focus(function () {
        $("#left_hand").animate({
            left: "150",
            top: " -38"
        }, {
            step: function () {
                if (parseInt($("#left_hand").css("left")) > 140) {
                    $("#left_hand").attr("class", "left_hand");
                }
            }
        }, 2000);
        $("#right_hand").animate({
            right: "-64",
            top: "-38px"
        }, {
            step: function () {
                if (parseInt($("#right_hand").css("right")) > -70) {
                    $("#right_hand").attr("class", "right_hand");
                }
            }
        }, 2000);
    });
    // 失去焦点
    $("#password").blur(function () {
        $("#left_hand").attr("class", "initial_left_hand");
        $("#left_hand").attr("style", "left:100px;top:-12px;");
        $("#right_hand").attr("class", "initial_right_hand");
        $("#right_hand").attr("style", "right:-112px;top:-12px");
    });
    // 登录
    $('#loginform').form({
        url: basePath + '/login/submit',
        onSubmit : function() {
            progressLoad();
            var isValid = $(this).form('validate');
            if(!isValid){
                progressClose();
            }
            return isValid;
        },
        success:function(result){
            progressClose();
            result = jQuery.parseJSON(result);
            $('#loginform').find('input[name="csrf"]').val(result.csrf);
            if (result.code == ajax_result_success_code) {
                window.location.href = basePath + '/dispatch';
            }else if(result.code == ajax_result_google_code){
                window.location.href = basePath + '/login/check';
            }else{
                nc.init(nc_option);
                showMsg(result.message);
            }
        }
    });
});
function submitForm(){
    $('#loginform').submit();
}
function clearForm(){
    $('#loginform').form('clear');
}
//回车登录
function enterlogin(){
    if (event.keyCode == 13){
        event.returnValue=false;
        event.cancel = true;
        $('#loginform').submit();
    }
}
