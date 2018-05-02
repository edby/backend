<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var invitationDataGrid;
    $(function () {
        invitationDataGrid = $('#invitationDataGrid').datagrid({
            url: '${ctx}/account/invitation/data',
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
				    width: '140',
				    title: '被邀请人ID',
				    field: 'accountId',
				    sortable: true
				},{
				    width: '180',
				    title: '被邀请人账户名',
				    field: 'accountName',
				    sortable: true
				},{
				    width: '180',
				    title: '邀请人账户名',
				    field: 'invitationAccountName',
				    sortable: true
				},{
				    width: '80',
				    title: '邀请码(UNID)',
				    field: 'invitCode',
				    sortable: true
				}
                    	]],
            columns: [[ 
            	{
                width: '80',
                title: 'BMS数量',
                field: 'bmsNum',
                sortable: true
            },{
                width: '80',
                title: '是否已发放BMS',
                field: 'isGrant',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '80',
                title: 'BMS到帐状态',
                field: 'grantFlag',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            }, {
                width: '150',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }  <shiro:hasPermission name="trade:setting:accountinvitation:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 100,
                    formatter: function (value, row, index) {
                        var str = '';
                        if( row.isGrant == trade_account_invitation_isGrant){
                        	str += $.formatString('<a href="javascript:void(0)" id="status_1" class="invitation-easyui-linkbutton-fafang" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editInvitationFun(\'{0}\',\'1\');" >发放</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:accountinvitation:operator">
                $('.invitation-easyui-linkbutton-fafang').linkbutton({text: '发放'});
                </shiro:hasPermission>
            }
        });
    });

    

    function editInvitationFun(id,status) {
        if (id == undefined) {
            var rows = invitationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            invitationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:accountinvitation:operator">
        
        $.messager.prompt('奖励发放', '请输入BMS数量', function(r){
			if (r){
				if(/^[1-9]\d*|0$/.test(r)){
					var  bmsNum = r;
					parent.$.messager.confirm('询问', '您确定要给改用户发放奖励吗?', function (b) {
			            if (b) {
			                progressLoad();
			                $.post('${ctx}/account/invitation/doGrant', {
			                	id: id,bmsNum:bmsNum
			                }, function (result) {
			                	 progressClose();
			                    if (result.code == ajax_result_success_code) {
			                        parent.$.messager.alert('提示', result.message, 'info');
			                        invitationDataGrid.datagrid('load', $.serializeObject($('#searchInvitationForm')));
			                    }else{
			                    	parent.$.messager.alert('提示', result.message, 'info');
			                    }
		                        $('#csrf-form').find('input[name="csrf"]').val(result.csrf);
		                        setCsrfToken("csrf-form");
			                }, 'JSON');
			            }
			        });
				}else{
					$.messager.alert('提示','发放失败，发放BMS数量必须为数字！','info');
				}
			}else{
				$.messager.alert('提示','发放失败，您未填写发放BMS数量！','info');
			}
		});
        </shiro:hasPermission>
    }
    function searchInvitationFun() {
    	invitationDataGrid.datagrid('load', $.serializeObject($('#searchInvitationForm')));
    }
    function cleanInvitationFun() {
        $('#searchInvitationForm input').val('');
        invitationDataGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchInvitationForm">
            <table>
                <tr>
                    <th>被邀请人账户名:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入被邀请人账户名"/></td>
                    <th>邀请码(UNID):</th>
                    <td><input name="invitCode" class="easyui-textbox" placeholder="请输入邀请码"/></td>
                    <th>邀请人账户名:</th>
                    <td><input name="invitationAccountName" class="easyui-textbox" placeholder="请输入邀请人账户名"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchInvitationFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanInvitationFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="invitationDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
