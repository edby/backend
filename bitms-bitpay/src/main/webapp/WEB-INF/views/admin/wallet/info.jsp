<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/_common.jsp" %>
<div class="formbody">
	<div class="formtitle"><span><c:if test="${empty keychain.id}">创建</c:if><c:if test="${not empty keychain.id}">修改</c:if>钱包参数</span></div>
	<form role="form" id="infoForm" method="post">
		<input type="hidden" name="id" id="id" value="${keychain.id}">  
		<ul class="forminfo">
  		<li><label>钱包ID<b></b></label><input type="text" class="dfinput" 
	          	value="${keychain.walletId}"  maxlength="100" readonly/></li>
  		<li><label>钱包名称<b>*</b></label><input type="text" class="dfinput" name="walletName" id="walletName" 
	          	value="${keychain.walletName}"  maxlength="500" required/></li>
  		<li><label>TOKEN<b>*</b></label><textarea class="textinput" name="token" id="token" style="height:100px;" required><c:if test="${not empty keychain.token}">******</c:if></textarea></li>
  		<li><label>私钥密文<b>*</b></label><textarea class="textinput" name="xprv" id="xprv" style="height:100px;" required><c:if test="${not empty keychain.xprv}">******</c:if></textarea></li>
  		<li><label>手续费费率<b></b></label><input type="text" class="dfinput" name="feeTxConfirmTarget" id="feeTxConfirmTarget" 
	          	value="${keychain.feeTxConfirmTarget}"  maxlength="10" required/> 最小2，最大20</li>
  		<li><label>证券信息ID<b></b></label><input type="text" class="dfinput"  
	          	value="${keychain.stockinfoId}"  maxlength="32" readonly/></li>
	    <li><label>钱包类型<b>*</b></label><input name="type" type="radio" value="1" <c:if test="${empty keychain.type || keychain.type==1}">checked</c:if> />平台充值钱包
	    <input name="type" type="radio" value="2" <c:if test="${keychain.type==2}">checked</c:if> />平台划拨钱包
	    </li>
      	<li><label>&nbsp;</label>
	    <c:if test="${empty keychain.id}">
		<button type="submit" class="btn btn-success">保存</button>
		</c:if>
	    <c:if test="${not empty keychain.id}">
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
				save($('#infoForm'), '${ctx}/admin/wallet/save.ajax');
			}
		});
	});
</script>