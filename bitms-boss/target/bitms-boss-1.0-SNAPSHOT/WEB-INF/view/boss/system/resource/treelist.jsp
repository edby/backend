<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var resTreeGrid;
    $(function () {
        resTreeGrid = $('#resTreeGrid').treegrid({
            url: '${ctx}/system/resource/data',
            idField: 'id',
            treeField: 'resName',
            parentField: 'parentId',
            fit: true,
            fitColumns: false,
            border: false,
            frozenColumns: [[{
                title: '主键',
                field: 'id',
                width: 40,
                hidden: true
            }]],
            columns: [[
                {
                    title: '上级编号',
                    field: 'parentId',
                    width: 100,
                    hidden: true
                }, {
                    title: '名称',
                    field: 'resName',
                    width: 180
                }, {
                    title: '编码',
                    field: 'resCode',
                    width: 150
                }, {
                    title: '类型',
                    field: 'type',
                    width: 50,
                    formatter: function (value) {
                        if (value == 1) {
                            return '权限';
                        } else {
                            return '菜单';
                        }
                    }
                }, {
                    title: '图标',
                    field: 'iconCls',
                    width: 100
                }, {
                    title: '地址',
                    field: 'resUrl',
                    width: 200
                }, {
                    title: '创建人',
                    field: 'createByName',
                    width: 100
                }, {
                    width: '130',
                    title: '创建时间',
                    field: 'createDate',
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }<shiro:hasPermission name="system:setting:resource:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-add" data-options="plain:true,iconCls:\'fi-page-add icon-blue\'" onclick="addResParentIdFun(\'{0}\');" >添加</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="editResFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-page-delete icon-red\'" onclick="deleteResFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission  name="system:setting:resource:operator">
                $('.easyui-linkbutton-add').linkbutton({text: '添加'});
                $('.easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#resToolbar'
        });
    });

    function addResFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 400,
            href: '${ctx}/system/resource/modify',
            buttons: [{
                text: '添加',
                handler: function () {
                    parent.$.modalDialog.openner_treeGrid = resTreeGrid;
                    var f = parent.$.modalDialog.handler.find('#editResForm');
                    f.submit();
                }
            }]
        });
    }

    function addResParentIdFun(id) {
        if (id != undefined) {
            resTreeGrid.treegrid('select', id);
        }
        var node = resTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '添加',
                width: 500,
                height: 400,
                href: '${ctx}/system/resource/modify?parentId=' + node.id,
                buttons: [{
                    text: '添加',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = resTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editResForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function editResFun(id) {
        if (id != undefined) {
            resTreeGrid.treegrid('select', id);
        }
        var node = resTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '编辑',
                width: 500,
                height: 400,
                href: '${ctx}/system/resource/modify?id=' + node.id,
                buttons: [{
                    text: '编辑',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = resTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editResForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function deleteResFun(id) {
        if (id != undefined) {
            resTreeGrid.treegrid('select', id);
        }
        var node = resTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.messager.confirm('询问', '您是否要删除当前资源?', function (b) {
                if (b) {
                    progressLoad();
                    $.post('${ctx}/system/resource/del', {
                        ids: node.id
                    }, function (result) {
                        if (result.code == 200) {
                            parent.$.messager.alert('提示', result.message, 'info');
                            resTreeGrid.treegrid('reload');
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
        <table id="resTreeGrid"></table>
    </div>
    <div id="resToolbar" style="display: none;">
        <shiro:hasPermission name="system:setting:resource:operator">
            <a onclick="addResFun();" href="javascript:void(0);" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
        </shiro:hasPermission>
    </div>
</div>
