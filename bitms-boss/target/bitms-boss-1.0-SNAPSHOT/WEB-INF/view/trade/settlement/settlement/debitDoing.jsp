<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var debitDoingDataGrid;
    $(function () {
        debitDoingDataGrid = $('#debitDoingDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/debitDoing/data',
            queryParams:{
                exchangePairMoney:${exchangePairMoney}
                ,exchangePairVCoin:${exchangePairVCoin}
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[{ field:'id',checkbox:true },
                            {
                                width: '120',
                                title: '贷款人账户ID',
                                field: 'lenderAccountId',
                                sortable: true
                            },{
							    width: '200',
							    title: '贷款人账户名',
							    field: 'lenderAccountName',
							    sortable: true
							},{
                                width: '120',
                                title: '借款人账户ID',
                                field: 'borrowerAccountId',
                                sortable: true
                            },{
                                width: '200',
                                title: '借款人账户名',
                                field: 'borrowerAccountName',
                                sortable: true
                            },{
            				    width: '100',
            				    title: '证券代码',
            				    field: 'stockCode',
            				    sortable: true
            				},{
            	                width: '100',
            	                title: '借贷数量或金额',
            	                field: 'debitAmt'
            	            }

                           ]],       
            columns: [[
                {
                    width: '150',
                    title: '备注',
                    field: 'remark',
                    sortable: true
                },{
                    width: '150',
                    title: '监控保证金比例%',
                    field: 'monitorMarginRatio',
                    sortable: true
                },{
                    width: '150',
                    title: '监控描述',
                    field: 'monitorDesc',
                    sortable: true
                }, {
                    width: '140',
                    title: '监控时间',
                    field: 'monitorDate',
                    formatter: function (value, row, index) {
                        if(value!='' && value!=null  && value!=0)
                        {
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");

                        }else
                        {
                            return '';
                        }
                       }
                }, {
                width: '140',
                title: '修改时间',
                field: 'updateDate',
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
        <table id="debitDoingDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
