<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var sysParameterDataGrid;
    $(function () {
        sysParameterDataGrid = $('#sysParameterDataGrid').datagrid({
            url: '${ctx}/system/params/data',
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
				    title: '主键',
				    field: 'id',
				    hidden: true
				}, {
				    width: '150',
				    title: '系统名称',
				    field: 'systemName',
				    sortable: true
				}, {
				    width: '250',
				    title: '参数名称',
				    field: 'parameterName',
				    sortable: true
				}
                            ]],
            columns: [[{
                width: '300',
                title: '参数描述',
                field: 'describe'
            }, {
                width: '100',
                title: '参数大类',
                field: 'division'
            }, {
                width: '100',
                title: '参数类型',
                field: 'type'
            }, {
                width: '150',
                title: '参数值值域',
                field: 'valueBound'
            }, {
                width: '100',
                title: '参数值',
                field: 'value'
            }, {
                width: '300',
                title: '备注',
                field: 'remark'
            }, {
                width: '70',
                title: '创建人',
                field: 'createByName'
            }, {
                width: '130',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            },{
                    width: '100',
                    title: '修改人',
                    field: 'updateByName',
                    hidden:true
            }, {
                    width: '150',
                    title: '修改时间',
                    field: 'updateDate',
                    hidden:true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
            }<shiro:hasPermission name="system:setting:sysparameter:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 90,
                    formatter: function (value, row, index) {
                        var str =  $.formatString('<a href="javascript:void(0)" class="sysparameter-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-green\'" onclick="editSysParameterFun(\'{0}\');" >编辑</a>', row.id);
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
                <shiro:hasPermission name="system:setting:sysparameter:operator">
                $('.sysparameter-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                </shiro:hasPermission>
            },
            toolbar: '#icoSubscribeToolbar'
        });
    });

    function editSysParameterFun(id) {
        if (id == undefined) {
            var rows = sysParameterDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            sysParameterDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title: '编辑',
            width: 500,
            height: 300,
            href: '${ctx}/system/params/modify?id=' + id,
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = sysParameterDataGrid;
                    var f = parent.$.modalDialog.handler.find('#editSysParameterForm');
                    f.submit();
                }
            }]
        });
    }

    
    function searchSysParameterFun() {
    	sysParameterDataGrid.datagrid('load', $.serializeObject($('#searchSysParameterForm')));
    }
    function cleanSysParameterFun() {
        $('#searchSysParameterForm input').val('');
        sysParameterDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchSysParameterForm">
            <table>
                <tr>
                    <th>系统名称:</th>
                    <td><input name="systemName" class="easyui-textbox"  style="width:100px;" placeholder="请输入系统名称"/></td>
                    <th>参数名称:</th>
                    <td><input name="parameterName" class="easyui-textbox"  style="width:100px;" placeholder="请输入参数名称"/></td>
                    <th>参数大类:</th>
                    <td><input name="division" class="easyui-textbox"  style="width:100px;" placeholder="请输入参数大类"/></td>
                    <th>参数类型:</th>
                    <td><input name="type" class="easyui-textbox"  style="width:100px;" placeholder="请输入参数类型"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchSysParameterFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanSysParameterFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:false">
        <table id="sysParameterDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="icoSubscribeToolbar" style="display: none;">
</div>
