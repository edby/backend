<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>

<script type="text/javascript">
    var monitorParamDataGrid;
    $(function () {
    	monitorParamDataGrid = $('#monitorParamDataGrid').datagrid({
    		url: '${ctx}/monitor/param/data',
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
            columns: [[
            	{
                    width: '90',
                    title: '参数类型',
                    field: 'paramType'
                },
                {
                    width: '120',
                    title: '参数名称',
                    field: 'paramName'
                },
                {
                    width: '90',
                    title: '参数代码',
                    field: 'paramCode',
                    sortable: true
                },
                {
                    width: '120',
                    title: '参数值',
                    field: 'paramValue'
                }<shiro:hasPermission name="monitor:setting:monitorparam:operator">,
                {
                    field: 'action',
                    title: '操作',
                    width: 140,
                    formatter: function (value, row) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorParamEdit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="monitorParamEditFun(\'{0}\');" >编辑</a>', row.id);
                        return str;
                    }
                }</shiro:hasPermission>,
                {
                    width: '500',
                    title: '参数备注',
                    field: 'paramDesc',
                }
            ]],
            onLoadSuccess: function (data) {
    		    //用户未登录时刷新页面
    		    var codeNum = JSON.stringify(data.code);
    		    if(codeNum==2003){
    		        window.location.reload();
                }
                <shiro:hasPermission name="monitor:setting:monitorparam:operator">
                $('.easyui-linkbutton-monitorParamEdit').linkbutton({text: '编辑'});
                $('.easyui-linkbutton-monitorParamUnActive').linkbutton({text: '停用'});
                $('.easyui-linkbutton-monitorParamActive').linkbutton({text: '启用'});
                </shiro:hasPermission>
            },
            toolbar: '#monitorParamToolBar'
    	});
    });
    
    
    function monitorParamAddFun()
    {
    	parent.$.modalDialog({
    		title:'添加',
    		width: 500,
            height: 400,
            href: '${ctx}/monitor/param/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorParamDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMonitorParamForm');
                    f.submit();
                }
            }]
    	});
    }
    
    function monitorParamEditFun(id) {
        if (id == undefined) {
            var rows = monitorParamDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
        	monitorParamDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 400,
            href: '${ctx}/monitor/param/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorParamDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMonitorParamForm');
                    f.submit();
                }
            }]
        });
    }

    function searchMonitorParamAllFun() {
    	monitorParamDataGrid.datagrid('load', 
    			 $.serializeObject($('#searchMonitorParamForm')));
    }
    
    function cleanMonitorParamFun() {
        $('#searchMonitorParamForm input').val('');
        monitorParamDataGrid.datagrid('load',{});
    }
    
    $(function(){
    	$("#paramTypeTd").html(dictDropDownOptionsList('paramTypeCurr','paramType', 'monitorParam','', '',  'width:142px,'));
    	$("#paramTypeCurr").combobox({
    	    valueField:'code',
    	    textField:'name'
    	}); 
    });
    
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
     <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
          <form id="searchMonitorParamForm">
               <table>
                    <tr>
                        <th>参数类型:</th>
                        <td>
                            <select id="paramType" class="easyui-combobox" name="paramType" style="width:100px;"  data-options="required:true">
                                <option value="">-请选择-</option>
                                <option value="Message">消息提醒</option>
                            </select>
                        </td>
	                    <td>
	                        <a href="javascript:void(0);" class="easyui-linkbutton"
	                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchMonitorParamAllFun();">查询</a>
	                        <a href="javascript:void(0);" class="easyui-linkbutton"
	                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorParamFun();">清空</a>
	                    </td>
                    </tr>
               </table>
          </form>
     </div>
     <div data-options="region:'center',border:true">
            <table id="monitorParamDataGrid" data-options="fit:true,border:false"></table>
     </div>
</div>

<div id="monitorParamToolBar" style="display: none;">  
     <shiro:hasPermission name="monitor:setting:monitorparam:operator">
           <a onclick="monitorParamAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
     </shiro:hasPermission>
</div>