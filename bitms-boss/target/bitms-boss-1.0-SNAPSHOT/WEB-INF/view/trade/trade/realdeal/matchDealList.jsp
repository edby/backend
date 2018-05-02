<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var matchDealListDataGrid;
    $(function () {
        matchDealListDataGrid = $('#matchDealListDataGrid').datagrid({
            url: '${ctx}/realdeal/matchDealList/data',
            queryParams:{
                dealStockinfoId:'<%=FundConsts.WALLET_BTC2USD_TYPE%>',tableName:'table'
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
                    width: '120',
                    title: '买单账户ID',
                    field: 'buyAccountId'
                },{
                    width: '120',
                    title: '买单账户',
                    field: 'buyAccountName'
                },{
                    width: '120',
                    title: '卖单账户ID',
                    field: 'sellAccountId'
                },{
                    width: '120',
                    title: '卖单账户',
                    field: 'sellAccountName'
                },{
                    width: '120',
                    title: '买单委托ID',
                    field: 'buyEntrustId'
                },{
                    width: '120',
                    title: '卖单委托ID',
                    field: 'sellEntrustId'
                },{
                    width: '60',
                    title: '交易类型',
                    field: 'tradeType',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '140',
                    title: '业务类别',
                    field: 'businessFlag',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '60',
                    title: '成交方向',
                    field: 'dealDirect',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }
            ]],
            columns: [[ {
                width: '150',
                title: '成交数量',
                field: 'dealAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '成交价格',
                field: 'dealPrice',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '成交金额',
                field: 'dealBalance',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '140',
                title: '成交时间',
                field: 'dealTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            },{
                width: '150',
                title: '买单手续费',
                field: 'buyFee',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '卖单手续费',
                field: 'sellFee',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '200',
                title: '备注',
                field: 'remark'
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


    function searchAccountFundCurrentAllFun() {
        matchDealListDataGrid.datagrid('load',
            $.serializeObject($('#searchAccountRealDealForm')));
    }
    function cleanAccountFundCurrentAllFun() {
        $('#searchAccountRealDealForm input').val('');
        matchDealListDataGrid.datagrid('load',{});
    }
    $(function(){
        $("#dealListTradeTypeListTd").html(dictDropDownOptionsList('dealListTradeTypeList','tradeType', 'tradeType','', '',  'width:102px,'));
        $("#dealListTradeTypeList").combobox({
            valueField:'code',
            textField:'name'
        });
        $("#dealListdealDirectTd").html(dictDropDownOptionsList('dealListdealDirectId','dealDirect', 'entrustDealDirect', '', '', 'width:102px,'));
        $("#dealListdealDirectId").combobox({
            valueField:'code',
            textField:'name'
        });
        $('#realdeal_table_id').switchbutton({
            checked: false,
            onChange: function(checked){
                if(checked)
                {
                    $("#realdeal_table_name").val("tableHis");
                }else
                {
                    $("#realdeal_table_name").val("table");
                }
                matchDealListDataGrid.datagrid('load',
                    $.serializeObject($('#searchAccountRealDealForm')));
            }
        });
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountRealDealForm">
            <table>
                <tr>
                    <th>交易对</th>
                    <td>
                        <input  name="dealStockinfoId"  id="deal_re_id"  class="easyui-combobox" name="language" style="width: 102px;"
                                placeholder="请选择交易对"   value="<%=FundConsts.WALLET_BTC2USD_TYPE%>"  data-options="
									url: '${ctx}/stock/info/allInExchange', method: 'get', valueField:'id',
									required:true,textField:'stockName', groupField:'group'"  >
                    </td>
                    <th>交易类型:</th>
                    <td id="dealListTradeTypeListTd"></td>
                    <th>成交方向:</th>
                    <td id="dealListdealDirectTd"></td>
                    <th>成交开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  style="width: 100px;" placeholder="请输入成交开始时间"/></td>
                    <th>成交结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"  style="width: 100px;" placeholder="请输入成交结束时间"/></td>
                    <td>
                    <td align="left">
                        <input id="realdeal_table_name" name="tableName" value="table" type="hidden" />
                        <input id="realdeal_table_id" style="width: 100px;" class="easyui-switchbutton" data-options="onText:'历史数据',offText:'当前数据'" >
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentAllFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentAllFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="matchDealListDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>