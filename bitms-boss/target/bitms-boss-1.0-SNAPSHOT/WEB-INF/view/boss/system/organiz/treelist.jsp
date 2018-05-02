<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var organizTreeGrid;
    $(function () {
        organizTreeGrid = $('#organizTreeGrid').treegrid({
            url: '${ctx}/system/organiz/data',
            idField: 'id',
            treeField: 'orgName',
            parentField: 'parentId',
            fit: true,
            fitColumns: false,
            border: false,
            frozenColumns: [[{
                title: 'ID',
                field: 'id',
                width: 40,
                hidden: true
            }]],
            columns: [[{
                title: '机构编号',
                field: 'orgCode',
                width: 150,
                hidden: true
            }, {
                title: 'PID',
                field: 'parentId',
                width: 100,
                hidden: true
            }, {
                title: '机构名称',
                field: 'orgName',
                width: 180
            }, {
                title: '机构描述',
                field: 'orgDest',
                width: 240
            }, {
                title: '创建人',
                field: 'createByName',
                width: 120
            }, {
                width: '130',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                }
            }<shiro:hasPermission name="system:setting:organiz:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-add" data-options="plain:true,iconCls:\'fi-page-add icon-blue\'" onclick="addOrgParentIdFun(\'{0}\');" >添加</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="editOrgFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-page-delete icon-red\'" onclick="deleteOrgFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="system:setting:organiz:operator">
                $('.easyui-linkbutton-add').linkbutton({text: '添加'});
                $('.easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#orgToolbar'
        });
    });

    function addOrgFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/system/organiz/modify',
            buttons: [{
                text: '添加',
                handler: function () {
                    parent.$.modalDialog.openner_treeGrid = organizTreeGrid;
                    var f = parent.$.modalDialog.handler.find('#editOrgForm');
                    f.submit();
                }
            }]
        });
    }

    function addOrgParentIdFun(id) {
        if (id != undefined) {
            organizTreeGrid.treegrid('select', id);
        }
        var node = organizTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '添加',
                width: 500,
                height: 300,
                href: '${ctx}/system/organiz/modify?parentId=' + node.id,
                buttons: [{
                    text: '添加',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = organizTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editOrgForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function editOrgFun(id) {
        if (id != undefined) {
            organizTreeGrid.treegrid('select', id);
        }
        var node = organizTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '编辑',
                width: 500,
                height: 300,
                href: '${ctx}/system/organiz/modify?id=' + node.id,
                buttons: [{
                    text: '编辑',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = organizTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editOrgForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function deleteOrgFun(id) {
        if (id != undefined) {
            organizTreeGrid.treegrid('select', id);
        }
        var node = organizTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.messager.confirm('询问', '您是否要删除当前资源?', function (b) {
                if (b) {
                    progressLoad();
                    $.post('${ctx}/system/organiz/del', {
                        ids: node.id
                    }, function (result) {
                        if (result.code == 200) {
                            parent.$.messager.alert('提示', result.message, 'info');
                            organizTreeGrid.treegrid('reload');
                        } else {
                            parent.$.messager.alert('提示', result.message, 'info');
                        }
                        $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                        setCsrfToken("csrf-form");
                        progressClose();
                    }, 'JSON');
                }
            });
        }
    }


</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="overflow: hidden;">
        <table id="organizTreeGrid"></table>
    </div>
    <div id="orgToolbar" style="display: none;">
        <shiro:hasPermission name="system:setting:organiz:operator">
            <a onclick="addOrgFun();" href="javascript:void(0);" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
        </shiro:hasPermission>
    </div>
</div>
