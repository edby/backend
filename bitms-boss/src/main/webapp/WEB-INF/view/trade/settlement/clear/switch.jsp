<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
</script>
<div class="easyui-layout" data-options="fit:true,border:false" style="padding:10px 200px;">
    <h1>${switchStatus eq 'yes'?'<color="green">总控开关状态：开启</font>':'<color="red">当前开关状态：关闭</font>'}</h1>
    <h1>${stockInfo.canTrade eq 'yes'?'<color="green">交易开关状态：开启</font>':'<color="red">当前开关状态：关闭</font>'}</h1>
    <h1>交易对：${stockInfo.stockName}</h1>
</div>
