<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>blocain</title>
<link rel="stylesheet" type="text/css" href="${res}/css/bootstrap.min.css"/>
<link href="${res}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${res}/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="${res}/js/select-ui.min.js"></script>
<script type="text/javascript" src="${res}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${res}/js/common/common.js"></script>
<script src="${res}/js/fileupload/jquery.ui.widget.js"></script>
<script src="${res}/js/fileupload/jquery.fileupload.js"></script>
<script src="${res}/js/bootstrap.min.js"></script>
<style>
span.error{color:#C00;}
</style>
</head>

<body>
<div class="col-md-12">
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    	<li><a href="#">首页</a></li>
    	<li><a href="#">比特币管理</a></li>
    	<li><a href="#">钱包信息</a></li>
    </ul>
    </div>
    <div class="rightinfo">
    	<div class="tools panel-search">
    	<form id="searchForm" action="">
    	<input type="hidden" id="pageNo" name="pageNo"/>
    			</form>
    	<ul class="toolbar1">
        	<li class="click" onclick="add('${ctx}/admin/wallet/info');"><span><img src="${res}/images/t01.png"/></span>添加</li>
        </ul>
		</div>
		<div class="panel-content"></div>
	</div>
</div>
<%@ include file="/WEB-INF/views/admin/home/cover.jsp" %>
<script type="text/javascript">
	$(function(){
		golist();
	});
	function page(number){
		golist(number);
	}
	function golist(number){ 
		$("#pageNo").val(number);
		search($('#searchForm'), '${ctx}/admin/wallet/list');
	}
</script>
</body>
</html>