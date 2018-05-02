<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountDataGrid;
    $(function () {
        accountDataGrid = $('#accountDataGrid').datagrid({
            url: '${ctx}/account/account/data',
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
				    title: '账户ID',
				    field: 'id',
				    sortable: true
				},{
				    width: '70',
				    title: 'UNID',
				    field: 'unid',
				    sortable: true
				},{
				    width: '180',
				    title: '账户名',
				    field: 'accountName',
				    sortable: true
				}
                    	]],
            columns: [[ {
                width: '180',
                title: '邮箱',
                field: 'email',
                sortable: true,
                hidden:true
            },{
                width: '80',
                title: '国家',
                field: 'country',
                sortable: true
            },{
                width: '150',
                title: '手机号',
                field: 'mobNo',
                sortable: true
            },{
                width: '100',
                title: '语言',
                field: 'lang',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '150',
                title: '备注',
                field: 'remark',
                sortable: true
            }, {
                width: '100',
                title: '状态',
                field: 'status',
                formatter: function (value, row, index) {
                    //0正常、1冻结、2注销
                    if(value == 0){return '正常';}
                    if(value == 1){return '冻结';}
                    if(value == 2){return '注销';}
                }
            }, {
                width: '130',
                title: '是否绑定google验证器',
                field: 'googlebind',
                hidden:true,
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:account:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 180,
                    formatter: function (value, row, index) {
                        var str = '';
                        var str = '';
                        if(row.authKey==null || row.authKey=='') {

                        }else
                        {
                            str += $.formatString('<a href="javascript:void(0)" class="account-easyui-linkbutton-unbangding" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="clearGaFun(\'{0}\');" >解绑GA</a>', row.id);
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        }
                        if( row.status == 0){
                        	str += $.formatString('<a href="javascript:void(0)" id="status_1" class="user-easyui-linkbutton-dongjie" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editAccountFun(\'{0}\',\'1\');" >冻结</a>', row.id);
                        }
                        if( row.status  == 1){
                        	str += $.formatString('<a href="javascript:void(0)" id="status_0" class="user-easyui-linkbutton-jiedong" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="editAccountFun(\'{0}\',\'0\');" >解冻</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:account:operator">
                $('.account-easyui-linkbutton-unbangding').linkbutton({text: '解绑GA'});
                $('.user-easyui-linkbutton-dongjie').linkbutton({text: '冻结'});
                $('.user-easyui-linkbutton-jiedong').linkbutton({text: '解冻'});
                </shiro:hasPermission>
            }
        });
    });

    function clearGaFun(id){
        parent.$.modalDialog({
            title: '解绑GA',
            width: 500,
            height: 400,
            href: '${ctx}/account/account/unbindga?id=' + id,
            buttons: [{
                text: '解绑',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = accountDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#clearAccountGaForm');
                    f.submit();
                }
            }]
        });}


    function editAccountFun(id,status) {
        if (id == undefined) {
            var rows = accountDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            accountDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:account:operator">
        var url='${ctx}/account/account/pass';
        var msg="你是否要解冻当前账户吗?";
        if(status == 1){
        	url='${ctx}/account/account/nopass';
        	msg="你是否要冻结当前账户吗?";
        }
        
        parent.$.messager.confirm('询问', msg, function (b) {
            if (b) {
                progressLoad();
                $.post(url, {
                	id: id
                }, function (result) {
                	 progressClose();
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        accountDataGrid.datagrid('load', $.serializeObject($('#searchAccountForm')));
                    }else{
                        parent.$.messager.alert('提示', result.message, 'info');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                }, 'JSON');
            }
        });
        </shiro:hasPermission>
    }
    function searchAccountFun() {
    	accountDataGrid.datagrid('load', $.serializeObject($('#searchAccountForm')));
    }
    function cleanAccountFun() {
        $('#searchAccountForm input').val('');
        accountDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入账户名"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
