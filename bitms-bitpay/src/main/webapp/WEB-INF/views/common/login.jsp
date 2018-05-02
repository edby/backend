<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
    <title>登录</title>
    <link rel="stylesheet" href="${res}/css/login/style.css">
    <link rel="stylesheet" href="${res}/css/login/iconfont.css">
    <script type="text/javascript" src="${res}/js/jquery-1.11.3.js"></script>
</head>

<body>
<form id="loginForm" method="post">
    <div class="login-banner"></div>

    <div class="login-box">

        <div class="box-con tran">

            <div class="login-con f-l">

                <div class="form-group">

                    <input type="text" name="username" placeholder="用户名" />
                </div>

                <div class="form-group">

                    <input type="password" name="password" placeholder="密码">

                </div>

                <div class="form-group">

                    <button type="button" id="loginBtn" class="tran pr">登录</button>

                </div>
            </div>

            <!-- 登录 -->
            
            
        </div>

    </div>
</form>

<script>
    $(function(){
        $("#loginBtn").click(function () {
            var postData = $("#loginForm").serialize();
            $.post("/common/login/submit.ajax", postData, function(result){
                if(100 != result.code){
                	$("#pcrimg").trigger("click");
                    alert(result.message);
                    return;
                }
                window.location.href = "/admin";
            }, "json");
        });
      	//验证码
		$("#pcrimg").on("click", function(){
			$(this).attr("src", $(this).attr("src")+"?t="+Math.random());
	  	}); 
    });
</script>
</body>

</html>
