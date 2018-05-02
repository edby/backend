<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %> 
<script type="text/javascript">
     var monitorBalDataGrid;
     
     $(function () {
    	 monitorBalDataGrid = $('#monitorBalDataGrid').datagrid({
    		 url: '${ctx}/monitor/monitorbal/data',
             queryParams:{monitorSubType:'${monitorSubType}'},
             fit: true,
             striped: true,
             rownumbers: true,
             pagination: true,
             singleSelect: true,
             idField: 'chkDate',
             sortName: 'chkDate',
             sortOrder: 'desc',
             pageSize: 20,
             pageList: [10, 20, 30, 40, 50, 100],
             frozenColumns:[[
            	 {
                     width: '130',
                     title: '监控时间',
                     field: 'chkDate', 
                     formatter: function (value, row, index) {
                         return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                 	}
                 },
                 {
                     width: '60',
                     title: 'UNID',
                     field: 'unid' 
                 },
                 {
                     width: '150',
                     title: '账户名称',
                     field: 'accountName' 
                 },
                 {
                     width: '100',
                     title: '证券编号',
                     field: 'stockinfoId' 
                 },
                 {
                     width: '100',
                     title: '证券名称',
                     field: 'stockName'
                 }
             ]],
             columns: [[
            	 {
                     width: '150',
                     title: '账户期初余额',
                     field: 'beginBal' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },{
                     width: '150',
                     title: '账户期初冻结余额',
                     field: 'beginFrozenBal' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },
                 {   width: '150',
                     title: '当期发生额',
                     field: 'amount' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },
                 {
                     width: '150',
                     title: '当期发生冻结额',
                     field: 'frozenAmt' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },
                 {
                     width: '150',
                     title: '期末余额',
                     field: 'endBal' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },
                 {
                     width: '150',
                     title: '期末冻结余额',
                     field: 'endFrozenBal' ,
                     sortable: true,
                     formatter: function (value, row, index) {
                         return parseFloat(value).toFixed(12);
                     }
                 },
                 {
                     width: '100',
                     title: '监控结果',
                     field: 'chkResult' ,
                     formatter: function (value, row, index) {
                         if(value == 1){
                             return '正常';
                         }else {
                             return '异常';
                         }
                      },
                     styler: function(value,row,index){
                         if(row.chkResult == -1){
                             return 'color:red;';
                         }
                     }
                 },
                 {
                     width: '500',
                     title: '描述',
                     field: 'monitorDesc'
                 }/*,

                 {
                     width: '100',
                     title: '账户状态',
                     field: 'status' ,
                     formatter: function (value, row, index) {
                         if(value == 0){
                             return '正常';
                         }else if(value == 1){
                             return '冻结';
                         }else if(value == 2){
                        	 return '注销';
                         }
                      },
                     styler: function(value,row,index){
                         if(row.status != 0){
                             return 'color:red;';
                         }
                     }
                 } */
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
     
     function searchMonitorBalFun() {
    	 monitorBalDataGrid.datagrid('load', 
     			 $.serializeObject($('#searchMonitorBalForm')));
     }
     function cleanMonitorBalFun() {
         $('#searchMonitorBalForm input').val('');
         monitorBalDataGrid.datagrid('load',{});
     } 
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
     <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
          <form id="searchMonitorBalForm">
               <table>
                     <tr>
                         <th>账户名:</th>
                         <td><input id="monitor_acctName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                         <th>监控结果:</th>
                         <td>
                            <select id="chkResult" class="easyui-combobox" name="chkResult" style="width:100px;"  data-options="required:true">
                                <option value="0">-请选择-</option>
	                            <option value="1">正常</option>
	                            <option value="-1">异常</option>
                            </select>
                        </td>
                         <th>开始时间:</th>
	                     <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入开始时间"/></td>
	                     <th>结束时间:</th>
	                     <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入结束时间"/></td>
	                     <td>
	                     <td colspan="2">
	                         <a href="javascript:void(0);" class="easyui-linkbutton"
	                              data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchMonitorBalFun();">查询</a>
	                         <a href="javascript:void(0);" class="easyui-linkbutton"
	                              data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorBalFun();">清空</a>
	                     </td>
                     </tr>
               </table>
          </form>
     </div>
     <div data-options="region:'center',border:true">
           <table id="monitorBalDataGrid" data-option="fit:true,border:false"></table>
     </div>
</div>
