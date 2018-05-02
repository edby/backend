<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts"%>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountCertificationDataGrid;
    $(function () {
    	accountCertificationDataGrid = $('#accountCertificationDataGrid').datagrid({
            url: '${ctx}/account/cert/data',
            fit: true,
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns:[[
			{
			    width: '200',
			    title: '账户名',
			    field: 'accountName',
			    sortable: true
			}, {
			    width: '150',
			    title: '姓氏',
			    field: 'surname',
			    sortable: true
			}, {
			    width: '150',
			    title: '名字',
			    field: 'realname',
			    sortable: true
			}, {
			    width: '100',
			    title: '区域代码',
			    field: 'regionId',
			    sortable: true,
			    hidden:true
			}		
            ]],
            columns: [[ {
                width: '250',
                title: '护照编号',
                field: 'passportId',
                sortable: true
            },{
                width: '70',
                title: '状态',
                field: 'status',
                formatter: function (value, row, index) {
                    value=value*1;
                	if(value==1)
                    {
                        return '审核通过';
                    }
                    else if(value==2)
                    {
                        return '审核未通过';
                    }else{
                        return '待审核';
                    }
                }
            }<shiro:hasPermission name="trade:setting:cert:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 170,
                    formatter: function (value, row, index) {
                        var str = '';
                        if(row.status*1==0)
                        {
                            str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editAccountCertificationFun(\'{0}\');" >审批</a>', row.id);
                        }else{
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="" data-options="plain:true,iconCls:\'fi-page-edit icon-blue\'" onclick="detailsAccountCertificationFun(\'{0}\');" >详情</a>', row.id);
                        }
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
                <shiro:hasPermission name="trade:setting:cert:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                </shiro:hasPermission>
            }
        });
        searchAccountCertificationFun();
    });

    function editAccountCertificationFun(id) {
        if (id == undefined) {
            var rows = accountCertificationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            accountCertificationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:cert:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/account/cert/approval?id='+id,
            buttons: [{
                text: '通过',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = accountCertificationDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editCertApprovalForm');
                    parent.$.modalDialog.handler.find('#editCertApprovalForm #status').val(1);
                    f.submit();
                }
            },{
            text: '拒绝',
                handler: function () {
                parent.$.modalDialog.openner_dataGrid = accountCertificationDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editCertApprovalForm');
                    parent.$.modalDialog.handler.find('#editCertApprovalForm #status').val(2);
                f.submit();
            }
        }]
        });
        </shiro:hasPermission>
    }
    function detailsAccountCertificationFun(id) {
        if (id == undefined) {
            var rows = accountCertificationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            accountCertificationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:cert:operator">
        parent.$.modalDialog({
            title: '详情',
            width: 1000,
            height: 500,
            href: '${ctx}/account/cert/details?id='+id
        });
        </shiro:hasPermission>
    }
    function searchAccountCertificationFun() {
    	accountCertificationDataGrid.datagrid('load', $.serializeObject($('#searchAccountCertificationForm')));
    }
    function cleanAccountCertificationFun() {
        $('#searchAccountCertificationForm input').val('');
        accountCertificationDataGrid.datagrid('load',{} );
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchAccountCertificationForm">
            <table>
                <tr>
                    <th>账户名:</th>
                    <td><input id="approve_accountName" class="easyui-textbox" name="accountName" placeholder="请输入账户名"/></td>
                    <th>姓氏:</th>
                    <td><input id="surname" class="easyui-textbox" name="surname" placeholder="请输入姓氏"/></td>
                    <th>名字:</th>
                    <td><input id="realname" class="easyui-textbox" name="realname" placeholder="请输入名字"/></td>
                    <th>审批状态:</th>
                    <td>
                        <select id="status" class="easyui-combobox" name="status" style="width:100px;"  data-options="required:true">
                            <option value="">-请选择-</option>
                            <option value="0" selected>待审核</option>
                            <option value="2">审核未通过</option>
                            <option value="1">审核通过</option>
                        </select>
					</td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountCertificationFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountCertificationFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="accountCertificationDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
