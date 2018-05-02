<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var entrustXDoingDataGrid;
    $(function () {
        entrustXDoingDataGrid = $('#entrustXDoingDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/entrustXDoing/data',
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
            frozenColumns:[[
                            {
                                width: '120',
                                title: 'ID',
                                field: 'id',
                                sortable: true
                            },{
							    width: '150',
							    title: '账户名',
							    field: 'accountName',
							    sortable: true
							},{
                                width: '150',
                                title: '业务类别',
                                field: 'businessFlag',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    return getDictValueByCode(value);
                                }
                            },{
                                width: '100',
                                title: '交易类型',
                                field: 'tradeType',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    return getDictValueByCode(value);
                                }
                            },{
                                width: '100',
                                title: '委托方向',
                                field: 'entrustDirect',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    return getDictValueByCode(value);
                                }
                            },{
                                width: '100',
                                title: '委托类型',
                                field: 'entrustType',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    return getDictValueByCode(value);
                                }
                            },{
            				    width: '100',
            				    title: '委托数量',
            				    field: 'entrustAmt',
            				    sortable: true
            				},{
            	                width: '100',
            	                title: '委托价格',
            	                field: 'entrustPrice'
            	            },{
                                width: '100',
                                title: '下单人员',
                                field: 'entrustAccountType',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    if(value == 1 || value == true || value == '1')
                                    {
                                        return '系统';
                                    }else{
                                        return '用户';
                                    }
                                }
                            }

                           ]],       
            columns: [[
                {
                    width: '120',
                    title: '委托来源',
                    field: 'entrustSource',
                    sortable: true
                }, {
                    width: '140',
                    title: '委托时间',
                    field: 'entrustTime',
                    formatter: function (value, row, index) {
                        if(value!='' && value!=null  && value!=0)
                        {
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");

                        }else
                        {
                            return '';
                        }
                    }
                },{
                    width: '100',
                    title: '委托状态',
                    field: 'status',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                },{
                    width: '150',
                    title: '备注',
                    field: 'entrustRemark',
                    sortable: true
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
        <table id="entrustXDoingDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
