<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var accountFundCurrentDataGrid;
    $(function () {
        accountFundCurrentDataGrid = $('#accountFundCurrentDataGrid').datagrid({
            url: '${ctx}/fund/account/approvalData',
            fit: true,
            queryParams: {
                accountAssetType: account_fund_current_accountAssetType,
                businessFlag: account_fund_current_businessFlag,
                relatedStockinfoId:<%=FundConsts.WALLET_BTC_TYPE%>
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            pageSize: 100,
            pageList: [10, 20, 30, 40, 50, 100],
            frozenColumns: [[]],
            columns: [[
                {
                    width: '60',
                    title: 'UID',
                    field: 'unid',
                    sortable: true
                }, {
                    width: '150',
                    title: '账户名',
                    field: 'accountName',
                    sortable: true
                }, {
                    width: '130',
                    title: '证券ID',
                    field: 'stockinfoId',
                    hidden: true,
                    sortable: true
                }, {
                    width: '40',
                    title: '代码',
                    field: 'stockCode',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券名称',
                    field: 'stockName',
                    sortable: true,
                    hidden: true

                }, {
                    width: '140',
                    title: '提币申请时间',
                    field: 'currentDate',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }, {
                    width: '70',
                    title: '业务类别',
                    field: 'businessFlag',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '150',
                    title: '原资产数量余额',
                    field: 'orgAmt',
                    hidden: true,
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '80',
                    title: '资产发生方向',
                    field: 'occurDirect',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '150',
                    title: '提现数量',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return (value - row.netFee).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '用户网络手续费',
                    field: 'netFee',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '实际网络手续费',
                    field: 'realTransferFee',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '150',
                    title: '最新资产数量余额',
                    field: 'lastAmt',
                    hidden: true,
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '270',
                    title: '提币目标地址',
                    field: 'withdrawAddr',
                    sortable: true
                }, {
                    width: '460',
                    title: '交易ID',
                    field: 'transId'
                }, {
                    width: '250',
                    title: '审批ID',
                    field: 'pendingApproval',
                    hidden:true
                }, {
                    width: '70',
                    title: '审批状态',
                    field: 'approveStatus',
                    formatter: function (value, row, index) {
                        var color = "";
                        if (value == 'checkThrough') {
                            color = 'green';
                        }
                        else if (value == 'auditPending' || value == 'checkPending') {
                            color = 'orange';
                        }
                        else if (value == 'auditReject' || value == 'checkReject') {
                            color = 'red';
                        }
                        return "<font color='" + color + "'>" + getDictValueByCode(value) + "</font>";
                    }
                }, {
                    width: '70',
                    title: '汇签状态',
                    field: 'transferStatus',
                    formatter: function (value, row, index) {
                        var color = "";
                        if (value == 'transfer') {
                            color = 'green';
                        }
                        else if (value == 'transferPending') {
                            color = 'orange';
                        }
                        else if (value == 'transferRejected') {
                            color = 'red';
                        }
                        return "<font color='" + color + "'>" + getDictValueByCode(value) + "</font>";
                    }
                }, {
                    width: '100',
                    title: '终签确认状态',
                    field: 'confirmStatus',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '100',
                    title: '备注',
                    hidden: true,
                    field: 'remark',
                    sortable: true
                }, {
                    width: '90',
                    title: '创建时间',
                    field: 'createDate',
                    hidden: true,
                    formatter: function (value, row, index) {
                        return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                    }
                }<shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 70,
                    formatter: function (value, row, index) {
                        var str = '';
                        if (row.approveStatus == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>') {
                            str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editAccountFundCurrentFun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>\',\'<%=FundConsts.WALLET_BTC2USD_TYPE%>\');" >审批</a>', row.id);
                        } else {
                            if(row.transferStatus=='unTransfer' && (row.pendingApproval!='' && row.pendingApproval!=null))
                            {
                                str += $.formatString('<a href="javascript:void(0)" class="accountfundcurrent-easyui-linkbutton-print" data-options="plain:true,iconCls:\'fi-page-export icon-red\'" onclick="window.open(\'${ctx}/fund/account/approvalPrint?id={0}&exchangePairMoney={1}\')" target="_blank">打印</a>', row.id, '<%=FundConsts.WALLET_BTC2USD_TYPE%>');
                            }
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
                <shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                $('.accountfundcurrent-easyui-linkbutton-jiedong1').linkbutton({text: '审核拒绝'});
                $('.accountfundcurrent-easyui-linkbutton-print').linkbutton({text: '打印'});
                </shiro:hasPermission>
            }
        });
    });


    function editAccountFundCurrentFun(id, status, exchangePairMoney) {
        if (id == undefined) {
            var rows = accountFundCurrentDataGrid.datagrid('getSelections');
            id = rows[0].accountFundCurrentName;
        } else {
            accountFundCurrentDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:accountfundcurrent:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/fund/account/approval?id=' + id + "&exchangePairMoney=" + exchangePairMoney,
            buttons: [{
                text: '通过',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = accountFundCurrentDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editApprovalForm');
                    parent.$.modalDialog.handler.find('#editApprovalForm #approveStatus').val(status);
                    $("#editApprovalForm").form("enableValidation");
                    f.submit();
                }
            }, {
                text: '拒绝',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = accountFundCurrentDataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editApprovalForm');
                    parent.$.modalDialog.handler.find('#editApprovalForm #approveStatus').val('<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT %>');
                    $("#editApprovalForm").form("disableValidation");
                    f.submit();
                }
            }]
        });
        </shiro:hasPermission>
    }
    function searchAccountFundCurrentFun() {
        accountFundCurrentDataGrid.datagrid('load', $.serializeObject($('#searchAccountFundCurrentForm')));
    }
    function cleanAccountFundCurrentFun() {
        //$('#searchAccountFundCurrentForm input').val('');
        $(':input', '#searchAccountFundCurrentForm')
            .not(':button, :submit, :reset, :hidden')
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
        accountFundCurrentDataGrid.datagrid('load', $.serializeObject($('#searchAccountFundCurrentForm')));
    }
    $(function () {

        $("#approve_approveStatusTd").html(dictDropDownOptionsList('approve_approveStatus', 'approveStatus', 'approveStatus', '', '', 'width:142px,'));
        $("#approve_approveStatus").combobox({
            valueField: 'code',
            textField: 'name'
        });
        $("#allot_approveStatusTd").html(dictDropDownOptionsList('allot_approveStatus', 'transferStatus', 'transferStatus', '', '', 'width:142px,'));
        $("#allot_approveStatus").combobox({
            valueField: 'code',
            textField: 'name'
        });


    });
    $(document).ready(function () {
        var app_timer;
        try {
            app_timer = window.setInterval(searchAccountFundCurrentFun, 60000);
        } catch (err) {
            window.clearInterval(bzj_timer);
        }

    });
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="overflow: hidden;">
        <form id="searchAccountFundCurrentForm">
            <input type="hidden" name="accountAssetType" value="<%=FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET %>"/>
            <input type="hidden" name="businessFlag" value="<%=FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW %>"/>
            <input type="hidden" name="relatedStockinfoId" value="<%=FundConsts.WALLET_BTC_TYPE %>"/>
            <table id="AutoTable">
                <tr>
                    <th>UID:</th>
                    <td><input id="unid" class="easyui-textbox" name="unid" style="width: 75px" placeholder="请输入账户ID"/></td>
                    <th>账户名:</th>
                    <td><input id="approve_accountName" class="easyui-textbox" style="width: 75px" name="accountName" placeholder="请输入账户名"/></td>
                    <th>证券代码:</th>
                    <td>
                        <input id="approve_stockCode" name="stockinfoId" style="width: 75px" class="easyui-combobox" name="language"
                               placeholder="请选择证券" data-options="
                                    url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_DIGITALCOIN%>', method: 'get', valueField:'id',
                                    textField:'stockCode', groupField:'group'">
                    </td>
                    <th>审批状态:</th>
                    <td id="approve_approveStatusTd"></td>
                    <th >划拨状态:</th>
                    <td id="allot_approveStatusTd"></td>
                    <th>审批ID:</th>
                    <td><input id="pendingApproval" style="width: 75px" class="easyui-textbox" name="pendingApproval" placeholder="请输入审批ID"/></td>
                    <th>交易ID:</th>
                    <td><input id="transId" style="width: 75px" class="easyui-textbox" name="transId" placeholder="请输入交易ID"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchAccountFundCurrentFun();">查询</a>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanAccountFundCurrentFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="accountFundCurrentDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
