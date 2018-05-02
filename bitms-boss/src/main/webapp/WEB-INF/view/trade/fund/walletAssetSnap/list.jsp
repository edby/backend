<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 60px; overflow: hidden;background-color: #fff">
        <form id="walletSnapAccountAssetForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <td>资产分类</td>
                    <td>
                        <input  name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择资产分类" data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="4">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSpotAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSpotAssetFun();">清空</a>
                    </td>
                </tr>
                <tr>
                    <th width="80px">风控基价:</th>
                    <td width="80px" id="indexPrice_wallet">&nbsp;</td>
                    <th width="80px">最新成交价:</th>
                    <td width="80px" id="paltformPrice_wallet">&nbsp;</td>
                    <th width="80px">溢价:</th>
                    <td width="80px" id="premiumPrice_wallet">&nbsp;</td>
                    <th width="80px">溢价率:</th>
                    <td width="80px" id="premiumRate_wallet">&nbsp;</td>
                    <th width="80px">快照时间:</th>
                    <td width="140px" id="marketTimestamp_wallet">&nbsp;</td>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="walletSnapAccountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<script type="text/javascript">
    var walletSnapAccountDataGrid;
    $(function () {
        walletSnapAccountDataGrid = $('#walletSnapAccountDataGrid').datagrid({
            url: '${ctx}/fund/walletAssetSnap/data',
            fit: true,
            striped: true,
            rownumbers: true,
            queryParams: {
            },
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '150',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '80',
                    title: '证券代码',
                    field: 'stockCode'
                },{
                    width: '100',
                    title: '方向',
                    field: 'direction',
                    hidden:true
                }, {
                    width: '150',
                    title: '价格',
                    field: 'price',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    },
                    hidden:true
                },{
                    width: '150',
                    title: '当前数量',
                    field: 'amount',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '冻结数量',
                    field: 'frozenAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '已成功充值累计数量',
                    field: 'chargedTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '已成功提币累计数量',
                    field: 'withdrawedTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '提币处理中累计数量',
                    field: 'withdrawingTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '300',
                    title: '备注',
                    field: 'remark',
                    hidden:true
                },{
                    width: '150',
                    title: '修改时间',
                    field: 'updateDate',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }
            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                else
                {
                    getMarketSnap();
                }
            }
        });
    });
    function getMarketSnap() {
        $.post('${ctx}/fund/marketSnap/data', {
        }, function (result) {
            if (result.code == 200) {
                $("#paltformPrice_wallet").html(result.object.paltformPrice.toFixed(8));
                $("#indexPrice_wallet").html(result.object.indexPrice.toFixed(8));
                $("#premiumPrice_wallet").html(result.object.premiumPrice.toFixed(8));
                $("#premiumRate_wallet").html(result.object.premiumRate.toFixed(8));
                $("#marketTimestamp_wallet").html(getFormatDateByLong(result.object.marketTimestamp*1, "yyyy-MM-dd hh:mm:ss"));
            }
        }, 'JSON');
    }
    function searchSpotAssetFun() {
        walletSnapAccountDataGrid.datagrid('load', $.serializeObject($('#walletSnapAccountAssetForm')));
    }
    function cleanSpotAssetFun() {
        $('#walletSnapAccountAssetForm input').val('');
        walletSnapAccountDataGrid.datagrid('load', {});
    }
</script>

