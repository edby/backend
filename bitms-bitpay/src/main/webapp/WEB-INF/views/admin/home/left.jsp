<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>左侧菜单</title>
<link href="${res}/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="${res}/js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('ul').slideUp();
		if($ul.is(':visible')){
			$(this).next('ul').slideUp();
		}else{
			$(this).next('ul').slideDown();
		}
	});
})	
</script>


</head>
<body style="background:#f0f9fd;">
	<div class="lefttop"><span></span>比特币管理</div> 
    
    <dl class="leftmenu">
        
    <!-- <dd> -->
    <div class="title">
    <%-- <span style="display:none;"><img src="${res}/images/leftico01.png" /></span>管理信息  --%>
    </div>
    	<ul class="menuson">  
        <li class="active"><cite></cite><a href="${ctx}/admin/wallet/search" target="rightFrame">钱包管理</i></li>
        <li><cite></cite><a href="${ctx}/admin/withdraw/search" target="rightFrame">提币划拨</a><i></i></li>
        <li><cite></cite><a href="${ctx}/admin/wallet/pass" target="rightFrame">修改密码</a><i></i></li>
        <li><cite></cite><a href="${ctx}/admin/wallet/transfer" target="rightFrame">资金归集</a><i></i></li>
        </ul>   
    <!-- </dd> -->
    </dl>
</body>
</html>