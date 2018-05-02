<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var regionDataGrid;
    $(function () {
        regionDataGrid = $('#regionDataGrid').datagrid({
            url: '${ctx}/common/region/data',
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
				{
				    width: '100',
				    title: 'ID',
				    field: 'id',
				    hidden:true
				},{
				    width: '100',
				    title: '国际简码',
				    field: 'sCode'
				}, {
				    width: '100',
				    title: '国际代码',
				    field: 'lCode',
				    sortable: true
				}, {
				    width: '200',
				    title: '英文名称',
				    field: 'enName',
				    sortable: true
				}, {
				    width: '200',
				    title: '中文名称',
				    field: 'cnName',
				    sortable: true
				}, {
				    width: '200',
				    title: '区域(洲)',
				    field: 'area',
				    sortable: true
				}, {
				    width: '100',
				    title: '排序',
				    field: 'area',
				    sortable: true
				}		
						
				
				
               ]],
            columns: [[ <shiro:hasPermission name="system:setting:region:operator">
                 {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editRegionFun(\'{0}\');" >编辑</a>', row.id);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteRegionFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="system:setting:region:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#regionToolbar' 
        });
    });

    function addRegionFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 500,
            height: 300,
            href: '${ctx}/common/region/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = regionDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editRegionForm');
                    f.submit();
                }
            }]
        });
    }

    function editRegionFun(id) {
        if (id == undefined) {
            var rows = regionDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            regionDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 300,
            href: '${ctx}/common/region/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = regionDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editRegionForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteRegionFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = regionDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            regionDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前区域代码吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/common/region/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        regionDataGrid.datagrid('reload');
                    }else {
                        parent.$.messager.alert('提示', result.message, 'info');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
    
    function searchRegionFun() {
    	regionDataGrid.datagrid('load', $.serializeObject($('#searchRegionForm')));
    }
    function cleanRegionFun() {
        $('#searchRegionForm input').val('');
        regionDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchRegionForm">
            <table>
                <tr>
                    <th>英文名称:</th>
                    <td><input name="enName" class="easyui-textbox" placeholder="请输入英文名称"/></td>
                    <th>中文名称:</th>
                    <td><input name="cnName" class="easyui-textbox" placeholder="请输入中文名称"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchRegionFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanRegionFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="regionDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="regionToolbar" style="display: none;">
    <shiro:hasPermission name="system:setting:region:operator">
    <a onclick="addRegionFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
