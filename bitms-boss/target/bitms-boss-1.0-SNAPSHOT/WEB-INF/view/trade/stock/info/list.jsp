<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var stockInfoDataGrid;
    $(function () {
        stockInfoDataGrid = $('#stockInfoDataGrid').datagrid({
            url: '${ctx}/stock/info/data',
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
				    title: '证券ID',
				    field: 'id'
				}, {
				    width: '100',
				    title: '证券代码',
				    field: 'stockCode',
				    sortable: true
				}, {
				    width: '150',
				    title: '证券名称',
				    field: 'stockName',
				    sortable: true
				}		
               ]],
            columns: [[ {
                width: '70',
                title: '证券类型',
                field: 'stockType',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '是否充值',
                field: 'canRecharge',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '是否提现',
                field: 'canWithdraw',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '70',
                title: '是否交易',
                field: 'canTrade',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '是否可借',
                field: 'canBorrow',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '能否可转换',
                field: 'canConversion',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '70',
                title: '是否是交易对',
                field: 'isExchange',
                formatter: function (value, row, index) {
                    return getDictValueByCode(value);
                }
            },{
                width: '150',
                title: '做多最大杠杆',
                field: 'maxLongLever',
                sortable: true
            },{
                width: '150',
                title: '做空最大杠杆',
                field: 'maxShortLever',
                sortable: true
            },{
                width: '150',
                title: '交割结算周期',
                field: 'settlementCycle',
                sortable: true
            },{
                width: '150',
                title: '单笔委托买入数量上限',
                field: 'maxSingleBuyEntrustAmt',
                sortable: true
            },{
                width: '150',
                title: '单笔委托卖出数量上限',
                field: 'maxSingleSellEntrustAmt',
                sortable: true
            },{
                    width: '150',
                    title: '多单爆仓价提前比例',
                    field: 'closePositionLongPrePercent',
                    sortable: true
                }
                ,{
                    width: '150',
                    title: '空单爆仓价提前比例',
                    field: 'closePositionShortPrePercent',
                    sortable: true
                },{
                    width: '150',
                    title: '交易资产配置表',
                    field: 'tableAsset',
                    sortable: true
                }
                ,{
                    width: '150',
                    title: '委托配置表',
                    field: 'tableEntrust',
                    sortable: true
                }
                ,{
                    width: '150',
                    title: '成交配置表',
                    field: 'tableRealDeal',
                    sortable: true
                }
                ,{
                    width: '150',
                    title: '资产流水配置表',
                    field: 'tableFundCurrent',
                    sortable: true
                },{
                width: '150',
                title: '交割结算价格',
                field: 'settlementPrice',
                sortable: true
            },{
                width: '150',
                title: '交割结算步骤',
                field: 'settlementStep',
                sortable: true
            },{
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '70',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '120',
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
            }<shiro:hasPermission name="trade:setting:stockinfo:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editStockInfoFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteStockInfoFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:stockinfo:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#stockInfoToolbar' 
        });
    });

    function addStockInfoFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 1200,
            height: 500,
            href: '${ctx}/stock/info/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = stockInfoDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editStockInfoForm');
                    f.submit();
                }
            }]
        });
    }

    function editStockInfoFun(id) {
        if (id == undefined) {
            var rows = stockInfoDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            stockInfoDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 1200,
            height: 500,
            href: '${ctx}/stock/info/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = stockInfoDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editStockInfoForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteStockInfoFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = stockInfoDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            stockInfoDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前证券吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/stock/info/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        stockInfoDataGrid.datagrid('reload');
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
    
    function searchStockInfoFun() {
    	stockInfoDataGrid.datagrid('load', $.serializeObject($('#searchStockInfoForm')));
    }
    function cleanStockInfoFun() {
        $('#searchStockInfoForm input').val('');
        stockInfoDataGrid.datagrid('load', {});
    }
    function exportdata(){
    	$("#export_stockinfo").attr("action", "${ctx}/stock/info/export");
    	$("#export_stockinfo").submit();
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchStockInfoForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td><input name="stockCode" class="easyui-textbox" placeholder="请输入证券代码"/></td>
                    <th>证券名称:</th>
                    <td><input name="stockName" class="easyui-textbox" placeholder="请输入证券名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchStockInfoFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanStockInfoFun();">清空</a>
                        <a onclick="exportdata();" href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'fi-save icon-green'">导出</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="stockInfoDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="stockInfoToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:stockinfo:operator">
    <%--<a onclick="addStockInfoFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
       <a onclick="exportdata();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-save icon-green'">导出</a>--%>
    </shiro:hasPermission>
</div>
<form id="export_stockinfo" method="get"  style="display: none;"></form>
