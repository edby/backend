<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var bankRechargeAppGrid;
    $(function () {
        bankRechargeAppGrid = $('#bankRechargeAppGrid').datagrid({
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
                }<shiro:hasPermission name="trade:setting:moneychargeapprove:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        if (row.status == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>') {
                            str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editRechargeAppFun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>\',\'<%=FundConsts.WALLET_BTC2USD_TYPE%>\');" >审批</a>', row.id);
                        } else {
                            str += $.formatString('<a href="javascript:void(0)" class="accountfundcurrent-easyui-linkbutton-print" data-options="plain:true,iconCls:\'fi-page-export icon-red\'" onclick="window.open(\'${ctx}/fund/bankRecharge/approvalPrint?id={0}&exchangePairMoney={1}\')" target="_blank">打印</a>', row.id, '<%=FundConsts.WALLET_BTC2USD_TYPE%>');
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
                <shiro:hasPermission name="trade:setting:moneychargeapprove:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                $('.accountfundcurrent-easyui-linkbutton-print').linkbutton({text: '打印'});
                </shiro:hasPermission>
            }
        });
    });

    function editRechargeAppFun(id, status, exchangePairMoney) {
        if (id == undefined) {
            var rows = bankRechargeAppGrid.datagrid('getSelections');
            id = rows[0].accountFundCurrentName;
        } else {
            bankRechargeAppGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:moneychargeapprove:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/fund/bankRecharge/approval?id=' + id + "&exchangePairMoney=" + exchangePairMoney,
            buttons: [{
                text: '通过',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = bankRechargeAppGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editRechargeAppForm');
                    parent.$.modalDialog.handler.find('#editRechargeAppForm #approveStatus').val(status);
                    $("#editRechargeAppForm").form("enableValidation");
                    f.submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    parent.$.modalDialog.handler.dialog('close');
                }
            }]
        });
        </shiro:hasPermission>
    }
    
    function searchBankRechargeAppFun() {
        bankRechargeAppGrid.datagrid('load', $.serializeObject($('#searchBankRechargeAppForm')));
    }
    function cleanBankRechargeAppFun() {
        $('#searchBankRechargeAppForm input').val('');
        bankRechargeAppGrid.datagrid('load', {});
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBankRechargeAppForm">
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
                    <th>状态</th>
                    <td><select class="easyui-combobox" name="status" style="width:200px;">
                        <option value="">-请选择-</option>
                        <option value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>">待审核</option>
                        <option value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH%>">已审核</option>
                    </select> </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchBankRechargeAppFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBankRechargeAppFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="bankRechargeAppGrid" data-options="fit:true,border:false"></table>
    </div>
</div>