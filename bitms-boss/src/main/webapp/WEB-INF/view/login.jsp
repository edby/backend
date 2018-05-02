<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>BOSS运营平台</title>
    <%@ include file="/commons/basejs.jsp" %>
    <!-- 此段必须要引入 t为小时级别的时间戳 -->
    <link type="text/css" href="//g.alicdn.com/sd/ncpc/nc.css?t=1502956831425" rel="stylesheet"/>
    <script type="text/javascript" src="//g.alicdn.com/sd/ncpc/nc.js?t=1502956831425"></script>
    <!-- 引入结束 -->
    <!-- 此段必须要引入 -->
    <div id="_umfp" style="display:inline;width:1px;height:1px;overflow:hidden"></div>
    <!-- 引入结束 -->
    <link rel="stylesheet" type="text/css" href="${res}/style/css/login.css" />
</head>
<body onkeydown="enterlogin();">
<div class="top_div"></div>
<div class="login-tit">BOSS运营平台</div>
<div class="login-con">
    <form:form method="post" id="loginform">
        <P style="padding: 30px 0px 10px; position: relative;">
            <span class="u_logo"></span>
            <input class="ipt" type="text" name="username" placeholder="请输入登录名"/>
        </P>
        <P style="position: relative;">
            <span class="p_logo"></span>
            <input class="ipt" id="password" type="password" name="password" placeholder="请输入密码"/>
        </P>
        <P style="position: relative;">
            <div class="ln">
                <div id="captcha"></div>
                <input type='hidden' id='csessionid' name='csessionid'/>
                <input type='hidden' id='sig' name='sig'/>
                <input type='hidden' id='token' name='token'/>
                <input type='hidden' id='scene' name='scene'/>
            </div>
        </P>
            <a  class="login-btn"  href="javascript:;" onclick="submitForm()">登录</a>
    </form:form>
</div>
<script type="text/javascript" src="${res}/login.js" charset="utf-8"></script>
</body>
</html>
