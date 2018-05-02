<%@ page import="lombok.experimental.var" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var keychainERC20DataGrid;
    $(function () {
        keychainERC20DataGrid = $('#keychainERC20DataGrid').datagrid({
            url: '${ctx}/bitpay/bitpayKeychainErc20/data',
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'keychainId',
            sortName: 'keychainId',
            sortOrder: 'desc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[
                {
                    width: '150',
                    title: '证券信息id',
                    field: 'stockinfoId',
                    sortable: true
                },
                {
                    width: '100',
                    title: '证券名称',
                    field: 'stockName'
                },{
                    width: '300',
                    title: '钱包id',
                    field: 'walletId',
                    sortable: true
                }, {
                    width: '180',
                    title: '钱包名称',
                    field: 'walletName',
                    sortable: true
                },
                {
                    width: '100',
                    title: '币种',
                    field: 'coin',
                    sortable: true
                }
                ,
                {
                    width: '100',
                    title: '钱包类型',
                    field: 'walletType',
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return '付款热钱包';
                        } else if(value == '2'){
                            return '付款冷钱包';
                        }else if(value == '3'){
                            return '归集eth费用钱包';
                        }else{
                            return '未知钱包类型';
                        }
                    }
                }<shiro:hasPermission name="trade:setting:keychainerc20:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 250,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteKeychainERC20Fun(\'{0}\');" >删除</a>', row.id);
//                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
//                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-lock" data-options="plain:true,iconCls:\'fi-lock icon-blue\'" onclick="editKeychainERC20Fun(\'{0}\');" >编辑</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:keychainerc20:operator">
                $('.keychain-easyui-linkbutton-edit').linkbutton({text: '修改密码'});
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#keychainERC20Toolbar'
        });
    });

    function addKeychainERC20Fun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/bitpay/bitpayKeychainErc20/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = keychainERC20DataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editKeychainERC20Form');
                    f.submit();
                }
            }]
        });
    }


    function editKeychainERC20Fun(id) {
        if (id == undefined) {
            var rows = keychainERC20DataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            keychainERC20DataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 400,
            href: '${ctx}/bitpay/bitpayKeychainErc20/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = keychainERC20DataGrid;
                    var f = parent.$.modalDialog.handler.find('#editKeychainERC20Form');
                    f.submit();
                }
            }]
        });
    }

    <%--function editPwdFun(id) {--%>
    <%--if (id == undefined) {--%>
    <%--var rows = keychainDataGrid.datagrid('getSelections');--%>
    <%--id = rows[0].id;--%>
    <%--} else {--%>
    <%--keychainDataGrid.datagrid('unselectAll').datagrid('uncheckAll');--%>
    <%--}--%>
    <%--parent.$.modalDialog({--%>
    <%--title: '编辑',--%>
    <%--width: 400,--%>
    <%--height: 180,--%>
    <%--href: '${ctx}/bitpay/bitpayKeychain/chagePwd?id=' + id,--%>
    <%--buttons: [{--%>
    <%--text: '确定',--%>
    <%--handler: function () {--%>
    <%--parent.$.modalDialog.openner_dataGrid = keychainDataGrid;--%>
    <%--var f = parent.$.modalDialog.handler.find('#editChgPwdForm');--%>
    <%--f.submit();--%>
    <%--}--%>
    <%--}]--%>
    <%--});--%>
    <%--}--%>

    function deleteKeychainERC20Fun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = keychainERC20DataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            keychainERC20DataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前记录吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/bitpay/bitpayKeychainErc20/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        keychainERC20DataGrid.datagrid('reload');
                    } else {
                        parent.$.messager.alert('提示', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }

    function searchKeychainERC20Fun() {
        keychainERC20DataGrid.datagrid('load', $.serializeObject($('#searchKeychainERC20Form')));
    }

    function cleanKeychainERC20Fun() {
        $('#searchKeychainERC20Form input').val('');
        keychainERC20DataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchKeychainERC20Form">
            <table>
                <tr>
                    <th>钱包名称:</th>
                    <td><input name="walletName" placeholder="请输入钱包名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchKeychainERC20Fun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanKeychainERC20Fun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="keychainERC20DataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="keychainERC20Toolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:keychainerc20:operator">
        <a onclick="addKeychainERC20Fun();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
