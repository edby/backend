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
    	<li><a href="#">提现管理</a></li>
    </ul>
    </div>
    <div class="rightinfo">
    	<div class="tools panel-search">
    	<form id="searchForm" action="">
    	<input name="fileupload" type="file" id="fileupload" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" style="display:none;"/>
    	<input type="hidden" id="pageNo" name="pageNo"/>
    	<input type="hidden" id="confirmType" />
    	<input type="hidden" id="confirmUrl" />
    	<ul class="seachform" style="float:left;">
    		<li><label>提现记录ID</label><input name="id" type="text" class="scinput" /></li>
    		<li><label>帐户ID</label><input name="accountId" type="text" class="scinput" /></li>
    		<li><label>提现地址</label><input name="withdrawAddr" type="text" class="scinput" /></li>
    		<li><label>提现状态</label>
    		<div class="vocation">
    		<select id="selectID" name ="state" class="select1">
    			<option value="">请选择提现状态</option>
	        	<option value="0">未提现</option>
	       		<option value="1">已提现未导出</option>
	        	<option value="2">已导出</option>
	    	</select>
	    	</div>
    		</li>
		    <li><label>&nbsp;</label><input name="" type="button" class="scbtn" value="查询" onclick="golist();"/></li>
		</ul>
    	</form>
        <ul class="toolbar1">
        	<li class="click" onclick="batchWithdraw('idAry', '${ctx}/admin/withdraw/confirm/batch.ajax');"><span><img src="${res}/images/t01.png"/></span>批量提现</li>
        	<li class="click" onclick="apiimport('${ctx}/admin/withdraw/apiimport.ajax')"><span><img src="${res}/images/t05.png"/></span>接口导入</li>
        	<li class="click upload"><span><img src="${res}/images/t02.png"/></span>文件导入</li>
        	<li class="click" onclick="excexport($('#listForm'), 'idAry', '${ctx}/admin/withdraw/export')"><span><img src="${res}/images/t04.png"/></span>导出</li>
        </ul>
		</div>
		<div class="panel-content"></div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/admin/home/uplodimg.jsp"/>

<form id="payForm" method="post">
<div class="modal fade edit-modal-lg" tabindex="-1" role="dialog" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
    <button class="close" aria-label="Close" data-dismiss="modal" type="button"><span aria-hidden="true">×</span></button>
    <h4 id="mySmallModalLabel" class="modal-title">输入支付密码</h4>
    </div>
     <div class="modal-body">
     <div class="address">
      <div class="form-group">
       <label><b>*</b>支付密码</label>
       <input type="password" id="pass" class="form-control" required maxlength="50"/>
      </div>
      </div>
     </div>
     <div class="modal-footer">
        <button type="submit" class="btn btn-primary" >确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      </div>
    </div>
  </div>
</div>
</form>
<%@ include file="/WEB-INF/views/admin/home/cover.jsp" %>
<script type="text/javascript">
	$(".select1").uedSelect({
		width : 130			  
	});
	$(function(){
		golist();
		$("#payForm").validate({
			submitHandler: function() {
				var pass = $("#pass").val();
				var confirmType = $("#confirmType").val();
				var confirmUrl = $("#confirmUrl").val();
				if(1 == confirmType){
					withdrawConfirm(pass, confirmUrl);
				}else{
					$("#password").val(pass)
					batchWithdrawConfirm($('#listForm'), confirmUrl);
				}
				$('#payForm')[0].reset();
				$("#pass").val("");
				$('#myModal').modal('hide');
			}
		});
	});
	function page(number){
		golist(number);
	}
	function golist(number){ 
		$("#pageNo").val(number);
		search($('#searchForm'), '${ctx}/admin/withdraw/list');
	}
	function withdraw(url){
		if(confirm("是否确认提现？")){
			$("#confirmType").val(1);
			$("#confirmUrl").val(url);
			$('#myModal').modal('show');
		}
	};  
	function withdrawConfirm(pass, url){
		showModal();
		$.ajax({
			type: 'post',
			url: url,
			data: {password:pass},
			dataType: 'json',
			success: function(data){
				if(data.code == GW.SUCCESS){
					alert("提现成功！");
					golist();
				}else{
					alert(data.message);
				}
				hideModal();
			}
		});
	}
	function batchWithdraw(name, url){
		var num = $("input[name='"+name+"']:checked").length;
		if(num <= 0){
			alert("请选择一条记录！")
			return;
		}else{
			if(confirm("是否确认批量提现？")){
				$("#confirmType").val(2);
				$("#confirmUrl").val(url);
				$('#myModal').modal('show');
			}
		}
	};  
	function batchWithdrawConfirm(obj, url){
		showModal();
		$.ajax({
			type: 'post',
			url: url,
			data:obj.serialize(),
			dataType: 'json',
			success: function(data){
				if(data.code == GW.SUCCESS){
					alert("批量提现成功");
					golist();
				}else{
					alert(data.message);
				}
				hideModal();
			}
		});
	}
	function excexport(obj, name, url){
		var num = $("input[name='"+name+"']:checked").length;
		if(num <= 0){
			alert("请选择一条记录！")
			return;
		}else{
			obj.attr("action", url); 
			obj.submit();
		}
	};
	function apiimport(url){
		if(confirm("是否确认接口导入提现数据？")){
			showModal();
			$.ajax({
				type: 'post',
				url: url,
				dataType: 'json',
				success: function(data){
					if(data.code == GW.SUCCESS){
						alert("接口导入提现数据成功");
						golist();
					}else{
						alert(data.message);
					}
					hideModal();
				}
			});
		}
	}
</script>
</body>
</html>