<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>头部菜单</title>
<link href="${res}/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="${res}/js/jquery.js"></script>
<script type="text/javascript">
    $(function() {
        //顶部导航切换
        $(".nav li a").click(function() {
            $(".nav li a.selected").removeClass("selected")
            $(this).addClass("selected");
        })
    })
</script>


</head>
<body style="background:url(${res}/images/topbg.gif) repeat-x;">

    <div class="topleft">
        <a href="main.html" target="_parent"><img src="${res}/images/logo.png" title="系统首页" /></a>
    </div>

    <ul class="nav">
        <li>
            <a href="default.html" target="rightFrame" class="selected">
                <img src="${res}/images/icon01.png" title="比特币管理" />
                <h2>比特币管理</h2>
            </a>
        </li>
    </ul>

    <div class="topright">
        <ul>
            <li><span><img src="${res}/images/help.png" title="帮助" class="helpimg" /></span><a href="#">帮助</a></li>
            <li><a href="#">关于</a></li>
            <li><a href="/common/logout" target="_parent">退出</a></li>
        </ul>

        <div class="user">
            <span>${sessionScope.session_member_key.userName}</span>
        </div>

    </div>

    <!-- <div style="display:none"><script src='http://v7.cnzz.com/stat.php?id=155540&web_id=155540' language='JavaScript' charset='gb2312'></script></div> -->
</body>
</html>