<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var withdrawRecordDataGrid;
    $(function () {
        withdrawRecordDataGrid = $('#withdrawRecordDataGrid').datagrid({
            url: '${ctx}/bitpay/withdrawRecord/data',
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
				    width: '300',
				    title: '提现地址',
				    field: 'withdrawAddr',
				    sortable: true
				}		
               ]],
            columns: [[ {
                width: '150',
                title: '提现金额',
                field: 'occurAmt',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '150',
                title: '手续费',
                field: 'netFee',
                formatter: function (value, row, index) {
                    return parseFloat(value).toFixed(12);
                }
            }, {
                width: '460',
                title: '交易ID',
                field: 'transId'
            }, {
                width: '100',
                title: '备注',
                field: 'remark'
            }, {
                width: '100',
                title: '提现状态',
                field: 'state',
                formatter: function (value, row, index) {
                	if(value=='0'){
                		return '未提现';
                	}else if(value=='1'){
                		return '已提现';
                	}else if(value=='2'){
                		return '已导出';
                	}else{
                		return '';
                	}
                }
            }, {
                width: '150',
                title: '提现时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:withdrawRecord:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        if(row.state==0){
                        	str += $.formatString('<a href="javascript:void(0)" class="tixian-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="doWithdraw(\'{0}\');" >提现</a>', row.id);
                        }
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
                <shiro:hasPermission name="trade:setting:withdrawRecord:operator">
                $('.tixian-easyui-linkbutton-edit').linkbutton({text: '提现'});
                </shiro:hasPermission>
                $("#withdrawRecordDataGrid").datagrid('unselectAll');
            },
            toolbar: '#withdrawRecordToolbar' 
        });
    });
	
    function doWithdraw(id){
        if (id == undefined) {
            var rows = withdrawRecordDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            withdrawRecordDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '提现确认',
            width: 380,
            height: 120,
            href: '${ctx}/bitpay/withdrawRecord/password?ids=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = withdrawRecordDataGrid;
                    var f = parent.$.modalDialog.handler.find('#passwordForm');
                    f.submit();
                }
            }]
        });
    }
	function doPiliangWithdraw(){
		 var checkedItems = $('#withdrawRecordDataGrid').datagrid('getChecked');
         var ids = [];
         var isOk=true;
         $.each(checkedItems, function(index, item){
         	if(item.state != '0'){
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
            width: 380,
            height: 120,
            href: '${ctx}/bitpay/withdrawRecord/password?ids=' + ids.join(","),
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = withdrawRecordDataGrid;
                    var f = parent.$.modalDialog.handler.find('#passwordForm');
                    f.submit();
                }
            }]
        });
    }
  
	function addFromInterfaceFun(){
		parent.$.messager.confirm('询问', '您确定要导入数据吗？', function (b) {
            if (b) {
			var url='${ctx}/bitpay/withdrawRecord/apiimport';
			 $.post(url, {
	         	
	         }, function (result) {
	         	 progressClose();
	             if (result.code == ajax_result_success_code) {
	                 parent.$.messager.alert('提示', result.message, 'info');
	                 withdrawRecordDataGrid.datagrid('load', $.serializeObject($('#searchWithdrawRecordForm')));
	             }else{
	             	parent.$.messager.alert('提示', result.message, 'info');
	             }
	         $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
	         setCsrfToken("csrf-form");
	         }, 'JSON');
        }});
	}
   
  
    
    function searchWithdrawRecordFun() {
    	withdrawRecordDataGrid.datagrid('load', $.serializeObject($('#searchWithdrawRecordForm')));
    }
    function cleanWithdrawRecordFun() {
        $('#searchWithdrawRecordForm input').val('');
        withdrawRecordDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchWithdrawRecordForm">
            <table>
                <tr>
                    <th>提现地址:</th>
                    <td><input name="withdrawAddr" class="easyui-textbox" placeholder="请输入提现地址"/></td>
                    <th>提现状态:</th>
                    <td><select id="state" class="easyui-combobox" name="state" style="width:150px;">
                    	<option value="">-请选择-</option>    
					    <option value="0">未提现</option>   
					    <option value="1">已提现</option>
					</select>  
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchWithdrawRecordFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanWithdrawRecordFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="withdrawRecordDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="withdrawRecordToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:withdrawRecord:operator">
        <a onclick="addFromInterfaceFun();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-archive icon-blue'">接口导入</a>
        <a onclick="doPiliangWithdraw();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-pencil icon-blue'">批量提现</a>
    </shiro:hasPermission>
</div>
