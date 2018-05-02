<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var accountFundCurrentAllDataGrid;
    $(function () {
        accountFundCurrentAllDataGrid = $('#accountFundCurrentAllDataGrid').datagrid({
            url: '${ctx}/fund/account/data',
            queryParams:{relatedStockinfoId:<%=FundConsts.WALLET_BTC2USD_TYPE%>,tableName:'table'},
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
			    width: '100',
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
                width: '90',
                title: '账户资产类型',
                field: 'accountAssetType',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '130',
                title: '账户资产ID',
                field: 'accountAssetId'
            },
                {
                    width: '140',
                    title: '交易时间',
                    field: 'currentDate',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }
                ]],
            columns: [[ {
                width: '120',
                title: '业务类别',
                field: 'businessFlag',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '合约数量',
                field: 'contractAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '80',
                title: '资产发生方向',
                field: 'occurDirect',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '资产发生数量',
                field: 'occurAmt',
                sortable: true,
                formatter: function (value, row, index) {
                	return (value-row.netFee).toFixed(12);
                }
            },{
                width: '150',
                title: '网络手续费',
                field: 'netFee',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '最新资产数量余额',
                field: 'lastAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '原冻结数量余额',
                field: 'forzenOrgAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '冻结解冻发生数量',
                field: 'occurForzenAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '150',
                title: '最新冻结余额',
                field: 'forzenLastAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '280',
                title: '充币目标地址',
                field: 'chargeAddr',
                sortable: true
            }, {
                width: '280',
                title: '提币目标地址',
                field: 'withdrawAddr',
                sortable: true
            }, {
                width: '70',
                title: '审批状态',
                field: 'approveStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '归集状态',
                field: 'collectStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '划拨状态',
                field: 'transferStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
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

    function searchAccountFundCurrentAllFun() {
    	accountFundCurrentAllDataGrid.datagrid('load', 
    			 $.serializeObject($('#searchAccountFundCurrentAllForm')));
    }
    function cleanAccountFundCurrentAllFun() {
        $('#searchAccountFundCurrentAllForm input').val('');
        accountFundCurrentAllDataGrid.datagrid('load',{});
    }
    $(function(){
    	$("#accountAssetTypeTd").html(dictDropDownOptionsList('accountAssetTypeCurr','accountAssetType', 'accountAssetType','', '',  'width:102px,'));
    	$("#accountAssetTypeCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#approveStatusTd").html(dictDropDownOptionsList('approveStatusCurr','approveStatus', 'approveStatus', '', '', 'width:102px;'));
    	$("#approveStatusCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#transferStatusTd").html(dictDropDownOptionsList('transferStatusCurr','transferStatus', 'transferStatus', '', '', 'width:102px;'));
    	$("#transferStatusCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#collectStatusTd").html(dictDropDownOptionsList('collectStatusCurr','collectStatus', 'collectStatus', '', '', 'width:102px;'));
    	$("#collectStatusCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#occurDirectTd").html(dictDropDownOptionsList('occurDirectCurr','occurDirect', 'assetDirect', '', '', 'width:102px;'));
    	$("#occurDirectCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
        $('#curr_table_id').switchbutton({
            checked: false,
            onChange: function(checked){
                if(checked)
                {
                    $("#curr_table_name").val("tableHis");
                }else
                {
                    $("#curr_table_name").val("table");
                }
                accountFundCurrentAllDataGrid.datagrid('load',
                    $.serializeObject($('#searchAccountFundCurrentAllForm')));
            }
        });
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 60px; overflow: hidden;background-color: #fff">
        <form id="searchAccountFundCurrentAllForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="check_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <!--
                    <th>证券代码:</th>
                    <td>
                    	<input  id="check_stockCode2" name="stockCode"  class="easyui-combobox" name="language" style="width: 102px;"
	                        placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'stockCode',
									textField:'stockName', groupField:'group'"  >
                    </td>
                    -->
                    <th>交易对:</th>
                    <td>
                        <input name="relatedStockinfoId" id="curr_re_id" class="easyui-combobox" name="language" style="width: 102px;"
                               placeholder="请关联证券代码" value="<%=FundConsts.WALLET_BTC2USD_TYPE%>" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  >
                    </td>
                    <th>账户资产类型:</th>
                	<td id="accountAssetTypeTd">
					</td>
                    <th>审批状态:</th>
                    <td id="approveStatusTd">	
					</td>
					<th>划拨状态:</th>
                	<td id="transferStatusTd">
					</td>
                </tr>
                <tr>
					<th>归集状态:</th>
                	<td id="collectStatusTd">
					</td>
					<th>资产发生方向:</th>
                	<td id="occurDirectTd">
					</td>
					<th>交易开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入交易开始时间"/></td>
                    <th>交易结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入交易结束时间"/></td>
                    <td>
                    <td align="left">
                        <input id="curr_table_name" name="tableName" value="table" type="hidden" />
                        <input id="curr_table_id" style="width: 100px;" class="easyui-switchbutton" data-options="onText:'历史数据',offText:'当前数据'" >
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
        <table id="accountFundCurrentAllDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
