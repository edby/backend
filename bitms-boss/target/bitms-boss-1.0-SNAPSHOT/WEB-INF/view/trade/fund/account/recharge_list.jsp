<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountFundCurrentRechargeDataGrid;
    $(function () {
        accountFundCurrentRechargeDataGrid = $('#accountFundCurrentRechargeDataGrid').datagrid({
            url: '${ctx}/fund/account/data',
            fit: true,
            queryParams:{accountAssetType:account_fund_current_accountAssetType,businessFlag:account_fund_current_businessFlag_recharge},
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
			{ field:'id',checkbox:true },
			{
			    width: '150',
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
			    sortable: true
			} 		
                            ]],
            columns: [[{
                width: '120',
                title: '充币时间',
                field: 'currentDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '70',
                title: '业务类别',
                field: 'businessFlag',
                hidden:true
            },{
                width: '150',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            },{
                width: '80',
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
                width: '120',
                title: '最新资产数量余额',
                field: 'lastAmt',
                sortable: true,
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '100',
                title: '充币目标地址',
                field: 'chargeAddr',
                sortable: true
            }, {
                width: '100',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '70',
                title: '归集状态',
                field: 'collectStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
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
    function searchAccountFundCurrentRechargeFun() {
    	accountFundCurrentRechargeDataGrid.datagrid('load', 
    			{accountAssetType:account_fund_current_accountAssetType,
    		businessFlag:account_fund_current_businessFlag_recharge,
    		timeStart:$("input[name='recharge_timeStart']").val(),timeEnd:$("input[name='recharge_timeEnd']").val(),
    		stockCode:$('#recharge_stockCode').combobox('getValue')});
    }
    function cleanAccountFundCurrentRechargeFun() {
        $('#searchAccountFundCurrentRechargeForm input').val('');
        accountFundCurrentRechargeDataGrid.datagrid('load', 
        		{accountAssetType:account_fund_current_accountAssetType,
        	businessFlag:account_fund_current_businessFlag_recharge});
    }
   function rechargeAccountFundCurrentRechargeFun(){
	   var checkedItems = $('#accountFundCurrentRechargeDataGrid').datagrid('getChecked');
       var ids = [];
       var isOk=true;
       $.each(checkedItems, function(index, item){
       	if(item.collectStatus != 'unCollect'){
       		isOk=false;
       		parent.$.messager.alert('错误', '第'+(index+1)+'行记录不是未归集款项，请选择未归集的款项进行归集!', 'error');
       	}else{
       		ids.push(item.id);
       	}
       });
       if(!isOk){
      	 ids = [];
      	 return false;
    	}
       if(ids.length == 0){
       	parent.$.messager.alert('错误', '请选择要归集的记录!', 'error');
			return false;
       }
       parent.$.messager.confirm('询问', '您确定要归集所选中的充值记录吗？', function (b) {
           if (b) {
               progressLoad();
		       var url='${ctx}/fund/account/recharge';
		       $.post(url, {
		           ids: ids.join(","),status:true
		       }, function (result) {
		           if (result.code == ajax_result_success_code) {
		               parent.$.messager.alert('提示', result.message, 'info');
		               accountFundCurrentRechargeDataGrid.datagrid('load', 
		            		   {accountAssetType:account_fund_current_accountAssetType,
		            	   businessFlag:account_fund_current_businessFlag_recharge});
		           }else{
                   	parent.$.messager.alert('提示', result.message, 'error');
                   }
                   $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                   setCsrfToken("csrf-form");
		           progressClose();
		       }, 'JSON');
           }
       });
		
   }
   function rechargeCancelAccountFundCurrentRechargeFun(){
	   var checkedItems = $('#accountFundCurrentRechargeDataGrid').datagrid('getChecked');
       var ids = [];
       var isOk=true;
       $.each(checkedItems, function(index, item){
       	if(item.collectStatus != 'collected'){
       		isOk=false;
       		parent.$.messager.alert('错误', '第'+(index+1)+'行记录是未归集款项，请选择已归集的款项进行归集!', 'error');
       	}else{
       		ids.push(item.id);
       	}
       });
       if(!isOk){
      	 ids = [];
      	 return false;
    	}
       if(ids.length == 0){
       	parent.$.messager.alert('错误', '请选择要取消归集的记录!', 'error');
			return false;
       }
       parent.$.messager.confirm('询问', '您确定要取消归集所选中的充值记录吗？', function (b) {
           if (b) {
               progressLoad();
               var url = '${ctx}/fund/account/recharge';
               $.post(url, {
                   ids: ids.join(","), status: false
               }, function (result) {
                   if (result.code == ajax_result_success_code) {
                       parent.$.messager.alert('提示', result.message, 'info');
                       accountFundCurrentRechargeDataGrid.datagrid('load',
                           {
                               accountAssetType: account_fund_current_accountAssetType,
                               businessFlag: account_fund_current_businessFlag_recharge
                           });
                   }else{
                   	parent.$.messager.alert('提示', result.message, 'error');
                   }
                   $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                   setCsrfToken("csrf-form");
                   progressClose();
               }, 'JSON');
           }
       });
	   
   }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountFundCurrentRechargeForm">
            <table>
                <tr>
                	<th>证券代码:</th>
                	<td>
	                    <input  id="recharge_stockCode" name="stockCode"  class="easyui-combobox" name="language" style="width: 142px;"
	                        placeholder="请选择证券"  value="${stockRate.stockinfoId}" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'stockCode',
									textField:'stockName', groupField:'group'"  >
					</td>				
                    <th>充币开始时间:</th>
                    <td><input name="recharge_timeStart" class="easyui-datetimebox"  placeholder="请输入交易开始时间"/></td>
                    <th>充币结束时间:</th>
                    <td><input name="recharge_timeEnd" class="easyui-datetimebox"   placeholder="请输入交易结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentRechargeFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentRechargeFun();">清空</a>
                         <shiro:hasPermission name="trade:setting:accountfundcurrent:rechargeoperator">
                         <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-book',plain:true" onclick="rechargeAccountFundCurrentRechargeFun();">充币归集</a>
                          <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-book',plain:true" onclick="rechargeCancelAccountFundCurrentRechargeFun();">取消充币归集</a>
                    	</shiro:hasPermission>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountFundCurrentRechargeDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<jsp:include page="/commons/setup_ajax.jsp"></jsp:include>	