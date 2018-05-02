<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var realDealDataGrid;
    $(function () {
    	
        realDealDataGrid = $('#realDealDataGrid').datagrid({
            url: '${ctx}/spot/realDeal/data',
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
	                    title: '交易类型',
	                    field: 'tradeType',
	                    formatter: function (value, row, index) {
	                    	return getDictValueByCode(value);
                    	}
	                },{
	                    width: '120',
	                    title: '成交方向',
	                    field: 'dealDirect',
	                    formatter: function (value, row, index) {
	                    	return getDictValueByCode(value);
	                    }
	                },{
					    width: '150',
					    title: '账户名',
					    field: 'accountName',
					    sortable: true
					}, {
					    width: '150',
					    title: '交易对手',
					    field: 'rivalName',
					    sortable: true,
					    hidden:true
					}		
                            ]],
            columns: [[ {
                width: '120',
                title: '成交时间',
                field: 'dealTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '100',
                title: '成交数量',
                field: 'dealAmt',
                sortable: true
            },{
                width: '80',
                title: '成交价格',
                field: 'dealPrice',
                sortable: true
            },{
                width: '300',
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
    	realDealDataGrid.datagrid('load', 
    			 $.serializeObject($('#searchAccountFundCurrentAllForm')));
    }
    function cleanAccountFundCurrentAllFun() {
        $('#searchAccountFundCurrentAllForm input').val('');
        realDealDataGrid.datagrid('load',{});
    }
    $(function(){
    	$("#tradeTypeListTd").html(dictDropDownOptionsList('tradeTypeList','tradeType', 'tradeType','', '',  'width:142px,'));
    	$("#tradeTypeList").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#dealDirectTd").html(dictDropDownOptionsList('dealDirectId','dealDirect', 'entrustDealDirect', '', '', ''));
    	$("#dealDirectId").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountFundCurrentAllForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="check_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <th>交易类型:</th>
                    <td id="tradeTypeListTd">	
					</td>
                    <th>成交方向:</th>
                    <td id="dealDirectTd">	
					</td>
					<th>成交开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入成交开始时间"/></td>
                    <th>成交结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入成交结束时间"/></td>
                    <td>
                    <td colspan="2">
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
        <table id="realDealDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
