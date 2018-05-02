<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var stockRateDataGrid;
    $(function () {
        stockRateDataGrid = $('#stockRateDataGrid').datagrid({
            url: '${ctx}/stock/rate/data',
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
            				    width: '140',
            				    title: 'ID',
            				    field: 'id',
                                hidden: true
            				}, {
            				    width: '100',
            				    title: '证券代码',
            				    field: 'stockCode',
            				    sortable: true
            				}, {
            				    width: '200',
            				    title: '证券名称',
            				    field: 'stockName',
            				    sortable: true
            				}		
                           ]],       
            columns: [[{
                width: '100',
                title: '费用类型',
                field: 'rateType',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
            width: '100',
                title: '值类型',
                field: 'rateValueType',
                formatter: function (value, row, index) {
                if(value*1==1)
                {
                    return '比例';
                }else
                {
                    return '绝对值';
                }
            }
        }, {
                width: '100',
                title: '费率(%)',
                field: 'rate',
                sortable: true
            },{
                width: '100',
                title: '备注',
                field: 'remark',
                sortable: true
            },{
                width: '100',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '140',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                    width: '100',
                    title: '修改人',
                    field: 'updateByName',
                    hidden:true
            }, {
                    width: '150',
                    title: '修改时间',
                    field: 'updateDate',
                    hidden:true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
            }<shiro:hasPermission name="trade:setting:stockrate:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editStockRateFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteStockRateFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:stockrate:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#stockRateToolbar'
        });
    });

    function addStockRateFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 330,
            href: '${ctx}/stock/rate/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = stockRateDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editStockRateForm');
                    f.submit();
                }
            }]
        });
    }

    function editStockRateFun(id) {
        if (id == undefined) {
            var rows = stockRateDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            stockRateDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 330,
            href: '${ctx}/stock/rate/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = stockRateDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editStockRateForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteStockRateFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = stockRateDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            stockRateDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前费率吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/stock/rate/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        stockRateDataGrid.datagrid('reload');
                    }else{
                        parent.$.messager.alert('错误', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
    function searchStockRateFun() {
    	stockRateDataGrid.datagrid('load', $.serializeObject($('#searchStockRateForm')));
    }
    function cleanStockRateFun() {
        $('#searchStockRateForm input').val('');
        stockRateDataGrid.datagrid('load', {});
    }
    function exportdata(){
    	$("#export_stockrate").attr("action", "${ctx}/stock/rate/export");
    	$("#export_stockrate").submit();
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchStockRateForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td><input name="stockCode" class="easyui-textbox" placeholder="请输入证券代码"/></td>
                    <th>证券名称:</th>
                    <td><input name="stockName" class="easyui-textbox" placeholder="请输入证券名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchStockRateFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanStockRateFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="stockRateDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="stockRateToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:stockrate:operator">
    <a onclick="addStockRateFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
       <a onclick="exportdata();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-save icon-green'">导出</a>
    </shiro:hasPermission>
</div>
<form id="export_stockrate" method="get"  style="display: none;"></form>
