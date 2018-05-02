<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var noticemsgDataGrid;
    $(function () {
        noticemsgDataGrid = $('#noticemsgDataGrid').datagrid({
            url: '${ctx}/account/notice/data',
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
				    title: '语言类型',
				    field: 'langType',
				    formatter: function (value, row, index) {
				    	return getDictValueByCode(value);
				    }
				},  {
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
            },  {
                width: '100',
                title: '状态',
                field: 'status',
                formatter: function (value, row, index) {
                	if(value==true){
                	    return '已发布';
                    }else{
                        return '未发布';
                    }
                }
            }, {
                width: '150',
                title: '发布时间',
                field: 'publicDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }, {
                width: '100',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }, {
                width: '100',
                title: '修改人',
                hidden:true,
                field: 'updateByName'
            }, {
                width: '150',
                title: '修改时间',
                field: 'updateDate',
                hidden:true,
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:notice:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 230,
                    formatter: function (value, row, index) {
                        var str = '';
                        if(row.status == true){//已发布
                        	str += $.formatString('<a href="javascript:void(0)" class="noctic-easyui-linkbutton-p1" data-options="plain:true,iconCls:\'fi-arrow-left icon-blue\'" onclick="updateStatusFun(\'{0}\',\'{1}\');" >未发布</a>', row.id, row.status);
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        }else{//未发布
                        	str += $.formatString('<a href="javascript:void(0)" class="noctic-easyui-linkbutton-p0" data-options="plain:true,iconCls:\'fi-arrow-right icon-blue\'" onclick="updateStatusFun(\'{0}\',\'{1}\');" >已发布</a>', row.id, row.status);
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        }
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editNoticeMsgFun(\'{0}\',\'{1}\');" >编辑</a>', row.id, row.status);
                        str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                        str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteNoticeMsgFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:notice:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                $('.noctic-easyui-linkbutton-p1').linkbutton({text: '未发布'});
                $('.noctic-easyui-linkbutton-p0').linkbutton({text: '发布'});
                </shiro:hasPermission>
            },
            toolbar: '#noticemsgToolbar2'
        });
    });
   
    function addNoticeMsgFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 600,
            height: 400,
            href: '${ctx}/account/notice/modify',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = noticemsgDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editNoticeForm');
                    f.submit();
                }
            }],
        });
    }
    
    function updateStatusFun(id,status){
    	var factStatus=0;
    	var msg = '';
    	if(status=='false'){
    		factStatus = 1;
    		msg = '你确定要发布选择的信息吗？';
    	}
    	if(status=='true'){
    		factStatus = 0;
    		msg = '你确定要取消发布选择的信息吗？';
    	}
    	parent.$.messager.confirm('询问', msg, function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/account/notice/updatestatus', {
                    id: id,status:factStatus
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        noticemsgDataGrid.datagrid('reload');
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

    function editNoticeMsgFun(id,status) {
    	if(status == 'published'){
    		$.messager.alert('提示','您选择的消息或公告已经发布，不能进行编辑!');
    		return ;
    	}
        if (id == undefined) {
            var rows = noticemsgDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            noticemsgDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 600,
            height: 400,
            href: '${ctx}/account/notice/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = noticemsgDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editNoticeForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteNoticeMsgFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = noticemsgDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            noticemsgDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前数据吗？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/account/notice/del', {
                    ids: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        noticemsgDataGrid.datagrid('reload');
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
    
    function searchNoticeMsgFun() {
    	noticemsgDataGrid.datagrid('load', $.serializeObject($('#searchNoticeMsgForm')));
    }
    function cleanNoticeMsgFun() {
        $('#searchNoticeMsgForm input').val('');
        noticemsgDataGrid.datagrid('load', {});
    }
    
    $(function(){
    	$("#noticeTypeTd").html(dictDropDownOptionsList('noticeType','noticeType', 'noticeType','', '',  'width:142px,'));
    	$("#noticeType").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    	$("#langTypeTd").html(dictDropDownOptionsList('langType','langType', 'langType', '', '', ''));
    	$("#langType").combobox({
    	    valueField:'code',
    	    textField:'name'
    	});
    });

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchNoticeMsgForm">
            <table>
                <tr>
                    <th>标题:</th>
                    <td><input name="title" class="easyui-textbox" placeholder="请输入标题"/></td>
                     <th>消息类型:</th>
                    <td id="noticeTypeTd">
					</td>
                    <th>语言类型:</th>
                    <td id="langTypeTd">
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchNoticeMsgFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanNoticeMsgFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
     	<table id="noticemsgDataGrid"  data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="noticemsgToolbar2" style="display: none;">
    <shiro:hasPermission name="trade:setting:notice:operator">
    <a onclick="addNoticeMsgFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>
