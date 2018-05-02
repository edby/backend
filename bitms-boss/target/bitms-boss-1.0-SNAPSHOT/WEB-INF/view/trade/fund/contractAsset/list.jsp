<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var contractAssetDataGrid;
    $(function () {
        contractAssetDataGrid = $('#contractAssetDataGrid').datagrid({
            url: '${ctx}/fund/contractAsset/data',
            queryParams:{
                relatedStockinfoId:'<%=FundConsts.WALLET_BTC2USDX_TYPE%>'
            },
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
                }, {
                    width: '120',
                    title: '关联证券代码',
                    field: 'relatedStockCode'
                },{
                    width: '100',
                    title: '方向',
                    field: 'direction'
                }, {
                    width: '150',
                    title: '价格',
                    field: 'price',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
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
                    title: '负债数量',
                    field: 'debitAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '净数量',
                    field: 'id2',
                    formatter: function (value, row, index) {
                        var debitAmt=0;
                        if(row.debitAmt !=null && row.debitAmt!='')
                        {
                            debitAmt=row.debitAmt;
                        }
                        return (row.amount-debitAmt).toFixed(12);
                    }
                },{
                    width: '100',
                    title: '本期期初数量',
                    field: 'initialAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '100',
                    title: '本期流入数量',
                    field: 'flowInAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '100',
                    title: '本期流出数量',
                    field: 'flowOutAmt',
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
    
    function searchContractAssetFun() {
    	contractAssetDataGrid.datagrid('load', 
    			 $.serializeObject($('#searchContractAssetForm')));
    }
    function cleanContractAssetFun() {
        $('#searchContractAssetForm input').val('');
        contractAssetDataGrid.datagrid('load',{});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchContractAssetForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="check_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <!--
                    <td>证券代码</td>
                    <td>
                        <input  id="stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    -->
                    <td>交易对</td>
                    <td>
                        <input id="relatedStockinfoId" name="relatedStockinfoId" class="easyui-combobox" name="language"
                               placeholder="请选择关联证券" value="<%=FundConsts.WALLET_BTC2USDX_TYPE%>" data-options="
								url: '${ctx}/stock/info/allContractExchange', method: 'get', valueField:'id',
								textField:'stockName', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchContractAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanContractAssetFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="contractAssetDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
