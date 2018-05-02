<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountFundCurrentAllotDataGrid;
    var accountFundCurrentAllotDataGrid2;
    $.ajax({
	  	  url:'${ctx}/common/dict/get',
	  	  data:{"code":"transferStatus"},
	  	  type:"POST",
	  	  cache:false,
	  	  async:false,//false=同步调用，锁定其它JS操作
	  	  dataType:"json",
	  		error: function(XMLHttpRequest, textStatus, errorThrown) {
	  				alert('服务器连接失败，请稍候重试！');
	  		   },
			  success: function(data, textStatus){
				  transferStatus = data;
			  }
		});
    $.ajax({
	  	  url:'${ctx}/common/dict/get',
	  	  data:{"code":"confirmStatus"},
	  	  type:"POST",
	  	  cache:false,
	  	  async:false,//false=同步调用，锁定其它JS操作
	  	  dataType:"json",
	  		error: function(XMLHttpRequest, textStatus, errorThrown) {
	  				alert('服务器连接失败，请稍候重试！');
	  		   },
			  success: function(data, textStatus){
				  allotconfirmStatus = data;
			  }
		});
    $(function () {
    	$.ajax({
    	  	  url:'${ctx}/common/dict/get',
    	  	  data:{"code":"approveStatus"},
    	  	  type:"POST",
    	  	  cache:false,
    	  	  async:false,//false=同步调用，锁定其它JS操作
    	  	  dataType:"json",
    	  		error: function(XMLHttpRequest, textStatus, errorThrown) {
    	  				alert('服务器连接失败，请稍候重试！');
    	  		   },
    			  success: function(data, textStatus){
    				approveStatus = data;
    			  }
    		});
        accountFundCurrentAllotDataGrid2 = $('#accountFundCurrentAllotDataGrid2').datagrid({
            fit: true,
            queryParams:{accountId:'0'},
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
			    title: '源钱包ID',
			    field: 'srcWalletId',
			    sortable: true
			}, {
			    width: '250',
			    title: '源钱包地址',
			    field: 'srcWalletAddr',
			    sortable: true
			}, {
			    width: '250',
			    title: '目标钱包地址',
			    field: 'targetWalletAddr',
			    sortable: true
			}		
                            ]],
            columns: [[
             {
                width: '120',
                title: '划拨时间',
                field: 'transferTime',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '150',
                title: '划拨数量',
                field: 'transferAmt',
                sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
            },{
                width: '150',
                title: '划拨费用',
                field: 'transferFee',
                sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
            },{
                width: '460',
                title: '区块交易ID',
                field: 'transId',
                sortable: true
            },{
                width: '70',
                title: '划拨状态',
                field: 'transferStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '120',
                title: '区块确认状态',
                field: 'confirmStatus',
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
        accountFundCurrentAllotDataGrid = $('#accountFundCurrentAllotDataGrid').datagrid({
            url: '${ctx}/fund/account/data',
            fit: true,
            queryParams:{
                relatedStockinfoId:<%=FundConsts.WALLET_BTC_TYPE%>,
            	transferStatus:'<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER %>',
            	accountAssetType:account_fund_current_accountAssetType,
            	businessFlag:account_fund_current_businessFlag,
            	approveStatus:account_fund_current_approveStatus},
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
			    width: '120',
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
			}		
                            ]],
            columns: [[ {
                width: '120',
                title: '提币申请时间',
                field: 'currentDate',
                sortable: true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                width: '100',
                title: '原资产数量余额',
                field: 'orgAmt',
                sortable: true
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
            }, {
                width: '250',
                title: '提币目标地址',
                field: 'withdrawAddr',
                sortable: true
            }, {
                width: '100',
                title: '备注',
                field: 'remark',
                sortable: true
            },{
                width: '70',
                title: '审批状态',
                field: 'approveStatus',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '划拨状态',
                field: 'transferStatus',
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
            },{
                width: '460',
                title: '区块交易ID',
                field: 'transId',
                sortable: true
            }
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
            	 var rows=accountFundCurrentAllotDataGrid.datagrid("getRows");
            	 var opts = accountFundCurrentAllotDataGrid2.datagrid("options");
            	    opts.url = '${ctx}/fund/account/transferdata';
            	   if(rows.length>0){
            		   accountFundCurrentAllotDataGrid2.datagrid('reload',
     	            		  {originalCurrentId:rows[0].id});
     	              $('#accountFundCurrentAllotDataGrid').datagrid('selectRow', 0);
            	   }
            },
            onClickRow:function(rowIndex, rowData){
            	var opts = accountFundCurrentAllotDataGrid2.datagrid("options");
        	    opts.url = '${ctx}/fund/account/transferdata';
                accountFundCurrentAllotDataGrid2.datagrid('reload',
                	{originalCurrentId:rowData.id});
                $('#accountFundCurrentAllotDataGrid').datagrid('clearSelections');
                $('#accountFundCurrentAllotDataGrid').datagrid('selectRow', rowIndex);
            },
            toolbar: '#allotbar'
        });
    });
    function searchAccountFundCurrentAllotFun() {
    	accountFundCurrentAllotDataGrid.datagrid('load', 
    			{
                    relatedStockinfoId:$('#allot_relatedStockinfoId').combobox('getValue'),
		    		accountAssetType:account_fund_current_accountAssetType,
		    		businessFlag:account_fund_current_businessFlag,
		    		approveStatus:account_fund_current_approveStatus,
		    		timeStart:$("input[name='allot_timeStart']").val(),timeEnd:$("input[name='allot_timeEnd']").val(),
		    		stockCode:$('#allot_stockCode').combobox('getValue'),transferStatus:$('#transferStatusAllot').combobox('getValue')});
    }
    function cleanAccountFundCurrentAllotFun() {
        $('#searchAccountFundCurrentAllotForm input').val('');
        $("#transferStatusAllot").combobox('setValue','<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER %>');
        accountFundCurrentAllotDataGrid.datagrid('load', 
        		{
                    relatedStockinfoId:<%=FundConsts.WALLET_BTC_TYPE%>,
        			accountAssetType:account_fund_current_accountAssetType,
        			businessFlag:account_fund_current_businessFlag,
        			approveStatus:account_fund_current_approveStatus,transferStatus:$('#transferStatusAllot').combobox('getValue')});
    }
   function accountFundCurrentAllotFun(){
		 var checkedItems = $('#accountFundCurrentAllotDataGrid').datagrid('getChecked');
         var ids = [];
         var isOk=true;
         $.each(checkedItems, function(index, item){
         	if(item.transferStatus != 'unTransfer'){
         		isOk=false;
         		parent.$.messager.alert('错误', '第'+(index+1)+'行记录不是未划拨款项，请选择未划拨的款项进行划拨!', 'error');
         	}else{
         		ids.push(item.id);
         	}
         });
         if(!isOk){
        	 ids = [];
        	 return false;
      	}
         if(ids.length == 0){
         	parent.$.messager.alert('错误', '请选择要划拨的记录!', 'error');
 			return false;
         }
         parent.$.messager.confirm('询问', '您确定要划拨所选中的充值记录吗？', function (b) {
             if (b) {
		         var url='${ctx}/fund/account/allot';
		         $.post(url, {
		             ids: ids.join(",")
		         }, function (result) {
		             if (result.code == ajax_result_success_code) {
		                 parent.$.messager.alert('提示', result.message, 'info');
		                 cleanAccountFundCurrentAllotFun();
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
   $(function(){
   	$("#transferStatusAllotTd").html(dictDropDownOptionsList('transferStatusAllot','transferStatus', 'transferStatus','<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER %>', '',  'width:142px,'));
   	$("#transferStatusAllot").combobox({
   	    valueField:'code',
   	    textField:'name'
   	});
   });
</script>


<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'north',split:true" style="width: 580px;height:360px;">
        <table id="accountFundCurrentAllotDataGrid" data-options="fit:true,border:false,title:'账户资金流水列表'"></table>
    </div>
    <div data-options="region:'center'"  style="padding: 5px; background: #eee;">
        <table id="accountFundCurrentAllotDataGrid2" data-options="fit:true,border:false,title:'账户资金划拨列表'"></table>
    </div>
</div>
<div id="allotbar" style="display: none;">
    <form id="searchAccountFundCurrentAllotForm">
        	<input id="ids" type="hidden" value=""/>
            <table>
                <tr>
                	<th>证券代码:</th>
                	<td>
	                    <input  id="allot_stockCode" name="stockCode"  class="easyui-combobox" name="language" style="width: 142px;"  
	                        placeholder="请选择证券"  value="" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'stockCode',
									textField:'stockCode', groupField:'group'"  >
					</td>
                    <th>关联证券代码:</th>
                    <td>
                        <input id="allot_relatedStockinfoId" name="relatedStockinfoId" class="easyui-combobox" name="language" style="width: 102px;"
                               placeholder="请关联证券代码" value="<%=FundConsts.WALLET_BTC_TYPE%>" data-options="
									url: '${ctx}/stock/info/all', method: 'get', valueField:'id',
									textField:'stockName', groupField:'group'"  >
                    </td>
					<th>划拨状态:</th>
                	<td id="transferStatusAllotTd">
					</td>					
                    <th>交易开始时间:</th>
                    <td><input name="allot_timeStart" class="easyui-datetimebox" placeholder="请输入交易开始时间"/></td>
                    <th>交易结束时间:</th>
                    <td><input name="allot_timeEnd" class="easyui-datetimebox"  placeholder="请输入交易结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentAllotFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentAllotFun();">清空</a>
                        <shiro:hasPermission name="trade:setting:accountfundcurrent:allotoperator">
                           <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-book',plain:true" onclick="accountFundCurrentAllotFun();">提币划拨</a>
                    	</shiro:hasPermission>
                    </td>
                </tr>
            </table>
        </form>
</div>	
