<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    function format (num) {
        return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
    }
    var debitSumDataGrid;
    debitSumDataGrid = $('#debitSumDataGrid').datagrid({
        url: '${ctx}/fund/debitAsset/debitSum/debitSumData',
        striped: true,
        rownumbers: true,
        pagination: true,
        queryParams:{relatedStockinfoId:<%=FundConsts.WALLET_BTC2USD_TYPE%>},
        singleSelect: true,
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50, 100],
        frozenColumns:[[
             {
                width: '130',
                title: '证券ID',
                field: 'stockinfoId',
                sortable: true
            }, {
                width: '120',
                title: '证券代码',
                field: 'stockCode',
                sortable: true
            }, {
                width: '130',
                title: '关联证券ID',
                field: 'relatedStockinfoId',
                sortable: true
            }, {
                width: '120',
                title: '关联证券代码',
                field: 'relatedStockCode',
                sortable: true
            }, {
                width: '120',
                title: '交易对或专区',
                field: 'relatedStockName',
                sortable: true
            },{
                width: '200',
                title: '借贷总金额',
                field: 'debitAmt',
                align:'right',
                formatter: function (value, row, index) {
                    return format(value);
                }
            }
        ]],
        columns: [[
        ]],
        onLoadSuccess: function (data) {
            //用户未登录时刷新页面
            var codeNum = JSON.stringify(data.code);
            if(codeNum==2003){
                window.location.reload();
            }
        },
        onClickRow:function(rowIndex, rowData){
           }
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'center',fit:true,border:false">
        <table id="debitSumDataGrid" data-options="fit:true,border:false,title:'借款资产统计'"></table>
    </div>
</div>