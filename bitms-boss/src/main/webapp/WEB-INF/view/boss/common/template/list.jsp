<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var msgTemplateDataGrid;
    $(function () {
        msgTemplateDataGrid = $('#msgTemplateDataGrid').datagrid({
            url: '${ctx}/common/msgTemplate/data',
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
            frozenColumns:[[
				{
				    width: '100',
				    title: '模板类型',
				    field: 'type',
				    formatter: function (value, row, index) {
				    	return getDictValueByCode(value);
				    }
				},{
				    width: '100',
				    title: '语言类型',
				    field: 'lang',
				    formatter: function (value, row, index) {
				    	return getDictValueByCode(value);
				    }
				},  {
				    width: '200',
				    title: '模版KEY',
				    field: 'key',
				    sortable: true
				}   ,  {
				    width: '300',
				    title: '标题',
				    field: 'title',
				    sortable: true
				}    		
             ]],
            columns: [[{
                width: '100',
                title: '主键',
                field: 'id',
                hidden: true
            }, {
                width: '100',
                title: '创建人',
                field: 'createName'
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="system:setting:msgtemplate:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 230,
                    formatter: function (value, row, index) {
                      /*  var str = '';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editMsgTemplateFun(\'{0}\',\'{1}\');" >编辑</a>', row.id, row.status);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteMsgTemplateFun(\'{0}\');" >删除</a>', row.id);
                        return str;*/
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
                <shiro:hasPermission name="system:setting:msgtemplate:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#msgTemplateToolbar'
        });
        dictDropDownOptions('templatetype','msgTemplateType', '');
    });
    function addMsgTemplateFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 600,
            height: 480,
            href: '${ctx}/common/msgTemplate/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = msgTemplateDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editMsgTemplateForm');
                    f.submit();
                }
            }],
        });
    }
    function editMsgTemplateFun(id,status) {
        if (id == undefined) {
            var rows = msgTemplateDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            msgTemplateDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 600,
            height: 480,
            href: '${ctx}/common/msgTemplate/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = msgTemplateDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editMsgTemplateForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteMsgTemplateFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = msgTemplateDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            msgTemplateDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前数据吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/common/msgTemplate/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        msgTemplateDataGrid.datagrid('reload');
                    }else{
                        parent.$.messager.alert('错误', result.message, 'error');
                    }
                    $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
                    setCsrfToken("csrf-form");
                    progressClose();
                }, 'JSON');
            }
        });
    }
    
    function searchMsgTemplateFun() {
    	msgTemplateDataGrid.datagrid('load', $.serializeObject($('#searchMsgTemplateForm')));
    }
    function cleanMsgTemplateFun() {
        $('#searchMsgTemplateForm input').val('');
        msgTemplateDataGrid.datagrid('load', {});
    }
    $(function(){
    	$("#typeTd").html(dictDropDownOptionsList('type','type', 'msgTemplateType','', '',  'width:142px,'));
    	$("#type").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#langTd").html(dictDropDownOptionsList('lang','lang', 'langType', '', '', ''));
    	$("#lang").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchMsgTemplateForm">
            <table>
                <tr>
                	<th>模板KEY:</th>
                    <td><input name="key" class="easyui-textbox" placeholder="请输入模板KEY"/></td>
                    <th>标题:</th>
                    <td><input name="title" class="easyui-textbox" placeholder="请输入标题"/></td>
                     <th>模板类型:</th>
                    <td id="typeTd">
					</td>
                    <th>语言类型:</th>
                    <td id="langTd">
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchMsgTemplateFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanMsgTemplateFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
     	<table id="msgTemplateDataGrid"  data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="msgTemplateToolbar" style="display: none;">
    <shiro:hasPermission name="system:setting:msgtemplate:operator">
    <%--<a onclick="addMsgTemplateFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>--%>
    </shiro:hasPermission>
</div>