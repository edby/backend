<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>BOSS运营平台</title>
    <%@ include file="/commons/basejs.jsp" %>
    <link rel="stylesheet" type="text/css" href="${res}/style/css/login.css" />
</head>
<body onkeydown="enterlogin();">
<div class="top_div"></div>
<div class="login-tit">BOSS运营平台</div>
<div class="login-con">
    <form:form  id="loginCheck">
        <P style="padding: 30px 0px 10px; position: relative;">
            <span class="p_logo p_ga"></span>
            <input class="ipt" type="text" name="gaCode" placeholder="请输入谷歌验证码"/>
        </P>
        <a  class="login-btn"  href="javascript:;" onclick="submitForm()">确定</a>
    </form:form>
</div>
<script>
    $(function(){
    $('#loginCheck').form({
        url: basePath + '/login/check/submit',
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
            result = $.parseJSON(result);
            $('#loginCheck').find('input[name="csrf"]').val(result.csrf);
            setCsrfToken("loginCheck");
            if (result.code == ajax_result_success_code) {
                window.location.href = basePath + '/dispatch';
            }else{
                showMsg(result.message);
            }
        }
    });
    })
    function submitForm(){
        $('#loginCheck').submit();
    }
    function enterlogin(){
        if (event.keyCode == 13){
            event.returnValue=false;
            event.cancel = true;
            $('#loginCheck').submit();
        }
    }
</script>
</body>
</html>
