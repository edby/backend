<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var BlocktransconfirmDataGrid;
    $(function () {
        BlocktransconfirmDataGrid = $('#BlocktransconfirmDataGrid').datagrid({
            url: '${ctx}/fund/blocktrans/chargeData',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
			{
			    width: '100',
			    title: '主键',
			    field: 'id',
			    hidden: true
			}, {
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
			    width: '100',
			    title: '证券代码',
			    field: 'stockCode',
			    sortable: true
			}, {
			    width: '100',
			    title: '证券名称',
			    field: 'stockName',
			    sortable: true,
			    hidden:true
			}, {
			    width: '70',
			    title: '证券类型',
			    field: 'stockType',
			    hidden:true,
			    formatter: function (value, row, index) {
			    	return getDictValueByCode(value);
			    }
			},{
			    width: '150',
			    title: '钱包ID',
			    field: 'walletId',
			    sortable: true
			},{
			    width: '150',
			    title: '钱包名称',
			    field: 'walletName',
			    sortable: true,
			    hidden:true
			    
			},{
			    width: '250',
			    title: '钱包地址',
			    field: 'walletAddr',
			    sortable: true
			}	
                            ]],
            columns: [[ {
                width: '100',
                title: '资产方向',
                field: 'direct',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '250',
                title: '交易ID',
                field: 'transId'
            }, {
                width: '150',
                title: '发生额',
                field: 'amount',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '150',
                title: '手续费',
                field: 'fee',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '100',
                title: '区块确认状态',
                field: 'status',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            }, {
                width: '100',
                title: '区块确认方',
                field: 'confirmSide',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '100',
                title: '区块确认状态',
                field: 'status',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            },{
                width: '120',
                title: '创建时间',
                field: 'createDate',
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

    function searchBlockTransConfirmFun() {
    	BlocktransconfirmDataGrid.datagrid('load', $.serializeObject($('#searchBlockTransConfirmForm')));
    }
    function cleanBlockTransConfirmFun() {
        $('#searchBlockTransConfirmForm input').val('');
        BlocktransconfirmDataGrid.datagrid('load', {});
    }
    $(function(){
    	$("#confirmSideChargeTd").html(dictDropDownOptionsList('confirmSideCharge','confirmSide', 'confirmSide','', '',  'width:142px,'));
    	$("#confirmSideCharge").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	
    	$("#statusChargeTd").html(dictDropDownOptionsList('statusCharge','status', 'confirmStatus','', '',  'width:142px,'));
    	$("#statusCharge").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});

    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBlockTransConfirmForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                         <input  id="allot_stockCode" name="stockCode"  class="easyui-combobox" name="language" style="width: 142px;"  
	                        placeholder="请选择证券"  value="" data-options="
									url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_DIGITALCOIN%>', method: 'get', valueField:'stockCode',
									textField:'stockCode', groupField:'group'"  >
					</td>
					<th>区块确认方:</th>
                    <td id="confirmSideChargeTd">
					</td>
                    <th>区块确认状态:</th>
                    <td id="statusChargeTd">
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchBlockTransConfirmFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBlockTransConfirmFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="BlocktransconfirmDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
