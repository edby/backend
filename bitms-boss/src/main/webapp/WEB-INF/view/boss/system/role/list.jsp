<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var roleDataGrid;
    $(function () {
        roleDataGrid = $('#roleDataGrid').datagrid({
            url: '${ctx}/system/role/data',
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[{
                width: '100',
                title: '主键',
                field: 'id',
                hidden: true
            }, {
                width: '200',
                title: '编码',
                field: 'roleCode',
                sortable: true
            }, {
                width: '250',
                title: '名称',
                field: 'roleName',
                sortable: true
            }, {
                width: '100',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '100',
                title: '是否需要绑定GA',
                field: 'needGa',
                formatter: function (value, row, index) {
                   if(value == 1){
                       return '是';
                   }else{
                       return '否';
                   }
                }
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }<shiro:hasPermission name="system:setting:role:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-ok" data-options="plain:true,iconCls:\'fi-check icon-green\'" onclick="grantRoleFun(\'{0}\');" >授权</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editRoleFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteRoleFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="system:setting:role:operator">
                $('.role-easyui-linkbutton-ok').linkbutton({text: '授权'});
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#roleToolbar'
        });
    });

    function addRoleFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/system/role/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = roleDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editRoleForm');
                    f.submit();
                }
            }]
        });
    }

    function editRoleFun(id) {
        if (id == undefined) {
            var rows = roleDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 300,
            href: '${ctx}/system/role/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = roleDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editRoleForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteRoleFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = roleDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前角色？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/system/role/del', {
                    ids: id
                }, function (result) {
                    if (result.code == 200) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        roleDataGrid.datagrid('reload');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }

    function grantRoleFun(id) {
        if (id == undefined) {
            var rows = roleDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }

        parent.$.modalDialog({
            title: '授权',
            width: 500,
            height: 500,
            href: '${ctx}/system/role/grant?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = roleDataGrid;
                    var f = parent.$.modalDialog.handler.find('#roleGrantForm');
                    f.submit();
                }
            }]
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',fit:true,border:false">
        <table id="roleDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="roleToolbar" style="display: none;">
    <shiro:hasPermission name="system:setting:role:operator">
    <a onclick="addRoleFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
