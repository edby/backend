<%@ page import="com.blocain.bitms.trade.fund.consts.FundConsts" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/setup_ajax.jsp" %>
<script type="text/javascript">
    var cashWithdrawAppGrid;
    $(function () {
        cashWithdrawAppGrid = $('#cashWithdrawAppGrid').datagrid({
            url: '${ctx}/fund/cashwithdraw/approvalData',
            queryParams: {
                accountAssetType: account_fund_current_accountAssetType,
                businessFlag: account_fund_current_businessFlag,
                stockinfoId:<%=FundConsts.WALLET_EUR_TYPE%>,
                approveStatus:'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>'
            },
            striped: true,
            rownumbers: true,
            pagination: true,
            singleSelect: true,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50, 100],
            columns: [[
                {
                    width: '130',
                    title: 'ID',
                    field: 'id',
                    sortable: true
                }, {
                    width: '130',
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
                    title: '证券代码',
                    field: 'stockCode',
                    sortable: true
                }, {
                    width: '100',
                    title: '证券名称',
                    field: 'stockName',
                    sortable: true,
                    hidden: true

                }, {
                    width: '120',
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
                    width: '140',
                    title: '提现数量',
                    field: 'occurAmt',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return (value - row.fee).toFixed(12);
                    }
                }, {
                    width: '140',
                    title: '手续费',
                    field: 'fee',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                },{
                    width: '150',
                    title: '最新资产数量余额',
                    field: 'lastAmt',
                    hidden: true,
                    sortable: true,
                    formatter: function (value, row, index) {
                        return parseFloat(value).toFixed(12);
                    }
                }, {
                    width: '170',
                    title: '提币目标银行名称',
                    field: 'withdrawBankName',
                    hidden: true,
                    sortable: true
                }, {
                    width: '150',
                    title: '提币目标卡号',
                    field: 'withdrawCardNo',
                    sortable: true
                }, {
                    width: '80',
                    title: 'SWIFT/BTC',
                    field: 'swiftBic',
                    sortable: true
                }, {
                    width: '280',
                    title: '交易ID',
                    field: 'transId'
                }, {
                    width: '160',
                    title: '备注',
                    field: 'remark'
                },{
                    width: '70',
                    title: '审批状态',
                    field: 'approveStatus',
                    formatter: function (value, row, index) {
                        return getDictValueByCode(value);
                    }
                }, {
                    width: '70',
                    title: '汇款状态',
                    field: 'transferStatus',
                    formatter: function (value, row, index) {
                        var color = "";
                        var values= getDictValueByCode(value);
                        if (value == 'transfer') {
                            color = 'green';
                            values='成功汇出';
                        }
                        else if (value == 'transferPending') {
                            color = 'orange';
                            values='等待汇出';
                        }
                        else if (value == 'transferRejected') {
                            color = 'red';
                            values='汇出失败';
                        }
                        return "<font color='" + color + "'>" + values + "</font>";
                    }
                },{
                    width: '120',
                    title: '审核时间',
                    field: 'auditDate',
                    formatter: function (value, row, index) {
                        if(null != value){
                            return getFormatDateByLong(value*1, "yyyy-MM-dd hh:mm:ss");
                        }
                    }
                }<shiro:hasPermission name="trade:setting:moneywithdrawapprove:operator">
                , {
                    field: 'action',
                    title: '操作',
                    width: 200,
                    formatter: function (value, row, index) {
                        var str = '';
                        if (row.approveStatus == '<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING %>') {
                            str += $.formatString('<a href="javascript:void(0)"  class="accountfundcurrent-easyui-linkbutton-dongjie1" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editcashWithdrawAppFun(\'{0}\',\'<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH %>\',\'<%=FundConsts.WALLET_BTC2USD_TYPE%>\');" >审批</a>', row.id);
                        } else {
                            str += $.formatString('<a href="javascript:void(0)" class="accountfundcurrent-easyui-linkbutton-print" data-options="plain:true,iconCls:\'fi-page-export icon-red\'" onclick="window.open(\'${ctx}/fund/cashwithdraw/approvalPrint?id={0}&exchangePairMoney={1}\')" target="_blank">打印</a>', row.id, '<%=FundConsts.WALLET_BTC2USD_TYPE%>');
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
                <shiro:hasPermission name="trade:setting:moneywithdrawapprove:operator">
                $('.accountfundcurrent-easyui-linkbutton-dongjie1').linkbutton({text: '审核'});
                $('.accountfundcurrent-easyui-linkbutton-print').linkbutton({text: '打印'});
                </shiro:hasPermission>
            }
        });
    });

    function editcashWithdrawAppFun(id, status, exchangePairMoney) {
        if (id == undefined) {
            var rows = cashWithdrawAppGrid.datagrid('getSelections');
            id = rows[0].accountFundCurrentName;
        } else {
            cashWithdrawAppGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        <shiro:hasPermission name="trade:setting:moneywithdrawapprove:operator">
        parent.$.modalDialog({
            title: '审核',
            width: 1000,
            height: 500,
            href: '${ctx}/fund/cashwithdraw/approval?id=' + id + "&exchangePairMoney=" + exchangePairMoney,
            buttons: [{
                text: '通过',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = cashWithdrawAppGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editcashWithdrawAppForm');
                    parent.$.modalDialog.handler.find('#editcashWithdrawAppForm #approveStatus').val(status);
                    $("#editcashWithdrawAppForm").form("enableValidation");
                    f.submit();
                }
            }, {
                text: '拒绝',
                handler: function () {
                    parent.$.modalDialog.openner_dataGrid = cashWithdrawAppGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#editcashWithdrawAppForm');
                    parent.$.modalDialog.handler.find('#editcashWithdrawAppForm #approveStatus').val('<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT%>');
                    $("#editcashWithdrawAppForm").form("enableValidation");
                    f.submit();
                }
            }]
        });
        </shiro:hasPermission>
    }
    
    function searchBankcashWithdrawAppFun() {
        cashWithdrawAppGrid.datagrid('load', $.serializeObject($('#searchBankcashWithdrawAppForm')));
    }
    function cleanBankcashWithdrawAppFun() {
        $('#searchBankcashWithdrawAppForm input').val('');
        cashWithdrawAppGrid.datagrid('load', {});
    }

    function exportcashwithdrawdata() {
        $("#searchBankcashWithdrawAppForm").attr("action", "${ctx}/fund/cashwithdraw/export");
        $("#searchBankcashWithdrawAppForm").submit();
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchBankcashWithdrawAppForm">
            <table>
                <tr>
                    <th>证券代码:</th>
                    <td>
                        <input name="stockinfoId" style="width: 75px" class="easyui-combobox"
                               placeholder="请选择证券" data-options="
                                    url: '${ctx}/stock/info/findByStockType?stockType=<%=FundConsts.STOCKTYPE_CASHCOIN%>', method: 'get', valueField:'id',
                                    textField:'stockName', groupField:'group'">
                    </td>
                    <th>用户名或UID:</th>
                    <td><input name="accountName" class="easyui-textbox" placeholder="请输入用户名或UID"/></td>
                    <th>审批状态</th>
                    <td><select class="easyui-combobox" name="approveStatus" style="width:80px;">
                        <option value="">-请选择-</option>
                        <option selected value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING%>">待审核</option>
                        <option value="<%=FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH%>">已审核</option>
                    </select> </td>
                    <th>审核开始时间:</th>
                    <td><input name="timeStart" id="timeStartApp" style="width:120px;"  class="easyui-datebox"  placeholder="请输入交易开始时间"/></td>
                    <th>审核结束时间:</th>
                    <td><input name="timeEnd"  id="timeEndApp"  style="width:120px;" class="easyui-datebox"   placeholder="请输入交易结束时间"/></td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchBankcashWithdrawAppFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanBankcashWithdrawAppFun();">清空</a>

                        <a onclick="exportcashwithdrawdata();" href="javascript:void(0);" class="easyui-linkbutton"
                           data-options="plain:true,iconCls:'fi-save icon-green'">导出已审核记录</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true">
        <table id="cashWithdrawAppGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<form id="export_cashwithdraw" method="get"  style="display: none;"></form>