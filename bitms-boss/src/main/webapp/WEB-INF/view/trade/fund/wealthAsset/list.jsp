<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var wealthAccountDataGrid;
    $(function () {
        wealthAccountDataGrid = $('#wealthAccountDataGrid').datagrid({
            url: '${ctx}/fund/wealthAsset/data',
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
                    width: '120',
                    title: '理财投资账户',
                    field: 'accountName',
                    sortable: true
                },{
                    width: '120',
                    title: '理财证券信息代码',
                    field: 'stockCode'
                }, {
                    width: '140',
                    title: '理财证券信息关联代码',
                    field: 'relatedStockName',
                    sortable: true
                },{
                    width: '150',
                    title: '理财财富数量或金额',
                    field: 'wealthAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '累计利息',
                    field: 'accumulateInterest',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '100',
                    title: '最后计息日',
                    field: 'lastInterestDay',
                    sortable: true
                },{
                    width: '200',
                    title: '备注',
                    field: 'remark'
                },{
                    width: '200',
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

    function searchWealthAssetFun() {
        wealthAccountDataGrid.datagrid('load', $.serializeObject($('#wealthAccountAssetForm')));
    }
    function cleanWealthAssetFun() {
        $('#wealthAccountAssetForm input').val('');
        wealthAccountDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="wealthAccountAssetForm">
            <table>
                <tr>
                    <th>理财投资账户:</th>
                    <td><input class="easyui-textbox" name="accountName" placeholder="请输入理财投资账户"/></td>
                    <td>资产分类</td>
                    <td>
                        <input  id="spot_stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择资产分类"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/allCoin', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchWealthAssetFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanWealthAssetFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="wealthAccountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
