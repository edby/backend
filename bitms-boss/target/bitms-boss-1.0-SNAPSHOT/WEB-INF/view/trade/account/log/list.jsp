<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountLogDataGrid;
    $(function () {
        accountLogDataGrid = $('#accountLogDataGrid').datagrid({
            url: '${ctx}/account/accountLog/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'accountName',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '140',
                    title: '系统标识',
                    field: 'systemName',
                    sortable: true
                },{
				    width: '140',
				    title: '账户名',
				    field: 'accountName',
				    sortable: true
				},{
				    width: '140',
				    title: '账户ID',
				    field: 'accountId',
				    sortable: true
				},{
				    width: '170',
				    title: 'IP地址',
				    field: 'ipAddr',
				    sortable: true
				},{
				    width: '180',
				    title: 'URL地址',
				    field: 'url',
				    sortable: true
				}
                    	]],
            columns: [[ {
                width: '180',
                title: '内容',
                field: 'content',
                sortable: true
            },{
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '150',
                title: '操作时间',
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


    function searchAccountLogLogFun() {
    	accountLogDataGrid.datagrid('load', $.serializeObject($('#searchAccountLogForm')));
    }
    function cleanAccountLogLogFun() {
        $('#searchAccountLogForm input').val('');
        accountLogDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountLogForm">
            <table>
                <tr>
                    <th>系统标识:</th>
                    <td><input name="systemName" class="easyui-textbox" placeholder="请输入系统标识"/></td>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <th>IP地址:</th>
                    <td><input name="ipAddr" class="easyui-textbox" placeholder="请输入IP地址"/></td>
                    <th>开始时间:</th>
                    <td><input name="timeStart" class="easyui-datetimebox"  placeholder="请输入开始时间"/></td>
                    <th>结束时间:</th>
                    <td><input name="timeEnd" class="easyui-datetimebox"   placeholder="请输入结束时间"/></td>
                    <td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountLogLogFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountLogLogFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountLogDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
