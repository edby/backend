<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var platLossShareDataGrid;
    $(function () {
        platLossShareDataGrid = $('#platLossShareDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/platLossShare/data',
            queryParams:{
                exchangePairMoney:${exchangePairMoney},
                exchangePairVCoin:${exchangePairVCoin}
            },
            striped: true,
            rownumbers: true,
            pagination: false,
            singleSelect: false,
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
            				    title: '数字货币持仓数量',
            				    field: 'personBtc',
            				    sortable: true
            				},{
            	                width: '100',
            	                title: 'BTC总流入数量',
            	                field: 'personIn'
            	            },{
                                width: '100',
                                title: '数字货币总流出数量',
                                field: 'personOut'
                            },{
                                width: '150',
                                title: '平台盈利数字货币数量',
                                field: 'superYingLi'
                            },{
                                width: '150',
                                title: '个人盈利数字货币数量',
                                field: 'personYingLi'
                            },{
                                width: '150',
                                title: '分摊比例%',
                                field: 'id',
                                formatter: function (value, row, index) {
                                    if(row.result == 1 || row.result == true)
                                    {
                                        return 0;
                                    }else
                                    {
                                        return (100*row.needBtc/row.superYingLi).toFixed(2);
                                    }

                                }
                            },{
                                width: '100',
                                title: '分摊数字货币数量',
                                field: 'shareLossAmt'
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
                if (row.monitorMarginRatio<=110 && row.monitorMarginRatio>0){
                    return 'background-color:#FF4500;color:blue;font-weight:bold;';
                }
                else if (row.monitorMarginRatio>110 && row.monitorMarginRatio<=120 ){
                    return 'background-color:#EE9A00;color:blue;font-weight:bold;';
                }
                else if (row.monitorMarginRatio>120 && row.monitorMarginRatio<=150 ){
                    return 'background-color:#EEEE00;color:blue;font-weight:bold;';
                }
                else if (row.monitorMarginRatio<=0 ){
                    return 'background-color:#C4C4C4;color:blue;font-weight:bold;';
                }
                else{
                    return 'background-color:#AEEEEE;color:blue;font-weight:bold;';
                }
            },
            toolbar: '#pingCangToolbar'
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:true">
        <table id="platLossShareDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
