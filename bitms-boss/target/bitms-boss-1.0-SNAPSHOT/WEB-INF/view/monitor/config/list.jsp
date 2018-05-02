<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/monitor.js" charset="utf-8"></script>
<script type="text/javascript">

    var monitorConfigDataGrid;
    $(function () {
        monitorConfigDataGrid = $('#monitorConfigDataGrid').datagrid({
            url: '${ctx}/monitor/config/data',
            fit: true,
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
                    width: '150',
                    title: '监控代码',
                    field: 'monitorCode',
                    sortable: true
                },
                {
                    width: '130',
                    title: '监控名称',
                    field: 'monitorName'
                },

                {
                    width: '150',
                    title: '监控业务列表',
                    field: 'monitorCategorys_str'
                },
                {
                    width: '100',
                    title: '指标id1',
                    field: 'idxid1',
                    hidden: true
                },
                {
                    width: '100',
                    title: '指标id2',
                    field: 'idxid2',
                    hidden: true
                },
                {
                    width: '100',
                    title: '指标id3',
                    field: 'idxid3',
                    hidden: true
                },
                {
                    width: '100',
                    title: '指标id4',
                    field: 'idxid4',
                    hidden: true
                },
                {
                    width: '240',
                    title: '指标1',
                    field: 'idxName1',
                    formatter: function (value, row, index) {
                        var str = "";
                        if (value == 0 || value == null) {
                            str = value;
                        } else {
                            str += $.formatString('<a href="javascript:void(0)" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="idxDetailFun(\'{0}\');" >'+value+'</a>', row.idxid1);
                        }
                        return str;
                    }
                },
                {
                    width: '240',
                    title: '指标2',
                    field: 'idxName2',
                    formatter: function (value, row, index) {
                        var str = "";
                        if (value == 0 || value == null) {
                            str = value;
                        } else {
                            str += $.formatString('<a href="javascript:void(0)" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="idxDetailFun(\'{0}\');" >'+value+'</a>', row.idxid2);
                        }
                        return str;
                    }
                },
                {
                    width: '100',
                    title: '指标3',
                    field: 'idxName3',
                    formatter: function (value, row, index) {
                        var str = "";
                        if (value == 0 || value == null) {
                            str = value;
                        } else {
                            str += $.formatString('<a href="javascript:void(0)" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="idxDetailFun(\'{0}\');" >'+value+'</a>', row.idxid3);
                        }
                        return str;
                    }
                },
                {
                    width: '100',
                    title: '指标4',
                    field: 'idxName4',
                    formatter: function (value, row, index) {
                        var str = "";
                        if (value == 0 || value == null) {
                            str = value;
                        } else {
                            str +=  $.formatString('<a href="javascript:void(0)" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="idxDetailFun(\'{0}\');" >'+value+'</a>', row.idxid4);
                        }
                        return str;
                    }
                },
                {
                    width: '80',
                    title: '启用状态',
                    field: 'active',
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '启用';
                        } else if (value == 0) {
                            return '停用';
                        }
                    }
                }<shiro:hasPermission name="monitor:setting:monitorconfig:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 140,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorConfigEdit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="monitorConfigEditFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        if (row.active == 1) {
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorConfigUnActive" data-options="plain:true,iconCls:\'fi-page-edit icon-red\'" onclick="monitorConfigUnActiveFun(\'{0}\');" >停用</a>', row.id);
                        }
                        else {
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-monitorConfigActive" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="monitorConfigActiveFun(\'{0}\');" >启用</a>', row.id);
                        }


                        return str;
                    }
                }</shiro:hasPermission>,
                {
                    width: '100',
                    title: '轮询时间',
                    field: 'pollingTime'
                },
                {
                    width: '500',
                    title: '参数备注',
                    field: 'monitorDesc',
                }
            ]],
            onLoadSuccess: function (data) {
                //用户未登录时刷新页面
                var codeNum = JSON.stringify(data.code);
                if(codeNum==2003){
                    window.location.reload();
                }
                <shiro:hasPermission name="monitor:setting:monitorconfig:operator">
                $('.easyui-linkbutton-monitorConfigEdit').linkbutton({text: '编辑'});
                $('.easyui-linkbutton-monitorConfigUnActive').linkbutton({text: '停用'});
                $('.easyui-linkbutton-monitorConfigActive').linkbutton({text: '启用'});
                </shiro:hasPermission>

            },
            toolbar: '#monitorConfigToolBar'
        });
    });


    //查看指标详情
    function idxDetailFun(id) {
        parent.$.modalDialog({
            title: '指标详情',
            width: 400,
            height: 250,
            href: '${ctx}/monitor/monitorIndex/detail?id=' + id
//            buttons: [{
//                text: '确定',
//                handler: function () {
//                    var f = parent.$.modalDialog.handler.find('#idxDetail');
//                }
//            }]
        });
        parent.$.modalDialog.handler.find('#idxDetail');

    }


    function monitorConfigAddFun() {
        parent.$.modalDialog({
            title: '编辑',
            width: 600,
            height: 600,
            href: '${ctx}/monitor/config/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorConfigDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMonitorConfigForm');
                    f.submit();
                }
            }]
        });
    }

    function monitorConfigEditFun(id) {
        if (id == undefined) {
            var rows = monitorConfigDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            monitorConfigDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 600,
            href: '${ctx}/monitor/config/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = monitorConfigDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMonitorConfigForm');
                    f.submit();
                }
            }]
        });
    }


    function monitorConfigActiveFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = stockInfoDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            monitorConfigDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '确认启用当前监控设置参数吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/monitor/config/active', {
                    id: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        monitorConfigDataGrid.datagrid('reload');
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

    function monitorConfigUnActiveFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = monitorConfigDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            monitorConfigDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '确认停用当前监控设置参数吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/monitor/config/unActive', {
                    id: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        monitorConfigDataGrid.datagrid('reload');
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

    function searchMonitorConfigAllFun() {
        monitorConfigDataGrid.datagrid('load',
            $.serializeObject($('#searchMonitorConfigForm')));
    }

    function cleanMonitorConfigFun() {
        $('#searchMonitorConfigForm input').val('');
        monitorConfigDataGrid.datagrid('load', {});
    }

    //    $(function(){
    //    	$("#paramTypeTd").html(dictDropDownOptionsList('paramTypeCurr','paramType', 'monitorParam','', '',  'width:142px,'));
    //    	$("#paramTypeCurr").combobox({
    //    	    valueField:'code',
    //    	    textField:'name'
    //    	});
    //    });


</script>


<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMonitorConfigForm">
            <table>
                <tr>
                    <th>监控名称:</th>
                    <td>
                        <input id="monitorName" name="monitorName" style="width:100px;">
                        </input>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true"
                           onclick="searchMonitorConfigAllFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMonitorConfigFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="monitorConfigDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>

<div id="monitorConfigToolBar" style="display: none;">
    <shiro:hasPermission name="monitor:setting:monitorconfig:operator">
        <a onclick="monitorConfigAddFun();" href="javascript:void(0);" class="easyui-linkbutton"
           data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>