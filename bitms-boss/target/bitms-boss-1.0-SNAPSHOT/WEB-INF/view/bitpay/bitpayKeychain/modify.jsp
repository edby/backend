<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editKeychainForm').form({
            url : '${ctx}/bitpay/bitpayKeychain/save',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if($("#token").val()==''){
                	parent.$.messager.alert('错误', 'TOKEN不能为空', 'error');
                	isValid=false;
                }
                if($("#xprv").val()==''){
                	parent.$.messager.alert('错误', '密文私钥不能为空', 'error');
                	isValid=false;
                }
                if($("input[name='type']").val()==''){
                	parent.$.messager.alert('错误', '钱包类型不能为空', 'error');
                	isValid=false;
                }
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success : function(result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editKeychainForm').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        $("#editKeychainForm").parent().css("overflow-y", "scroll");
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editKeychainForm" method="post">
        	<input name="id" type="hidden"  value="${keychain.id}">
            <table class="grid">
                <tr>
                    <td>钱包ID</td>
                    <td colspan="3">
                        <input name="stockCode" type="text" placeholder=""  style="width: 374px;"
                           readonly value="${keychain.walletId}">
                    </td>
                </tr>
                <tr>
                    <td>钱包名称<font color='red'>*</font></td>
                    <td colspan="3"><input name="walletName" type="text" placeholder="请输入钱包名称" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,validType:['length[0,16]']" value="${keychain.walletName}"></td>
                </tr>
                <tr>
                    <td>TOKEN<font color='red'>*</font></td>
                    <td colspan="3">
                        <textarea id="token" name="token" style="width: 374px;height: 60px;">${keychain.token}</textarea>
                    </td>
                </tr>
                <tr>
                    <td>私钥密文<font color='red'>*</font></td>
                    <td colspan="3">
                        <textarea id="xprv" name="xprv" style="width: 374px;height: 60px;">${keychain.xprv}</textarea>
                    </td>
                </tr>
                <tr>
                    <td>币种(调用接口用)<font color='red'>*</font></td>
                    <td colspan="3"><input name="coin" type="text" placeholder="请输入币种" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                                           data-options="required:true,validType:['length[0,16]']" value="${keychain.coin}"></td>
                </tr>
                <tr>
                    <td>支付密码<font color='red'>*</font></td>
                    <td colspan="3"><input name="systemPass" type="password" placeholder="请输入支付密码" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                                           data-options="required:true,validType:['length[0,16]']" value="${keychain.systemPass}"></td>
                </tr>
                <tr>
                    <td>手续费费率<font color='red'>*</font></td>
                    <td colspan="3"><input name="feeTxConfirmTarget" type="text" placeholder="请输入手续费费率" class="easyui-numberbox" style="width: 374px;"
                               data-options="required:true,min:2,max:20,precision:0" value="${keychain.feeTxConfirmTarget}"></td>
                </tr>
                <tr>
                    <td>证券信息ID</td>
                    <td colspan="3"><input id="stockinfoId" name="stockinfoId" class="easyui-combobox" name="language" style="width: 374px;"
                                           placeholder="请关联证券代码" value="<%=FundConsts.WALLET_BTC_TYPE%>" data-options="
									url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  ></td>
                </tr>
                <tr>
                    <td>钱包类型<font color='red'>*</font></td>
                    <td colspan="3"><input  name="type" type="radio" value="1" <c:if test="${empty keychain.type || keychain.type==1}">checked</c:if> />平台充值钱包
	                                <input  name="type" type="radio" value="2" <c:if test="${keychain.type==2}">checked</c:if> />平台划拨钱包
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>