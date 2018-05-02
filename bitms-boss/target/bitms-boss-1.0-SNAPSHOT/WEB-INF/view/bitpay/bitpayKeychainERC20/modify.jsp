<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">

    if ("${keychainERC20.id}" == "") {
        //如果是添加模式则移除监控代码输入框的只读属性
        $('input[name=walletPwd]').removeAttr("readonly");

    }
    $(function() {
        $('#editKeychainERC20Form').form({
            url : '${ctx}/bitpay/bitpayKeychainErc20/save',
            onSubmit : function() {
                progressLoad();
            },
            success : function(result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editKeychainERC20Form').find('input[name="csrf"]').val(result.csrf);
                }
            }
        });
        $("#editKeychainERC20Form").parent().css("overflow-y", "scroll");
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false,required:true" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editKeychainERC20Form" method="post">
        	<input name="id" type="hidden"  value="${keychainERC20.id}" />
            <table class="grid">
                <tr>
                    <td>钱包ID</td>
                    <td colspan="3">
                        <input name="walletId"  placeholder=""  style="width: 374px;"
                            value="${keychainERC20.walletId}" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>钱包名称</td>
                    <td colspan="3">
                        <input name="walletName" placeholder="请输入钱包名称" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                               data-options="required:true,validType:['length[0,16]']" value="${keychainERC20.walletName}" /></td>
                </tr>
                <tr>
                    <td>钱包密码</td>
                    <td colspan="3">
                        <input id="walletPwd" name="walletPwd"  value="${keychainERC20.walletPwd}" readonly style="width: 374px;" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>币种</td>
                    <td colspan="3"><input name="coin" placeholder="请输入币种" class="easyui-validatebox easyui-textbox" style="width: 374px;"
                                           data-options="required:true,validType:['length[0,16]']" value="${keychainERC20.coin}" /></td>
                </tr>
                <tr>
                    <td>证券信息ID</td>
                    <td colspan="3"><input id="stockinfoId" name="stockinfoId" class="easyui-combobox" name="language" style="width: 374px;"
                                           placeholder="请输入证券信息id" value="${keychainERC20.stockinfoId}" data-options="required:true,editable:false,
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_ERC20_TOKEN%>', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  /></td>
                </tr>
                <tr>
                    <td>钱包类型</td>
                    <td colspan="3"><input data-options="required:true"  name="walletType" type="radio" value="1" <c:if test="${empty keychainERC20.walletType || keychainERC20.walletType==1}">checked</c:if> />付款热钱包
	                                <input data-options="required:true" name="walletType" type="radio" value="2" <c:if test="${keychainERC20.walletType==2}">checked</c:if> />付款冷钱包
                                    <input data-options="required:true" name="walletType" type="radio" value="3" <c:if test="${keychainERC20.walletType==3}">checked</c:if> />归集eth费用钱包
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>