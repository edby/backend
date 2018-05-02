<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var erc20TokenDataGrid;
    $(function () {
        erc20TokenDataGrid = $('#erc20TokenDataGrid').datagrid({
            url: '${ctx}/stock/erc20Token/data',
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
            				    width: '100',
            				    title: 'ID',
            				    field: 'id',
                                hidden: true
            				}, {
            				    width: '80',
            				    title: '符号',
            				    field: 'symbol',
            				    sortable: true
            				}, {
            				    width: '150',
            				    title: '全称',
            				    field: 'symbolName',
            				    sortable: true
            				}		
                           ]],       
            columns: [[{
                width: '100',
                title: '平台交易对',
                field: 'pair',
                sortable:true
            },{
            width: '100',
                title: '激活状态',
                field: 'isActive',
                formatter: function (value, row, index) {
                if(value=='no')
                {
                    return '未激活';
                }else
                {
                    return '已激活';
                }
            }
        }, {
                width: '300',
                title: '合约地址',
                field: 'contractAddr',
                sortable: true
            }, {
                width:'100',
                 title:'精度',
                field: 'tokenDecimals',
                sortable: true
                }, {
                width:'120',
                title:'发行量',
                field: 'totalSupply',
                sortable: true
            },{
                width: '140',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            } , {
                    width: '150',
                    title: '有效期结束时间',
                    field: 'activeEndDate',
                    sortable:true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
            }
                <shiro:hasPermission name="trade:setting:erc20Token:operator">
              <%--  , {
                    field: 'action',
                    title: '操作',
                    width: 100,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editErc20TokenFun(\'{0}\');" >编辑</a>', row.id);
                        return str;
                    }
                }--%>
                </shiro:hasPermission>
                ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="trade:setting:erc20Token:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                </shiro:hasPermission>
            },
            toolbar: '#erc20TokenToolbar'
        });
    });
    function addErc20TokenFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 600,
            height: 330,
            href: '${ctx}/stock/erc20Token/modify?id=',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = erc20TokenDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editErc20TokenForm');
                    f.submit();
                }
            }]
        });
    }
    function editErc20TokenFun(id) {
        if (id == undefined) {
            var rows = erc20TokenDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            erc20TokenDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 600,
            height: 330,
            href: '${ctx}/stock/erc20Token/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = erc20TokenDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editErc20TokenForm');
                    f.submit();
                }
            }]
        });
    }
    function searchErc20TokenFun() {
        erc20TokenDataGrid.datagrid('load', $.serializeObject($('#searchErc20TokenForm')));
    }
    function cleanErc20TokenFun() {
        $('#searchErc20TokenForm input').val('');
        erc20TokenDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchErc20TokenForm">
            <table>
                <tr>
                    <th>符号:</th>
                    <td><input name="symbol" class="easyui-textbox" placeholder="请输入符号"/></td>
                    <th>全称:</th>
                    <td><input name="symbolName" class="easyui-textbox" placeholder="请输入全称"/></td>
                    <th>平台交易对:</th>
                    <td><input name="pair" class="easyui-textbox" placeholder="请输入平台交易对"/></td>
                    <th>合约地址:</th>
                    <td><input name="contractAddr" class="easyui-textbox" placeholder="请输入合约地址"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchErc20TokenFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanErc20TokenFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="erc20TokenDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="erc20TokenToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:erc20Token:operator">
    <a onclick="addErc20TokenFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>

