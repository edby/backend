<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var usdxHaveDataGrid;
    $(function () {
        usdxHaveDataGrid = $('#usdxHaveDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/usdxHave/data',
            queryParams:{
                exchangePairMoney:${exchangePairMoney},
                exchangePairVCoin:${exchangePairVCoin}
            },
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
                                title: '账户ID',
                                field: 'accountId',
                                sortable: true
                            },{
							    width: '200',
							    title: '账户名',
							    field: 'accountName',
							    sortable: true
							},{
            				    width: '100',
            				    title: '证券代码',
            				    field: 'stockCode',
            				    sortable: true
            				},{
            	                width: '100',
            	                title: '余额',
            	                field: 'amount'
            	            },{
                                width: '100',
                                title: '冻结余额',
                                field: 'frozenAmt'
                            }

                           ]],       
            columns: [[
                {
                    width: '150',
                    title: '备注',
                    field: 'remark',
                    sortable: true
                }
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            },
            rowStyler:function(index,row){
                if (row.amount>0 ){
                    return 'background-color:yellow;color:blue;font-weight:bold;';
                }
                else if (row.amount==0 ){
                    return 'background-color:green;color:blue;font-weight:bold;';
                }
                else if (row.amount<0 ){
                    return 'background-color:red;color:blue;font-weight:bold;';
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:true">
        <table id="usdxHaveDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
