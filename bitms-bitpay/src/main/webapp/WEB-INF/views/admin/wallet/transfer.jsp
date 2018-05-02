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
    	<li><a href="#">内部转帐</a></li>
    </ul>
    </div>
    <div class="rightinfo">
		<div class="panel-content">
		<div class="formbody">
			<div class="formtitle"><span>内部转帐</span></div>
				<form role="form" id="infoForm" method="post"> 
					<ul class="forminfo">
					<li><label>钱包名称<b></b></label><input type="text" class="dfinput" 
				          	value="${wallet.walletName}" axlength="100" readonly/></li>
			  		<li><label>转帐金额<b>*</b></label><input type="text" class="dfinput" 
				          	value=""  name="amount" axlength="100" required/></li>
				    <li><label>手续费<b></b></label><input type="text" class="dfinput" 
				          	value=""  name="fee" axlength="100"/> 手续费不能超过0.1BTC</li>
				    <li><label>转帐地址<b>*</b></label><input type="text" class="dfinput" 
				          	value=""  name="address" axlength="100" required/></li>
			  		<li><label>支付密码<b>*</b></label><input type="password" class="dfinput" 
				          	value="" name="payPass"  maxlength="500" required/></li>
			      	<li><label>&nbsp;</label>
				    <button type="submit" class="btn btn-success">提交</button>
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
			showModal();
			$.ajax({
				type: 'post',
				url: '${ctx}/admin/wallet/transfer/confirm.ajax',
				data: $('#infoForm').serialize(),
				dataType: 'json',
				success: function(data){
					if(data.code == GW.SUCCESS){
						alert("转帐成功！");
						$('#infoForm')[0].reset();
					}else{
						alert(data.message);
					}
					hideModal();
				}
			});
		}
	});
});
</script>
</body>
</html>