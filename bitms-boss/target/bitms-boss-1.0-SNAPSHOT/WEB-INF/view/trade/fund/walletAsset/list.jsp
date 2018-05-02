<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var walletAccountDataGrid;
    $(function () {
        walletAccountDataGrid = $('#walletAccountDataGrid').datagrid({
            url: '${ctx}/fund/walletAsset/data',
            fit: true,
            striped: true,
            rownumbers: true,
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
                    width: '120',
                    title: '证券代码',
                    field: 'stockCode'
                },{
                    width: '100',
                    title: '方向',
                    field: 'direction'
                }, {
                    width: '60',
                    title: '价格',
                    field: 'price',
                    sortable: true
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
                }
                ]],
            columns: [[
                {
                    width: '150',
                    title: '已成功充值累计',
                    field: 'chargedTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '已成功提币累计',
                    field: 'withdrawedTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '申请中提币累计',
                    field: 'withdrawingTotal',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '300',
                    title: '备注',
                    field: 'remark'
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
            }
        });
    });

    function searchWalletAssetFun() {
        walletAccountDataGrid.datagrid('load', $.serializeObject($('#walletAccountAssetForm')));
    }
    function cleanWalletAssetFun() {
        $('#walletAccountAssetForm input').val('');
        walletAccountDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="walletAccountAssetForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="spot_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <td>资产分类</td>
                    <td>
                        <input  id="spot_stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择资产分类"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchWalletAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanWalletAssetFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="walletAccountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
