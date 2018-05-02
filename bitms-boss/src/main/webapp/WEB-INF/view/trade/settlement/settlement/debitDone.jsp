<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var debitDoneDataGrid;
    $(function () {
        debitDoneDataGrid = $('#debitDoneDataGrid').datagrid({
            url: '${ctx}/settlement/settlement/debitDone/data',
            queryParams:{
                accountAssetType:'<%=FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET %>',
                timeStart:'<fmt:formatDate value="${timeStart}" pattern="yyyy-MM-dd HH:mm:ss"/>',
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
			    width: '100',
			    title: '账户名',
			    field: 'accountName',
			    sortable: true
			}, {
			    width: '130',
			    title: '证券ID',
			    field: 'stockinfoId',
			    sortable: true
			}, {
			    width: '80',
			    title: '证券代码',
			    field: 'stockCode',
			    sortable: true
			}, {
			    width: '100',
			    title: '证券名称',
			    field: 'stockName',
			    sortable: true,
			    hidden:true
			},{
                width: '180',
                title: '账户资产类型',
                field: 'accountAssetType',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '180',
                title: '账户资产ID',
                field: 'accountAssetId'
            }			
                            ]],
            columns: [[ {
                width: '120',
                title: '交易时间',
                field: 'currentDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '180',
                title: '业务类别',
                field: 'businessFlag',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '100',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true
            },{
                width: '80',
                title: '合约数量',
                field: 'contractAmt',
                sortable: true
            },{
                width: '80',
                title: '资产发生方向',
                field: 'occurDirect',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '80',
                title: '资产发生数量',
                field: 'occurAmt',
                sortable: true,
                formatter: function (value, row, index) {
                	return (value-row.netFee);
                }
            },{
                width: '100',
                title: '网络手续费',
                field: 'netFee',
                sortable: true
            },{
                width: '100',
                title: '最新资产数量余额',
                field: 'lastAmt',
                sortable: true
            },{
                width: '100',
                title: '原冻结数量余额',
                field: 'forzenOrgAmt',
                sortable: true
            },{
                width: '100',
                title: '冻结解冻发生数量',
                field: 'occurForzenAmt',
                sortable: true
            },{
                width: '100',
                title: '最新冻结余额',
                field: 'forzenLastAmt',
                sortable: true
            }, {
                width: '280',
                title: '充币目标地址',
                field: 'chargeAddr',
                sortable: true
            }, {
                width: '600',
                title: '备注',
                field: 'remark'
            }, {
                width: '90',
                title: '创建时间',
                field: 'createDate',
                hidden:true,
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
        <table id="debitDoneDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
