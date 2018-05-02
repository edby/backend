<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var sheetBalanceDataGrid;
    $(function () {
        sheetBalanceDataGrid = $('#sheetBalanceDataGrid').datagrid({
            url: '${ctx}/fund/sheetBalance/data',
            queryParams:{
               
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
                    title: '资产负债日',
                    field: 'balanceDay'
                },{
                    width: '150',
                    title: '账户名',
                    field: 'accountName'
                }, {
                    width: '120',
                    title: '证券代码',
                    field: 'stockCode'
                }, {
                    width: '120',
                    title: '关联证券代码',
                    field: 'relatedStockCode'
                }, {
                    width: '120',
                    title: '关联证券名称',
                    field: 'relatedStockName'
                }
                ]],

            columns: [[
                {
                    width: '100',
                    title: '账户类型',
                    field: 'accountType'
                },{
                    width: '150',
                    title: '资产数量',
                    field: 'assetAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '冻结数量',
                    field: 'assetFrozenAmt',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '负债数量',
                    field: 'debitAmt',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '资产净数量',
                    field: 'balanceAmt',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '300',
                    title: '备注',
                    field: 'remark'
                },{
                    width: '150',
                    title: '新增时间',
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
    
    function searchSheetBalanceFun() {
    	sheetBalanceDataGrid.datagrid('load', 
    			 $.serializeObject($('#searchSheetBalanceForm')));
    }
    function cleanSheetBalanceFun() {
        $('#searchSheetBalanceForm input').val('');
        sheetBalanceDataGrid.datagrid('load',{});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchSheetBalanceForm">
            <table>
                <tr>
                    <th>资产负债日(YYYYMMDD):</th>
                    <td><input class="easyui-numberbox"  data-options="min:0,precision:0,max:99999999" name="balanceDay" placeholder="请输入资产负债日"/></td>
                    <td>证券代码</td>
                    <td>
                        <input  id="stockinfoId" name="stockinfoId"  class="easyui-combobox" name="language"
                                placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockCode', groupField:'group'"  >
                    </td>
                    <td>关联证券</td>
                    <td>
                        <input id="relatedStockinfoId" name="relatedStockinfoId" class="easyui-combobox" name="language"
                               placeholder="请选择关联证券"  data-options="
								url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
								textField:'stockName', groupField:'group'"  >
                    </td>
                    <td colspan="2">
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSheetBalanceFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSheetBalanceFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="sheetBalanceDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
