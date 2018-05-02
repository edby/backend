<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var spotAccountDataGrid;
    $(function () {
        spotAccountDataGrid = $('#spotAccountDataGrid').datagrid({
            url: '${ctx}/fund/spotAsset/data',
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

    function searchSpotAssetFun() {
        spotAccountDataGrid.datagrid('load', $.serializeObject($('#spotAccountAssetForm')));
    }
    function cleanSpotAssetFun() {
        $('#spotAccountAssetForm input').val('');
        spotAccountDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="spotAccountAssetForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="wallet_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <td>资产分类</td>
                    <td>
                        <input  id="wallet_stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择资产分类"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSpotAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSpotAssetFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="spotAccountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
