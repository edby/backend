<%@ page import="lombok.experimental.var" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var keychainDataGrid;
    $(function () {
        keychainDataGrid = $('#keychainDataGrid').datagrid({
            url: '${ctx}/bitpay/bitpayKeychain/data',
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'keychainId',
            sortName: 'keychainId',
            sortOrder: 'desc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
                {
                    width: '150',
                    title: '证券id',
                    field: 'stockinfoId',
                    sortable: true
                },{
				    width: '300',
				    title: '钱包id',
				    field: 'id',
				    sortable: true
				}, {
				    width: '180',
				    title: '钱包名称',
				    field: 'label',
				    sortable: true
				}, {
                    width: '180',
                    title: '币种',
                    field: 'coin',
                    sortable: true
                }
				
               ]],
            columns: [[  {
                width: '150',
                title: '余额',
                field: 'balance',
                formatter: function (value, row, index) {
                	return (value/100000000).toFixed(12);
                }
            }, {
                width: '150',
                title: '已确认余额',
                field: 'confirmedBalance',
                formatter: function (value, row, index) {
                	return (value/100000000).toFixed(12);
                }
            }, {
                width: '90',
                title: '手续费费率',
                field: 'feeTxConfirmTarget'
            },{
                width: '100',
                title: '类型',
                field: 'walletType',
                formatter: function (value, row, index) {
                	if(value=='1'){
                		return '平台充值钱包';
                	}else{
                		return '平台划拨钱包';
                	}
                }
            }<shiro:hasPermission name="trade:setting:keychain:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 250,
                    formatter: function (value, row, index) {
                        var str = '';
                        <%-- str += $.formatString('<a href="javascript:void(0)" class="keychain-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editPwdFun(\'{0}\');" >修改密码</a>', row.keychainId);
                         str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                         str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editKeychainFun(\'{0}\');" >编辑</a>', row.keychainId);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                         --%>
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteKeychainFun(\'{0}\');" >删除</a>', row.keychainId);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-lock" data-options="plain:true,iconCls:\'fi-lock icon-blue\'" onclick="lockFun(\'{0}\');" >锁定</a>', row.keychainId);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-unlock" data-options="plain:true,iconCls:\'fi-unlock icon-blue\'" onclick="unlockFun(\'{0}\');" >解锁</a>', row.keychainId);
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
                <shiro:hasPermission name="trade:setting:keychain:operator">
                $('.keychain-easyui-linkbutton-edit').linkbutton({text: '修改密码'});
                $('.role-easyui-linkbutton-lock').linkbutton({text: '锁定'});
                $('.role-easyui-linkbutton-unlock').linkbutton({text: '解锁'});
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#keychainToolbar' 
        });
    });

    function addKeychainFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/bitpay/bitpayKeychain/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = keychainDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editKeychainForm');
                    f.submit();
                }
            }]
        });
    }

    function lockFun(id)
    {
        $.messager.confirm('确认锁定','您确认要锁定bitgo账户吗？锁定后将无法交易，确定锁定请点击确认!',function(r){
            if (r){
                $.post('${ctx}/bitpay/bitpayKeychain/lock', {
                }, function (result) {
                    console.log(result);
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        keychainDataGrid.datagrid('reload');
                    }else{
                        parent.$.messager.alert('提示', "incorrect otp", 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
    function unlockFun(id)
    {
        $.messager.prompt('确认解锁','您确认要解锁bitgo账户吗？确定解锁，请输入GA：',function(r){
            if (r){
                $.post('${ctx}/bitpay/bitpayKeychain/unlock', {
                    gaCode: r
                }, function (result) {
                    console.log(result);
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        keychainDataGrid.datagrid('reload');
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
    <%--
    function editKeychainFun(id) {
        if (id == undefined) {
            var rows = keychainDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            keychainDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 400,
            href: '${ctx}/bitpay/bitpayKeychain/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = keychainDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editKeychainForm');
                    f.submit();
                }
            }]
        });
    }

    function editPwdFun(id) {
        if (id == undefined) {
            var rows = keychainDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            keychainDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 400,
            height: 180,
            href: '${ctx}/bitpay/bitpayKeychain/chagePwd?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = keychainDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editChgPwdForm');
                    f.submit();
                }
            }]
        });
    }--%>
    function deleteKeychainFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = keychainDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            keychainDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前记录吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/bitpay/bitpayKeychain/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        keychainDataGrid.datagrid('reload');
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
    
    function searchKeychainFun() {
    	keychainDataGrid.datagrid('load', $.serializeObject($('#searchKeychainForm')));
    }
    function cleanKeychainFun() {
        $('#searchKeychainForm input').val('');
        keychainDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchKeychainForm">
            <table>
                <tr>
                    <th>钱包ID:</th>
                    <td><input name="walletId" class="easyui-textbox" placeholder="请输入钱包ID"/></td>
                    <th>钱包名称:</th>
                    <td><input name="walletName" placeholder="请输入钱包名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchKeychainFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanKeychainFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="keychainDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="keychainToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:keychain:operator">
    <a onclick="addKeychainFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
