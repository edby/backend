<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var dictTreeGrid;
    $(function () {
        dictTreeGrid = $('#dictTreeGrid').treegrid({
            url: '${ctx}/common/dict/data',
            idField: 'id',
            treeField: 'name',
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
            columns: [[
                {
                    title: 'PID',
                    field: 'parentId',
                    hidden: true
                }, {
                    title: '名称',
                    field: 'name',
                    width: 220
                }, {
                    title: '编码',
                    field: 'code',
                    width: 220
                }, {
                    title: '语言',
                    field: 'lang',
                    width: 80,
                    formatter: function (value, row, index) {
                        switch (value) {
                            case 'en_US':
                                return 'English';
                            case 'zh_CN':
                                return '简体中文';
                            case 'zh_HK':
                                return '繁体中文';
                        }
                    }
                }, {
                    title: '状态',
                    field: 'active',
                    width: 50,
                    formatter: function (value, row, index) {
                        switch (value) {
                            case true:
                                return '启用';
                            case false:
                                return '停用';
                        }
                    }
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
                }<shiro:hasPermission name="system:setting:dict:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-add" data-options="plain:true,iconCls:\'fi-page-add icon-blue\'" onclick="addDictParentIdFun(\'{0}\');" >添加</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="editDictFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-page-delete icon-red\'" onclick="deleteDictFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="system:setting:dict:operator">
                $('.easyui-linkbutton-add').linkbutton({text: '添加'});
                $('.easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#dictToolbar'
        });
    });

    function addDictFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/common/dict/modify',
            buttons: [{
                text: '添加',
                handler: function () {
                    parent.$.modalDialog.openner_treeGrid = dictTreeGrid;
                    var f = parent.$.modalDialog.handler.find('#editDictForm');
                    f.submit();
                }
            }]
        });
    }

    function addDictParentIdFun(id) {
        if (id != undefined) {
            dictTreeGrid.treegrid('select', id);
        }
        var node = dictTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '添加',
                width: 500,
                height: 300,
                href: '${ctx}/common/dict/modify?parentId=' + node.id,
                buttons: [{
                    text: '添加',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = dictTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editDictForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function editDictFun(id) {
        if (id != undefined) {
            dictTreeGrid.treegrid('select', id);
        }
        var node = dictTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.modalDialog({
                title: '编辑',
                width: 500,
                height: 300,
                href: '${ctx}/common/dict/modify?id=' + node.id,
                buttons: [{
                    text: '编辑',
                    handler: function () {
                        parent.$.modalDialog.openner_treeGrid = dictTreeGrid;
                        var f = parent.$.modalDialog.handler.find('#editDictForm');
                        f.submit();
                    }
                }]
            });
        }
    }

    function deleteDictFun(id) {
        if (id != undefined) {
            dictTreeGrid.treegrid('select', id);
        }
        var node = dictTreeGrid.treegrid('getSelected');
        if (node) {
            parent.$.messager.confirm('询问', '您是否要删除当前资源?', function (b) {
                if (b) {
                    progressLoad();
                    $.post('${ctx}/common/dict/del', {
                        ids: node.id
                    }, function (result) {
                        if (result.code == 200) {
                            parent.$.messager.alert('提示', result.message, 'info');
                            dictTreeGrid.treegrid('reload');
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
        <table id="dictTreeGrid"></table>
    </div>
    <div id="dictToolbar" style="display: none;">
        <shiro:hasPermission name="system:setting:dict:operator">
            <a onclick="addDictFun();" href="javascript:void(0);" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
        </shiro:hasPermission>
    </div>
</div>
