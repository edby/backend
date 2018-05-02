<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/monitor.js" charset="utf-8"></script>
<script type="text/javascript">
    var monitorIndexDataGrid;
    var monitorIndexDataGrid2;
    $(function () {
        monitorIndexDataGrid2 = $('#monitorIndexDataGrid2').datagrid({
            //url: '${ctx}/fund/systemwallet/addr/data',
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
                    width: '100',
                    title: '主键',
                    field: 'id',
                    hidden: true
                }, {
                    width: '200',
                    title: '指标名称',
                    field: 'idxName',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券id',
                    field: 'stockinfoId',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券名称',
                    field: 'stockName',
                    sortable: true
                },
                {
                    width: '100',
                    title: '最小阈值',
                    field: 'minValue',
                    sortable: true
                }, {
                    width: '100',
                    title: '最大阈值',
                    field: 'maxValue',
                    sortable: true
                },{
                    width: '100',
                    title: '比较方向',
                    field: 'compDirect',
                    formatter: function (value, row, index) {
                       return getCompDirectName(value);
                    }
                }
                <shiro:hasPermission name="monitor:setting:monitorindex:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 140,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editMonitorLimitParamFun(\'{0}\',\'{1}\');" >编辑</a>', row.id,row.idxName);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteMonitorLimitParamFun(\'{0}\');" >删除</a>', row.id);
                        return str;
                    }
                }
                </shiro:hasPermission>, {
                    width: '300',
                    title: '备注',
                    field: 'paramDesc',
                    sortable: true
                }, {
                    width: '150',
                    title: '创建时间',
                    field: 'createDate',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value * 1, "yyyy-MM-dd hh:mm:ss");
                    }
                }

            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="monitor:setting:monitorindex:operator">
                $('.icoproduct-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.icoproduct-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#monitorLimitParambar'
        });
    });

    monitorIndexDataGrid = $('#monitorIndexDataGrid').datagrid({
        url: '${ctx}/monitor/monitorIndex/data',
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
                width: '100',
                title: '主键',
                field: 'id',
                hidden: true
            }, {
                width: '200',
                title: '指标名称',
                field: 'idxName'
            },  {
                width: '100',
                title: '指标级别',
                field: 'idxLevel',
                sortable: true,
                formatter: function (value, row, index) {
                   return getIdxLevelName(value);
                }
            },{
                width: '150',
                title: '处理类型',
                field: 'actionType',
                sortable: true
            },
            {
                width: '130',
                title: '参数值',
                field: 'actionValue',
                formatter: function (value, row, index) {
                    var str = '';
                    if(value.indexOf("100000000001") != -1){
                        str += "短信通知、";
                    }
                    if(value.indexOf("100000000002") != -1){
                        str += "邮件通知、";
                    }
                    return str.substr(0,str.length-1);
                }
            }<shiro:hasPermission name="monitor:setting:monitorindex:operator">
            , {
                field: 'action',
                title: '操作',
                width: 140,
                formatter: function (value, row, index) {
                    var str = '';
                    str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editMonitorIndexFun(\'{0}\');" >编辑</a>', row.id);
                    str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    str += $.formatString('<a href="javascript:void(0)" class="icoproduct-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteMonitorIndexFun(\'{0}\');" >删除</a>', row.id);
                    return str;
                }
            }
            </shiro:hasPermission>, {
                width: '300',
                title: '备注',
                field: 'paramDesc'
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value * 1, "yyyy-MM-dd hh:mm:ss");
                }
            }
        ]],
        onLoadSuccess: function (data) {
            <shiro:hasPermission name="monitor:setting:monitorindex:operator">
            $('.icoproduct-easyui-linkbutton-edit').linkbutton({text: '编辑'});
            $('.icoproduct-easyui-linkbutton-del').linkbutton({text: '删除'});
            </shiro:hasPermission>
            var rows = $('#monitorIndexDataGrid').datagrid("getRows");
            if (rows.length > 0) {
                $("#relatedId").val(rows[0].id);
                $("#selectedName").val(rows[0].idxName);
                var opts = monitorIndexDataGrid2.datagrid("options");
                opts.url = '${ctx}/monitor/index/limitParam/data';
                monitorIndexDataGrid2.datagrid('reload', {
//                    stockinfoId: rows[0].stockinfoId,
                    relatedId: rows[0].id
                });
                $('#monitorIndexDataGrid').datagrid('selectRow', 0);
            }
        },
        onClickRow: function (rowIndex, rowData) {
            $("#relatedId").val(rowData.id);
            $("#selectedName").val(rowData.idxName);
            monitorIndexDataGrid2.datagrid('reload', {relatedId: rowData.id});
        },
        toolbar: '#monitorIndexbar'
    });

    function isHaveProduct() {
        if ($("#walletId").val() == null || $("#walletId").val() == '') {
            $.messager.alert('提示', '请选择指标id后,再对指标内容进行编辑!');
            return false;
        } else {
            return true;
        }
    }

    //添加指标
    function addMonitorIndexFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/monitor/monitorIndex/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorIndexDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editMonitorIndexForm');
                    f.submit();
                }
            }]
        });
    }

    //编辑指标
    function editMonitorIndexFun(id) {
        if (id == undefined) {
            var rows = monitorIndexDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            monitorIndexDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 300,
            href: '${ctx}/monitor/monitorIndex/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorIndexDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMonitorIndexForm');
                    f.submit();
                }
            }]
        });
    }

    //删除指标
    function deleteMonitorIndexFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = monitorIndexDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            monitorIndexDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前监控指标吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/monitor/monitorIndex/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        monitorIndexDataGrid.datagrid('reload');
                    } else {
                        parent.$.messager.alert('错误', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }


    //添加指标参数
    function addMonitorLimitParamFun() {

        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/monitor/index/limitParam/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorIndexDataGrid2;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editMonitorLimitParamForm');
                    f.submit();
                }
            }]
        });
    }

    //编辑指标参数
    function editMonitorLimitParamFun(id,idxName) {
        if (id == undefined) {
            var rows = monitorIndexDataGrid2.datagrid('getSelections');
            id = rows[0].id;
        } else {
            monitorIndexDataGrid2.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑阈值参数',
            width: 500,
            height: 400,
            href: '${ctx}/monitor/index/limitParam/modify?id=' + id + '&idxName=' + idxName,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorIndexDataGrid2;
                    var f = parent.$.modalDialog.handler.find('#editMonitorLimitParamForm');
                    f.submit();
                }
            }]
        });
    }

    //删除指标
    function deleteMonitorLimitParamFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = monitorIndexDataGrid2.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            monitorIndexDataGrid2.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前监控指标参数吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/monitor/index/limitParam/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        monitorIndexDataGrid2.datagrid('reload');
                    } else {
                        parent.$.messager.alert('错误', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
</script>
<input type="hidden" id="selectedName"/>
<input type="hidden" id="relatedId"/>
<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%;">
    <div data-options="region:'north',split:true" style="width: 580px;height:400px;">
        <table id="monitorIndexDataGrid" data-options="fit:true,border:false,title:'监控指标'"></table>
    </div>
    <div data-options="region:'center'" style="padding: 5px; background: #eee;">
        <table id="monitorIndexDataGrid2" data-options="fit:true,border:false,title:'监控指标参数'"></table>
    </div>
</div>
<div id="monitorIndexbar" style="display: none;">
    <shiro:hasPermission name="monitor:setting:index:operator">
        <a onclick="addMonitorIndexFun();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>

<div id="monitorLimitParambar" style="display: none;">
    <shiro:hasPermission name="monitor:setting:monitorindex:operator">
        <a onclick="addMonitorLimitParamFun();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>

