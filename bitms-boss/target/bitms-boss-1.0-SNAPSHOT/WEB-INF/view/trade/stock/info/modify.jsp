<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<style>
    #tbodyxxx input[readonly] {
        background-color: #efefef;
    }
</style>
<script type="text/javascript">
    $(function () {
        $('#editStockInfoForm').form({
            url: '${ctx}/stock/info/save',
            onSubmit: function () {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success: function (result) {
                progressClose();
                var result = $.parseJSON(result);
                if (result.code == ajax_result_success_code) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.message, 'error');
                    $('#editStockInfoForm').find('input[name="csrf"]').val(result.csrf);

                }
            }
        });
        $("#editStockInfoForm").parent().css("overflow-y", "scroll");

        $("#canRechargeTd").html(dictDropDownOptions('canRecharge', 'canRecharge', 'yesOrNo', '${stockInfo.canRecharge}', 'required:true,', 'width:142px'));
        $("#canRecharge").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#canWithdrawTd").html(dictDropDownOptions('canWithdraw', 'canWithdraw', 'yesOrNo', '${stockInfo.canWithdraw}', 'required:true,', 'width:142px'));
        $("#canWithdraw").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#canTradeTd").html(dictDropDownOptions('canTrade', 'canTrade', 'yesOrNo', '${stockInfo.canTrade}', 'required:true,', 'width:142px'));
        $("#canTrade").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#canBorrowTd").html(dictDropDownOptions('canBorrow', 'canBorrow', 'yesOrNo', '${stockInfo.canBorrow}', 'required:true,', 'width:142px'));
        $("#canBorrow").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#isActiveTd").html(dictDropDownOptions('isActive', 'isActive', 'yesOrNo', '${stockInfo.isActive}', 'required:true,', 'width:142px'));
        $("#isActive").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#canConversionTd").html(dictDropDownOptions('canConversion', 'canConversion', 'yesOrNo', '${stockInfo.canConversion}', 'required:true,', 'width:142px'));
        $("#canConversion").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#maxLongLeverSwitchTd").html(dictDropDownOptions('maxLongLeverSwitch', 'maxLongLeverSwitch', 'yesOrNo', '${stockInfo.maxLongLeverSwitch}', 'required:true,', 'width:142px'));
        $("#maxLongLeverSwitch").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#maxShortLeverSwitchTd").html(dictDropDownOptions('maxShortLeverSwitch', 'maxShortLeverSwitch', 'yesOrNo', '${stockInfo.maxShortLeverSwitch}', 'required:true,', 'width:142px'));
        $("#maxShortLeverSwitch").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#isExchangeTd").html(dictDropDownOptions('isExchange', 'isExchange', 'yesOrNo', '${stockInfo.isExchange}', 'required:true,', 'width:142px'));
        $("#isExchange").combobox({
            valueField: 'code',
            textField: 'name',
            readonly: true
        });

        $("#canWealthTd").html(dictDropDownOptions('canWealth', 'canWealth', 'yesOrNo', '${stockInfo.canWealth}', 'required:true,', 'width:142px'));
        $("#canWealth").combobox({
            valueField: 'code',
            textField: 'name'
        });

        $("#stockTypeTd").html(dictDropDownOptions('stockType', 'stockType', 'stockType', '${stockInfo.stockType}', 'required:true,', 'width:142px'));
        $("#stockType").combobox({
            valueField: 'code',
            textField: 'name',
            readonly: true
        });


        (function ($) {
            /**
             * 功能扩展
             * 新增 validatebox 校验规则
             */
            var arr = new Array();
            arr[0] = "limitPrice";
            arr[1] = "marketPrice";
            arr[2] = "limitAndMarket";
            $.extend($.fn.validatebox.defaults.rules, {
                contents: {
                    validator: function (value, param) {
                        return $.inArray(value, arr) > -1;
                    },
                    message: '开放委托类型范围(limitPrice、marketPrice、limitAndMarket)'
                }
            });
        })(jQuery);
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form:form id="editStockInfoForm" method="post">
            <input name="id" type="hidden" value="${stockInfo.id}">
            <c:if test="${stockInfo.stockType eq 'digitalCoin'}">
                <%-- 纯数字货币--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否充值</td>
                        <td id="canRechargeTd">
                        </td>
                        <td>是否提现</td>
                        <td id="canWithdrawTd">
                        </td>
                        </td>
                        <td>能否互转</td>
                        <td id="canConversionTd">
                        </td>
                    </tr>
                    <tr>
                        <td>是否启用</td>
                        <td id="isActiveTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否可理财</td>
                        <td id="canWealthTd">
                        <td>理财日收益率${stockInfo.canWealth eq 'no'?'<font color="red">*不可修改</font>':''}</td>
                        <td>
                            <input name="wealthDayRate" type="text" placeholder="请输入理财日收益率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6"
                                   value="${stockInfo.canWealth eq 'yes'?stockInfo.wealthDayRate:0.0}">
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;
                        </td>
                    </tr>

                    <tr>
                        <td>未认证用户提现当日额度上限</td>
                        <td>
                            <input name="unauthUserWithdrawDayUpper" type="text" placeholder="请输入未认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:0,precision:0"
                                   value="${stockInfo.unauthUserWithdrawDayUpper}">
                        </td>
                        <td>已认证用户提现当日额度上限</td>
                        <td>
                            <input name="authedUserWithdrawDayUpper" type="text" placeholder="请输入已认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:9999999999,precision:0"
                                   value="${stockInfo.authedUserWithdrawDayUpper}">
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="5">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>

            </c:if>
            <c:if test="${stockInfo.stockType eq 'cashCoin'}">
                <%-- 纯数字货币--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否充值</td>
                        <td id="canRechargeTd">
                        </td>
                        <td>是否提现</td>
                        <td id="canWithdrawTd">
                        </td>
                        </td>
                        <td>能否互转</td>
                        <td id="canConversionTd">
                        </td>
                    </tr>
                    <tr>
                        <td>是否启用</td>
                        <td id="isActiveTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否可理财</td>
                        <td id="canWealthTd">
                        <td>理财日收益率${stockInfo.canWealth eq 'no'?'<font color="red">*不可修改</font>':''}</td>
                        <td>
                            <input name="wealthDayRate" type="text" placeholder="请输入理财日收益率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6"
                                   value="${stockInfo.canWealth eq 'yes'?stockInfo.wealthDayRate:0.0}">
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>未认证用户提现当日额度上限</td>
                        <td>
                            <input name="unauthUserWithdrawDayUpper" type="text" placeholder="请输入未认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:0,precision:0"
                                   value="${stockInfo.unauthUserWithdrawDayUpper}">
                        </td>
                        <td>已认证用户提现当日额度上限</td>
                        <td>
                            <input name="authedUserWithdrawDayUpper" type="text" placeholder="请输入已认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:9999999999,precision:0"
                                   value="${stockInfo.authedUserWithdrawDayUpper}">
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="5">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>

            </c:if>
            <c:if test="${stockInfo.stockType eq 'erc20Token'}">
                <%-- ERC20 TOKEN--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否充值</td>
                        <td id="canRechargeTd">
                        </td>
                        <td>是否提现</td>
                        <td id="canWithdrawTd">
                        </td>
                        </td>
                        <td>能否互转</td>
                        <td id="canConversionTd">
                        </td>
                    </tr>
                    <tr>
                        <td>能否交易</td>
                        <td id="canTradeTd">
                        </td>
                        <td>是否启用</td>
                        <td id="isActiveTd">
                        </td>
                    </tr>
                    <tr>
                        <td>token小数精度</td>
                        <td>
                            <input name="tokenDecimals" type="text" placeholder="请输入token小数精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,readonly:true,min:0,max:30,precision:0"
                                   value="${stockInfo.tokenDecimals}">
                        </td>
                        <td>token合约地址</td>
                        <td><input name="tokenContactAddr" type="text" placeholder="请输入token合约地址" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,readonly:true,validType:['length[0,42]']" value="${stockInfo.tokenContactAddr}">
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>未认证用户提现当日额度上限</td>
                        <td>
                            <input name="unauthUserWithdrawDayUpper" type="text" placeholder="请输入未认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:0,precision:0"
                                   value="${stockInfo.unauthUserWithdrawDayUpper}">
                        </td>
                        <td>已认证用户提现当日额度上限</td>
                        <td>
                            <input name="authedUserWithdrawDayUpper" type="text" placeholder="请输入已认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:9999999999,precision:0"
                                   value="${stockInfo.authedUserWithdrawDayUpper}">
                        </td>
                        <td>小额提现热签临界值数量(<=)</td>
                        <td>
                            <input name="smallWithdrawHotSignValue" type="text" placeholder="请输入小额提现热签临界值数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:9999999999,precision:0"
                                   value="${stockInfo.smallWithdrawHotSignValue}">
                        </td>
                    </tr>
                    <tr>
                        <td>小额充值手续费数量(>=收取手续费)</td>
                        <td>
                            <input name="smallDepositFeeValue" type="text" placeholder="请输入未认证用户提现当日额度上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:0,precision:6"
                                   value="${stockInfo.smallDepositFeeValue}">
                        </td>
                        <td>小额充值标准临界值数量(大于不收取手续费)</td>
                        <td>
                            <input name="smallDepositStandardValue" type="text" placeholder="小额充值标准临界值数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:9999999999,precision:6"
                                   value="${stockInfo.smallDepositStandardValue}">
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="5">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>

            </c:if>
            <c:if test="${stockInfo.stockType eq 'pureSpot'}">
                <%--纯现货交易--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                        <td>能否交易</td>
                        <td id="canTradeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>是否启用</td>
                        <td id="isActiveTd">
                        </td>
                    </tr>
                    <tr>
                        <td>单笔委托买入数量上限</td>
                        <td>
                            <input name="maxSingleBuyEntrustAmt" type="text" placeholder="请输入单笔委托买入数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleBuyEntrustAmt}"></td>
                        </td>
                        <td>单笔委托卖出数量上限</td>
                        <td>
                            <input name="maxSingleSellEntrustAmt" type="text" placeholder="请输入单笔委托卖出数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleSellEntrustAmt}"></td>
                        </td>
                        <td>买入最小数量</td>
                        <td>
                            <input name="buyMinAmount" type="text" placeholder="请输入买入最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.buyMinAmount}">
                        </td>
                        <td>卖出最小数量</td>
                        <td>
                            <input name="sellMinAmount" type="text" placeholder="请输入卖出最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.sellMinAmount}">
                        </td>
                    </tr>
                    <tr>
                        <td>买入价格精度</td>
                        <td>
                            <input name="buyPricePrecision" type="text" placeholder="请输入买入价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.buyPricePrecision}">
                        </td>
                        <td>买入数量精度</td>
                        <td>
                            <input name="buyAmountPrecision" type="text" placeholder="请输入买入数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.buyAmountPrecision}"></td>
                        </td>
                        <td>卖出价格精度</td>
                        <td>
                            <input name="sellPricePrecision" type="text" placeholder="请输入卖出价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.sellPricePrecision}">
                        </td>
                        <td>卖出数量精度</td>
                        <td>
                            <input name="sellAmountPrecision" type="text" placeholder="请输入卖出数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.sellAmountPrecision}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入小限价上浮%</td>
                        <td>
                            <input name="buyMinLimitPriceUpPercent" type="text" placeholder="请输入买入小限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMinLimitPriceUpPercent}">
                        </td>
                        <td>卖出小限价下浮%</td>
                        <td>
                            <input name="sellMinLimitPriceDownPercent" type="text" placeholder="请输入卖出小限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMinLimitPriceDownPercent}"></td>
                        </td>
                        <td>盘口累计量比例条分母</td>
                        <td>
                            <input name="entrustAccumDenom" type="text" placeholder="请输入盘口累计量比例条分母" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:1,precision:0"
                                   value="${stockInfo.entrustAccumDenom}">
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="7">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${stockInfo.stockType eq 'leveragedSpot'}">
                <%--杠杆现货--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                        <td>能否交易</td>
                        <td id="canTradeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>是否启用</td>
                        <td id="isActiveTd">
                        </td>
                    </tr>
                    <tr>
                        <td>做多杠杆开关</td>
                        <td id="maxLongLeverSwitchTd">
                        </td>
                        <td>做多杠杆倍数</td>
                        <td>
                            <input name="maxLongLever" type="text" placeholder="请输入做多做大杠杆" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:2" value="${stockInfo.maxLongLever}"></td>
                        </td>
                        <td>做空杠杆开关</td>
                        <td id="maxShortLeverSwitchTd">
                        </td>
                        <td>做空杠杆倍数</td>
                        <td>
                            <input name="maxShortLever" type="text" placeholder="请输入做空做大杠杆" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:2" value="${stockInfo.maxShortLever}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>单笔委托买入数量上限</td>
                        <td>
                            <input name="maxSingleBuyEntrustAmt" type="text" placeholder="请输入单笔委托买入数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleBuyEntrustAmt}"></td>
                        </td>
                        <td>单笔委托卖出数量上限</td>
                        <td>
                            <input name="maxSingleSellEntrustAmt" type="text" placeholder="请输入单笔委托卖出数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleSellEntrustAmt}"></td>
                        </td>
                        <td>多单爆仓价提前比例</td>
                        <td>
                            <input name="closePositionLongPrePercent" type="text" placeholder="请输入多单爆仓价提前比例" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:3" value="${stockInfo.closePositionLongPrePercent}"></td>
                        </td>

                        <td>空单爆仓价提前比例</td>
                        <td>
                            <input name="closePositionShortPrePercent" type="text" placeholder="请输入空单爆仓价提前比例" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:3" value="${stockInfo.closePositionShortPrePercent}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入大限价上浮%</td>
                        <td>
                            <input name="buyMaxLimitPriceUpPercent" type="text" placeholder="请输入买入大限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMaxLimitPriceUpPercent}">
                        </td>
                        <td>买入大限价下浮%</td>
                        <td>
                            <input name="buyMaxLimitPriceDownPercent" type="text" placeholder="请输入买入大限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMaxLimitPriceDownPercent}"></td>
                        </td>
                        <td>卖出大限价上浮%</td>
                        <td>
                            <input name="sellMaxLimitPriceUpPercent" type="text" placeholder="请输入卖出大限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMaxLimitPriceUpPercent}">
                        </td>
                        <td>卖出大限价下浮%</td>
                        <td>
                            <input name="sellMaxLimitPriceDownPercent" type="text" placeholder="请输入卖出大限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMaxLimitPriceDownPercent}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入价格精度</td>
                        <td>
                            <input name="buyPricePrecision" type="text" placeholder="请输入买入价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.buyPricePrecision}">
                        </td>
                        <td>买入数量精度</td>
                        <td>
                            <input name="buyAmountPrecision" type="text" placeholder="请输入买入数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.buyAmountPrecision}"></td>
                        </td>
                        <td>卖出价格精度</td>
                        <td>
                            <input name="sellPricePrecision" type="text" placeholder="请输入卖出价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.sellPricePrecision}">
                        </td>
                        <td>卖出数量精度</td>
                        <td>
                            <input name="sellAmountPrecision" type="text" placeholder="请输入卖出数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.sellAmountPrecision}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入最小数量</td>
                        <td>
                            <input name="buyMinAmount" type="text" placeholder="请输入买入最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.buyMinAmount}">
                        </td>
                        <td>卖出最小数量</td>
                        <td>
                            <input name="sellMinAmount" type="text" placeholder="请输入卖出最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.sellMinAmount}">
                        </td>
                        <td>数字货币借款日利率</td>
                        <td>
                            <input name="digitBorrowDayRate" type="text" placeholder="请输入数字货币借款日利率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6" value="${stockInfo.digitBorrowDayRate}">
                        </td>
                        <td>法定货币借款日利率</td>
                        <td>
                            <input name="legalBorrowDayRate" type="text" placeholder="请输入法定货币借款日利率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6" value="${stockInfo.legalBorrowDayRate}">
                        </td>
                    </tr>

                    <tr>
                        <td>借款总额度(交易标的)</td>
                        <td>
                            <input name="tradeDebitTotal" type="text" placeholder="请输入借款总额度(交易标的)" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:10000000000000,precision:2" value="${stockInfo.tradeDebitTotal}">
                        </td>
                        <td>计价标的借款总额度</td>
                        <td>
                            <input name="capitalDebitTotal" type="text" placeholder="请输入计价标的借款总额度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:10000000000000,precision:2" value="${stockInfo.capitalDebitTotal}">
                        </td>
                        <td>做多溢价不高于百分比</td>
                        <td>
                            <input name="maxLongFuse" type="text" placeholder="请输入做多溢价不高于百分比" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:4" value="${stockInfo.maxLongFuse}">
                        </td>
                        <td>做空负溢价不高于百分比</td>
                        <td>
                            <input name="maxShortFuse" type="text" placeholder="请输入做空负溢价不高于百分比" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:4" value="${stockInfo.maxShortFuse}">
                        </td>
                    </tr>
                    <tr>
                        <td>盘口累计量比例条分母</td>
                        <td>
                            <input name="entrustAccumDenom" type="text" placeholder="请输入盘口累计量比例条分母" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:1,precision:0"
                                   value="${stockInfo.entrustAccumDenom}">
                        </td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="7">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${stockInfo.stockType eq 'spotContract'}">
                <%--现货合约--%>
                <table class="grid">
                    <tr>
                        <td>证券代码</td>
                        <td>
                            <input name="stockCode" type="text" placeholder="请输入证券代码" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,32]']" value="${stockInfo.stockCode}">
                        </td>
                        <td>证券名称</td>
                        <td><input name="stockName" type="text" placeholder="请输入证券名称" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.stockName}">
                        </td>
                        <td>证券类型<font color="red">*不可修改</font></td>
                        <td id="stockTypeTd">
                        </td>
                        <td>交割周期</td>
                        <td>
                            <input name="settlementCycle" type="text" placeholder="请输入交割周期" class="easyui-validatebox easyui-textbox" style="width: 140px;"
                                   data-options="required:true,validType:['length[0,16]']" value="${stockInfo.settlementCycle}">
                        </td>
                    </tr>
                    <tr>
                        <td>做多杠杆开关</td>
                        <td id="maxLongLeverSwitchTd">
                        </td>
                        <td>做多杠杆倍数</td>
                        <td>
                            <input name="maxLongLever" type="text" placeholder="请输入做多做大杠杆" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:2" value="${stockInfo.maxLongLever}"></td>
                        </td>
                        <td>做空杠杆开关</td>
                        <td id="maxShortLeverSwitchTd">
                        </td>
                        <td>做空杠杆倍数</td>
                        <td>
                            <input name="maxShortLever" type="text" placeholder="请输入做空做大杠杆" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:2" value="${stockInfo.maxShortLever}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>单笔委托买入数量上限</td>
                        <td>
                            <input name="maxSingleBuyEntrustAmt" type="text" placeholder="请输入单笔委托买入数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleBuyEntrustAmt}"></td>
                        </td>
                        <td>单笔委托卖出数量上限</td>
                        <td>
                            <input name="maxSingleSellEntrustAmt" type="text" placeholder="请输入单笔委托卖出数量上限" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,precision:2" value="${stockInfo.maxSingleSellEntrustAmt}"></td>
                        </td>
                        <td>多单爆仓价提前比例</td>
                        <td>
                            <input name="closePositionLongPrePercent" type="text" placeholder="请输入多单爆仓价提前比例" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:3" value="${stockInfo.closePositionLongPrePercent}"></td>
                        </td>

                        <td>空单爆仓价提前比例</td>
                        <td>
                            <input name="closePositionShortPrePercent" type="text" placeholder="请输入空单爆仓价提前比例" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:20,precision:3" value="${stockInfo.closePositionShortPrePercent}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入大限价上浮%</td>
                        <td>
                            <input name="buyMaxLimitPriceUpPercent" type="text" placeholder="请输入买入大限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMaxLimitPriceUpPercent}">
                        </td>
                        <td>买入大限价下浮%</td>
                        <td>
                            <input name="buyMaxLimitPriceDownPercent" type="text" placeholder="请输入买入大限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMaxLimitPriceDownPercent}"></td>
                        </td>
                        <td>买入小限价上浮%</td>
                        <td>
                            <input name="buyMinLimitPriceUpPercent" type="text" placeholder="请输入买入小限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.buyMinLimitPriceUpPercent}">
                        </td>
                        <td>盘口累计量比例条分母</td>
                        <td>
                            <input name="entrustAccumDenom" type="text" placeholder="请输入盘口累计量比例条分母" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,max:999999,min:1,precision:0"
                                   value="${stockInfo.entrustAccumDenom}">
                        </td>
                    </tr>
                    <tr>
                        <td>卖出大限价上浮%</td>
                        <td>
                            <input name="sellMaxLimitPriceUpPercent" type="text" placeholder="请输入卖出大限价上浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMaxLimitPriceUpPercent}">
                        </td>
                        <td>卖出大限价下浮%</td>
                        <td>
                            <input name="sellMaxLimitPriceDownPercent" type="text" placeholder="请输入卖出大限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMaxLimitPriceDownPercent}"></td>
                        </td>
                        <td>卖出小限价下浮%</td>
                        <td>
                            <input name="sellMinLimitPriceDownPercent" type="text" placeholder="请输入卖出小限价下浮%" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:100,precision:4" value="${stockInfo.sellMinLimitPriceDownPercent}"></td>
                        </td>
                        <td id="canTradeTd">
                        </td>
                    </tr>
                    <tr>
                        <td>买入价格精度</td>
                        <td>
                            <input name="buyPricePrecision" type="text" placeholder="请输入买入价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.buyPricePrecision}">
                        </td>
                        <td>买入数量精度</td>
                        <td>
                            <input name="buyAmountPrecision" type="text" placeholder="请输入买入数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.buyAmountPrecision}"></td>
                        </td>
                        <td>卖出价格精度</td>
                        <td>
                            <input name="sellPricePrecision" type="text" placeholder="请输入卖出价格精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:8,precision:0" value="${stockInfo.sellPricePrecision}">
                        </td>
                        <td>卖出数量精度</td>
                        <td>
                            <input name="sellAmountPrecision" type="text" placeholder="请输入卖出数量精度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:6,precision:0" value="${stockInfo.sellAmountPrecision}"></td>
                        </td>
                    </tr>
                    <tr>
                        <td>买入最小数量</td>
                        <td>
                            <input name="buyMinAmount" type="text" placeholder="请输入买入最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.buyMinAmount}">
                        </td>
                        <td>卖出最小数量</td>
                        <td>
                            <input name="sellMinAmount" type="text" placeholder="请输入卖出最小数量" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1000,precision:4" value="${stockInfo.sellMinAmount}">
                        </td>
                        <td>数字货币借款日利率</td>
                        <td>
                            <input name="digitBorrowDayRate" type="text" placeholder="请输入数字货币借款日利率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6" value="${stockInfo.digitBorrowDayRate}">
                        </td>
                        <td>法定货币借款日利率</td>
                        <td>
                            <input name="legalBorrowDayRate" type="text" placeholder="请输入法定货币借款日利率" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:6" value="${stockInfo.legalBorrowDayRate}">
                        </td>
                    </tr>
                    <tr>
                        <td>借款总额度(交易标的)</td>
                        <td>
                            <input name="tradeDebitTotal" type="text" placeholder="请输入借款总额度(交易标的)" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:10000000000000,precision:2" value="${stockInfo.tradeDebitTotal}">
                        </td>
                        <td>计价标的借款总额度</td>
                        <td>
                            <input name="capitalDebitTotal" type="text" placeholder="请输入计价标的借款总额度" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:10000000000000,precision:2" value="${stockInfo.capitalDebitTotal}">
                        </td>
                        <td>做多溢价不高于百分比</td>
                        <td>
                            <input name="maxLongFuse" type="text" placeholder="请输入做多溢价不高于百分比" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:4" value="${stockInfo.maxLongFuse}">
                        </td>
                        <td>做空负溢价不高于百分比</td>
                        <td>
                            <input name="maxShortFuse" type="text" placeholder="请输入做空负溢价不高于百分比" class="easyui-numberbox" style="width: 140px;"
                                   data-options="required:true,min:0,max:1,precision:4" value="${stockInfo.maxShortFuse}">
                        </td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td colspan="7">
                            <textarea name="remark" style="width: 1045px;height: 50px;">${stockInfo.remark}</textarea>
                        </td>
                    </tr>
                </table>
            </c:if>
        </form:form>
    </div>
</div>