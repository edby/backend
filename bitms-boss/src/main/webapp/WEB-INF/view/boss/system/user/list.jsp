<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var userDataGrid;
    var organizTree;

    $(function () {
        organizTree = $('#organizTree').tree({
            url: '${ctx}/system/organiz/tree',
            parentField: 'pid',
            lines: true,
            onClick: function (node) {
                userDataGrid.datagrid('load', {
                    orgId: node.id
                });
            },
            onLoadSuccess:function (node, data) {
                userDataGrid = $('#userDataGrid').datagrid({
                    url: '${ctx}/system/user/data',
                    fit: true,
                    striped: true,
                    rownumbers: true,
                    pagination: true,
                    singleSelect: true,
                    idField: 'id',
                    pageSize: 20,
                    pageList: [10, 20, 30, 40, 50, 100],
                    columns: [[{
                        width: '80',
                        title: '登录名',
                        field: 'userName'
                    }, {
                        width: '80',
                        title: '姓名',
                        field: 'trueName'
                    }, {
                        width: '180',
                        title: '身份证',
                        field: 'idCard'
                    }, {
                        width: '80',
                        title: '部门ID',
                        field: 'orgId',
                        hidden: true
                    }, {
                        width: '180',
                        title: '所属部门',
                        field: 'orgName'
                    }, {
                        width: '40',
                        title: '性别',
                        field: 'gender',
                        formatter: function (value, row, index) {
                            switch (value) {
                                case true:
                                    return '男';
                                case false:
                                    return '女';
                            }
                        }
                    }, {
                        width: '60',
                        title: '状态',
                        field: 'active',
                        sortable: true,
                        formatter: function (value, row, index) {
                            switch (value) {
                                case true:
                                    return '启用';
                                case false:
                                    return '停用';
                            }
                        }
                    }, {
                        width: '100',
                        title: 'GA绑定状态',
                        field: 'authKey',
                        sortable: true,
                        formatter: function (value, row, index) {
                            if(value!=null && value!='') {
                                return '已绑定';
                            }else{
                                return '未绑定';
                            }
                        }
                    }<shiro:hasPermission name="system:setting:user:operator">
                        , {
                            field: 'action',
                            title: '操作',
                            width: 250,
                            formatter: function (value, row, index) {
                                var str = '';
                                if(row.authKey==null || row.authKey=='') {
                                    str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-bangding" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="bindUserFun(\'{0}\');" >绑定GA</a>', row.id);
                                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                                }else
                                {
                                    str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-unbangding" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="clearUserFun(\'{0}\');" >解绑GA</a>', row.id);
                                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                                }
                               str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editUserFun(\'{0}\');" >编辑</a>', row.id);
                                str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                                str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteUserFun(\'{0}\');" >删除</a>', row.id);
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
                        <shiro:hasPermission name="system:setting:user:operator">
                        $('.user-easyui-linkbutton-bangding').linkbutton({text: '绑定GA'});
                        $('.user-easyui-linkbutton-unbangding').linkbutton({text: '解绑GA'});
                        $('.user-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                        $('.user-easyui-linkbutton-del').linkbutton({text: '删除'});
                        </shiro:hasPermission>
                    },
                    toolbar: '#userToolbar'
                });
            }
        });
    });

    function addUserFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/system/user/modify',
            buttons: [{
                text: '添加',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editUserForm');
                    f.submit();
                }
            }]
        });
    }
        function bindUserFun(id){
            parent.$.modalDialog({
                title: '绑定GA',
                width: 500,
                height: 420,
                href: '${ctx}/system/user/bindga?id=' + id,
                buttons: [{
                text: '绑定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#bindUserForm');
                    f.submit();
                }
            }]
        });}

    function clearUserFun(id){
        parent.$.modalDialog({
            title: '解绑GA',
            width: 500,
            height: 420,
            href: '${ctx}/system/user/unbindga?id=' + id,
            buttons: [{
                text: '解绑',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#clearUserForm');
                    f.submit();
                }
            }]
        });}


    function deleteUserFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = userDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            userDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前用户？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/system/user/del', {
                    ids: id
                }, function (result) {
                    if (result.code = 200) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        userDataGrid.datagrid('reload');
                    } else {
                        parent.$.messager.alert('错误', result.object, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }

    function editUserFun(id) {
        if (id == undefined) {
            var rows = userDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            userDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 400,
            href: '${ctx}/system/user/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editUserForm');
                    f.submit();
                }
            }]
        });
    }

    function searchUserFun() {
        userDataGrid.datagrid('load', $.serializeObject($('#searchUserForm')));
    }
    function cleanUserFun() {
        $('#searchUserForm input').val('');
        userDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchUserForm">
            <table>
                <tr>
                    <th>帐号:</th>
                    <td><input name="userName" class="easyui-textbox" placeholder="请输入用户帐号"/></td>
                    <th>姓名:</th>
                    <td><input name="trueName" class="easyui-textbox" placeholder="请输入用户姓名"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchUserFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanUserFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'用户列表'">
        <table id="userDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div data-options="region:'west',border:true,split:false,title:'组织机构'" style="width:150px;overflow: hidden; ">
        <ul id="organizTree" style="width:160px;margin: 10px 10px 10px 10px"></ul>
    </div>
</div>
<div id="userToolbar" style="display: none;">
    <shiro:hasPermission name="system:setting:user:operator">
    <a onclick="addUserFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
<jsp:include page="/commons/setup_ajax.jsp"></jsp:include>