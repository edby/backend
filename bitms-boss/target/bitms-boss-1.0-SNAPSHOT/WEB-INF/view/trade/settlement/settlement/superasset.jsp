<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var superAssetDataGrid;
    $(function () {
        superAssetDataGrid = $('#superAssetDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/superasset/data',
            queryParams:{
                exchangePairMoney:${exchangePairMoney},
                exchangePairVCoin:${exchangePairVCoin}
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
					    width: '60',
					    title: '价格',
					    field: 'price',
					    sortable: true
					},{
                width: '100',
                title: '数量',
                field: 'amount',
                sortable: true
            },{
                width: '80',
                title: '冻结数量',
                field: 'frozenAmt',
                sortable: true
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
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:true">
        <table id="superAssetDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
