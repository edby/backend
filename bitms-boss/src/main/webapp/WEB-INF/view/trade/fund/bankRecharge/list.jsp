<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var bankRechargeInputGrid;
    $(function () {
        bankRechargeInputGrid = $('#bankRechargeInputGrid').datagrid({
            url: '${ctx}/fund/bankRecharge/data',
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
				    width: '140',
				    title: '证券ID',
				    field: 'id',
                    hidden:true
				}, {
				    width: '100',
				    title: '证券代码',
				    field: 'stockCode',
				    sortable: true
				}	
               ]],
            columns: [[
                {
                    width: '100',
                    title: 'UID',
                    field: 'unid',
                    sortable: true
                },{
                    width: '100',
                    title: '充值账户',
                    field: 'accountName',
                    sortable: true
                },{
                    width: '150',
                    title: '充值金额',
                    field: 'amount',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '手续费',
                    field: 'fee',
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '100',
                    title: '交易ID',
                    field: 'transId',
                    sortable: true
                },{
                    width: '100',
                    title: '备注',
                    field: 'remark',
                    sortable: true
                },{
                width: '70',
                title: '审批状态',
                field: 'status',
                formatter: function (value, row, index) {
                	return getDictValueByCode(value);
                }
            },{
                width: '120',
                title: '创建时间',
                field: 'createDate',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
            	}
            }<shiro:hasPermission name="trade:setting:moneychargeinput:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        if(row.status == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>')
                        {
                            str += $.formatString('<a href="javascript:void(0)" class="role-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteBankRechargeInputFun(\'{0}\');" >删除</a>', row.id);
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
                <shiro:hasPermission name="trade:setting:moneychargeinput:operator">
                $('.role-easyui-linkbutton-edit').linkbutton({text: '编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text: '删除'});
                </shiro:hasPermission>
            },
            toolbar: '#moneychargeinputToolbar' 
        });
    });

    function addBankRechargeInputFun() {
        parent.$.modalDialog({
            title: '添加',
            width: 400,
            height: 350,
            href: '${ctx}/fund/bankRecharge/edit',
            buttons: [{
                text: '确定',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = bankRechargeInputGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editBankRechargeInputForm');
                    f.submit();
                }
            }]
        });
    }

    function deleteBankRechargeInputFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = bankRechargeInputGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            bankRechargeInputGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前记录？', function (b) {
            if (b) {
                progressLoad();
                $.post('${ctx}/fund/bankRecharge/del', {
                    id: id
                }, function (result) {
                    if (result.code == ajax_result_success_code) {
                        parent.$.messager.alert('提示', result.message, 'info');
                        bankRechargeInputGrid.datagrid('reload');
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
    
    function searchBankRechargeInputFun() {
    	bankRechargeInputGrid.datagrid('load', $.serializeObject($('#searchBankRechargeInputForm')));
    }
    function cleanBankRechargeInputFun() {
        $('#searchBankRechargeInputForm input').val('');
        bankRechargeInputGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBankRechargeInputForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td>
                        <input name="stockinfoId" style="width: 75px" class="easyui-combobox"
                               placeholder="请选择证券" data-options="
                                    url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_CASHCOIN%>', method: 'get', valueField:'id',
                                    textField:'stockCode', groupField:'group'">
                    </td>
                    <th>用户名或UID:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入用户名或UID"/></td>
                    <th>交易ID:</th>
                    <td><input name="transId" class="easyui-textbox" placeholder="请输入交易ID"/></td>
                    <th>备注:</th>
                    <td><input name="remark" class="easyui-textbox" placeholder="请输入备注"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchBankRechargeInputFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBankRechargeInputFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div data-options="region:'center',border:true">
        <table id="bankRechargeInputGrid" data-options="fit:true,border:false"></table>
    </div>
</div>	
<div id="moneychargeinputToolbar" style="display: none;">
    <shiro:hasPermission name="trade:setting:moneychargeinput:operator">
        <a onclick="addBankRechargeInputFun();" href="javascript:void(0);" class="easyui-linkbutton"
       data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>