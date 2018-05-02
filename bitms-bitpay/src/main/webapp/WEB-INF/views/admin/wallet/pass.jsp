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
    	<li><a href="#">修改密码</a></li>
    </ul>
    </div>
    <div class="rightinfo">
		<div class="panel-content">
		<c:if test="${'admin' == sessionScope.session_member_key.role}">
		<div class="formbody">
			<div class="formtitle"><span>修改支付密码</span></div>
				<form role="form" id="infoForm" method="post"> 
					<ul class="forminfo">
			  		<li><label>旧密码<b>*</b></label><input type="password" class="dfinput" 
				          	value=""  name="oldPass" axlength="100" required/></li>
			  		<li><label>新密码<b>*</b></label><input type="password" class="dfinput" 
				          	value="" name="newPass" id="newPass"  maxlength="500" required/></li>
			  		<li><label>确认密码<b>*</b></label><input type="password" class="dfinput" 
				          	value="" name="resPass" maxlength="500" required equalTo="#newPass"/></li>
			  		<li><label>钱包类型<b>*</b></label><input name="type" type="radio" value="1" checked />平台充值钱包
				    <input name="type" type="radio" value="2" />平台划拨钱包
				    </li>
			      	<li><label>&nbsp;</label>
				    <button type="submit" class="btn btn-success">修改</button>
				    </li>
			      	</ul>
				</form>
			</div> 
			</c:if>
			<div class="formbody">
			<div class="formtitle"><span>修改登录密码</span></div>
				<form role="form" id="passForm" method="post"> 
					<ul class="forminfo">
			  		<li><label>旧密码<b>*</b></label><input type="password" class="dfinput" 
				          	value=""  name="oldPass" axlength="100" required/></li>
			  		<li><label>新密码<b>*</b></label><input type="password" class="dfinput" 
				          	value="" name="newPass" id="newLogPass"  maxlength="500" required/></li>
			  		<li><label>确认密码<b>*</b></label><input type="password" class="dfinput" 
				          	value="" name="resPass" maxlength="500" required equalTo="#newLogPass"/></li>
			      	<li><label>&nbsp;</label>
				    <button type="submit" class="btn btn-success">修改</button>
				    </li>
			      	</ul>
				</form>
			</div> 

		</div>
	</div>
</div>
<%@ include file="/WEB-INF/views/admin/home/cover.jsp" %>
<script type="text/javascript">
$(function(){
	$('#infoForm').validate({
		submitHandler: function() {
			save($('#infoForm'), '${ctx}/admin/wallet/pass/update.ajax');
		}
	});
	$('#passForm').validate({
		submitHandler: function() {
			save($('#passForm'), '${ctx}/admin/member/pass/update.ajax');
			$('#passForm')[0].reset();
		}
	});
});

</script>
</body>
</html>