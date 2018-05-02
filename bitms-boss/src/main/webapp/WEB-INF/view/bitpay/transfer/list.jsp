<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var withdrawTransferDataGrid;
    $(function () {
        withdrawTransferDataGrid = $('#withdrawTransferDataGrid').datagrid({
            url: '${ctx}/bitpay/transfer/data',
            queryParams:{
                transferStatus:'<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING %>'},
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
				    width: '150',
				    title: '提现记录ID',
				    field: 'rid',
	                formatter: function (value, row, index) {
	                	return row.id;
	                }
				}, {
				    width: '150',
				    title: '账户ID',
				    field: 'accountId',
				    sortable: true
				}, {
                    width: '150',
                    title: '账户',
                    field: 'accountName',
                    sortable: true
                }, {
				    width: '300',
				    title: '提现地址',
				    field: 'targetWalletAddr',
				    sortable: true
				}		
               ]],
            columns: [[ {
                width: '150',
                title: '提现金额',
                field: 'transferAmt',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '150',
                title: '手续费',
                field: 'transferFee',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '460',
                title: '交易ID',
                field: 'transId'
            }, {
                width: '250',
                title: '审批ID',
                field: 'pendingApproval'
            }, {
                width: '100',
                title: '备注',
                field: 'remark'
            }, {
                width: '100',
                title: '签汇状态',
                field: 'transferStatus',
                formatter: function (value, row, index) {
                    var color = "";
                    if(value == 'transfer')
                    {
                        color = 'green';
                    }
                    else if(value == 'transferPending')
                    {
                        color = 'orange';
                    }
                    else if(value == 'transferRejected')
                    {
                        color = 'red';
                    }
                    return "<font color='"+color+"'>"+getDictValueByCode(value)+"</font>";
                }
            }, {
                width: '100',
                title: '终签确认状态',
                field: 'confirmStatus',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            }, {
                width: '150',
                title: '提现时间',
                field: 'transferTime',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:accountfundtransfer:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                      <%-- if(row.transferStatus=='<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING %>'){
                        	str += $.formatString('<a href="javascript:void(0)" class="tixian-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="doTransfer(\'{0}\');" >提现</a>', row.id);
                        } --%>
                        return str;
                    }
                }
                </shiro:hasPermission>
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="trade:setting:accountfundtransfer:operator">
                $('.tixian-easyui-linkbutton-edit').linkbutton({text: '提现'});
                </shiro:hasPermission>
                $("#withdrawTransferDataGrid").datagrid('unselectAll');
            },
            toolbar: '#WithdrawTransferToolbar' 
        });
    });

<%-- function doTransfer(id){
     if (id == undefined) {
         var rows = withdrawTransferDataGrid.datagrid('getSelections');
         id = rows[0].id;
     } else {
         withdrawTransferDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
     }
     parent.$.modalDialog({
         title: '提现确认',
         width: 880,
         height: 350,
         href: '${ctx}/bitpay/transfer/password?ids=' + id,
         buttons: [{
             text: '确定',
             handler: function () {
                 parent.$.modalDialog.openner_dataGrid = withdrawTransferDataGrid;
                 var f = parent.$.modalDialog.handler.find('#passwordTransferForm');
                 f.submit();
             }
         }]
     });
 }
 function doPiliangTransfer(){
      var checkedItems = $('#withdrawTransferDataGrid').datagrid('getChecked');
      var ids = [];
      var isOk=true;
      $.each(checkedItems, function(index, item){
          if(item.transferStatus!='<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING %>'){
              isOk=false;
              parent.$.messager.alert('错误', '第'+(index+1)+'行记录不是未提现款项，请选择未提现款项进行提现!', 'error');
          }else{
              ids.push(item.id);
          }
      });
      if(!isOk){
          ids = [];
          return false;
       }
      if(ids.length == 0){
          parent.$.messager.alert('错误', '请选择要提现的记录!', 'error');
          return false;
      }
     parent.$.modalDialog({
         title: '提现确认',
         width: 880,
         height: 350,
         href: '${ctx}/bitpay/transfer/password?ids=' + ids.join(","),
         buttons: [{
             text: '确定',
             handler: function () {
                 parent.$.modalDialog.openner_dataGrid = withdrawTransferDataGrid;
                 var f = parent.$.modalDialog.handler.find('#passwordTransferForm');
                 f.submit();
             }
         }]
     });
 }
 --%>
 function searchWithdrawTransferFun() {
     withdrawTransferDataGrid.datagrid('load', $.serializeObject($('#searchWithdrawTransferForm')));
 }
 function cleanWithdrawTransferFun() {
     $('#searchWithdrawTransferForm input').val('');
     withdrawTransferDataGrid.datagrid('load', {});
 }
 $(function(){
     $("#transferStatusTd").html(dictDropDownOptionsList('transferStatus','transferStatus', 'transferStatus','<%=FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING %>', '',  'width:142px,'));
     $("#transferStatus").combobox({
         valueField:'code',
         textField:'name'
     });
 });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
 <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
     <form id="searchWithdrawTransferForm">
         <table>
             <tr>
                 <th>提现地址:</th>
                 <td><input name="targetWalletAddr" class="easyui-textbox" placeholder="请输入提现地址"/></td>
                 <th>提现状态:</th>
                 <td id="transferStatusTd">
                 </td>
                 </td>
                 <td>
                     <a href="javascript:void(0);" class="easyui-linkbutton"
                        data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchWithdrawTransferFun();">查询</a>
                     <a href="javascript:void(0);" class="easyui-linkbutton"
                        data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanWithdrawTransferFun();">清空</a>
                 </td>
             </tr>
         </table>
     </form>
 </div>
 <div data-options="region:'center',border:true">
     <table id="withdrawTransferDataGrid" data-options="fit:true,border:false"></table>
 </div>
</div>
<div id="WithdrawTransferToolbar" style="display: none;">
 <shiro:hasPermission name="trade:setting:accountfundtransfer:operator">
    <%-- <a onclick="doPiliangTransfer();" href="javascript:void(0);" class="easyui-linkbutton"
        data-options="plain:true,iconCls:'fi-pencil icon-blue'">批量提现</a>--%>
 </shiro:hasPermission>
</div>
