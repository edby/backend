<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<div class="formbody">
	<div class="formtitle"><span><c:if test="${empty withdrawRecord.id}">创建</c:if><c:if test="${not empty withdrawRecord.id}">修改</c:if>bitpay</span></div>
	<form role="form" id="infoForm" method="post">
		<input type="hidden" name="id" id="id" value="${withdrawRecord.id}">  
		<ul class="forminfo">
  		<li><label>帐户ID<b>*</b></label><input type="text" class="dfinput" name="accountId" id="accountId" 
	          	value="${withdrawRecord.accountId}"  maxlength="32"/></li>
  		<li><label>提现地址<b>*</b></label><input type="text" class="dfinput" name="withdrawAddr" id="withdrawAddr"
	          	value="${withdrawRecord.withdrawAddr}"  maxlength="64"/></li>
  		<li><label>提现金额<b>*</b></label><input type="text" class="dfinput" name="occurAmt" id="occurAmt" 
	          	value="${withdrawRecord.occurAmt}"  maxlength="20"/></li>
  		<li><label>手续费<b>*</b></label><input type="text" class="dfinput" name="netFee" id="netFee" 
	          	value="${withdrawRecord.netFee}"  maxlength="20"/></li>
  		<li><label>证券信息ID<b>*</b></label><input type="text" class="dfinput" name="stockinfoId" id="stockinfoId" 
	          	value="${withdrawRecord.stockinfoId}"  maxlength="32"/></li>
  		<li><label>状态：0、未提现 1、已提现 2、提现失败<b>*</b></label><input type="text" class="dfinput" name="state" id="state" 
	          	value="${withdrawRecord.state}"  maxlength="10"/></li>
  		<li><label>备注<b>*</b></label><input type="text" class="dfinput" name="remark" id="remark" 
	          	value="${withdrawRecord.remark}"  maxlength="200"/></li>
  		<li><label>提现时间<b>*</b></label><input type="text" class="dfinput" name="createDate" id="createDate" 
	          	value="${withdrawRecord.createDate}"  maxlength=""/></li>
      	<li><label>&nbsp;</label>
      	<c:if test="${empty withdrawRecord.id}">
		<button type="submit" class="btn btn-success">保存</button>
		</c:if>
	    <c:if test="${not empty withdrawRecord.id}">
	    <button type="submit" class="btn btn-success">修改</button>
	    </c:if>
      	
		<button type="button" class="btn btn-cancel" onclick="golist();">返回</button>
	    </li>
      	</ul>
	</form>
</div> 
<script type="text/javascript">
	$(function(){
		$('#infoForm').validate({
			submitHandler: function() {
				save($('#infoForm'), '${ctx}/test/withdrawRecord/save.ajax');
			}
		});
	});
</script>